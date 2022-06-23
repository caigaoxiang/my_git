package com.resoft.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author 陈礼松
 */
public class ConnSqlServer {
	// sqlserver2005
//	public static String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//	public static String URL = "jdbc:sqlserver://192.168.60.165:1433;DatabaseName=testByThgk";
//	public static String USERNAME = "sa";
//	public static String PASSWORD = "cist123!@#";

	private final Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public static Connection getConnection(){
		Connection conn = null;
		String DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url = "jdbc:sqlserver://192.168.60.192:1433;DatabaseName=Usci";
		String user = "sa";// 用户名,系统默认的账户名
		String password = "cist123!@#";// 你安装时选设置的密码
		try {
			Class.forName(DRIVER);// 加载数据库驱动程序
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, user, password);// 获得Connection对象
			System.out.println("sqlserver 连接成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static Connection getMysqlConnection(){
		Connection conn = null;
		String DRIVER="com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://59.225.203.109:5206/platform";
		String user = "platform";// 用户名,系统默认的账户名
		String password = "platform";// 你安装时选设置的密码
		try {
			Class.forName(DRIVER);// 加载数据库驱动程序
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, user, password);// 获得Connection对象
			System.out.println("Mysql 连接成功");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Mysql 连接失败");
		}
		return conn;
	}


//	public ConnSqlServer() {
//
//		try {
//			Class.forName(DRIVER);
//			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//			System.out.println("sqlserver 连接成功");
//		} catch (Exception e) {
//			System.out.println("sqlserver 连接失败");
//			e.printStackTrace();
//		}
//	}

	public int count(String table) {
		int count = 0 ;
		try {
			ps = conn.prepareStatement("select count(*) from "+table);
			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public List<List> selectData(String sql, List<String> paramsList) {
		List<List> list = new ArrayList<List>();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				List temp = new ArrayList();
				for (String param : paramsList) {
					temp.add(rs.getObject(param));
				}
				list.add(temp);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

//	@Test
//	public void testSqlServer() {
//		String sql = "select users.userid ,users.username,users.userpassword,users.pinyin from users";
//		List paramsList = new ArrayList();
//		paramsList.add("userid");
//		paramsList.add("username");
//		paramsList.add("userpassword");
//		paramsList.add("pinyin");
//		List<List> list = new ConnSqlServer().selectData(sql, paramsList);
//		for (List t : list) {
//			for (Object o : t) {
//				System.out.println(o);
//			}
//		}
//	}

	public void closeConn() {
		try {
			if (conn != null) {
				conn.close();
			}
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
