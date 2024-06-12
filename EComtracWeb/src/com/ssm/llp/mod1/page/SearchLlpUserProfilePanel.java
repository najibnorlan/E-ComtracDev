package com.ssm.llp.mod1.page;

import java.io.Serializable;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;

@SuppressWarnings("all")
public class SearchLlpUserProfilePanel extends SecBasePanel {

	public SearchLlpUserProfilePanel(String panelId) {
		super(panelId);
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	
            	SearchLlpUserProfileModel obj = (SearchLlpUserProfileModel) getDefaultModel().getObject();
            	if(obj != null){
            		return obj;
            	}
            	
            	return new SearchLlpUserProfileModel();
            }
        }));
		add(new SearchForm("searchLlpUserProfileForm",getDefaultModel()));
	}

    public SearchLlpUserProfilePanel(String id,final String userRefNo, final String idType,final String idNo, final String name,final String userStatus, final Date dtFrom, final Date dtTo, final Date dtApproveTo, final Date dtApproveFrom, final String approveChannel, final String approveBy, final String email)
    {
    	super(id);
    	setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	SearchLlpUserProfileModel searchLlpUserProfileModel = new SearchLlpUserProfileModel();
            	searchLlpUserProfileModel.setIdType(idType);
            	searchLlpUserProfileModel.setIdNo(idNo);
            	searchLlpUserProfileModel.setName(name);
            	searchLlpUserProfileModel.setUserRefNo(userRefNo);
            	searchLlpUserProfileModel.setUserStatus(userStatus);
            	searchLlpUserProfileModel.setDtTo(dtTo);
            	searchLlpUserProfileModel.setDtFrom(dtFrom);
            	searchLlpUserProfileModel.setDtApproveTo(dtApproveTo);
            	searchLlpUserProfileModel.setDtApproveFrom(dtApproveFrom);
            	searchLlpUserProfileModel.setApproveChannel(approveChannel);
            	searchLlpUserProfileModel.setApproveBy(approveBy);
            	searchLlpUserProfileModel.setEmail(email);
            	return searchLlpUserProfileModel;
            }
        }));
    	add(new SearchForm("searchLlpUserProfileForm",getDefaultModel()));
    }
    

	public final class SearchLlpUserProfileModel implements Serializable {
		private String idType;
		private String idNo;
		private String name;
		private String userStatus;
		private String userRefNo;
		private Date dtFrom;
		private Date dtTo;
		private Date dtApproveTo;
		private Date dtApproveFrom;		
		private String approveChannel;
		private String approveBy;
		private String email;
		
		
		public SearchLlpUserProfileModel(String idType, String idNo,
				String name, String userStatus, String userRefNo, Date dtFrom,
				Date dtTo, Date dtApproveTo, Date dtApproveFrom, String approveChannel) {
			super();
			this.idType = idType;
			this.idNo = idNo;
			this.name = name;
			this.userStatus = userStatus;
			this.userRefNo = userRefNo;
			this.dtFrom = dtFrom;
			this.dtTo = dtTo;
			this.dtApproveFrom = dtApproveFrom;
			this.dtApproveTo = dtApproveTo;		
			this.approveChannel = approveChannel;
		}


		public SearchLlpUserProfileModel() {
		}
		
		public String getUserRefNo() {
			return userRefNo;
		}

		public void setUserRefNo(String userRefNo) {
			this.userRefNo = userRefNo;
		}

		public String getUserStatus() {
			return userStatus;
		}

		public void setUserStatus(String userStatus) {
			this.userStatus = userStatus;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIdType() {
			return idType;
		}

		public void setIdType(String idType) {
			this.idType = idType;
		}

		public String getIdNo() {
			return idNo;
		}

		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}
		public Date getDtFrom() {
			return dtFrom;
		}
		public void setDtFrom(Date dtFrom) {
			this.dtFrom = dtFrom;
		}
		public Date getDtTo() {
			return dtTo;
		}
		public void setDtTo(Date dtTo) {
			this.dtTo = dtTo;
		}
		
		public Date getDtApproveTo() {
			return dtApproveTo;
		}
		public void setDtApproveTo(Date dtApproveTo) {
			this.dtApproveTo = dtApproveTo;
		}
		public Date getDtApproveFrom() {
			return dtApproveFrom;
		}
		public void setDtApproveFrom(Date dtApproveFrom) {
			this.dtApproveFrom = dtApproveFrom;
		}
		
		public String getApproveChannel() {
			return approveChannel;
		}
		public void setApproveChannel(String approveChannel) {
			this.approveChannel = approveChannel;
		}


		public String getApproveBy() {
			return approveBy;
		}


		public void setApproveBy(String approveBy) {
			this.approveBy = approveBy;
		}


		public String getEmail() {
			return email;
		}


		public void setEmail(String email) {
			this.email = email;
		}
	}

	private class SearchForm extends Form  {

		public SearchForm(String id, IModel m) {
			super(id, m);

			SSMTextField userRefNo = new SSMTextField("userRefNo");
			add(userRefNo);

			SSMDropDownChoice idType = new SSMDropDownChoice("idType", Parameter.ID_TYPE);
			add(idType);

			SSMTextField idNo = new SSMTextField("idNo");
			add(idNo);

			SSMTextField name = new SSMTextField("name");
			add(name);
			
			SSMTextField approveBy = new SSMTextField("approveBy");
			approveBy.setUpperCase(false);
			add(approveBy);

			SSMDropDownChoice approveChannel= new SSMDropDownChoice("approveChannel", Parameter.USER_APPROVED_CHANNEL);
			add(approveChannel);
			
			SSMDateTextField dtFrom = new SSMDateTextField("dtFrom");
			add(dtFrom);
			
			SSMDateTextField dtTo = new SSMDateTextField("dtTo");
			add(dtTo);
			
			SSMDateTextField dtApproveTo = new SSMDateTextField("dtApproveTo");
			add(dtApproveTo);
			
			SSMDateTextField dtApproveFrom = new SSMDateTextField("dtApproveFrom");
			add(dtApproveFrom);
			
			SSMRadioChoice userStatus = new SSMRadioChoice("userStatus", Parameter.USER_STATUS);
			add(userStatus);
			
			SSMTextField email = new SSMTextField("email");
			email.setUpperCase(false);
			add(email);

			// tf.setRequired(true);
			setMarkupId("search-form");
			
			SSMAjaxButton search = new SSMAjaxButton("search") {
				public void onSubmit(AjaxRequestTarget target, Form form) {
					SearchLlpUserProfileModel model = (SearchLlpUserProfileModel) form.getModelObject();
					setResponsePage(new ListLlpUserProfilePage(model.getUserRefNo(), model.getIdType(), model.getIdNo(), model.getName(),
								model.getUserStatus(), model.getDtFrom(), model.getDtTo(), model.getDtApproveTo(), model.getDtApproveFrom(), model.getApproveChannel(), model.getApproveBy(),  model.getEmail()));
				}
			};
			add(search);
		}

		

	}

}