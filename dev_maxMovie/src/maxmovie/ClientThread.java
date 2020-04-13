package maxmovie;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;


import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.sun.jndi.toolkit.dir.ContainmentFilter;


public class ClientThread extends Thread{
	

	MaxMovieView mmv = null;
	
	boolean stop = false;
	
	//GET_SEATSTATUS : 좌석현황을 담아 SeatChoiceView의 전역변수에 초기화할 변수
	List<Map<String, Object>> seatList = new Vector<Map<String,Object>>();

	public ClientThread(MaxMovieView mmv) {
		this.mmv = mmv;
	}

	//화면전환 할 때 사용하는 제어 메소드
	public void display(boolean lv, boolean mv, 
							boolean miv,boolean muv, boolean thv, 
							boolean mrv, boolean mcv, boolean pv, boolean scv, boolean rv) {
		mmv.jp_lv.setVisible(lv);//로그인
		mmv.jp_mv.setVisible(mv);//마이페이지-틀뷰
		mmv.jp_mv.jp_miv.setVisible(miv);//마이페이지-비밀번호입력뷰
		mmv.jp_mv.jp_muv.setVisible(muv);//마이페이지-회원정보수정뷰
		mmv.jp_mv.jp_thv.setVisible(thv);//마이페이지-영화내역뷰
		mmv.jp_mrv.setVisible(mrv);//영화예매-틀뷰
		mmv.jp_mrv.jp_mcv.setVisible(mcv);//영화예매-영화선택뷰
		mmv.jp_mrv.jp_scv.setVisible(scv);//영화예매-좌석선택뷰
		mmv.jp_mrv.jp_pv.setVisible(pv);//영화예매-결제뷰
		mmv.jp_rv.setVisible(rv);//결과화면
	}
	
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
	

	//서버로부터 메세지를 듣고 뷰에 변화를 주는 메소드
	@Override
	public void run() {
		String msg = null;
		int protocol = 0;
		try {
			while(!stop) {
				msg = (String)mmv.ois.readObject();
				StringTokenizer st = null;
				if(msg!=null) {
					st = new StringTokenizer(msg,"#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				//******************************************************************************
				case MovieProtocol.LOGIN:{//로그인
					//로그인 성공하면 아이디, 닉네임순 실패하면 -1이나 2
					String login_result = st.nextToken();
					mmv.jp_lv.jtf_id.setText("");
					mmv.jp_lv.jpf_pw.setText("");
					if("-1".equals(login_result)) {//아이디존재하지 않음
						mmv.jp_lv.jtf_id.setText("");
						mmv.jp_lv.jl_id_warning.setVisible(true);
						mmv.jp_lv.jl_pw_warning.setVisible(false);
					}
					else if("2".equals(login_result)) {//비번이 틀림
						mmv.jp_lv.jpf_pw.setText("");
						mmv.jp_lv.jl_id_warning.setVisible(false);
						mmv.jp_lv.jl_pw_warning.setVisible(true);
					}
					else { //로그인 성공했을때 login_result에는 id. nextToken()에는 nick
						mmv.mem_id = login_result;
						mmv.mem_nick = st.nextToken();
						System.out.println("닉넴 아이디" + mmv.mem_id + mmv.mem_nick);
						//
						mmv.jp_lv.jl_id_warning.setVisible(false);
						mmv.jp_lv.jl_pw_warning.setVisible(false);
						mmv.jl_nickInfo.setText(mmv.mem_nick);
						mmv.jp_mv.jp_thv.jl_pageInfoLeft.setText(mmv.mem_nick);
						mmv.jp_mv.jp_muv.jl_mem_id.setText(mmv.mem_nick);
						mmv.jl_logo_small.setVisible(true);
						mmv.jl_nickInfo.setVisible(true);
						mmv.jl_nickInfoEnd.setVisible(true);
						mmv.jbt_logout.setVisible(true);
						mmv.jbt_myPage.setVisible(true);
						mmv.jbt_ticketing.setVisible(true);
						System.out.println("ClientThread에서 받은 id, nick : "+mmv.mem_id+", "+mmv.mem_nick);
						display(false, false, false, false, false, true, true, false,false,false);//moviechoiceView 화면전환메소드 호출
						mmv.em.tVO = null;
						mmv.em.tVO = new TicketingVO();
						//
						System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@사이즈: "+  mmv.movieList.size());
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
						System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@시간을 고려한 리스트: "+list.size());
						mmv.em.tVO = new TicketingVO();
						mmv.em.movieDtm(mmv.em.containMovieList(list));
						mmv.em.locDtm(mmv.em.containLocList(list));
						mmv.em.theaterDtm(mmv.em.containTheaterList(list), "서울");
//						mmv.em.containDateList(list);
						mmv.em.dateDtm();
//						mmv.em.dateDtm2(mmv.em.containDateList(list));
						//mmv.em.scrDtm(mmv.em.containScrNameList(list));
					}
					
				}break;
				//******************************************************************************
				case MovieProtocol.LOGOUT:{//로그아웃
					mmv.mem_id = null;
					mmv.mem_nick = null;
					mmv.jl_logo_small.setVisible(false);
					mmv.jl_nickInfo.setVisible(false);
					mmv.jl_nickInfoEnd.setVisible(false);
					mmv.jbt_logout.setVisible(false);
					mmv.jbt_myPage.setVisible(false);
					mmv.jbt_ticketing.setVisible(false);
					display(true, false, false, false, false, false, false, false, false, false);
					mmv.jp_lv.jtf_id.setText("");
					mmv.jp_lv.jpf_pw.setText("");
				}break;
				//******************************************************************************
				case MovieProtocol.JOIN:{//회원가입
					String join_result = st.nextToken();
					if("1".equals(join_result)) {//회원가입(DB저장) 성공 -화면전환은 eventmapping에서 처리
						JOptionPane.showConfirmDialog(mmv, "회원가입이 완료되었습니다.");
						mmv.em.jv.jtf_id.setText("");
						mmv.em.jv.jtf_name.setText("");
						mmv.em.jv.jtf_nick.setText("");
						mmv.em.jv.jtf_email.setText("");
						mmv.em.jv.jtf_email_r.setText("");
						mmv.em.jv.jpf_pw.setText("");
						mmv.em.refreshCheck();//기준체크 리프레시
						mmv.em.jv.dispose();
					} else {
						JOptionPane.showConfirmDialog(mmv, "회원가입 중 오류가 발생했습니다. 재시도해주세요.");
					}

				}break;
				//******************************************************************************
				case MovieProtocol.CHECK_ID:{//회원가입-아이디 중복확인
					String checkId_result = st.nextToken();
					System.out.println(checkId_result);
					if("1".equals(checkId_result)) {//사용가능한 아이디라면
						mmv.em.jv.jl_id_success.setVisible(true);
						mmv.em.id = 1;
					}else if("-1".equals(checkId_result)){//중복되었다면
						mmv.em.jv.jl_id_warning.setVisible(true);
						mmv.em.id = 0;
					}
				}break;
				//******************************************************************************
				case MovieProtocol.MY_MOVIE:{//회원 예매내역 조회
					//영화이름, 지역, 지점, 상영날짜 시간, 상영관, 좌석, 예매번호
					Vector<String> v = new Vector<String>();
					TicketingVO pVO = new TicketingVO();
					String movie = st.nextToken();
					String locThe = st.nextToken()+"/"+st.nextToken();
					String dateTime = st.nextToken()+"/"+st.nextToken();
					String screenSeat = st.nextToken()+"/"+st.nextToken();
					String ticketcode = st.nextToken();
					v.add(movie);
					v.add(locThe);
					v.add(dateTime);
					v.add(screenSeat);
					v.add(ticketcode);
					mmv.jp_mv.jp_thv.dtm_history.addRow(v);
					System.out.println("테이블 사이즈: "+mmv.jp_mv.jp_thv.dtm_history.getRowCount());
					display(false, true, false, false, true, false, false, false, false, false);
//					for(int i=0; i<ticket_list.size(); i++) {
//						String movieName = ticket_list.get(i).getMovie_name();
//						String loc = ticket_list.get(i).getLoc();
//						String theater = ticket_list.get(i).getTheater();
//						String date = ticket_list.get(i).getMovie_date();
//						String time = ticket_list.get(i).getMovie_time();
//						String screen = ticket_list.get(i).getMovie_screen();
//						String seat = ticket_list.get(i).getScreen_seat();
//						String ticketCode = ticket_list.get(i).getTicketing_code();
//					}
				}break;
				//******************************************************************************
				case MovieProtocol.MY_INFO:{//회원 정보조회(PW)
//					넘어오는 토큰
//					String myinfo_msg = MovieProtocol.MY_INFO+"#"+id+"#"+pw+"#"+name+
//					"#"+nickname+"#"+birth+"#"+gender+"#"+email;
					String firstToken = st.nextToken();
					if("2".equals(firstToken)) {
						JOptionPane.showMessageDialog(mmv, "비밀번호가 맞지 않습니다.");
					} else {
						mmv.jp_mv.jp_muv.jl_mem_id.setText(firstToken);
						//비밀번호와 닉네임은 회원정보조회화면에 세팅하지 않는다
						String pw = st.nextToken();
						mmv.jp_mv.jp_muv.jl_mem_name.setText(st.nextToken());
						String nickname = st.nextToken();
						String tempDate = st.nextToken();
						System.out.println("ClientThread tempDate: "+ tempDate);
						mmv.jp_mv.jp_muv.jl_mem_year.setText(tempDate.substring(0, 4));
						mmv.jp_mv.jp_muv.jl_mem_month.setText(tempDate.substring(4, 6));
						mmv.jp_mv.jp_muv.jl_mem_day.setText(tempDate.substring(6, 8));
						System.out.println("ClientThread "+mmv.jp_mv.jp_muv.jl_mem_year.getText()+"&"+mmv.jp_mv.jp_muv.jl_mem_month.getText()+"&"+mmv.jp_mv.jp_muv.jl_mem_day.getText());
						mmv.jp_mv.jp_muv.jl_mem_gender.setText(st.nextToken());
						mmv.jp_mv.jp_muv.jtf_email.setText(st.nextToken());
						//display()마이페이지-틀뷰 & 마이페이지-회원정보수정뷰
						display(false, true, false, true, false, false, false, false, false ,false);//
						mmv.movieList = null;
					}
				}break;
				//******************************************************************************
				case MovieProtocol.INFO_UPDATE:{//회원 정보수정
//					넘어오는 토큰
//					String update_msg = MovieProtocol.INFO_UPDATE+"#"+update_result;
					String update_result = st.nextToken();
					if("1".equals(update_result)) {
						JOptionPane.showMessageDialog(mmv, "회원정보가 수정되었습니다.");
						mmv.em.refreshCheck();//기준체크 리프레시
						//display()마이페이지-틀뷰 & 마이페이지-비밀번호입력뷰
						display(false, true, true, false, false, false, false, false, false, false);
					} else {
						JOptionPane.showMessageDialog(mmv, "회원정보 수정 중 오류가 발생했습니다. 재시도해주세요.");
					}
				}break;
				//******************************************************************************
				case MovieProtocol.SELECT:{//클라이언트에게 영화정보 리스트를 넘겨주는!!!!!
					//@@@@@이 아이는... 처음 화면을 켰을 때만 호출이 됩니다. 
					//오늘의 영화정보 list 선언부
					/*
					 *  showMap.put("M_TITLE", rs.getString("M_TITLE"));
						showMap.put("M_CERTIF", rs.getString("M_CERTIF"));
						showMap.put("T_LOC", rs.getString("T_LOC"));
						showMap.put("T_NAME", rs.getString("T_NAME"));
						showMap.put("S_DATE", rs.getString("S_DATE"));
						showMap.put("S_TIME", rs.getString("S_TIME"));
						showMap.put("SC_NAME", rs.getString("SC_NAME"));
						List<Map<String, Object>> movieList = null;
						
						String movielist_msg = MovieProtocol.SELECT+"#"+age+"#"+title+"#"+
							loc+"#"+theater+"#"+date+"#"+time+"#"+screen;
					 */
					String age = st.nextToken();
					String title = st.nextToken();
					String loc = st.nextToken();
					String theater = st.nextToken();
					String date = st.nextToken();
					String time = st.nextToken();
					String screen = st.nextToken();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("M_CERTIF", age);
					map.put("M_TITLE", title);
					map.put("T_LOC", loc);
					map.put("T_NAME", theater);
					map.put("S_DATE", date);
					map.put("S_TIME", time);
					map.put("SC_NAME", screen);
					mmv.movieList.add(map);
					System.out.println("무비리스트 사이즈"+mmv.movieList.size()+", 연령: "+age);
				}break;
				//******************************************************************************
//				case MovieProtocol.SELECT_MOVIE:{//영화선택
//					
//				}break;
//				case MovieProtocol.SELECT_LOCAL:{//지역선택
//					
//				}break;
//				case MovieProtocol.SELECT_SCR:{//지점명선택
//					
//				}break;
//				case MovieProtocol.SELECT_DATE:{//시간선택
//					
//				}break;
				case MovieProtocol.GET_SEATSTATUS:{//좌석선택
					String seatcode = st.nextToken();
					String status = st.nextToken();
					System.out.println("seatcode, status : "+seatcode+", "+status);
					Map<String, Object> rmap = new HashMap<String, Object>();
					rmap.put("좌석", seatcode);
					rmap.put("현황", status);
					seatList.add(rmap);
					mmv.jp_mrv.jp_scv.seatSetting(seatList);
					display(false, false, false, false, false, true, false, false, true, false);
				}break;
				case MovieProtocol.PAY:{//결제하기
					
				}break;
				}//end of switch
			}//end of while
		} catch (Exception e) {
			
			System.out.println(e.toString());
			e.printStackTrace();
		}
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
	
}
