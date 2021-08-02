package com.qitcorp.connection;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.qitcorp.util.Tools;

public class ConnectionDB {
	private static final Logger logger = Logger.getLogger("log4j.properties");
	
	public static Connection getConnection() {
		Connection conn = null;
		Properties pp = new Properties();
		
		try {
			pp.load(new FileReader(Tools.facturasVantive));
			String url = "jdbc:oracle:thin:@" + pp.getProperty("host") + ":" + pp.getProperty("port") + ":"
					+ pp.getProperty("sid");
			conn = DriverManager.getConnection(url, pp.getProperty("user"), pp.getProperty("password"));
		} catch (SQLException e) {
			logger.error(e);
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		return conn;
	}
}
