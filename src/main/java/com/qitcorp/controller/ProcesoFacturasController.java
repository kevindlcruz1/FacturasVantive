package com.qitcorp.controller;

import java.net.MalformedURLException;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.apache.axis.AxisFault;
import org.apache.log4j.Logger;

import com.qit.www.wsdoc1.intf.client.WsCreateBillRequest;
import com.qit.www.wsdoc1.intf.client.WsCreateBillResponse;
import com.qit.www.wsdoc1.intf.client.WsDoc1;
import com.qit.www.wsdoc1.intf.client.WsDoc1Service;
import com.qit.www.wsdoc1.intf.client.WsDoc1ServiceLocator;
import com.qit.www.wsdoc1.intf.client.WsDoc1Soap11Stub;
import com.qitcorp.dao.ProcesoFacturasDao;

import com.qitcorp.model.TcFacturasVantiveModel;
import com.qitcorp.util.Tools;




public class ProcesoFacturasController {
	private static final Logger logger = Logger.getLogger("log4j.properties");

	
	
	public WsCreateBillResponse requestWsFacturasVantive(List<TcFacturasVantiveModel> parametro,TcFacturasVantiveModel facturas ) {
		WsDoc1Service servicio = new WsDoc1ServiceLocator();
		WsDoc1 wsExec;

		try {
			wsExec = new WsDoc1Soap11Stub(new URL(servicio.getWsDoc1Soap11Address()), servicio);
			
			//Se valida si los 3 campos telefono traen valor o vienen null
			//int BILL_REF_NO = facturas.getTCFACTURASCABID();
			WsCreateBillRequest request = new WsCreateBillRequest();
			request.setBill_ref_no(0);
			
			WsCreateBillResponse response = wsExec.wsCreateBill(request);
					                              
			
					logger.info("Respuesta de WS facturas: "+response.getMensaje());
			return response;
		} catch (AxisFault e) {
			logger.error(e);
		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (RemoteException e) {
			logger.error(e);
		}
		
		return null;
	}
	public void startProcessFactura() {
		ProcesoFacturasDao objDao = new ProcesoFacturasDao();
		List<TcFacturasVantiveModel> list = objDao.obtenerFacturasVantive();

		if (list != null && list.size() > 0) {
			List<TcFacturasVantiveModel> parametros = ProcesoFacturasDao.obtenerParametrosWS();
			Iterator<TcFacturasVantiveModel> iterator = list.iterator();
			while (iterator.hasNext()) {
				TcFacturasVantiveModel facturas = iterator.next();
				String response = procesaWSPagosCreaCasos(parametros, facturas);
				logger.info("OBJETO FACTURAS: " + facturas.getBILL_REF_NO());
				logger.info("Resultado de consumo: "+response);

				//obtenerFacturasVantive(parametros);
			}
		} else {
			logger.warn("Lista de acciones vacia");
		}

	}
	 
		public String procesaWSPagosCreaCasos(List<TcFacturasVantiveModel> parametros,
				TcFacturasVantiveModel historial) {
			
			String result = ProcesoFacturasDao.ejecutaApldesRecargaSP(historial.getTCFACTURASCABID());
			logger.info("[ProcesoFacturas] Ejecuta SP para id => "
			+ historial.getTCFACTURASCABID() + " Resultado => " + result);	
			
			
			return result;
		}
		
		

}
