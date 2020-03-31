package maxmovie;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
/*************************************************************************************************************************
 * 로그인 프로시저 메소드 
 * @param LoginView에서 입력된 사용자 아이디 p_id와 비밀번호 p_pw
 * @return 사용자의 nickname 반환 : 아이디 비밀번호가 모두 맞음
 * 			"-1" 반환 : 아이디가 존재하지 않음
 * 			" 2" 반환 : 비밀번호가 맞지 않음 
 *************************************************************************************************************************/
	public String proc_login(String p_id, String p_pw) {
		String sth = null;
		con = dbMgr.getConnection();
		try {
			cstmt = con.prepareCall("{call proc_logintest(?,?,?)}");
			cstmt.setString(1, p_id);
			cstmt.setString(2, p_pw);
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			rs = cstmt.executeQuery();
			sth = cstmt.getString(3);
		} catch (SQLException e) {
			System.out.println("proc_login() Exception : " + e.toString());
			e.printStackTrace();
		}
		return sth;
	}
/*************************************************************************************************************************
 * 아이디 중복검사 프로시저 메소드 
 * @param JoinView에서 입력된 사용자 아이디 p_id
 * @return 메세지 반환 : "-1" - 동일아이디 존재. OR "1" - 사용 가능
 *************************************************************************************************************************/
	public String proc_checkID(String p_id) {
		String msg = null;
		con = dbMgr.getConnection();
		try {
			cstmt = con.prepareCall("{call proc_checkID(?,?)}");
			cstmt.setString(1, p_id);
			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
			rs = cstmt.executeQuery();
			msg = cstmt.getString(2);
		} catch (SQLException e) {
			System.out.println("proc_checkID() Exception : " + e.toString());
			e.printStackTrace();
		}
		return msg;
	}
/*************************************************************************************************************************
 * 회원가입 DB로 INSERT하는 메소드 
 * @param JoinView에서 입력된 사용자 정보값이 모두 담긴 MemberVO
 * @return INSERT 성공 : 1 , INSERT 실패 : 0
 *************************************************************************************************************************/
	public int insertUser(MemberVO pmVO) {
		int result = 0;
		con = dbMgr.getConnection();
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO member VALUES(?, ?, ?, ?, ?, ?, ?)");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pmVO.mem_name);
			pstmt.setString(2, pmVO.mem_id);
			pstmt.setString(3, pmVO.mem_pw);
			pstmt.setString(4, pmVO.mem_email);
			pstmt.setString(5, pmVO.mem_nickname);
			pstmt.setString(6, pmVO.mem_birth);
			pstmt.setString(7, pmVO.mem_gender);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return result;
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
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pmVO.mem_id);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				rmVO.setMem_name(rs.getString("mem_name"));
				rmVO.setMem_id(rs.getString("mem_id"));
				rmVO.setMem_pw("mem_pw");
				rmVO.setMem_email(rs.getString("mem_email"));
				rmVO.setMem_nickname("mem_nickname");
				rmVO.setMem_birth("mem_birth");
				rmVO.setMem_gender("mem_gender");
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return rmVO;
	}
/*************************************************************************************************************************
 * 회원정보 수정(UPDATE)하는 메소드 
 * @param 사용자가 수정한 정보값이 담긴 MemberVO 
 * @return UPDATE 성공 : 1 , UPDATE 실패 : 0
 *************************************************************************************************************************/
	public int updateUser(MemberVO pmVO) {
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
				pstmt.setString(i++, pmVO.mem_pw);
				System.out.println("수정된 mem_pw : "+pmVO.mem_pw);
			}
			if(pmVO.getMem_email()!=null&&pmVO.getMem_email().length()>0) {
				pstmt.setString(i++, pmVO.mem_email);
				System.out.println("수정된 mem_email : "+pmVO.mem_email);
			}
			if(pmVO.getMem_nickname()!=null&&pmVO.getMem_nickname().length()>0) {
				pstmt.setString(i++, pmVO.mem_nickname);
				System.out.println("수정된 mem_nickname : "+pmVO.mem_nickname);
			}
			pstmt.setString(i, pmVO.mem_id);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return result;
	}
/*************************************************************************************************************************
 * 해당 회원의 예매내역(1건 이상)을 조회하는 메소드 
 * @param 사용자 아이디값이 담긴 MemberVO 
 * @return DB에서 조회한 해당 사용자의 예매내역(1건 이상)을 모두 담은 List<Map<String, Object>>
 *************************************************************************************************************************/
		public List<Map<String, Object>> showMyticket(MemberVO pmVO) {
			List<Map<String, Object>> ticketList = new Vector<Map<String,Object>>();
			Map<String, Object> rMap = null;
			con = dbMgr.getConnection();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT mem_id, movie_title, theater_name");
	        sql.append(" , theater_loc, scr_name, show_date     ");
	        sql.append(" , show_time, ticketing_code            ");
	        sql.append(" FROM v_myticket                        ");
	        sql.append(" WHERE mem_id=?			                ");
			try {
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, pmVO.mem_id);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					rMap = new HashMap<String, Object>();
					rMap.put("mem_id", rs.getString("mem_id"));
					rMap.put("movie_title", rs.getString("movie_title"));
					rMap.put("theater_name", rs.getString("theater_name"));
					rMap.put("theater_loc", rs.getString("theater_loc"));
					rMap.put("scr_name", rs.getString("scr_name"));
					rMap.put("show_date", rs.getString("show_date"));
					rMap.put("show_time", rs.getString("show_time"));
					rMap.put("ticketing_code", rs.getString("ticketing_code"));
					ticketList.add(rMap);
				}
			} catch (SQLException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
			return ticketList;
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
	/************************************************************************
	 * 허준호 : 단위 테스트 용으로 만든 메인입니다.
	 * @param args
	 */
	public static void main(String[] args) {
		/* 허준호 : refreshMovieAll() : 함수 단위 테스트 코드입니다.*/
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

		//박미경 : proc_login() & proc_checkID() : 함수 단위 테스트 코드입니다.
		MovieDao md = new MovieDao();
		String nickname = null;
		nickname = md.proc_login("clousky7", "1234");
		System.out.println(nickname);
		
		String msg = null;
		msg = md.proc_checkID("clou7");
		System.out.println(msg);
		
		MemberVO mVO = new MemberVO();
		mVO.setMem_id("mimitest");
		mVO.setMem_email("2222@gmail.com");
		mVO.setMem_nickname("hhhha");
		int result = 0;
//		result = md.insertUser(mVO);
//		result = md.updateUser(mVO);
		System.out.println(result);
		
		mVO = new MemberVO();
		mVO.setMem_id("cloudsky7");
		List<Map<String, Object>> tList = null;
		tList = md.showMyticket(mVO);
		for(Map<String, Object> rMap : tList) {
			System.out.println(rMap.get("movie_title"));
		}
	}
	
}
















