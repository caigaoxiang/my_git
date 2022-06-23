
支持从sqlserver2005向oracle11的不同表结构的数据迁移；
支持大数据传输，设置pageSize即设置每次传输多少条数据。

config.properties
	key对应oracle11的表名.字段
	value对应sqlserver2005的表明.字段
	
	如果修改不同数据库之间的迁移，将连接URL CON修改即可，
	注：源数据库的分页查询不一致，需要修改，