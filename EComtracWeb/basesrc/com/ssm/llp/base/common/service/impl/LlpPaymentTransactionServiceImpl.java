/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.service.RobPaymentCreditNoteService;
import com.ssm.gaf.GafCompany;
import com.ssm.gaf.GafFooter;
import com.ssm.gaf.GafLedger;
import com.ssm.gaf.GafSupply;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpPaymentTransactionDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.model.LlpPaymentTransactionDetail;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobPaymentCreditNote;

@Service
public class LlpPaymentTransactionServiceImpl extends BaseServiceImpl<LlpPaymentTransaction, String> implements LlpPaymentTransactionService {
	@Autowired
	LlpPaymentTransactionDao llpPaymentTransactionDao;

	@Autowired
	LlpPaymentTransactionDetailService llpPaymentTransactionDetailService;

	@Autowired
	LlpPaymentReceiptService llpPaymentReceiptService;

	@Autowired
	LlpParametersService llpParametersService;

	@Autowired
	LlpPaymentFeeService llpPaymentFeeService;
	
	@Autowired
	RobPaymentCreditNoteService robPaymentCreditNoteService;
	
	@Override
	public BaseDao getDefaultDao() {
		return llpPaymentTransactionDao;
	}


	@Override
	@Transactional
	public void updateExcelGAFSetup(byte[] byteExcel) throws SSMException {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(byteExcel);
			Map<String, String> mapBranchCodeDB = llpParametersService.findAllCodeTypeAsMap(Parameter.BRANCH_CODE);
			Map<String, String> mapAccCodeCrDB = llpParametersService.findAllCodeTypeAsMap(Parameter.GAF_CONFIG_ACC_CODE_CR);

		    Workbook workbook = Workbook.getWorkbook(bais);
		    Sheet sheet = workbook.getSheet(0);
		    
		    int totalRows = sheet.getRows();
		    int totalColl = sheet.getColumns();
		    
//		    System.out.println(totalRows+":"+totalColl);
		    
		    Map<String, Map<String, LinkedHashMap<String, String>>> mapBranches = new HashMap<String, Map<String, LinkedHashMap<String, String>>>();
		    
		    int row = 2;
		    for (int i = 0; i < totalRows-row; i++) {
				String accountCode = sheet.getCell(0, i+row).getContents().trim();
				if(!mapAccCodeCrDB.containsKey(accountCode)){
//					System.out.println(accountCode);
					LlpParameters llpParameter = llpParametersService.findParameter(Parameter.GAF_CONFIG_ACC_CODE_CR, accountCode);
					if(llpParameter==null || llpParameter.getSeq()==null || llpParameter.getSeq().intValue()<100){
						throw new SSMException("Invalid AccountCode:"+accountCode);
					}
				}
				for (int j = 1; j < totalColl; j++) {
					String branchAndType = sheet.getCell(j, 1).getContents();//Example NS_CREDIT
					String branch = StringUtils.split(branchAndType,"_")[0];// NS
					String type = StringUtils.split(branchAndType,"_")[1]; //CREDIT
					String fullAccountCode = sheet.getCell(j, i+row).getContents().trim();//S-B07-D44-E08-R71250

					
					if(!mapBranchCodeDB.containsKey(branch)){
						throw new SSMException("Invalid branch:"+branch+" cell:"+j+","+1);
					}
					if(StringUtils.isBlank(fullAccountCode)){
						continue;
					}
					
					Map<String, LinkedHashMap<String, String>> mapBranch = mapBranches.get(branch);
					if(mapBranch==null){
						mapBranch = new HashMap<String, LinkedHashMap<String, String>>();
						mapBranches.put(branch, mapBranch);
					}
					if("CREDIT".equals(type)){
						LinkedHashMap<String, String> mapCr = mapBranch.get("CREDIT");
						if(mapCr==null){
							mapCr = new LinkedHashMap<String, String>();
							mapBranch.put("CREDIT", mapCr);
						}
						mapCr.put(accountCode, fullAccountCode);
						
					}else if("DEBIT".equals(type)){
						LinkedHashMap<String, String> mapDebit = mapBranch.get("DEBIT");
						if(mapDebit==null){
							mapDebit = new LinkedHashMap<String, String>();
							mapBranch.put("DEBIT", mapDebit);
						}
						mapDebit.put(accountCode, fullAccountCode);
					}else{
						throw new SSMException("Unknown type:"+type+" cell:"+j+","+1);
					}
					
				}
				
			}
		    
		    
		    
		    for (Iterator iterator = mapBranches.keySet().iterator(); iterator.hasNext();) {
				String branchCode = (String) iterator.next();
				System.out.println(branchCode);
				
				Map<String, String> mapCr = mapBranches.get(branchCode).get("CREDIT");
				String codeTypeCr = Parameter.GAF_CONFIG_ACC_CODE_CR+"_"+branchCode;
				String codeTypeDebit = Parameter.GAF_CONFIG_ACC_CODE_DEBIT+"_"+branchCode;
				
				if("KL".equals(branchCode)){
					codeTypeCr = Parameter.GAF_CONFIG_ACC_CODE_CR;
					codeTypeDebit = Parameter.GAF_CONFIG_ACC_CODE_DEBIT;
				}
				
				
				if(mapCr.size()>0){
					llpParametersService.deleteAllByCodeType(codeTypeCr);
					
					List<LlpParameters> listLlpParameter  = new ArrayList<LlpParameters>();
					for (Iterator iterator2 = mapCr.keySet().iterator(); iterator2.hasNext();) {
						String accountCode = (String) iterator2.next();
						String fullAccountCode = mapCr.get(accountCode);
						LlpParameters parameters = new LlpParameters(accountCode,fullAccountCode,codeTypeCr);
						listLlpParameter.add(parameters);
					}
					
					llpParametersService.insertAll(listLlpParameter);
				}
				
				Map<String, LinkedHashMap<String, String>> s = mapBranches.get(branchCode);
				Map<String, String> mapDebit = mapBranches.get(branchCode).get("DEBIT");
				if(mapDebit.size()>0){
					llpParametersService.deleteAllByCodeType(codeTypeDebit);
					
					List<LlpParameters> listLlpParameter  = new ArrayList<LlpParameters>();
					for (Iterator iterator2 = mapDebit.keySet().iterator(); iterator2.hasNext();) {
						String accountCode = (String) iterator2.next();
						String fullAccountCode = mapDebit.get(accountCode);
						LlpParameters parameters = new LlpParameters(accountCode,fullAccountCode,codeTypeDebit);
						listLlpParameter.add(parameters);
					}
					
					llpParametersService.insertAll(listLlpParameter);
				}
				
				
			}
		    bais.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SSMException(e);
		}
		
		
	}
	
	@Override
	public byte[] generateExcelGAFSetup() throws SSMException {
			
//		if(1==1){
//			updateExcelGAFSetup(null);
//			return null;
//		}
			List<LlpParameters> listBranchCode = llpParametersService.findByActiveCodeType(Parameter.BRANCH_CODE);
			List<LlpParameters>  listAccCodeCr = llpParametersService.findByActiveCodeType(Parameter.GAF_CONFIG_ACC_CODE_CR, "seq,code");
			
			Map<String, String> mapAccCodeCr = llpParametersService.findAllCodeTypeAsMap(Parameter.GAF_CONFIG_ACC_CODE_CR);
			Map<String, String> mapAccCodeDebit = llpParametersService.findAllCodeTypeAsMap(Parameter.GAF_CONFIG_ACC_CODE_DEBIT);
			
			
			Map<String, Map<String, String>> mapAccCodeDebitBranch = new HashMap();
			Map<String, Map<String, String>> mapAccCodeCrBranch = new HashMap();
			for (int i = 0; i < listBranchCode.size(); i++) {
				String branchCode = listBranchCode.get(i).getCode();
				if("KL".equals(branchCode)){
					mapAccCodeCrBranch.put(branchCode, mapAccCodeCr);
					mapAccCodeDebitBranch.put(branchCode, mapAccCodeDebit);
				}else{
					Map<String, String> mapAccCodeCrBrTmp = llpParametersService.findAllCodeTypeAsMap(Parameter.GAF_CONFIG_ACC_CODE_CR+"_"+branchCode);
					mapAccCodeCrBranch.put(branchCode, mapAccCodeCrBrTmp);
					
					Map<String, String> mapAccCodeDebitBrTmp = llpParametersService.findAllCodeTypeAsMap(Parameter.GAF_CONFIG_ACC_CODE_DEBIT+"_"+branchCode);
					mapAccCodeDebitBranch.put(branchCode, mapAccCodeDebitBrTmp);
				}
			}
			
			
			try {
//				FileOutputStream fos = new FileOutputStream("d:/list.xls");
				ByteArrayOutputStream fos = new ByteArrayOutputStream();
				
				WritableCellFormat checkCellFontFmt = new WritableCellFormat();
		        checkCellFontFmt.setBorder(Border.ALL, BorderLineStyle.THIN);
		        checkCellFontFmt.setLocked(false);
		        checkCellFontFmt.setAlignment(Alignment.CENTRE);
		        
		        
		    	WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		        WritableCellFormat headerFmt = new WritableCellFormat();
		        headerFmt.setBorder(Border.ALL, BorderLineStyle.THIN);
		        headerFmt.setLocked(false);
		        headerFmt.setAlignment(Alignment.LEFT);
		        headerFmt.setFont(headerFont);
		        headerFmt.setBackground(Colour.GRAY_25);
		        
		        WritableCellFormat headerFmtOth = new WritableCellFormat();
		        headerFmtOth.setBorder(Border.ALL, BorderLineStyle.THIN);
		        headerFmtOth.setLocked(false);
		        headerFmtOth.setAlignment(Alignment.LEFT);
		        headerFmtOth.setFont(headerFont);
		        headerFmtOth.setBackground(Colour.BLUE_GREY);
		        
				WritableWorkbook w = Workbook.createWorkbook(fos);
				WritableSheet s = w.createSheet("GAF SETUP", 0);
				
				

				SheetSettings settings = s.getSettings();
				settings.setHorizontalFreeze(1);
				settings.setVerticalFreeze(2);
				
				
				
				// Title
				s.addCell(new Label(0, 0, "EZBIZ GAF SETUP", headerFmt));
				int col = 1;
				
				s.addCell(new Label(col, 1, "KL"+"_CREDIT", headerFmt));
				col++;
				s.addCell(new Label(col, 1, "KL"+"_DEBIT", headerFmt));
				col++;
				
				for (int j = 0; j < listBranchCode.size(); j++) {
					String branchCode = listBranchCode.get(j).getCode();
					if("KL".equals(branchCode)){
						continue;
					}
					s.addCell(new Label(col, 1, branchCode+"_CREDIT", headerFmt));
					col++;
					s.addCell(new Label(col, 1, branchCode+"_DEBIT", headerFmt));
					col++;
				}
				
				
				int row = 2;
				Set setResizeColl = new HashSet();
				for (int i = 0; i < listAccCodeCr.size(); i++) {
					String accCode = listAccCodeCr.get(i).getCode();

					col = 0;
					if(listAccCodeCr.get(i).getSeq()!=null && listAccCodeCr.get(i).getSeq().intValue()>=100){
						s.addCell(new Label(col, i+row, accCode, headerFmtOth));
					}else{
						s.addCell(new Label(col, i+row, accCode, headerFmt));
					}
					
					col++;
					
					
					
					Map<String, String> mapAccCodeCrBrTmp = mapAccCodeCrBranch.get("KL");
					if(mapAccCodeCrBrTmp.size()>0){
						s.addCell(new Label(col, i+row, mapAccCodeCrBrTmp.get(accCode), checkCellFontFmt));
						setResizeColl.add(col);
					}
					col++;
					
					Map<String, String> mapAccCodeDebitBrTmp = mapAccCodeDebitBranch.get("KL");
					if(mapAccCodeDebitBrTmp.size()>0){
						s.addCell(new Label(col, i+row, mapAccCodeDebitBrTmp.get(accCode), checkCellFontFmt));
						setResizeColl.add(col);
					}
					col++;
					
					for (int j = 0; j < listBranchCode.size(); j++) {
						String branchCode = listBranchCode.get(j).getCode();
						
						if("KL".equals(branchCode)){
							continue;
						}
						mapAccCodeCrBrTmp = mapAccCodeCrBranch.get(branchCode);
						if(mapAccCodeCrBrTmp.size()>0){
							s.addCell(new Label(col, i+row, mapAccCodeCrBrTmp.get(accCode), checkCellFontFmt));
							setResizeColl.add(col);
						}else{
							s.addCell(new Label(col, i+row, "", checkCellFontFmt));
						}
						
						col++;
						
						mapAccCodeDebitBrTmp = mapAccCodeDebitBranch.get(branchCode);
						if(mapAccCodeDebitBrTmp.size()>0){
							s.addCell(new Label(col, i+row, mapAccCodeDebitBrTmp.get(accCode), checkCellFontFmt));
							setResizeColl.add(col);
						}else{
							s.addCell(new Label(col, i+row, "", checkCellFontFmt));
						}
						col++;
					}
				}
				
				
				//By Default All 12 Width
				int totalCol = s.getColumns();
				for (int j = 0; j < totalCol-1; j++) {
					s.setColumnView(j, 12);
				}
				
				
				s.setColumnView(0, 25);
				for (Iterator iterator = setResizeColl.iterator(); iterator.hasNext();) {
					Integer coll = (Integer) iterator.next();
					s.setColumnView(coll, 25);
				}
				
				w.write();
				w.close();
				
				byte byteDataExcel[] = fos.toByteArray();
				fos.close();
				return byteDataExcel;
			} catch (Exception e) {
				// TODO: handle exception
			}
						
			
			
			
			
			
			
			
			return null;
	}	
	
	@Override
	public byte[] generateExcel(Date searchFromDt, Date searchToDt, String paymentMode, String transactionId, String status, String refNo, String paymentGroup) throws SSMException {

		if(searchFromDt==null || searchToDt==null){
			throw new SSMException("From and To Date cannot be null!!");
		}
		
		
		
		try {
			
				String paymentGroupPrefix = "";
				if (StringUtils.isNotBlank((paymentGroup))) {
					paymentGroupPrefix = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_GROUP_REFNO_PREFIX, paymentGroup);
				}
				List<LlpPaymentTransaction> listPayment = llpPaymentTransactionDao.findDetailPaymentForGaf(searchFromDt, searchToDt, paymentMode, transactionId, status, refNo, paymentGroupPrefix);
				
				ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
				if (!listPayment.isEmpty()) {
					
					Map<String, String> mapCodeTypePaymentStatus = llpParametersService.findAllCodeTypeAsMap(Parameter.PAYMENT_STATUS);
					Map<String, String> mapCodeTypePaymentType = llpParametersService.findAllCodeTypeAsMap(Parameter.PAYMENT_TYPE);
					
					WritableWorkbook w = Workbook.createWorkbook(bAOS);
					WritableSheet s = w.createSheet("PAYMENT TRANSACTION", 0);
					// Title
					s.addCell(new Label(0, 0, "EZBIZ PAYMENT TRANSACTION REPORT"));
					// Header
					int headerColIdx = 0;
					s.addCell(new Label(headerColIdx++, 2, "REQUEST DATE"));
					s.addCell(new Label(headerColIdx++, 2, "TRANSACTION ID"));
					s.addCell(new Label(headerColIdx++, 2, "APPROVAL CODE"));
					s.addCell(new Label(headerColIdx++, 2, "PAYMENT MODE"));
					s.addCell(new Label(headerColIdx++, 2, "PAYMENT DETAIL"));
					s.addCell(new Label(headerColIdx++, 2, "RECEIPT NO"));
					s.addCell(new Label(headerColIdx++, 2, "INVOICE NO"));
					s.addCell(new Label(headerColIdx++, 2, "CREDITE NOTE NO"));
					s.addCell(new Label(headerColIdx++, 2, "ORIGINAL INVOICE NO"));
					s.addCell(new Label(headerColIdx++, 2, "PAYMENT ITEM"));
					s.addCell(new Label(headerColIdx++, 2, "AMOUNT"));
					s.addCell(new Label(headerColIdx++, 2, "GST"));
					s.addCell(new Label(headerColIdx++, 2, "TOTAL"));
					s.addCell(new Label(headerColIdx++, 2, "INVOICE_AMT"));
					s.addCell(new Label(headerColIdx++, 2, "PAYER NAME"));
					s.addCell(new Label(headerColIdx++, 2, "PAYER ID"));
					s.addCell(new Label(headerColIdx++, 2, "APPLICATION REF NO"));
					s.addCell(new Label(headerColIdx++, 2, "STATUS"));

					// Format
					NumberFormat twodpsNF = new NumberFormat("#0.00");
					WritableCellFormat twodpsFormat = new WritableCellFormat(twodpsNF);

					int currRow = 2;
					for (Iterator iterator = listPayment.iterator(); iterator.hasNext();) {
						LlpPaymentTransaction llpPaymentTransaction = (LlpPaymentTransaction) iterator.next();
					
						String receieptNo = "";
						String crNoteNo = "";
						String invoiceNo = "";
						String originalInvoiceNo = "";
						
						if (Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())) {
							if(Parameter.PAYMENT_MODE_CN.equals(llpPaymentTransaction.getPaymentMode())){
								RobPaymentCreditNote creditNote = llpPaymentTransaction.getRobPaymentCreditNote();
								if(creditNote==null){
									throw new SSMException("CreditNote not generated for transaction: "+llpPaymentTransaction.getTransactionId());
								}
								crNoteNo = creditNote.getCreditNoteNo();
								originalInvoiceNo = creditNote.getPaymentReceiptNo().getTaxInvoiceNo();
							}else{
								LlpPaymentReceipt llpPaymentReceipt = llpPaymentTransaction.getLlpPaymentReceipt();
								if(llpPaymentReceipt==null){
									throw new SSMException("Reciept not generated for transaction: "+llpPaymentTransaction.getTransactionId());
								}
								receieptNo = llpPaymentReceipt.getReceiptNo();
								if(StringUtils.isNotBlank(llpPaymentReceipt.getTaxInvoiceNo())){
									invoiceNo = llpPaymentReceipt.getTaxInvoiceNo();
								}
							}
						}

						List<LlpPaymentTransactionDetail> list = llpPaymentTransaction.getLlpPaymentTransactionDetails();
						for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
							++currRow;
							LlpPaymentTransactionDetail llpPaymentTransactionDetail = (LlpPaymentTransactionDetail) iterator2.next();
							int colIdx = 0;
							s.addCell(new Label(colIdx++, currRow, llpPaymentTransaction.getRequestDt().toString()));
							s.addCell(new Label(colIdx++, currRow, llpPaymentTransaction.getTransactionId()));
							s.addCell(new Label(colIdx++, currRow, llpPaymentTransaction.getApprovalCode()));
							s.addCell(new Label(colIdx++, currRow, llpPaymentTransaction.getPaymentMode()));
							s.addCell(new Label(colIdx++, currRow, llpPaymentTransaction.getPaymentDetail()));
							s.addCell(new Label(colIdx++, currRow, receieptNo));
							s.addCell(new Label(colIdx++, currRow, invoiceNo));
							s.addCell(new Label(colIdx++, currRow, crNoteNo));
							s.addCell(new Label(colIdx++, currRow, originalInvoiceNo));
							s.addCell(new Label(colIdx++, currRow, mapCodeTypePaymentType.get(llpPaymentTransactionDetail.getPaymentItem())));

							s.addCell(new jxl.write.Number(colIdx++, currRow, llpPaymentTransactionDetail.getAmountWithOutGst(), twodpsFormat));
							s.addCell(new jxl.write.Number(colIdx++, currRow, llpPaymentTransactionDetail.getGstAmt(), twodpsFormat));
							s.addCell(new jxl.write.Number(colIdx++, currRow, llpPaymentTransactionDetail.getAmount(), twodpsFormat));
							s.addCell(new jxl.write.Number(colIdx++, currRow, llpPaymentTransaction.getAmount(), twodpsFormat));
							s.addCell(new Label(colIdx++, currRow, llpPaymentTransaction.getPayerName()));
							s.addCell(new Label(colIdx++, currRow, llpPaymentTransaction.getPayerId()));
							s.addCell(new Label(colIdx++, currRow, llpPaymentTransaction.getAppRefNo()));
							s.addCell(new Label(colIdx++, currRow, mapCodeTypePaymentStatus.get(llpPaymentTransaction.getStatus()) ));
						}

					}

					w.write();
					w.close();
				}
				byte byteDataExcel[] = bAOS.toByteArray();
				bAOS.close();
				return byteDataExcel;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SSMException(e);
		} 
		
	}

	@Override
	public byte[] generateGaf(Date searchFromDt, Date searchToDt, String paymentMode, String transactionId, String status, String refNo, String paymentGroup) throws SSMException {

		SimpleDateFormat gafDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat gafDecimalFormat = new DecimalFormat("#0.00");
		if(searchFromDt==null || searchToDt==null){
			throw new SSMException("From and To Date cannot be null!!");
		}
		if(!Parameter.PAYMENT_STATUS_SUCCESS.equals(status)){
			throw new SSMException("GAF can only generate for success only!!");
		}
		
		String paymentGroupPrefix = "";
		if (StringUtils.isNotBlank((paymentGroup))) {
			paymentGroupPrefix = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_GROUP_REFNO_PREFIX, paymentGroup);
		}
		
		
		List<LlpPaymentTransaction> listPayment = llpPaymentTransactionDao.findDetailPaymentForGaf(searchFromDt, searchToDt, paymentMode, transactionId, status, refNo, paymentGroupPrefix);
		
		try {

			if (listPayment.size() > 0) {

				// (1)COMPANY
				GafCompany gC = new GafCompany();

				gC.setPeriodStart(gafDateFormat.format(searchFromDt));
				gC.setPeriodEnd(gafDateFormat.format(searchToDt));
				gC.setFileCreationDt(gafDateFormat.format(new Date()));

				// (2)SUPPLY
				List listSupply = new ArrayList<GafSupply>();
				List<LlpParameters> listPaymentType = llpParametersService.findByActiveCodeType(Parameter.GAF_CONFIG_ACC_CODE_CR);
				Map<String, String> mapPaymentModeAccountName = llpParametersService.findAllCodeTypeAsMap(Parameter.GAF_CONFIG_ACC_PAY_MODE);
				Map<String, String> mapAccCodeCr = llpParametersService.findAllCodeTypeAsMap(Parameter.GAF_CONFIG_ACC_CODE_CR);
				Map<String, String> mapAccCodeDebit = llpParametersService.findAllCodeTypeAsMap(Parameter.GAF_CONFIG_ACC_CODE_DEBIT);
				List<LlpParameters> listBranchCode = llpParametersService.findByActiveCodeType(Parameter.BRANCH_CODE);
				
				
				
				Map<String, Map<String, String>> mapAccCodeCrBranch = new HashMap();
				for (int i = 0; i < listBranchCode.size(); i++) {
					String branchCode = listBranchCode.get(i).getCode();
					if("KL".equals(branchCode)){
						mapAccCodeCrBranch.put(branchCode, mapAccCodeCr);
					}else{
						Map<String, String> mapAccCodeCrBrTmp = llpParametersService.findAllCodeTypeAsMap(Parameter.GAF_CONFIG_ACC_CODE_CR+"_"+branchCode);
						mapAccCodeCrBranch.put(branchCode, mapAccCodeCrBrTmp);
					}
				}
				
				
				Map<String, Map<String, String>> mapAccCodeDebitBranch = new HashMap();
				for (int i = 0; i < listBranchCode.size(); i++) {
					String branchCode = listBranchCode.get(i).getCode();
					if("KL".equals(branchCode)){
						mapAccCodeDebitBranch.put(branchCode, mapAccCodeDebit);
					}else{
						Map<String, String> mapAccCodeCrBrTmp = llpParametersService.findAllCodeTypeAsMap(Parameter.GAF_CONFIG_ACC_CODE_DEBIT+"_"+branchCode);
						mapAccCodeDebitBranch.put(branchCode, mapAccCodeCrBrTmp);
					}
				}
				
				TreeMap<String, List> mapLedgerDebitBranch = new TreeMap<String, List>();
				for (int i = 0; i < listBranchCode.size(); i++) {
					String branchCode = listBranchCode.get(i).getCode();
					mapLedgerDebitBranch.put(branchCode, new ArrayList<GafLedger>());
				}
				
				TreeMap<String, TreeMap<String, List>> mapLedgerCreditBranch = new TreeMap<String, TreeMap<String, List>>();
				for (int i = 0; i < listBranchCode.size(); i++) {
					String branchCode = listBranchCode.get(i).getCode();
					
					mapLedgerCreditBranch.put(branchCode, new TreeMap<String, List>());
				}
				
				
				TreeMap<String, List> mapSupply = new TreeMap<String, List>();
				for (int i = 0; i < listPaymentType.size(); i++) {
					mapSupply.put(listPaymentType.get(i).getCode(), new ArrayList());
				}
				
				int lineNo = 1;
				Double totalSupplyAmt = new Double(0);
				Double totalSupplyGstAmt = new Double(0);
				
				
				// (3)LEDGER
				List listLedgerDebit = new ArrayList<GafLedger>();
				
				String chartOfAccDebit = llpParametersService.findByCodeTypeValue(Parameter.GAF_CONFIG , Parameter.GAF_CONFIG_GAF_CHART_ACC_CODE_DEBIT);
				String OPENING_BALANCE = llpParametersService.findByCodeTypeValue(Parameter.GAF_CONFIG , Parameter.GAF_CONFIG_GAF_OP_BAL);
				String gstCtrlAcoount = llpParametersService.findByCodeTypeValue(Parameter.GAF_CONFIG , Parameter.GAF_CONFIG_GST_CTRL_ACCOUNT);
//				String cashMovementAccountCodeDebit = llpParametersService.findByCodeTypeValue(Parameter.GAF_CONFIG , Parameter.GAF_CONFIG_CASH_MOVE_ACCOUNT_DEBIT);
//				String cashMovementAccountCodeCredit = llpParametersService.findByCodeTypeValue(Parameter.GAF_CONFIG , Parameter.GAF_CONFIG_CASH_MOVE_ACCOUNT_CREDIT);
				String cashMovementTransDesc = llpParametersService.findByCodeTypeValue(Parameter.GAF_CONFIG , Parameter.GAF_CONFIG_CASH_MOVE_TRANS_DESC);
				String cashMovementSourceType = llpParametersService.findByCodeTypeValue(Parameter.GAF_CONFIG , Parameter.GAF_CONFIG_CASH_MOVE_SRC_TYPE);
				
				Double totalLedgerDebitAmt = new Double(0);
				Double totalLedgerCreditAmt = new Double(0);
				

				GafLedger gLBalance = new GafLedger();
				gLBalance.setTransactionDate(gafDateFormat.format(new Date()));
				gLBalance.setAccountID(chartOfAccDebit);
				gLBalance.setTransDesc(OPENING_BALANCE);
				listLedgerDebit.add(gLBalance);

				
//				List<LlpPaymentReceipt> listOTCResitWithNoZeroAmt = new ArrayList<LlpPaymentReceipt>();
				
				LinkedHashMap<String, Object[]> mapOTCResitWithNoZeroAmt = new LinkedHashMap<String, Object[]>();
				
				for (Iterator iterator = listPayment.iterator(); iterator.hasNext();) {
					LlpPaymentTransaction paymentTrans = (LlpPaymentTransaction) iterator.next();
//					System.out.println("PaymentTransaction:"+paymentTrans.getTransactionId());
					
					String receieptOrCrNoteNo = null;
					Date receieptOrCrNoteCrDate = null;
					
					String accountName;
					String branchCode="KL";
					String bankSlipNo="";
					boolean isCreditNoteTxn = false;
					if(Parameter.PAYMENT_MODE_CN.equals(paymentTrans.getPaymentMode())){
						isCreditNoteTxn = true;
						RobPaymentCreditNote creditNote = paymentTrans.getRobPaymentCreditNote();
						if(creditNote==null){
							throw new SSMException("CreditNote not generated for transaction: "+paymentTrans.getTransactionId());
						}
						receieptOrCrNoteNo = creditNote.getCreditNoteNo();
						receieptOrCrNoteCrDate = creditNote.getCreateDt();
						
						LlpPaymentTransaction oriPaymentTrans =  llpPaymentTransactionDao.findById(creditNote.getPaymentReceiptNo().getTransactionId());;
						accountName = mapPaymentModeAccountName.get(oriPaymentTrans.getPaymentMode());
					}else{
						LlpPaymentReceipt paymentReceipt = paymentTrans.getLlpPaymentReceipt();
						if(paymentReceipt==null){
							throw new SSMException("Reciept not generated for transaction: "+paymentTrans.getTransactionId());
						}
						if(StringUtils.isNotBlank(paymentReceipt.getTaxInvoiceNo())){//Choose Tax Invoice if have
							receieptOrCrNoteNo = paymentReceipt.getTaxInvoiceNo();
						}else{
							receieptOrCrNoteNo = paymentReceipt.getReceiptNo();
						}
						if(StringUtils.isBlank(receieptOrCrNoteNo)){
							throw new SSMException("Reciept not generated for transaction: "+paymentTrans.getTransactionId());
						}
						if(paymentReceipt.getTotalAmount()!=0 && paymentReceipt.getCounterSessionId()!=null){
							if(mapOTCResitWithNoZeroAmt.get(receieptOrCrNoteNo)==null){
								mapOTCResitWithNoZeroAmt.put(receieptOrCrNoteNo, new Object[]{paymentReceipt,paymentTrans});
							}
						}
						
						receieptOrCrNoteCrDate = paymentReceipt.getCreateDt();
						accountName = mapPaymentModeAccountName.get(paymentTrans.getPaymentMode());
						if(paymentReceipt.getCounterSessionId() != null){
							branchCode = paymentReceipt.getCounterSessionId().getBranch();
							if(paymentReceipt.getCounterSessionId().getCounterBankinSlipNo()!=null){
								bankSlipNo = paymentReceipt.getCounterSessionId().getCounterBankinSlipNo().getBankinSlipNo();
							}
						}
					}
					if(StringUtils.isBlank(accountName)){
						throw new SSMException("Account Name for Transaction ID: "+paymentTrans.getTransactionId()+ " is blank or null");
					}
					
					
					
					
					List<GafLedger> listLedgerDebitBranch = mapLedgerDebitBranch.get(branchCode);
					
					Map<String, List> mapLedgerCreditByType = mapLedgerCreditBranch.get(branchCode);
					
					List<LlpPaymentTransactionDetail> paymentDetailList = paymentTrans.getLlpPaymentTransactionDetails();
					lineNo = 1;
					boolean hasStudentIncv = false;
					boolean hasOkuIncv = false;
					for (int i = 0; i < paymentDetailList.size(); i++) {
						
						LlpPaymentTransactionDetail paymentDetail = paymentDetailList.get(i);
						// Gaf Supply Preparation
						GafSupply gS = new GafSupply();
						gS.setCustomerName(paymentTrans.getPayerName());
						gS.setCustomerBRN(paymentTrans.getPayerId());
						gS.setInvoiceDate(gafDateFormat.format(receieptOrCrNoteCrDate));
						gS.setInvoiceNo(receieptOrCrNoteNo);
						gS.setProductDesc(paymentDetail.getPaymentItem());
						gS.setSalesValue(gafDecimalFormat.format(paymentDetail.getAmountWithOutGst()));
						gS.setSalesGSTValue(gafDecimalFormat.format(paymentDetail.getGstAmt()));
//						if(paymentDetail.getGstAmt()>0){
//							gS.setTaxCode("SR");
//						}
						gS.setTaxCode(paymentDetail.getGstCode());
						if(paymentDetail.getPaymentItem().indexOf("_STUD")!=-1){
							if(!hasStudentIncv){
								lineNo=1;
								hasStudentIncv = true;
							}
							gS.setInvoiceNo(receieptOrCrNoteNo+"-1P");
						}
						
						if(paymentDetail.getPaymentItem().indexOf("_OKU")!=-1){
							if(!hasOkuIncv){
								lineNo=1;
								hasOkuIncv = true;
							}
							gS.setInvoiceNo(receieptOrCrNoteNo+"-OKU");
						}
						gS.setLineNo(String.valueOf(lineNo));
						gS.setPaymentMode(paymentTrans.getPaymentMode());
						totalSupplyAmt += new Double(gS.getSalesValue());
						totalSupplyGstAmt += new Double(gS.getSalesGSTValue());
						
						listSupply.add(gS);
						// Gaf Ledger Debit Preparation
						GafLedger gL = new GafLedger();
						gL.setTransDesc(gS.getProductDesc());
						gL.setAccountName(accountName);
						gL.setBranchCode(branchCode);
						gL.setBankSlipNo(bankSlipNo);
						

						if(mapAccCodeDebitBranch.get(branchCode).get(gL.getTransDesc())==null){
							throw new SSMException("GAF DEBIT Account Code for >> "+branchCode+":"+gL.getTransDesc()+" not configure.  Payment ID :"+paymentTrans.getTransactionId());
						}
						
						
						gL.setTransactionDate(gS.getInvoiceDate());
						gL.setAccountID(mapAccCodeDebitBranch.get(branchCode).get(gL.getTransDesc()));
						gL.setName(gS.getCustomerName());
						gL.setTransId( paymentDetail.getTransactionId()+"/"+paymentTrans.getAppRefNo());
						gL.setSourceDocId(gS.getInvoiceNo());
						
//						if(isCreditNoteTxn){
//							gL.setSourceType("ARCN");
//						}
//						if(paymentDetail.getPaymentItem().indexOf("_STUD")!=-1){
//							gL.setSourceType("ARCN");
//						}
						if ((isCreditNoteTxn)||(paymentDetail.getPaymentItem().indexOf("_STUD")!=-1)||(paymentDetail.getPaymentItem().indexOf("_OKU")!=-1)) {
							gL.setSourceType("ARCN");
						}
						
						gL.setTaxCode(gS.getTaxCode());
						if(Double.parseDouble(gS.getSalesValue())<0){//mean incentive or Credit Note
							gL.setCredit(StringUtils.remove(gS.getSalesValue(),"-"));
//							if(Double.parseDouble(gS.getSalesGSTValue())!=0){
								//EVEN SR need to set OS for GL akaun S-H01-000-000-A16003 
								double salePlusGst = (Double.parseDouble(gS.getSalesValue()) + Double.parseDouble(gS.getSalesGSTValue())) * -1;//Remove -ve value
								gL.setCredit(gafDecimalFormat.format(salePlusGst));
//							}
						}else{
							gL.setDebit(gS.getSalesValue());
//							if(Double.parseDouble(gS.getSalesGSTValue())!=0){
//								gL.setTaxCode("OS");//EVEN SR need to set OS for GL akaun S-H01-000-000-A16003 
								double salePlusGst = Double.parseDouble(gS.getSalesValue())+ Double.parseDouble(gS.getSalesGSTValue());
								gL.setDebit(gafDecimalFormat.format(salePlusGst));
//							}
						}
						
						
						
						totalLedgerDebitAmt += new Double(gL.getDebit());
						totalLedgerCreditAmt += new Double(gL.getCredit());
						listLedgerDebit.add(gL);
						
						listLedgerDebitBranch.add(gL);
						
						
						if(mapAccCodeCrBranch.get(branchCode).get(gL.getTransDesc())==null){
							throw new SSMException("GAF CREDIT Account Code for >> "+branchCode+":"+gL.getTransDesc()+" not configure.  Payment ID :"+paymentTrans.getTransactionId());
						}
						

						List listLedgerCredit = mapLedgerCreditByType.get(paymentDetail.getPaymentItem());
						if(listLedgerCredit==null){
							listLedgerCredit = new ArrayList<GafLedger>();
						}
						
						
						GafLedger gLcredit = new GafLedger();
						gLcredit.setTransactionDate(gL.getTransactionDate());
						gLcredit.setAccountID(mapAccCodeCrBranch.get(branchCode).get(gL.getTransDesc()));
						gLcredit.setName(gL.getName());
						gLcredit.setTransId(gL.getTransId());
						gLcredit.setSourceDocId(gL.getSourceDocId());
						gLcredit.setSourceType(gL.getSourceType());
						gLcredit.setTransDesc(gL.getTransDesc());
						gLcredit.setAccountName(gL.getAccountName());
						gLcredit.setBranchCode(branchCode);
						gLcredit.setBankSlipNo(bankSlipNo);
						gLcredit.setTaxCode(gL.getTaxCode());
						
						if(Double.parseDouble(gS.getSalesValue())<0){//mean incentive or Credit Note
							gLcredit.setDebit(StringUtils.remove(gS.getSalesValue(),"-"));//Remove -ve value
//							if(Double.parseDouble(gS.getSalesGSTValue())!=0){
//								gLcredit.setTaxCode("SR");
								gLcredit.setOutputGSTAmount(StringUtils.remove(gS.getSalesGSTValue(),"-"));
//							}
						}else{
							gLcredit.setCredit(gS.getSalesValue());
//							if(Double.parseDouble(gS.getSalesGSTValue())!=0){
//								gLcredit.setTaxCode("SR");
								gLcredit.setOutputGSTAmount(gS.getSalesGSTValue());
//							}
						}
						if("SR".equals(gL.getTaxCode())){
							gLcredit.setGstControlAccount(gstCtrlAcoount);
						}
						
						listLedgerCredit.add(gLcredit);
						mapLedgerCreditByType.put(paymentDetail.getPaymentItem(), listLedgerCredit);
						
						
						mapSupply.get(paymentDetail.getPaymentItem()).add(new Object[]{gS,gLcredit});
						lineNo++;
						
					}
				}
				
				
				TreeMap<String, List> mapLedgerCreditByType = new TreeMap<String, List>();
				
				
//				Double totalLedgerCreditGstAmt = new Double(0);
				
				for (int i = 0; i < listPaymentType.size(); i++) {
					String paymentItemCode = listPaymentType.get(i).getCode();
//					System.out.println(paymentItemCode);
					List<Object[]> listByType = mapSupply.get(paymentItemCode);
					if(listByType.size()==0){
						continue;//no need to generate Ledger
					}
					String taxCode = llpPaymentFeeService.findById(paymentItemCode).getGstCode();
					String ledgerAccCodeCredit = mapAccCodeCr.get(paymentItemCode);
					
					List<GafLedger> listGafLedgerByType = new ArrayList<GafLedger>();
					mapLedgerCreditByType.put(paymentItemCode, listGafLedgerByType);
					
					GafLedger gLCredit = new GafLedger();
					gLCredit.setTransactionDate(gafDateFormat.format(new Date()));
					gLCredit.setAccountID(ledgerAccCodeCredit);
					gLCredit.setTransDesc(OPENING_BALANCE);
					gLCredit.setTaxCode(taxCode);
					listGafLedgerByType.add(gLCredit);
					
					for (int j = 0; j < listByType.size(); j++) {
						GafSupply gSupplyByType = (GafSupply) listByType.get(j)[0];
						GafLedger gLCreditByType = (GafLedger) listByType.get(j)[1];

						GafLedger gL = new GafLedger();
						gL.setTransactionDate(gLCreditByType.getTransactionDate());
						gL.setAccountID(ledgerAccCodeCredit);
						gL.setName(gLCreditByType.getName());
						gL.setTransId(gLCreditByType.getTransId());
						gL.setSourceDocId(gLCreditByType.getSourceDocId());
						gL.setSourceType(gLCreditByType.getSourceType());
						gL.setTaxCode(gLCreditByType.getTaxCode());
						
						if(Double.parseDouble(gSupplyByType.getSalesValue())<0){//mean incentive or Credit Note
							gL.setDebit(StringUtils.remove(gSupplyByType.getSalesValue(),"-"));//Remove -ve value
//							if(Double.parseDouble(gSupplyByType.getSalesGSTValue())!=0){
								gL.setOutputGSTAmount(StringUtils.remove(gSupplyByType.getSalesGSTValue(),"-"));
//							}
						}else{
							gL.setCredit(gSupplyByType.getSalesValue());
//							if(Double.parseDouble(gSupplyByType.getSalesGSTValue())!=0){
								gL.setOutputGSTAmount(gSupplyByType.getSalesGSTValue());
								
//							}
						}
						if("SR".equals(gL.getTaxCode())){
							gL.setGstControlAccount(gstCtrlAcoount);
						}
						
						gL.setTransDesc(gLCreditByType.getTransDesc());
						gL.setAccountName(gLCreditByType.getAccountName());
						totalLedgerCreditAmt += new Double(gL.getCredit());
						totalLedgerDebitAmt += new Double(gL.getDebit());
//						totalLedgerCreditGstAmt += new Double(gL.getOutputGSTAmount());
						listGafLedgerByType.add(gL);
					}
				}
				
				//Cash Movement
				
				int totalCashMovementLedger = 0;
				List<GafLedger> listLedgerDebitCashMovement = new ArrayList<GafLedger>();
				List<GafLedger> listLedgerCreditCashMovement = new ArrayList<GafLedger>();
				
				for (Iterator iterator = mapOTCResitWithNoZeroAmt.keySet().iterator(); iterator.hasNext();) {
					String resitOrInvoice = (String) iterator.next();
					Object obj[] = (Object[]) mapOTCResitWithNoZeroAmt.get(resitOrInvoice);
					LlpPaymentReceipt paymentReceipt = (LlpPaymentReceipt) obj[0];
					String counterBranch = paymentReceipt.getCounterSessionId().getBranch();
					LlpPaymentTransaction llpPaymentTransaction = (LlpPaymentTransaction) obj[1];
					String cashMovementAccountCodeDebit = mapAccCodeDebitBranch.get(counterBranch).get("CASH_MOVE_ACCOUNT_CODE");
					String cashMovementAccountCodeCredit = mapAccCodeCrBranch.get(counterBranch).get("CASH_MOVE_ACCOUNT_CODE");
					
					String branchCode = "";
					String bankInSlipNo = "";
					
					if(paymentReceipt.getCounterSessionId()!=null && paymentReceipt.getCounterSessionId().getCounterBankinSlipNo()!=null){
						bankInSlipNo = paymentReceipt.getCounterSessionId().getCounterBankinSlipNo().getBankinSlipNo();
					}
					
					GafLedger gLDebit = new GafLedger();
					gLDebit.setTransactionDate(gafDateFormat.format(paymentReceipt.getCreateDt()));
					gLDebit.setAccountID(cashMovementAccountCodeDebit);
					gLDebit.setAccountName(mapPaymentModeAccountName.get(Parameter.PAYMENT_MODE_CASH));
					gLDebit.setTransDesc(cashMovementTransDesc);
					gLDebit.setName(llpPaymentTransaction.getPayerName());
					gLDebit.setTransId(llpPaymentTransaction.getTransactionId()+"/"+llpPaymentTransaction.getAppRefNo()+"/"+bankInSlipNo);
					gLDebit.setSourceDocId(resitOrInvoice);
					gLDebit.setSourceType(cashMovementSourceType);
					
					
					GafLedger gLCredit = new GafLedger();
					gLCredit.setTransactionDate(gLDebit.getTransactionDate());
					gLCredit.setAccountID(cashMovementAccountCodeCredit);
					gLCredit.setAccountName(gLDebit.getAccountName());
					gLCredit.setTransDesc(gLDebit.getTransDesc());
					gLCredit.setName(gLDebit.getName());
					gLCredit.setTransId(gLDebit.getTransId());
					gLCredit.setSourceDocId(gLDebit.getSourceDocId());
					gLCredit.setSourceType(gLDebit.getSourceType());
						
					if(paymentReceipt.getTotalAmount()>0 ){//mean incentive or Credit Note
						gLDebit.setDebit(gafDecimalFormat.format(paymentReceipt.getTotalAmount()));
					}else{
						gLDebit.setCredit(gafDecimalFormat.format(paymentReceipt.getTotalAmount()));
					}
					
					gLCredit.setCredit(gLDebit.getDebit());
					gLCredit.setDebit(gLDebit.getCredit());
					
					totalLedgerCreditAmt += new Double(gLDebit.getCredit()) + new Double(gLCredit.getCredit());;
					totalLedgerDebitAmt += new Double(gLDebit.getDebit()) + new Double(gLCredit.getDebit());;
					listLedgerDebitCashMovement.add(gLDebit);
					listLedgerCreditCashMovement.add(gLCredit);
					
					totalCashMovementLedger+=2;
				}
				
				

				// set to byte file
				// append GC
				StringBuffer sb = new StringBuffer();
				sb.append(convertObjToStringPipeDelimited(gC));
				sb.append(System.getProperty("line.separator"));
				
				String salesSrMappingIfGST0 = llpParametersService.findByCodeTypeValue(Parameter.GAF_CONFIG, Parameter.GAF_CONFIG_SALES_SR_MAPPING_IF_GST_0);
				// append GS
				for (Iterator iterator = listSupply.iterator(); iterator.hasNext();) {
					GafSupply gS = (GafSupply) iterator.next();
					if(Parameter.PAYMENT_GST_CODE_SR.equals(gS.getTaxCode())&& StringUtils.isNotBlank(gS.getSalesGSTValue())){
						if(Double.parseDouble(gS.getSalesGSTValue())==0){
							gS.setTaxCode(salesSrMappingIfGST0);
						}
					}
					sb.append(convertObjToStringPipeDelimited(gS));
					sb.append(System.getProperty("line.separator"));
				}

				int totalLedger = 0;
				System.out.println("DEBIT");
				for (Iterator iterator = mapLedgerDebitBranch.keySet().iterator(); iterator.hasNext();) {
					String branchCode = (String) iterator.next();
					List<GafLedger> listGlDebit = (List<GafLedger>) mapLedgerDebitBranch.get(branchCode);
					for (int i = 0; i < listGlDebit.size(); i++) {
						GafLedger gLDebit = listGlDebit.get(i);
						gLDebit.setTaxCode("OS");
						sb.append(convertObjToStringPipeDelimited(gLDebit));
						sb.append(System.getProperty("line.separator"));
						totalLedger++;
					}
				}
				
				String ledgerSrMappingIfGST0 = llpParametersService.findByCodeTypeValue(Parameter.GAF_CONFIG, Parameter.GAF_CONFIG_LEDGER_SR_MAPPING_IF_GST_0);
				System.out.println("CREDIT");
				for (Iterator iterator = mapLedgerCreditBranch.keySet().iterator(); iterator.hasNext();) {
					String branchCode = (String) iterator.next();
					Map mapLedgerCreditByType2 = mapLedgerCreditBranch.get(branchCode);
					for (Iterator iterator2 = mapLedgerCreditByType2.keySet().iterator(); iterator2.hasNext();) {
						String paymentItem = (String) iterator2.next();
						List<GafLedger> listGlCredit = (List<GafLedger>) mapLedgerCreditByType2.get(paymentItem);
						for (int i = 0;listGlCredit!=null && i < listGlCredit.size(); i++) {
							GafLedger gLCredit = listGlCredit.get(i);
							if(Parameter.PAYMENT_GST_CODE_SR.equals(gLCredit.getTaxCode()) && StringUtils.isNotBlank(gLCredit.getOutputGSTAmount())){
								if(Double.parseDouble(gLCredit.getOutputGSTAmount())==0){
									gLCredit.setTaxCode(ledgerSrMappingIfGST0);
								}
							}
							
							sb.append(convertObjToStringPipeDelimited(gLCredit));
							sb.append(System.getProperty("line.separator"));
							totalLedger++;
						}
					}
				}
				
				// append Cash Movement Debit
				// append GL Debit
				for (Iterator iterator = listLedgerDebitCashMovement.iterator(); iterator.hasNext();) {
					GafLedger gL = (GafLedger) iterator.next();
					sb.append(convertObjToStringPipeDelimited(gL));
					sb.append(System.getProperty("line.separator"));
					totalLedger++;
				}
				// append GL Credit
				for (Iterator iterator = listLedgerCreditCashMovement.iterator(); iterator.hasNext();) {
					GafLedger gL = (GafLedger) iterator.next();
					sb.append(convertObjToStringPipeDelimited(gL));
					sb.append(System.getProperty("line.separator"));
					totalLedger++;
				}
				
				
				System.out.println(totalLedger);
				// (4)FOOTER
				GafFooter gF = new GafFooter();
				gF.setSupplyCount(listSupply.size());
				gF.setSupplyAmount(gafDecimalFormat.format(totalSupplyAmt));
				gF.setSupplyGSTAmount(gafDecimalFormat.format(totalSupplyGstAmt));
				gF.setLedgerCount(totalLedger);
				gF.setDebitSum(gafDecimalFormat.format(totalLedgerDebitAmt));
				gF.setCreditSum(gafDecimalFormat.format(totalLedgerCreditAmt));
				
				// append footer
				sb.append(convertObjToStringPipeDelimited(gF));
				return sb.toString().getBytes();
			}
		} catch (Exception e) {
			throw new SSMException(e);
		}
		return null;
	}

	public String convertObjToStringPipeDelimited(Object obj) {
		MyStyle myToStringStyle = new MyStyle();

		
		ReflectionToStringBuilder rfs = new ReflectionToStringBuilder(obj,myToStringStyle) {
	         protected boolean accept(Field f) {
	             return super.accept(f) && !f.getName().equals("branchCode") && !f.getName().equals("bankSlipNo");
	         }
	     };
		String str2 = rfs.toString();

		return str2 + Parameter.GAF_DELIMITER;
	}

	private class MyStyle extends ToStringStyle {
		public MyStyle() {
			super();
			this.setUseClassName(false);
			this.setUseIdentityHashCode(false);
			this.setUseFieldNames(false);
			this.setContentStart("");
			this.setContentEnd("");
			this.setFieldSeparator(Parameter.GAF_DELIMITER);
			
		}
	}

	@Override
	public LlpPaymentTransaction findSuccessByAppRefNo(String appRefNo) {
		return llpPaymentTransactionDao.findSuccessByAppRefNo(appRefNo);
	}

	@Override
	public List<LlpPaymentTransaction> findPendingByAppRefNo(String appRefNo) {
		return llpPaymentTransactionDao.findPendingByAppRefNo(appRefNo);
	}
	
	@Override
	public LlpPaymentTransaction findPendingByTransactionId(String transactionId){
		return llpPaymentTransactionDao.findPendingByTransactionId(transactionId);
	}

	@Override
	public List<LlpPaymentTransaction> findByAppRefNoStatus(String appRefNo, String status) {
		return llpPaymentTransactionDao.findByAppRefNoStatus(appRefNo, status);
	}
	
	@Override
	public LlpPaymentTransaction findByAppRefNoStatusPaymentMode(String appRefNo, String status, String paymentMode){
		return llpPaymentTransactionDao.findByAppRefNoStatusPaymentMode(appRefNo, status, paymentMode);
	}
	
	@Override
	public void cancelAllOtc(String appRefNo, String transactionId){
		SearchCriteria sc = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, appRefNo);
		sc = sc.andIfNotNull("paymentDetail", SearchCriteria.EQUAL, Parameter.PAYMENT_DETAIL_OTC);
		sc = sc.andIfNotNull("status", SearchCriteria.EQUAL, Parameter.PAYMENT_STATUS_PENDING);
		sc = sc.andIfNotNull("transactionId", SearchCriteria.NOT_EQUAL, transactionId);
		
		List<LlpPaymentTransaction> list = findByCriteria(sc).getList();
		
		if(list.size() > 0){
			for(LlpPaymentTransaction i : list){
				i.setStatus(Parameter.PAYMENT_STATUS_CANCEL);
				update(i);
			}
		}
		
	}


	@Override
	public void cancelAllPreviosDayOTCPayment() {
		llpPaymentTransactionDao.cancelAllPreviosDayOTCPayment();
	}


	@Override
	public boolean hasPendingOnlineAndSuccessPaymentByAppRefNo(String appRefNo) {
		return llpPaymentTransactionDao.hasPendingOnlineAndSuccessPaymentByAppRefNo(appRefNo);
	}


	@Override
	@Transactional
	public void cancelPendingOtcByAppRefNo(String appRefNo) {
		llpPaymentTransactionDao.cancelPendingOtcByAppRefNo(appRefNo);
	}
 

	@Override
	public LlpPaymentTransaction findDetailByLatestByAppRefNo(String robFormACode) {
		SearchCriteria sc = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, robFormACode);
		sc.addOrderBy("createDt", SearchCriteria.DESC);
		
		List<LlpPaymentTransaction> list = findByCriteria(sc).getList();
		
		if(list.size() > 0){
			LlpPaymentTransaction trns = list.get(0);
			List<LlpPaymentTransactionDetail> listDetail = llpPaymentTransactionDetailService.find(trns.getTransactionId());
			trns.setLlpPaymentTransactionDetails(listDetail);
			return trns;
		}
		return null;
	}
}
