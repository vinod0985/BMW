package com.ericsson.swgw.da.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

//import org.apache.commons.dbcp2.BasicDataSource;


import com.ericsson.swgw.da.constants.DBConstants;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.utils.StringUtils;

public class DBUtils {

	/*public static void initDataBase(ClassLoader classLoader, String sqlResource,
			String driver, BasicDataSource dataSource) throws Exception {
		 Connection c = null;
		 
		 try {
			 Class.forName(driver);
	    	 c = dataSource.getConnection();
	         c.setAutoCommit(false);
	         
	       //Creating a table in Derby database
	         String query = getSWGWDataTableSpceQuery();
	         
	       //Creating the Statement object
		     Statement stmt = c.createStatement();
	         stmt.execute(query);		      
	         System.out.println("Table created");
	         
		        DBUtils dbUtils = new DBUtils();
		        dbUtils.updateConfig(123456,"Test","Test");
		 }catch (Exception var16) {
	         if (c != null) {
	             try {
	                c.rollback();
	             } catch (SQLException var15) {
	                System.out.println("Unable to rollback ..."+ var15);
	             }
	          }
	          throw var16;
	       } finally {	          
	    	   if (c != null) {
	  	         try {
	  	            c.close();
	  	         } catch (SQLException var2) {
	  	        	 var2.printStackTrace();
	  	         }
	  	      }
	       }
		
	}*/
	
	/**
	 * @param secret_key 
	 * @param client_key 
	 * 
	 */
	/*public  String updateConfig(int euft,String clientKey, String secretKey) {
		String sMessage = "";
		Connection c = null;
		try {
			
			c = DBService.getDBConnection();
			
			//Inserting data
	         String query = insertingdataQuery(euft, clientKey, secretKey);
	         System.out.println("query inserted:::::"+query);
	         
	         //Creating the Statement object
		     Statement stmt = c.createStatement();
	         Boolean b= stmt.execute(query);		      
	         System.out.println("Values inserted"+b);
	         
		} catch (Exception var16) {
			var16.printStackTrace();
			sMessage = var16.getMessage();
	         if (c != null) {
	             try {
	                c.rollback();
	             } catch (SQLException var15) {
	            	 var15.printStackTrace();
	            	 sMessage = var15.getMessage();
	            	 var15.printStackTrace();
	                System.out.println("Unable to rollback ..."+ var15);
	             }
	          }
	          	       
		} finally {	          
    	   if (c != null) {
	  	         try {
	  	            c.close();
	  	         } catch (SQLException var2) {
	  	        	 sMessage = var2.getMessage();
	  	        	 var2.printStackTrace();
	  	         }
	  	      }
		}		
		return sMessage;
	}
	
*//*
	public static Map<String, String> getConfig(int euft) {
		Map<String,String> configMap = new HashMap<String,String>();
		Connection c = null;
		try{
			Class.forName(DBConstants.DRIVER);
			c = DBService.getDBConnection();
			
			//GET data
	         String query = getConfigdataQuery(euft);
	         
	         //Creating the Statement object
		     Statement stmt = c.createStatement();


		     ResultSet rs = stmt.executeQuery(query);
		      System.out.println("Contents of the table SWGWData table:");
			while (rs.next()) {
				System.out.print("Id: " + rs.getInt("Id") + ", ");
				System.out.print("ClientId: " + rs.getString("ClientId") + ", ");
				System.out.print("SecretKey: " + rs.getString("SecretKey")+ ", ");
				System.out.println();
				
				configMap.put(Constants.CLIENT_KEY, rs.getString(Constants.CLIENT_KEY));
				configMap.put(Constants.SECRET_KEY, rs.getString(Constants.SECRET_KEY));
			}
	         
	         System.out.println("Contents of the table SWGWData table:");
			
		} catch (Exception var16) {
			var16.printStackTrace();
			configMap.put(Constants.MESSAGE, var16.getMessage());
			if (c != null) {
				try {
					c.rollback();
				} catch (SQLException var15) {
					configMap.put(Constants.MESSAGE, var15.getMessage());
					var15.printStackTrace();
				}
			}
		} finally {	          
			if (c != null) {
				try {
					c.close();
				} catch (SQLException var2) {
					configMap.put(Constants.MESSAGE, var2.getMessage());
					var2.printStackTrace();
				}
			}
		}	
		
		
		return configMap;
	}*/
	
	static String insertingdataQuery(int euft, String clientKey, String secretKey) {
		StringBuilder insertingQuery= new StringBuilder();
		insertingQuery.append("INSERT INTO SWGWDATA ");
		insertingQuery.append("( ");
		insertingQuery.append(Constants.Euft);
		insertingQuery.append(", ");
		insertingQuery.append(Constants.CLIENT_KEY);
		insertingQuery.append(", ");
		insertingQuery.append(Constants.SECRET_KEY);
		insertingQuery.append(" ) VALUES  ");
		insertingQuery.append("(");
		insertingQuery.append(euft);
		insertingQuery.append(", '");
		insertingQuery.append(clientKey);
		insertingQuery.append("', '");
		insertingQuery.append(secretKey);
		insertingQuery.append("')");
		String query = insertingQuery.toString();
		return query;
	}

	/**
	 * 
	 */
	static String getSWGWDataTableSpceQuery() {
		StringBuilder tableQuery= new StringBuilder();
		tableQuery.append("CREATE TABLE SWGWDATA ( ");
		tableQuery.append("");
		tableQuery.append(Constants.Euft);
		tableQuery.append(" INT NOT NULL ,");
		tableQuery.append(" ");
		tableQuery.append(Constants.CLIENT_KEY);
		tableQuery.append(" VARCHAR(255) NOT NULL , ");
		tableQuery.append(" ");
		tableQuery.append(Constants.SECRET_KEY);
		tableQuery.append(" VARCHAR(255) NOT NULL , ");
		tableQuery.append("PRIMARY KEY (Euft))");
		String query = tableQuery.toString();
		return query;
	}
	
	/**
	 * 
	 */

	static String getConfigdataQuery(int euft) {
		StringBuilder configQuery= new StringBuilder();
		configQuery.append("Select * FROM SWGWDATA ");
		configQuery.append("WHERE ");
		configQuery.append(Constants.Euft);
		configQuery.append("=");
		configQuery.append(euft);
		configQuery.append("");
		String query= configQuery.toString();
		return query;
	}
	
	/**
	 * 
	 */

	 static boolean tableExist(Connection conn, String tableName) throws SQLException {
		boolean tExists = false;
	    try (ResultSet res = conn.getMetaData().getTables(null, null, tableName, new String[] {"TABLE"})) {
	        while (res.next()) { 
	            String tName = res.getString("TABLE_NAME");
	            if (tName != null && tName.equals(tableName)) {
	                tExists = true;
	                break;
	            }
	        }
	    }
	    return tExists;
		
	}
	
	/**
	 * 
	 */

	 static String updatedataQuery(int euft, String clientKey,
			String secretKey) {
		 StringBuilder updateQuery= new StringBuilder();
		 updateQuery.append("UPDATE SWGWDATA ");
		 updateQuery.append("SET ");
		 updateQuery.append(Constants.CLIENT_KEY);
		 updateQuery.append("='");
		 updateQuery.append(clientKey);
		 updateQuery.append("'");
		 updateQuery.append(",");
		 updateQuery.append(Constants.SECRET_KEY);
		 updateQuery.append("='");
		 updateQuery.append(secretKey);
		 updateQuery.append("'");
		 updateQuery.append("WHERE ");
		 updateQuery.append(Constants.Euft);
		 updateQuery.append("=");
		 updateQuery.append(euft);
		 updateQuery.append("");
		 String query = updateQuery.toString();
		return query;

	}

	/**
	 * @param euft
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public static Map getSecrects(String euft) throws SQLException {
		Map<String, String> configMap = new HashMap<>();
		if (!StringUtils.isOnlyWhitespaceOrEmpty(euft)) {
			if (euft.matches("[0-9]+")) {
				configMap = DerbyDBService.getData(Integer.parseInt(euft));
			}
		}
		return configMap;
	}

}
