package maxmovie;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

public class ClientThread extends Thread{
	
	/*to 미경언니
		언니, 
		로그인 성공시 넘어오는 아이디와 닉네임을 
		mmv.em으로 접근해서 EventMapping 클래스 전변에 있는 
		myid, mynickname에 저장해주세여~~
	* dear 진아
	* 	EventMapping 전변에 mynick만 저장
	* 	myid는 로그인시 입력값은 전변에 저장해야함
	* 	EventMapping 전변에 저장한 두 값은 mypage에서 쓰일 것
	*/
	MaxMovieView mmv = null;
	
	boolean stop = false;

	public ClientThread(MaxMovieView mmv) {
		this.mmv = mmv;
	}
	
	//서버로부터 메세지를 듣고 뷰에 변화를 주는 메소드
	@Override
	public void run() {
		Vector<TicketingVO> v = new Vector<TicketingVO>();
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
					String login_result = st.nextToken();
					if(login_result==null){//로그인 성공시 result값엔 저장값 없음
						mmv.jp_lv.jl_id_warning.setVisible(false);
						mmv.jp_lv.jl_pw_warning.setVisible(false);
						mmv.em.mynickname = st.nextToken();
						//moviechoiceView 화면전환메소드 호출
						mmv.em.display(false, false, false, false, false, true, false, false);
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
				}
				case MovieProtocol.JOIN:{//회원가입
					String join_result = st.nextToken();
					if(join_result=="1") {//회원가입(DB저장) 성공 -화면전환은 eventmapping에서 처리
						JOptionPane.showConfirmDialog(mmv, "회원가입이 완료되었습니다.");
					} else {
						JOptionPane.showConfirmDialog(mmv, "회원가입 중 오류가 발생했습니다. 재시도해주세요.");
						
					}
				}
				case MovieProtocol.CHECK_ID:{//회원가입-아이디 중복확인
					String checkId_result = st.nextToken();
					if(checkId_result=="1") {//사용가능한 아이디라면
						mmv.em.jv.jl_id_success.setVisible(true);
						mmv.em.id = 1;
					}else if(checkId_result=="-1"){//중복되었다면
						mmv.em.jv.jl_id_warning.setVisible(true);
						mmv.em.id = 0;
					}
				}
				case MovieProtocol.MY_MOVIE:{//회원 예매내역 조회@@@@@@@@@@@@@@@@
					//영화이름, 지역, 지점, 상영날짜 시간, 상영관, 좌석, 예매번호
					TicketingVO tVO = new TicketingVO();
					tVO.setMovie_name(st.nextToken());
					tVO.setLoc(st.nextToken());
					tVO.setTheater(st.nextToken());
					tVO.setMovie_date(st.nextToken());
					tVO.setMovie_time(st.nextToken());
					tVO.setMovie_screen(st.nextToken());
					tVO.setScreen_seat(st.nextToken());
					tVO.setTicketing_code(st.nextToken());
					v.add(tVO);
					mmv.jp_mv.jp_thv.dtm_history.addRow(v);
					mmv.em.display(false, false, false, false, true, false, false, false);
				}
				case MovieProtocol.MY_INFO:{//회원 정보조회
					//회원정보 조회와 수정을 같은 화면으로 사용하고 있다. 
					//
					mmv.jp_mv.jp_muv.jl_mem_id.setText(st.nextToken());
					mmv.jp_mv.jp_muv.jpf_pw.setText(st.nextToken());
					mmv.jp_mv.jp_muv.jl_mem_name.setText(st.nextToken());
					mmv.jp_mv.jp_muv.jtf_nick.setText(st.nextToken());
					//"20200331" 문자열 자르기
					String tempDate = st.nextToken();
					mmv.jp_mv.jp_muv.jl_mem_year.setText(tempDate.substring(0, 3));
					mmv.jp_mv.jp_muv.jl_mem_month.setText(tempDate.substring(4, 5));
					mmv.jp_mv.jp_muv.jl_mem_day.setText(tempDate.substring(6, 7));
					mmv.jp_mv.jp_muv.jl_mem_gender.setText(st.nextToken());
					mmv.jp_mv.jp_muv.jtf_email.setText(st.nextToken());
					mmv.em.display(false, false, true, false, false, false, false, false);
				}
				case MovieProtocol.INFO_UPDATE:{//회원 정보수정
					String update_result = st.nextToken();
					if(update_result=="1") {
						JOptionPane.showConfirmDialog(mmv, "회원정보가 수정되었습니다."); 
						mmv.em.display(false, false, false, true, false, false, false, false);
					} else {
						JOptionPane.showConfirmDialog(mmv, "회원정보 수정 중 오류가 발생했습니다. 재시도해주세요.");
						mmv.em.display(false, false, false, true, false, false, false, false);
					}
					
				}
				case MovieProtocol.SELECT:{//MovieChoiceView 호출하는 프로토콜
					
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
