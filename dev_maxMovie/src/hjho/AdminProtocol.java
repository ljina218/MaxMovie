package hjho;

public class AdminProtocol {
	//로그인 시
	public static final int _LOGIN = 100;
	
	//상영시간표 새로고침
	//전체 상영시간표를 가져온다.
	public static final int _REFRESH = 200;
	
	//당일에 상영하는 상영시간표 가져오기
	public static final int _SEL = 300;
	
	//다이얼로그 화면 띄우기
	//로그인 성공시 서버에 전송하여 상영중인 영화, 지점의 상영관을 불러오는프로토콜
	//  - 다이얼로그의 JComboBox 초기화에 쓰인다.
	public static final int _DIAL = 400;
	
	//상영시간표 추가
	//다이얼로그창에서 반영하기를 누를 때 오라클에 전송하는 프로토콜
	public static final int _INS = 410;
	
	//상영시간표 삭제
	public static final int _DEL = 500;
	
	//종료
	public static final int _EXIT = 600;
	
}
