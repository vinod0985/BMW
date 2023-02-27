package com.ericsson.swgw.da.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestDBConnection {
	private DBService dbc;
	private Connection con;
	
	@BeforeEach
	public void initData(){
		dbc= new DBService();
		
	}
	
	@Test
	public  void testGetCon() throws Exception{
		 con = dbc.getDBConnection();
		 
		//assertEquals(con, "Connection is not created");
		 assertNull(con);
	}
  
	
	@AfterEach
	public void clean(){
		dbc= null;
		con= null;
	}
}
