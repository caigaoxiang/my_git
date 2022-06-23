package com.resoft.data;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
/**
 * @author yy
 */
public class MainThread {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws FileNotFoundException, SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SS");
		TimeZone t = sdf.getTimeZone();
		t.setRawOffset(0);
		sdf.setTimeZone(t);
		Long startTime = System.currentTimeMillis();
		//此段为要放置测取时间的函数
//		connSqlServer.executeSql("TRUNCATE table disease_drug_associate_view");
		/**
		 * 将工商数据迁移至192库中
		 */
		List<List<Object>> newDrug = tableInput();
		List<List<Object>> newDrugAlt = YzymeAlt.tableInput();
		List<List<Object>> newDrugYzymECancel = YzymECancel.tableInput();
		List<List<Object>> yzymEInv = YzymEInv.tableInput();//原数据为空

		List<List<Object>> yzymEContact = YzymEContact.tableInput();
		List<List<Object>> yzymEFinance = YzymEFinance.tableInput();
		List<List<Object>> yzymELerep = YzymELerep.tableInput();
		List<List<Object>> yzymEliILLDISHONESTY = YzymEliILLDISHONESTY.tableInput();
		List<List<Object>> yzymESub = YzymESub.tableInput();
		List<List<Object>> yzymAoOPANOMALY = YzymAoOPANOMALY.tableInput();
		List<List<Object>> lzhyEGtAltinfo = LzhyEGtAltinfo.tableInput();
		List<List<Object>> lzhyEGtBaseinfo = LzhyEGtBaseinfo.tableInput();
		List<List<Object>> lzhyEgtCancel = LzhyEgtCancel.tableInput();
		List<List<Object>> lzhyEgtMember = LzhyEgtMember.tableInput();
		List<List<Object>> lzhyEgtOPERATOR = LzhyEgtOPERATOR.tableInput();
		List<List<Object>> yzymAnALTERSTOCKINFO = YzymAnALTERSTOCKINFO.tableInput();
		List<List<Object>> yzymAnBaseinfo = YzymAnBaseinfo.tableInput();//应执行全部更新 缺少主键ID
		List<List<Object>> yzymAnFORINVESTMENT = YzymAnFORINVESTMENT.tableInput();
		List<List<Object>> yzymAnSUBCAPITAL = YzymAnSUBCAPITAL.tableInput();
		List<List<Object>> yzymAnWEBSITEINFO = YzymAnWEBSITEINFO.tableInput();
		//List<List<String>> bb165ToMz119 = Bb165ToMz119.tableInput();//执行编办数据同步到119数据库上面
		/**
		 * 将oracle117数据库迁移至109mysql数据库中
		 */
		/*List<List<Object>> qyINfoList = QyInfoList.tableInput();//企业数据
		List<List<Object>> gtINfoList = GtInfoList.tableInput();//个体数据
		List<List<Object>> mzINfoList = MzInfoList.tableInput();//民政数据
		List<List<Object>> otherINfoList = OtherInfoList.tableInput();//其他数据
*/
		Long endTime = System.currentTimeMillis();
		System.out.println("用时：" + sdf.format(new Date(endTime - startTime)));
	}
	//转换的时候进行单表操作


	public static List<List<Object>> tableInput() throws FileNotFoundException,SQLException {
		List<List<Object>> FindList = new ArrayList<List<Object>>();
		Connection con = ConnOracle.getConnection();
		//连接sqlserver
		Connection conSqlserver = ConnSqlServer.getConnection();
		PreparedStatement pre = null;
		ResultSet resultSet = null;
		System.out.println("执行YZYM_E_BASEINFO");
		//获取sqlserver中S_EXT_TIMESTAMP中最大的时间
		String sqlServer = "SELECT MAX(S_EXT_TIMESTAMP) as times from usci.YZYM_E_BASEINFO ";
		pre = conSqlserver.prepareStatement(sqlServer);
		resultSet = pre.executeQuery();
		String maxDate = null;
		if(resultSet.next()){
			maxDate =  resultSet.getString(1);
		}
		System.out.println("最后数据时间"+maxDate);
		String sql = "SELECT t.*,null as   JGDM ,0 as dflag from YZYM_E_BASEINFO t " +
				"where S_EXT_TIMESTAMP > to_date('"+maxDate+"','yyyy-mm-dd hh24:mi:ss')  ORDER BY t.S_EXT_TIMESTAMP";//获取数据列表

		/*String sql = "SELECT t.*,null as   JGDM ,0 as dflag from YZYM_E_BASEINFO t " +
		"where S_EXT_TIMESTAMP > to_date('2019-10-01','yyyy-mm-dd hh24:mi:ss')  ORDER BY t.S_EXT_TIMESTAMP";//获取数据列表
*/		try {
			pre = con.prepareStatement(sql);
			resultSet = pre.executeQuery();
			//字段名称数组
			String[] columu = {"ETPSID","INFOACTIONTYPE","UNISCID","REGNO","ENTNAME","ENTTYPE","ESTDATE",
					"LEREP","REGCAP","FUNDAM","SUBCONAM","ACCONAM","CONGRO","CRNCYID",
					"DOM","OPSCOPE","INDUSTRYCODE","OPFROM","OPTO","REGORG","AREAORG",
					"DISTRICT","APPRDATE","OPSTATE","PROLOC","YIEDISTRICT","COUNTRY","POSTCODE","TELEPHONE","CCMETHOD",
					"TAXLICWITHDRAW","ORGLICWITHDRAW","S_EXT_TIMESTAMP","JGDM","dflag","XGJL"};
			int i=0;
			while (resultSet.next()) {
				List<Object> minList = new ArrayList<Object>();
				for(String each:columu){
					if(each == "ESTDATE" ||each == "OPFROM" ||each == "OPTO" ||each == "APPRDATE"||each == "S_EXT_TIMESTAMP" ){
						minList.add(resultSet.getTimestamp(each));
					}else if(each == "REGCAP" ||each == "FUNDAM" || each == "SUBCONAM" || each == "ACCONAM" || each == "CONGRO" ) {
						minList.add(resultSet.getDouble(each));
					}else if(each=="XGJL"){
						minList.add("default");
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
	    Connection con = ConnSqlServer.getConnection();
	    //Connection conOracle = ConnOracle.getConnection();//oracle
		PreparedStatement pre = null;
		ResultSet resultSet = null;
	    con.setAutoCommit(false);
	    PreparedStatement pstInsert = con
	            .prepareStatement("insert into USCI.USCI.YZYM_E_BASEINFO values (?,?,?,?,?,?,?,?,?,?," +
	            		"?,?,?,?,?,?,?,?,?,?," +
	            		"?,?,?,?,?,?,?,?,?,?," +
	            		"?,?,?,?,?,?)");
	    PreparedStatement pstUpdate = null ;
	    int updateNUm = 0;
	    int insertNum = 0;
	    for (List<Object> minList: FindList) {
	    	String etpsid = minList.get(0).toString() ;
//	    	System.out.println(etpsid);
	    	String sql1 = "select ETPSID from USCI.USCI.YZYM_E_BASEINFO where ETPSID = '"+etpsid+"'";
	    	pre = con.prepareStatement(sql1);
	    	resultSet = pre.executeQuery();
	    	 pstUpdate = con
		    		.prepareStatement("update USCI.USCI.YZYM_E_BASEINFO set ETPSID = ?,  INFOACTIONTYPE = ? ,UNISCID = ? ,REGNO = ? ,ENTNAME = ? ,ENTTYPE= ? ," +
		    				"ESTDATE= ? ,LEREP  = ? ,REGCAP  = ? ,FUNDAM = ? ,SUBCONAM = ? ,ACCONAM = ? ,CONGRO = ? ," +
		    				"CRNCYID  = ? ,DOM  = ? ,OPSCOPE = ? ,INDUSTRYCODE= ? ,OPFROM= ? ,OPTO = ? ,REGORG = ? ," +
		    				"AREAORG = ? ,DISTRICT = ? ,APPRDATE= ? ,OPSTATE = ? ,PROLOC = ? ,YIEDISTRICT  = ? ,COUNTRY= ? ," +
		    				"POSTCODE = ? ,TELEPHONE = ? ,CCMETHOD = ? ,TAXLICWITHDRAW = ? ,ORGLICWITHDRAW= ? ,S_EXT_TIMESTAMP = ? ," +
		    				"JGDM= ? ,dflag= ?,XGJL=? where ETPSID = '"+etpsid+"'");
	    	if(resultSet.next()){
	    		 for(int i=0;i<minList.size();i++){
	 	        	//查询的数据为false的时候执行insert    当查询的数据为true的时候执行update
 	        		pstUpdate.setObject(i+1, minList.get(i));
	    		 }
//	    		 pstUpdate.addBatch();
	    		 updateNUm++;
	    		 pstUpdate.executeUpdate();
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
