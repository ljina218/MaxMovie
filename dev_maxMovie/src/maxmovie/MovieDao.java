package maxmovie;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.util.DBConnectionMgr;

public class MovieDao {
	DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	CallableStatement cstmt = null;
	/************************************************************************
	 * 단위 테스트용
	 */
	public static void main(String[] args) {
		/* 허준호 : refreshMovieAll() : 함수 단위 테스트 코드입니다.
		MovieDao md = new MovieDao();
		List<Map<String, Object>> movieList = md.refreshMovieAll(new Vector<>());
		for(Map<String, Object> showtime : movieList) {
			System.out.print(showtime.get("M_TITLE").toString()
							+" "+showtime.get("M_CERTIF").toString()
							+" "+showtime.get("T_LOC").toString()
							+" "+showtime.get("T_NAME").toString()
							+" "+showtime.get("S_DATE").toString()
							+" "+showtime.get("S_TIME").toString()
							+" "+showtime.get("SC_NAME").toString()
							+"\n");
		}
		*/
		MovieDao dao = new MovieDao();
		
		TicketingVO ptVO = new TicketingVO();
		ptVO.setTheater("해운대점");
		ptVO.setMovie_screen("2관");
		ptVO.setMovie_date("20200411");
		ptVO.setMovie_time("19:40");
		List<Map<String, Object>> seatList = dao.get_SeatStatus(ptVO);
		for(Map<String, Object> rmap:seatList) {
			System.out.println(rmap.get("좌석").toString() + rmap.get("현황").toString());
		}
	}
	/*************************************************************************************************************************
	 * 좌석현황 조회하는 오라클 함수 처리 메소드 
	 * @param TicketingVO
	 * @return 좌석현황 예)A1, 0 을 담은 List<Map<String, Object>> 반환
	 *************************************************************************************************************************/
	public List<Map<String, Object>> get_SeatStatus(TicketingVO ptVO) {
		List<Map<String, Object>> seatList = new ArrayList<Map<String,Object>>();
		Map<String, Object> seatmap = null;
		String tablename = null;
		con = dbMgr.getConnection();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT func_SeatStatus(?,?,?,?) seattable FROM dual");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, ptVO.getTheater());
			pstmt.setString(2, ptVO.getMovie_screen());
			pstmt.setString(3, ptVO.getMovie_date());
			pstmt.setString(4, ptVO.getMovie_time());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				tablename = rs.getString("seattable");
				System.out.println(tablename);
			}
			pstmt = null;
			rs = null;
			sql = new StringBuilder();
			sql.append("SELECT seat_code, pay_status FROM ");
			sql.append(tablename);
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				seatmap = new HashMap<String, Object>();
				seatmap.put("좌석", rs.getString("seat_code"));
				seatmap.put("현황", rs.getString("pay_status"));
				seatList.add(seatmap);
			}
		} catch (SQLException e) {
			System.out.println("proc_login() Exception : " + e.toString());
			e.printStackTrace();
		}
		return seatList;
	}
	
	
	/*************************************************************************************************************************
	 * 결제-예매완료 정보 DB저장
	 * + seat테이블의 pay_status UPDATE 프로시저 처리 메소드 
	 * @param 결제버튼 클릭시 예매정보 담은 ticketingVO (pay_status : "1" 일단 결제 중 으로 ..)
	 * @return 
	 *************************************************************************************************************************/
	public void proc_payTicket(List<TicketingVO> ptList) {
		List<TicketingVO> tList = ptList;
		con = dbMgr.getConnection();
		for(TicketingVO tvo: tList) {
			try {
				cstmt = con.prepareCall("{call proc_payTicket1(?,?,?,?,?,?,?,?,?,?)}");
				cstmt.setString(1, tvo.getMem_id());
				cstmt.setString(2, tvo.getMovie_name());
				cstmt.setString(3, tvo.getTheater());
				cstmt.setString(4, tvo.getMovie_screen());
				cstmt.setString(5, tvo.getScreen_seat());
				cstmt.setString(6, tvo.getMovie_date());
				cstmt.setString(7, tvo.getMovie_time());
				cstmt.registerOutParameter(8, java.sql.Types.VARCHAR);
				cstmt.registerOutParameter(9, java.sql.Types.NUMERIC);
				cstmt.registerOutParameter(10, java.sql.Types.NUMERIC);
				cstmt.execute();
				String seatTableName = cstmt.getString(8);
				int theater_code = cstmt.getInt(9);
				int scr_code = cstmt.getInt(10);
				System.out.println("seat table 이름 : "+ seatTableName);
				System.out.println("seat_code 이름 : "+ tvo.getScreen_seat());
				System.out.println("theater_code 이름 : "+ theater_code);
				System.out.println("scr_code 이름 : "+  scr_code);
				cstmt = null;
				cstmt = con.prepareCall("{call update_seat(?,?)}");
				cstmt.setString(1, seatTableName);
				cstmt.setString(2, tvo.getScreen_seat());
				cstmt.execute();
			} catch (SQLException e) {
				System.out.println("proc_checkID() Exception : " + e.toString());
				e.printStackTrace();
			} 
		}
	}
	
	/*************************************************************************************************************************
	 * 로그인 프로시저 메소드 
	 * @param LoginView에서 입력된 사용자 아이디 p_id와 비밀번호 p_pw
	 * @return 사용자의 nickname 반환 : 아이디 비밀번호가 모두 맞음
	 * 			"-1" 반환 : 아이디가 존재하지 않음
	 * 			" 2" 반환 : 비밀번호가 맞지 않음 
	 *************************************************************************************************************************/
	public MemberVO proc_login(String p_id, String p_pw) {
		MemberVO rmVO = new MemberVO();
		con = dbMgr.getConnection();
		try {
			cstmt = con.prepareCall("{call proc_logintest(?,?,?,?)}");
			cstmt.setString(1, p_id);
			cstmt.setString(2, p_pw);
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			cstmt.execute();
			//사용자가 입력한 아이디 (존재 유무에 상관없이 입력한 아이디가 그대로 저장됨)
			rmVO.setMem_id(cstmt.getString(3));
			//사용자가 입력한 아이디가 존재하고 로그인 성공이면 닉네임이 세팅되고 그렇지 않으면 -1이나 2가 저장됨
			rmVO.setResult(cstmt.getString(4));
			System.out.println("dao "+rmVO.getMem_id() + rmVO.getResult());
		} catch (SQLException e) {
			System.out.println("proc_login() Exception : " + e.toString());
			e.printStackTrace();
		}
		return rmVO;
	}
	//테스트용
//	public MemberVO proc_login(String p_id, String p_pw) {
//		MemberVO mvo = new MemberVO();
//		String sth = null;
//		con = dbMgr.getConnection();
//		try {
//			cstmt = con.prepareCall("{call proc_logintest(?,?,?)}");
//			cstmt.setString(1, p_id);
//			cstmt.setString(2, p_pw);
//			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
//			rs = cstmt.executeQuery();
//			sth = cstmt.getString(3);
//			mvo.setMem_nickname(sth);
//		} catch (SQLException e) {
//			System.out.println("proc_login() Exception : " + e.toString());
//			e.printStackTrace();
//		}
//		return mvo;
//	}
	/*************************************************************************************************************************
	 * 아이디 중복검사 프로시저 메소드 
	 * @param JoinView에서 입력된 사용자 아이디 p_id
	 * @return 결과값 반환 : "-1" - 동일아이디 존재. OR "1" - 사용 가능
	 *************************************************************************************************************************/
	public String proc_checkID(String p_id) {
		String result = null;
		con = dbMgr.getConnection();
		try {
			cstmt = con.prepareCall("{call proc_checkID(?,?)}");
			cstmt.setString(1, p_id);
			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
			cstmt.execute();
			result = cstmt.getString(2);
		} catch (SQLException e) {
			System.out.println("proc_checkID() Exception : " + e.toString());
			e.printStackTrace();
		}
		return result;
	}
	/*************************************************************************************************************************
	 * 회원가입 DB로 INSERT하는 메소드 
	 * @param JoinView에서 입력된 사용자 정보값이 모두 담긴 MemberVO
	 * @return INSERT 성공 : 1 , INSERT 실패 : 0
	 *************************************************************************************************************************/
	public String insertUser(MemberVO pmVO) {
		int result = 0;
		con = dbMgr.getConnection();
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO member VALUES(?, ?, ?, ?, ?, ?, ?)");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pmVO.getMem_name());
			pstmt.setString(2, pmVO.getMem_id());
			pstmt.setString(3, pmVO.getMem_pw());
			pstmt.setString(4, pmVO.getMem_email());
			pstmt.setString(5, pmVO.getMem_nickname());
			pstmt.setString(6, pmVO.getMem_birth());
			pstmt.setString(7, pmVO.getMem_gender());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return Integer.toString(result);
	}
	/*************************************************************************************************************************
	 * 회원정보 조회(SELECT)하는 메소드 
	 * @param 사용자 아이디값이 담긴 MemberVO 
	 * @return DB에서 조회한 해당 사용자의 정보를 모두 담은 MemberVO
	 *************************************************************************************************************************/
	public MemberVO showUserInfo(MemberVO pmVO) {
		MemberVO rmVO = new MemberVO();
		con = dbMgr.getConnection();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT mem_name, mem_id, mem_pw, mem_email");
        sql.append(" , mem_nickname, mem_birth, mem_gender    ");
        sql.append(" FROM member                              ");
        sql.append(" WHERE mem_id=?                           ");
        sql.append(" AND mem_pw=?                           ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pmVO.getMem_id());
			pstmt.setString(2, pmVO.getMem_pw());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				rmVO.setMem_id(rs.getString("mem_id"));
				rmVO.setMem_pw(rs.getString("mem_pw"));
				rmVO.setMem_name(rs.getString("mem_name"));
				rmVO.setMem_nickname(rs.getString("mem_nickname"));
				rmVO.setMem_birth(rs.getString("mem_birth"));
				rmVO.setMem_gender(rs.getString("mem_gender"));
				rmVO.setMem_email(rs.getString("mem_email"));
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return rmVO;
	}
	/*************************************************************************************************************************
	 * sc801104021900 조회 (SELECT)하는 메소드 
	 * @param 사용자 아이디값이 담긴 MemberVO 
	 * @return DB에서 조회한 해당 상영관의 좌석과 pay_status를 모두 담은 List<Map<String,Object>>
	 *************************************************************************************************************************/
	public List<Map<String,Object>> selectSeat(TicketingVO ptVO) {
		List<Map<String,Object>> rList = new ArrayList<Map<String,Object>>();
		Map<String, Object> rMap = null;
		con = dbMgr.getConnection();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT seat_code, pay_status FROM sc801104021900");
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				rMap = new HashMap<String, Object>();
				rMap.put("좌석", rs.getString("seat_code"));
				rMap.put("현황", rs.getString("pay_status"));
				rList.add(rMap);
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return rList;
	}
	
	/*************************************************************************************************************************
	 * 회원정보 수정(UPDATE)하는 메소드 
	 * @param 사용자가 수정한 정보값이 담긴 MemberVO 
	 * @return UPDATE 성공 : 1 , UPDATE 실패 : 0
	 *************************************************************************************************************************/
	public String updateUser(MemberVO pmVO) {
		int result = 0;
		con = dbMgr.getConnection();
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE member SET 		  ");
		if(pmVO.getMem_pw()!=null&&pmVO.getMem_pw().length()>0) {
			sql.append(" 		mem_pw=? ,    	  ");
		}
		if(pmVO.getMem_email()!=null&&pmVO.getMem_email().length()>0) {
			sql.append("        mem_email=? ,     ");
		}
		if(pmVO.getMem_nickname()!=null&&pmVO.getMem_nickname().length()>0) {
			sql.append("        mem_nickname=?  ");
		}
        sql.append(" WHERE mem_id=?           ");
        System.out.println("updateUser() SQL : " + sql.toString());
        int i=1;
		try {
			pstmt = con.prepareStatement(sql.toString());
			if(pmVO.getMem_pw()!=null&&pmVO.getMem_pw().length()>0) {
				pstmt.setString(i++, pmVO.getMem_pw());
				System.out.println("수정된 mem_pw : "+pmVO.getMem_pw());
			}
			if(pmVO.getMem_email()!=null&&pmVO.getMem_email().length()>0) {
				pstmt.setString(i++, pmVO.getMem_email());
				System.out.println("수정된 mem_email : "+pmVO.getMem_email());
			}
			if(pmVO.getMem_nickname()!=null&&pmVO.getMem_nickname().length()>0) {
				pstmt.setString(i++, pmVO.getMem_nickname());
				System.out.println("수정된 mem_nickname : "+pmVO.getMem_nickname());
			}
			pstmt.setString(i, pmVO.getMem_id());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return Integer.toString(result);
	}
	/*************************************************************************************************************************
	 * 해당 회원의 예매내역(1건 이상)을 조회하는 메소드 
	 * @param 사용자 아이디값이 담긴 MemberVO 
	 * @return DB에서 조회한 해당 사용자의 예매내역(1건 이상)을 모두 담은 List<Map<String, Object>>
	 *************************************************************************************************************************/
		public List<TicketingVO> showMyticket(TicketingVO ptVO) {
			List<TicketingVO> ticket_List = new Vector<TicketingVO>();
			TicketingVO rtVO = null;
			con = dbMgr.getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT mem_id, movie_title, theater_name");
	        sql.append(" , theater_loc, scr_name, show_date     ");
	        sql.append(" , show_time, ticketing_code, seat_code ");
	        sql.append(" FROM v_myticket                        ");
	        sql.append(" WHERE mem_id=?			                ");
			try {
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, ptVO.getMem_id());
				rs = pstmt.executeQuery();
				while(rs.next()) {
					rtVO = new TicketingVO();
					rtVO.setMem_id(rs.getString("mem_id"));
					rtVO.setMovie_name(rs.getString("movie_title"));
					rtVO.setTheater(rs.getString("theater_name"));
					rtVO.setLoc(rs.getString("theater_loc"));
					rtVO.setMovie_screen(rs.getString("scr_name"));
					rtVO.setMovie_date(rs.getString("show_date"));
					rtVO.setMovie_time(rs.getString("show_time"));
					rtVO.setTicketing_code(rs.getString("ticketing_code"));
					rtVO.setScreen_seat(rs.getString("seat_code"));
					ticket_List.add(rtVO);
				}
			} catch (SQLException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
			return ticket_List;
		}
/*************************************************************************************************************************
 * Server가 On이 되거나 24시가 되면 데이터베이스에 있는 상영시간표를 가져오는 메소드
 * @param p_movieList MovieServer에 있는 전역변수
 * @return r_movieList 오라클에 저장된 상영시간표를 저장한 List<Map<String, Object>>
 *************************************************************************************************************************/
	public List<Map<String, Object>> refreshMovieAll(List<Map<String, Object>> p_movieList) {
		List<Map<String, Object>> r_movieList = null;
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT M_TITLE, M_CERTIF, T_LOC, T_NAME, S_DATE, S_TIME, SC_NAME ");
		sql.append("   FROM v_maxmovie                                      ");
		sql.append("  WHERE 1=1                                             ");
		//서버에 있는 상영시간표(p_movieList)가 없으면 오라클에서 3일치의 상영시간표를 가져다줘
		if(p_movieList ==null) { 
			sql.append("   and to_date(s_date) >= to_date('20200324')       ");
			sql.append("   and to_date(s_date) < (to_date('20200324')+3)    ");
			//실제로 오라클에 들어가야 하는 데이터 
			//WHERE to_date(s_date) >= to_date(to_char(sysdate,'YYYYMMDD'))
			//and to_date(s_date) < to_date(to_char(sysdate,'YYYYMMDD')+3)
			r_movieList = new Vector<Map<String,Object>>();
		}
		//서버에 있는 상영시간표(p_movieList)가 있으면 오라클에서 2일 뒤 의 상영시간표를 가져다줘
		else {
			sql.append("   and to_date(s_date) = (to_date('20200325')+2)    ");
			//실제로 오라클에 들어가야 하는 데이터 
			//WHERE to_date(s_date) = to_date(to_char(sysdate,'YYYYMMDD')+2)
			r_movieList = p_movieList;
		}

		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> showMap = new HashMap<>();
				showMap.put("M_TITLE", rs.getString("M_TITLE"));
				showMap.put("M_CERTIF", rs.getString("M_CERTIF"));
				showMap.put("T_LOC", rs.getString("T_LOC"));
				showMap.put("T_NAME", rs.getString("T_NAME"));
				showMap.put("S_DATE", rs.getString("S_DATE"));
				showMap.put("S_TIME", rs.getString("S_TIME"));
				showMap.put("SC_NAME", rs.getString("SC_NAME"));
				r_movieList.add(showMap);
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			dbMgr.freeConnection(con, pstmt, rs);
		}
		
		return r_movieList;
	}
	
	public List<Map<String, Object>> refreshMovieDate(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
















