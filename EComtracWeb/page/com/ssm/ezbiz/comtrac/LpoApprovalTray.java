package com.ssm.ezbiz.comtrac;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robformA.EditRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAIncentivePage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage;
import com.ssm.ezbiz.robformA.ViewRobFormAPage2;
import com.ssm.ezbiz.service.RobFormAOwnerService;
import com.ssm.ezbiz.service.RobFormAService;
import com.ssm.ezbiz.service.RobTrainingConfigService;
import com.ssm.ezbiz.service.RobTrainingTransactionService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.RobTrainingReprintcertList.SearchTrainingModel;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "all" })
public class LpoApprovalTray extends SecBasePage {

	@SpringBean(name = "RobTrainingTransactionService")
	RobTrainingTransactionService robTrainingTransactionService;

	@SpringBean(name = "LlpUserProfileService")
	LlpUserProfileService llpUserProfileService;

	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;

	public LpoApprovalTray() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SearchListLpoModel lpoModel = new SearchListLpoModel();
				return lpoModel;
			}
		}));
		add(new SearchLpoForm("form", (IModel<SearchListLpoModel>) getDefaultModel()));
	}

	private class SearchLpoForm extends Form implements Serializable {

		public SearchLpoForm(String id, IModel<SearchListLpoModel> m) {
			super(id, m);

			SearchCriteria searchResult = null;
			SearchListLpoModel searchModel = m.getObject();
			SSMTextField transactionCode = new SSMTextField("transactionCode");
			transactionCode.setLabelKey("page.lbl.comtrac.lpoSearch.transactionCode");
			add(transactionCode);

			SSMDateTextField createDtFrom = new SSMDateTextField("createDtFrom");
			createDtFrom.setLabelKey("page.lbl.comtrac.lpoSearch.createDtFrom");
			add(createDtFrom);

			SSMDateTextField createDtTo = new SSMDateTextField("createDtTo");
			createDtTo.setLabelKey("page.lbl.comtrac.lpoSearch.createDtTo");
			add(createDtTo);

			SSMDropDownChoice status = new SSMDropDownChoice("status", Parameter.COMTRAC_TRANSACTION_STATUS);
			status.getChoices().add(0, Parameter.PAYMENT_STATUS_ALL);
			status.setLabelKey("page.lbl.ecomtrac.paymentStatus");
			add(status);

			SSMAjaxButton search = new SSMAjaxButton("search") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

					SearchListLpoModel searchModelForm = (SearchListLpoModel) form.getDefaultModelObject();
					SearchCriteria sc = generateScTemplate(searchModelForm.getTransactionCode(),
							searchModelForm.getStatus(), searchModelForm.getCreateDtFrom(),
							searchModelForm.getCreateDtTo());
					populateTable(sc, target);
				}
			};
			add(search);

			searchResult = new SearchCriteria("paymentChannel", SearchCriteria.EQUAL,
					Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid).andIfNotNull("registrationChannel", SearchCriteria.EQUAL,
							Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac);

			populateTable(searchResult, null);

		}

		public void populateTable(SearchCriteria sc, AjaxRequestTarget target) {
			WebMarkupContainer wmcSearchResult = new WebMarkupContainer("wmcSearchResult");
			wmcSearchResult.setOutputMarkupId(true);
			wmcSearchResult.setVisible(true);

			SSMSortableDataProvider dp = new SSMSortableDataProvider("updateDt", SortOrder.DESCENDING, sc,
					RobTrainingTransactionService.class);
			final SSMDataView<RobTrainingTransaction> dataView = new SSMDataView<RobTrainingTransaction>("sorting",
					dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<RobTrainingTransaction> item) {
					final RobTrainingTransaction robTrainingTransaction = item.getModelObject();

//						item.add(new SSMLabel("robFormACode", robTrainingTransaction.getRobFormACode()));
//						item.add(new SSMLabel("bizName", robTrainingTransaction.getBizName()));
//						item.add(new SSMLabel("bizOwnerName", robFormAOwnerService.findByRobFormACode(robFormA.getRobFormACode()).get(0).getName()));
//						item.add(new SSMLabel("status", robTrainingTransaction.getStatus(), Parameter.COMTRAC_TRANSACTION_STATUS ));
//						item.add(new SSMLabel("createBy", robTrainingTransaction.getCreateBy()));
//						item.add(new SSMLabel("createDt", robTrainingTransaction.getCreateDt() , "dd/MM/yyyy hh:mm:ss a"));
//						item.add(new SSMLabel("updateDt", robTrainingTransaction.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));

					item.add(new SSMLabel("transactionCode", robTrainingTransaction.getTransactionCode()));
					item.add(new SSMLabel("trainingCode", robTrainingTransaction.getTrainingId().getTrainingCode()));
					item.add(new SSMLabel("trainingName", robTrainingTransaction.getTrainingId().getTrainingName()));
					item.add(new SSMLabel("status", robTrainingTransaction.getStatus(),
							Parameter.COMTRAC_TRANSACTION_STATUS));
					item.add(new SSMLabel("createDt", robTrainingTransaction.getCreateDt(), "dd/MM/yyyy hh:mm:ss a"));
//						item.add(new SSMLabel("updateDt", robTrainingTransaction.getUpdateDt() , "dd/MM/yyyy hh:mm:ss a"));

					item.add(new Link("detail", item.getDefaultModel()) {
						public void onClick() {
							RobTrainingTransaction robTrainingTransaction = item.getModelObject();
							setResponsePage(
									new ViewListLpoPage(robTrainingTransaction.getTransactionCode(), getPage()));
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

			wmcSearchResult.add(dataView);
			wmcSearchResult.add(new SSMPagingNavigator("navigator", dataView));
			wmcSearchResult.add(new NavigatorLabel("navigatorLabel", dataView));

			if (target == null) {
				add(wmcSearchResult);
			} else {
				replace(wmcSearchResult);
				target.add(wmcSearchResult);
			}

		}

		public SearchCriteria generateScTemplate(String transactionCode, String status, Date createDtFrom,
				Date createDtTo) {

			SearchCriteria sc = null;
			SearchCriteria sc1 = null;

			if (StringUtils.isNotBlank(transactionCode)) {
				sc = new SearchCriteria("paymentChannel", SearchCriteria.EQUAL,
						Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid)
								.andIfNotNull("registrationChannel", SearchCriteria.EQUAL,
										Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac)
								.andIfNotNull("transactionCode", SearchCriteria.EQUAL, transactionCode);
			}	

//				if(StringUtils.isNotBlank(searchModelForm.getIcNo())){
//					LlpUserProfile profile = llpUserProfileService.findByIdTypeAndIdNo("01", searchModelForm.getIcNo());
//					if(profile != null){
//						if(sc==null){
//							sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, profile.getLoginId()); 
//						}else{
//							sc = sc.andIfNotNull("createBy", SearchCriteria.EQUAL, profile.getLoginId());
//						}
//					}else{
//						if(sc==null){
//							sc = new SearchCriteria("createBy", SearchCriteria.EQUAL, ""); 
//						}else{
//							sc = sc.andIfNotNull("createBy", SearchCriteria.EQUAL, "");
//						}
//					}
//				}

			if (sc != null) {
				if (status != null) {
					if (status != null) {
						if (status.equals("ALL")) {
//							sc = new SearchCriteria("createBy", SearchCriteria.IS_NOT_NULL);
							sc = new SearchCriteria("paymentChannel", SearchCriteria.EQUAL,
									Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid).andIfNotNull("registrationChannel",
											SearchCriteria.EQUAL, Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac);
						} else {
							sc = new SearchCriteria("paymentChannel", SearchCriteria.EQUAL,
									Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid)
											.andIfNotNull("registrationChannel", SearchCriteria.EQUAL,
													Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac)
											.andIfNotNull("status", SearchCriteria.EQUAL, status);
						}
					}
				}
			} else {
				if (status != null) {
					if (status.equals("ALL")) {
//						sc = new SearchCriteria("createBy", SearchCriteria.IS_NOT_NULL);
						sc = new SearchCriteria("paymentChannel", SearchCriteria.EQUAL,
								Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid).andIfNotNull("registrationChannel",
										SearchCriteria.EQUAL, Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac);
					} else {
						sc = new SearchCriteria("paymentChannel", SearchCriteria.EQUAL,
								Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid)
										.andIfNotNull("registrationChannel", SearchCriteria.EQUAL,
												Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac)
										.andIfNotNull("status", SearchCriteria.EQUAL, status);
					}
				}
			}

			if (createDtFrom != null) {
				if (sc == null) {
					sc = new SearchCriteria("paymentChannel", SearchCriteria.EQUAL,
							Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid)
									.andIfNotNull("registrationChannel", SearchCriteria.EQUAL,
											Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac)
									.andIfNotNull("createDt", SearchCriteria.GREATER_EQUAL, createDtFrom);
				} else {
					sc = sc.andIfNotNull("createDt", SearchCriteria.GREATER_EQUAL, createDtFrom);
				}
			}
			if (createDtTo != null) {
				createDtTo.setHours(23);
				createDtTo.setMinutes(59);
				createDtTo.setSeconds(59);
				if (sc == null) {
					sc = new SearchCriteria("paymentChannel", SearchCriteria.EQUAL,
							Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid)
									.andIfNotNull("registrationChannel", SearchCriteria.EQUAL,
											Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac)
									.andIfNotNull("paymentChannel", SearchCriteria.EQUAL,
											Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid)
									.andIfNotNull("registrationChannel", SearchCriteria.EQUAL,
											Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac)
									.andIfNotNull("createDt", SearchCriteria.LESS_EQUAL, createDtTo);
				} else {
					sc = sc.andIfNotNull("paymentChannel", SearchCriteria.EQUAL,
							Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid)
							.andIfNotNull("registrationChannel", SearchCriteria.EQUAL,
									Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac)
							.andIfNotNull("createDt", SearchCriteria.LESS_EQUAL, createDtTo);
				}
			}

			if (sc == null) {
				sc = new SearchCriteria("paymentChannel", SearchCriteria.EQUAL,
						Parameter.COMTRAC_PAYMENT_CHANNEL_unpaid).andIfNotNull("registrationChannel",
								SearchCriteria.EQUAL, Parameter.COMTRAC_REGISTRATION_CHANNEL_ecomtrac);
			}

			return sc;

		}

	}

	private class SearchListLpoModel {
		private String transactionCode;
		private String status;
		private Date createDtFrom;
		private Date createDtTo;

		public String getTransactionCode() {
			return transactionCode;
		}

		public void setTransactionCode(String transactionCode) {
			this.transactionCode = transactionCode;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Date getCreateDtFrom() {
			return createDtFrom;
		}

		public void setCreateDtFrom(Date createDtFrom) {
			this.createDtFrom = createDtFrom;
		}

		public Date getCreateDtTo() {
			return createDtTo;
		}

		public void setCreateDtTo(Date createDtTo) {
			this.createDtTo = createDtTo;
		}
	}

//		@Override
//		public String getPageTitle() {
//		return "menu.comtrac.listLpoVerification";
//		}
}
