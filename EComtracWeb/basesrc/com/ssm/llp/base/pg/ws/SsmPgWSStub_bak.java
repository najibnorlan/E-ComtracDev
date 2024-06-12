/**
 * SsmPgWSStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package com.ssm.llp.base.pg.ws;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.FaultMapKey;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.OutInAxisOperation;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.util.CallbackReceiver;
import org.apache.axis2.util.Utils;
import org.apache.axis2.wsdl.WSDLConstants;

import com.ssm.llp.base.pg.model.ReturnStatus;
import com.ssm.llp.base.pg.model.ReturnStatusResponse;
import com.ssm.llp.base.pg.model.StoreData;
import com.ssm.llp.base.pg.model.StoreDataResponse;
import com.ssm.llp.base.pg.model.TransRecord;
import com.ssm.llp.base.pg.model.TransRecordResponse;

/*
 *  SsmPgWSStub java implementation
 */

public class SsmPgWSStub_bak extends Stub {
	protected AxisOperation[] _operations;

	// hashmaps to keep the fault mapping
	private HashMap faultExceptionNameMap = new HashMap();
	private HashMap faultExceptionClassNameMap = new HashMap();
	private HashMap faultMessageMap = new HashMap();

	private static int counter = 0;

	private static synchronized String getUniqueSuffix() {
		// reset the counter if it is greater than 99999
		if (counter > 99999) {
			counter = 0;
		}
		counter = counter + 1;
		return Long.toString(System.currentTimeMillis()) + "_" + counter;
	}

	private void populateAxisService() throws AxisFault {

		// creating the Service with a unique name
		_service = new AxisService("SsmPgWS" + getUniqueSuffix());
		addAnonymousOperations();

		// creating the operations
		AxisOperation __operation;

		_operations = new AxisOperation[3];

		__operation = new OutInAxisOperation();

		__operation.setName(new javax.xml.namespace.QName(
				"http://ws.synergy.com", "transRecord"));
		_service.addOperation(__operation);

		_operations[0] = __operation;

		__operation = new OutInAxisOperation();

		__operation.setName(new javax.xml.namespace.QName(
				"http://ws.synergy.com", "storeData"));
		_service.addOperation(__operation);

		_operations[1] = __operation;

		__operation = new OutInAxisOperation();

		__operation.setName(new javax.xml.namespace.QName(
				"http://ws.synergy.com", "returnStatus"));
		_service.addOperation(__operation);

		_operations[2] = __operation;

	}

	// populates the faults
	private void populateFaults() {

	}

	/**
	 * Constructor that takes in a configContext
	 */

	public SsmPgWSStub_bak(ConfigurationContext configurationContext,
			String targetEndpoint) throws AxisFault {
		this(configurationContext, targetEndpoint, false);
	}

	/**
	 * Constructor that takes in a configContext and useseperate listner
	 */
	public SsmPgWSStub_bak(ConfigurationContext configurationContext,
			String targetEndpoint, boolean useSeparateListener)
			throws AxisFault {
		// To populate AxisService
		populateAxisService();
		populateFaults();

		_serviceClient = new ServiceClient(configurationContext, _service);

		_serviceClient.getOptions()
				.setTo(new EndpointReference(targetEndpoint));
		_serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

		// Set the soap version
		_serviceClient
				.getOptions()
				.setSoapVersionURI(
						org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

	}

	/**
	 * Default Constructor
	 */
	public SsmPgWSStub_bak(ConfigurationContext configurationContext)
			throws AxisFault {

		this(configurationContext,
				"http://192.168.3.84/axis2/services/SsmPgWS.SsmPgWSHttpSoap12Endpoint/");
//				"http://211.24.161.14/axis2/services/SsmPgWS.SsmPgWSHttpSoap12Endpoint/");

	}

	/**
	 * Default Constructor
	 */
	public SsmPgWSStub_bak() throws AxisFault {

		this("http://192.168.3.84/axis2/services/SsmPgWS.SsmPgWSHttpSoap12Endpoint/");
//				"http://211.24.161.14/axis2/services/SsmPgWS.SsmPgWSHttpSoap12Endpoint/");

	}

	/**
	 * Constructor taking the target endpoint
	 */
	public SsmPgWSStub_bak(String targetEndpoint) throws AxisFault {
		this(null, targetEndpoint);
	}

	/**
	 * Auto generated method signature
	 * 
	 * @see com.synergy.ws.SsmPgWS#transRecord
	 * @param transRecord0
	 */

	public TransRecordResponse transRecord(TransRecord transRecord0)

	throws java.rmi.RemoteException

	{
		MessageContext _messageContext = null;
		try {
			OperationClient _operationClient = _serviceClient
					.createClient(_operations[0].getName());
			_operationClient.getOptions().setAction("urn:transRecord");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(
					true);

			addPropertyToOperationClient(_operationClient,
					WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

			// create a message context
			_messageContext = new MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;

			env = toEnvelope(getFactory(_operationClient.getOptions()
					.getSoapVersionURI()), transRecord0,
					optimizeContent(new javax.xml.namespace.QName(
							"http://ws.synergy.com", "transRecord")),
					new javax.xml.namespace.QName("http://ws.synergy.com",
							"transRecord"));

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			MessageContext _returnMessageContext = _operationClient
					.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext
					.getEnvelope();

			Object object = fromOM(_returnEnv.getBody().getFirstElement(),
					TransRecordResponse.class,
					getEnvelopeNamespaces(_returnEnv));

			return (TransRecordResponse) object;

		} catch (AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(new FaultMapKey(faultElt
						.getQName(), "transRecord"))) {
					// make the fault by reflection
					try {
						String exceptionClassName = (String) faultExceptionClassNameMap
								.get(new FaultMapKey(faultElt.getQName(),
										"transRecord"));
						Class exceptionClass = Class
								.forName(exceptionClassName);
						Constructor constructor = exceptionClass
								.getConstructor(String.class);
						Exception ex = (Exception) constructor.newInstance(f
								.getMessage());
						// message class
						String messageClassName = (String) faultMessageMap
								.get(new FaultMapKey(faultElt.getQName(),
										"transRecord"));
						Class messageClass = Class.forName(messageClassName);
						Object messageObject = fromOM(faultElt, messageClass,
								null);
						Method m = exceptionClass.getMethod("setFaultMessage",
								new Class[] { messageClass });
						m.invoke(ex, new Object[] { messageObject });

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender()
						.cleanup(_messageContext);
			}
		}
	}

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @see com.synergy.ws.SsmPgWS#starttransRecord
	 * @param transRecord0
	 */
	public void starttransRecord(

	TransRecord transRecord0,

	final SsmPgWSCallbackHandler callback)

	throws java.rmi.RemoteException {

		OperationClient _operationClient = _serviceClient
				.createClient(_operations[0].getName());
		_operationClient.getOptions().setAction("urn:transRecord");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

		addPropertyToOperationClient(_operationClient,
				WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final MessageContext _messageContext = new MessageContext();

		// Style is Doc.

		env = toEnvelope(getFactory(_operationClient.getOptions()
				.getSoapVersionURI()), transRecord0,
				optimizeContent(new javax.xml.namespace.QName(
						"http://ws.synergy.com", "transRecord")),
				new javax.xml.namespace.QName("http://ws.synergy.com",
						"transRecord"));

		// adding SOAP soap_headers
		_serviceClient.addHeadersToEnvelope(env);
		// create message context with that soap envelope
		_messageContext.setEnvelope(env);

		// add the message context to the operation client
		_operationClient.addMessageContext(_messageContext);

		_operationClient.setCallback(new AxisCallback() {
			public void onMessage(MessageContext resultContext) {
				try {
					org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext
							.getEnvelope();

					Object object = fromOM(resultEnv.getBody()
							.getFirstElement(), TransRecordResponse.class,
							getEnvelopeNamespaces(resultEnv));
					callback.receiveResulttransRecord((TransRecordResponse) object);

				} catch (AxisFault e) {
					callback.receiveErrortransRecord(e);
				}
			}

			public void onError(Exception error) {
				if (error instanceof AxisFault) {
					AxisFault f = (AxisFault) error;
					org.apache.axiom.om.OMElement faultElt = f.getDetail();
					if (faultElt != null) {
						if (faultExceptionNameMap.containsKey(new FaultMapKey(
								faultElt.getQName(), "transRecord"))) {
							// make the fault by reflection
							try {
								String exceptionClassName = (String) faultExceptionClassNameMap
										.get(new FaultMapKey(faultElt
												.getQName(), "transRecord"));
								Class exceptionClass = Class
										.forName(exceptionClassName);
								Constructor constructor = exceptionClass
										.getConstructor(String.class);
								Exception ex = (Exception) constructor
										.newInstance(f.getMessage());
								// message class
								String messageClassName = (String) faultMessageMap
										.get(new FaultMapKey(faultElt
												.getQName(), "transRecord"));
								Class messageClass = Class
										.forName(messageClassName);
								Object messageObject = fromOM(faultElt,
										messageClass, null);
								Method m = exceptionClass.getMethod(
										"setFaultMessage",
										new Class[] { messageClass });
								m.invoke(ex, new Object[] { messageObject });

								callback.receiveErrortransRecord(new java.rmi.RemoteException(
										ex.getMessage(), ex));
							} catch (ClassCastException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrortransRecord(f);
							} catch (ClassNotFoundException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrortransRecord(f);
							} catch (NoSuchMethodException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrortransRecord(f);
							} catch (InvocationTargetException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrortransRecord(f);
							} catch (IllegalAccessException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrortransRecord(f);
							} catch (InstantiationException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrortransRecord(f);
							} catch (AxisFault e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrortransRecord(f);
							}
						} else {
							callback.receiveErrortransRecord(f);
						}
					} else {
						callback.receiveErrortransRecord(f);
					}
				} else {
					callback.receiveErrortransRecord(error);
				}
			}

			public void onFault(MessageContext faultContext) {
				AxisFault fault = Utils
						.getInboundFaultFromMessageContext(faultContext);
				onError(fault);
			}

			public void onComplete() {
				try {
					_messageContext.getTransportOut().getSender()
							.cleanup(_messageContext);
				} catch (AxisFault axisFault) {
					callback.receiveErrortransRecord(axisFault);
				}
			}
		});

		CallbackReceiver _callbackReceiver = null;
		if (_operations[0].getMessageReceiver() == null
				&& _operationClient.getOptions().isUseSeparateListener()) {
			_callbackReceiver = new CallbackReceiver();
			_operations[0].setMessageReceiver(_callbackReceiver);
		}

		// execute the operation client
		_operationClient.execute(false);

	}

	/**
	 * Auto generated method signature
	 * 
	 * @see com.synergy.ws.SsmPgWS#storeData
	 * @param storeData2
	 */

	public StoreDataResponse storeData(

	StoreData storeData2)

	throws java.rmi.RemoteException

	{
		MessageContext _messageContext = null;
		try {
			OperationClient _operationClient = _serviceClient
					.createClient(_operations[1].getName());
			_operationClient.getOptions().setAction("urn:storeData");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(
					true);

			addPropertyToOperationClient(_operationClient,
					WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

			// create a message context
			_messageContext = new MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;

			env = toEnvelope(getFactory(_operationClient.getOptions()
					.getSoapVersionURI()), storeData2,
					optimizeContent(new javax.xml.namespace.QName(
							"http://ws.synergy.com", "storeData")),
					new javax.xml.namespace.QName("http://ws.synergy.com",
							"storeData"));

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			MessageContext _returnMessageContext = _operationClient
					.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext
					.getEnvelope();

			Object object = fromOM(_returnEnv.getBody().getFirstElement(),
					StoreDataResponse.class, getEnvelopeNamespaces(_returnEnv));

			return (StoreDataResponse) object;

		} catch (AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(new FaultMapKey(faultElt
						.getQName(), "storeData"))) {
					// make the fault by reflection
					try {
						String exceptionClassName = (String) faultExceptionClassNameMap
								.get(new FaultMapKey(faultElt.getQName(),
										"storeData"));
						Class exceptionClass = Class
								.forName(exceptionClassName);
						Constructor constructor = exceptionClass
								.getConstructor(String.class);
						Exception ex = (Exception) constructor.newInstance(f
								.getMessage());
						// message class
						String messageClassName = (String) faultMessageMap
								.get(new FaultMapKey(faultElt.getQName(),
										"storeData"));
						Class messageClass = Class.forName(messageClassName);
						Object messageObject = fromOM(faultElt, messageClass,
								null);
						Method m = exceptionClass.getMethod("setFaultMessage",
								new Class[] { messageClass });
						m.invoke(ex, new Object[] { messageObject });

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender()
						.cleanup(_messageContext);
			}
		}
	}

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @see com.synergy.ws.SsmPgWS#startstoreData
	 * @param storeData2
	 */
	public void startstoreData(

	StoreData storeData2,

	final SsmPgWSCallbackHandler callback)

	throws java.rmi.RemoteException {

		OperationClient _operationClient = _serviceClient
				.createClient(_operations[1].getName());
		_operationClient.getOptions().setAction("urn:storeData");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

		addPropertyToOperationClient(_operationClient,
				WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final MessageContext _messageContext = new MessageContext();

		// Style is Doc.

		env = toEnvelope(getFactory(_operationClient.getOptions()
				.getSoapVersionURI()), storeData2,
				optimizeContent(new javax.xml.namespace.QName(
						"http://ws.synergy.com", "storeData")),
				new javax.xml.namespace.QName("http://ws.synergy.com",
						"storeData"));

		// adding SOAP soap_headers
		_serviceClient.addHeadersToEnvelope(env);
		// create message context with that soap envelope
		_messageContext.setEnvelope(env);

		// add the message context to the operation client
		_operationClient.addMessageContext(_messageContext);

		_operationClient.setCallback(new AxisCallback() {
			public void onMessage(MessageContext resultContext) {
				try {
					org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext
							.getEnvelope();

					Object object = fromOM(resultEnv.getBody()
							.getFirstElement(), StoreDataResponse.class,
							getEnvelopeNamespaces(resultEnv));
					callback.receiveResultstoreData((StoreDataResponse) object);

				} catch (AxisFault e) {
					callback.receiveErrorstoreData(e);
				}
			}

			public void onError(Exception error) {
				if (error instanceof AxisFault) {
					AxisFault f = (AxisFault) error;
					org.apache.axiom.om.OMElement faultElt = f.getDetail();
					if (faultElt != null) {
						if (faultExceptionNameMap.containsKey(new FaultMapKey(
								faultElt.getQName(), "storeData"))) {
							// make the fault by reflection
							try {
								String exceptionClassName = (String) faultExceptionClassNameMap
										.get(new FaultMapKey(faultElt
												.getQName(), "storeData"));
								Class exceptionClass = Class
										.forName(exceptionClassName);
								Constructor constructor = exceptionClass
										.getConstructor(String.class);
								Exception ex = (Exception) constructor
										.newInstance(f.getMessage());
								// message class
								String messageClassName = (String) faultMessageMap
										.get(new FaultMapKey(faultElt
												.getQName(), "storeData"));
								Class messageClass = Class
										.forName(messageClassName);
								Object messageObject = fromOM(faultElt,
										messageClass, null);
								Method m = exceptionClass.getMethod(
										"setFaultMessage",
										new Class[] { messageClass });
								m.invoke(ex, new Object[] { messageObject });

								callback.receiveErrorstoreData(new java.rmi.RemoteException(
										ex.getMessage(), ex));
							} catch (ClassCastException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorstoreData(f);
							} catch (ClassNotFoundException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorstoreData(f);
							} catch (NoSuchMethodException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorstoreData(f);
							} catch (InvocationTargetException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorstoreData(f);
							} catch (IllegalAccessException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorstoreData(f);
							} catch (InstantiationException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorstoreData(f);
							} catch (AxisFault e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorstoreData(f);
							}
						} else {
							callback.receiveErrorstoreData(f);
						}
					} else {
						callback.receiveErrorstoreData(f);
					}
				} else {
					callback.receiveErrorstoreData(error);
				}
			}

			public void onFault(MessageContext faultContext) {
				AxisFault fault = Utils
						.getInboundFaultFromMessageContext(faultContext);
				onError(fault);
			}

			public void onComplete() {
				try {
					_messageContext.getTransportOut().getSender()
							.cleanup(_messageContext);
				} catch (AxisFault axisFault) {
					callback.receiveErrorstoreData(axisFault);
				}
			}
		});

		CallbackReceiver _callbackReceiver = null;
		if (_operations[1].getMessageReceiver() == null
				&& _operationClient.getOptions().isUseSeparateListener()) {
			_callbackReceiver = new CallbackReceiver();
			_operations[1].setMessageReceiver(_callbackReceiver);
		}

		// execute the operation client
		_operationClient.execute(false);

	}

	/**
	 * Auto generated method signature
	 * 
	 * @see com.synergy.ws.SsmPgWS#returnStatus
	 * @param returnStatus4
	 */

	public ReturnStatusResponse returnStatus(

	ReturnStatus returnStatus4)

	throws java.rmi.RemoteException

	{
		MessageContext _messageContext = null;
		try {
			OperationClient _operationClient = _serviceClient
					.createClient(_operations[2].getName());
			_operationClient.getOptions().setAction("urn:returnStatus");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(
					true);

			addPropertyToOperationClient(_operationClient,
					WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

			// create a message context
			_messageContext = new MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;

			env = toEnvelope(getFactory(_operationClient.getOptions()
					.getSoapVersionURI()), returnStatus4,
					optimizeContent(new javax.xml.namespace.QName(
							"http://ws.synergy.com", "returnStatus")),
					new javax.xml.namespace.QName("http://ws.synergy.com",
							"returnStatus"));

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			MessageContext _returnMessageContext = _operationClient
					.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext
					.getEnvelope();

			Object object = fromOM(_returnEnv.getBody().getFirstElement(),
					ReturnStatusResponse.class,
					getEnvelopeNamespaces(_returnEnv));

			return (ReturnStatusResponse) object;

		} catch (AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(new FaultMapKey(faultElt
						.getQName(), "returnStatus"))) {
					// make the fault by reflection
					try {
						String exceptionClassName = (String) faultExceptionClassNameMap
								.get(new FaultMapKey(faultElt.getQName(),
										"returnStatus"));
						Class exceptionClass = Class
								.forName(exceptionClassName);
						Constructor constructor = exceptionClass
								.getConstructor(String.class);
						Exception ex = (Exception) constructor.newInstance(f
								.getMessage());
						// message class
						String messageClassName = (String) faultMessageMap
								.get(new FaultMapKey(faultElt.getQName(),
										"returnStatus"));
						Class messageClass = Class.forName(messageClassName);
						Object messageObject = fromOM(faultElt, messageClass,
								null);
						Method m = exceptionClass.getMethod("setFaultMessage",
								new Class[] { messageClass });
						m.invoke(ex, new Object[] { messageObject });

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender()
						.cleanup(_messageContext);
			}
		}
	}

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @see com.synergy.ws.SsmPgWS#startreturnStatus
	 * @param returnStatus4
	 */
	public void startreturnStatus(

	ReturnStatus returnStatus4,

	final SsmPgWSCallbackHandler callback)

	throws java.rmi.RemoteException {

		OperationClient _operationClient = _serviceClient
				.createClient(_operations[2].getName());
		_operationClient.getOptions().setAction("urn:returnStatus");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

		addPropertyToOperationClient(_operationClient,
				WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final MessageContext _messageContext = new MessageContext();

		// Style is Doc.

		env = toEnvelope(getFactory(_operationClient.getOptions()
				.getSoapVersionURI()), returnStatus4,
				optimizeContent(new javax.xml.namespace.QName(
						"http://ws.synergy.com", "returnStatus")),
				new javax.xml.namespace.QName("http://ws.synergy.com",
						"returnStatus"));

		// adding SOAP soap_headers
		_serviceClient.addHeadersToEnvelope(env);
		// create message context with that soap envelope
		_messageContext.setEnvelope(env);

		// add the message context to the operation client
		_operationClient.addMessageContext(_messageContext);

		_operationClient.setCallback(new AxisCallback() {
			public void onMessage(MessageContext resultContext) {
				try {
					org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext
							.getEnvelope();

					Object object = fromOM(resultEnv.getBody()
							.getFirstElement(), ReturnStatusResponse.class,
							getEnvelopeNamespaces(resultEnv));
					callback.receiveResultreturnStatus((ReturnStatusResponse) object);

				} catch (AxisFault e) {
					callback.receiveErrorreturnStatus(e);
				}
			}

			public void onError(Exception error) {
				if (error instanceof AxisFault) {
					AxisFault f = (AxisFault) error;
					org.apache.axiom.om.OMElement faultElt = f.getDetail();
					if (faultElt != null) {
						if (faultExceptionNameMap.containsKey(new FaultMapKey(
								faultElt.getQName(), "returnStatus"))) {
							// make the fault by reflection
							try {
								String exceptionClassName = (String) faultExceptionClassNameMap
										.get(new FaultMapKey(faultElt
												.getQName(), "returnStatus"));
								Class exceptionClass = Class
										.forName(exceptionClassName);
								Constructor constructor = exceptionClass
										.getConstructor(String.class);
								Exception ex = (Exception) constructor
										.newInstance(f.getMessage());
								// message class
								String messageClassName = (String) faultMessageMap
										.get(new FaultMapKey(faultElt
												.getQName(), "returnStatus"));
								Class messageClass = Class
										.forName(messageClassName);
								Object messageObject = fromOM(faultElt,
										messageClass, null);
								Method m = exceptionClass.getMethod(
										"setFaultMessage",
										new Class[] { messageClass });
								m.invoke(ex, new Object[] { messageObject });

								callback.receiveErrorreturnStatus(new java.rmi.RemoteException(
										ex.getMessage(), ex));
							} catch (ClassCastException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorreturnStatus(f);
							} catch (ClassNotFoundException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorreturnStatus(f);
							} catch (NoSuchMethodException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorreturnStatus(f);
							} catch (InvocationTargetException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorreturnStatus(f);
							} catch (IllegalAccessException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorreturnStatus(f);
							} catch (InstantiationException e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorreturnStatus(f);
							} catch (AxisFault e) {
								// we cannot intantiate the class -
								// throw the original Axis fault
								callback.receiveErrorreturnStatus(f);
							}
						} else {
							callback.receiveErrorreturnStatus(f);
						}
					} else {
						callback.receiveErrorreturnStatus(f);
					}
				} else {
					callback.receiveErrorreturnStatus(error);
				}
			}

			public void onFault(MessageContext faultContext) {
				AxisFault fault = Utils
						.getInboundFaultFromMessageContext(faultContext);
				onError(fault);
			}

			public void onComplete() {
				try {
					_messageContext.getTransportOut().getSender()
							.cleanup(_messageContext);
				} catch (AxisFault axisFault) {
					callback.receiveErrorreturnStatus(axisFault);
				}
			}
		});

		CallbackReceiver _callbackReceiver = null;
		if (_operations[2].getMessageReceiver() == null
				&& _operationClient.getOptions().isUseSeparateListener()) {
			_callbackReceiver = new CallbackReceiver();
			_operations[2].setMessageReceiver(_callbackReceiver);
		}

		// execute the operation client
		_operationClient.execute(false);

	}

	/**
	 * A utility method that copies the namepaces from the SOAPEnvelope
	 */
	private Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env) {
		Map returnMap = new HashMap();
		Iterator namespaceIterator = env.getAllDeclaredNamespaces();
		while (namespaceIterator.hasNext()) {
			org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator
					.next();
			returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
		}
		return returnMap;
	}

	private javax.xml.namespace.QName[] opNameArray = null;

	private boolean optimizeContent(javax.xml.namespace.QName opName) {

		if (opNameArray == null) {
			return false;
		}
		for (int i = 0; i < opNameArray.length; i++) {
			if (opName.equals(opNameArray[i])) {
				return true;
			}
		}
		return false;
	}

	private org.apache.axiom.om.OMElement toOM(TransRecord param,
			boolean optimizeContent) throws AxisFault {

		try {
			return param.getOMElement(TransRecord.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (ADBException e) {
			throw AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(TransRecordResponse param,
			boolean optimizeContent) throws AxisFault {

		try {
			return param.getOMElement(TransRecordResponse.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (ADBException e) {
			throw AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(StoreData param,
			boolean optimizeContent) throws AxisFault {

		try {
			return param.getOMElement(StoreData.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (ADBException e) {
			throw AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(StoreDataResponse param,
			boolean optimizeContent) throws AxisFault {

		try {
			return param.getOMElement(StoreDataResponse.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (ADBException e) {
			throw AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(ReturnStatus param,
			boolean optimizeContent) throws AxisFault {

		try {
			return param.getOMElement(ReturnStatus.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (ADBException e) {
			throw AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(ReturnStatusResponse param,
			boolean optimizeContent) throws AxisFault {

		try {
			return param.getOMElement(ReturnStatusResponse.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (ADBException e) {
			throw AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory, TransRecord param,
			boolean optimizeContent, javax.xml.namespace.QName methodQName)
			throws AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
					.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(
					param.getOMElement(TransRecord.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (ADBException e) {
			throw AxisFault.makeFault(e);
		}

	}

	/* methods to provide back word compatibility */

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory, StoreData param,
			boolean optimizeContent, javax.xml.namespace.QName methodQName)
			throws AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
					.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(
					param.getOMElement(StoreData.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (ADBException e) {
			throw AxisFault.makeFault(e);
		}

	}

	/* methods to provide back word compatibility */

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory, ReturnStatus param,
			boolean optimizeContent, javax.xml.namespace.QName methodQName)
			throws AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
					.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(
					param.getOMElement(ReturnStatus.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (ADBException e) {
			throw AxisFault.makeFault(e);
		}

	}

	/* methods to provide back word compatibility */

	/**
	 * get the default envelope
	 */
	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory) {
		return factory.getDefaultEnvelope();
	}

	private Object fromOM(org.apache.axiom.om.OMElement param, Class type,
			Map extraNamespaces) throws AxisFault {

		try {

			if (TransRecord.class.equals(type)) {

				return TransRecord.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (TransRecordResponse.class.equals(type)) {

				return TransRecordResponse.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (StoreData.class.equals(type)) {

				return StoreData.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (StoreDataResponse.class.equals(type)) {

				return StoreDataResponse.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (ReturnStatus.class.equals(type)) {

				return ReturnStatus.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (ReturnStatusResponse.class.equals(type)) {

				return ReturnStatusResponse.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

		} catch (Exception e) {
			throw AxisFault.makeFault(e);
		}
		return null;
	}

}
