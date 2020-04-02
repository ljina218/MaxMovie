package maxmovie;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
	List<Map<String, Object>> movieList = null;
	
	boolean stop = false;

	public ClientThread(MaxMovieView mmv) {
		this.mmv = mmv;
	}
	//화면전환 할 때 사용하는 제어 메소드
		public void display(boolean lv, boolean mrv, boolean mcv, boolean scv, boolean pv,
							boolean mv, boolean thv, boolean miv, boolean muv, boolean rv) {
			mmv.jp_lv.setVisible(lv);
			mmv.jp_mrv.setVisible(mrv);
			mmv.jp_mrv.jp_mcv.setVisible(mcv);
			mmv.jp_mrv.jp_scv.setVisible(scv);
			mmv.jp_mrv.jp_pv.setVisible(pv);
			mmv.jp_mv.setVisible(mv);
			mmv.jp_mv.jp_thv.setVisible(thv);
			mmv.jp_mv.jp_miv.setVisible(miv);
			mmv.jp_mv.jp_muv.setVisible(muv);
			mmv.jp_rv.setVisible(rv);
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
		String msg = null;
		int protocol = 0;
		try {
			while(!stop) {
				//msg = (String)mmv.ois.readObject();
				StringTokenizer st = null;
				if(msg!=null) {
					st = new StringTokenizer("","#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {//@@@@@@@@@@@@@@@@@@@@@@@@@@@
				case MovieProtocol.LOGIN:{//로그인
					
				}
				case MovieProtocol.LOGOUT:{//로그아웃
					movieList = null;
				}
				case MovieProtocol.JOIN:{//회원가입
					
				}
				case MovieProtocol.CHECK_ID:{//회원가입-아이디 중복확인
					/*서버가 트이면~~~~~
					if() {//사용가능한 아이디라면
						mmv.jv.jl_id_success.setVisible(true);
						mmv.em.id =1;
					}else if(){//중복되었다면
						mmv.jv.jl_id_warning.setVisible(true);
						mmv.em.id=0;
					}
					*/
				}
				case MovieProtocol.MY_MOVIE:{//회원 예매내역 조회
					//영화이름, 지역, 지점, 상영날짜 시간, 상영관, 좌석, 예매번호
				}
				case MovieProtocol.MY_INFO:{//회원 정보조회
					movieList = null;
				}
				case MovieProtocol.INFO_UPDATE:{//회원 정보수정
					
				}
				case MovieProtocol.SELECT:{//MovieChoiceView 호출하는 프로토콜
					movieList = null;
					//오늘의 영화정보 list 선언부
					/*
					 * showMap.put("M_TITLE", rs.getString("M_TITLE"));
						showMap.put("T_LOC", rs.getString("T_LOC"));
						showMap.put("T_NAME", rs.getString("T_NAME"));
						showMap.put("S_DATE", rs.getString("S_DATE"));
						showMap.put("S_TIME", rs.getString("S_TIME"));
						showMap.put("SC_NAME", rs.getString("SC_NAME"));
						
						List<Map<String, Object>> movieList = null;
						
						String movielist_msg = MovieProtocol.SELECT+"#"+age+"#"+title+"#"+
							loc+"#"+theater+"#"+date+"#"+time+"#"+screen;
					 */
					//String age = st.nextToken();
					//String title = ms.movieList.get(i).get("M_TITLE").toString();
					//String loc = ms.movieList.get(i).get("T_LOC").toString();
					//String theater = ms.movieList.get(i).get("T_NAME").toString();
					//String date = ms.movieList.get(i).get("S_DATE").toString();
					//String time = ms.movieList.get(i).get("S_TIME").toString();
					//String screen = ms.movieList.get(i).get("SC_NAME").toString();
					
				}
				case MovieProtocol.SELECT_MOVIE:{//영화선택
					
				}
				case MovieProtocol.SELECT_LOCAL:{//지역선택
					
				}
				case MovieProtocol.SELECT_SCR:{//지점명선택
					
				}
				case MovieProtocol.SELECT_DATE:{//시간선택
					
				}
				case MovieProtocol.SELECT_SEAT:{//좌석선택
					
				}
				case MovieProtocol.PAY:{//결제하기
					
				}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			//e.printStackTrace();
		}
	}
	
}
