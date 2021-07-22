package com.qitcorp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import com.qitcorp.connection.ConnectionDB;
import com.qitcorp.model.TcFacturasVantiveModel;
import com.qitcorp.util.Tools;

public class ProcesoFacturasDao {

	private static final Logger logger = Logger.getLogger("log4j.properties");

	
	
	// Obtener parametros para consumo de WS Pagos y Servicio
		public static List<TcFacturasVantiveModel> obtenerParametrosWS() {
			List<TcFacturasVantiveModel> listResult = new ArrayList<TcFacturasVantiveModel>();
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rst = null;
			TcFacturasVantiveModel model = null;
			try {

				conn = ConnectionDB.getConnection();
				ps = conn.prepareStatement(Tools.obtener_tc_facturas_vantive_vw);
				rst = ps.executeQuery();
				while (rst.next()) {
					model = new TcFacturasVantiveModel();
					model.setBILL_REF_NO(rst.getInt(1));

					listResult.add(model);
				}
			} catch (SQLException e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			} finally {
				finalizaConexion(conn, ps, null, rst);
			}

			return listResult;
		}
	public static List<TcFacturasVantiveModel> obtenerFacturasVantive() {
		List<TcFacturasVantiveModel> lst = new ArrayList<TcFacturasVantiveModel>();
		Statement stmt = null;
		ResultSet rst = null;
		Connection conn = null;
		TcFacturasVantiveModel obj = null;
		try {
			conn = ConnectionDB.getConnection();
			stmt = conn.createStatement();
			logger.info(Tools.obtener_tc_facturas_vantive_vw);
			rst = stmt.executeQuery(Tools.obtener_tc_facturas_vantive_vw);

			while (rst.next()) {
				obj = new TcFacturasVantiveModel();
				obj.setTCFACTURASCABID(rst.getInt(TcFacturasVantiveModel.FIELD_TCFACTURASCABID));
				lst.add(obj);				
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			finalizaConexion(conn, null, stmt, rst);
		}
		return lst;

	}

	private static void finalizaConexion(Connection conn, Object object, Statement stmt, ResultSet rst) {
		if (rst != null)
			try {
				rst.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		
	}
	public static String ejecutaApldesRecargaSP(int cbhistorialaccionid) {
		boolean result = (Boolean) null;
		String result1 = "";
		Connection conn = null;
		CallableStatement cmd = null;
		try {
			conn = ConnectionDB.getConnection();

			cmd = conn.prepareCall(Tools.FACCURAS_VANTIVE_SP);
			cmd.setInt(1, cbhistorialaccionid);
			
			result = cmd.executeUpdate() > 0;
			//result1 = result;
			logger.info( "Ejecuta => "+Tools.FACCURAS_VANTIVE_SP+" => resultado: "+result);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (cmd != null)
					cmd.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return result1;
	}
}