/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ssm.llp.mod1.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.common.service.RocBusinessObjectCodeService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.BaseFrame;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RocBusinessObjectCode;
import com.ssm.llp.mod1.model.LlpBusinessCodeLink;
import com.ssm.llp.mod1.model.LlpRegistration;
import com.ssm.llp.mod1.service.LlpRegistrationService;

/**
 * @author Matej Knopp
 * 
 */
public class SearchBusinessCodePage extends BaseFrame {
	private Page parentPage;
	private String llpNo;
	
	@SpringBean(name = "LlpRegTransactionService")
	private LlpRegTransactionService llpRegTransactionService;
	/**
	 * 
	 * @param modalWindowPage
	 * @param window
	 */
	public SearchBusinessCodePage(final Page parentPage, final ModalWindow window, String searchStr) {
		this.parentPage = parentPage;
		this.llpNo = llpNo;
		add(new InputForm("inputForm", parentPage, window, searchStr));
	}

	/** form for processing the input. */
	private class InputForm extends Form {

		private String searchString;
		// holds NameWrapper elements
		private WebMarkupContainer wmc;
		private SSMDataView dataView;

		private List<RocBusinessObjectCode> listSelected = new ArrayList<RocBusinessObjectCode>();

		public InputForm(String name, final Page parentPage, final ModalWindow window, String searchString) {
			super(name);
			this.searchString = searchString;
			populateTable();
			SSMTextField tf = new SSMTextField("searchString", new PropertyModel(this, "searchString"));
			add(tf);

			add(new AjaxButton("ajaxSubmit") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					System.out.println("AJAX");
					// LlpBusinessCodeLinkService llpCodeService =
					// (LlpBusinessCodeLinkService)
					// getService(LlpBusinessCodeLinkService.class.getSimpleName());
					//
					// for (int i = 0; i < listSelected.size(); i++) {
					// LlpBusinessCodeLink codeLink = new LlpBusinessCodeLink();
					// codeLink.setBusinessCode(listSelected.get(i).getVchbusinesscode());
					// codeLink.setLlpNo(llpNo);
					// codeLink.setStatus(Parameter.BUSINESS_CODE_STATUS_active);
					// llpCodeService.insert(codeLink);
					// }
					//
					final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
					LlpRegistrationService llpRegistrationService = (LlpRegistrationService) getService(LlpRegistrationService.class.getSimpleName());
					
					try {
						List<LlpBusinessCodeLink> llpBusinessCodeLinks = llpRegistration.getLlpBusinessCodeLink();
							
						int count = llpBusinessCodeLinks.size()+listSelected.size();
						System.out.println("BizCodeLink total: " +count);
						if(count>3){
							llpRegistrationService.validateBizCodePopup(count); //validate before add
						}
						
						for (int i = 0; i < listSelected.size(); i++) {
							LlpBusinessCodeLink codeLink = new LlpBusinessCodeLink();
							codeLink.setBusinessCode(listSelected.get(i).getVchbusinesscode());
							codeLink.setLlpNo(llpNo);
							codeLink.setStatus(Parameter.BUSINESS_CODE_STATUS_active);
							codeLink.setRocBusinessObjectCode(listSelected.get(i));
							// llpCodeService.insert(codeLink);
							llpBusinessCodeLinks.add(codeLink);
						}
						
						getSession().setAttribute("llpRegistration_",llpRegistration);
						window.close(target);
					} catch (SSMException e) {
						ssmError(e);
						target.add(feedbackPanel); //error throws (2) (try-catch)
					}
					
					
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// update feedback to display errors
					//error if ie. have required mandatory field in popup (1)
				}

			});

			add(new AjaxButton("ajaxSubmitSearch") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					final LlpRegistration llpRegistration = (LlpRegistration) getSession().getAttribute("llpRegistration_");
					setResponsePage(new SearchBusinessCodePage(parentPage, window, getSearchString()));
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// update feedback to display errors
				}

			});
		}

		private void populateTable() {
			SearchCriteria sc = null;
			if (StringUtils.isNotBlank(getSearchString())) {
				sc = new SearchCriteria("chrstatus", SearchCriteria.EQUAL, "A");
				sc = SearchCriteria.andIfNotNull(sc, "vchbusinessdesceng", SearchCriteria.LIKE, "%" + getSearchString().toUpperCase() + "%");
			}

			SSMSortableDataProvider dp = new SSMSortableDataProvider("vchbusinessdesceng", sc, RocBusinessObjectCodeService.class);
			dataView = new SSMDataView<RocBusinessObjectCode>("sorting", dp) {
				private static final long serialVersionUID = 1L;

				protected void populateItem(final Item<RocBusinessObjectCode> item) {
					RocBusinessObjectCode code = item.getModelObject();
					item.add(new SSMLabel("businessCode", code.getVchbusinesscode()));
					item.add(new SSMLabel("businessDesc", code.getVchbusinessdesceng()));
					// item.add(new SSMCheckBox("check", new PropertyModel(code,
					// "selected")));

					item.add(new AjaxCheckBox("check", new PropertyModel(code, "selected")) {
						@Override
						protected void onUpdate(AjaxRequestTarget target) {
							RocBusinessObjectCode businessObjectCode = (RocBusinessObjectCode) getParent().getDefaultModelObject();
							if (String.valueOf(true).equals(getValue())) {
								businessObjectCode.setSelected(true);
								listSelected.add(businessObjectCode);
							} else {
								businessObjectCode.setSelected(false);
								listSelected.remove(businessObjectCode);
							}
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

			// add(dataView);
			dataView.setOutputMarkupId(true);

			PagingNavigator navigator = new PagingNavigator("navigator", dataView);
			// add(navigator);
			navigator.setOutputMarkupId(true);

			NavigatorLabel navigatorLabel = new NavigatorLabel("navigatorLabel", dataView);
			// add(navigatorLabel);
			navigatorLabel.setOutputMarkupId(true);

			if (wmc == null) {
				wmc = new WebMarkupContainer("listDataView");
			}
			wmc.add(dataView);
			wmc.add(navigator);
			wmc.add(navigatorLabel);
			wmc.setOutputMarkupId(true);
			add(wmc);
		}

		public String getSearchString() {
			return searchString;
		}

		public void setSearchString(String searchString) {
			this.searchString = searchString;
		}
	}

	@Override
	public String getPageTitle() {
		return "search.business.code.title";
	}

	public Page getParentPage() {
		return parentPage;
	}
}
