/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.dao.CmpTransactionDao;
import com.ssm.ezbiz.service.CmpDetailService;
import com.ssm.ezbiz.service.CmpMasterService;
import com.ssm.ezbiz.service.CmpTransactionService;
import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.WSManagementService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.CmpInfo;
import com.ssm.llp.ezbiz.model.CmpMaster;
import com.ssm.llp.ezbiz.model.CmpTransaction;
import com.ssm.webis.client.CompoundClient;
import com.ssm.webis.param.CompoundPaymentReq;
import com.ssm.webis.param.CompoundPaymentResp;
import com.ssm.webis.param.CompoundStatusParam;
import com.ssm.webis.param.CompoundStatusReq;
import com.ssm.webis.param.CompoundStatusResp;

@Service
public class CmpTransactionServiceImpl extends BaseServiceImpl<CmpTransaction, String> implements CmpTransactionService, PaymentInterface {
	@Autowired
	CmpTransactionDao cmpTransactionDao;

	@Autowired
	CmpMasterService cmpMasterService;

	@Autowired
	CmpDetailService cmpDetailService;

	@Autowired
	@Qualifier("LlpPaymentReceiptService")
	private LlpPaymentReceiptService llpPaymentReceiptService;

	@Autowired
	@Qualifier("LlpParametersService")
	LlpParametersService llpParametersService;

	@Autowired
	@Qualifier("LlpPaymentTransactionService")
	LlpPaymentTransactionService llpPaymentTransactionService;

	@Autowired
	@Qualifier("WSManagementService")
	WSManagementService wSManagementService;

	@Override
	public BaseDao getDefaultDao() {
		return cmpTransactionDao;
	}

	@Override
	public List<CmpTransaction> findByDate(Date fromDt, Date toDt, String cmpNo, String updateBy, String status, String page) throws SSMException {
		List<CmpTransaction> listCmpTransRet = new ArrayList<CmpTransaction>();
		if (fromDt == null && toDt == null && StringUtils.isNotEmpty(cmpNo)) {
			SearchCriteria sc = new SearchCriteria("cmpNo", SearchCriteria.EQUAL, cmpNo);
			SearchResult sr = cmpMasterService.findByCriteria(sc);

			if (sr != null) {
				List<CmpMaster> listCmpMaster = sr.getList();

				if (listCmpMaster != null && !listCmpMaster.isEmpty()) {

					for (Iterator iterator = listCmpMaster.iterator(); iterator.hasNext();) {
						CmpMaster cmpMaster = (CmpMaster) iterator.next();

						SearchCriteria sc2 = new SearchCriteria("cmpTransactionCode", SearchCriteria.EQUAL, cmpMaster.getCmpTransactionCode());
						sc2 = SearchCriteria.andIfNotNull(sc2, "createBy", SearchCriteria.EQUAL, updateBy);

						if ("list".equals(page)) {
							sc2 = SearchCriteria.andIfNotNull(sc2, "status", SearchCriteria.NOT_EQUAL, Parameter.CMP_PAYMENT_DATA_ENTRY);
						} else {
							sc2 = SearchCriteria.andIfNotNull(sc2, "status", SearchCriteria.EQUAL, status);
						}

						SearchResult sr2 = cmpTransactionDao.findByCriteria(sc2);

						if (sr2 != null) {
							List<CmpTransaction> listTrans = sr2.getList();
							CmpTransaction cmpTrans = listTrans.get(0);
							listCmpTransRet.add(cmpTrans);
						}

						// if(StringUtils.isNotEmpty(updateBy)){
						// SearchCriteria sc2 = new
						// SearchCriteria("cmpTransactionCode",SearchCriteria.EQUAL,
						// cmpMaster.getCmpTransactionCode());
						// sc2 = SearchCriteria.andIfNotNull(sc2, "updateBy",
						// SearchCriteria.EQUAL, updateBy);
						// sc2 = SearchCriteria.andIfNotNull(sc2, "status",
						// SearchCriteria.NOT_EQUAL,
						// Parameter.CMP_PAYMENT_DATA_ENTRY);
						// SearchResult sr2 =
						// cmpTransactionDao.findByCriteria(sc2);
						//
						// if(sr2 != null){
						// List<CmpTransaction> listTrans = sr2.getList();
						// CmpTransaction cmpTrans = listTrans.get(0);
						// listCmpTransRet.add(cmpTrans);
						// }
						//
						// }else{
						// CmpTransaction cmpTrans =
						// cmpTransactionDao.findById(cmpMaster.getCmpTransactionCode());
						// listCmpTransRet.add(cmpTrans);
						// }
					}

				}
			}

		} else {
			SearchCriteria sc = null;
			if (fromDt != null) {
				sc = new SearchCriteria("updateDt", SearchCriteria.GREATER_EQUAL, fromDt);
			}

			if (toDt != null) {
				if (sc == null) {
					sc = new SearchCriteria("updateDt", SearchCriteria.LESS_EQUAL, toDt);
				} else {
					sc = sc.andIfNotNull("updateDt", SearchCriteria.LESS_EQUAL, toDt);
				}
			}

			if (updateBy != null) {
				if (sc == null) {
					sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, updateBy);
				} else {
					sc = sc.andIfNotNull("createBy", SearchCriteria.EQUAL, updateBy);
				}
			}


			if ("list".equals(page)) {
				sc = SearchCriteria.andIfNotNull(sc, "status", SearchCriteria.NOT_EQUAL, Parameter.CMP_PAYMENT_DATA_ENTRY);
			} else {
				sc = SearchCriteria.andIfNotNull(sc, "status", SearchCriteria.EQUAL, status);
			}

			sc.addOrderBy("updateDt", SearchCriteria.DESC);
			SearchResult sr = cmpTransactionDao.findByCriteria(sc);

			if (sr != null) {
				List<CmpTransaction> listCmpTrans = sr.getList();
				if (StringUtils.isNotEmpty(cmpNo)) {
					for (Iterator iterator = listCmpTrans.iterator(); iterator.hasNext();) {
						CmpTransaction cmpTransaction = (CmpTransaction) iterator.next();

						List<CmpMaster> listCmpMaster = cmpMasterService.findByTransCode(cmpTransaction.getCmpTransactionCode());

						if (listCmpMaster != null && !listCmpMaster.isEmpty()) {
							CmpMaster cmpMaster = listCmpMaster.get(0);
							if (cmpMaster.getCmpNo().equals(cmpNo)) {
								listCmpTransRet.add(cmpTransaction);
							}
						}
					}
				} else {
					return listCmpTrans;
				}
			}
		}

		return listCmpTransRet;
	}

	@Override
	@Transactional
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		CmpInfo cmpInfoTemp = (CmpInfo) obj;
		CmpTransaction cmpTrans =  findById(cmpInfoTemp.getCmpTransaction().getCmpTransactionCode());
		if (StringUtils.isNotBlank(paymentTransId)) {
			if (Parameter.CMP_PAYMENT_PENDING_PAYMENT.equals(cmpTrans.getStatus())) {
				LlpPaymentTransaction paymentTransaction = llpPaymentTransactionService.findById(paymentTransId);
				cmpTrans.setPaymentDt(paymentTransaction.getCreateDt());

				cmpTrans.setStatus(Parameter.CMP_PAYMENT_PAYMENT_SUCCESS);
				cmpTransactionDao.update(cmpTrans);
			}
		} else {
			throw new SSMException("No Success payment reciept ,Cannot Cmp Payment WS:" + cmpTrans.getCmpTransactionCode());
		}

	}

	@Override
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
		// TODO Auto-generated method stub
		System.out.println("SUCCESS NOTIFICATION");
		CmpInfo cmpInfoTemp = (CmpInfo) obj;
		CmpTransaction cmpTransaction =  findById(cmpInfoTemp.getCmpTransaction().getCmpTransactionCode());
		obj = cmpTransaction;
		lodgeCompoundPaymentWS(cmpTransaction);
	}

	@Override
	public CompoundPaymentResp lodgeCompoundPaymentWS(CmpTransaction cmpTransaction, LlpPaymentReceipt receipt) throws SSMException {
		List<CmpMaster> listCmpMaster = cmpMasterService.findByTransCode(cmpTransaction.getCmpTransactionCode());
		String url = wSManagementService.getWsUrl("CompoundClient.updateCompoundPayment");

		CompoundPaymentResp ssmWsResp = new CompoundPaymentResp();

		com.ssm.webis.param.CmpMaster[] cmpMasterArr = new com.ssm.webis.param.CmpMaster[listCmpMaster.size()];

		int i = 0;
		for (Iterator iterator = listCmpMaster.iterator(); iterator.hasNext();) {
			CmpMaster cmpMaster = (CmpMaster) iterator.next();
			com.ssm.webis.param.CmpMaster cmpMasterWs = new com.ssm.webis.param.CmpMaster();
			cmpMasterWs.setCmpMasterKeyCode(cmpMaster.getCmpMasterKeyCode());
			cmpMasterWs.setCmpNo(cmpMaster.getCmpNo());
			cmpMasterWs.setCmpStatus(cmpMaster.getCmpStatus());
			cmpMasterWs.setCmpType(cmpMaster.getCmpType());
			cmpMasterWs.setEntityNo(cmpMaster.getEntityNo());
			cmpMasterWs.setEntityType(cmpMaster.getEntityType());
			cmpMasterWs.setReceiptDt(receipt.getCreateDt());
			cmpMasterWs.setReceiptNo(receipt.getReceiptNo());
			cmpMasterWs.setTotalAmt(cmpMaster.getCmpAmt());
			cmpMasterArr[i] = cmpMasterWs;
			i++;
		}

		CompoundPaymentReq param = new CompoundPaymentReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setListCompound(cmpMasterArr);
		param.setReqRefNo(cmpTransaction.getCmpTransactionCode());
		param.setReqDate(new Date());

		try {
			ssmWsResp = CompoundClient.updateCompoundPayment(url, param);
			if ("00".equals(ssmWsResp.getSuccessCode())) {
				CmpTransaction cmpTransactionToUpdate = findById(cmpTransaction.getCmpTransactionCode());
				cmpTransactionToUpdate.setStatus(Parameter.CMP_PAYMENT_CMP_CBS_PAID);
				update(cmpTransactionToUpdate);
			} else {
				throw new SSMException(ssmWsResp.getErrorMsg());
			}
		} catch (Exception fault) {
			System.out.println(fault.getMessage());
			throw new SSMException(fault);
		}

		return ssmWsResp;

	}

	@Override
	public CompoundStatusResp getCmpStatusWS(CmpTransaction cmpTransaction) throws SSMException{
		List<CmpMaster> listCmpMaster = cmpMasterService.findByTransCode(cmpTransaction.getCmpTransactionCode());
		String url = wSManagementService.getWsUrl("CompoundClient.getCompoundStatus");

		CompoundStatusReq param = new CompoundStatusReq();
		param.setAgencyId(Parameter.ROB_AGENCY_ID);
		param.setReqRefNo(cmpTransaction.getCmpTransactionCode());
		param.setReqDate(new Date());
		
		CompoundStatusParam[] compoundStatusParam = new CompoundStatusParam[listCmpMaster.size()];

		int i = 0;
		for (Iterator iterator = listCmpMaster.iterator(); iterator.hasNext();) {
			CmpMaster cmpMaster = (CmpMaster) iterator.next();
			CompoundStatusParam statusParam = new CompoundStatusParam();
			statusParam.setCmpNo(cmpMaster.getCmpNo());
			statusParam.setCmpType(cmpMaster.getCmpType());
			compoundStatusParam[i] = statusParam;
			i++;
		}
		param.setListCompoundStatusParam(compoundStatusParam);
		
		try {
			return CompoundClient.getCompoundStatus(url, param);
		} catch (Exception fault) {
			System.out.println(fault.getMessage());
			throw new SSMException(fault);
		}
	}

	@Override
	public void lodgeCompoundPaymentWS(CmpTransaction cmpTransaction) throws SSMException {
		LlpPaymentTransaction paymentTrans = llpPaymentTransactionService.findSuccessByAppRefNo(cmpTransaction.getCmpTransactionCode());
		LlpPaymentReceipt llpPaymentReceipt = llpPaymentReceiptService.find(paymentTrans.getTransactionId());
		lodgeCompoundPaymentWS(cmpTransaction, llpPaymentReceipt);
	}

}
