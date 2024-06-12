package com.ssm.common.mobile;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.mod1.page.ListLlpUserProfilePage;

@SuppressWarnings("unchecked")
public class ESearchMobilePanel extends SecBasePanel {

	
	public ESearchMobilePanel(String panelId) {
		super(panelId);
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	
            	ESearchMobileModel obj = (ESearchMobileModel) getDefaultModel().getObject();
            	if(obj != null){
            		return obj;
            	}
            	ESearchMobileModel searchMobileModel =  new ESearchMobileModel();
            	searchMobileModel.setEntityType(Parameter.ENTITY_TYPE_ROB);
            	return new ESearchMobileModel();
            }
        }));
		add(new SearchForm("eSearchMobilePanelForm",getDefaultModel()));
	}

    public ESearchMobilePanel(String id, final String entityType,final String entityNo )
    {
    	super(id);
    	setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	ESearchMobileModel searchMobileModel = new ESearchMobileModel();
            	searchMobileModel.setEntityType(entityType);
            	searchMobileModel.setEntityNo(entityNo);
            	return searchMobileModel;
            }
        }));
    	add(new SearchForm("eSearchMobilePanelForm",getDefaultModel()));
    }
    

	public final class ESearchMobileModel implements Serializable {
		private String entityType;
		private String entityNo;

		public ESearchMobileModel() {
		}
		
		public ESearchMobileModel(String entityType,String entityNo)  {
			this.entityType = entityType;
			this.entityNo = entityNo;
		}
		public String getEntityType() {
			return entityType;
		}
		public void setEntityType(String entityType) {
			this.entityType = entityType;
		}
		public String getEntityNo() {
			return entityNo;
		}
		public void setEntityNo(String entityNo) {
			this.entityNo = entityNo;
		}
		
	}

	private class SearchForm extends Form  {

		public SearchForm(String id, IModel m) {
			super(id, m);

			SSMDropDownChoice entityType = new SSMDropDownChoice("entityType", Parameter.ENTITY_TYPE);
			add(entityType);

			SSMTextField entityNo = new SSMTextField("entityNo");
			add(entityNo);

			// tf.setRequired(true);
			setMarkupId("search-form");
		}

		public void onSubmit() {
			ESearchMobileModel model = (ESearchMobileModel) getModelObject();
		//	if (model.getUserRefNo() != null || model.getIdType() != null || model.getIdNo() != null || model.getName() != null
		//			|| model.getUserStatus() != null) {
				setResponsePage(new ESearchMainPageMobile(model.getEntityType(), model.getEntityNo() ));
		//	} else {
				//error("Please choose atleast one search criteria.");
			//	return;
			//}

		}

	}

}