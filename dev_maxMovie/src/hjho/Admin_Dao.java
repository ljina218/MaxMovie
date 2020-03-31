package hjho;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
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

//	  M_TITLE,
//	   T_LOC,
//	   T_NAME,
//	   S_DATE,
//	   S_TIME,
//	   SC_NAME,
//	   A_ID
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
		}
		
		return r_movieList;
	}

}
