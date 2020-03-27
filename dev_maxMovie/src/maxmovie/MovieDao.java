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
	
	//로그인 프로시저 호출 메소드
	public void proc_login(String p_id, String p_pw) {
		con = dbMgr.getConnection();
		try {
			cstmt = con.prepareCall("{call proc_login(?,?,?)}");
			cstmt.setString(1, p_id);
			cstmt.setString(2, p_pw);
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			rs = cstmt.executeQuery();
			while(rs.next()) {
				System.out.println(cstmt.getString(0));/////////////
			}
		} catch (SQLException e) {
			System.out.println("proc_login() Exception : " + e.toString());
			e.printStackTrace();
		}
		
	}
/*************************************************************************************************************************
 * Server가 On이 되거나 24시가 되면 데이터베이스에 있는 상영시간표를 가져오는 메소드
 * @param p_movieList MovieServer에 있는 전역변수
 * @return r_movieList 오라클에 저장된 상영시간표를 저장한 List<Map<String, Object>>
 *************************************************************************************************************************/
	public List<Map<String, Object>> refreshMovieAll(List<Map<String, Object>> p_movieList) {
		List<Map<String, Object>> r_movieList = null;
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT M_TITLE, T_LOC, T_NAME, S_DATE, S_TIME, SC_NAME ");
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
		/* 허준호 : refreshMovieAll() : 함수 단위 테스트 코드입니다.
		MovieDao md = new MovieDao();
		List<Map<String, Object>> movieList = md.refreshMovieAll(new Vector<>());
		for(Map<String, Object> showtime : movieList) {
			System.out.print(showtime.get("M_TITLE").toString()
							+" "+showtime.get("T_LOC").toString()
							+" "+showtime.get("T_NAME").toString()
							+" "+showtime.get("S_DATE").toString()
							+" "+showtime.get("S_TIME").toString()
							+" "+showtime.get("SC_NAME").toString()
							+"\n");
		}
		*/
		
		
	}
	
}
















