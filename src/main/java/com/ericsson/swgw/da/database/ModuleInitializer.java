package com.ericsson.swgw.da.database;

//import org.apache.commons.dbcp2.BasicDataSource;

public interface ModuleInitializer {
	void init() throws Exception;

	//void init(BasicDataSource dataSource, String driver) throws Exception;
}
