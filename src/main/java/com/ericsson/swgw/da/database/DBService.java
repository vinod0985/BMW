package com.ericsson.swgw.da.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
//import org.apache.commons.dbcp2.BasicDataSource;
//import org.apache.commons.dbcp2.BasicDataSourceFactory;
import com.ericsson.swgw.da.constants.DBConstants;
public class DBService {

	//private static BasicDataSource dataSource;

	static String driver = DBConstants.DRIVER;
	static String url = DBConstants.URL;
	static String user = DBConstants.USER;
	static String pass = DBConstants.PASS;	
	
	public static void init() throws Exception{	            
	      
	      if (driver == null || url == null || user == null) {
	            driver = "org.apache.derby.jdbc.EmbeddedDriver";
	            url = "jdbc:derby:DADB";
	            user = DBConstants.USER;
	            pass = DBConstants.PASS;	
	      }
	      
	      Properties dbcpProperties = new Properties();
	      setDataSourceProperty(dbcpProperties, "driverClassName", driver);
	      setDataSourceProperty(dbcpProperties, "url", url);
	      setDataSourceProperty(dbcpProperties, "username", user);
	      setDataSourceProperty(dbcpProperties, "password", pass);
	      setDataSourceProperty(dbcpProperties, "connectionProperties", ";create=true;upgrade=true");
	      setDataSourceProperty(dbcpProperties, "defaultAutoCommit", String.valueOf(Boolean.FALSE));
	      setDataSourceProperty(dbcpProperties, "maxTotal", "20");
	      setDataSourceProperty(dbcpProperties, "poolPreparedStatements", String.valueOf(Boolean.TRUE));
	      setDataSourceProperty(dbcpProperties, "maxOpenPreparedStatements", "50");
	      
	      try {
	            //dataSource = BasicDataSourceFactory.createDataSource(dbcpProperties);
	         } catch (Exception var11) {
	            throw new Exception(var11);
	         }	
	}
	
	private static void setDataSourceProperty(Properties dbcpProperties, String name, String defaultValue) {
	      //String value = String.format("db.connection.%s", name), defaultValue;
	      dbcpProperties.put(name, defaultValue);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initialize() throws Exception {
		Collection<ModuleInitializer> initializers = new LinkedList();
		initializers.add(new DBInitializer());
		
		Iterator var4 = initializers.iterator();
		while(var4.hasNext()) {
	         ModuleInitializer initializer = (ModuleInitializer)var4.next();
	         System.out.println(String.format("%1$s Start: %2$s %1$s", "==========", initializer.getClass().getSimpleName()));
	         long t0 = System.currentTimeMillis();
	        // initializer.init(dataSource,driver);
	         System.out.println(String.format("%1$s End: %2$s (time: %3$d ms.) %1$s", "----------", initializer.getClass().getSimpleName(), System.currentTimeMillis() - t0));
	      }
	}
	
	@SuppressWarnings("unused")
	public static  Connection getDBConnection() throws Exception{
		Connection c = null;
		Class.forName(DBConstants.DRIVER);
		try {		
			init();
			 /*c = dataSource.getConnection();
			 if(null != c){
				 c.setAutoCommit(false);
			 } */        
	         
		}catch (Exception var16) {
			var16.printStackTrace();
			/*if (c != null) {
	             try {
	                c.rollback();
	             } catch (SQLException var15) {
	                System.out.println("Unable to rollback ..."+ var15);
	             }
	          }
*/	          throw var16;
		}
		return c;
	}

	@SuppressWarnings("unused")
	private static  Connection getConnection() {
		Connection c = null;
		try {
			System.out.println("url::::" + url);

			Properties properties = new Properties();
			properties.put("create", "true");
			properties.put("user", DBConstants.USER);
			properties.put("password", DBConstants.PASS);

			c = DriverManager.getConnection(url,DBConstants.USER,DBConstants.PASS);
			
			/*if (c != null) {
				System.out.println("Connected to database #3");
			} else {
				System.out.println("Not Connected to database");
			}*/

		}catch (Exception var1) {
			var1.printStackTrace();
		}
		return c;
	}
	
		
}
