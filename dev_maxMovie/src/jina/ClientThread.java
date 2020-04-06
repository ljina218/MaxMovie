package jina;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

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
	
//	//화면전환 할 때 사용하는 제어 메소드
//	public void display(boolean lv, boolean mv, 
//							boolean miv,boolean muv, boolean thv, 
//									boolean mcv, boolean sc, boolean rv) {
//		mmv.jp_lv.setVisible(lv);//로그인
//		mmv.jp_mv.setVisible(mv);//마이페이지-틀뷰
//		mmv.jp_mv.jp_miv.setVisible(miv);//마이페이지-비밀번호입력뷰
//		mmv.jp_mv.jp_muv.setVisible(muv);//마이페이지-회원정보수정뷰
//		mmv.jp_mv.jp_thv.setVisible(thv);//마이페이지-영화내역뷰
//		mmv.jp_mrv.setVisible(mcv);//영화선택뷰
//		mmv.jp_mrv.jp_mcv.setVisible(mcv);//영화선택뷰
//		mmv.jp_mrv.jp_pv.setVisible(mcv);//영화선택뷰
//		mmv.jp_mrv.jp_scv.setVisible(mcv);//영화선택뷰
//		mmv.jp_rv.setVisible(rv);//결제뷰
//	}
		
	
	//서버로부터 메세지를 듣고 뷰에 변화를 주는 메소드
	@Override
	public void run() {
		String msg = null;
		int protocol = 0;
		try {
			while(!stop) {
				msg = (String)mmv.ois.readObject();
				System.out.println(msg);
				StringTokenizer st = null;
				if(msg!=null) {
					st = new StringTokenizer(msg,"#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {//@@@@@@@@@@@@@@@@@@@@@@@@@@@
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
					String age = st.nextToken();
					String title = st.nextToken();
					String loc = st.nextToken();
					String theater = st.nextToken();
					Map<String, String> map = new HashMap<String, String>();
					map.put("M_CERTIF", age);
					map.put("M_TITLE", title);
					map.put("T_LOC", loc);
					map.put("T_NAME", theater);
					mmv.jp_mrv.jp_mcv.movielist.add(map);
					System.out.println(mmv.jp_mrv.jp_mcv.movielist.size());
					/**********************************************************************************************
					 * 영화 셋팅
					 */
					mmv.jp_mrv.jp_mcv.dtm_movie.setRowCount(0);
					Vector<Object> m_title = null;
					String before_title = "";
					synchronized (this) {
						
						for(int i=0; i<mmv.jp_mrv.jp_mcv.movielist.size(); i++) {
							String after_title = mmv.jp_mrv.jp_mcv.movielist.get(i).get("M_TITLE");
							if(!after_title.equals(before_title)) {
								m_title = new Vector<Object>();
								if("15".equals(mmv.jp_mrv.jp_mcv.movielist.get(i).get("M_CERTIF").toString())) {
									m_title.add(mmv.jp_mrv.jp_mcv._15);					
								}
								else if("19".equals(mmv.jp_mrv.jp_mcv.movielist.get(i).get("M_CERTIF").toString())) {
									m_title.add(mmv.jp_mrv.jp_mcv._19);					
								} 
								else if("0".equals(mmv.jp_mrv.jp_mcv.movielist.get(i).get("M_CERTIF").toString())) {
									m_title.add(mmv.jp_mrv.jp_mcv._0);					
								}
								else if("12".equals(mmv.jp_mrv.jp_mcv.movielist.get(i).get("M_CERTIF").toString())) {
									m_title.add(mmv.jp_mrv.jp_mcv._12);					
								}
								m_title.add(after_title);
								mmv.jp_mrv.jp_mcv.dtm_movie.addRow(m_title);
							}
							before_title = after_title;
						}
						
						
						/**********************************************************************************************
						 * 지역 지점 셋팅
						 */
						
						mmv.jp_mrv.jp_mcv.dtm_local.setRowCount(0);
						mmv.jp_mrv.jp_mcv.dtm_theater.setRowCount(0);
						//지역 테이블에 추가
						Vector<String> area = null;
						mmv.jp_mrv.jp_mcv.arealist = new Vector<String>();//지역정보 저장
						String before_area = "";
						for(int i=0; i<mmv.jp_mrv.jp_mcv.movielist.size(); i++) {
							String after_area = mmv.jp_mrv.jp_mcv.movielist.get(i).get("T_LOC");
							if(!after_area.equals(before_area)) {
								area = new Vector<String>();
								area.add(after_area);
								mmv.jp_mrv.jp_mcv.dtm_local.addRow(area);//테이블에 추가
								mmv.jp_mrv.jp_mcv.arealist.add(after_area);//지역정보에 추가
							}
							before_area = after_area;
						}
						//지점 테이블에 추가
						/*
					Vector<String> loc = null;
					loclist = new Vector<String>();//지점정보 저장
					String before_loc ="";
					for(int i=0; i<movielist.size(); i++) {
						String after_loc = movielist.get(i).get("지점");
						if(!after_loc.equals(before_loc)) {
							if(movielist.get(i).get("지역").equals(arealist.get(0))) {
								loc = new Vector<String>();
								loc.add(after_loc);
								dtm_theater.addRow(loc);//테이블에 추가					
							}
							loclist.add(after_loc);//지역정보에 추가
						}
						before_loc = after_loc;
					}
						 */
						Vector<String> th_loc = null;
						String before_loc ="";
						for(int i=0; i<mmv.jp_mrv.jp_mcv.movielist.size(); i++) {
							String after_loc = mmv.jp_mrv.jp_mcv.movielist.get(i).get("T_NAME");
							if(!after_loc.equals(before_loc)) {
								if(mmv.jp_mrv.jp_mcv.movielist.get(i).get("T_LOC").equals(mmv.jp_mrv.jp_mcv.arealist.get(0))) {
									th_loc = new Vector<String>();
									th_loc.add(after_loc);
									mmv.jp_mrv.jp_mcv.dtm_theater.addRow(th_loc);//테이블에 추가
								}
							}
							before_loc = after_loc;
						}
						
						//각 지역의 지점 갯수
						Vector<String> arealist2 = new Vector<String>();
						arealist2 = mmv.jp_mrv.jp_mcv.arealist;
						String before_loc2 = "";
						int num =0;
						int a = 0;
						for(int j=0; j<arealist2.size(); j++) {
							String areaname = arealist2.get(j);//지역을 하나 뽑아서
							for(int i=0; i<mmv.jp_mrv.jp_mcv.movielist.size(); i++) {
								if(areaname.equals(mmv.jp_mrv.jp_mcv.movielist.get(i).get("T_LOC"))){//같은 지역이라면
									String after_loc = mmv.jp_mrv.jp_mcv.movielist.get(i).get("T_NAME");
									if(!after_loc.equals(before_loc2)) {//지점 다르다면
										a = a+1;//갯수를 1 더해서
										String count = Integer.toString(a);
										arealist2.set(j, count);//그 지역 자리에 갯수를 저장
									}
									before_loc2 = after_loc;
								}else if(!areaname.equals(mmv.jp_mrv.jp_mcv.movielist.get(i).get("T_LOC"))) {//다른 지역이라면
									a=0;//다시 셋팅을 위한 초기화
								}
							}
						}
						
						//지역 테이블에 갯수 추가
						Vector<String> arealist3 = new Vector<String>();
						for(int i=0; i<mmv.jp_mrv.jp_mcv.dtm_local.getRowCount(); i++){
							arealist3.add(mmv.jp_mrv.jp_mcv.dtm_local.getValueAt(i, 0)+"("+arealist2.get(i)+")");
						}
						Vector<String> area2 = null;
						mmv.jp_mrv.jp_mcv.dtm_local.setRowCount(0);
						for(int i=0; i<arealist3.size(); i++) {
							area2 = new Vector<String>();
							area2.add(arealist3.get(i));
							mmv.jp_mrv.jp_mcv.dtm_local.addRow(area2);
						}
						
						/**********************************************************************************************
						 * 날짜 셋팅
						 */
						mmv.jp_mrv.jp_mcv.dtm_date.setRowCount(0);
						Calendar today = Calendar.getInstance();
						//today.add(Calendar.MONTH, -3);
						//today.add(Calendar.DAY_OF_MONTH, -10);
						String before_year ="";
						String before_month = "";
						Vector<String> date = null;
						for(int i=0; i<20; i++) {
							int year = today.get(Calendar.YEAR);
							String after_year = Integer.toString(year);
							if(!before_year.equals(after_year)) {
								date = new Vector<String>();
								date.add(year+"년");
								mmv.jp_mrv.jp_mcv.dtm_date.addRow(date);
								before_year = after_year;
							}
							int month = today.get(Calendar.MONTH)+1;
							String after_month = Integer.toString(month);
							if(!before_month.equals(after_month)) {
								date = new Vector<String>();
								date.add(after_month+"월");
								mmv.jp_mrv.jp_mcv.dtm_date.addRow(date);
								before_month = after_month;
							}
							String day = Integer.toString(today.get(Calendar.DAY_OF_MONTH));
							date = new Vector<String>();
							date.add(day+"일");
							today.add(Calendar.DAY_OF_MONTH, +1);
							mmv.jp_mrv.jp_mcv.dtm_date.addRow(date);
						}
					}
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
			e.printStackTrace();
		}
	}
	
}
