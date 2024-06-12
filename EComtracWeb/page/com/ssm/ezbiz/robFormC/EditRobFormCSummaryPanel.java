package com.ssm.ezbiz.robFormC;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.ezbiz.model.RobFormC;

public class EditRobFormCSummaryPanel extends RobFormCBasePanel{
	
	public EditRobFormCSummaryPanel(String panelId, final RobFormC robFormC) {
		super(panelId);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	return new EditRobFormCSummaryModel();
            }
        }));
		add(new RobFormCSummaryForm("robFormCSummaryForm",getDefaultModel(),robFormC));
	}
	
	private class RobFormCSummaryForm extends Form implements Serializable {
		
		final EditRobFormCSummaryModel robFormSummaryModel;
		final boolean isQuery;
		final SSMAjaxButton submitPayment;
		final RepeatingView listError;
		final SSMLabel formCFee;
		final SSMLabel totalFormCFee;
		final SSMLabel bisnessInfoFee;
		final SSMLabel bisnessInfoFeeQuantity;
		final SSMLabel totalBisnessInfoFee;
		final SSMLabel totalFee;
		final SSMLabel summaryError;
		
		public RobFormCSummaryForm(String id, IModel m, final RobFormC robFormC) {
			super(id, m);
			setPrefixLabelKey("page.lbl.ezbiz.robFormCSummary.");
			robFormSummaryModel = (EditRobFormCSummaryModel) m.getObject();
			
			if(Parameter.ROB_FORM_C_STATUS_QUERY.equals(robFormC.getStatus())){
				isQuery = true;
			}else{
				isQuery = false;
			}
			formCFee = new SSMLabel("formCFee","");
			add(formCFee);
			
			totalFormCFee = new SSMLabel("totalFormCFee","");
			add(totalFormCFee);
			
			bisnessInfoFee = new SSMLabel("bisnessInfoFee","");
			add(bisnessInfoFee);
			
			bisnessInfoFeeQuantity = new SSMLabel("bisnessInfoFeeQuantity","");
			add(bisnessInfoFeeQuantity);
			
			totalBisnessInfoFee = new SSMLabel("totalBisnessInfoFee","");
			add(totalBisnessInfoFee);
			
			
			totalFee = new SSMLabel("totalFee","");
			add(totalFee);
			
			summaryError = new SSMLabel("summaryError","");
			summaryError.setEscapeModelStrings(true);
			add(summaryError);
			
			listError = new RepeatingView("listError");
			listError.setVisible(false);
			listError.setOutputMarkupId(true);
			add(listError);
			
			
			final SSMTextArea queryAnswer = new SSMTextArea("queryAnswer");
			queryAnswer.setVisible(false);
			queryAnswer.setOutputMarkupId(true);
			add(queryAnswer);
			
			if(isQuery){
				queryAnswer.setVisible(true);
			}
			
			final SSMAjaxCheckBox declarationChkBox = new SSMAjaxCheckBox("declarationChkBox", new PropertyModel(robFormSummaryModel, "declarationChkBox") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) {
						robFormSummaryModel.setDeclarationChkBox(true);
					} else {
						robFormSummaryModel.setDeclarationChkBox(false);
					}
					if(submitPayment.isVisible()){
						submitPayment.setEnabled(robFormSummaryModel.getDeclarationChkBox());
						target.add(submitPayment);
					}
					
					
				}
			};
			add(declarationChkBox);
			
			submitPayment = new SSMAjaxButton("submitPayment") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					if(listError.size()>0){
						return;
					}
				}
			};
			submitPayment.setOutputMarkupId(true);
			submitPayment.setEnabled(false);
			add(submitPayment);
			
			
			
			SSMAjaxButton cSummaryPrev = new SSMAjaxButton("cSummaryPrev") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 3);//mean show segment 3 - B1
				}
			};
			add(cSummaryPrev);
		}
	}
	
	private class EditRobFormCSummaryModel implements Serializable{
		private String status;
		private String queryAnswer;
		private Boolean declarationChkBox = Boolean.FALSE;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getQueryAnswer() {
			return queryAnswer;
		}

		public void setQueryAnswer(String queryAnswer) {
			this.queryAnswer = queryAnswer;
		}

		public Boolean getDeclarationChkBox() {
			return declarationChkBox;
		}

		public void setDeclarationChkBox(Boolean declarationChkBox) {
			this.declarationChkBox = declarationChkBox;
		}
		
	}
}