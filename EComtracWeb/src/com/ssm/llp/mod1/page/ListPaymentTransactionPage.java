package com.ssm.llp.mod1.page;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.otcPayment.ViewCreditNoteSlipPanel;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentReceipt;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpPaymentReceiptService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionDetailService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.common.service.LlpRunningNoService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.ViewPaymentReceiptPanel;
import com.ssm.llp.base.page.ViewPaymentReceiptPanel2;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.sec.InternalUserEnviroment;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class ListPaymentTransactionPage extends LlpPaymentTransactionBasePage {
	@SpringBean(name = "LlpPaymentReceiptService")
	LlpPaymentReceiptService llpPaymentReceiptService;

	@SpringBean(name = "LlpRunningNoService")
	LlpRunningNoService llpRunningNoService;

	@SpringBean(name = "LlpPaymentTransactionDetailService")
	LlpPaymentTransactionDetailService llpPaymentTransactionDetailService;

	public ListPaymentTransactionPage() {
		this(null, null, null, null, null, null, null);
	}

	public ListPaymentTransactionPage(final Date searchFromDt, final Date searchToDt, final String paymentMode,
			final String transactionId, final String status, final String refNo, final String paymentGroup) {
		super(searchFromDt, searchToDt, paymentMode, transactionId, status, refNo, paymentGroup);
		SearchCriteria sc = getSearchCriteria(searchFromDt, searchToDt, paymentMode, transactionId, status, refNo,
				paymentGroup);

		SSMSortableDataProvider dp = new SSMSortableDataProvider("transactionId", sc,
				LlpPaymentTransactionService.class);
		final SSMDataView<LlpPaymentTransaction> dataView = new SSMDataView<LlpPaymentTransaction>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<LlpPaymentTransaction> item) {
				final LlpPaymentTransaction llpPaymentTransaction = item.getModelObject();

				item.add(new SSMLabel("requestDt", llpPaymentTransaction.getRequestDt(), "dd/MM/yyyy hh:mm:ss a"));
				item.add(new SSMLabel("transactionId", llpPaymentTransaction.getTransactionId()));
				item.add(new SSMLabel("approvalCode", llpPaymentTransaction.getApprovalCode()));
				item.add(new SSMLabel("paymentMode", llpPaymentTransaction.getPaymentMode()));
				item.add(new SSMLabel("paymentDetail", llpPaymentTransaction.getPaymentDetail()));
				item.add(new SSMLabel("amount", llpPaymentTransaction.getAmount()));
				item.add(new SSMLabel("payerName", llpPaymentTransaction.getPayerName()));
				item.add(new SSMLabel("payerId", llpPaymentTransaction.getPayerId()));
				item.add(new SSMLabel("status", llpPaymentTransaction.getStatus(), Parameter.PAYMENT_STATUS));

				if (Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())) {
					LlpPaymentReceipt receipt = llpPaymentReceiptService.find(llpPaymentTransaction.getTransactionId());
					item.add(new SSMLabel("receiptNo", receipt.getReceiptNo()));
				} else {
					item.add(new SSMLabel("receiptNo", ""));
				}

				final ModalWindow viewPaymentDetailsPopUp = new ModalWindow("viewDetailsDiv");
				item.add(viewPaymentDetailsPopUp);

				final ModalWindow viewReceiptPopUp = new ModalWindow("viewReceiptDiv");
				item.add(viewReceiptPopUp);

				final ModalWindow creditNotePopUp = new ModalWindow("viewCreditNoteDiv");
				item.add(creditNotePopUp);

				// Transaction detail modal window
				viewPaymentDetailsPopUp.setCookieName("viewDetailsCookies" + llpPaymentTransaction.getTransactionId());
				viewPaymentDetailsPopUp.setResizable(true);

				viewPaymentDetailsPopUp.setPageCreator(new ModalWindow.PageCreator() {
					@Override
					public Page createPage() {
						return new ViewLlpPaymentTransDetailsPage(ListPaymentTransactionPage.this.getPage(),
								viewPaymentDetailsPopUp, llpPaymentTransaction.getTransactionId());
					}
				});

				viewPaymentDetailsPopUp.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
					@Override
					public boolean onCloseButtonClicked(AjaxRequestTarget target) {
						return true;
					}
				});

				item.add(new AjaxLink<Void>("viewDetails") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						viewPaymentDetailsPopUp.show(target);
					}
				});

				// creditNote Modal
				if (llpPaymentTransaction.getPaymentMode() != null) {
					if (llpPaymentTransaction.getPaymentMode().equalsIgnoreCase(Parameter.PAYMENT_MODE_CN)) {
						creditNotePopUp
								.setCookieName("viewCreditNoteCookies" + llpPaymentTransaction.getTransactionId());
						creditNotePopUp.setResizable(true);

						creditNotePopUp.setContent(new ViewCreditNoteSlipPanel(creditNotePopUp.getContentId(),
								llpPaymentTransaction.getTransactionId()));

						creditNotePopUp.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
							@Override
							public boolean onCloseButtonClicked(AjaxRequestTarget target) {
								return true;
							}
						});
					} else {
						// Transaction detail modal window
						viewReceiptPopUp.setCookieName("viewReceiptCookies" + llpPaymentTransaction.getTransactionId());
						viewReceiptPopUp.setResizable(true);

						PageParameters param = new PageParameters();
						param.set("transId", llpPaymentTransaction.getTransactionId());
						viewReceiptPopUp.setContent(getRecieptPanel(viewReceiptPopUp.getContentId(), param));

						viewReceiptPopUp.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
							@Override
							public boolean onCloseButtonClicked(AjaxRequestTarget target) {
								return true;
							}
						});
					}
				} else {
					// Transaction detail modal window
					viewReceiptPopUp.setCookieName("viewReceiptCookies" + llpPaymentTransaction.getTransactionId());
					viewReceiptPopUp.setResizable(true);

					PageParameters param = new PageParameters();
					param.set("transId", llpPaymentTransaction.getTransactionId());
					viewReceiptPopUp.setContent(new ViewPaymentReceiptPanel(viewReceiptPopUp.getContentId(), param));

					viewReceiptPopUp.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
						@Override
						public boolean onCloseButtonClicked(AjaxRequestTarget target) {
							return true;
						}
					});
				}

				item.add(new AjaxLink<Void>("viewReceipt") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						viewReceiptPopUp.show(target);
					}

					@Override
					public boolean isVisible() {
						if (Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())
								&& !Parameter.PAYMENT_MODE_CN.equals(llpPaymentTransaction.getPaymentMode())
								&& llpPaymentTransaction.getLlpPaymentReceipt() != null) {
							return true;
						}
						return false;
					}
				});
				String confirmRegenerate = resolve("page.lbl.ezbiz.listPaymentTransaction.confirmRegenerateReceipt");
				SSMAjaxLink regenerateReceipt = new SSMAjaxLink("regenerateReceipt") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy h:m:s a");
						LlpPaymentReceipt llpPaymentReceipt = new LlpPaymentReceipt();
						String receiptNo = llpRunningNoService.generateRunningNo(Parameter.RECEIPT_RUNNING_NO,
								Parameter.RECEIPT_FIELDCODE, null, "yyyyMMdd", "000000",
								llpPaymentTransaction.getCreateDt());
						llpPaymentReceipt.setReceiptNo(receiptNo);
						llpPaymentReceipt.setCashReceived(llpPaymentTransaction.getAmount());
						llpPaymentReceipt.setTotalAmount(llpPaymentTransaction.getAmount());
						llpPaymentReceipt.setBalance(Double.valueOf(0));
						llpPaymentReceipt.setIsCancel(Parameter.PAYMENT_RECEIPT_ISCANCEL_no);
						llpPaymentReceipt.setTransactionId(llpPaymentTransaction.getTransactionId());
						llpPaymentReceipt.setRemarks("Receipt generate manually on " + dateFormat.format(new Date())
								+ " by " + UserEnvironmentHelper.getLoginName());
						llpPaymentReceipt.setCreateDt(llpPaymentTransaction.getCreateDt());
						llpPaymentReceipt.setUpdateDt(llpPaymentTransaction.getCreateDt());

						if (llpPaymentTransactionDetailService
								.isHaveSrProduct(llpPaymentTransaction.getTransactionId()) == true) {
							String taxInvoiceNo = llpRunningNoService.generateRunningNo(Parameter.INVOICE_RUNNING_NO,
									Parameter.INVOICE_FIELDCODE, null, null, "000000000", new Date());
							llpPaymentReceipt.setTaxInvoiceNo(taxInvoiceNo);
						}
						llpPaymentReceiptService.insert(llpPaymentReceipt);

						setResponsePage(new ListPaymentTransactionPage(searchFromDt, searchToDt, paymentMode,
								transactionId, status, refNo, paymentGroup));
					}

					@Override
					public boolean isVisible() {
						if (Parameter.PAYMENT_STATUS_SUCCESS.equals(llpPaymentTransaction.getStatus())
								&& !Parameter.PAYMENT_MODE_CN.equals(llpPaymentTransaction.getPaymentMode())
								&& llpPaymentTransaction.getLlpPaymentReceipt() == null) {
							return true;
						}
						return false;
					}
				};
				regenerateReceipt.setConfirmQuestion(confirmRegenerate);
				item.add(regenerateReceipt);

				item.add(new AjaxLink<Void>("viewCreditNote") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						creditNotePopUp.show(target);
					}

					@Override
					public boolean isVisible() {
						if (Parameter.PAYMENT_MODE_CN.equals(llpPaymentTransaction.getPaymentMode())) {
							return true;
						}
						return false;
					}
				});

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};

		dataView.setItemsPerPage(20L);
		add(new OrderByBorder("orderByTransactionId", "transactionId", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(new OrderByBorder("orderByRequestDt", "requestDt", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(new OrderByBorder("orderByPaymentMode", "paymentMode", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(new OrderByBorder("orderByStatus", "status", dp) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged() {
				dataView.setCurrentPage(0);
			}
		});

		add(dataView);
		add(new SSMPagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));

		SSMDownloadLink linkDownload = new SSMDownloadLink("downloadExcel") {
			public void onClick() {
				if (getByteData() != null) {
					super.onClick();
					return;
				}
				try {
					String fileName = "Payment Transaction-" + DateUtil.formatDate(new Date(), "yyyyMMdd") + ".xls";
					byte byteExcel[] = ((LlpPaymentTransactionService) getService(
							LlpPaymentTransactionService.class.getSimpleName())).generateExcel(searchFromDt, searchToDt,
									paymentMode, transactionId, status, refNo, paymentGroup);
					setDownloadData(fileName, SSMDownloadLink.TYPE_EXCEL, byteExcel);
					super.onClick();
				} catch (Exception e) {
					ssmError(e.getMessage());
					e.printStackTrace();
				}
			}

		};
		linkDownload.setVisible(false);
		add(linkDownload);

		SSMDownloadLink linkGafDownload = new SSMDownloadLink("linkGafDownload") {
			public void onClick() {
				if (getByteData() != null) {
					super.onClick();
					return;
				}
				try {
					String fileName = "GAF-EZBIZ-" + DateUtil.formatDate(new Date(), "yyyyMMdd") + ".txt";
//					SearchCriteria sc = getSearchCriteria();

					byte byteGaf[] = ((LlpPaymentTransactionService) getService(
							LlpPaymentTransactionService.class.getSimpleName())).generateGaf(searchFromDt, searchToDt,
									paymentMode, transactionId, status, refNo, paymentGroup);
					setDownloadData(fileName, "application/txt", byteGaf);
					super.onClick();
				} catch (Exception e) {
					ssmError(e.getMessage());
					e.printStackTrace();
				}
			}

		};

		add(linkGafDownload);
		linkGafDownload.setVisible(false);
		if (UserEnvironmentHelper.getUserenvironment() instanceof InternalUserEnviroment) {
			if (Parameter.PAYMENT_STATUS_SUCCESS.equals(status)) {
				linkGafDownload.setVisible(true);
			}
			linkDownload.setVisible(true);
		}
	}

	private final SearchCriteria getSearchCriteria(Date searchFromDt, Date searchToDt, String paymentMode,
			String transactionId, String status, String refNo, String paymentGroup) {
		SearchCriteria sc = null;
		if (searchFromDt == null && searchToDt == null && paymentMode == null && transactionId == null && status == null
				&& refNo == null) {
			if (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment) {
				searchFromDt = new Date();
			}
		}
		String paymentModes[] = null;
//		if(Parameter.PAYMENT_MODE_CC.equals(paymentMode)){
//			paymentModes = new String[]{"CC","DD","credit card"};
//		}else if(Parameter.PAYMENT_MODE_CASH.equals(paymentMode)){
//			paymentModes = new String[]{"CASH"};
//		}else if(Parameter.PAYMENT_MODE_CN.equals(paymentMode) || "CN".equals(paymentMode)){
//			paymentModes = new String[]{"CREDIT NOTE"};
//		}else if(Parameter.PAYMENT_MODE_fpx.equals(paymentMode)){
//			paymentModes = new String[]{"fpx"};
//		}
		if (StringUtils.isNotBlank(paymentMode)) {
			paymentModes = getCodeTypeWithValue(Parameter.PAYMENT_MODE_SEARCH_MAPPING, paymentMode).split(",");
		}

		if (StringUtils.isNotBlank((status))) {
			if (!Parameter.PAYMENT_STATUS_ALL.equals(status)) {
				sc = new SearchCriteria("status", SearchCriteria.EQUAL, status);
			}
		}

		if (StringUtils.isNotBlank((paymentGroup))) {
			String paymentGroupPrefix = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_GROUP_REFNO_PREFIX,
					paymentGroup);
			String paymentGroupPrefixArr[] = StringUtils.split(paymentGroupPrefix, ",");

			if (paymentGroupPrefixArr.length == 1) {
				if (sc != null) {
					SearchCriteria newSc = new SearchCriteria("appRefNo", SearchCriteria.LIKE,
							paymentGroupPrefixArr[0] + "%");
					sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
				} else {
					sc = new SearchCriteria("appRefNo", SearchCriteria.LIKE, paymentGroupPrefixArr[0] + "%");
				}

			} else if (paymentGroupPrefixArr.length > 1) {
				SearchCriteria newScGroup = new SearchCriteria("appRefNo", SearchCriteria.LIKE,
						paymentGroupPrefixArr[0] + "%");
				for (int i = 1; i < paymentGroupPrefixArr.length; i++) {
					SearchCriteria newSc1 = new SearchCriteria("appRefNo", SearchCriteria.LIKE,
							paymentGroupPrefixArr[i] + "%");
					newScGroup = new SearchCriteria(newScGroup, SearchCriteria.OR, newSc1);
				}

				if (sc != null) {
					sc = new SearchCriteria(sc, SearchCriteria.AND, newScGroup);
				} else {
					sc = newScGroup;
				}
			}

		}

		if (StringUtils.isNotBlank((paymentMode))) {
			if (sc != null) {
				SearchCriteria newSc = new SearchCriteria("paymentMode", SearchCriteria.IN, paymentModes, false);
				sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
			} else {
				sc = new SearchCriteria("paymentMode", SearchCriteria.IN, paymentModes, false);
			}
		}

		if (StringUtils.isNotBlank((transactionId))) {
			if (sc != null) {
				SearchCriteria newSc = new SearchCriteria("transactionId", SearchCriteria.EQUAL, transactionId);
				sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
			} else {
				sc = new SearchCriteria("transactionId", SearchCriteria.EQUAL, transactionId);
			}
		}

		if (StringUtils.isNotBlank((refNo))) {
			if (sc != null) {
				SearchCriteria newSc = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, refNo);
				sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
			} else {
				sc = new SearchCriteria("appRefNo", SearchCriteria.EQUAL, refNo);
			}
		}

		if (searchFromDt != null) {
			if (sc != null) {
				SearchCriteria newSc = new SearchCriteria("requestDt", SearchCriteria.GREATER_EQUAL, searchFromDt);
				sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
			} else {
				sc = new SearchCriteria("requestDt", SearchCriteria.GREATER_EQUAL, searchFromDt);
			}
		}

		if (searchToDt != null) {
			Date toDate = new Date(searchToDt.getTime() + TimeUnit.DAYS.toMillis(1));
			if (sc != null) {
				SearchCriteria newSc = new SearchCriteria("requestDt", SearchCriteria.LESS_EQUAL, toDate);
				sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
			} else {
				sc = new SearchCriteria("requestDt", SearchCriteria.LESS_EQUAL, toDate);
			}
		}

		if (UserEnvironmentHelper.getUserenvironment() instanceof LlpUserEnviroment) {
			String userLlp = UserEnvironmentHelper.getUserenvironment().getLoginName();
			if (StringUtils.isNotBlank(userLlp)) {
				if (sc != null) {
					SearchCriteria newSc = new SearchCriteria("createBy", SearchCriteria.EQUAL, userLlp);
					sc = new SearchCriteria(sc, SearchCriteria.AND, newSc);
				} else {
					sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, userLlp);
				}
			}
		}

		return sc;
	}

	public String getPageTitle() {
		String titleKey = "page.title.payment.transactionlist";
		return titleKey;
	}
}
