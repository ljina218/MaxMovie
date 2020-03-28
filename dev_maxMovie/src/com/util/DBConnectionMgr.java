package com.util;
/*
 * DBConnectionMgr은 여러 업무에서 공통으로 사용하는 클래스입니다
 * 사용한 자원(Connection, PreparedStatement, ResultSet)은 반드시 반납을 하도록 합니다
 * 동시 접속자 수가 많은 시스템에서 자원 사용은 곧 메모리 소모랑 직결되므로
 * 서버가 다운되거나 시스템 장애발생의 원인이 됩니다
 */
import java.awt.event.ActionEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;


public class DBConnectionMgr {
	//싱글톤패턴 적용
	private static DBConnectionMgr dbMgr = new DBConnectionMgr();
	private DBConnectionMgr () { 
		
	}
	public static DBConnectionMgr getInstance () {
		if(dbMgr==null) {
			dbMgr = new DBConnectionMgr();
		}
		return dbMgr;
	}
	//선언부 
	public static final String _DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String _URL = "jdbc:oracle:thin:@192.168.0.37:1521:orcl11";

	public static String _USER = "MAXMOVIE";
	public static String _PW = "tiger";
	
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public Connection getConnection() {
		System.out.println("Connection getConnection() 호출 성공");
		//오라클 회사 정보 수집 코드
		try {
			Class.forName(_DRIVER);
			con = DriverManager.getConnection(_URL, _USER, _PW);
		} catch (ClassNotFoundException ce) {
			System.out.println("드라이버 클래스 이름을 찾을 수 없어요.");
		} catch (Exception e) {
			System.out.println("예외가 발생했음. 정상적으로 처리가 안됨.");
		}
		return con;
	}
	
	//사용한 자원(Connection, PreparedStatement, ResultSet)을 반납하는 메소드
	public void freeConnection(Connection con, PreparedStatement pstmt, ResultSet rs) {
		try {
			//사용자원의 생성 역순으로 반환할 것
			if(rs!=null) {
				rs.close();
			}
			if(pstmt!=null) {
				pstmt.close();
			}
			if(con!=null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.toString()); //어떤 예외사항에러인지 확인하기위한 출력문
		}
	}
	//사용한 자원(Connection, PreparedStatement, ResultSet)을 반납하는 메소드
	public void freeConnection(Connection con, PreparedStatement pstmt) {
		try {
			//사용자원의 생성 역순으로 반환할 것
			if(pstmt!=null) {
				pstmt.close();
			}
			if(con!=null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.toString()); //어떤 예외사항에러인지 확인하기위한 출력문
		}
	}
	public void freeConnection(Connection con, CallableStatement cstmt) {
		try {
			//사용자원의 생성 역순으로 반환할 것
			if(cstmt!=null) {
				cstmt.close();
			}
			if(con!=null) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.toString()); //어떤 예외사항에러인지 확인하기위한 출력문
		}
	}

	
	
	
}
