package com.resoft.data;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * YzymeAlt類主要用於-.将个体主要信息同步至前置机数据库中
 * </p>
 * <p>
 * 創建時間 2018-10-29 - 下午12:00:07
 * </p>
 * <blockquote>
 * <h4>歷史修改記錄</h4>
 * <ul>
 * <li>修改人 修改時間 修改描述
 * </ul>
 * </blockquote>
 * <p>
 * copyright cdthgk 2010-2016, all rights reserved.
 * </p>
 *
 * @author 尹怡
 * @author cdthgk r&d
 * @since 1.0
 * @version 1.0
 */
public class GtInfoList {

	public static List<List<Object>> tableInput() throws FileNotFoundException,SQLException {
		List<List<Object>> FindList = new ArrayList<List<Object>>();
		//连接oracle
		Connection con = ConnOracle.getConnection();
		//连接sqlserver
		Connection conSqlserver = ConnSqlServer.getMysqlConnection();
		PreparedStatement pre = null;
		ResultSet resultSet = null;
		//获取Mysql中S_EXT_TIMESTAMP中最大的时间
//		String mysql = "SELECT MAX(createtime) from t307024012900001054 where xydm like '92%'";
//		pre = conSqlserver.prepareStatement(mysql);
//		resultSet = pre.executeQuery();
//		String maxDate = null;
//		System.out.println("执行GT");
//		while(resultSet.next()){
//			maxDate =  resultSet.getDate(1)+" "+resultSet.getTime(1) ;
//		}
		String sql = "SELECT  t.PEID as guid, t.UNISCID as xydm, t.TRANAME as jgmc,a.NAME as fddbr,a.CERNO as fddbr_zjhm, " +
				"t.ESTDATE as clrq,t.OPLOC as zcdz ,t.REGSTATE as zt ,t.REGNO as zch ,t.APPRDATE as hzrq, " +
				"t.OPSCOPE as jyfw ,t.FUNDAM as zczj ,t.REGORG as djglbm ,null as yxq_ks,null as yxq_js," +
				" t.TEL as lxdh,a.NAME as lxr, t.ENTTYPE as jjlx,t.PROLOC as scjydz,t.OPLOCDISTRICT as xzqh," +
				"t.S_EXT_TIMESTAMP as  createtime ,t.PEID as  gdtzr  from (SELECT row_number() over ( partition BY UNISCID ORDER BY S_EXT_TIMESTAMP ) AS ids,PEID," +
				"UNISCID,TRANAME,ESTDATE,OPLOC,REGSTATE,REGNO,APPRDATE,OPSCOPE,FUNDAM,REGORG,TEL,ENTTYPE," +
				"PROLOC,OPLOCDISTRICT,S_EXT_TIMESTAMP from  LZHY_E_GT_BASEINFO) t " +
				",(select row_number() over ( partition BY PEID ORDER BY S_EXT_TIMESTAMP ) AS ids,PEID,NAME,CERNO from LZHY_E_GT_OPERATOR )  a " +
				"where  t.PEID = a.PEID and t.ids = a.ids ";
//				"where t.S_EXT_TIMESTAMP > to_date('"+maxDate+"','yyyy-mm-dd hh24:mi:ss') and   ORDER BY t.S_EXT_TIMESTAMP";//获取数据列表
		try {
			pre = con.prepareStatement(sql);
			resultSet = pre.executeQuery();
			//字段名称数组
			String[] columu = {"guid","xydm",
								"jgmc",
								"fddbr",
								"fddbr_zjhm",
								"clrq",
								"zcdz",
								"zt",
								"zch",
								"hzrq",
								"jyfw",
								"zczj",
								"djglbm",
								"yxq_ks",
								"yxq_js",
								"lxdh",
								"lxr",
								"jjlx",
								"scjydz",
								"xzqh",
								"createtime",
								"gdtzr"
							};
			int i=0;
			while (resultSet.next()) {
				List<Object> minList = new ArrayList<Object>();
				for(String each:columu){
					if( each == "clrq"||each == "createtime" ||each == "hzrq"  ){
						minList.add(resultSet.getTimestamp(each));
					}else  {
						minList.add(resultSet.getString(each));
					}
				}
			FindList.add(minList);
			i++;
			if(i%1000==0){ //设置的每次提交大小为10000
				executeManySql(FindList);
				FindList.removeAll(FindList);
				System.out.println(i);
			}
		}
			if(FindList.size() != 0){
				executeManySql(FindList);//最后别忘了提交剩余的
			}
			return FindList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		try {
			pre.close();// 关闭Statement
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			con.close();// 关闭Connection
		} catch (SQLException e) {
			e.printStackTrace();
			}
		}
		return null;
	}
	public static void executeManySql(List<List<Object>> FindList) throws SQLException {
	    Connection con = ConnSqlServer.getMysqlConnection();
		PreparedStatement pre = null;
		ResultSet resultSet = null;
	    con.setAutoCommit(false);
	    PreparedStatement pstInsert = con
	            .prepareStatement("insert into t307024012900001054 (guid,xydm," +
	            		"jgmc, fddbr, fddbr_zjhm, clrq, zcdz, zt, zch, hzrq, jyfw, zczj, djglbm, yxq_ks, yxq_js, lxdh," +
	            		" lxr, jjlx, scjydz, xzqh, createtime, gdtzr) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    PreparedStatement pstUpdate = null ;
	    int updateNUm = 0;
	    int insertNum = 0;
	    for (List<Object> minList: FindList) {
	    	String etpsid = minList.get(0).toString() ;
	    	String sql1 = "select guid from t307024012900001054 where guid = '"+etpsid+"'";
	    	pre = con.prepareStatement(sql1);
	    	resultSet = pre.executeQuery();
	    	 pstUpdate = con
		    		.prepareStatement("update t307024012900001054 set guid= ?, xydm = ? , jgmc = ? ,fddbr= ? ,fddbr_zjhm = ? ," +
		    				"clrq = ? ,zcdz = ? ,zt = ? ,zch = ? ,hzrq = ? ,jyfw = ? ,zczj = ? ,djglbm = ? ," +
		    				"yxq_ks = ? ,yxq_js = ? ,lxdh = ? ,lxr = ? ,jjlx = ? ,scjydz = ? ,xzqh = ? ," +
		    				"createtime = ? ,gdtzr = ?      where guid = '"+etpsid+"'");
	    	if(resultSet.next()){
	    		 for(int i=0;i<minList.size();i++){
	 	        	//查询的数据为false的时候执行insert    当查询的数据为true的时候执行update
 	        		pstUpdate.setObject(i+1, minList.get(i));
	    		 }
	    		 pstUpdate.executeUpdate();
	    		 updateNUm++;
	    	}else{
	    		 for(int i=0;i<minList.size();i++){
	        		pstInsert.setObject(i+1, minList.get(i));
		        }
	    		 pstInsert.addBatch();
	    		 insertNum++;

	    	}
	        // 把一个SQL命令加入命令列表
	    }
	    // 执行批量更新

	    System.out.println("更新条数:  "+updateNUm+"   新增条数: "+insertNum);
//	    if(updateNUm !=  0 ){
//	    	pstUpdate.executeBatch();
//	    }
	    if ( insertNum != 0 ){
	    	pstInsert.executeBatch();
	    }
	    // 语句执行完毕，提交本事务
	    con.commit();
	    pstUpdate.close();
	    pstInsert.close();
	    con.close();//一定要记住关闭连接，不然mysql回应为too many connection自我保护而断开。
	}



}
