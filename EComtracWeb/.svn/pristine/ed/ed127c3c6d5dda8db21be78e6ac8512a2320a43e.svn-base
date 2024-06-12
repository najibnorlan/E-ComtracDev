package com.ssm.ezbiz.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.ssm.ezbiz.dao.RobTrainingFinalListingDao;
import com.ssm.ezbiz.dao.RobTrainingTransactionDao;
import com.ssm.ezbiz.service.RobTrainingConfigService;
import com.ssm.ezbiz.service.RobTrainingFinalListingService;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpPaymentTransactionDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingFinalListing;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@Service
public class RobTrainingFinalListingServiceImpl extends BaseServiceImpl<RobTrainingFinalListing, Integer>
		implements RobTrainingFinalListingService {

	@Autowired
	RobTrainingFinalListingDao robTrainingFinalListingDao;

	@Autowired
	LlpUserProfileService llpUserProfileService;

	@Autowired
	@Qualifier("mailService")
	MailService mailService;

	@Autowired
	RobTrainingTransactionDao robTrainingTransactionDao;

	@Autowired
	LlpPaymentTransactionDao llpPaymentTransactionDao;

	@Autowired
	LlpPaymentReceiptService llpPaymentReceiptService;

	@Autowired
	RobTrainingConfigService robTrainingConfigService;

	@Override
	public BaseDao getDefaultDao() {

		return robTrainingFinalListingDao;
	}

	@Override
	public RobTrainingFinalListing updateInsertAll(RobTrainingFinalListing robTrainingFinalListing) {

		if (StringUtils.isBlank(robTrainingFinalListing.getFinalListingRefNo())) {
			insert(robTrainingFinalListing);
		} else {
			update(robTrainingFinalListing);
		}
		return robTrainingFinalListing;

	}

	@Override
	public RobTrainingFinalListing findByFinalListingRefNo(String finalListingRefNo) {

		SearchCriteria sc = new SearchCriteria("finalListingRefNo", SearchCriteria.EQUAL, finalListingRefNo);
		List<RobTrainingFinalListing> finalListing = findByCriteria(sc).getList();
		if (finalListing.size() > 0) {
			return finalListing.get(0);
		}

		return null;
	}

	@Override
	public RobTrainingFinalListing findByTrainingCode(String trainingCode) {

		SearchCriteria sc = new SearchCriteria("trainingCode", SearchCriteria.EQUAL, trainingCode);
		List<RobTrainingFinalListing> trainingCodeExist = findByCriteria(sc).getList();
		if (trainingCodeExist.size() > 0) {
			return trainingCodeExist.get(0);
		}

		return null;
	}

	@Override
	public void sendEmailRefundComtrac(String transactionCode) {

		RobTrainingTransaction robTrainingTransaction = robTrainingTransactionDao
				.findByTransactionCodeWithParticipant(transactionCode);
		LlpPaymentTransaction robPaymentTransaction = llpPaymentTransactionDao.findSuccessByAppRefNo(transactionCode);
//		LlpPaymentTransaction robPaymentTransaction = llpPaymentTransactionDao
//				.findByAppRefNoStatusPaymentMode(transactionCode, Parameter.PAYMENT_STATUS_SUCCESS, null);
//		LlpPaymentReceipt robPaymentReceipt = llpPaymentReceiptDao.findById(robPaymentTransaction.getTransactionId());
		LlpPaymentReceipt robPaymentReceipt = llpPaymentReceiptService.find(robPaymentTransaction.getTransactionId());

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String lodgerEmail = null;
		String lodgerFullName = null;
		LlpUserProfile llpUserProfile = llpUserProfileService
				.findProfileInfoByUserId(robTrainingTransaction.getLodgerName());
		lodgerEmail = llpUserProfile.getEmail();
		lodgerFullName = llpUserProfile.getName();
		String ccEmailComtrac = "danealhz@gmail.com";//norfakhira@ssm.com.my

		double amount = robPaymentTransaction.getAmount();
		String amountAsString = String.valueOf(amount);
		String trainingDt = sdf.format(robTrainingTransaction.getTrainingId().getTrainingStartDt());

//		LlpPaymentTransaction robPaymentTransaction = llpPaymentTransactionDao.findByAppRefNoStatus(appRefNo, status);
//		String cpIcNo = robTrainingReprintcert.getTransactionCode() + "/" + robTrainingReprintcert.getIcNo();

		// send to lodger
		if (lodgerEmail != null) {
//			for (RobTrainingParticipant participant : robTrainingTransaction.getRobTrainingParticipantList()) {
			mailService.sendMail(lodgerEmail, "email.notification.ecomtrac.refundComtrac.subject", transactionCode,
					"email.notification.ecomtrac.refundComtrac.body", amountAsString,
					robTrainingTransaction.getTrainingId().getTrainingName(), trainingDt,
					robPaymentReceipt.getReceiptNo(), lodgerFullName, robTrainingTransaction.getLodgerId());
		}
		mailService.sendMail(ccEmailComtrac, "email.notification.ecomtrac.refundComtrac.subject", transactionCode,
				"email.notification.ecomtrac.refundComtrac.body", amountAsString,
				robTrainingTransaction.getTrainingId().getTrainingName(), trainingDt, robPaymentReceipt.getReceiptNo(),
				lodgerFullName, robTrainingTransaction.getLodgerId());

	}

	@Override
	public void sendEmailListingApprove(String trainingCode) {

		RobTrainingFinalListing robTrainingFinalListing = findByTrainingCode(trainingCode);
		RobTrainingConfig robTrainingConfig = robTrainingConfigService
				.findByTrainingCode(trainingCode);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String ccEmailFinance = "dannzyhazel@gmail.com";//noriman@ssm.com.my dannzyhazel@gmail.com
		String trainingDt = sdf.format(robTrainingConfig.getTrainingStartDt());

		mailService.sendMail(ccEmailFinance, "email.notification.ecomtrac.listingApprove.subject",
				robTrainingFinalListing.getFinalListingRefNo(), "email.notification.ecomtrac.listingApprove.body",
				robTrainingConfig.getTrainingName(), robTrainingConfig.getTrainingCode(), trainingDt);
	}

}