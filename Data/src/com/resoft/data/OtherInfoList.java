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
 * YzymeAlt類主要用於-.将编办等主要信息同步至前置机数据库中
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
public class OtherInfoList {

	public static List<List<Object>> tableInput() throws FileNotFoundException,SQLException {
		List<List<Object>> FindList = new ArrayList<List<Object>>();
		//连接oracle
		Connection con = ConnOracle.getConnection119();
		//连接sqlserver
		Connection conSqlserver = ConnSqlServer.getMysqlConnection();
		PreparedStatement pre = null;
		ResultSet resultSet = null;
		//获取Mysql中S_EXT_TIMESTAMP中最大的时间
		String mysql = "SELECT MAX(createtime) from t307024012900001054 where xydm like '5%'";
		pre = conSqlserver.prepareStatement(mysql);
		resultSet = pre.executeQuery();
		String maxDate = null;
		System.out.println("执行Mz");
		while(resultSet.next()){
			maxDate =  resultSet.getDate(1)+" "+resultSet.getTime(1) ;
		}
		String sql = "SELECT  t.SF_ID as guid, t.TYSHXYDM as xydm, t.JGMC as jgmc,a.RDDBRXM as fddbr,a.FDDBRZJHM as fddbr_zjhm, " +
				"t.ZCRQ as clrq,t.JGDZ as zcdz ,t.JYZT as zt ,t.ZCH as zch ,t.LAST_MODIFIED as hzrq, " +
				"t.JYFW as jyfw ,t.ZCZJ as zczj ,t.PZJGMC as djglbm ,t.JYQXS as yxq_ks,t.JYQXE as yxq_js," +
				" t.LXRYDDH as lxdh,t.LXRXM as lxr, t.JJLX_ID as jjlx,t.JGDZ as scjydz,t.XZQH as xzqh," +
				"t.LAST_MODIFIED as  createtime ,t.SF_ID as  gdtzr  from (SELECT row_number() over ( partition BY TYSHXYDM ORDER BY LAST_MODIFIED ) AS ids,SF_ID," +
				"TYSHXYDM,JGMC,ZCRQ,JGDZ,JYZT,ZCH,JYFW,ZCZJ,PZJGMC,JYQXS,JYQXE,LXRYDDH,LXRXM,JJLX_ID," +
				"XZQH,LAST_MODIFIED,JGDM from  OTHERS_INFO) t left join  " +
				"(select row_number() over ( partition BY JG_ID ORDER BY S_EXT_TIMESTAMP ) AS ids,JG_ID,RDDBRXM,FDDBRZJHM from OTHERS_FR_INFO )  a on  t.JGDM = a.JG_ID " +
				"where   t.ids = a.ids ";
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
					if( each == "clrq"||each == "createtime" ||each == "hzrq"||each == "yxq_ks"||each == "yxq_js"   ){
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
		    		.prepareStatement("update t307024012900001054 set guid = ?,xydm = ? ,jgmc = ? , fddbr= ? ,fddbr_zjhm = ? ," +
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
