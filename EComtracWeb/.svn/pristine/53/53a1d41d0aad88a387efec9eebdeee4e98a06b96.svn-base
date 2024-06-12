package com.ssm.ezbiz.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.ezbiz.dao.RobTrainingParticipantDao;
import com.ssm.ezbiz.service.RobTrainingParticipantService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.odt.LLPOdtUtils;
import com.ssm.llp.ezbiz.model.RobTrainingConfig;
import com.ssm.llp.ezbiz.model.RobTrainingParticipant;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Service
public class RobTrainingParticipantServiceImpl extends BaseServiceImpl<RobTrainingParticipant, Integer>
		implements RobTrainingParticipantService {

	@Autowired
	RobTrainingParticipantDao robTrainingParticipantDao;

	@Autowired
	LlpParametersService llpParametersService;

	@Autowired
	LlpPaymentReceiptService llpPaymentReceiptService;

	@Autowired
	LlpPaymentTransactionService llpPaymentTransactionService;

	@Autowired
	LlpFileUploadService llpFileUploadService;

	@Autowired
	LlpFileDataService llpFileDataService;

	@Override
	public BaseDao getDefaultDao() {
		return robTrainingParticipantDao;
	}

	public List<RobTrainingParticipant> findAllParticipantByTrainingIdStatus(Integer trainingId, String[] statuses,
			String ic) {

		String status = "";
		Integer index = 0;
		for (String s : statuses) {
			if (index != 0) {
				status += ",";
			}

			status += "'" + s + "'";
			index++;
		}
		return robTrainingParticipantDao.findAllParticipantByTrainingIdStatus(trainingId, status, ic);
	}

	@Override
	@Transactional
	public void deleteUsingParticipantId(Integer participantId) {
		robTrainingParticipantDao.deleteUsingParticipantId(participantId);
	}

	@Override
	@Transactional
	public void deleteNotInId(String transactionCode, long[] idToDelete) {
		robTrainingParticipantDao.deleteNotInId(transactionCode, idToDelete);
	}

	public RobTrainingParticipant findByTransactionCodeIcNo(String transactionCode, String icNo) {

		SearchCriteria sc = new SearchCriteria("robTrainingTransaction.transactionCode", SearchCriteria.EQUAL,
				transactionCode);
		sc = sc.andIfNotNull("icNo", SearchCriteria.EQUAL, icNo);

		List<RobTrainingParticipant> list = findByCriteria(sc).getList();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;

	}

	public RobTrainingParticipant findByTransactionCodeIcNoLodger(String transactionCode, String icNo,
			String lodgerName) {

		SearchCriteria sc = new SearchCriteria("robTrainingTransaction.transactionCode", SearchCriteria.EQUAL,
				transactionCode);
		sc = sc.andIfNotNull("icNo", SearchCriteria.EQUAL, icNo);
		sc = sc.andIfNotNull("robTrainingTransaction.status", SearchCriteria.EQUAL, "PS");
		sc = sc.andIfNotNull("robTrainingTransaction.createBy", SearchCriteria.EQUAL, lodgerName);
		sc = sc.andIfNotNull("isEligible", SearchCriteria.EQUAL, "Y");

		SearchCriteria sc2 = new SearchCriteria("fileId", SearchCriteria.IS_NOT_NULL);
		sc = new SearchCriteria(sc, SearchCriteria.AND, sc2);

		List<RobTrainingParticipant> list = findByCriteria(sc).getList();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;

	}

	@Override
	public byte[] generateExcelParticipant(RobTrainingConfig robTrainingConfig,
			List<RobTrainingParticipant> listParticipant) {

		try {
			ByteArrayOutputStream bAOS = new ByteArrayOutputStream();

			Map<String, String> mapFeeType = llpParametersService.findAllCodeTypeAsMap(Parameter.COMTRAC_FEE_TYPE);
			Map<String, String> mapPaymentChannel = llpParametersService
					.findAllCodeTypeAsMap(Parameter.COMTRAC_PAYMENT_CHANNEL);
			Map<String, String> mapRegisterChannel = llpParametersService
					.findAllCodeTypeAsMap(Parameter.COMTRAC_REGISTRATION_CHANNEL);
			Map<String, String> mapYesNo = llpParametersService.findAllCodeTypeAsMap(Parameter.YES_NO);
			Map<String, String> mapStateCode = llpParametersService.findAllCodeTypeAsMap(Parameter.CBS_ROB_STATE);
			Map<String, String> mapTypeDietary = llpParametersService.findAllCodeTypeAsMap(Parameter.DIETARY_TYPE);
			Map<String, String> mapOccupationCode = llpParametersService
					.findAllCodeTypeAsMap(Parameter.COMTRAC_DESIGNATION);

			WritableWorkbook w = Workbook.createWorkbook(bAOS);
			WritableSheet s = w.createSheet("ATTENDANCE", 0);

			// Title
			s.addCell(new Label(0, 0, "(" + robTrainingConfig.getTrainingCode() + ") "
					+ robTrainingConfig.getTrainingName() + " ATTENDANCE REPORT"));
			// Header
			int headerColIdx = 0;
			s.addCell(new Label(headerColIdx++, 2, "BIL"));
			s.addCell(new Label(headerColIdx++, 2, "NAME"));
			s.addCell(new Label(headerColIdx++, 2, "IC NO"));
			s.addCell(new Label(headerColIdx++, 2, "TEL NO"));
			s.addCell(new Label(headerColIdx++, 2, "EMAIL"));
			s.addCell(new Label(headerColIdx++, 2, "FEE TYPE"));
			s.addCell(new Label(headerColIdx++, 2, "LS NO"));
			s.addCell(new Label(headerColIdx++, 2, "MEMBERSHIP NO"));
			s.addCell(new Label(headerColIdx++, 2, "FEE"));
			s.addCell(new Label(headerColIdx++, 2, "OCCUPATION"));
			s.addCell(new Label(headerColIdx++, 2, "DESIGNATION"));
			s.addCell(new Label(headerColIdx++, 2, "COMPANY"));
			s.addCell(new Label(headerColIdx++, 2, "ADDRESS 1"));
			s.addCell(new Label(headerColIdx++, 2, "ADDRESS 2"));
			s.addCell(new Label(headerColIdx++, 2, "ADDRESS 3"));
			s.addCell(new Label(headerColIdx++, 2, "CITY"));
			s.addCell(new Label(headerColIdx++, 2, "POSTCODE"));
			s.addCell(new Label(headerColIdx++, 2, "STATE"));
			s.addCell(new Label(headerColIdx++, 2, "TYPE OF DIETARY"));
			s.addCell(new Label(headerColIdx++, 2, "LODGER NAME"));
			s.addCell(new Label(headerColIdx++, 2, "LODGER ID"));
			s.addCell(new Label(headerColIdx++, 2, "CREATE DATE"));
			s.addCell(new Label(headerColIdx++, 2, "APPLICATION REF NO"));
			s.addCell(new Label(headerColIdx++, 2, "EZBIZ RECEIPT NO"));
			s.addCell(new Label(headerColIdx++, 2, "EZBIZ INVOICE NO"));
			s.addCell(new Label(headerColIdx++, 2, "EXTERNAL RECEIPT NO"));
			s.addCell(new Label(headerColIdx++, 2, "EXTERNAL INVOICE NO"));
			s.addCell(new Label(headerColIdx++, 2, "EXTERNAL LPO/LOU NO"));
			s.addCell(new Label(headerColIdx++, 2, "REGISTRATION CHANNEL"));
			s.addCell(new Label(headerColIdx++, 2, "PAYMENT CHANNEL"));
			s.addCell(new Label(headerColIdx++, 2, "IS ATTEND?"));
			s.addCell(new Label(headerColIdx++, 2, "ELIGIBLE FOR CERT?"));
			s.addCell(new Label(headerColIdx++, 2, "ELIGIBLE FOR REFUND?"));
			s.addCell(new Label(headerColIdx++, 2, "REMARKS"));

			// Format
			NumberFormat twodpsNF = new NumberFormat("#0.00");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy h:m a");
			WritableCellFormat twodpsFormat = new WritableCellFormat(twodpsNF);

			int currRow = 2;
			int bil = 0;
			for (RobTrainingParticipant participant : listParticipant) {

				String ezbizReceipt = "";
				String ezbizInvoice = "";
				currRow++;
				bil++;
				int colIdx = 0;
				s.addCell(new Label(colIdx++, currRow, String.valueOf(bil)));
				s.addCell(new Label(colIdx++, currRow, participant.getName()));
				s.addCell(new Label(colIdx++, currRow, participant.getIcNo()));
				s.addCell(new Label(colIdx++, currRow, participant.getTelNo()));
				s.addCell(new Label(colIdx++, currRow, participant.getEmail()));
				s.addCell(new Label(colIdx++, currRow, mapFeeType.get(participant.getFeeType())));
				s.addCell(new Label(colIdx++, currRow, participant.getLsNo()));
				s.addCell(new Label(colIdx++, currRow, participant.getMembershipNo()));
				s.addCell(new jxl.write.Number(colIdx++, currRow, participant.getAmount(), twodpsFormat));
				s.addCell(new Label(colIdx++, currRow, mapOccupationCode.get(participant.getOccupation_code())));
				s.addCell(new Label(colIdx++, currRow, participant.getJob_title()));
				s.addCell(new Label(colIdx++, currRow, participant.getCompany()));
				s.addCell(new Label(colIdx++, currRow, participant.getAddress1()));
				s.addCell(new Label(colIdx++, currRow, participant.getAddress2()));
				s.addCell(new Label(colIdx++, currRow, participant.getAddress3()));
				s.addCell(new Label(colIdx++, currRow, participant.getCity()));
				s.addCell(new Label(colIdx++, currRow, participant.getPostcode()));
				s.addCell(new Label(colIdx++, currRow, mapStateCode.get(participant.getState())));
				s.addCell(new Label(colIdx++, currRow, mapTypeDietary.get(participant.getDiet())));
				s.addCell(new Label(colIdx++, currRow, participant.getRobTrainingTransaction().getLodgerName()));
				s.addCell(new Label(colIdx++, currRow, participant.getRobTrainingTransaction().getLodgerId()));
				s.addCell(new Label(colIdx++, currRow,
						dateFormat.format(participant.getRobTrainingTransaction().getCreateDt())));
				s.addCell(new Label(colIdx++, currRow, participant.getRobTrainingTransaction().getTransactionCode()));

				// excel bezakan normal reg, lpo dan free training
				if (!participant.getRobTrainingTransaction().getLodgerId().equals("SSM STAF")) {

					if (participant.getRobTrainingTransaction().getPaymentChannel().equals("UNPAID")
							&& participant.getRobTrainingTransaction().getRegistrationChannel().equals("ecomtrac")) {

						System.out.println(participant.getRobTrainingTransaction().getTransactionCode());
					} else if (participant.getRobTrainingTransaction().getPaymentChannel().equals("ezbiz")
							&& participant.getRobTrainingTransaction().getRegistrationChannel().equals("ecomtrac")) {

						System.out.println(participant.getRobTrainingTransaction().getTransactionCode());
					} else if (participant.getRobTrainingTransaction().getPaymentChannel().equals("ezbiz")
							&& participant.getRobTrainingTransaction().getRegistrationChannel().equals("ezbiz")) {

						System.out.println(participant.getRobTrainingTransaction().getTransactionCode());
						LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService
								.findByAppRefNoStatusPaymentMode(
										participant.getRobTrainingTransaction().getTransactionCode(),
										Parameter.PAYMENT_STATUS_SUCCESS, null);

						LlpPaymentReceipt llpPaymentReceipt = llpPaymentReceiptService
								.find(llpPaymentTransaction.getTransactionId());
						ezbizReceipt = llpPaymentReceipt.getReceiptNo();
						ezbizInvoice = llpPaymentReceipt.getTaxInvoiceNo();
					}
				}

//				//previous excel method
//				if (!participant.getRobTrainingTransaction().getLodgerId().equals("SSM STAF")) {
//					System.out.println(participant.getRobTrainingTransaction().getTransactionCode());
//					LlpPaymentTransaction llpPaymentTransaction = llpPaymentTransactionService
//							.findByAppRefNoStatusPaymentMode(
//									participant.getRobTrainingTransaction().getTransactionCode(),
//									Parameter.PAYMENT_STATUS_SUCCESS, null);
//
//					LlpPaymentReceipt llpPaymentReceipt = llpPaymentReceiptService
//							.find(llpPaymentTransaction.getTransactionId());
//					ezbizReceipt = llpPaymentReceipt.getReceiptNo();
//					ezbizInvoice = llpPaymentReceipt.getTaxInvoiceNo();
//				}

				s.addCell(new Label(colIdx++, currRow, ezbizReceipt));
				s.addCell(new Label(colIdx++, currRow, ezbizInvoice));
				s.addCell(new Label(colIdx++, currRow, participant.getRobTrainingTransaction().getReceiptNo()));
				s.addCell(new Label(colIdx++, currRow, participant.getRobTrainingTransaction().getInvoiceNo()));
				s.addCell(new Label(colIdx++, currRow, participant.getRobTrainingTransaction().getLouLoaRefNo()));
				s.addCell(new Label(colIdx++, currRow,
						mapRegisterChannel.get(participant.getRobTrainingTransaction().getRegistrationChannel())));
				s.addCell(new Label(colIdx++, currRow,
						mapPaymentChannel.get(participant.getRobTrainingTransaction().getPaymentChannel())));
				s.addCell(new Label(colIdx++, currRow, mapYesNo.get(participant.getIsAttend())));
				s.addCell(new Label(colIdx++, currRow, mapYesNo.get(participant.getIsEligible())));
				s.addCell(new Label(colIdx++, currRow, mapYesNo.get(participant.getIsRefund())));
				s.addCell(new Label(colIdx++, currRow, participant.getRemarkAbsent()));
			}

			s.mergeCells(0, 0, 15, 0);

			w.write();
			w.close();

			byte byteDataExcel[] = bAOS.toByteArray();
			bAOS.close();
			return byteDataExcel;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public List<RobTrainingParticipant> findAllEligible() {
		// TODO Auto-generated method stub
		return robTrainingParticipantDao.findAllEligible();
	}

	@Override
	public void generateCertificate(RobTrainingParticipant participant) {
		// TODO Auto-generated method stub

		RobTrainingTransaction robTrainingTransaction = participant.getRobTrainingTransaction();
		RobTrainingConfig trainingConfig = robTrainingTransaction.getTrainingId();

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
		String certType = trainingConfig.getTrainingType() + "_" + year;

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
					participant.setFileId(eCertComtracPdf.getFileDataId());
					robTrainingParticipantDao.update(participant);
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
					participant.setFileId(eCertComtracPdf.getFileDataId());
					robTrainingParticipantDao.update(participant);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Date calculateDob(String idno) throws ParseException {
		if (idno.length() == 12) {
			String Year = idno.substring(0, 2);
			String Month = idno.substring(2, 4);
			String Day = idno.substring(4, 6);
			Integer cutoff = Calendar.getInstance().get(Calendar.YEAR) - 2000;
			String dob = Day + "/" + Month + "/" + (Integer.valueOf(Year) > Math.abs(cutoff) ? 19 : 20) + Year;

			return new SimpleDateFormat(Parameter.DATEFORMATSHORT).parse(dob);
		} else {

			return null;
		}
	}

	@Override
	public Double totalRevenue(String type, String year, String month) {
		return robTrainingParticipantDao.totalRevenue(type, year, month);
	}
	
}
