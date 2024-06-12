package com.ssm.llp.mod1.page;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class AddPaymentCode extends SecBasePage  {

	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;
	public AddPaymentCode() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				return new PaymentObject();
			}
		}));
		init();
	}
	
	
	public AddPaymentCode(final String paymentCode) {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {		
				PaymentObject obj = new PaymentObject();
							
				String desc = ((LlpParametersService)getService(LlpParametersService.class.getSimpleName())).findByCodeTypeValue(Parameter.PAYMENT_TYPE, paymentCode);
//				String desc = llpParametersService.findByCodeTypeValue(Parameter.PAYMENT_TYPE, paymentCode);
				
				LlpPaymentFee fee = llpPaymentFeeService.findById(paymentCode);
				obj.setPaymentCode(fee.getPaymentCode());
				obj.setPaymentDesc(desc);
				obj.setPaymentFee(fee.getPaymentFee());
				obj.setGstCode(fee.getGstCode());
				obj.setStatus(fee.getStatus());
				return obj;
			}
		}));
		init();
	}
	
	private void init() {
		add(new PaymentFeeForm("form", getDefaultModel()));
	}
	
	private class PaymentObject  implements Serializable{
		String paymentCode;
		String paymentDesc;
		double paymentFee;
		String gstCode;
		String status;
		
		public PaymentObject() {
			// TODO Auto-generated constructor stub
		}
		
		public PaymentObject(String paymentCode,String paymentDesc,double paymentFee,String status) {
			this.paymentCode=paymentCode;
			this.paymentDesc=paymentDesc;
			this.paymentFee = paymentFee;
			this.status = status;
		}
		
		public String getPaymentCode() {
			return paymentCode;
		}
		public void setPaymentCode(String paymentCode) {
			this.paymentCode = paymentCode;
		}
		public String getPaymentDesc() {
			return paymentDesc;
		}
		public void setPaymentDesc(String paymentDesc) {
			this.paymentDesc = paymentDesc;
		}
		public double getPaymentFee() {
			return paymentFee;
		}
		public void setPaymentFee(double paymentFee) {
			this.paymentFee = paymentFee;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}

		public String getGstCode() {
			return gstCode;
		}

		public void setGstCode(String gstCode) {
			this.gstCode = gstCode;
		}
	}

	private class PaymentFeeForm extends Form implements Serializable{

		public PaymentFeeForm(String id, IModel m) {
			super(id, m);

			SSMTextField paymentCode = new SSMTextField("paymentCode");
			add(paymentCode);
			
			SSMTextField paymentDesc = new SSMTextField("paymentDesc");
			paymentDesc.setUpperCase(false);
			add(paymentDesc);
			
			SSMTextField paymentFee = new SSMTextField("paymentFee");
		    add(paymentFee);
		    
		    SSMTextField gstCode = new SSMTextField("gstCode");
		    add(gstCode);
		
			SSMDropDownChoice status = new SSMDropDownChoice("status", Parameter.PAYMENT_CONFIG);
			add(status);
		    
//			SSMTextField status = new SSMTextField("status");
//			add(status);
			
			add(new Button("save") {
				public void onSubmit() {
					PaymentObject obj = (PaymentObject)getForm().getModelObject();
					
					LlpPaymentFee lp =  llpPaymentFeeService.findById(obj.getPaymentCode());
				
					
					if(lp==null){
						lp = new LlpPaymentFee();
						lp.setPaymentCode(obj.getPaymentCode());
						lp.setPaymentFee(obj.getPaymentFee());
						lp.setGstCode(obj.getGstCode());
						lp.setStatus(obj.getStatus());
						llpPaymentFeeService.insertWithParameter(lp, obj.paymentDesc);
					}else {
						lp.setPaymentCode(obj.getPaymentCode());
						lp.setPaymentFee(obj.getPaymentFee());
						lp.setGstCode(obj.getGstCode());
						lp.setStatus(obj.getStatus());
						llpPaymentFeeService.updateWithParameter(lp, obj.paymentDesc);
					}
					
				
					setResponsePage(LlpPaymentFeePage.class);
					
					//LlpPaymentFee lp = (LlpPaymentFee) getForm().getModelObject();

//					if (StringUtils.isBlank(lp.getPaymentCode())) {
//						((LlpPaymentFeeService)getService(LlpPaymentFeeService.class.getSimpleName())).insertWithParameter(lp, obj.paymentDesc);
//					} else {
//						//getService(LlpPaymentFeeService.class.getSimpleName()).update(lp);
//						((LlpPaymentFeeService)getService(LlpPaymentFeeService.class.getSimpleName())).updateWithParameter(lp, obj.paymentDesc);
//					}
					
				}
			});
			add(new Button("cancel") {
				public void onSubmit() {
					setResponsePage(LlpPaymentFeePage.class);
				}
			}.setDefaultFormProcessing(false));
			
		
		}
	}

}
