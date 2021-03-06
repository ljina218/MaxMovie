package maxmovie;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;



public class EventMapping implements ActionListener, ItemListener, KeyListener, MouseListener{

	/* 뷰 패널들의 변수이름
	//로그인뷰
	JPanel 					jp_lv 			= new LoginView(this);
	//마이페이지뷰
	JPanel 					jp_mv 			= new MyPageView(this);
	//무비초이스뷰
	JPanel 					jp_mcv 			= new MovieChoiceView(this);
	//시트초이스뷰
	JPanel 					jp_scv 			= new SeatChoiceView(this);
	//리절트뷰
	JPanel 					jp_rv 			= new ResultView(this);
	 */
	
	/* 마이페이지 뷰의 변수 이름
	 * MemInfoView miv = new MemInfoView();
		MemUpdateView muv = new MemUpdateView();
		TicketHistoryView thv = new TicketHistoryView();
	*/
	
	//회원 아이디 및 닉네임
	//String myid = null;
	//String mynickname = null;
	
	//영화선택 정보 저장
	//영화정보 셋팅할 변수
	TicketingVO tVO = null;
	
	//필요한 주소값 선언부
	MaxMovieView mmv = null;
	MovieController ctrl = new MovieController();
	JoinView jv = null;
	
	//회원가입 목록별 기준체크를 위한 선언부 (회원정보 수정 포함)
	int id = 0;
	int pw = 0;
	int name = 0;
	int nickName = 0;
	int gender = 0;
	int birth = 0;
	int email = 0;
	int email_r = 0;
	
	//인증메일을 위한 선언부
	SendMail sm = null;
	long start_millisecond = 0;//메일 보낸 시간
	long end_millisecond = 0;//인증번호 입력된 시간
	String beforeEmail = null;
	String afterEmail = null;
	
	//생성자
	public EventMapping(MaxMovieView mmv) {
		this.mmv = mmv;
	}
	
	//서버스레드에게 말하기 위한 메소드 
	public void send(String msg) {
		try {
			mmv.oos.writeObject(msg);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
	//시간 체크를 위한 메소드
	public List<Map<String, Object>> timeCheck() {
		//시간 조건이 안맞는 값들 리스트에서 제거
		List<Map<String,Object>> bufList = new Vector<Map<String,Object>>();
		Map<String, Object> map = null;
		//원본인 movieList를 나두고 buffer(bufList) 역할
		for(int i=0; i<mmv.movieList.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("M_TITLE", mmv.movieList.get(i).get("M_TITLE"));
			map.put("M_CERTIF", mmv.movieList.get(i).get("M_CERTIF"));
			map.put("T_LOC", mmv.movieList.get(i).get("T_LOC"));
			map.put("T_NAME", mmv.movieList.get(i).get("T_NAME"));
			map.put("S_DATE", mmv.movieList.get(i).get("S_DATE"));
			map.put("S_TIME", mmv.movieList.get(i).get("S_TIME"));
			map.put("SC_NAME", mmv.movieList.get(i).get("SC_NAME"));
			bufList.add(map);
		}
		//시간체크 for
		for(int i=0; i<bufList.size(); i++) {
			String time = bufList.get(i).get("S_TIME").toString();
			String date = bufList.get(i).get("S_DATE").toString();
			int result = checkTime(date, time);
			if(result==-1) {//예약할 수 없는 시간이라면....
				bufList.remove(i);
			}
		}
		System.out.println("시간 조건 안맞는 값 제거 후 사이즈 : "+bufList.size()); 
		return bufList;
		
	}
	
	
	/**************************************************************************************************
	 * 영화 선택을 위한 메소드들
	 */
	//영화 시간 조건 메소드
		public int checkTime (String date, String time) {//스케줄에 있는 영화 시간 받아서
			int result = 0;
			StringTokenizer st = new StringTokenizer(time, ":");
			String si = st.nextToken();
			String bun = st.nextToken();
			time = si+bun;
			
			String c_Date = setYMD(); //현재날짜
			String c_Time = setHMS(); //현재시간
			//선택한 날짜가 이미 지났니? 
			if(Integer.parseInt(c_Date)
					> Integer.parseInt(date)) {
				//astVO.setMsg("이미 지난 날짜입니다.");
				result = -1;
			}
			//선택한 날짜가 오늘 날짜야?
			else if(c_Date.equals(date)){
				//그러면 시간은 이미 지났니?
				if(Integer.parseInt(c_Time)
						> Integer.parseInt(time)) {
					//astVO.setMsg("이미 지난 시간입니다.");
					result = -1;
				}
				else { //시간 안지났으면
					//날짜 저장 및 시간형식으로 저장
					//astVO.setMsg("확인 완료");
					result = 1;
				}
			}
			//선택한 날짜가 현재날짜보다 미래라면 
			else { 
				//astVO.setMsg("확인 완료");
				result = 1;
			}
			return result;
		}
		
		public String setYMD() {
			Calendar cal = Calendar.getInstance();
			int yyyy = cal.get(Calendar.YEAR);
			int mm = cal.get(Calendar.MONTH)+1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			
			return yyyy
				+(mm < 10 ? "0"+mm:""+mm)
				+(day < 10 ? "0"+day:""+day);
		}//end of setTimer
	/********************************************************************
	 * 상영시간표를 추가하거나 삭제 할 때 현재 시간 정보를 받아온다.
	 * 사용자가 입력한 시간를 비교 할 때 사용한다.  
	 * @return
	 */
		public String setHMS() {
			Calendar cal = Calendar.getInstance();
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int min = cal.get(Calendar.MINUTE);
			
			return (hour < 10 ? "0"+hour:""+hour)
				   +(min < 10 ? "0"+min:""+min);
		}//end of setTimer
	
	//---------------------------------------------------------------------------------------------- 셋팅
	//영화-연령 중복제거
	public List<String> containMovieList(List<Map<String, Object>> userList) {
		List<String> containList = new Vector<String>();
		for(Map<String, Object> userMap:userList) {
			if(!(" ".equals(userMap.get("M_TITLE")))) {
				if(!containList.contains(userMap.get("M_TITLE")+"/"+userMap.get("M_CERTIF"))) {
					containList.add(userMap.get("M_TITLE")+"/"+userMap.get("M_CERTIF"));
					System.out.println(userMap.get("M_TITLE")+"/"+userMap.get("M_CERTIF"));
				}
			}
		}
		System.out.println("영화 중복제거된 리스트: "+containList.size());
		return containList;
	}
	
//	//영화-연령 dtm 셋팅
//	public void movieDtm (List<String> containList) {
//		mmv.jp_mrv.jp_mcv.dtm_movie.setRowCount(0);
//		Vector<Object> movie = null;
//		for(int i=0; i<containList.size(); i++) {
//			movie = new Vector<Object>();
//			StringTokenizer st = new StringTokenizer(containList.get(i),"/");
//			String title = st.nextToken().toString();
//			String age = st.nextToken().toString();
//			if("15".equals(age)) {//이용등급
//				movie.add(mmv.jp_mrv.jp_mcv._15);
//			}
//			else if("19".equals(age)) {
//				movie.add(mmv.jp_mrv.jp_mcv._19);
//			}
//			else if("0".equals(age)) {
//				movie.add(mmv.jp_mrv.jp_mcv._0);	
//			}
//			else if("12".equals(age)) {
//				movie.add(mmv.jp_mrv.jp_mcv._12);	
//			}
//			movie.add(title);
//			mmv.jp_mrv.jp_mcv.dtm_movie.addRow(movie);
//		}
//	}
	
	//영화-연령 dtm 셋팅
	public List<String> movieDtm (List<String> containList) {
		Vector<Object> movie = null;
		//사용자가 선택하지 않은 리스트 뽑기
		List<String> noList = new Vector<String>();
		if(mmv.jp_mrv.jp_mcv.dtm_movie.getRowCount()>0) {
			for(int j=0; j<mmv.jp_mrv.jp_mcv.dtm_movie.getRowCount(); j++) {
				Object ob = mmv.jp_mrv.jp_mcv.dtm_movie.getValueAt(j, 0);
				ImageIcon ii = (ImageIcon) ob;
				String age = ii.getDescription();
				String imsi = mmv.jp_mrv.jp_mcv.dtm_movie.getValueAt(j, 1).toString()+"/"+age;
				if(!containList.contains(imsi)) {
					noList.add(imsi);
					System.out.println("//선택하지 않음 영화: "+imsi);
				}
			}
		}
		//dtm을 초기화해서
		mmv.jp_mrv.jp_mcv.dtm_movie.setRowCount(0);
		//사용자가 선택한 리스트 꽂아주고...
		for(int i=0; i<containList.size(); i++) {
			movie = new Vector<Object>();
			System.out.println("//선택한 영화: "+containList.get(i));
			StringTokenizer st = new StringTokenizer(containList.get(i),"/");
			String title = st.nextToken().toString();
			String age = st.nextToken().toString();
			if(!" ".equals(title)||!" ".equals(age)) {//" "인 아이들 지워주기....
				if("15".equals(age)) {//이용등급
					movie.add(mmv.jp_mrv.jp_mcv._15);
				}
				else if("19".equals(age)) {
					movie.add(mmv.jp_mrv.jp_mcv._19);
				}
				else if("0".equals(age)) {
					movie.add(mmv.jp_mrv.jp_mcv._0);	
				}
				else if("12".equals(age)) {
					movie.add(mmv.jp_mrv.jp_mcv._12);	
				}
				movie.add(title);
				mmv.jp_mrv.jp_mcv.dtm_movie.addRow(movie);				
			}
		}
		//사용자가 선택하지 않은 리스트 꽂아주고...
		if(noList.size()>0) {
			for(int i=0; i<noList.size(); i++) {
				movie = new Vector<Object>();
				StringTokenizer st = new StringTokenizer(noList.get(i),"/");
				String title = st.nextToken().toString();
				String age = st.nextToken().toString();
				if("15".equals(age)) {//이용등급
					movie.add(mmv.jp_mrv.jp_mcv._15);
				}
				else if("19".equals(age)) {
					movie.add(mmv.jp_mrv.jp_mcv._19);
				}
				else if("0".equals(age)) {
					movie.add(mmv.jp_mrv.jp_mcv._0);	
				}
				else if("12".equals(age)) {
					movie.add(mmv.jp_mrv.jp_mcv._12);	
				}
				movie.add(title);
				mmv.jp_mrv.jp_mcv.dtm_movie.addRow(movie);				
				//;
			}
		}
		return noList;
	}
	
	//지역
	public List<String> containLocList(List<Map<String, Object>> userList) {
		List<String> containList = new Vector<String>();
		for(Map<String, Object> userMap:userList) {
			if(!containList.contains(userMap.get("T_LOC"))) {
				containList.add(userMap.get("T_LOC").toString());
			}
		}
		System.out.println("지역 중복제거된 리스트: "+containList.size());
		return containList;
	}
	
	//지역 dtm 셋팅
	public void locDtm(List<String> containList) {
		mmv.jp_mrv.jp_mcv.dtm_local.setRowCount(0);
		Vector<Object> loc = null;
		for(int i=0; i<containList.size(); i++) {
			loc = new Vector<Object>();
			loc.add(containList.get(i));
			mmv.jp_mrv.jp_mcv.dtm_local.addRow(loc);
		}
	}
	
	//지역-지점
	public List<String> containTheaterList(List<Map<String, Object>> userList) {
		List<String> containList = new Vector<String>();
		for(Map<String, Object> userMap:userList) {
			if(!(" ".equals(userMap.get("T_LOC")))) {
				if(!containList.contains(userMap.get("T_LOC")+"/"+userMap.get("T_NAME"))) {
					containList.add(userMap.get("T_LOC")+"/"+userMap.get("T_NAME"));
				}
			}
		}
		System.out.println("지점 중복제거된 리스트: "+containList.size());
		return containList;
	}
	
//	//지점 dtm 셋팅
//	public void theaterDtm(List<String> containList, String sel_loc) {
//		mmv.jp_mrv.jp_mcv.dtm_theater.setRowCount(0);
//		Vector<Object> theater = null;
//		for(int i=0; i<containList.size(); i++) {
//			StringTokenizer st = new StringTokenizer(containList.get(i),"/");
//			String loc = st.nextToken().toString();
//			String th = st.nextToken().toString();
//			if(sel_loc.equals(loc)) {
//				theater = new Vector<Object>();
//				theater.add(th);
//				mmv.jp_mrv.jp_mcv.dtm_theater.addRow(theater);
//			}
//		}
//	}
	
	//지점 dtm 셋팅
	public List<String> theaterDtm(List<String> containList, String sel_loc) {
		//해당 지역의 모든 지점들을 dtm에 꽂아두고....
		mmv.jp_mrv.jp_mcv.dtm_theater.setRowCount(0);
		Vector<Object> theater = null;
		List<Map<String,Object>> bufList = timeCheck();
		List<String> all_list = containTheaterList(bufList) ;
		for(int i=0; i<all_list.size(); i++) {
			StringTokenizer st = new StringTokenizer(all_list.get(i),"/");
			String loc = st.nextToken().toString();
			String th = st.nextToken().toString();
			if(sel_loc.equals(loc)) {
				theater = new Vector<Object>();
				theater.add(th);
				mmv.jp_mrv.jp_mcv.dtm_theater.addRow(theater);
			}
		}
		//사용자가 선택하지 않은 리스트 뽑기
		List<String> noList = new Vector<String>();
		if(mmv.jp_mrv.jp_mcv.dtm_theater.getRowCount()>0) {
			for(int i=0; i<mmv.jp_mrv.jp_mcv.dtm_theater.getRowCount(); i++) {
				String imsi = sel_loc+"/"+mmv.jp_mrv.jp_mcv.dtm_theater.getValueAt(i, 0);
				if(!containList.contains(imsi)) {
					noList.add(imsi);
				}
			}
		}
		//dtm을 초기화해주고...
		mmv.jp_mrv.jp_mcv.dtm_theater.setRowCount(0);
		//사용자가 선택한 리스트 꽂기...
		for(int i=0; i<containList.size(); i++) {
			StringTokenizer st = new StringTokenizer(containList.get(i),"/");
			String loc = st.nextToken().toString();
			String th = st.nextToken().toString();
			if(!" ".equals(loc)||!" ".equals(th)) {//" "인 아이들 지워주기....
				if(sel_loc.equals(loc)) {
					theater = new Vector<Object>();
					theater.add(th);
					mmv.jp_mrv.jp_mcv.dtm_theater.addRow(theater);
				}				
			}
		}
		//사용자가 선택하지 않은 리스트 꽂기...
		if(noList.size()>0) {
			for(int i=0; i<noList.size(); i++) {
				theater = new Vector<Object>();
				StringTokenizer st = new StringTokenizer(noList.get(i),"/");
				String loc = st.nextToken().toString();
				String th = st.nextToken().toString();
				if(sel_loc.equals(loc)) {
					theater = new Vector<Object>();
					theater.add(th);
					mmv.jp_mrv.jp_mcv.dtm_theater.addRow(theater);
				}
			}
		}
		return noList;
	}
	
	//날짜
	public List<String> containDateList(List<Map<String, Object>> userList) {
		List<String> containList = new Vector<String>();
		List<String> containString = new Vector<String>();
		for(Map<String, Object> userMap:userList) {
			if(!(" ".equals(userMap.get("S_DATE")))) {
				if(!containList.contains(userMap.get("S_DATE"))) {
					containList.add(userMap.get("S_DATE").toString());
				}
			}
		}
		for(String list: containList) {
			containString.add(list.substring(6)+"일");
		}
		System.out.println("날짜 중복제거된 리스트: "+containList.size());
		for(String list: containString) {
			System.out.println("contain 안에서 List : "+list);
		}
		return containString;
	}
	
//	//날짜 dtm 셋팅2
//	public void dateDtm2(List<String> containList) {
//		mmv.jp_mrv.jp_mcv.dtm_date.setRowCount(0);
//		Vector<Object> date = null;
//		for(int i=0; i<containList.size(); i++) {
//			date = new Vector<Object>();
//			date.add(containList.get(i));
//			mmv.jp_mrv.jp_mcv.dtm_date.addRow(date);
//		}
//	}
	
//	//날짜 dtm 셋팅2
//	public List<String> dateDtm2(List<String> containList) {
//		//사용자가 선택하지 않은 리스트 뽑기
//		List<String> noList = new Vector<String>();
//		if(mmv.jp_mrv.jp_mcv.dtm_date.getRowCount()>0) {
//			for(int i=0; i<mmv.jp_mrv.jp_mcv.dtm_date.getRowCount(); i++) {
//				String imsi = mmv.jp_mrv.jp_mcv.dtm_date.getValueAt(i, 0).toString();
//				if(!containList.contains(imsi)) {
//					noList.add(imsi);
//				}
//			}
//		}
//		//dtm 초기화시키고..
//		//mmv.jp_mrv.jp_mcv.dtm_date.setRowCount(0);
//		//사용자가 선택한 리스트 꽂기...
//		Vector<Object> date = null;
//		for(int i=0; i<containList.size(); i++) {
//			if(!" ".equals(containList.get(i))) {//" "인 아이들 지워주기....
//				date = new Vector<Object>();
//				date.add(containList.get(i));
//				//mmv.jp_mrv.jp_mcv.dtm_date.addRow(date);				
//			}
//		}
//		//사용자가 선택하지 않은 리스트 꽂기...
//		if(noList.size()>0) {
//			for(int i=0; i<noList.size(); i++) {
//				date = new Vector<Object>();
//				date.add(noList.get(i));
//				//mmv.jp_mrv.jp_mcv.dtm_date.addRow(date);
//			}
//		}
//		return noList;
//	}
	
	//nolist
	public List<String> dateNoList(List<String> containList) {
		//사용자가 선택하지 않은 리스트 뽑기
		List<String> noList = new Vector<String>();
		if(mmv.jp_mrv.jp_mcv.dtm_date.getRowCount()>0) {
			for(int i=0; i<mmv.jp_mrv.jp_mcv.dtm_date.getRowCount(); i++) {
				String imsi = mmv.jp_mrv.jp_mcv.dtm_date.getValueAt(i, 0).toString();
				if(!containList.contains(imsi)) {
					int result =0;
					String imsis[] = imsi.split("");
					for(int j=0; j<imsis.length; j++) {
						if("년".equals(imsis[j])||"월".equals(imsis[j])) {
							result =1;
						}
					}
					if(result==0) {
						noList.add(imsi);						
					}
				}
			}
		}
		System.out.println("================================nolist사이즈: "+noList.size());
		for(String k: noList){
			System.out.println("nolist: "+k);
		}
		return noList;
	}
	
	//날짜 dtm 셋팅
	public void dateDtm () {
		mmv.jp_mrv.jp_mcv.dtm_date.setRowCount(0);
		Calendar today = Calendar.getInstance();//현재 날짜에서
		String before_year ="";
		String before_month = "";
		Vector<String> nowdate = null;
		for(int i=0; i<3; i++) {//20일 간 정보
			int year = today.get(Calendar.YEAR);
			String after_year = Integer.toString(year);
			if(!before_year.equals(after_year)) {
				nowdate = new Vector<String>();
				nowdate.add("******* "+year+"년 *******");
				mmv.jp_mrv.jp_mcv.dtm_date.addRow(nowdate);
				before_year = after_year;
			}
			int month = today.get(Calendar.MONTH)+1;
			String after_month = Integer.toString(month);
			if(!before_month.equals(after_month)) {
				nowdate = new Vector<String>();
				nowdate.add("===== "+after_month+"월 =====");
				mmv.jp_mrv.jp_mcv.dtm_date.addRow(nowdate);
				before_month = after_month;
			}
			String day = Integer.toString(today.get(Calendar.DAY_OF_MONTH));
			nowdate = new Vector<String>();
			nowdate.add(day+"일");
			today.add(Calendar.DAY_OF_MONTH, +1);
			mmv.jp_mrv.jp_mcv.dtm_date.addRow(nowdate);
		}
	}
	
	//관이름
	public List<String> containScrNameList(List<Map<String, Object>> userList) {
		List<String> containList = new Vector<String>();
		for(Map<String, Object> userMap:userList) {
			if(!containList.contains(userMap.get("SC_NAME")+"/"+userMap.get("S_TIME"))) {
				containList.add(userMap.get("SC_NAME")+"/"+userMap.get("S_TIME"));
				System.out.println("관: "+userMap.get("SC_NAME")+"/"+userMap.get("S_TIME"));
			}
		}
		System.out.println("@@@@@@@관 중복제거된 리스트: "+containList.size());
		return containList;
	}
	
	//관 dtm 셋팅
	public void scrDtm(List<String> containList) {
		mmv.jp_mrv.jp_mcv.dtm_time.setRowCount(0);
		Vector<Object> screen = null;
		for(int i=0; i<containList.size(); i++) {
			System.out.println("관: "+containList.get(i));
			StringTokenizer st = new StringTokenizer(containList.get(i),"/");
			String scr = st.nextToken().toString();
			String time = st.nextToken().toString();
			if(!" ".equals(scr)||!" ".equals(time)) {
				screen = new Vector<Object>();
				screen.add(scr);
				screen.add(time);
				mmv.jp_mrv.jp_mcv.dtm_time.addRow(screen);				
			}
		}
	}
	
	//---------------------------------------------------------------------------------------------- 선택
	//영화, 지점, 날짜 선택 메소드
	public List<Map<String, Object>> choiceAll(TicketingVO tVO) {
		List<Map<String, Object>> user_list = new Vector<Map<String,Object>>();
		Map<String, Object> umap = null;
		//상영시간표에 있는 무비리스트 
		for(int i=0; i<mmv.movieList.size(); i++) {
			String m_title = mmv.movieList.get(i).get("M_TITLE").toString();
			String m_age = mmv.movieList.get(i).get("M_CERTIF").toString();
			String m_loc = mmv.movieList.get(i).get("T_LOC").toString();
			String m_theater = mmv.movieList.get(i).get("T_NAME").toString();
			String m_date = mmv.movieList.get(i).get("S_DATE").toString();
			String m_time = mmv.movieList.get(i).get("S_TIME").toString();
			String m_screen = mmv.movieList.get(i).get("SC_NAME").toString();
			
			int result = checkTime(m_date, m_time);
			if(result!=-1) {//상영시간이 30분 전이고....
				if(tVO.getMovie_name().equals(m_title)
			     &&tVO.getLoc().equals(m_loc)
			     &&tVO.getTheater().equals(m_theater)
			     &&tVO.getMovie_date().equals(m_date)){
					umap = new HashMap<String, Object>();
					umap.put("S_TIME", m_time);
					umap.put("SC_NAME", m_screen);
					user_list.add(umap);
				}
			}
		}//for문 끝
		return user_list;
	}
	
	//영화선택 -> 지점, 날짜 refresh
	public List<Map<String, Object>> choiceMovie(List<Map<String, Object>> bufList, TicketingVO tVO) {
		List<Map<String, Object>> userList = new Vector<Map<String,Object>>();
		Map<String, Object> userMap = null;
		Map<String, Object> movieMap = null;
		//72개의 상영시간표 중에서 
		for (int i = 0; i < bufList.size(); i++) {
			movieMap = bufList.get(i);
			//영화가 선택됐을 때 지점과 날짜가 선택되었니?
			if(tVO.getTheater()!=null && tVO.getMovie_date()!=null) {
				//좌석정보까지 리프레쉬 
				userList = choiceAll(tVO);
			}
			//영화가 선택됐을 때 지점이 선택되었니?
			else if(tVO.getTheater()!=null) {
				if(movieMap.get("M_TITLE").equals(tVO.getMovie_name())
						||movieMap.get("T_NAME").equals(tVO.getTheater())) {
					userMap = new HashMap<String, Object>();
					userMap.put("M_TITLE",movieMap.get("M_TITLE"));
					userMap.put("M_CERTIF",movieMap.get("M_CERTIF"));
					userMap.put("T_LOC",movieMap.get( "T_LOC"));
					userMap.put("T_NAME", movieMap.get("T_NAME"));
					userMap.put("S_DATE", " ");
					//or조건일 때는 날짜는 null;
					if(movieMap.get("M_TITLE").equals(tVO.getMovie_name())
							&&movieMap.get("T_NAME").equals(tVO.getTheater())) {
						userMap.put("S_DATE", movieMap.get("S_DATE"));
					}
					userList.add(userMap);
				}
			}
			//영화가 선택됐을 때 날짜가 선택되었니?
			else if(tVO.getMovie_date()!=null) {
				if(movieMap.get("M_TITLE").equals(tVO.getMovie_name())
						||movieMap.get("S_DATE").equals(tVO.getMovie_date())) {
					userMap = new HashMap<String, Object>();
					userMap.put("M_TITLE",movieMap.get("M_TITLE"));
					userMap.put("M_CERTIF",movieMap.get("M_CERTIF")); 
					userMap.put("S_DATE", movieMap.get("S_DATE"));
					userMap.put("T_LOC", " ");
					userMap.put("T_NAME", " ");
					//or조건일 때는 날짜는 null;
					if(movieMap.get("M_TITLE").equals(tVO.getMovie_name())
							&&movieMap.get("S_DATE").equals(tVO.getMovie_date())) {
						userMap.put("T_LOC",movieMap.get( "T_LOC"));
						userMap.put("T_NAME", movieMap.get("T_NAME"));
					}
					userList.add(userMap);
				}
			}
			//영화가 선택됐을 때 선택된게 아무것도 없니?
			else {
				if(movieMap.get("M_TITLE").equals(tVO.getMovie_name())) {
					userMap = new HashMap<String, Object>();
					userMap.put("M_TITLE", " ");
					userMap.put("M_CERTIF", " ");
					userMap.put("T_LOC",movieMap.get( "T_LOC"));
					userMap.put("T_NAME", movieMap.get("T_NAME"));
					userMap.put("S_DATE", movieMap.get("S_DATE"));
					userList.add(userMap);
				}
			}
		}
		return userList;
	}
	
	//지점선택 -> 영화, 날짜 refresh
	public List<Map<String, Object>> choiceTheater(List<Map<String, Object>> bufList, TicketingVO tVO) {
		List<Map<String, Object>> userList = new Vector<Map<String,Object>>();
		Map<String, Object> userMap = null;
		Map<String, Object> movieMap = null;
		//72개의 상영시간표 중에서 
		for (int i = 0; i < bufList.size(); i++) {
			movieMap = bufList.get(i);
			//지점이 선택됐을 때 영화와 날짜가 선택되었니?
			if(tVO.getMovie_name()!=null && tVO.getMovie_date()!=null) {
				//좌석정보까지 리프레쉬 
				userList = choiceAll(tVO);
			}
			//지점이 선택됐을 때 영화가 선택되었니?
			else if(tVO.getMovie_name()!=null) {
				if(movieMap.get("T_NAME").equals(tVO.getTheater())
				 ||movieMap.get("M_TITLE").equals(tVO.getMovie_name())) {
					userMap = new HashMap<String, Object>();
					userMap.put("M_TITLE",movieMap.get( "M_TITLE"));
					userMap.put("M_CERTIF",movieMap.get( "M_CERTIF"));
					userMap.put("T_LOC",movieMap.get( "T_LOC"));
					userMap.put("T_NAME", movieMap.get("T_NAME"));
					userMap.put("S_DATE", " ");
					//or조건일 때는 날짜는 null;
					if(movieMap.get("T_NAME").equals(tVO.getTheater())
					 &&movieMap.get("M_TITLE").equals(tVO.getMovie_name())) {
						userMap.put("S_DATE", movieMap.get("S_DATE"));
					}
					userList.add(userMap);
				}
			}
			//지점이 선택됐을 때 날짜가 선택되었니?
			else if(tVO.getMovie_date()!=null) {
				if(movieMap.get("T_NAME").equals(tVO.getTheater())
						||movieMap.get("S_DATE").equals(tVO.getMovie_date())) {
					userMap = new HashMap<String, Object>();
					userMap.put("M_TITLE", " ");
					userMap.put("M_CERTIF", " "); 
					userMap.put("S_DATE", movieMap.get("S_DATE"));
					userMap.put("T_LOC",movieMap.get( "T_LOC"));
					userMap.put("T_NAME", movieMap.get("T_NAME"));
					//or조건일 때는 날짜는 null;
					if(movieMap.get("T_NAME").equals(tVO.getTheater())
							&&movieMap.get("S_DATE").equals(tVO.getMovie_date())) {
						userMap.put("M_TITLE",movieMap.get( "M_TITLE"));
						userMap.put("M_CERTIF",movieMap.get( "M_CERTIF")); 
					}
					userList.add(userMap);
				}
			}
			//지점이 선택됐을 때 선택된게 아무것도 없니?
			else {
				if(movieMap.get("T_NAME").equals(tVO.getTheater())) {
					userMap = new HashMap<String, Object>();
					userMap.put("M_TITLE", movieMap.get( "M_TITLE"));
					userMap.put("M_CERTIF", movieMap.get( "M_CERTIF"));
					userMap.put("T_LOC",  " ");
					userMap.put("T_NAME", " ");
					userMap.put("S_DATE", movieMap.get("S_DATE"));
					userList.add(userMap);
				}
			}
		}
		return userList;
	}
	
	//날짜선택 -> 영화, 지점 refresh
	public List<Map<String, Object>> choiceDate(List<Map<String, Object>> bufList, TicketingVO tVO) {
		List<Map<String, Object>> userList = new Vector<Map<String,Object>>();
		Map<String, Object> userMap = null;
		Map<String, Object> movieMap = null;
		//72개의 상영시간표 중에서 
		for (int i = 0; i < bufList.size(); i++) {
			movieMap = bufList.get(i);
			//날짜가 선택됐을 때 영화와 지점이 선택되었니?
			if(tVO.getMovie_name()!=null && tVO.getTheater()!=null) {
				//좌석정보까지 리프레쉬 
				userList = choiceAll(tVO);
			}
			
			//날짜가 선택됐을 때 영화가 선택되었니?
			else if(tVO.getMovie_name()!=null) {
				if(movieMap.get("S_DATE").equals(tVO.getMovie_date())
				 ||movieMap.get("M_TITLE").equals(tVO.getMovie_name())) {
					userMap = new HashMap<String, Object>();
					userMap.put("M_TITLE",movieMap.get( "M_TITLE"));
					userMap.put("M_CERTIF",movieMap.get( "M_CERTIF"));
					userMap.put("T_LOC", " ");
					userMap.put("T_NAME", " ");
					userMap.put("S_DATE", movieMap.get("S_DATE"));
					//or조건일 때는 날짜는 null;
					if(movieMap.get("S_DATE").equals(tVO.getMovie_date())
					 &&movieMap.get("M_TITLE").equals(tVO.getMovie_name())) {
						userMap.put("T_LOC",movieMap.get( "T_LOC"));
						userMap.put("T_NAME", movieMap.get("T_NAME"));
					}
					userList.add(userMap);
				}
			}
			//날짜가 선택됐을 때 지점이 선택되었니?
			else if(tVO.getTheater()!=null) {
				if(movieMap.get("S_DATE").equals(tVO.getMovie_date())
						 ||movieMap.get("T_NAME").equals(tVO.getTheater())) {
					userMap = new HashMap<String, Object>();
					userMap.put("M_TITLE", " ");
					userMap.put("M_CERTIF", " "); 
					userMap.put("S_DATE", movieMap.get("S_DATE"));
					userMap.put("T_LOC", movieMap.get( "T_LOC"));
					userMap.put("T_NAME", movieMap.get("T_NAME"));
					//or조건일 때는 날짜는 null;
					if(movieMap.get("S_DATE").equals(tVO.getMovie_date())
							&&movieMap.get("T_NAME").equals(tVO.getTheater())) {
						userMap.put("M_TITLE",movieMap.get( "M_TITLE"));
						userMap.put("M_CERTIF",movieMap.get( "M_CERTIF")); 
					}
					userList.add(userMap);
				}
			}
			//날짜가 선택됐을 때 선택된게 아무것도 없니?
			else {
				if(movieMap.get("S_DATE").equals(tVO.getMovie_date())) {
					userMap = new HashMap<String, Object>();
					userMap.put("M_TITLE", movieMap.get( "M_TITLE"));
					userMap.put("M_CERTIF", movieMap.get( "M_CERTIF"));
					userMap.put("T_LOC",  movieMap.get( "T_LOC"));
					userMap.put("T_NAME", movieMap.get("T_NAME"));
					userMap.put("S_DATE", " ");
					userList.add(userMap);
				}
			}
		}
		return userList;
	}
	
		
	/**************************************************************************************************
	 * 회원가입 기준체크를 위한 메소드들.....
	 */
	//기준 모두 초기화 메소드
	public void refreshCheck() {
		this.id = 0;
		this.pw = 0;
		this.name = 0;
		this.nickName = 0;
		this.gender = 0;
		this.birth = 0;
		this.email = 0;
		this.email_r = 0;
		this.start_millisecond = 0;
		this.end_millisecond = 0;
		this.beforeEmail = null;
		this.afterEmail = null;
	}
	
	//아이디 기준체크 메소드
	public int checkId (String inputId) {
		int sum = 0;
		if(7<=inputId.length()&&inputId.length()<=12) {//문자 길이가 7~12니?
			char[] ids = inputId.toCharArray();//스트링을 쪼개서
			int[] result = new int[inputId.length()];
			for(int i=0; i<inputId.length(); i++) {
				int cha = (int)ids[i];
				if(33<=cha&&cha<=126) {//영문, 숫자, 특수문자니?
					if((33<=cha&&cha<=47)||(58<=cha&&cha<=64)||
							(91<=cha&&cha<=96)||(123<=cha&&cha<=126)) {//특수문자니? 문제군
						result[i]=1;
					}else {//영문, 숫자니? 좋아
						result[i]=0;
					}
				}else {//공백, 한글이니? 문제야
					result[i]=1;
				}
			}
			for(int a:result) {
				sum+= a;
			}
		}else {//문자길이가 기준에 맞지 않군
			sum = inputId.length();
		}
		return sum;
	}
	
	//패스워드 스트링으로 만들어주는 메소드
	public String pwToString (char[] password) {
		StringBuilder sb = new StringBuilder();
		for(char cha: password) {
			Character.toString(cha);
			sb.append(cha);
			System.out.println(cha);
		}
		return sb.toString();
	}
	
	//이름, 닉네임 기준체크 메소드
	public int checkNames(String inputnames, int s, int e) {
		System.out.println(inputnames);
		int sum =0;
		if(s<=inputnames.length()&&inputnames.length()<=e) {//문자길이 맞니?
			char[] names = inputnames.toCharArray();//스트링을 쪼개서
			int[] result = new int[inputnames.length()];
			for(int i=0; i<inputnames.length(); i++) {
				int cha = (int)names[i];
				if((33<=cha&&cha<=47)||(58<=cha&&cha<=64)||
							(91<=cha&&cha<=96)||(123<=cha&&cha<=126)) {//특수문자니? 문제군
					result[i]=1;
				}else {
					result[i]=0;
				}
			}
			for(int a:result) {
				sum+= a;
				System.out.println(a);
			}
		}else {
			sum = inputnames.length();
		}
		System.out.println("sum: "+sum);
		return sum;
	}
	
	//비번 기준체크 메소드
	public int checkPw(String inputpw, int s, int e) {//비밀번호는 알아서 영문으로 입력됩니다
		int sum =0;
		if(s<=inputpw.length()&&inputpw.length()<=e) {//문자길이 맞니?
			char[] names = inputpw.toCharArray();//스트링을 쪼개서
			int[] result = new int[inputpw.length()];
			for(int i=0; i<inputpw.length(); i++) {
				int cha = (int)names[i];
				if(33<=cha&&cha<=126) {//영문, 숫자니?				
					result[i]=0;
				}else {//한글, 공백이니? 문제군
					result[i]=1;
				}
			}
			for(int a:result) {
				sum+= a;
			}
		}else {
			sum = inputpw.length();
		}
		return sum;
	}
	
	//생년월일 기준체크 메소드
	/*
	 * jl_year_warning.setText(" 생년을 선택하여주세요");
		jl_month_warning.setText(" 생월을 선택하여주세요");
		jl_day_warning.setText(" 생일을 선택하여주세요");
	 */
	public void birth_checking() {
		if("년도".equals(jv.choiceYear)) {
			jv.jl_year_warning.setVisible(true);
			jv.jl_month_warning.setVisible(false);
			jv.jl_day_warning.setVisible(false);
			birth = 0;
		}else {
			if("월".equals(jv.choiceMonth)) {
				jv.jl_year_warning.setVisible(false);
				jv.jl_month_warning.setVisible(true);
				jv.jl_day_warning.setVisible(false);
				birth = 0;
			}else{
				if("일".equals(jv.choiceDay)) {
					jv.jl_year_warning.setVisible(false);
					jv.jl_month_warning.setVisible(false);
					jv.jl_day_warning.setVisible(true);
					birth = 0;
				}else {
					jv.jl_year_warning.setVisible(false);
					jv.jl_month_warning.setVisible(false);
					jv.jl_day_warning.setVisible(false);
					birth = 1;
				}
			}
		}
	}
	
	//이메일 기준 체크 메소드
	/*
	 * 로컬부분:대문자A~Z,소문자a~z,숫자0~9,특수문자!#$%&'*+-/=?^_{|}~,(.은사용가능하나첫번째, 마지막아니어야됨) 
	 도메인부분 : 대문자 A~Z, 소문자 a~z, 숫자 0~9, 하이픈-(첫번째 또는 마지막 아니어야됨)
	 정의) 로컬부분 + @ + 도메인부분 (상세하게 하면 매우다르지만 일반적으로 이정도) 
	 */
	public static int checkEmail(String inputemail) {
		String local = null;
		String domain = null;
		StringTokenizer st = null;
		int sum = 0;
		int sum2 = 0;
		if(inputemail.length()==0) {//이메일을 적지 않았다면,
			sum = 1;
			sum2 = 1;
		}else {//이메일을 적었다면,
			//먼저 @ 갯수를 확인할게
			char[] emails = inputemail.toCharArray();
			int [] check = new int[inputemail.length()];
			int checking = 0;
			for(int i =0; i<inputemail.length(); i++) {
				int email = emails[i];
				if(email==64) {
					checking+= 1;
				}
			}
			if(checking>1) {//@가 여러개니?
				sum = 1;
				sum2 = 1;
			}else {//@가 하나니?
				st = new StringTokenizer(inputemail,"@");//그럼 이제 로컬과 도메인 검사 할게
				local = st.nextToken();
				if(st.hasMoreTokens()) {//@가 있다면,
					domain = st.nextToken();
					if(domain!=null) {
						//로컬 검사
						char[] locals = local.toCharArray();
						int [] result = new int[local.length()];
						if(locals[0]==46||locals[local.length()-1]==46) {//.이 첫번째 혹은 마지막이니?
							sum = local.length();
						}else {//아니라면 다음 기준 간다
							for(int i=0; i<local.length(); i++) {
								int loc = (int)locals[i];
								if(33<=loc&&loc<=126){//영문, 숫자, 특수문자 니?
									if(loc==34||loc==40||loc==41||
											loc==44||loc==46||loc==58||loc==59||
											loc==60||loc==62||loc==64||(91<=loc&&loc<=96)) {
										result[i] = 1;//문제군
									}else {//특수문자 !#$%&'*+-/=?^_{|}~. 니?
										result[i] = 0;//좋아
									}
								}else {//아니니?
									result[i] = 1;//문제군
								}
							}
							for(int a:result) {
								sum+= a;
							}
						}
						//도메인 검사
						char[] domains = domain.toCharArray();
						int [] result2 = new int[domain.length()];
						if(domains[0]==45||domains[domain.length()-1]==45) {//-이 첫번째 혹은 마지막이니?
							sum2 = domain.length();
						}else {//아니라면 다음 기준 간다
							for(int i=0; i<domain.length(); i++) {
								int dom = (int)domains[i];
								if((48<=dom&&dom<=57)||(65<=dom&&dom<=90)||(97<=dom&&dom<=122)||dom==45||dom==46) {//영문, 숫자, -니?
									result2[i] = 0;//좋아
								}else {//아니니?
									result2[i] = 1;//문제군
								}
							}
							for(int a:result2) {
								sum2+= a;
							}
						}
					}else {//domain==null 이라면,
						sum = local.length();
						sum2 = 1;
					}
				}else {//@이 하나도 없니?
					sum = local.length();
					sum2 = 1;
				}
			}
		}
		return sum+sum2;
	}
	
	/**************************************************************************************************
	 * ActionListener(로그인,회원가입,회원가입-이메일,마이페이지,화면전환)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		//로그인 -----------------------------------------------------------------------------------------
		if(obj==mmv.jp_lv.jbt_login) {//로그인이 하고 싶어요
			System.out.println("클릭");
			mmv.mem_id = mmv.jp_lv.jtf_id.getText(); //아이디 전역변수 저장
			String login_id = mmv.jp_lv.jtf_id.getText();
			String login_pw = pwToString(mmv.jp_lv.jpf_pw.getPassword());
			String login_msg = MovieProtocol.LOGIN+"#"+login_id+"#"+login_pw;
			send(login_msg);//아이디,비번 검사해주세요
		}
		
		//회원가입 ----------------------------------------------------------------------------------------
		else if(obj==mmv.jp_lv.jbt_join) {//회원가입하고 싶니?
			jv = new JoinView(this);//화면띄워줄게
		}
		else if(jv!=null&&obj==jv.jbt_joingo) {//드디어 회원가입 버튼을 눌렀군
			//회원가입을 위한 기준들 마지막 체크 
			afterEmail = jv.jtf_email.getText();
			if(afterEmail!=null) {
				if(afterEmail.equals(beforeEmail)) {
					email_r = 1;								
				}
			}
			int sum = id + pw + name + nickName + birth + email + email_r + gender;
			if(sum==8) {//모든 기준 통과시, 모든 값들 가져와서
				String name = jv.jtf_name.getText();
				String id = jv.jtf_id.getText();
				String pw = pwToString(jv.jpf_pw.getPassword());
				String email = jv.jtf_email.getText();
				String nickName = jv.jtf_nick.getText();
				if(jv.choiceMonth.length()==1) {//생년월일 - "월,일" 1자리면 앞에 0 붙여주기
					System.out.println("월 "+jv.choiceMonth);
					jv.choiceMonth = "0"+jv.choiceMonth;
				}
				if(jv.choiceDay.length()==1) {
					System.out.println("일"+jv.choiceDay);
					jv.choiceDay = "0"+jv.choiceDay;
				}
				String birth = jv.choiceYear+""+jv.choiceMonth+""+jv.choiceDay;//[형식]19960218
				String gender = jv.jcb_genderChoice;
				//서버스레드로 메세지 전송
				String join_msg = MovieProtocol.JOIN+"#"+name+"#"+id+"#"+pw+"#"+email+"#"+nickName+"#"+birth+"#"+gender;
				send(join_msg);//db에 넣어주세요
				System.out.println(join_msg);
			}else {
				JOptionPane.showMessageDialog(jv, "입력한 정보가 부적합합니다. 다시 확인해주세요.");
			}
		}
		else if(jv!=null&&obj==jv.jbt_back) {//회원가입 화면을 나가고 싶니?
			jv.dispose();//okay bye...
		}
		//아이디 -----------------------------------------------------------------------------------------
		/*
		 * jl_id_warning.setText(" 동일한 아이디가 존재합니다.");
		jl_id_success.setText(" 사용 가능한 아이디입니다.");
		jl_id_warning2.setText(" 7~12자이어야하고 특수문자는 입력할 수 없습니다.");
		 */
		else if(jv!=null&&obj==jv.jbt_id_check) {//아이디 중복 체크하고 싶니?
			jv.jl_id_warning.setVisible(false);
			jv.jl_id_warning2.setVisible(false);
			jv.jl_id_success.setVisible(false);
			//일단 기준 체크좀 할게
			String inputId = jv.jtf_id.getText();
			int result = checkId(inputId);
			if(result>0) {//기준이 안맞네
				jv.jl_id_warning2.setVisible(true);
				id = 0;
			}else {//기준통과했다면, 이제 중복 체크 해줄게
				String chektid_msg = MovieProtocol.CHECK_ID+"#"+inputId;
				send(chektid_msg);//중복체크해주세요
			}
		}
		
		//이메일 -----------------------------------------------------------------------------------------
		/*
		 * jl_email_warning.setText(" 이메일주소 형식에 맞지 않습니다.");
		jl_email_warning2.setText(" 인증번호 입력시간은 2분입니다.");
		jl_email_r_success.setText(" 인증성공");
		jl_email_r_warning.setText(" 인증번호가 일치하지 않습니다.");
		jl_email_r_warning2.setText(" 입력시간이 초과했습니다.");
		 */
		else if(jv!=null&&obj==jv.jbt_email) {//이메일 입력버튼 눌렀니?
			String inputemail = jv.jtf_email.getText();
			if(inputemail!=null) {
				beforeEmail = inputemail;
				jv.jl_email_warning.setVisible(false);
				jv.jl_email_r_warning.setVisible(false);
				jv.jl_email_r_warning2.setVisible(false);
				jv.jl_email_r_success.setVisible(false);
				//기준 검사를 먼저 할게
				int result = checkEmail(inputemail);
				if(result>0) {//이메일 형식에 안맞다면
					jv.jl_email_warning.setVisible(true);
					email = 0;
				}else {//이메일형식이 맞다면
					jv.jl_email_warning.setVisible(false);
					email = 1;
					try {//메일 발송
						sm = new SendMail(inputemail);
						jv.jl_email_warning2.setVisible(true);//입력시간 2분이라는 알림
						start_millisecond = System.currentTimeMillis();//메일 보낸시간 저장
					} catch (UnsupportedEncodingException ee) {
						System.out.println(ee.toString());
						//e.printStackTrace();
					}
				}
			}else {
				jv.jl_email_warning.setVisible(true);
				email = 0;
			}
		}
		else if(jv!=null&&obj==jv.jbt_email_r) {//인증번호 버튼을 눌렀니?
			
			end_millisecond = System.currentTimeMillis();//인증번호 입력시간 저장
			long term = end_millisecond - start_millisecond;//전송~입력 시간 계산
			if(term<120000) {//2분안에 입력한다면
				String inputNum = jv.jtf_email_r.getText();
				if(sm.rnum!=null) {
					if(sm.rnum.equals(inputNum)) {//인증번호가 일치시
						jv.jl_email_r_success.setVisible(true);
						jv.jl_email_r_warning.setVisible(false);
						jv.jl_email_warning2.setVisible(false);
						
					}
					else {//인증번호가 일치하지 않다면
						jv.jl_email_r_warning.setVisible(true);
						jv.jtf_email_r.setText("");
						email_r = 0;
					}
				}
			}
			else {//입력시간이 초과했다면
				jv.jl_email_r_warning2.setVisible(true);
				jv.jtf_email_r.setText("");
				email_r = 0;
				sm.rnum = null;
			}
		}
		/************************************************************************************************
		 * 마이페이지뷰
		 */
		else if(obj==mmv.jp_mv.jp_miv.jbt_modified) {//회원정보 수정버튼 => 일단 회원정보 조회가 됨
			String pw = pwToString(mmv.jp_mv.jp_miv.jpf_pw.getPassword());//비번을 입력했어요
			if(pw.length()>0) {
				String info_msg = MovieProtocol.MY_INFO+"#"+pw; //DAO 구현필요
				this.send(info_msg);
			}else {
				JOptionPane.showMessageDialog(mmv.jp_mv.jp_miv, "비밀번호를 입력해주세요.");
			}
		}
		else if(obj==mmv.jp_mv.jp_muv.jbt_back) {//다시 뒤로! miv로
			String mypageView_msg = MovieProtocol.MY_MOVIE+"#"+mmv.mem_id;
			this.send(mypageView_msg);
		}
		else if(obj==mmv.jp_mv.jp_muv.jbt_email) {//마이페이지 회원정보 수정- 이메일 입력버튼 눌렀니?
			String inputemail = mmv.jp_mv.jp_muv.jtf_email.getText();
			if(inputemail!=null) {
				beforeEmail = inputemail;
				mmv.jp_mv.jp_muv.jl_email_warning.setVisible(false);
				mmv.jp_mv.jp_muv.jl_email_r_warning.setVisible(false);
				mmv.jp_mv.jp_muv.jl_email_r_warning2.setVisible(false);
				mmv.jp_mv.jp_muv.jl_email_r_success.setVisible(false);
				//기준 검사를 먼저 할게
				int result = checkEmail(inputemail);
				if(result>0) {//이메일 형식에 안맞다면
					mmv.jp_mv.jp_muv.jl_email_warning.setVisible(true);
					email = 0;
				}else {//이메일형식이 맞다면
					mmv.jp_mv.jp_muv.jl_email_warning.setVisible(false);
					email = 1;
					try {//메일 발송
						sm = new SendMail(inputemail);
						mmv.jp_mv.jp_muv.jl_email_warning2.setVisible(true);//입력시간 2분이라는 알림
						start_millisecond = System.currentTimeMillis();//메일 보낸시간 저장
					} catch (UnsupportedEncodingException ee) {
						System.out.println(ee.toString());
						//e.printStackTrace();
					}
				}
			}else {
				mmv.jp_mv.jp_muv.jl_email_warning.setVisible(true);
				email = 0;
			}
		}
		else if(obj==mmv.jp_mv.jp_muv.jbt_email_r) {//마이페이지 회원정보 수정- 인증번호 버튼을 눌렀니?
			end_millisecond = System.currentTimeMillis();//인증번호 입력시간 저장
			long term = end_millisecond - start_millisecond;//전송~입력 시간 계산
			if(term<120000) {//2분안에 입력한다면
				String inputNum =mmv.jp_mv.jp_muv.jtf_email_r.getText();
				if(sm.rnum!=null) {
					if(sm.rnum.equals(inputNum)) {//인증번호가 일치시
						mmv.jp_mv.jp_muv.jl_email_r_success.setVisible(true);
						mmv.jp_mv.jp_muv.jl_email_r_warning.setVisible(false);
						mmv.jp_mv.jp_muv.jl_email_warning2.setVisible(false);
					}
					else {//인증번호가 일치하지 않다면
						mmv.jp_mv.jp_muv.jl_email_r_warning.setVisible(true);
						mmv.jp_mv.jp_muv.jtf_email_r.setText("");
						email_r = 0;
					}
				}
			}
			else {//입력시간이 초과했다면
				mmv.jp_mv.jp_muv.jl_email_r_warning2.setVisible(true);
				mmv.jp_mv.jp_muv.jtf_email_r.setText("");
				email_r = 0;
				sm.rnum = null;
			}
		}
		else if(obj==mmv.jp_mv.jp_muv.jbt_modifiedGo) {//정보수정버튼 클릭
			//회원정보 수정을 위한 기준들 마지막 체크 
			afterEmail = mmv.jp_mv.jp_muv.jtf_email.getText();
			if(afterEmail!=null) {
				if(afterEmail.equals(beforeEmail)) {
					email_r = 1;								
				}
			}
			int sum = pw + nickName + email + email_r;
			System.out.println("pw "+pw);
			System.out.println("nickName "+nickName);
			System.out.println("email"+email);
			System.out.println("email_r "+email_r);
			System.out.println(sum);
			if(sum==4) {//모든 기준 통과시, 모든 값들 가져와서
				String id = mmv.mem_id;
				String pw = pwToString(mmv.jp_mv.jp_muv.jpf_pw.getPassword());
				String email = mmv.jp_mv.jp_muv.jtf_email.getText();
				String nickName = mmv.jp_mv.jp_muv.jtf_nick.getText();
				//서버스레드로 메세지 전송
				String update_msg = MovieProtocol.INFO_UPDATE+"#"+id+"#"+pw+"#"+email+"#"+nickName;
				this.send(update_msg);//db에 넣어주세요
				//refreshCheck();//기준모두초기화@@@@@@@
				//jv.dispose();//가입화면 닫기
			}else {
				JOptionPane.showMessageDialog(mmv.jp_mv.jp_muv, "입력한 정보가 부적합합니다. 다시 확인해주세요.");
			}
		}
		
		/************************************************************************************************
		 * 화면전환
		 */
		else if(obj==mmv.jbt_logout) {//로그아웃 하려고?
			System.out.println("로그아웃 호출");
			String logout_msg = MovieProtocol.LOGOUT+"#"+mmv.mem_id;
			this.send(logout_msg);
		}
		else if(obj==mmv.jbt_myPage||obj==mmv.jp_mv.jbt_thv) {//마이페이지로 가보자 => 영화예매내역
			if (mmv.jp_mv.jp_thv.dtm_history.getRowCount() > 0) {
			    for (int i = mmv.jp_mv.jp_thv.dtm_history.getRowCount() - 1; i > -1; i--) {
			    	mmv.jp_mv.jp_thv.dtm_history.removeRow(i);
			    }
			}
			String mypageView_msg = MovieProtocol.MY_MOVIE+"#"+mmv.mem_id;
			this.send(mypageView_msg);
			System.out.println("마이페이지뷰@@@@@@@@@@@@@@@@@@@@@@@@@@@@ "+mypageView_msg);
		}
		else if(obj==mmv.jbt_ticketing) {//예매를 하고 싶구나
			tVO = new TicketingVO();
			//
			mmv.jp_lv.setVisible(false);//로그인
			mmv.jp_mv.setVisible(false);//마이페이지-틀뷰
			mmv.jp_mv.jp_miv.setVisible(false);//마이페이지-비밀번호입력뷰
			mmv.jp_mv.jp_muv.setVisible(false);//마이페이지-회원정보수정뷰
			mmv.jp_mv.jp_thv.setVisible(false);//마이페이지-영화내역뷰
			mmv.jp_mrv.setVisible(true);//영화예매-틀뷰
			mmv.jp_mrv.jp_mcv.setVisible(true);//영화예매-영화선택뷰
			mmv.jp_mrv.jp_scv.setVisible(false);//영화예매-좌석선택뷰
			mmv.jp_mrv.jp_pv.setVisible(false);//영화예매-결제뷰
			mmv.jp_rv.setVisible(false);//결과화면
			//
//			tVO.setMovie_name(mmv.jp_mrv.jl_south_movie.getText());
//			tVO.setMovie_age(mmv.jp_mrv.jl_south_ctf.getText());
//			tVO.setLoc(mmv.jp_mrv.jl_south_loc.getText());
//			tVO.setTheater(mmv.jp_mrv.jl_south_theater.getText());
//			tVO.setMovie_date(mmv.jp_mrv.jl_south_date.getText());
//			tVO.setMovie_time(mmv.jp_mrv.jl_south_time.getText());
//			tVO.setMovie_screen(mmv.jp_mrv.jl_south_screen.getText());
			//
			List<Map<String,Object>> list = new Vector<Map<String,Object>>();
			Map<String, Object> map = null;
			for(int i=0; i<mmv.movieList.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("M_TITLE", mmv.movieList.get(i).get("M_TITLE"));
				map.put("M_CERTIF", mmv.movieList.get(i).get("M_CERTIF"));
				map.put("T_LOC", mmv.movieList.get(i).get("T_LOC"));
				map.put("T_NAME", mmv.movieList.get(i).get("T_NAME"));
				map.put("S_DATE", mmv.movieList.get(i).get("S_DATE"));
				map.put("S_TIME", mmv.movieList.get(i).get("S_TIME"));
				map.put("SC_NAME", mmv.movieList.get(i).get("SC_NAME"));
				list.add(map);
			}
			for(int i=0; i<list.size(); i++) {
				String time = list.get(i).get("S_TIME").toString();
				String date = list.get(i).get("S_DATE").toString();
				int result = checkTime(date, time);
				if(result==-1) {//예약할 수 없는 시간이라면....
					list.remove(i);
				}
			}
			mmv.em.movieDtm(mmv.em.containMovieList(list));
			mmv.em.locDtm(mmv.em.containLocList(list));
			mmv.em.theaterDtm(mmv.em.containTheaterList(list), "서울");
			mmv.em.containDateList(list);
			mmv.em.dateDtm();
//			mmv.em.dateDtm2(mmv.em.containDateList(list));
			//mmv.em.scrDtm(mmv.em.containScrNameList(list));
		}

		else if(obj==mmv.jp_mv.jbt_miv) {//마이페이지에서 회원정보조회버튼
			System.out.println("회원정보버튼 클릭");
			mmv.jp_mv.jp_miv.jl_pageInfoLeft.setText(mmv.mem_nick);
			mmv.jp_mv.jp_miv.jl_mem_id.setText(mmv.mem_id);
			mmv.jp_lv.setVisible(false);
			mmv.jp_mv.setVisible(true);//마이페이지-틀뷰
			mmv.jp_mv.jp_miv.setVisible(true);//마이페이지-비밀번호입력뷰
			mmv.jp_mv.jp_muv.setVisible(false);
			mmv.jp_mv.jp_thv.setVisible(false);
			mmv.jp_mrv.setVisible(false);
			mmv.jp_mrv.jp_mcv.setVisible(false);
			mmv.jp_mrv.jp_pv.setVisible(false);
			mmv.jp_mrv.jp_scv.setVisible(false);
		}
		else if(obj==mmv.jp_mrv.jbt_backMovieChoice) {
			//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		}
		/*************************************************************************************************
		 * 좌석 선택이 끝나고 결제 버튼을 눌렀을때 */

		else if(obj==mmv.jp_mrv.jbt_goPayChoice) {
			System.out.println(mmv.jp_mrv.jp_scv.seatChoiceList.size());
			for(String seatCode : mmv.jp_mrv.jp_scv.seatChoiceList) {
				String mem_id 		= mmv.mem_id;
				String movieName 	= mmv.jp_mrv.jl_south_movie.getText();
				String temptheater 		= mmv.jp_mrv.jl_south_theater.getText();
				StringTokenizer st = new StringTokenizer(temptheater, "/");
				st.nextToken();
				String theater = st.nextToken();
				String screen 		= mmv.jp_mrv.jl_south_screen.getText();
				String seat 		= seatCode;
				String tempdate 		= mmv.jp_mrv.jl_south_date.getText();
				System.out.println("tempdate"+ tempdate);
				//년월일 제외하여 20200408 형식 맞추기
//				jl_south_date.setText("2020년 03월 28일") -> 20200328
				String date = (tempdate.substring(0, 4)+tempdate.substring(6, 8)+tempdate.substring(10, 12));
				//시간 형식 24:00 라면 그냥 집어넣어
				String time 		= mmv.jp_mrv.jl_south_time.getText();
				String pay_msg 	= MovieProtocol.PAY+"#"+mem_id+"#"+movieName+"#"+theater+"#"
									+screen+"#"+seat+"#"+date+"#"+time;
				this.send(pay_msg);
			}

		} 
		
		/**************************************************************************************************
		 * 좌석선택 버튼 클릭했을때 	 */
		else if(obj==mmv.jp_mrv.jbt_goSeatChoice) {
			/*********단위테스트하기위해 잠시 주석처리*************/
//			String temptheater 		= mmv.jp_mrv.jl_south_theater.getText();
//			StringTokenizer st = new StringTokenizer(temptheater, "/");
//			st.nextToken();
//			String theater = st.nextToken();
//			String screen 		= mmv.jp_mrv.jl_south_screen.getText();
//			String tempdate 		= mmv.jp_mrv.jl_south_date.getText();
//			System.out.println("tempdate"+ tempdate);
//			String date = (tempdate.substring(0, 4)+tempdate.substring(6, 8)+tempdate.substring(10, 12));
//			String time = mmv.jp_mrv.jl_south_time.getText();
//			String seatstatus_msg 	= MovieProtocol.GET_SEATSTATUS+"#"+theater+"#"+screen+"#"+date+"#"+time;
			
			//단위테스트용
			String seatstatus_msg = MovieProtocol.GET_SEATSTATUS+"#"+"해운대점"+"#"+"2관"+"#"+"20200411"+"#"+"19:40";
			this.send(seatstatus_msg);
		}
		else if(jv!=null&&obj==jv.jbt_email_r) {//인증번호 버튼을 눌렀니?
			
			end_millisecond = System.currentTimeMillis();//인증번호 입력시간 저장
			long term = end_millisecond - start_millisecond;//전송~입력 시간 계산
			if(term<120000) {//2분안에 입력한다면
				String inputNum = jv.jtf_email_r.getText();
				if(sm.rnum!=null) {
					if(sm.rnum.equals(inputNum)) {//인증번호가 일치시
						jv.jl_email_r_success.setVisible(true);
						jv.jl_email_r_warning.setVisible(false);
						jv.jl_email_warning2.setVisible(false);
						
					}
					else {//인증번호가 일치하지 않다면
						jv.jl_email_r_warning.setVisible(true);
						jv.jtf_email_r.setText("");
						email_r = 0;
					}
				}
			}
			else {//입력시간이 초과했다면
				jv.jl_email_r_warning2.setVisible(true);
				jv.jtf_email_r.setText("");
				email_r = 0;
				sm.rnum = null;
			}
		}
		/*************************************************************************************************
		 * 좌석 선택 화면에서 좌석 선택이 끝나고 결제 버튼을 눌렀을때 
		 */
		else if(obj==mmv.jp_mrv.jbt_goPayChoice) {
			System.out.println("결제버튼 클릭 : "+mmv.jp_mrv.jp_scv.seatChoiceList.size());
			for(String seatCode : mmv.jp_mrv.jp_scv.seatChoiceList) {
//				String mem_id 		= mmv.mem_id;
//				String movieName 	= mmv.jp_mrv.jl_south_movie.getText();
//				String temptheater 		= mmv.jp_mrv.jl_south_theater.getText();
//				StringTokenizer st = new StringTokenizer(temptheater, "/");
//				st.nextToken();
//				String theater = st.nextToken();
//				String screen 		= mmv.jp_mrv.jl_south_screen.getText();
//				String seat 		= seatCode;
//				String tempdate 		= mmv.jp_mrv.jl_south_date.getText();
//				System.out.println("tempdate"+ tempdate);
//				//년월일 제외하여 20200408 형식 맞추기
////			jl_south_date.setText("2020년 03월 28일") -> 20200328
//				String date = (tempdate.substring(0, 4)+tempdate.substring(6, 8)+tempdate.substring(10, 12));
//				//시간 형식 24:00 라면 그냥 집어넣어
//				String time 		= mmv.jp_mrv.jl_south_time.getText();
//				String pay_msg 	= MovieProtocol.PAY+"#"+mem_id+"#"+movieName+"#"+theater+"#"
//						+screen+"#"+seat+"#"+date+"#"+time;
//				this.send(pay_msg);
				
				//단위테스트용
				String seatstatus_msg = MovieProtocol.PAY+"#"+"cloudsky7"+"#"+"작은아씨들"+"#"+"해운대점"+"#"
						+"2관"+"#"+"A1"+"#"+"20200411"+"#"+"19:40";
				this.send(seatstatus_msg);
			}
			
		}//end of obj==mmv.jp_mrv.jbt_goPayChoice
		
		/* 좌석선택 화면에서 다시 영화선택화면으로 돌아갔을 때 */
		else if(obj==mmv.jp_mrv.jbt_backMovieChoice) {
			//좌석 뿌려진 패널 제거 
			mmv.jp_mrv.jp_scv.remove(mmv.jp_mrv.jp_scv.jp_center);
			mmv.jp_mrv.jl_south_ctf.setText("");
			mmv.jp_mrv.jl_south_theater.setText("");
			mmv.jp_mrv.jl_south_time.setText("");
			mmv.jp_mrv.jl_south_seat.setBounds(900, 698, 150, 20);
			mmv.jp_mrv.jl_south_pay.setBounds(1040, 698, 150, 20);	
			mmv.jp_mrv.jta_south_Allseat.setText("");
			mmv.jp_mrv.jl_south_totalPay.setText("");
			mmv.jp_mrv.jbt_goPayChoice.setVisible(false);
			mmv.jp_mrv.jbt_goSeatChoice.setVisible(true);
			mmv.jp_mrv.jbt_backMovieChoice.setVisible(false);
			
			mmv.jp_lv.setVisible(false);//로그인
			mmv.jp_mv.setVisible(false);//마이페이지-틀뷰
			mmv.jp_mv.jp_miv.setVisible(false);//마이페이지-비밀번호입력뷰
			mmv.jp_mv.jp_muv.setVisible(false);//마이페이지-회원정보수정뷰
			mmv.jp_mv.jp_thv.setVisible(false);//마이페이지-영화내역뷰
			mmv.jp_mrv.setVisible(true);//영화예매-틀뷰
			mmv.jp_mrv.jp_mcv.setVisible(true);//영화예매-영화선택뷰
			mmv.jp_mrv.jp_scv.setVisible(false);//영화예매-좌석선택뷰
			mmv.jp_mrv.jp_pv.setVisible(false);//영화예매-결제뷰
			mmv.jp_rv.setVisible(false);//결과화면
			
		}
		
		/* 좌석 선택화면에서 adult, teen 인원 선택 - 선택한 인원만큼 좌석 선택	 */
		else {
			//고를 수 있는 총 좌석 개수
			int maxChoiceCount = (mmv.jp_mrv.jp_scv.teenChoice+mmv.jp_mrv.jp_scv.adultChoice);
			//고른 좌석 개수
			int nowChoiceCount = mmv.jp_mrv.jp_scv.seatChoiceList.size();
			//좌석 이름
			String seatChoice = "";
			//이미 고른좌석인지 안고른 좌석인지 존재여부
			Boolean	exist = false;
			String totalPrice = Integer.toString(mmv.jp_mrv.jp_scv.adultChoice * 10000 + mmv.jp_mrv.jp_scv.teenChoice * 8000);
			for(int i=0; i<mmv.jp_mrv.jp_scv.jbts_adult.length; i++) {
				if(obj==mmv.jp_mrv.jp_scv.jbts_adult[i]) {
					mmv.jp_mrv.jp_scv.jbts_adult[mmv.jp_mrv.jp_scv.adultChoice].setBackground(new Color(230, 230, 230));
					mmv.jp_mrv.jp_scv.adultChoice = i;
					mmv.jp_mrv.jp_scv.jbts_adult[i].setBackground(Color.yellow);
					mmv.jp_mrv.jp_scv.jbts_adult[i].setForeground(Color.black);
					break;
				}	
			}
			for(int i=0; i<mmv.jp_mrv.jp_scv.jbts_teen.length; i++) {
				if(obj==mmv.jp_mrv.jp_scv.jbts_teen[i]) {
					mmv.jp_mrv.jp_scv.jbts_teen[mmv.jp_mrv.jp_scv.teenChoice].setBackground(new Color(230, 230, 230));
					mmv.jp_mrv.jp_scv.teenChoice = i;
					mmv.jp_mrv.jp_scv.jbts_teen[i].setBackground(Color.yellow);
					mmv.jp_mrv.jp_scv.jbts_teen[i].setForeground(Color.black);
					break;
				}					
			}
			maxChoiceCount = (mmv.jp_mrv.jp_scv.teenChoice+mmv.jp_mrv.jp_scv.adultChoice);
			totalPrice = Integer.toString(mmv.jp_mrv.jp_scv.adultChoice * 10000 + mmv.jp_mrv.jp_scv.teenChoice * 8000);
			if(nowChoiceCount>maxChoiceCount) {
				for(int k=0; k<mmv.jp_mrv.jp_scv.jbts_seat.length; k++) {
					for(int l=0; l<mmv.jp_mrv.jp_scv.jbts_seat[k].length; l++) {
						mmv.jp_mrv.jp_scv.jbts_seat[k][l].setBackground(Color.green);
						mmv.jp_mrv.jp_scv.jbts_seat[k][l].setForeground(Color.white);
					}
				}
				mmv.jp_mrv.jp_scv.seatChoiceList = null;
				mmv.jp_mrv.jp_scv.seatChoiceList = new ArrayList<>();
				mmv.jp_mrv.jl_south_seat.setBounds(900, 698, 150, 20);	
				mmv.jp_mrv.jl_south_pay.setBounds(1040, 698, 150, 20);
				mmv.jp_mrv.jta_south_Allseat.setText("");
				mmv.jp_mrv.jl_south_totalPay.setText("");
			}
			if(maxChoiceCount==0){
				mmv.jp_mrv.jbt_goPayChoice.setBackground(new Color(230, 230, 230));
				mmv.jp_mrv.jbt_goPayChoice.setForeground(Color.gray);
				mmv.jp_mrv.jbt_goPayChoice.setEnabled(false);
				mmv.jp_mrv.jl_south_pay.setBounds(1040, 698, 150, 20);
				mmv.jp_mrv.jl_south_totalPay.setText("");
			} else {
				mmv.jp_mrv.jl_south_pay.setBounds(1040, 690, 150, 20);
				mmv.jp_mrv.jl_south_totalPay.setText("총 금액 " + totalPrice + "원");
			}
			for(int i=0; i<mmv.jp_mrv.jp_scv.jbts_seat.length; i++) {
				for(int j=0; j<mmv.jp_mrv.jp_scv.jbts_seat[i].length;  j++) {
					if(obj==mmv.jp_mrv.jp_scv.jbts_seat[i][j]) {
						//(행) : (char)(i+65)   (열) : Integer.toString(j+1) 행과열 : (char)(i+65) + Integer.toString(j+1)
						seatChoice = (char)(i+65) + Integer.toString(j+1);
						if(nowChoiceCount<=maxChoiceCount) {
							for(int k=0; k<mmv.jp_mrv.jp_scv.seatChoiceList.size();k++) {
								if(seatChoice.equals(mmv.jp_mrv.jp_scv.seatChoiceList.get(k))){
									mmv.jp_mrv.jp_scv.jbts_seat[i][j].setBackground(Color.green);
									mmv.jp_mrv.jp_scv.jbts_seat[i][j].setForeground(Color.white);
									mmv.jp_mrv.jp_scv.seatChoiceList.remove(k);
									if(mmv.jp_mrv.jp_scv.seatChoiceList.size()==0) {
										mmv.jp_mrv.jl_south_seat.setBounds(900, 698, 150, 20);
										mmv.jp_mrv.jta_south_Allseat.setText("");
									}
									exist = true;
									break;
								}
							}
						}
						if(exist==false&&!(nowChoiceCount==maxChoiceCount)){
							mmv.jp_mrv.jp_scv.jbts_seat[i][j].setBackground(Color.white);
							mmv.jp_mrv.jp_scv.jbts_seat[i][j].setForeground(Color.black);
							mmv.jp_mrv.jl_south_seat.setBounds(922, 656, 110, 50);	
							mmv.jp_mrv.jl_south_pay.setBounds(1040, 690, 150, 20);
							mmv.jp_mrv.jp_scv.seatChoiceList.add(seatChoice);
							
						} else {
							return;
						}
						for(int l=0; l<mmv.jp_mrv.jp_scv.seatChoiceList.size(); l++) {					
							mmv.jp_mrv.sb_seatChoiceList.append(mmv.jp_mrv.jp_scv.seatChoiceList.get(l) + " ");
							mmv.jp_mrv.jta_south_Allseat.setText(mmv.jp_mrv.sb_seatChoiceList.toString());
						}
						mmv.jp_mrv.sb_seatChoiceList = new StringBuilder();
					}
				}
			}
			//선택한 인원수에 맞게 좌석수를 선택한 경우에 좌석버튼이 활성화됨
			if(mmv.jp_mrv.jp_scv.seatChoiceList.size()==(mmv.jp_mrv.jp_scv.teenChoice+mmv.jp_mrv.jp_scv.adultChoice)
			 &&!(maxChoiceCount==0)) {
				mmv.jp_mrv.jbt_goPayChoice.setBackground(new Color(52, 152, 219));
				mmv.jp_mrv.jbt_goPayChoice.setForeground(Color.white);
				mmv.jp_mrv.jbt_goPayChoice.setEnabled(true);
			} else {
				mmv.jp_mrv.jbt_goPayChoice.setBackground(new Color(230, 230, 230));
				mmv.jp_mrv.jbt_goPayChoice.setForeground(Color.gray);
				mmv.jp_mrv.jbt_goPayChoice.setEnabled(false);
			}
		}
		
	}
	/**************************************************************************************************
	 * ItemListener(회원가입-생년월일, 회원가입-성별)
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		//생년월일 ----------------------------------------------------------------------------------------
		if(obj==jv.jcb_year) {//년도 콤보박스가 선택되었어요
			if(e.getStateChange()==ItemEvent.SELECTED) {
				this.jv.choiceYear = jv.jcb_year.getSelectedItem().toString();//선택한 년도 정보 일단 저장할게
				String []months = null;//얘는 월정보 저장할 배열이야
				jv.remove(jv.jcb_month);
				jv.remove(jv.jcb_day);
				if(!"년도".equals(jv.choiceYear)) {//숫자년도를 선택했다면		
					if(jv.jcb_year.getSelectedItem().toString().equals(Integer.toString(jv.year))) {//선택한 년도가 올해와 같니?
						months = new String[jv.month+1];//그럼 올해의 월을 고려할게
						months[0] = "월";
						for(int i=1; i<months.length; i++) {
							months[i]= Integer.toString(i);
						}
					}
					else {//선택한 년도가 올해가 아니니?
						months = new String[13];//그럼 기본 12개월을 기준으로할게
						months[0] = "월";
						for(int i=1; i<months.length; i++) {
							months[i]= Integer.toString(i);
						}
					}
					jv.jcb_month = new JComboBox<String>(months);//월,일 콤보박스 생성
					jv.jcb_day = new JComboBox<String>();
					jv.jcb_month.addItemListener(this);
					jv.jcb_day.addItemListener(this);
					jv.jcb_day.addItem("일");
				}
				else {//설마 선택한게 문자 년도니?
					jv.jcb_month = new JComboBox<String>();//월,일 콤보박스 생성
					jv.jcb_month.addItem("월");
					jv.jcb_day = new JComboBox<String>();
					jv.jcb_day.addItem("일");
				}
				jv.choiceMonth ="월";
				jv.choiceDay ="일";
				jv.jcb_month.setBounds(288, 428 ,80 ,32);//화면에 붙이기
				jv.jcb_day.setBounds(370, 428 ,80 ,32);
				jv.jcb_month.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
				jv.jcb_day.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
				jv.add(jv.jcb_month);
				jv.add(jv.jcb_day);
				jv.revalidate();
				birth_checking();//기준체크 메소드
			}
		}
		else if(obj==jv.jcb_month) {//월 콤보박스가 선택되었어요
			if(e.getStateChange()==ItemEvent.SELECTED) {
				this.jv.choiceMonth = jv.jcb_month.getSelectedItem().toString();//선택한 월 정보 일단 저장할게
				String []days = null;//얘는 일정보 저장할 배열이야
				jv.remove(jv.jcb_day);
				if(!"월".equals(jv.choiceMonth)) {//숫자 월을 선택했다면
					if(jv.jcb_year.getSelectedItem().toString().equals(Integer.toString(jv.year))&&
							jv.jcb_month.getSelectedItem().toString().equals(Integer.toString(jv.month))) {//올해 월을 선택했니?
						days = new String[jv.day+1];//이번 달을 기준으로 할게
					}
					else {//다른 년도의 월을 선택했니? (31일/28일/30일)을 기준으로 할게
						if("1".equals(jv.choiceMonth)||"3".equals(jv.choiceMonth)||"5".equals(jv.choiceMonth)||
								"7".equals(jv.choiceMonth)||"8".equals(jv.choiceMonth)||"10".equals(jv.choiceMonth)||
										"12".equals(jv.choiceMonth)) {
							days = new String[32];
						}
						else if("2".equals(jv.choiceMonth)) {
							days = new String[29];
						}else {
							days = new String[31];
						}
					}
					days[0] = "일";
					for(int i=1; i<days.length; i++) {//일 콤보박스 생성
						days[i]= Integer.toString(i);
					}
					jv.jcb_day = new JComboBox<String>(days);
					jv.jcb_day.addItemListener(this);
				}
				else {//설마 선택한게 문자 월이니?
					jv.jcb_day = new JComboBox<String>();//일 콤보박스 생성
					jv.jcb_day.addItem("일");
				}
				jv.choiceDay ="일";
				jv.jcb_day.setBounds(370, 428 ,80 ,32);//화면에 붙이기
				jv.jcb_day.setBorder(new TitledBorder(new LineBorder(new Color(0,80,255),3)));
				jv.add(jv.jcb_day);
				jv.revalidate();
				birth_checking();//기준체크 메소드
			}
		}
		else if(obj==jv.jcb_day) {//일 콤보박스가 선택되었어요
			if(e.getStateChange()==ItemEvent.SELECTED) {
				this.jv.choiceDay = jv.jcb_day.getSelectedItem().toString();//선택한 일 정보 일단 저장할게
				birth_checking();//기준체크 메소드
			}
		}
		//성별 ------------------------------------------------------------------------------------------
		/*
		 * jl_gender_warning.setText(" 성별을 선택해주세요.");
		 */
		else if(obj==jv.jcb_gender) {
			if(e.getStateChange()==ItemEvent.SELECTED) {
				jv.jcb_genderChoice = jv.jcb_gender.getSelectedItem().toString();
				if("선택".equals(jv.jcb_genderChoice)) {
					jv.jl_gender_warning.setVisible(true);
					gender = 0;
				}else {
					jv.jl_gender_warning.setVisible(false);
					gender = 1;
				}
			}
		}
	}
	/**************************************************************************************************
	 * KeyListener(회원가입-이름, 회원가입-pw, 회원가입-닉네임, 마이페이지-pw, 마이페이지-닉네임)
	 */
	@Override
	public void keyReleased(KeyEvent e)   {
		Object obj = e.getSource();
		//회원가입 ---------------------------------------------------------------------------------------
		/*
		 * jl_pw_warning.setText(" 7~12자이어야 하고 공백은 불가합니다.");
		jl_name_warning.setText(" 2~8자이어야 하고 특수문자는 사용할 수 없습니다");
		jl_nick_warning.setText(" 2~8자이이어야 하고 특수문자는 사용할 수 없습니다.")
		 */
		if(jv!=null&&obj==jv.jtf_name) {//이름 검사
			String inputname = jv.jtf_name.getText();
			int result = checkNames(inputname, 2, 8);
			if(result>0) {//기준미통과
				jv.jl_name_warning.setVisible(true);
				name = 0;
			}else {//기준통과
				jv.jl_name_warning.setVisible(false);
				name = 1;
			}
		}
		else if(jv!=null&&obj==jv.jtf_nick) {//닉네임 검사
			String inputnick = jv.jtf_nick.getText();
			int result = checkNames(inputnick, 2, 8);
			if(result>0) {//기준미통과
				jv.jl_nick_warning.setVisible(true);
				nickName = 0;
			}else {//기준통과
				jv.jl_nick_warning.setVisible(false);
				nickName = 1;
			}
		}
		else if(jv!=null&&obj==jv.jpf_pw) {//비밀번호 검사
			String inputpw = pwToString(jv.jpf_pw.getPassword());
			int result = checkPw(inputpw, 7 ,12);
			if(result>0) {//기준미통과
				jv.jl_pw_warning.setVisible(true);
				pw = 0;
			}else {//기준통과
				jv.jl_pw_warning.setVisible(false);
				pw = 1;
			}
		}
		//마이페이지 --------------------------------------------------------------------------------------
		/*jl_pw_warning  	   		= new JLabel(" 7~12자이어야 하고 공백은 불가합니다.");
		jl_nick_warning	    	= new JLabel(" 2~8자이이어야 하고 특수문자는 사용할 수 없습니다.");
		 */
		else if(obj==mmv.jp_mv.jp_muv.jpf_pw) {//비밀번호 검사
			System.out.println("비번검사");
			String inputpw = pwToString(mmv.jp_mv.jp_muv.jpf_pw.getPassword());
			int result = checkPw(inputpw, 7 ,12);
			if(result>0) {//기준미통과
				mmv.jp_mv.jp_muv.jl_pw_warning.setVisible(true);
				pw = 0;
			}else {//기준통과
				mmv.jp_mv.jp_muv.jl_pw_warning.setVisible(false);
				pw = 1;
			}
		}
		else if(obj==mmv.jp_mv.jp_muv.jtf_nick) {//닉네임 검사
			System.out.println("닉검사");
			String inputnick = mmv.jp_mv.jp_muv.jtf_nick.getText();
			int result = checkNames(inputnick, 2, 8);
			if(result>0) {//기준미통과
				mmv.jp_mv.jp_muv.jl_nick_warning.setVisible(true);
				nickName = 0;
			}else {//기준통과
				mmv.jp_mv.jp_muv.jl_nick_warning.setVisible(false);
				nickName = 1;
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	/**************************************************************************************************
	 * MouseListener(영화선택)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		//영화예매 화면 ----------------------------------------------------------------------------------
		Object obj = e.getSource();
		if(obj==mmv.jp_mrv.jp_mcv.jt_movie) {//------------------------------------------------ 영화선택
			System.out.println("영화 클릭=======================");
			int selectRow = mmv.jp_mrv.jp_mcv.jt_movie.getSelectedRow();
			if(selectRow >= 0) {
				String movieChoice = mmv.jp_mrv.jp_mcv.dtm_movie.getValueAt(selectRow, 1).toString();
				Object icon = mmv.jp_mrv.jp_mcv.dtm_movie.getValueAt(selectRow, 0);
				ImageIcon age = (ImageIcon) icon;
				String movie_age = age.getDescription();
				mmv.jp_mrv.jl_south_movie.setBounds(340, 690, 150, 20);
				mmv.jp_mrv.jl_south_movie.setText(movieChoice);
				if("0".equals(movie_age)) {
					mmv.jp_mrv.jl_south_ctf.setText("전체 이용가");
				}
				else {
					mmv.jp_mrv.jl_south_ctf.setText(movie_age+"세 이상");
				}
				List<Map<String,Object>> userList = null;
				List<Map<String,Object>> bufList = timeCheck();
				//라벨 값 바꿔주기 VO
				tVO.setMovie_name(movieChoice);
				tVO.setMovie_age(movie_age);
				//선택한 그 영화가 있는 userList
				userList = choiceMovie(bufList, tVO);
				//중복제거
				//지점이나 날짜가 선택이 되었으면 영화도 중복제거를 해줘애한다.
				if(tVO.getMovie_date()!=null && tVO.getTheater()!=null) {
					List<String> containScrNameList = containScrNameList(userList);
					scrDtm(containScrNameList);
					mmv.jp_mrv.jp_mcv.jl_timeLock.setVisible(false);
					return;
				}
				else if(tVO.getMovie_date()!=null || tVO.getTheater()!=null) {
					//영화중복제거
					List<String> containMovieList = containMovieList(userList);
					movieDtm(containMovieList);
				}
				//지점 중복제거
				List<String> containTheaterList = containTheaterList(userList);
				System.out.println("tVO.getLoc() : "+tVO.getLoc());
				if(tVO.getLoc()==null) {
					theaterDtm(containTheaterList, "서울");
				}
				else {
					theaterDtm(containTheaterList, tVO.getLoc());
				}
				//날짜 중복 제거
				List<String> containDateList = containDateList(userList);
				dateNoList(containDateList);
			}
		}
		
		else if(obj==mmv.jp_mrv.jp_mcv.jt_local) {//------------------------------------------------ 지역선택
			System.out.println("지역 클릭=======================");
			mmv.jp_mrv.jl_south_loc.setText("극장선택");
			mmv.jp_mrv.jl_south_loc.setBounds(480, 698, 150, 20);	
			mmv.jp_mrv.jl_south_theater.setText("");
			//선택되면 색 바꾸는
//			mmv.jp_mrv.jp_mcv.localChoiceName = localChoice;
//			mmv.jp_mrv.jp_mcv.jt_local.setSelectionBackground(Color.white);
//			TableCellRenderer tcr = mmv.jp_mrv.jp_mcv.jt_local.getCellRenderer(localIndex, 0);
			//시간 조건이 안맞는 값들 리스트에서 제거
			List<Map<String,Object>> bufList = new Vector<Map<String,Object>>();
			Map<String, Object> map = null;
			//원본인 movieList를 나두고 buffer 역할
			
			for(int i=0; i<mmv.movieList.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("M_TITLE", mmv.movieList.get(i).get("M_TITLE"));
				map.put("M_CERTIF", mmv.movieList.get(i).get("M_CERTIF"));
				map.put("T_LOC", mmv.movieList.get(i).get("T_LOC"));
				map.put("T_NAME", mmv.movieList.get(i).get("T_NAME"));
				map.put("S_DATE", mmv.movieList.get(i).get("S_DATE"));
				map.put("S_TIME", mmv.movieList.get(i).get("S_TIME"));
				map.put("SC_NAME", mmv.movieList.get(i).get("SC_NAME"));
				bufList.add(map);
			}
			//시간체크 for
			for(int i=0; i<bufList.size(); i++) {
				String time = bufList.get(i).get("S_TIME").toString();
				String date = bufList.get(i).get("S_DATE").toString();
				int result = checkTime(date, time);
				if(result==-1) {//예약할 수 없는 시간이라면....
					bufList.remove(i);
				}
			}
			System.out.println("시간 조건 안맞는 값 제거: "+bufList.size()); 
			/*
			//선택된 지역이 아닌 값들 리스트에서 제거
			int selectRow = mmv.jp_mrv.jp_mcv.jt_local.getSelectedRow();
			String locChoice = mmv.jp_mrv.jp_mcv.dtm_local.getValueAt(selectRow, 0).toString();
			System.out.println("클릭된  지역: "+ locChoice);
			for(int i=0; i<bufList.size(); i++) {
				String loc = bufList.get(i).get("T_LOC").toString();
				if(!loc.equals(locChoice)) {
					bufList.remove(i);
				}
			}
			*/
			int selectRow = mmv.jp_mrv.jp_mcv.jt_local.getSelectedRow();
			String locChoice = mmv.jp_mrv.jp_mcv.dtm_local.getValueAt(selectRow, 0).toString();
			tVO.setLoc(locChoice);
			//지점 리프레시
			List<String> theaterlist = containTheaterList(bufList);//"지역/지점"
			theaterDtm(theaterlist, locChoice);
		}
		
		else if(obj==mmv.jp_mrv.jp_mcv.jt_theater) {//------------------------------------------------ 지점선택
			System.out.println("지점 클릭=======================");
			int selectRow = mmv.jp_mrv.jp_mcv.jt_theater.getSelectedRow();
			if(selectRow >= 0) {
				if(tVO.getLoc()==null) {
					tVO.setLoc("서울");
				}
				String locChoice = tVO.getLoc();
				String theaterChoice = mmv.jp_mrv.jp_mcv.dtm_theater.getValueAt(selectRow, 0).toString();
				mmv.jp_mrv.jl_south_loc.setBounds(480, 690, 150, 20);
				mmv.jp_mrv.jl_south_loc.setText(locChoice);
				mmv.jp_mrv.jl_south_theater.setText(theaterChoice);
				List<Map<String,Object>> userList = null;
				List<Map<String,Object>> bufList = timeCheck();
				//라벨 값 바꿔주기 VO
				tVO.setTheater(theaterChoice);
				//선택한 그 영화가 있는 userList
				userList = choiceTheater(bufList, tVO);
				//중복제거
				//영화나 날짜가 선택이 되었으면 지점도 중복제거를 해줘애한다.
				if(tVO.getMovie_name()!=null && tVO.getMovie_date()!=null) {
					List<String> containScrNameList = containScrNameList(userList);
					scrDtm(containScrNameList);
					mmv.jp_mrv.jp_mcv.jl_timeLock.setVisible(false);
					return;
				}
				else if(tVO.getMovie_name()!=null || tVO.getMovie_date()!=null) {
					List<String> containTheaterList = containTheaterList(userList);
					//지점중복제거
					if(tVO.getLoc()==null) {
						theaterDtm(containTheaterList, "서울");
					}
					else {
						theaterDtm(containTheaterList, tVO.getLoc());
					}
				}
				//영화중복제거
				List<String> containMovieList = containMovieList(userList);
				movieDtm(containMovieList);
				//날짜 중복 제거
				List<String> containDateList = containDateList(userList);
				dateNoList(containDateList);
			}
		}
		else if(obj==mmv.jp_mrv.jp_mcv.jt_date) {//------------------------------------------------ 날짜선택
			System.out.println("날짜 클릭=======================");
			int selectRow = mmv.jp_mrv.jp_mcv.jt_date.getSelectedRow();
			if(selectRow<=1) {
				JOptionPane.showMessageDialog(mmv.jp_mrv.jp_mcv, "일자를 선택하세요");
				return;
			}
			else if(selectRow >= 2) {
				String dateChoice = mmv.jp_mrv.jp_mcv.dtm_date.getValueAt(selectRow, 0).toString();
				String dateLabel = dateChoice;
				if(dateChoice.length()>3) {
					JOptionPane.showMessageDialog(mmv.jp_mrv.jp_mcv, "일자를 선택하세요");
					return;
				}
				else if(dateChoice.length()==3) {
					dateChoice = dateChoice.substring(0,2);				
				}else if(dateChoice.length()==2){
					dateChoice = "0"+dateChoice.substring(0,1);				
				}
				for(int i=selectRow; i>=0; i--) {
					String imsi = mmv.jp_mrv.jp_mcv.dtm_date.getValueAt(i, 0).toString();
					String [] imsi2 = imsi.split("");
					for(int j=0; j<imsi2.length; j++) {
						if("월".equals(imsi2[j])) {
							if(" ".equals(imsi2[j-2])) {
								dateChoice = "0"+imsi2[j-1]+dateChoice;
								dateLabel = "0"+imsi2[j-1]+imsi2[j]+dateLabel;
							}
							else { 
								dateChoice = imsi2[j-2]+imsi2[j-1]+dateChoice;
								dateLabel = imsi2[j-2]+imsi2[j-1]+imsi2[j]+dateLabel;
							}
						}
						else if("년".equals(imsi2[j])) {
							dateChoice = imsi2[j-4]+imsi2[j-3]+imsi2[j-2]+imsi2[j-1]+dateChoice;
							dateLabel = imsi2[j-4]+imsi2[j-3]+imsi2[j-2]+imsi2[j-1]+imsi2[j]+dateLabel;
						}
					}
				}
				System.out.println("dateChoice : "+dateChoice);
				System.out.println("dateLabel : "+dateLabel);
				mmv.jp_mrv.jl_south_date.setText(dateLabel);
				List<Map<String,Object>> userList = null;
				List<Map<String,Object>> bufList = timeCheck();
				//라벨 값 바꿔주기 VO
				tVO.setMovie_date(dateChoice);
				//선택한 그 영화가 있는 userList
				userList = choiceDate(bufList, tVO);
				//중복제거
				//영화나 지점이 선택이 되었으면 날짜도 중복제거를 해줘야한다.
				if(tVO.getMovie_name()!=null && tVO.getTheater()!=null) {
					List<String> containScrNameList = containScrNameList(userList);
					scrDtm(containScrNameList);
					mmv.jp_mrv.jp_mcv.jl_timeLock.setVisible(false);
					return;
				}
				else if(tVO.getMovie_name()!=null || tVO.getTheater()!=null) {
					//날짜 중복 제거
					List<String> containDateList = containDateList(userList);
					//dateDtm2(containDateList);
				}
				//지점중복제거
				List<String> containTheaterList = containTheaterList(userList);
				if(tVO.getLoc()==null) {
					theaterDtm(containTheaterList, "서울");
				}
				else {
					theaterDtm(containTheaterList, tVO.getLoc());
				}
				//영화중복제거
				List<String> containMovieList = containMovieList(userList);
				movieDtm(containMovieList);
			}	
		}
		else if(obj==mmv.jp_mrv.jp_mcv.jt_time) {//------------------------------------------------ 관-시간선택
			mmv.jp_mrv.jbt_goSeatChoice.setForeground(Color.white);
			mmv.jp_mrv.jbt_goSeatChoice.setBackground(new Color(52, 152, 219));
			mmv.jp_mrv.jbt_goSeatChoice.setEnabled(true);
			
			int selectRow = mmv.jp_mrv.jp_mcv.jt_time.getSelectedRow();
			String scrChoice = mmv.jp_mrv.jp_mcv.dtm_time.getValueAt(selectRow, 0).toString();
			String timeChoice = mmv.jp_mrv.jp_mcv.dtm_time.getValueAt(selectRow, 1).toString();
			
			mmv.jp_mrv.jl_south_screen.setBounds(760, 690, 150, 20);
			mmv.jp_mrv.jl_south_screen.setText(scrChoice);
			mmv.jp_mrv.jl_south_time.setText(timeChoice);
//			jl_south_screen.setBounds(760, 690, 150, 20);
//			jl_south_screen.setText("1관");
//			jl_south_time.setVisible(true);
//			jl_south_time.setText("오전 09:00:00");
			
		}
	}
	
}
