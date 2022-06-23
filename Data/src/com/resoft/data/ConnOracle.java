package com.resoft.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
/**
 * @author 陈礼松
 */
public class ConnOracle {

	private PreparedStatement ps = null;
	private Connection conn = null;


	public static Connection getConnection(){
		Connection conn = null;
		String DRIVER="oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@192.168.70.104:1521:orcl";
		String user = "usci_01";// 用户名,系统默认的账户名
		String password = "Abcd123456";// 你安装时选设置的密码
		try {
			Class.forName(DRIVER);// 加载数据库驱动程序
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, user, password);// 获得Connection对象
			System.out.println("oracle 连接成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static Connection getConnection119(){
		Connection conn = null;
		String DRIVER="oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@192.168.70.105:1521:orcl";
		String user = "usci";// 用户名,系统默认的账户名
		String password = "usci";// 你安装时选设置的密码
		try {
			Class.forName(DRIVER);// 加载数据库驱动程序
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, user, password);// 获得Connection对象
			System.out.println("oracle 连接成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}




	public void insertData(String sql){
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void addBatch(String sql,List<List> value){
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for(int i=0;i<value.size();i++){
				for(int j=0;j<value.get(i).size();j++){
					ps.setObject(j+1, value.get(i).get(j));
				}
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
//	@Test
//	public void testInsertDate(){
//		new ConnOracle().insertData("insert into ainfo(id,username,password,name) values (1,'chenlisong','cls','陈礼松')");
//	}
	public void closeConn(){
		try {
			if(conn!=null){
				conn.close();
			}
			if(ps!=null){
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
