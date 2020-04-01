package hjho;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.util.DBConnectionMgr;

public class Admin_Dao {
	//오라클과의 통신을 위한 선언부 
	DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();
	Connection 			con 	= null;
	PreparedStatement 	pstmt 	= null;
	ResultSet 			rs 		= null;
	CallableStatement 	cstmt 	= null;
	
	AdminMovieVO mVO = null;
	
	String admin_name = null;
	String admin_t_loc = null;
	String admin_t_name = null;
	
	/******************************************************************
	 * ins() : 메인 화면에서 상영시간표 추가를 누르면 서버에서 영화정보를 insert 해주고
	 * 		  클라이언트로 넘겨준다. 
	 * @param adminID
	 * @return
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
	
	public String login(String admin_id, String admin_pw) {
		try {
			con = dbMgr.getConnection();
			cstmt = con.prepareCall("{ call proc_admin_login(?,?,?,?,?)}");
			cstmt.setString(1, admin_id);
			cstmt.setString(2, admin_pw);
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			cstmt.execute();
			admin_name = cstmt.getString(3);
			admin_t_loc = cstmt.getString(4);
			admin_t_name = cstmt.getString(5);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbMgr.freeConnection(con, cstmt);
		}
			System.out.println("지역 : "+admin_t_loc+", 지점 : "+admin_t_name);
		return admin_name;
	}

	public List<AdminMovieVO> refreshData(String adminID) {
		List<AdminMovieVO> r_movieList = null;
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
			r_movieList = new Vector<AdminMovieVO>();
			while(rs.next()) {
				mVO = new AdminMovieVO();
				mVO.setScr_name(rs.getString("SC_NAME"));
				mVO.setMovie_title(rs.getString("M_TITLE"));
				mVO.setShow_date(rs.getString("S_DATE"));
				mVO.setShow_time(rs.getString("S_TIME"));
				r_movieList.add(mVO);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			dbMgr.freeConnection(con, pstmt, rs);
		}
		return r_movieList;
	}
//	public static void main(String[] args) {
//		Admin_Dao ad = new Admin_Dao();
//		List<Map<String, Object>> rList = new Vector<Map<String,Object>>();
//		rList = ad.ins("gangnam");
//		for (Map<String, Object> map : rList) {
//			System.out.println(map.get("First"));
//			System.out.println(map.get("Second"));
//			
//		}
//	}

}
