/**
 * SsmPgWSCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.ssm.llp.base.pg.ws;

import com.ssm.llp.base.pg.model.ReturnStatusResponse;
import com.ssm.llp.base.pg.model.StoreDataResponse;
import com.ssm.llp.base.pg.model.TransRecordResponse;

/**
 * SsmPgWSCallbackHandler Callback class, Users can extend this class and
 * implement their own receiveResult and receiveError methods.
 */
public abstract class SsmPgWSCallbackHandler {

	protected Object clientData;

	/**
	 * User can pass in any object that needs to be accessed once the
	 * NonBlocking Web service call is finished and appropriate method of this
	 * CallBack is called.
	 * 
	 * @param clientData
	 *            Object mechanism by which the user can pass in user data that
	 *            will be avilable at the time this callback is called.
	 */
	public SsmPgWSCallbackHandler(Object clientData) {
		this.clientData = clientData;
	}

	/**
	 * Please use this constructor if you don't want to set any clientData
	 */
	public SsmPgWSCallbackHandler() {
		this.clientData = null;
	}

	/**
	 * Get the client data
	 */

	public Object getClientData() {
		return clientData;
	}

	/**
	 * auto generated Axis2 call back method for transRecord method override
	 * this method for handling normal response from transRecord operation
	 */
	public void receiveResulttransRecord(TransRecordResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from transRecord operation
	 */
	public void receiveErrortransRecord(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for storeData method override this
	 * method for handling normal response from storeData operation
	 */
	public void receiveResultstoreData(StoreDataResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from storeData operation
	 */
	public void receiveErrorstoreData(java.lang.Exception e) {
	}

	/**
	 * auto generated Axis2 call back method for returnStatus method override
	 * this method for handling normal response from returnStatus operation
	 */
	public void receiveResultreturnStatus(ReturnStatusResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from returnStatus operation
	 */
	public void receiveErrorreturnStatus(java.lang.Exception e) {
	}

}
