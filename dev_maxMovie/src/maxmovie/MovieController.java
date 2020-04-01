 package maxmovie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieController {
	MovieDao dao = new MovieDao();
	final String SELECT_LOGIN = "로그인";
	final String CHECK_ID = "중복검사";
	final String INSERT_JOIN = "회원가입";
	final String UPDATE = "회원정보수정";
	final String SELECT_MY = "회원정보";//회원정보조회
	final String SELECT_TICKET = "예매내역";//예매내역조회
	final String SELECT_MOVIE = "영화선택";
	final String SELECT_SCR = "극장선택";
	final String SELECT_DATE = "날짜선택";
	final String SELECT_SEAT = "좌석선택";
	//상영시간표 새로고침
	final String MOVIE_REFRESH = "새로고침";
	
	/***********************************************************************
	 * 로그인, 중복검사, 회원가입, 회원정보수정 처리하는 메소드
	 * @param MemberVO
	 * @return SELECT_LOGIN => 사용자 닉네임 OR "-1" OR "2" 반환
	 * 			CHECK_ID 	=> 메세지 반환
	 ***********************************************************************/
	
	public MemberVO control(MemberVO pmVO) {
		MemberVO rmVO = new MemberVO();
		//반환할 VO : 파라미터VO와 구분짓기 위해 인스턴스화
		System.out.println("send(pmVO) - command : " + pmVO.getCommand());
		String command = pmVO.getCommand();
		if(SELECT_LOGIN.equals(command)) { //로그인
			rmVO.result = dao.proc_login(pmVO.getMem_id(), pmVO.getMem_pw());
		}
		else if(CHECK_ID.equals(command)) { //중복검사
			rmVO.result = dao.proc_checkID(pmVO.mem_id);
		}
		else if(INSERT_JOIN.equals(command)) { //회원가입
			rmVO.result = dao.insertUser(pmVO);
		}
		else if(UPDATE.equals(command)) { //회원정보수정
			rmVO.result = dao.insertUser(pmVO);
		}
		else if(UPDATE.equals(command)) { //회원정보(조회)
			rmVO = dao.showUserInfo(pmVO);
		}
		return rmVO;
	}
	/***********************************************************************
	 * 예매내역 DB에 저장을 처리하는 메소드
	 * @param List<TicketingVO> =>한사람의 여러 좌석 예매정보를 저장한 List
	 * @return 
	 ***********************************************************************/
	public void control(List<TicketingVO> tVOList) {
		//dao.proc_payTicket(tVOList);
	}
	
	
	public List<Map<String, Object>> sendAll(String date) {//서버 켰을 때
		List<Map<String, Object>> rList = null;
		//rList = dao.refreshMovieAll(date);
		return rList;
	}
	
	public List<Map<String, Object>> sendDate(String date) {//오늘이 지났을 때
		List<Map<String, Object>> rList = null;
		rList = dao.refreshMovieDate(date);
		return rList;
	}
	
}
