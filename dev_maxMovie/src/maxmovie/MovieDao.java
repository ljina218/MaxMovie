package maxmovie;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.util.DBConnectionMgr;

public class MovieDao {
	DBConnectionMgr dbMgr = DBConnectionMgr.getInstance();
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	CallableStatement cstmt = null;
	
	//로그인 프로시저 호출 메소드
	public void proc_login(String p_id, String p_pw) {
		con = dbMgr.getConnection();
		try {
			cstmt = con.prepareCall("{call proc_logintest(?,?,?)}");
			cstmt.setString(1, p_id);
			cstmt.setString(2, p_pw);
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			rs = cstmt.executeQuery();
			while(rs.next()) {
				System.out.println(cstmt.getString(3));/////////////
			}
		} catch (SQLException e) {
			System.out.println("proc_login() Exception : " + e.toString());
			e.printStackTrace();
		}
		
	}
/*************************************************************************************************************************
 * Server가 On이 되거나 24시가 되면 데이터베이스에 있는 상영시간표를 가져오는 메소드
 * @param pList MovieServer에 있는 전역변수
 * @return rList 오라클에 저장된 상영시간표를 저장한 List<Map<String, Object>>
 *************************************************************************************************************************/
	public List<Map<String, Object>> refreshMovieAll(String date) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Map<String, Object>> refreshMovieDate(String date) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
