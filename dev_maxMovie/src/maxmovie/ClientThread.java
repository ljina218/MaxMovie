package maxmovie;

import java.awt.Color;
import java.awt.Component;
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
		mmv.jp_mrv.jp_scv.setVisible(pv);//영화예매-좌석선택뷰
		mmv.jp_mrv.jp_pv.setVisible(scv);//영화예매-결제뷰
		mmv.jp_rv.setVisible(rv);//결과화면
	}
	
	//영화 시간 조건 메소드
	public int checkTime (String time) {//스케줄에 있는 영화 시간 받아서
		int result = 0;
		StringTokenizer token = new StringTokenizer(time, ":");//시와 분으로 쪼개기
		char[] scheduledhour = token.nextToken().toCharArray();
		char[] scheduledmin = token.nextToken().toCharArray();
		String sethour = null;
		String setmin = null;
		if(scheduledhour[0]==0){//시 앞에 붙은 0 떼어내기
			sethour = scheduledhour[1]+"";
		}else {
			sethour = scheduledhour[0]+""+scheduledhour[1]; 
		}
		if(scheduledmin[0]==0){//분 앞에 붙은 0 떼어내기
			setmin = scheduledmin[1]+"";
		}else {
			setmin = scheduledmin[0]+""+scheduledmin[1]; 
		}
		int hour = Integer.parseInt(sethour);//시와 분을 string에서 int 형으로 바꾸기
		int min = Integer.parseInt(setmin);
		Calendar now = Calendar.getInstance();//현재 시간에서 
		now.add(Calendar.MINUTE, -30);//30분 빼기 => 30분 전까지만 예매가능
		int nowhour = now.get(Calendar.HOUR_OF_DAY);
		int nowmin = now.get(Calendar.MINUTE);
		if(hour>=nowhour) {//시가 아직 안지났고
			if(min>=nowmin) {//분이 아직 안지났다면,
				result=1;
			}
		}
		return result;
	}
	

	//서버로부터 메세지를 듣고 뷰에 변화를 주는 메소드
	@Override
	public void run() {
		Vector<TicketingVO> v = new Vector<TicketingVO>();
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
					String login_result = st.nextToken();
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
					else {
						mmv.jp_lv.jl_id_warning.setVisible(false);
						mmv.jp_lv.jl_pw_warning.setVisible(false);
						mmv.mem_nick = login_result;//닉네임저장
						mmv.jl_nickInfo.setText(mmv.mem_nick);
						mmv.jl_logo_small.setVisible(true);
						mmv.jl_nickInfo.setVisible(true);
						mmv.jl_nickInfoEnd.setVisible(true);
						mmv.jbt_logout.setVisible(true);
						mmv.jbt_myPage.setVisible(true);
						mmv.jbt_ticketing.setVisible(true);
						System.out.println(mmv.mem_nick);
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
							int result = mmv.em.checkTime(time);
							if(result==0) {//예약할 수 없는 시간이라면....
								list.remove(i);
							}
						}
						System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@시간을 고려한 리스트: "+list.size());
						mmv.em.movieDtm(mmv.em.containMovieList(list));
						mmv.em.locDtm(mmv.em.containLocList(list));
						mmv.em.theaterDtm(mmv.em.containTheaterList(list), "서울");
						mmv.em.containDateList(list);
						mmv.em.dateDtm();
						mmv.em.scrDtm(mmv.em.containScrNameList(list));
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
				}break;
				//******************************************************************************
				case MovieProtocol.MY_INFO:{//회원 정보조회(PW)
					String firstToken = st.nextToken();
					if("2".equals(firstToken)) {
						JOptionPane.showMessageDialog(mmv, "비밀번호가 맞지 않습니다.");
					} else {
						mmv.jp_mv.jp_muv.jl_mem_id.setText(firstToken);
						mmv.jp_mv.jp_muv.jpf_pw.setText(st.nextToken());
						mmv.jp_mv.jp_muv.jl_mem_name.setText(st.nextToken());
						mmv.jp_mv.jp_muv.jtf_nick.setText(st.nextToken());
						String tempDate = st.nextToken();
						mmv.jp_mv.jp_muv.jl_mem_year.setText(tempDate.substring(0, 3));
						mmv.jp_mv.jp_muv.jl_mem_month.setText(tempDate.substring(4, 5));
						mmv.jp_mv.jp_muv.jl_mem_day.setText(tempDate.substring(6, 7));
						mmv.jp_mv.jp_muv.jl_mem_gender.setText(st.nextToken());
						mmv.jp_mv.jp_muv.jtf_email.setText(st.nextToken());
						//display()마이페이지-틀뷰 & 마이페이지-회원정보수정뷰
						display(false, true, false, true, false, false, false, false, false ,false);//
						mmv.movieList = null;
					}
				}break;
				//******************************************************************************
				case MovieProtocol.INFO_UPDATE:{//회원 정보수정
					String update_result = st.nextToken();
					if("1".equals(update_result)) {
						JOptionPane.showConfirmDialog(mmv, "회원정보가 수정되었습니다.");
						mmv.em.refreshCheck();//기준체크 리프레시
						//display()마이페이지-틀뷰 & 마이페이지-비밀번호입력뷰
						display(false, true, true, false, false, false, false, false, false, false);//
					} else {
						JOptionPane.showConfirmDialog(mmv, "회원정보 수정 중 오류가 발생했습니다. 재시도해주세요.");
					}
				}break;
				//******************************************************************************
				case MovieProtocol.SELECT:{//클라이언트에게 영화정보 리스트를 넘겨주는!!!!!
					//@@@@@이 아이는... 처음 화면을 켰을 때만 호출이 됩니다. 
					//오늘의 영화정보 list 선언부
					/*
					 * showMap.put("M_TITLE", rs.getString("M_TITLE"));
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
				case MovieProtocol.SELECT_SEAT:{//좌석선택
					
				}break;
				case MovieProtocol.PAY:{//결제하기

				}break;
				}
			}
		} catch (Exception e) {
			
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
}
