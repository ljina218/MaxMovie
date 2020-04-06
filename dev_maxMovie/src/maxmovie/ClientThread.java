package maxmovie;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;


import javax.swing.JOptionPane;


public class ClientThread extends Thread{
	

	MaxMovieView mmv = null;
	
	//오늘의 영화정보 list 선언부
		/*
		 * showMap.put("M_TITLE", rs.getString("M_TITLE"));
			showMap.put("T_LOC", rs.getString("T_LOC"));
			showMap.put("T_NAME", rs.getString("T_NAME"));
			showMap.put("S_DATE", rs.getString("S_DATE"));
			showMap.put("S_TIME", rs.getString("S_TIME"));
			showMap.put("SC_NAME", rs.getString("SC_NAME"));
		 */
	
	boolean stop = false;

	public ClientThread(MaxMovieView mmv) {
		this.mmv = mmv;
	}


	//화면전환 할 때 사용하는 제어 메소드
	public void display(boolean lv, boolean mv, 
							boolean miv,boolean muv, boolean thv, 
									boolean mcv, boolean sc, boolean rv) {
		mmv.jp_lv.setVisible(lv);//로그인
		mmv.jp_mv.setVisible(mv);//마이페이지-틀뷰
		mmv.jp_mv.jp_miv.setVisible(miv);//마이페이지-비밀번호입력뷰
		mmv.jp_mv.jp_muv.setVisible(muv);//마이페이지-회원정보수정뷰
		mmv.jp_mv.jp_thv.setVisible(thv);//마이페이지-영화내역뷰
		mmv.jp_mrv.setVisible(mcv);//영화선택뷰
		mmv.jp_mrv.jp_mcv.setVisible(mcv);//영화선택뷰
		mmv.jp_mrv.jp_pv.setVisible(mcv);//영화선택뷰
		mmv.jp_mrv.jp_scv.setVisible(mcv);//영화선택뷰
		mmv.jp_rv.setVisible(rv);//결제뷰
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
				switch(protocol) {//@@@@@@@@@@@@@@@@@@@@@@@@@@@
				case MovieProtocol.LOGIN:{//로그인
					String login_result = st.nextToken();
					if(login_result==null){//로그인 성공시 result값엔 저장값 없음
						mmv.jp_lv.jl_id_warning.setVisible(false);
						mmv.jp_lv.jl_pw_warning.setVisible(false);
						mmv.mem_nick = st.nextToken();
						//moviechoiceView 화면전환메소드 호출
						display(false, false, false, false, false, true, false, false);
						//예매화면에 세팅해야하니까 예매그거 호출하는거 
						mmv.movieList = new Vector<Map<String,Object>>();
						String moviechoiceView_msg = MovieProtocol.SELECT+"#";
						mmv.em.send(moviechoiceView_msg);
						
					}else {//로그인 실패시 "-1" or "2"
						if(login_result=="-1") {
							mmv.jp_lv.jtf_id.setText("");
							mmv.jp_lv.jpf_pw.setText("");
							mmv.jp_lv.jl_id_warning.setText("아이디가 존재하지 않습니다.");;
						}
						else if(login_result=="2") {
							mmv.jp_lv.jtf_id.setText("");
							mmv.jp_lv.jpf_pw.setText("");
							mmv.jp_lv.jl_pw_warning.setText("비밀번호가 일치하지 않습니다.");
						}
					}
				}break;
				case MovieProtocol.LOGOUT:{//로그아웃
					mmv.em.refreshCheck();
					mmv.mem_id = null;
					mmv.mem_nick = null;
					mmv.movieList = null;
				}break;

				case MovieProtocol.JOIN:{//회원가입
					String join_result = st.nextToken();
					if(join_result=="1") {//회원가입(DB저장) 성공 -화면전환은 eventmapping에서 처리
						JOptionPane.showConfirmDialog(mmv, "회원가입이 완료되었습니다.");
					} else {
						JOptionPane.showConfirmDialog(mmv, "회원가입 중 오류가 발생했습니다. 재시도해주세요.");
					}

				}break;

				case MovieProtocol.CHECK_ID:{//회원가입-아이디 중복확인
					String checkId_result = st.nextToken();
					if(checkId_result=="1") {//사용가능한 아이디라면
						mmv.em.jv.jl_id_success.setVisible(true);
						mmv.em.id = 1;
					}else if(checkId_result=="-1"){//중복되었다면
						mmv.em.jv.jl_id_warning.setVisible(true);
						mmv.em.id = 0;
					}
				}break;
				case MovieProtocol.MY_MOVIE:{//회원 예매내역 조회
					//영화이름, 지역, 지점, 상영날짜 시간, 상영관, 좌석, 예매번호
				}break;
				case MovieProtocol.MY_INFO:{//회원 정보조회(PW)
					String firstToken = st.nextToken();
					if(firstToken=="2") {
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
//display()				마이페이지-틀뷰 & 마이페이지-회원정보수정뷰
						display(false, true, false, true, false, false, false, false);
						mmv.movieList = null;
					}
				}break;
				case MovieProtocol.INFO_UPDATE:{//회원 정보수정
					String update_result = st.nextToken();
					if(update_result=="1") {
						JOptionPane.showConfirmDialog(mmv, "회원정보가 수정되었습니다."); 
//display()				마이페이지-틀뷰 & 마이페이지-비밀번호입력뷰
						display(false, true, true, false, false, false, false, false);
					} else {
						JOptionPane.showConfirmDialog(mmv, "회원정보 수정 중 오류가 발생했습니다. 재시도해주세요.");
					}
				}break;

				case MovieProtocol.SELECT:{//MovieChoiceView 호출하는 프로토콜
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
					mmv.em.tVO = null;
					mmv.em.tVO = new TicketingVO();
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
					synchronized (this) {

					}
				}break;
				case MovieProtocol.SELECT_MOVIE:{//영화선택
					
				}break;
				case MovieProtocol.SELECT_LOCAL:{//지역선택
					
				}break;
				case MovieProtocol.SELECT_SCR:{//지점명선택
					
				}break;
				case MovieProtocol.SELECT_DATE:{//시간선택
					
				}break;
				case MovieProtocol.SELECT_SEAT:{//좌석선택
					
				}break;
				case MovieProtocol.PAY:{//결제하기

				}break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
	}
	
}
