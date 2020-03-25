package maxmovie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieController {
	MovieDao dao = new MovieDao();
	final String SELECT_LOGIN = "로그인";
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
	

	public Map<String, Object> send(TheaterVO pVO) {
		Map<String, Object> rMap = new HashMap<String, Object>();
		
		return rMap;
	}
	
	public List<Map<String, Object>> sendAll(String date) {
		List<Map<String, Object>> rList = null;
		rList = dao.refreshMovieAll(date);
		return rList;
	}
	
	public List<Map<String, Object>> sendDate(String date) {
		List<Map<String, Object>> rList = null;
		rList = dao.refreshMovieDate(date);
		return rList;
	}
	
}
