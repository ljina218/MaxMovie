 package maxmovie;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MovieController {
	MovieDao dao = new MovieDao();
	final String SELECT_LOGIN 	= "로그인";
	final String CHECK_ID 		= "중복검사";
	final String INSERT_JOIN 	= "회원가입";
	final String UPDATE 		= "회원정보수정";
	final String SELECT_MY 		= "회원정보";//회원정보조회
	final String SELECT_TICKET 	= "예매내역";//예매내역조회
	final String SELECT_MOVIE	= "영화선택";
	final String SELECT_SCR 	= "극장선택";
	final String SELECT_DATE 	= "날짜선택";
	final String GET_SEATSTATUS = "좌석현황";
	final String PAY 			= "결제";
	//상영시간표 새로고침
	final String MOVIE_REFRESH = "새로고침";
	
	/***********************************************************************
	 * 좌석 현황 조회 처리하는 메소드 *****메소드명이 다른 것 주의.
	 * @param  TicketingVO
	 * @return List<Map<String, Object>> => 각 좌석의 예매현황을 조회하여 HashMap에 담음
	 * 			예) <"좌석", A1> <"현황", 0>
	 *			현황 : 0 -> 빈좌석, 1 -> 결제중, 2 -> 결제완료 		
	 ***********************************************************************/
	public List<Map<String, Object>> controlSeat(TicketingVO ptVO) {
		System.out.println("controlSeat(TicketingVO) - command : " + ptVO.getCommand());
		List<Map<String, Object>> seatList = null;
		String command = ptVO.getCommand();
		if(GET_SEATSTATUS.equals(command)) {
			seatList = dao.proc_SeatStatus(ptVO);
		}
		return seatList;
	}
	/***********************************************************************
	 * 로그인, 중복검사, 회원가입, 회원정보수정, 회원정보조회 처리하는 메소드
	 * @param MemberVO
	 * @return SELECT_LOGIN => "사용자 nickname" OR "-1" OR "2" 반환
	 * 			CHECK_ID 	=> 메세지 반환
	 ***********************************************************************/
	public MemberVO control(MemberVO pmVO) {
		MemberVO rmVO = new MemberVO();
		//반환할 VO : 파라미터VO와 구분짓기 위해 인스턴스화
		System.out.println("control(pmVO) - command : " + pmVO.getCommand());
		String command = pmVO.getCommand();
		if(SELECT_LOGIN.equals(command)) { //로그인
			rmVO = dao.proc_login(pmVO.getMem_id(), pmVO.getMem_pw());
			System.out.println("사용자가 입력한 아이디 : "+ rmVO.getMem_id());
			if("-1".equals(rmVO.getResult()) || "2".equals(rmVO.getResult())) { //로그인실패 결과값
				System.out.println("result : 로그인실패 -1 or 2 : " + rmVO.getResult());
				rmVO.setMem_id(null);
			} else { //로그인성공시 닉네임 반환
				rmVO.setMem_nickname(rmVO.getResult());
				rmVO.setResult(null);
				System.out.println("result : 로그인성공 nickname : " + rmVO.getMem_nickname());
			}
		}
		else if(CHECK_ID.equals(command)) { //중복검사
			rmVO.setResult(dao.proc_checkID(pmVO.getMem_id()));
		}
		else if(INSERT_JOIN.equals(command)) { //회원가입
			rmVO.setResult(dao.insertUser(pmVO));
		}
		else if(SELECT_MY.equals(command)) { //회원정보(조회)
			rmVO = dao.showUserInfo(pmVO);
			if(rmVO.getMem_id()==null) {
				rmVO.setResult("2");
			}
		}
		else if(UPDATE.equals(command)) {//회원정보수정
			rmVO.setResult(dao.updateUser(pmVO));
		}
		return rmVO;
	}
	/***********************************************************************
	 * 회원 예매내역 조회 처리하는 메소드
	 * @param List<TicketingVO> =>한사람의 여러 좌석 예매정보를 저장한 List
	 * @return 
	 ***********************************************************************/
	public List<TicketingVO> control(TicketingVO ptVO) {
		System.out.println("control(TicketingVO) - command : " + ptVO.getCommand());
		List<TicketingVO> ticket_list = new Vector<>();
		String command = ptVO.getCommand();
		System.out.println("control(ptVO) - command : " + command);
		if(SELECT_TICKET.equals(command)) {
			ticket_list = dao.showMyticket(ptVO);
		}
		return ticket_list;
	}

	/***********************************************************************
	 * 예매완료 정보 DB저장 및 seat테이블의 pay_status UPDATE 처리하는 메소드
	 * @param List<TicketingVO> =>한사람의 여러 좌석 예매정보를 저장한 List
	 * @return 
	 ***********************************************************************/
	public List<TicketingVO> control(List<TicketingVO> tVOList) {
		System.out.println("control(List<TicketingVO>) - command : " + tVOList.get(0).getCommand());
		dao.proc_payTicket(tVOList);
		return tVOList;
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
