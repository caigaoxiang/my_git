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
 * YzymeAlt類主要用於-.主要用于执行编办165上面53/54民政数据,将其写入119oracle数据库中执行同步更新
 * </p>
 * <p>
 * 創建時間 2018-8-29 - 下午12:00:07
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
public class Bb165ToMz119 {

	public static List<List<String>> tableInput() throws FileNotFoundException,SQLException {
		List<List<String>> FindList = new ArrayList<List<String>>();
		//连接oracle
		Connection con = ConnOracle.getConnection119();
		//连接sqlserver
		Connection conSqlserver = ConnSqlServer.getConnection();
		PreparedStatement pre = null;
		ResultSet resultSet = null;
		//获取sqlserver中S_EXT_TIMESTAMP中最大的时间
		String sqlServer = "SELECT MAX(LASTDATE) from MZ_INFO where (TYSHXYDM like '55%' or TYSHXYDM like '54%' )";//根据sqlserver更新时间进行判断数据
		pre = con.prepareStatement(sqlServer);
		resultSet = pre.executeQuery();
		System.out.println("执行DB_UCARCHIVETo119");
		/*String maxDate = null;
		while(resultSet.next()){
			maxDate =  resultSet.getDate(1)+" "+resultSet.getTime(1) ;
		}
		System.out.println(maxDate);*/
		/*String sql = "SELECT t.*   from dbo.DB_UCARCHIVE t " +
				"where convert(datetime,t.IMPORTDATE+' '+t.IMPORTTIME) > convert(datetime,'"+maxDate+"')   and (TYSHXYDM like '55%' or TYSHXYDM like '54%' ) ORDER BY t.LAST_MODIFIED";//获取数据列表
*///		String sql = "SELECT t.*   from dbo.DB_UCARCHIVE t " +
//				"where   (TYSHXYDM like '55%' or TYSHXYDM like '54%' ) ORDER BY t.LAST_MODIFIED";//获取数据列表
		String sql = "SELECT t.*   from dbo.DB_UCARCHIVE t " +
				"where convert(datetime,t.IMPORTDATE+' '+t.IMPORTTIME) > convert(datetime,'2019-10-01 00:00:00')   and (TYSHXYDM like '55%' or TYSHXYDM like '54%' ) ORDER BY t.LAST_MODIFIED";//获取数据列表
		try {
			pre = conSqlserver.prepareStatement(sql);
			resultSet = pre.executeQuery();
			//字段名称数组
			String[] columu = {"ARCH_ID",
								"JJHY_ID",
								"JJLX_ID",
								"XZQH_ID",
								"JGDM",
								"JGMC",
								"JGLX",
								"FDDBR",
								"JJHY",
								"JJLX",
								"ZCRQ",
								"ZGJGDM",
								"PZJGDM",
								"XZQH",
								"JGDZ",
								"YZBM",
								"DHHM",
								"BZRQ",
								"BZJGDM",
								"ZGRS",
								"NJRQ",
								"ZCH",
								"QZBZ",
								"ZGJGMC",
								"PZJGMC",
								"PZWH",
								"PZRQ",
								"ISPUBLIC",
								"MEMO",
								"VERSION",
								"ARCH_GUID",
								"CREATEDATE",
								"INPUTDATE",
								"ARCHKIND_ID",
								"ARCH_PAGES",
								"ARCH_FILENAME",
								"EXPORTTIME",
								"URL",
								"IMPORTDATE",
								"IMPORTTIME",
								"CENTER_ID",
								"ISDEL",
								"MEMO1",
								"MEMO2",
								"MEMO3",
								"IMPORTSYSTEMDATE",
								"IMPORTSYSTEMTIME",
								"ISCHECK",
								"ZFRQ",
								"CFBJ",
								"SAMPLEFLAG",
								"CHECKDATE",
								"XIAOWEI",
								"MEMO5",
								"JJHY2011",
								"JJLX2011",
								"NJJHY",
								"NJJLX",
								"NJGLX",
								"LAST_MODIFIED",
								"YWLSH",
								"TYSHXYDM",
								"FRZJLX",
								"FRZJHM",
								"JYDZ",
								"JYDZXZQH",
								"JYFW",
								"JYQXS",
								"JYQXE",
								"ZCZJ",
								"WSTZGB",
								"HBZL",
								"WZ",
								"DZYX",
								"JBRXM",
								"JBRZJHM",
								"JBRLXDH",
								"FRZW",
								"FRYDDH",
								"FRGDDH",
								"FRDZYX",
								"FRSLFS",
								"CWXM",
								"CWYDDH",
								"CWGDDH",
								"CWDZYX",
								"CWZHLX",
								"CWZJHM",
								"LXRXM",
								"LXRYDDH",
								"LXRGDDH",
								"LXRDZYX",
								"LXRZJLX",
								"LXRZJHM",
								"ISUNIFIED",
								"ISUUPLOAD",
								"FLAG1",
								"FLAG2",
								"FLAG3",
								"DATETIME1",
								"DATETIME2",
								"DATETIME3",
								"MEMO6",
								"MEMO7",
								"MEMO8",
								"MEMO9",
								"MEMO10",
								"QYLX",
								"CLRQ",
								"HZRQ",
								"JYZT",
								"USPAN",
								"CODEPASS",
								"JGDM8",
								"TYSHXYDM17",
								"EFLAG",
								"ENUM",
								"DFLAG",
								"PRIPID",
								"HSFS",
								"GSDJZH",
								"DSDJZH",
								"YWLX",
								"DPTCODE",
								"CHECKFLAG"
							};
			int i=0;
			while (resultSet.next()) {
				List<String> minList = new ArrayList<String>();
				for(String each:columu){
//					if( each == "LAST_MODIFIED12"  ){
//						minList.add(resultSet.getTimestamp(each));
//					} else  {
						minList.add(resultSet.getString(each));
//					}
				}
			FindList.add(minList);
			i++;
			if(i%500==0){ //设置的每次提交大小为10000
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
			conSqlserver.close();// 关闭Connection
		} catch (SQLException e) {
			e.printStackTrace();
			}
		}
		return null;
	}
	public static void executeManySql(List<List<String>> findList) throws SQLException {
//	    Connection con = ConnSqlServer.getConnection();
	    Connection conOracle = ConnOracle.getConnection119();//oracle
		PreparedStatement pre = null;
		ResultSet resultSet = null;
		conOracle.setAutoCommit(false);
//	    Statement stat = null;
//23  31 50 69 70 79 80
	    PreparedStatement pstInsert = conOracle
	            .prepareStatement("insert into MZ_INFO  values (?,?,?,?,?,?,?,?,to_date(?,'yyyy-MM-dd hh24:mi:ss'),?," +
	            		"?,?,?,?,?,to_date(?,'yyyy-MM-dd hh24:mi:ss'),to_date(?,'yyyy-MM-dd hh24:mi:ss'),to_date(?,'yyyy-MM-dd hh24:mi:ss'),?,to_number(?)," +
	            		"?,to_number(?),to_date(?,'yyyy-MM-dd hh24:mi:ss'),?,to_date(?,'yyyy-MM-dd hh24:mi:ss'),?,?,?,?,?," +
	            		"to_date(?,'yyyy-MM-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,?," +
	            		"?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-MM-dd hh24:mi:ss')," +
	            		"?,?,?,?,?,?,?,?,?,?," +
	            		"?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-MM-dd hh24:mi:ss')," +
	            		"?,?,?,?,?,?,?,?,to_date(?,'yyyy-MM-dd hh24:mi:ss'),to_date(?,'yyyy-MM-dd hh24:mi:ss'),?)");
	    PreparedStatement pstInsertMzFRINFO = conOracle
	    		.prepareStatement("insert into FR_INFO  values (?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-MM-dd hh24:mi:ss'),?,to_date(?,'yyyy-MM-dd hh24:mi:ss'),to_date(?,'yyyy-MM-dd hh24:mi:ss'),to_date(?,'yyyy-MM-dd hh24:mi:ss'))");
	    PreparedStatement pstInsertMzBG = conOracle
	    		.prepareStatement("insert into MZ_BIANGENG_JILU  values (?,?,?,?,?,?,?,to_date(?,'yyyy-MM-dd hh24:mi:ss'),to_date(?,'yyyy-MM-dd hh24:mi:ss'))");



	    PreparedStatement pstUpdateMzinfo = null ;
	    PreparedStatement pstUpdateMzFr = null ;
	    int updateNUm = 0;
	    int insertNum = 0;
	    int insertBG = 0;
	    for (List<String> minList: findList) {
	    	String etpsid = minList.get(0).toString() ;
	    	String sql1 = "select MZ_ID from MZ_INFO where MZ_ID = '"+etpsid+"'";
	    	pre = conOracle.prepareStatement(sql1);
	    	resultSet = pre.executeQuery();
	    	 pstUpdateMzinfo = conOracle
		    		.prepareStatement("update MZ_INFO set MZ_ID = ? ,JGDM = ? ,JGMC = ? ,JGLX = ? ,FDDBR = ? ,JYFW = ? ," +
		    				"JJHY2011 = ? ,JJLX2011 = ? ,ZCRQ = to_date(?,'yyyy-MM-dd hh24:mi:ss') ,ZGDM = ? ,PZJGDM  = ? ,XZQH = ? ,JGDZ = ? ,YZBM = ? ,DHHM = ? ," +
		    				"BZRQ = to_date(?,'yyyy-MM-dd hh24:mi:ss') ,YXQQS = to_date(?,'yyyy-MM-dd hh24:mi:ss') ,YXQJZ = to_date(?,'yyyy-MM-dd hh24:mi:ss') ,BZJGDM  = ? ,ZCZJ = to_number(?) ,HBZL = ? ,ZGRS = ? ,BGRQ = to_date(?,'yyyy-MM-dd hh24:mi:ss') ,LRY  = ? ," +
		    				"NJRQ  =to_date(?,'yyyy-MM-dd hh24:mi:ss') ,ZCH  = ? ,ZGMC  = ? ,PZJGMC = ? ,EMAIL  = ? ,URL    = ? ,LASTDATE = to_date(?,'yyyy-MM-dd hh24:mi:ss') ,TBRXM    = ? ," +
		    				"TBRSFZH  = ? ,TBRLXFS  = ? ,JYDZ     = ? ,JYZT     = ? ,SCJYXZQH = ? ,TBRZJLX  = ? ,TYSHXYDM = ? ," +
		    				"YWLX  = ? ,HSFS  = ? ,JHBZ  = ? ,DJXS  = ? ,FZXS  = ? ,HDDY  = ? ,JJHLX  = ? ,DYBZ   = ? ,DWHYSL = ? ," +
		    				"GRHYSL = ? ,YXQXS  = to_date(?,'yyyy-MM-dd hh24:mi:ss') ,FRDHHM = ? ,ZZRYSL = ? ,JZRYSL = ? ,LSSL   = ? ,JSSL   = ? ,CWLSSL = ? ," +
		    				"TBRMOBILE  = ? ,KHYH   = ? ,KYZH   = ? ,ISDANG = ? ,DZZ_ID = ? ,WJLYY  = ? ,MEMO   = ? ,MEMO1  = ? ," +
		    				"MEMO2  = ? ,MEMO3  = ? ,MEMO4  = ? ,MEMO5  = ? ,BZRQ1  = ? ,CREATEDATE = to_date(?,'yyyy-MM-dd hh24:mi:ss') ,CISHAN = ? ,MUJUAN = ? ," +
		    				"TUOGOU = ? ,ZJDJLX = ? ,AREA   = ? ,DATASTUTS = ? ,SJLY   = ? ,SFSBBS  = ? ,CREATE_TIME  = to_date(?,'yyyy-MM-dd hh24:mi:ss') ," +
		    				"MODIFY_TIME  = to_date(?,'yyyy-MM-dd hh24:mi:ss') ,SJFLBS  = ?    where MZ_ID = '"+etpsid+"'");
	    	 pstUpdateMzFr = conOracle
			    		.prepareStatement("update FR_INFO set FR_ID = ?,ED_ID = ?,JG_ID = ?,RDDBRXM = ?,FDDBRZJLX = ?,FDDBRZJHM = ?," +
			    				"ADDRESS = ?,DHHM = ?,XB = ?,CSRQ = to_date(?,'yyyy-MM-dd hh24:mi:ss'), CSDQDM= ?,S_EXT_TIMESTAMP= to_date(?,'yyyy-MM-dd hh24:mi:ss'),CREATE_TIME= to_date(?,'yyyy-MM-dd hh24:mi:ss')," +
			    				"MODIFY_TIME= to_date(?,'yyyy-MM-dd hh24:mi:ss')    where FR_ID = '"+etpsid+"'");
	    	 //判断主表中是否存在ID,存在执行更新操作,不存在进行插入操作
	    	if(resultSet.next()){
	    		String sqlFR = "select RDDBRXM,FDDBRZJHM from FR_INFO where FR_ID = '"+etpsid+"'";
	    		pre = conOracle.prepareStatement(sqlFR);
	    		resultSet = pre.executeQuery();
	    		String fr = null;
	    		String frzjhm = null;
	    		while(resultSet.next()){
	    			fr =  resultSet.getString("RDDBRXM");
	    			frzjhm =  resultSet.getString("FDDBRZJHM");
	    		}
	    		String sqlmz = "select JGMC,TYSHXYDM,JYFW,JGDZ,JYDZ,JYZT from MZ_INFO where MZ_ID = '"+etpsid+"'";
	    		pre = conOracle.prepareStatement(sqlmz);
	    		resultSet = pre.executeQuery();
	    		String JGMC = null;
	    		String TYSHXYDM = null;
	    		String JYFW = null;
	    		String JGDZ = null;//地址
	    		String JYDZ = null;//地址
	    		String JYZT = null;//其他
	    		while(resultSet.next()){
	    			JGMC =  resultSet.getString("JGMC");
	    			TYSHXYDM =  resultSet.getString("TYSHXYDM");
	    			JYFW =  resultSet.getString("JYFW");
	    			JGDZ =  resultSet.getString("JGDZ");
	    			JYDZ =  resultSet.getString("JYDZ");
	    			JYZT =  resultSet.getString("JYZT");
	    		}
	    		String  LASTDATE = minList.get(38)+" "+minList.get(39);
	    		String laTime = minList.get(59).substring(0, 19);
	    		//substr(INVOICE_DATE,1,10)
	 	        	//若根据单表拆分为多表时候,需根据相对应的序列号进行操作设置.
	    			 	pstUpdateMzinfo.setObject(1, minList.get(0));   ///MZ_ID
	    			 	pstUpdateMzinfo.setObject(2, minList.get(4));   ///JGDM
		    			pstUpdateMzinfo.setObject(3, minList.get(5));   ///JGMC
	 	        		pstUpdateMzinfo.setObject(4, minList.get(55));   ///JGLX
	 	        		pstUpdateMzinfo.setObject(5, minList.get(0));   ///FDDBR
	 	        		pstUpdateMzinfo.setObject(6, minList.get(66));   ///JYFW
	 	        		pstUpdateMzinfo.setObject(7, minList.get(54));   ///JJHY2011
	 	        		pstUpdateMzinfo.setObject(8, minList.get(55));   ///JJLX2011
	 	        		pstUpdateMzinfo.setObject(9, minList.get(10));   ///ZCRQ
	 	        		pstUpdateMzinfo.setObject(10, minList.get(11));  ///ZGDM
	 	        		pstUpdateMzinfo.setObject(11, minList.get(12));  ///PZJGDM
	 	        		pstUpdateMzinfo.setObject(12, minList.get(13));  ///XZQH
	 	        		pstUpdateMzinfo.setObject(13, minList.get(14));  ///JGDZ
	 	        		pstUpdateMzinfo.setObject(14, minList.get(15));  ///YZBM
	 	        		pstUpdateMzinfo.setObject(15, minList.get(16));  ///DHHM
	 	        		pstUpdateMzinfo.setObject(16, minList.get(17));  ///BZRQ
	 	        		pstUpdateMzinfo.setObject(17, minList.get(67));  ///YXQQS
	 	        		pstUpdateMzinfo.setObject(18, minList.get(68));  ///YXQJZ
	 	        		pstUpdateMzinfo.setObject(19, minList.get(15));  ///BZJGDM
	 	        		pstUpdateMzinfo.setObject(20, minList.get(69));  ///ZCZJ
	 	        		pstUpdateMzinfo.setObject(21, minList.get(71));  ///HBZL
	 	        		pstUpdateMzinfo.setObject(22, minList.get(19));  ///ZGRS
	 	        		pstUpdateMzinfo.setObject(23, laTime);  ///BGRQ
	 	        		pstUpdateMzinfo.setObject(24, minList.get(74));  ///LRY
	 	        		pstUpdateMzinfo.setObject(25, minList.get(109));  ///NJRQ
	 	        		pstUpdateMzinfo.setObject(26, minList.get(21));  ///ZCH
	 	        		pstUpdateMzinfo.setObject(27, minList.get(11));  ///ZGMC
	 	        		pstUpdateMzinfo.setObject(28, minList.get(23));  ///PZJGMC
	 	        		pstUpdateMzinfo.setObject(29, minList.get(73));  ///EMAIL
	 	        		pstUpdateMzinfo.setObject(30, minList.get(72));  ///URL
	 	        		pstUpdateMzinfo.setObject(31, LASTDATE);  ///LASTDATE  定义为最后更新时间
	 	        		pstUpdateMzinfo.setObject(32, minList.get(74));  ///TBRXM
	 	        		pstUpdateMzinfo.setObject(33, minList.get(75));  ///TBRSFZH
	 	        		pstUpdateMzinfo.setObject(34, minList.get(76));  ///TBRLXFS
	 	        		pstUpdateMzinfo.setObject(35, minList.get(64));  ///JYDZ
	 	        		pstUpdateMzinfo.setObject(36, minList.get(95));  ///JYZT
	 	        		pstUpdateMzinfo.setObject(37, minList.get(65));  ///SCJYXZQH
	 	        		pstUpdateMzinfo.setObject(38, minList.get(62));  ///TBRZJLX
	 	        		pstUpdateMzinfo.setObject(39, minList.get(61));  ///TYSHXYDM
	 	        		pstUpdateMzinfo.setObject(40, minList.get(107));  ///YWLX
	 	        		pstUpdateMzinfo.setObject(41, minList.get(119));  ///HSFS
	 	        		pstUpdateMzinfo.setObject(42, null);  ///JHBZ
	 	        		pstUpdateMzinfo.setObject(43, null);  ///DJXS
	 	        		pstUpdateMzinfo.setObject(44, null);  ///FZXS
	 	        		pstUpdateMzinfo.setObject(45, minList.get(14));  ///HDDY
	 	        		pstUpdateMzinfo.setObject(46, minList.get(9));  ///JJHLX
	 	        		pstUpdateMzinfo.setObject(47, null);  ///DYBZ
	 	        		pstUpdateMzinfo.setObject(48, null);  ///DWHYSL
	 	        		pstUpdateMzinfo.setObject(49, null);  ///GRHYSL
	 	        		pstUpdateMzinfo.setObject(50, minList.get(67));  ///YXQXS
	 	        		pstUpdateMzinfo.setObject(51, minList.get(79));  ///FRDHHM
	 	        		pstUpdateMzinfo.setObject(52, null);  ///ZZRYSL
	 	        		pstUpdateMzinfo.setObject(53, null);  ///JZRYSL
	 	        		pstUpdateMzinfo.setObject(54, null);  ///LSSL
	 	        		pstUpdateMzinfo.setObject(55, null);  ///JSSL
	 	        		pstUpdateMzinfo.setObject(56, null);  ///CWLSSL
	 	        		pstUpdateMzinfo.setObject(57, minList.get(76));  ///TBRMOBILE
	 	        		pstUpdateMzinfo.setObject(58, null);  ///KHYH
	 	        		pstUpdateMzinfo.setObject(59, null);  ///KYZH
	 	        		pstUpdateMzinfo.setObject(60, null);  ///ISDANG
	 	        		pstUpdateMzinfo.setObject(61, null);  ///DZZ_ID
	 	        		pstUpdateMzinfo.setObject(62, null);  ///WJLYY
	 	        		pstUpdateMzinfo.setObject(63, minList.get(28));  ///MEMO
	 	        		pstUpdateMzinfo.setObject(64, minList.get(42));  ///MEMO1
	 	        		pstUpdateMzinfo.setObject(65, minList.get(43));  ///MEMO2
	 	        		pstUpdateMzinfo.setObject(66, minList.get(44));  ///MEMO3
	 	        		pstUpdateMzinfo.setObject(67, minList.get(102));  ///MEMO4
	 	        		pstUpdateMzinfo.setObject(68, minList.get(103));  ///MEMO5
	 	        		pstUpdateMzinfo.setObject(69, minList.get(17));  ///BZRQ1
	 	        		pstUpdateMzinfo.setObject(70, minList.get(10));  ///CREATEDATE
	 	        		pstUpdateMzinfo.setObject(71, null);  ///CISHAN
	 	        		pstUpdateMzinfo.setObject(72, null);  ///MUJUAN
	 	        		pstUpdateMzinfo.setObject(73, null);  ///TUOGOU
	 	        		pstUpdateMzinfo.setObject(74, minList.get(107));  ///ZJDJLX
	 	        		pstUpdateMzinfo.setObject(75, minList.get(11));  ///AREA
	 	        		pstUpdateMzinfo.setObject(76, minList.get(110));  ///DATASTUTS
	 	        		pstUpdateMzinfo.setObject(77, "165库更新");  ///SJLY
	 	        		pstUpdateMzinfo.setObject(78, minList.get(124));  ///SFSBBS
	 	        		pstUpdateMzinfo.setObject(79, minList.get(10));  ///CREATE_TIME
	 	        		pstUpdateMzinfo.setObject(80, laTime);  ///MODIFY_TIME
	 	        		pstUpdateMzinfo.setObject(81, minList.get(117));  ///SJFLBS
	 	        		//民政法人信息写入
	 	        		pstUpdateMzFr.setObject(1, minList.get(0));    //FR_ID
	 	        		pstUpdateMzFr.setObject(2, null);    //ED_ID
	 	        		pstUpdateMzFr.setObject(3, minList.get(0));    //JG_ID
	 	        		pstUpdateMzFr.setObject(4, minList.get(7));    //RDDBRXM
	 	        		pstUpdateMzFr.setObject(5, minList.get(62));    //FDDBRZJLX
	 	        		pstUpdateMzFr.setObject(6, minList.get(63));    //FDDBRZJHM
	 	        		pstUpdateMzFr.setObject(7, null);    //ADDRESS
	 	        		pstUpdateMzFr.setObject(8, minList.get(16));    //DHHM
	 	        		pstUpdateMzFr.setObject(9, null);    //XB
	 	        		pstUpdateMzFr.setObject(10, null);   //CSRQ
	 	        		pstUpdateMzFr.setObject(11, null);   //CSDQDM
	 	        		pstUpdateMzFr.setObject(12, LASTDATE);   //S_EXT_TIMESTAMP
	 	        		pstUpdateMzFr.setObject(13, minList.get(10));   //CREATE_TIME
	 	        		pstUpdateMzFr.setObject(14, laTime);   //MODIFY_TIME
	 	        		/**
						 * 1、判断最后修改时间和库中修改时间是否一致.
						 *
						 * JGMC  TYSHXYDM JYFW JGDZ JYDZ JYZT
						 */
	 	        		if(fr !=  minList.get(7)|| frzjhm != minList.get(63) ){
	 	        			pstInsertMzBG.setObject(1, RandomUtils.generateString(50));  //ALTID
	 	        			pstInsertMzBG.setObject(2, minList.get(0));  //MZ_ID
	 	        			pstInsertMzBG.setObject(3, minList.get(21));  //REGNO
	 	        			pstInsertMzBG.setObject(4, minList.get(61));  //UNISCID
	 	        			pstInsertMzBG.setObject(5, "1");  //ALTITEM
	 	        			pstInsertMzBG.setObject(6, "法人:"+fr +" 证件号码:"+frzjhm);  //ALTBE
	 	        			pstInsertMzBG.setObject(7,"法人:"+minList.get(7) +" 证件号码:"+minList.get(63));  //ALTAF
	 	        			pstInsertMzBG.setObject(8, minList.get(26));  //ALTDATE
	 	        			pstInsertMzBG.setObject(9, LASTDATE);  //S_EXT_TIMESTAMP
	 	        			insertBG++;
	 	        		}
	 	        		if(JGMC !=  minList.get(5)  ){
	 	        			pstInsertMzBG.setObject(1,RandomUtils.generateString(50));  //ALTID
	 	        			pstInsertMzBG.setObject(2, minList.get(0));  //MZ_ID
	 	        			pstInsertMzBG.setObject(3, minList.get(21));  //REGNO
	 	        			pstInsertMzBG.setObject(4, minList.get(61));  //UNISCID
	 	        			pstInsertMzBG.setObject(5, "5");  //ALTITEM
	 	        			pstInsertMzBG.setObject(6, "机构名称: "+JGMC  );  //ALTBE
	 	        			pstInsertMzBG.setObject(7,"机构名称: "+minList.get(5)  );  //ALTAF
	 	        			pstInsertMzBG.setObject(8, minList.get(26));  //ALTDATE
	 	        			pstInsertMzBG.setObject(9, LASTDATE);  //S_EXT_TIMESTAMP
	 	        			insertBG++;
	 	        		}
	 	        		if(TYSHXYDM !=  minList.get(61)  ){
	 	        			pstInsertMzBG.setObject(1, RandomUtils.generateString(50));  //ALTID
	 	        			pstInsertMzBG.setObject(2, minList.get(0));  //MZ_ID
	 	        			pstInsertMzBG.setObject(3, minList.get(21));  //REGNO
	 	        			pstInsertMzBG.setObject(4, minList.get(61));  //UNISCID
	 	        			pstInsertMzBG.setObject(5, "4");  //ALTITEM
	 	        			pstInsertMzBG.setObject(6, "统一社会信用代码: "+JGMC  );  //ALTBE
	 	        			pstInsertMzBG.setObject(7,"统一社会信用代码: "+minList.get(61)  );  //ALTAF
	 	        			pstInsertMzBG.setObject(8, minList.get(26));  //ALTDATE
	 	        			pstInsertMzBG.setObject(9, LASTDATE);  //S_EXT_TIMESTAMP
	 	        			insertBG++;
	 	        		}
	 	        		if(JYFW !=  minList.get(66)  ){
	 	        			pstInsertMzBG.setObject(1, RandomUtils.generateString(50));  //ALTID
	 	        			pstInsertMzBG.setObject(2, minList.get(0));  //MZ_ID
	 	        			pstInsertMzBG.setObject(3, minList.get(21));  //REGNO
	 	        			pstInsertMzBG.setObject(4, minList.get(61));  //UNISCID
	 	        			pstInsertMzBG.setObject(5, "6");  //ALTITEM
	 	        			pstInsertMzBG.setObject(6, "经营范围: "+JGMC  );  //ALTBE
	 	        			pstInsertMzBG.setObject(7,"经营范围: "+minList.get(66)  );  //ALTAF
	 	        			pstInsertMzBG.setObject(8, minList.get(26));  //ALTDATE
	 	        			pstInsertMzBG.setObject(9, LASTDATE);  //S_EXT_TIMESTAMP
	 	        			insertBG++;
	 	        		}//JGDZ JYDZ
	 	        		if(JGDZ !=  minList.get(14) || JYDZ !=  minList.get(64)){
	 	        			pstInsertMzBG.setObject(1, RandomUtils.generateString(50));  //ALTID
	 	        			pstInsertMzBG.setObject(2, minList.get(0));  //MZ_ID
	 	        			pstInsertMzBG.setObject(3, minList.get(21));  //REGNO
	 	        			pstInsertMzBG.setObject(4, minList.get(61));  //UNISCID
	 	        			pstInsertMzBG.setObject(5, "7");  //ALTITEM
	 	        			pstInsertMzBG.setObject(6, "机构地址:"+JGDZ +" 经营地址:"+JYDZ );  //ALTBE
	 	        			pstInsertMzBG.setObject(7,"机构地址: "+minList.get(14)+" 经营地址:"+ minList.get(64) );  //ALTAF
	 	        			pstInsertMzBG.setObject(8, minList.get(26));  //ALTDATE
	 	        			pstInsertMzBG.setObject(9, LASTDATE);  //S_EXT_TIMESTAMP
	 	        			insertBG++;
	 	        		}
	 	        		if(JYZT !=  minList.get(95)  ){
	 	        			pstInsertMzBG.setObject(1, RandomUtils.generateString(50));  //ALTID
	 	        			pstInsertMzBG.setObject(2, minList.get(0));  //MZ_ID
	 	        			pstInsertMzBG.setObject(3, minList.get(21));  //REGNO
	 	        			pstInsertMzBG.setObject(4, minList.get(61));  //UNISCID
	 	        			pstInsertMzBG.setObject(5, "9");  //ALTITEM
	 	        			pstInsertMzBG.setObject(6, "状态改变:"+JYZT   );  //ALTBE
	 	        			pstInsertMzBG.setObject(7,"状态改变: "+minList.get(95) );  //ALTAF
	 	        			pstInsertMzBG.setObject(8, minList.get(26));  //ALTDATE
	 	        			pstInsertMzBG.setObject(9, LASTDATE);  //S_EXT_TIMESTAMP
	 	        			insertBG++;
	 	        		}
	    		 pstUpdateMzFr.executeUpdate();
	    		 pstUpdateMzinfo.executeUpdate();
	    		 pstInsertMzBG.addBatch();
	    		 updateNUm++;
	    	}else{
	    		String  LASTDATE = minList.get(38)+" "+minList.get(39);
	    		String laTime = minList.get(59).substring(0, 19);
	    		 ///for(int i=0;i<minList.size();i++){
	    			pstInsert.setObject(1, minList.get(0));   ///MZ_ID
	    			pstInsert.setObject(2, minList.get(4));   ///JGDM
	    			pstInsert.setObject(3, minList.get(5));   ///JGMC
 	        		pstInsert.setObject(4, minList.get(55));   ///JGLX
 	        		pstInsert.setObject(5, minList.get(0));   ///FDDBR
 	        		pstInsert.setObject(6, minList.get(66));   ///JYFW
 	        		pstInsert.setObject(7, minList.get(54));   ///JJHY2011
 	        		pstInsert.setObject(8, minList.get(55));   ///JJLX2011
 	        		pstInsert.setObject(9, minList.get(10) == "" ? null : minList.get(10) );   ///ZCRQ
 	        		pstInsert.setObject(10, minList.get(11));  ///ZGDM
 	        		pstInsert.setObject(11, minList.get(12));  ///PZJGDM
 	        		pstInsert.setObject(12, minList.get(13));  ///XZQH
 	        		pstInsert.setObject(13, minList.get(14));  ///JGDZ
 	        		pstInsert.setObject(14, minList.get(15));  ///YZBM
 	        		pstInsert.setObject(15, minList.get(16));  ///DHHM
 	        		pstInsert.setObject(16, minList.get(17)== "" ? null : minList.get(17));  ///BZRQ
 	        		pstInsert.setObject(17, minList.get(67)== "" ? null : minList.get(67));  ///YXQQS
 	        		pstInsert.setObject(18, minList.get(68)== "" ? null : minList.get(68));  ///YXQJZ
 	        		pstInsert.setObject(19, minList.get(15));  ///BZJGDM
 	        		pstInsert.setObject(20, minList.get(69));  ///ZCZJ
 	        		pstInsert.setObject(21, minList.get(71));  ///HBZL
 	        		pstInsert.setObject(22, minList.get(19));  ///ZGRS
 	        		pstInsert.setObject(23, laTime== "" ? null : laTime);  ///BGRQ
 	        		pstInsert.setObject(24, minList.get(74));  ///LRY
 	        		pstInsert.setObject(25, minList.get(109)== "" ? null :minList.get(109));  ///NJRQ
 	        		pstInsert.setObject(26, minList.get(21));  ///ZCH
 	        		pstInsert.setObject(27, minList.get(11));  ///ZGMC
 	        		pstInsert.setObject(28, minList.get(23));  ///PZJGMC
 	        		pstInsert.setObject(29, minList.get(73));  ///EMAIL
 	        		pstInsert.setObject(30, minList.get(72));  ///URL
 	        		pstInsert.setObject(31, LASTDATE == " " ? null : laTime);  ///LASTDATE  定义为最后更新时间
 	        		pstInsert.setObject(32, minList.get(74));  ///TBRXM
 	        		pstInsert.setObject(33, minList.get(75));  ///TBRSFZH
 	        		pstInsert.setObject(34, minList.get(76));  ///TBRLXFS
 	        		pstInsert.setObject(35, minList.get(64));  ///JYDZ
 	        		pstInsert.setObject(36, minList.get(95));  ///JYZT
 	        		pstInsert.setObject(37, minList.get(65));  ///SCJYXZQH
 	        		pstInsert.setObject(38, minList.get(62));  ///TBRZJLX
 	        		pstInsert.setObject(39, minList.get(61));  ///TYSHXYDM
 	        		pstInsert.setObject(40, minList.get(107));  ///YWLX
 	        		pstInsert.setObject(41, minList.get(119));  ///HSFS
 	        		pstInsert.setObject(42, null);  ///JHBZ
 	        		pstInsert.setObject(43, null);  ///DJXS
 	        		pstInsert.setObject(44, null);  ///FZXS
 	        		pstInsert.setObject(45, minList.get(14));  ///HDDY
 	        		pstInsert.setObject(46, minList.get(9));  ///JJHLX
 	        		pstInsert.setObject(47, null);  ///DYBZ
 	        		pstInsert.setObject(48, null);  ///DWHYSL
 	        		pstInsert.setObject(49, null);  ///GRHYSL
 	        		pstInsert.setObject(50, minList.get(67) == "" ? null : minList.get(67) );  ///YXQXS
 	        		pstInsert.setObject(51, minList.get(79));  ///FRDHHM
 	        		pstInsert.setObject(52, null);  ///ZZRYSL
 	        		pstInsert.setObject(53, null);  ///JZRYSL
 	        		pstInsert.setObject(54, null);  ///LSSL
 	        		pstInsert.setObject(55, null);  ///JSSL
 	        		pstInsert.setObject(56, null);  ///CWLSSL
 	        		pstInsert.setObject(57, minList.get(76));  ///TBRMOBILE
 	        		pstInsert.setObject(58, null);  ///KHYH
 	        		pstInsert.setObject(59, null);  ///KYZH
 	        		pstInsert.setObject(60, null);  ///ISDANG
 	        		pstInsert.setObject(61, null);  ///DZZ_ID
 	        		pstInsert.setObject(62, null);  ///WJLYY
 	        		pstInsert.setObject(63, minList.get(28));  ///MEMO
 	        		pstInsert.setObject(64, minList.get(42));  ///MEMO1
 	        		pstInsert.setObject(65, minList.get(43));  ///MEMO2
 	        		pstInsert.setObject(66, minList.get(44));  ///MEMO3
 	        		pstInsert.setObject(67, minList.get(102));  ///MEMO4
 	        		pstInsert.setObject(68, minList.get(103));  ///MEMO5
 	        		pstInsert.setObject(69, minList.get(17));  ///BZRQ1
 	        		pstInsert.setObject(70, minList.get(10) == "" ? null : minList.get(10) );  ///CREATEDATE
 	        		pstInsert.setObject(71, null);  ///CISHAN
 	        		pstInsert.setObject(72, null);  ///MUJUAN
 	        		pstInsert.setObject(73, null);  ///TUOGOU
 	        		pstInsert.setObject(74, minList.get(107));  ///ZJDJLX
 	        		pstInsert.setObject(75, minList.get(11));  ///AREA
 	        		pstInsert.setObject(76, minList.get(110));  ///DATASTUTS
 	        		pstInsert.setObject(77, "165库");  ///SJLY
 	        		pstInsert.setObject(78, minList.get(124));  ///SFSBBS
 	        		pstInsert.setObject(79, minList.get(10) == "" ? null : minList.get(10));  ///CREATE_TIME
 	        		pstInsert.setObject(80, laTime == "" ? null :laTime);  ///MODIFY_TIME
 	        		pstInsert.setObject(81, minList.get(117));  ///SJFLBS
 	        		//民政法人信息写入
 	        		pstInsertMzFRINFO.setObject(1, minList.get(0));    //FR_ID
 	        		pstInsertMzFRINFO.setObject(2, null);    //ED_ID
 	        		pstInsertMzFRINFO.setObject(3, minList.get(0));    //JG_ID
 	        		pstInsertMzFRINFO.setObject(4, minList.get(7));    //RDDBRXM
 	        		pstInsertMzFRINFO.setObject(5, minList.get(62));    //FDDBRZJLX
 	        		pstInsertMzFRINFO.setObject(6, minList.get(63));    //FDDBRZJHM
 	        		pstInsertMzFRINFO.setObject(7, null);    //ADDRESS
 	        		pstInsertMzFRINFO.setObject(8, minList.get(16));    //DHHM
 	        		pstInsertMzFRINFO.setObject(9, null);    //XB
 	        		pstInsertMzFRINFO.setObject(10, null);   //CSRQ
 	        		pstInsertMzFRINFO.setObject(11, null);   //CSDQDM
 	        		pstInsertMzFRINFO.setObject(12, LASTDATE);   //S_EXT_TIMESTAMP
 	        		pstInsertMzFRINFO.setObject(13, minList.get(10) == "" ? null : minList.get(10));   //CREATE_TIME
 	        		pstInsertMzFRINFO.setObject(14, laTime == "" ? null : laTime);   //MODIFY_TIME
		      //  }
	    		 pstInsert.addBatch();
	    		 pstInsertMzFRINFO.addBatch();
	    		 insertNum++;

	    	}
	        // 把一个SQL命令加入命令列表
	    }
	    // 执行批量更新

	    System.out.println("更新条数:  "+updateNUm+"   新增条数: "+insertNum);
//	    if(updateNUm !=  0 ){
//	    	 pstUpdateMzFr.executeBatch();
//    		 pstUpdateMzinfo.executeBatch();
//	    }
	    if(insertBG !=  0 ){
	    	pstInsertMzBG.executeBatch();
	    }
	    if ( insertNum != 0 ){
	    	pstInsert.executeBatch();
	    	pstInsertMzFRINFO.executeBatch();
	    }
	    // 语句执行完毕，提交本事务
	    conOracle.commit();
	    pstUpdateMzFr.close();
		 pstUpdateMzinfo.close();
		 pstInsertMzBG.close();
		 pstInsert.close();
	    pstInsertMzFRINFO.close();
//	    pre.close();
	    conOracle.close();//一定要记住关闭连接，不然mysql回应为too many connection自我保护而断开。
	}



}
