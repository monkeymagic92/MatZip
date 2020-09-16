package com.koreait.matzip.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbManager {
	
	public static Connection getCon() throws Exception {
		String url = "jdbc:mysql://127.0.0.1:3306/matzip?useUnicode=true&characterEncoding=UTF-8";
		String user = "root";
		String pw = "orcl";
		String className = "com.mysql.cj.jdbc.Driver";
		
		Class.forName(className);
		Connection con = DriverManager.getConnection(url, user, pw);
		System.out.println("DB 연결 완료!");
		return con;
	}
	
	public static void close(Connection con, PreparedStatement ps) {
		close(con, ps, null);
	}
	
	public static void close(Connection con, PreparedStatement ps, ResultSet rs) {
		if(rs != null) { try { rs.close(); } catch (SQLException e) { e.printStackTrace();} }
		if(ps != null) { try { ps.close(); } catch (SQLException e) { e.printStackTrace();} }
		if(con != null) { try { con.close(); } catch (SQLException e) { e.printStackTrace();} }
	}
}
