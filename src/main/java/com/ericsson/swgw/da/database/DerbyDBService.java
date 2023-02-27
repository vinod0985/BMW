package com.ericsson.swgw.da.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ericsson.swgw.da.constants.DBConstants;
import com.ericsson.swgw.da.constants.Constants;

public class DerbyDBService {
	private static  Logger logger = LogManager.getLogger(DerbyDBService.class);
	public static void initializeDB() throws SQLException {

		Connection conn = getConnection();

		// check table exist or not
		String tableName = "SWGWDATA";
		boolean tableExist = DBUtils.tableExist(conn, tableName);
		if (tableExist) {

			logger.info("Table already created");

		} else {
			// Creating the Statement object
			Statement stmt = null;
			try {
				stmt = conn.createStatement();

				// Creating a table in Derby database
				String query = DBUtils.getSWGWDataTableSpceQuery();
				stmt.execute(query);
				logger.info("Table created");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException(e.getMessage());
			} finally {
				if(stmt != null ){
					stmt.close();
				}if(conn!=null){
					conn.close();
				}
				
			}
			
		}

	}

	/**
	 * @return
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
		String URL = DBConstants.URL;
		String driver = DBConstants.DRIVER;
		String create = DBConstants.CREATE_TRUE;
		if (driver == null || URL == null) {
			driver = "org.apache.derby.jdbc.EmbeddedDriver";
			URL = "jdbc:derby:data";
		}
		URL = URL + Constants.SEMI_COLON + create;

		Connection conn = DriverManager.getConnection(URL);
		return conn;
	}

	/**
	 * @param stmt
	 * @return
	 * @throws Exception 
	 * @throws ClassNotFoundException
	 */
	public static void insertingData(String clientKey, String secretKey,
			int euft) throws SQLException {

		Connection conn = null;
		// Creating the Statement object
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();

			stmt = conn.createStatement();

			// checking data exist or not
			String query = DBUtils.getConfigdataQuery(euft);

			rs = stmt.executeQuery(query);

			if (rs.next()) {
				// Updating the data if record exist
				String query1 = DBUtils.updatedataQuery(euft, clientKey, secretKey);
				stmt.execute(query1);
				logger.info("Values updated");

			} else {
				// Inserting data if record not exist
				String query1 = DBUtils.insertingdataQuery(euft, clientKey,
						secretKey);
				stmt.execute(query1);
				logger.info("Values inserted");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}finally{
			if(conn != null){
				conn.close();
			}
			
			if (stmt != null){
				stmt.close();
			}
			
			if(rs != null){
				rs.close();
			}
		}
		

	}

	/**
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, String> getData(int euft) throws SQLException {
		Map<String, String> configMap = new HashMap<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			 conn = getConnection();

			// Creating the Statement object
			 stmt = conn.createStatement();

			// Get data
			String query = DBUtils.getConfigdataQuery(euft);

			 rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				configMap.put(Constants.CLIENT_KEY,rs.getString(Constants.CLIENT_KEY));
				configMap.put(Constants.SECRET_KEY,rs.getString(Constants.SECRET_KEY));
				configMap.put(Constants.Euft,Integer.toString(rs.getInt(Constants.Euft)));
			}
			
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		} finally {
			if( conn != null) {
				conn.close();
			}
			
			if(stmt != null ) {
				stmt.close();
			}
			
			if(rs != null )	{
				rs.close();				
			}	
		}
		return configMap;
	}
}
