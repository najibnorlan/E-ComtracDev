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
package com.ssm.llp.page.supplyinfo;

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
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.model.LlpSupplyInfoDtl;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.base.common.service.LlpSupplyInfoDtlService;
import com.ssm.llp.base.common.service.RocBusinessObjectCodeService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.BaseFrame;
import com.ssm.llp.base.page.LLPFileAttachmentPage;
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
public class LlpSupplyInfoUploadPopUpPage extends BaseFrame {
	
	@SpringBean(name = "LlpSupplyInfoDtlService")
	private LlpSupplyInfoDtlService llpSupplyInfoDtlService;
	/**
	 * 
	 * @param modalWindowPage
	 * @param window
	 */
	public LlpSupplyInfoUploadPopUpPage(final LlpSupplyInfoDetailPanel parentPage, final ModalWindow window, String uploadType, LlpSupplyInfoDtl llpSupplyInfoDtl) {
		add(new InputForm("inputForm", parentPage, window, uploadType, llpSupplyInfoDtl));
	}

	/** form for processing the input. */
	private class InputForm extends Form {
		private List<FileUpload> fileUpload;
		public InputForm(String name, final LlpSupplyInfoDetailPanel parentPage, final ModalWindow window, final String uploadType, final LlpSupplyInfoDtl llpSupplyInfoDtl) {
			super(name);
			
			SSMLabel llpNo = new SSMLabel("entityNo",llpSupplyInfoDtl.getEntityNo());
			add(llpNo);
			
			SSMLabel llpName = new SSMLabel("entityName",llpSupplyInfoDtl.getEntityName());
			add(llpName);
			
			SSMLabel uploadTypeLbl = new SSMLabel("uploadType",uploadType);
			add(uploadTypeLbl);
			
			FileUploadField fileUpload = new FileUploadField("fileUpload", new PropertyModel(this, "fileUpload"));
			fileUpload.setRequired(true);
			add(fileUpload);
			
			
			add(new AjaxButton("upload") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

					if (getFileUpload() != null) {
						llpSupplyInfoDtlService.updateUploadFile(llpSupplyInfoDtl.getDtlId(), uploadType, getFileUpload().get(0).getBytes());
					}

					ssmSuccess("success.update");
					window.close(target);
					
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// update feedback to display errors
					//error if ie. have required mandatory field in popup (1)
				}

			});
		}
		public List<FileUpload> getFileUpload() {
			return fileUpload;
		}
		public void setFileUpload(List<FileUpload> fileUpload) {
			this.fileUpload = fileUpload;
		}
		

		
	}

	@Override
	public String getPageTitle() {
		return "supplyInfo.upload.title";
	}

}
