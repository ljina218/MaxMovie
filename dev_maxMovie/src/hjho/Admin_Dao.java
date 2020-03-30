package hjho;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.util.DBConnectionMgr;

public class Admin_Dao {
	//오라클과의 통신을 위한 선언부 
	DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();
	Connection 			con 	= null;
	PreparedStatement 	pstmt 	= null;
	ResultSet 			rs 		= null;
	CallableStatement 	cstmt 	= null;
	
	
	public String login(String admin_id, String admin_pw) {
		String admin_name = null;
		String admin_t_loc = null;
		String admin_t_name = null;
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

}
