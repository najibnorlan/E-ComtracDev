package com.ssm.ezbiz.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.comtrac.SearchComtracList.SearchComtracModel;
import com.ssm.ezbiz.dao.RobTrainingTransactionDao;
import com.ssm.ezbiz.service.PaymentInterface;
import com.ssm.ezbiz.service.RobTrainingParticipantService;
import com.ssm.ezbiz.service.RobTrainingTransactionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.db.SearchResult;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormABranches;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@Service
public class RobTrainingTransactionServiceImpl extends BaseServiceImpl<RobTrainingTransaction, Integer>
		implements RobTrainingTransactionService, PaymentInterface {

	@Autowired
	RobTrainingTransactionDao robTrainingTransactionDao;

	@Autowired
	RobTrainingParticipantService robTrainingParticipantService;

	@Autowired
	LlpUserProfileService llpUserProfileService;

	@Autowired
	@Qualifier("mailService")
	MailService mailService;

	@Override
	public BaseDao getDefaultDao() {
		return robTrainingTransactionDao;
	}

	@Override
	public void updateInsertAll(RobTrainingTransaction robTrainingTransaction) {

		Boolean isNew = false;

		List<RobTrainingParticipant> listParticipant = robTrainingTransaction.getRobTrainingParticipantList();
		if (StringUtils.isBlank(robTrainingTransaction.getTransactionCode())) {
			insert(robTrainingTransaction);
			isNew = true;
		} else {
			update(robTrainingTransaction);
		}

		long setParticipantId[] = new long[listParticipant.size()];
		Integer index = 0;
		for (RobTrainingParticipant i : listParticipant) {
			i.setRobTrainingTransaction(robTrainingTransaction);
			RobTrainingParticipant participant = robTrainingParticipantService
					.findByTransactionCodeIcNo(robTrainingTransaction.getTransactionCode(), i.getIcNo());
			if (participant == null) {
				participant = new RobTrainingParticipant();
				participant = i;
				participant.setParticipantId(null);

				robTrainingParticipantService.insert(participant);
				participant = robTrainingParticipantService
						.findByTransactionCodeIcNo(robTrainingTransaction.getTransactionCode(), participant.getIcNo());
			} else {
				participant = copyFrom(i, participant);
				robTrainingParticipantService.update(participant);
			}
			setParticipantId[index] = participant.getParticipantId();
			index++;
		}

		if (!isNew) {

			SearchResult sr = robTrainingParticipantService
					.findByCriteria(new SearchCriteria("robTrainingTransaction.transactionCode", SearchCriteria.EQUAL,
							robTrainingTransaction.getTransactionCode()));
			if (sr.getList().size() > 0) {
				List<RobTrainingParticipant> listToDelete = sr.getList();
				for (int i = 0; i < setParticipantId.length; i++) {
					for (int j = 0; j < listToDelete.size(); j++) {
						if (setParticipantId[i] == listToDelete.get(j).getParticipantId()) {
							listToDelete.remove(listToDelete.get(j));
							break;
						}
					}
				}

				for (int i = 0; i < listToDelete.size(); i++) {
					robTrainingParticipantService.deleteUsingParticipantId(listToDelete.get(i).getParticipantId());
				}
			}
//			robTrainingParticipantService.deleteNotInId(robTrainingTransaction.getTransactionCode(), setParticipantId);
		}

	}

	public RobTrainingParticipant copyFrom(RobTrainingParticipant from, RobTrainingParticipant to) {

		to.setName(from.getName());
		to.setIcNo(from.getIcNo());
		to.setLsNo(from.getLsNo());
		to.setMembershipNo(from.getMembershipNo());
		to.setDesignation(from.getDesignation());
		to.setCompany(from.getCompany());
		to.setAddress1(from.getAddress1());
		to.setAddress2(from.getAddress2());
		to.setAddress3(from.getAddress3());
		to.setTelNo(from.getTelNo());
		to.setFaxNo(from.getFaxNo());
		to.setEmail(from.getEmail());
		to.setAmount(from.getAmount());
		to.setFeeType(from.getFeeType());
		to.setRobTrainingTransaction(from.getRobTrainingTransaction());

		return to;
	}

	@Override
	@Transactional
	public void sucessPayment(Object obj, String paymentTransId) throws SSMException {
		RobTrainingTransaction trainingTmp = (RobTrainingTransaction) obj;
		RobTrainingTransaction robTrainingTransaction = findByTransactionCode(trainingTmp.getTransactionCode());
		if (Parameter.COMTRAC_TRANSACTION_STATUS_pending_payment.equals(robTrainingTransaction.getStatus())) {

			robTrainingTransaction.setStatus(Parameter.COMTRAC_TRANSACTION_STATUS_payment_success);
			robTrainingTransactionDao.update(robTrainingTransaction);
		}

	}

	@Override
	public void sucessNotification(Object obj, String paymentTransId) throws SSMException {

		RobTrainingTransaction transaction = (RobTrainingTransaction) obj;
		sendEmailConfirmation(transaction.getTransactionCode());

	}

	@Override
	public void sendEmailConfirmation(String transactionCode) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		String lodgerName = "us";
		String lodgerPhone = "603-7721 4000";
		String lodgerEmail = null;

		RobTrainingTransaction robTrainingTransaction = findByTransactionCodeWithParticipant(transactionCode);
		String trainingDt = sdf.format(robTrainingTransaction.getTrainingId().getTrainingStartDt());
		if (!robTrainingTransaction.getTrainingId().getTrainingEndDt()
				.equals(robTrainingTransaction.getTrainingId().getTrainingStartDt())) {
			trainingDt += " - " + sdf.format(robTrainingTransaction.getTrainingId().getTrainingEndDt());
		}

		if (!robTrainingTransaction.getLodgerId().equals("SSM STAF")) {
			LlpUserProfile llpUserProfile = llpUserProfileService
					.findProfileInfoByUserId(robTrainingTransaction.getLodgerName());
			lodgerName = llpUserProfile.getName();
			lodgerPhone = llpUserProfile.getHpNo();
			lodgerEmail = llpUserProfile.getEmail();
		}

		// send to lodger
			if(lodgerEmail != null){
				mailService.sendEmailCalender(robTrainingTransaction.getTrainingId().getTrainingStartDt(),robTrainingTransaction.getTrainingId().getTrainingEndDt(),lodgerEmail, "email.notification.comtrac.lodgerConfirm.subject", robTrainingTransaction.getTransactionCode(), "email.notification.comtrac.lodgerConfirm.body",
						robTrainingTransaction.getTransactionCode(), robTrainingTransaction.getTrainingId().getTrainingName(), robTrainingTransaction.getTrainingId().getTrainingCode(), trainingDt, 
						robTrainingTransaction.getTrainingId().getTrainingDesc(), robTrainingTransaction.getTotalPax().toString(), df.format(robTrainingTransaction.getAmount()));
			}

		// send to all participant
		for (RobTrainingParticipant participant : robTrainingTransaction.getRobTrainingParticipantList()) {
			try {
				mailService.sendEmailCalender(robTrainingTransaction.getTrainingId().getTrainingStartDt(),
						robTrainingTransaction.getTrainingId().getTrainingEndDt(), participant.getEmail(),
						"email.notification.comtrac.confirmation.subject", robTrainingTransaction.getTransactionCode(),
						"email.notification.comtrac.confirmation.body",
						robTrainingTransaction.getTrainingId().getTrainingName(),
						robTrainingTransaction.getTrainingId().getTrainingCode(), trainingDt,
						robTrainingTransaction.getTrainingId().getTrainingDesc(), lodgerName, lodgerPhone);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void sendEmailLpoInProcess(String transactionCode) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		String lodgerName = "us";
		String lodgerPhone = "603-7721 4000";
//		String lodgerEmail = null;

		RobTrainingTransaction robTrainingTransaction = findByTransactionCodeWithParticipant(transactionCode);
		String trainingDt = sdf.format(robTrainingTransaction.getTrainingId().getTrainingStartDt());
		if (!robTrainingTransaction.getTrainingId().getTrainingEndDt()
				.equals(robTrainingTransaction.getTrainingId().getTrainingStartDt())) {
			trainingDt += " - " + sdf.format(robTrainingTransaction.getTrainingId().getTrainingEndDt());
		}

		// send to all participant
		for (RobTrainingParticipant participant : robTrainingTransaction.getRobTrainingParticipantList()) {
			mailService.sendMail(participant.getEmail(), "email.notification.ecomtrac.lpoInProcess.subject",
					robTrainingTransaction.getTransactionCode(), "email.notification.ecomtrac.lpoInProcess.body",
					robTrainingTransaction.getTrainingId().getTrainingName(),
					robTrainingTransaction.getTrainingId().getTrainingCode(), trainingDt,
					robTrainingTransaction.getTransactionCode(), lodgerName, lodgerPhone);
		}
	}

	@Override
	public void sendEmailLpoApproved(String transactionCode) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		String lodgerName = "us";
		String lodgerPhone = "603-7721 4000";
//		String lodgerEmail = null;

		RobTrainingTransaction robTrainingTransaction = findByTransactionCodeWithParticipant(transactionCode);
		String trainingDt = sdf.format(robTrainingTransaction.getTrainingId().getTrainingStartDt());
		if (!robTrainingTransaction.getTrainingId().getTrainingEndDt()
				.equals(robTrainingTransaction.getTrainingId().getTrainingStartDt())) {
			trainingDt += " - " + sdf.format(robTrainingTransaction.getTrainingId().getTrainingEndDt());
		}

		// send to all participant
		for (RobTrainingParticipant participant : robTrainingTransaction.getRobTrainingParticipantList()) {
			mailService.sendMail(participant.getEmail(), "email.notification.ecomtrac.lpoApproved.subject",
					robTrainingTransaction.getTransactionCode(), "email.notification.ecomtrac.lpoApproved.body",
					robTrainingTransaction.getTrainingId().getTrainingName(),
					robTrainingTransaction.getTrainingId().getTrainingCode(), trainingDt,
					robTrainingTransaction.getTransactionCode(), lodgerName, lodgerPhone);
		}
	}

	@Override
	public void sendEmailLpoRejected(String transactionCode) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		String lodgerName = "us";
		String lodgerPhone = "603-7721 4000";
//		String lodgerEmail = null;

		RobTrainingTransaction robTrainingTransaction = findByTransactionCodeWithParticipant(transactionCode);
		String trainingDt = sdf.format(robTrainingTransaction.getTrainingId().getTrainingStartDt());
		if (!robTrainingTransaction.getTrainingId().getTrainingEndDt()
				.equals(robTrainingTransaction.getTrainingId().getTrainingStartDt())) {
			trainingDt += " - " + sdf.format(robTrainingTransaction.getTrainingId().getTrainingEndDt());
		}

		// send to all participant
		for (RobTrainingParticipant participant : robTrainingTransaction.getRobTrainingParticipantList()) {
			mailService.sendMail(participant.getEmail(), "email.notification.ecomtrac.lpoRejected.subject",
					robTrainingTransaction.getTransactionCode(), "email.notification.ecomtrac.lpoRejected.body",
					robTrainingTransaction.getTrainingId().getTrainingName(),
					robTrainingTransaction.getTrainingId().getTrainingCode(), trainingDt,
					robTrainingTransaction.getTransactionCode(), lodgerName, lodgerPhone);
		}
	}

	@Override
	public RobTrainingTransaction findByTransactionCode(String transactionCode) {
		SearchCriteria sc = new SearchCriteria("transactionCode", SearchCriteria.EQUAL, transactionCode);
		List<RobTrainingTransaction> transactions = findByCriteria(sc).getList();
		if (transactions.size() > 0) {
			return transactions.get(0);
		}

		return null;
	}

	@Override
	public RobTrainingTransaction findByTransactionCodeWithParticipant(String transactionCode) {

		return robTrainingTransactionDao.findByTransactionCodeWithParticipant(transactionCode);

	}

	@Override
	public Integer countParticipantByStatusAndTrainingId(Integer trainingId, String[] status) {

		Integer count = 0;
		SearchCriteria sc = new SearchCriteria("trainingId.trainingId", SearchCriteria.EQUAL, trainingId);
		sc = sc.andIfInNotNull("status", status, false);

		List<RobTrainingTransaction> list = findByCriteria(sc).getList();

		if (list.size() > 0) {
			for (RobTrainingTransaction i : list) {
				count += i.getTotalPax();
			}
		}

		return count;
	}

	@Override
	public List<RobTrainingTransaction> searchComtractTransactions(SearchComtracModel searchComtracModel) {

		return robTrainingTransactionDao.searchComtractTransactions(searchComtracModel);
	}

}
