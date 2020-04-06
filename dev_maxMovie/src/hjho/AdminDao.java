package hjho;

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

public class AdminDao {
	//오라클과의 통신을 위한 선언부 
	DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();
	Connection 			con 	= null;
	PreparedStatement 	pstmt 	= null;
	ResultSet 			rs 		= null;
	CallableStatement 	cstmt 	= null;
	
	AdminShowtimeVO astVO = null;
	
	String admin_name = null;
	String admin_t_loc = null;
	String admin_t_name = null;
	
/******************************************************************
 * ins() : 클라이언트의 아이디로 식별하여 현재상영중인 영화정보와 요청한 지점의 상영관을 반환해주는 함수 
 * @param adminID : 요청한 클라이언트 아이디
 * @return rList : 현재상영중인 영화정보와 요청한 지점의 상영관을 반환
 */
	public List<Map<String, Object>> ins(String adminID) {
		List<Map<String, Object>> rList= null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT '영화제목' as First      ");
		sql.append("        ,MOVIE_TITLE as Second ");
		sql.append("   FROM MOVIE                  ");
		sql.append("  UNION ALL                    ");
		sql.append(" SELECT t_name                 ");
		sql.append("       ,s_name                 ");
		sql.append("   FROM v_admin_insert         ");
		sql.append("  WHERE a_id = ?               ");
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, adminID);
			rs = pstmt.executeQuery();
			rList = new Vector<>();
			Map<String, Object> rMap = null;
			while(rs.next()) {
				rMap = new HashMap<String, Object>();
				rMap.put("First", rs.getString("First"));
				rMap.put("Second", rs.getString("Second"));
				rList.add(rMap);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return rList;
	}
/***********************************************************************************************
 * login() : 데이터베이스에 ADMIN 테이블에서 프로시저로 
 *  		IN :아이디와 비밀번호 
 *  		OUT : 이름, 지역, 지점명
 * @param admin_id : 로그인 아이디
 * @param admin_pw : 로그인 비번 
 * @return admin_name : 이름 반환
 */
	public Map<String, Object> login(String admin_id, String admin_pw) {
		Map<String, Object> rMap = new HashMap<>();
		try {
			con = dbMgr.getConnection();
			cstmt = con.prepareCall("{ call proc_admin_login(?,?,?,?,?)}");
			cstmt.setString(1, admin_id);
			cstmt.setString(2, admin_pw);
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			cstmt.execute();
			rMap.put("Name", cstmt.getString(3));
			rMap.put("TLoc", cstmt.getString(4));
			rMap.put("TName", cstmt.getString(5));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbMgr.freeConnection(con, cstmt);
		}
			System.out.println("지역 : "+admin_t_loc+", 지점 : "+admin_t_name);
		return rMap;
	}
/************************************************************************************************
 * 요청한 지점의 아이디로 식별하여 그 지점에 상영하고 있는 전체 상영시간표 정보를 반환해준다. 
 * @param adminID : 요청한 지점의 아이디 
 * @return r_movieList : 상영관이름, 영화제목, 상영날짜, 상영시간
 */
	public List<AdminShowtimeVO> refreshData(String adminID) {
		List<AdminShowtimeVO> r_movieList = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT SC_NAME, M_TITLE, S_DATE, S_TIME  ");
		sql.append("   FROM V_ADMIN_SHOWTIME                  ");
		sql.append("  WHERE a_id = ?                          ");
		sql.append("  ORDER BY s_date, s_time                 ");
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, adminID);
			rs = pstmt.executeQuery();
			r_movieList = new Vector<AdminShowtimeVO>();
			while(rs.next()) {
				astVO = new AdminShowtimeVO();
				astVO.setScrName(rs.getString("SC_NAME"));
				astVO.setMovieTitle(rs.getString("M_TITLE"));
				astVO.setDate(rs.getString("S_DATE"));
				astVO.setTime(rs.getString("S_TIME"));
				r_movieList.add(astVO);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return r_movieList;
	}
/************************************************************************************************
 * 요청한 지점의 아이디로 식별하여 그 지점에 상영하고 있는 당일의 상영시간표 정보를 반환해준다. 
 * @param adminID : 요청한 지점의 아이디 
 * @return r_movieList : 상영관이름, 영화제목, 상영날짜, 상영시간
 */
	public List<AdminShowtimeVO> selectData(String adminID, String currentYMD) {
		List<AdminShowtimeVO> r_movieList = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT SC_NAME, M_TITLE, S_DATE, S_TIME  ");
		sql.append("   FROM V_ADMIN_SHOWTIME                  ");
		sql.append("  WHERE a_id   = ?                          ");
		sql.append("    AND S_DATE = ?                          ");
		sql.append("  ORDER BY s_date, s_time                 ");
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, adminID);
			pstmt.setString(2, "20200324");//실제 데이터는 currentYMD 으로 넣는다
			rs = pstmt.executeQuery();
			r_movieList = new Vector<AdminShowtimeVO>();
			while(rs.next()) {
				astVO = new AdminShowtimeVO();
				astVO.setScrName(rs.getString("SC_NAME"));
				astVO.setMovieTitle(rs.getString("M_TITLE"));
				astVO.setDate(rs.getString("S_DATE"));
				astVO.setTime(rs.getString("S_TIME"));
				r_movieList.add(astVO);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return r_movieList;
	}

/***********************************************************************************************
 * insertShowtime() : 상영시간표 추가해주는 함수로 처리결과 메시지를 반환한다.
 * @param astVO : 요청아이디, 영화제목, 상영관, 연도, 월, 일, 시, 분 
 * @return msg : 처리결과 메시지 
 */
	public String insertShowtime(AdminShowtimeVO astVO) {
		String msg = null;
		int i = 0;
		try {
			con = dbMgr.getConnection();
			cstmt = con.prepareCall("{ call proc_showtime_insert(?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(++i, astVO.getId());
			cstmt.setString(++i, astVO.getMovieTitle());
			cstmt.setString(++i, astVO.getScrName());
			cstmt.setString(++i, astVO.getYy());
			cstmt.setString(++i, astVO.getMm());
			cstmt.setString(++i, astVO.getDd());
			cstmt.setString(++i, astVO.getHh24());
			cstmt.setString(++i, astVO.getMi() );
			cstmt.registerOutParameter(++i, java.sql.Types.VARCHAR);
			cstmt.execute();
			msg = cstmt.getString(i);
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			msg = "오라클서버에서 처리 오류";
			e.printStackTrace();
		} finally {
			dbMgr.freeConnection(con, cstmt);
		}
		return msg;
	}
/***********************************************************************************************
 * insertShowtime() : 상영시간표 삭제해주는 함수로 처리결과 메시지를 반환한다.
 * @param astVO : 요청아이디, 영화제목, 상영관, 연도, 월, 일, 시, 분 
 * @return msg : 처리결과 메시지 
 */
	public String deleteShowtime(AdminShowtimeVO astVO) {
		String msg = null;
		int i = 0;
		try {
			con = dbMgr.getConnection();
			cstmt = con.prepareCall("{ call proc_showtime_delete(?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(++i, astVO.getId());
			cstmt.setString(++i, astVO.getMovieTitle());
			cstmt.setString(++i, astVO.getScrName());
			cstmt.setString(++i, astVO.getYy());
			cstmt.setString(++i, astVO.getMm());
			cstmt.setString(++i, astVO.getDd());
			cstmt.setString(++i, astVO.getHh24());
			cstmt.setString(++i, astVO.getMi() );
			cstmt.registerOutParameter(++i, java.sql.Types.VARCHAR);
			cstmt.execute();
			msg = cstmt.getString(i);
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			msg = "오라클서버에서 처리 오류";
			e.printStackTrace();
		} finally {
			dbMgr.freeConnection(con, cstmt);
		}
		return msg;
	}
//	단위테스트를 하기위한 메인 메소드
//	public static void main(String[] args) {
//		Admin_Dao ad = new Admin_Dao();
//		List<Map<String, Object>> rList = new Vector<Map<String,Object>>();
//		rList = ad.ins("gangnam");
//		for (Map<String, Object> map : rList) {
//			System.out.println(map.get("First"));
//			System.out.println(map.get("Second"));
//		}
//	}

}


















