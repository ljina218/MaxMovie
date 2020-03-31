package maxmovie;

import java.util.StringTokenizer;

public class ClientThread extends Thread{
	
	/*to 미경언니
		언니, 
		로그인 성공시 넘어오는 아이디와 닉네임을 
		mmv.em으로 접근해서 EventMapping 클래스 전변에 있는 
		myid, mynickname에 저장해주세여~~
	* dear 진아
	*/
	MaxMovieView mmv = null;
	
	boolean stop = false;

	public ClientThread(MaxMovieView mmv) {
		this.mmv = mmv;
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
				case MovieProtocol.JOIN:{//회원가입
					
				}
				case MovieProtocol.CHECK_ID:{//회원가입-아이디 중복확인
					
				}
				case MovieProtocol.MY_MOVIE:{//회원 예매내역 조회
					//영화이름, 지역, 지점, 상영날짜 시간, 상영관, 좌석, 예매번호
				}
				case MovieProtocol.MY_INFO:{//회원 정보조회
					
				}
				case MovieProtocol.INFO_UPDATE:{//회원 정보수정
					
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
