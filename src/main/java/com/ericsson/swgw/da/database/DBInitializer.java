package com.ericsson.swgw.da.database;

//import org.apache.commons.dbcp2.BasicDataSource;


public class DBInitializer implements ModuleInitializer {
	
	public static final String TIF_SCHEMA = "com/ericsson/swgw/da/database/da-schema.sql";
	private static final String PROD = "prod";
	
	public void init() throws Exception {
		
	}

	/*	@Override
	public void init(BasicDataSource dataSource,String driver) throws Exception {
		DBUtils.initDataBase(this.getClass().getClassLoader(),
				"com/ericsson/swgw/da/database/da-schema.sql", driver,dataSource);
		
	}*/	
}
