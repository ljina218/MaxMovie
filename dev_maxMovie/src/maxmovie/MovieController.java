 package maxmovie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieController {
	MovieDao dao = new MovieDao();
	final String SELECT_LOGIN = "로그인";
	final String CHECK_ID = "중복검사";
	final String INSERT_JOIN = "회원가입";
	final String UPDATE = "회원정보";
	final String SELECT_MY = "회원정보수정";
	final String SELECT_TICKET = "예매내역";
	final String SELECT_MOVIE = "영화선택";
	final String SELECT_SCR = "극장선택";
	final String SELECT_DATE = "날짜선택";
	final String SELECT_SEAT = "좌석선택";
	//상영시간표 새로고침
	final String MOVIE_REFRESH = "새로고침";
	
/***********************************************************************
 * 로그인, 중복검사 처리하는 메소드
 * @param MemberVO
 * @return SELECT_LOGIN => 사용자 닉네임 OR "-1" OR "2" 반환
 * 			CHECK_ID 	=> 메세지 반환
 ***********************************************************************/
	public String control(MemberVO pmVO) {
		String result = "";
		//반환할 VO : 파라미터VO와 구분짓기 위해 인스턴스화
		MemberVO rmVO = null;
		System.out.println("send(pmVO) - command : " + pmVO.getCommand());
		String command = pmVO.getCommand();
		if(SELECT_LOGIN.equals(command)) { //로그인
			result = dao.proc_login(pmVO.getMem_id(), pmVO.getMem_pw());
		}
		
		
		else if(CHECK_ID.equals(command)) { //중복검사
			result = dao.proc_checkID(pmVO.mem_id);
		}
		else if(INSERT_JOIN.equals(command)) { //회원가입
			int intresult = 0;
			intresult = dao.insertUser(pmVO);
			result = Integer.toString(intresult);
		}
		else if(SELECT_MY.equals(command)) { //회원정보수정
			int intresult = 0;
			intresult = dao.insertUser(pmVO);
			result = Integer.toString(intresult);
		}
		return result;
	}
	
	public List<Map<String, Object>> sendAll(String date) {
		List<Map<String, Object>> rList = null;
		rList = dao.refreshMovieAll(date);
		return rList;
	}
	
	
}
