package maxmovie;

public class MovieProtocol {
	//로그인
	public final static int LOGIN 	= 100;
	//회원가입
	public final static int JOIN 	= 200;
	public final static int CHECK_ID 	= 210;
	//회원 예매내역조회
	public final static int MY_MOVIE = 300;
	//회원정보조회
	public final static int MY_INFO	= 310; 
	//회원정보수정
	public final static int INFO_UPDATE	= 320; 
	//MovieChoiceView 호출하는 프로토콜 
	public final static int SELECT = 400;//다른 페이지에서 영화예매화면으로 가고 싶을 때
	//영화 선택
	public final static int SELECT_MOVIE = 410;
	//지역 선택
	public final static int SELECT_LOCAL = 420;
	//지점명 선택
	public final static int SELECT_SCR 	= 430;
	//시간 선택
	public final static int SELECT_DATE = 440;
	//좌석 선택 버튼
	public final static int SELECT_SEAT = 450;
	//결제하기 버튼
	public final static int PAY = 500;
	
	//관리자 로그인
	public final static int ADMIN_LOGIN = 900; 
	//관리자 상영시간표 추가
	public final static int ADMIN_INSERT = 910;
	//관리자 상영시간표 변경
	public final static int ADMIN_UPDATE = 920;
	//관리자 상영시간표 삭제
	public final static int ADMIN_DELETE = 930;
	//현재 매출현황
	public final static int ADMIN_SELECT = 940;
	
	
}




