package com.ssm.ezbiz.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.dao.RobTrainingReprintcertDao;
import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.RobTrainingParticipantService;
import com.ssm.ezbiz.service.RobTrainingReprintcertService;
import com.ssm.ezbiz.service.RobTrainingTransactionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;
import com.ssm.llp.ezbiz.model.RobTrainingReprintcert;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;


@Service
public class RobTrainingReprintcertServiceImpl extends BaseServiceImpl<RobTrainingReprintcert, Integer>
		implements RobTrainingReprintcertService, PaymentInterface{

	@Autowired
	RobTrainingReprintcertDao robTrainingReprintcertDao;
	
	@Autowired
	RobTrainingTransactionService robTrainingTransactionService;
	
	@Autowired
	RobTrainingParticipantService robTrainingParticipantService;
	
	@Autowired
	LlpFileUploadService llpFileUploadService;

	@Autowired
	LlpFileDataService llpFileDataService;
	
	@Autowired
	LlpUserProfileService llpUserProfileService;

	@Autowired
	@Qualifier("mailService")
	MailService mailService;
	
	@Override
	public BaseDao getDefaultDao() {
		return robTrainingReprintcertDao;
	}
	
	@Override
	public RobTrainingReprintcert updateInsertAll(RobTrainingReprintcert robTrainingReprintcert) {
				
		if (StringUtils.isBlank(robTrainingReprintcert.getCertRefNo())) {
			insert(robTrainingReprintcert);
		} else {
			update(robTrainingReprintcert);
		}
		return robTrainingReprintcert;


	}
	
	@Override
	public RobTrainingReprintcert findBycertRefNo(String certRefNo) {
		SearchCriteria sc = new SearchCriteria("certRefNo", SearchCriteria.EQUAL, certRefNo);
		List<RobTrainingReprintcert> transactions = findByCriteria(sc).getList();
		if (transactions.size() > 0) {
			return transactions.get(0);
		}

		return null;
	}
	
	@Override
	public RobTrainingReprintcert findDrafTransaction(String transactionCode, String icNo, String lodgerId) {
		Object[] vals = {"DE","PP"};
		
		SearchCriteria sc = new SearchCriteria("transactionCode", SearchCriteria.EQUAL, transactionCode);
		               sc = sc.andIfNotNull("icNo", SearchCriteria.EQUAL, icNo);
		               sc = sc.andIfNotNull("lodgerId", SearchCriteria.EQUAL, lodgerId);
		               sc = sc.andIfInNotNull("status", vals, false);
		List<RobTrainingReprintcert> reprintcertTransactions = findByCriteria(sc).getList();
		if (reprintcertTransactions.size() > 0) {
			return reprintcertTransactions.get(0);
		}

		return null;
	}
	
	@Override
	public List<RobTrainingReprintcert> findAllPendingGenerateCert() {
			// TODO Auto-generated method stub
			return robTrainingReprintcertDao.findAllPendingGenerateCert();
		}
	
	@Override
	public void generateCertificate(RobTrainingReprintcert reprintcert) {
		// TODO Auto-generated method stub
		RobTrainingReprintcert robTrainingReprintcert = reprintcert;
		RobTrainingParticipant participant = robTrainingParticipantService.findByTransactionCodeIcNo(reprintcert.getTransactionCode(), reprintcert.getIcNo());
		RobTrainingTransaction robTrainingTransaction = robTrainingTransactionService.findByTransactionCode(reprintcert.getTransactionCode());
		RobTrainingConfig trainingConfig = robTrainingTransaction.getTrainingId();
		
		trainingConfig.setTrainingEndDt(reprintcert.getCreateDt());
		
		Map mapData = new HashMap();
		mapData.put("participant", participant);
		mapData.put("robTrainingTransaction", robTrainingTransaction);
		mapData.put("trainingConfig", trainingConfig);

//alter table training config tambah column trainingType flag : NORMAL/SSMNC
// keluarkan value trainingType, year(createDate) String certType = trainingType + year(createDate)
//if(trainingType) = 'NORMAL' certType = trainingType
//else certType = trainingType + year(createDate)
//certType : ECOMTRAC_CERTIFICATE_NORMAL
//certType : ECOMTRAC_CERTIFICATE_SSMNC_2023

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(trainingConfig.getCreateDt());
		int year = calendar.get(Calendar.YEAR);
		String certType = trainingConfig.getTrainingType()  + "_" + year;

		try {
			// BACA KT KOMPUTER! (bukak kalau nk testing)
//			FileInputStream fis = new FileInputStream("D:/SSMNC_CERT.odt");
//			byte[] odtFormAData = new byte[fis.available()];
//			fis.read(odtFormAData);
//			fis.close();
//			byte bytePdfEcertComtrac[] = LLPOdtUtils.generatePdf(odtFormAData, mapData);

			if (trainingConfig.getTrainingType().equals("NORMAL")) {

				// Tutup 2 line ni kalau nk buat testing
				LlpFileUpload fileUpload = llpFileUploadService
						.findByFileCode(Parameter.ECOMTRAC_CERTIFICATE + "_" + "NORMAL");
				byte bytePdfEcertComtrac[] = LLPOdtUtils.generatePdf(fileUpload.getFileData(), mapData);

				if (bytePdfEcertComtrac.length > 0) {

					// Bukak 10 line ni kalau nk buat testing
//					String file = "D:/SSMNC_CERT_TEST.pdf";
//					FileOutputStream fos = new FileOutputStream(file);
//					fos.write(bytePdfEcertComtrac);
//					fos.close();
//					try {
//						Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file);
//						p.waitFor();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}

					// Remain for generate pdf
					LlpFileData eCertComtracPdf = new LlpFileData();
					eCertComtracPdf.setFileData(bytePdfEcertComtrac);
					eCertComtracPdf.setFileDataType("PDF");
					llpFileDataService.insert(eCertComtracPdf);
					robTrainingReprintcert.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_cert_generated);
					robTrainingReprintcert.setFileId(eCertComtracPdf.getFileDataId());
					update(robTrainingReprintcert);
				}

			} else {

				System.out.println("Year cert created : " + calendar.get(Calendar.YEAR));
				
				LlpFileUpload fileUpload = llpFileUploadService
						.findByFileCode(Parameter.ECOMTRAC_CERTIFICATE + "_" + certType);
				byte bytePdfEcertComtrac[] = LLPOdtUtils.generatePdf(fileUpload.getFileData(), mapData);

				if (bytePdfEcertComtrac.length > 0) {

					LlpFileData eCertComtracPdf = new LlpFileData();
					eCertComtracPdf.setFileData(bytePdfEcertComtrac);
					eCertComtracPdf.setFileDataType("PDF");
					llpFileDataService.insert(eCertComtracPdf);
					robTrainingReprintcert.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_cert_generated);
					robTrainingReprintcert.setFileId(eCertComtracPdf.getFileDataId());
					update(robTrainingReprintcert);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		RobTrainingReprintcert trainingCert = (RobTrainingReprintcert) obj;
		RobTrainingReprintcert robTrainingReprintcert = findBycertRefNo(trainingCert.getCertRefNo());
		if (Parameter.COMTRAC_TRANSACTION_STATUS_pending_payment.equals(robTrainingReprintcert.getStatus())) {
			robTrainingReprintcert.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_payment_success);
			update(robTrainingReprintcert);
		}
		
	}

	@Override
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {
		// TODO Auto-generated method stub
		RobTrainingReprintcert trainingCert = (RobTrainingReprintcert) obj;
		RobTrainingReprintcert robTrainingReprintcert = findBycertRefNo(trainingCert.getCertRefNo());
		if(Parameter.COMTRAC_TRANSACTION_STATUS_payment_success.equals(robTrainingReprintcert.getStatus())){
		sendEmailConfirmation(trainingCert.getCertRefNo());
		generateCertificate(trainingCert);
		}
	}

	@Override
	public void sendEmailConfirmation(String certRefNo) {

		String lodgerEmail = null;

		RobTrainingReprintcert robTrainingReprintcert = findBycertRefNo(certRefNo);
		String cpIcNo = robTrainingReprintcert.getTransactionCode()+"/"+robTrainingReprintcert.getIcNo();
		
		LlpUserProfile llpUserProfile = llpUserProfileService
					.findProfileInfoByUserId(robTrainingReprintcert.getLodgerName());
			lodgerEmail = llpUserProfile.getEmail();

		// send to lodger
			if(lodgerEmail != null){
				mailService.sendMail(lodgerEmail, "email.notification.ecomtrac.reprintCertOrders.subject", certRefNo, "email.notification.ecomtrac.reprintCertOrders.body", robTrainingReprintcert.getTrainingName(),robTrainingReprintcert.getTrainingCode(),cpIcNo,certRefNo);
			}



	}
	
}
