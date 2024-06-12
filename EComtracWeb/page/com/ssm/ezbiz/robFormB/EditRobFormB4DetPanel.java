package com.ssm.ezbiz.robFormB;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.robFormB.EditRobFormB4Panel.RobFormB4Form;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.page.BasePanel;
import com.ssm.llp.base.page.SecBasePanel;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.utils.WicketUtils;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMRadioChoice;
import com.ssm.llp.base.wicket.component.SSMTextArea;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormA;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB1;
import com.ssm.llp.ezbiz.model.RobFormB2Det;
import com.ssm.llp.ezbiz.model.RobFormB4;	

@SuppressWarnings("all")
public class EditRobFormB4DetPanel extends SecBasePanel {
	

	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;
	
	private RobFormB robFormB;
	
	public EditRobFormB4DetPanel(String panelId, RobFormB robFormB, final RobFormB4Form robFormB4Form) {
		super(panelId);
//		super(panelId , "page.lbl.ezbiz.robFormB4."+panelId);
		this.robFormB = robFormB;
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	return new RobFormB4();
            }
        }));
		add(new RobFormB4DetForm("robFormB4DetForm",getDefaultModel(), -1, robFormB4Form));
	}
	
	
	
	public EditRobFormB4DetPanel(String panelId, RobFormB robFormB,final RobFormB4 robFormB4,final int ownerIdx, final RobFormB4Form robFormB4Form) {
		super(panelId);
//		super(panelId , "page.lbl.ezbiz.robFormB4."+panelId);
		this.robFormB = robFormB;
		
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	return robFormB4;
            }
        }));
		RobFormB4DetForm robFormB4DetForm = new RobFormB4DetForm("robFormB4DetForm", getDefaultModel(), ownerIdx, robFormB4Form);
		robFormB4DetForm.setOutputMarkupId(true);
		robFormB4DetForm.setOutputMarkupPlaceholderTag(true);
		add(robFormB4DetForm);
		
	}

//	public List<RobFormB4> getListDataProviderRobFormB4Owners() {
//		WebMarkupContainer wmcOwners1 = (WebMarkupContainer) getParent().get("wmcOwners");
//		SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB4>)wmcOwners1.get("sortingRobFormB4Owners")).getDataProvider();
//		return dpProvider.getListResult();
//	}
	
//	public void resetViewListDataProviderRobFormB4Owners(List<RobFormB4> listB4Owner, AjaxRequestTarget target) {
//		WebMarkupContainer wmcOwners = (WebMarkupContainer) getParent().get("wmcOwners");
//		SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB4>)wmcOwners.get("sortingRobFormB4Owners")).getDataProvider();
//		dpProvider.resetView(listB4Owner);
//		target.add(wmcOwners);
//		
//		
//		
//		Date longestBackDate = null;
//		for (int i = 0; i < listB4Owner.size(); i++) {
//			RobFormB4 formB4 = listB4Owner.get(i);
//			if(!Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NO_CHANGES.equals(formB4.getAmmendmentType())) {
//				if(formB4.getAmmendmentDate()==null) {
//					target.prependJavaScript("Please update ammendment date for "+formB4.getName());
//					break;
//				}
//				if(longestBackDate==null) {
//					longestBackDate = formB4.getAmmendmentDate();
//				}else {
//					if(longestBackDate.after(formB4.getAmmendmentDate())) {
//						longestBackDate = formB4.getAmmendmentDate();
//					}
//				}
//			}
//		}
//		SSMLabel b4AmmendmendDtLabel = new SSMLabel("b4AmmendmendDt", longestBackDate, "dd MMM yyyy");
//		getParent().replace(b4AmmendmendDtLabel);
//		target.add(b4AmmendmendDtLabel);
//		
//		robFormB.setB4AmmendmendDt(longestBackDate);
//		
//		
//		RobFormB4Form b4Form = (RobFormB4Form) getParent();
//		b4Form.reCheckChanges(target);
//	}
	

//	public void setRobFormB4(final RobFormB4 robFormB4, int ownersIdx) {
////		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
////            protected Object load() {
////            	return robFormB4;
////            }
////        }));
////		RobFormB4DetForm aOwnersesForm =  new RobFormB4DetForm("robFormB4DetForm", getDefaultModel(), ownersIdx);
////		replace(aOwnersesForm);
////		RobFormB4DetForm form =  (RobFormB4DetForm) get("robFormB4DetForm");
////		form.ownersIdx=ownersIdx;
////		form.setModelObject(robFormB4);
////		replace(form);
////		.setDefaultModelObject(robFormB4);
//		
//	}
	
	private class RobFormB4DetForm extends Form  {
		private int ownersIdx = -1;
		final boolean isQuery;
		public SSMAjaxButton updateOwners;
		public RobFormB4Form b4ListingForm;
		final RepeatingView listError;
		final WebMarkupContainer wmcListError ;
		final RobFormB4 ownersSnapshot;
		
		public RobFormB4DetForm(String id, IModel m , int ownIdx, RobFormB4Form robFormB4Form) {
			super(id, m);
			setMultiPart(true);
			b4ListingForm = robFormB4Form;
			final String lodgerLoginName = robFormB.getCreateBy();
			
			this.ownersIdx=ownIdx;
			setAutoCompleteForm(false);
			setPrefixLabelKey("page.lbl.ezbiz.robFormB4.");
			final RobFormB4 owners = (RobFormB4) m.getObject();
			ownersSnapshot = (RobFormB4) SerializationUtils.clone(owners);
			
			if(Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
				isQuery = true;
			}else{
				isQuery = false;
			}
			
//			System.out.println("owners:"+owners.getName());
			add(new SSMLabel("name",owners.getName()));
			add(new SSMLabel("newicno",owners.getNewicno()));
			add(new SSMLabel("dob",owners.getDob(),"dd MMM yyyy"));
			add(new SSMLabel("gender",owners.getGender(), Parameter.GENDER));
			add(new SSMLabel("race",owners.getRace(), Parameter.RACE));
			add(new SSMLabel("otherrace",owners.getOtherrace()));
			add(new SSMLabel("nationality",owners.getNationality(), Parameter.NATIONALITY_TYPE_FOR_CMP_OFFICER));
			add(new SSMLabel("countryoforiginifpr",owners.getCountryoforiginifpr(), Parameter.COUNTRY_CODE));
			
			add(new SSMTextField("addr"));
			
			SSMTextField addr2 = new SSMTextField("addr2");
			addr2.setNoLabel();
			add(addr2);
			
			SSMTextField addr3 = new SSMTextField("addr3");
			addr3.setNoLabel();
			add(addr3);
			
			SSMTextField addrPostcode = new SSMTextField("addrPostcode");
			add(addrPostcode);
			
			
			WicketUtils.generatePostcodeTownState(this, addrPostcode, owners, "addrPostcode", "addrTown","addrState" , false );

			
			SSMTextField telNo = new SSMTextField("telNo");
			add(telNo);
			
			final SSMTextField mobileNo = new SSMTextField("mobileNo");
			add(mobileNo);
			
			
			setOutputMarkupId(true);
			
			
			final SSMDateTextField ammendmentDate = new SSMDateTextField("ammendmentDate");
			add(ammendmentDate);
			
			SSMLabel ammendmentDateStr = new SSMLabel("ammendmentDateStr",owners.getAmmendmentDate(),"dd MMM yyyy");
			ammendmentDateStr.setVisible(false);
			add(ammendmentDateStr);
			
			
			final String addOwnersJS = "addOwnersJS";
			//Arrays.asList will return fixarray so cannot add
			List<String> ownersesAddOwnersField = new ArrayList<String>(Arrays.asList(new String[]{"addr","addrTown","addrPostcode","addrTownTmp","addrStateDesc","addrUrl","ammendmentDate","mobileNo"}));
			List<String> ownersesAddOwnersFieldRules = new ArrayList<String>(Arrays.asList(new String[]{"empty","empty","exactLengthNumber[5]","empty","empty","isNotReqUrl","empty","minLengthNumber[10]"}));
			
			
			final FileUploadField fileUpload = new FileUploadField("fileUploadTmp", new PropertyModel(" ", ""));
			fileUpload.setVisible(false);
			add(fileUpload);
			
			
			final SSMDropDownChoice ammendmentType = new SSMDropDownChoice("ammendmentType", Parameter.ROB_FORM_B4_AMMENDMENT_TYPE);
//			final SSMRadioChoice ammendmentType = new SSMRadioChoice("ammendmentType", Parameter.ROB_FORM_B4_AMMENDMENT_TYPE);
			ammendmentType.setVisible(false);
			add(ammendmentType);
			
			
			if(owners.getCbsOwnerId()!=null && !lodgerLoginName.equals(owners.getEzbizLoginName()) ) {//mean existing Owner not lodger
				ammendmentType.setVisible(true);
				ownersesAddOwnersField.add("ammendmentType"); 
				ownersesAddOwnersFieldRules.add("empty");
				
			}
			
			SSMLabel ammendmentTypeStr = new SSMLabel("ammendmentTypeStr",owners.getAmmendmentType(),Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_ALL);
			ammendmentTypeStr.setVisible(false);
			add(ammendmentTypeStr);

			if(isQuery) {
				
				ammendmentDate.setVisible(false);
				ammendmentDateStr.setVisible(true);
				
				if(ammendmentType.isVisible()) {
					ammendmentType.setVisible(false);
					ammendmentTypeStr.setVisible(true);
				}
				
			}
			fileUpload.setVisible(ammendmentType.isVisible());
			
			setSemanticJSValidation(this, addOwnersJS, ownersesAddOwnersField,ownersesAddOwnersFieldRules);
			
			SSMAjaxLink cancelButton = new SSMAjaxLink("cancel") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					Component b4Form = getParent().getParent().getParent();
					
					Component b4DetPanel = b4Form.get("editRobFormB4DetPanel");
					b4DetPanel.setVisible(false);
					target.add(b4Form);
					
					
					Component wmcNewOwnerB4 = b4Form.get("wmcNewOwnerB4");
					wmcNewOwnerB4.setVisible(true);
					target.add(wmcNewOwnerB4);
				}
			};
//			SSMAjaxButton cancelButton = new SSMAjaxButton("cancel", addOwnersJS) {
//				@Override
//				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//					RobFormB4 ownersFromForm  = (RobFormB4) form.getDefaultModelObject();
//					
//					Component b4Form = getParent().getParent().getParent();
//					
//					Component b4DetPanel = b4Form.get("editRobFormB4DetPanel");
//					b4DetPanel.setVisible(false);
//					target.add(b4Form);
//					
//					
//					Component wmcNewOwnerB4 = b4Form.get("wmcNewOwnerB4");
//					wmcNewOwnerB4.setVisible(true);
//					target.add(wmcNewOwnerB4);
//				}
//			};
			add(cancelButton);
			
			wmcListError = new WebMarkupContainer("wmcListError");
			wmcListError.setOutputMarkupId(true);
			wmcListError.setPrefixLabelKey("page.lbl.ezbiz.robFormB4Summary.");
			add(wmcListError);
			
			listError = new RepeatingView("listError");
			listError.setOutputMarkupId(true);
			listError.setVisible(false);
			wmcListError.add(listError);
			
			updateOwners = new SSMAjaxButton("updateOwners", addOwnersJS) {
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					RobFormB4 ownersFromForm  = (RobFormB4) form.getDefaultModelObject();
					
					listError.removeAll();
					
					if(ownersFromForm.getAmmendmentDate()!=null) {
						System.out.println("Date:"+robFormB.getBizProfileDetResp().getMainInfo().getBizStartDate());
						if(ownersFromForm.getAmmendmentDate().after(new Date())){
							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB4.ammendDateCannotFutureDate")));
						}
						if(ownersFromForm.getAmmendmentDate().before(robFormB.getBizProfileDetResp().getMainInfo().getBizStartDate())){
							listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB4.ammendDateCannotB4RegDate")));
						}
					}
					
					if(ammendmentType.isVisible() && ownersFromForm.getAmmendmentType()==null) {//mean existing Owner not lodger
						listError.add(new SSMLabel(listError.newChildId() ,resolve("page.lbl.ezbiz.robFormB4.fillInAmendmentType")));
					}
					
					LlpFileData supportingDoc = null;
					if(ammendmentType.isVisible() && Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_DECEASED.equals(ownersFromForm.getAmmendmentType()) ) {
						
						supportingDoc = validateAndGenerateFileData(fileUpload, listError);
						if(supportingDoc!=null) {
							llpFileDataService.insert(supportingDoc);
						}
					}
					
					
					
					
					if(listError.size()>0) {
						listError.setVisible(true);
						ownersFromForm.setAddr(ownersSnapshot.getAddr());
						ownersFromForm.setAddr2(ownersSnapshot.getAddr2());
						ownersFromForm.setAddr3(ownersSnapshot.getAddr3());
						ownersFromForm.setAddrPostcode(ownersSnapshot.getAddrPostcode());
						ownersFromForm.setAddrTown(ownersSnapshot.getAddrTown());
						ownersFromForm.setAddrState(ownersSnapshot.getAddrState());
						ownersFromForm.setTelNo(ownersSnapshot.getTelNo());
						ownersFromForm.setMobileNo(ownersSnapshot.getMobileNo());
						
						ownersFromForm.setAmmendmentDate(ownersSnapshot.getAmmendmentDate());
						ownersFromForm.setAmmendmentType(ownersSnapshot.getAmmendmentType());
						
						target.add(wmcListError);
						return;
					}else {
						listError.setVisible(false);
						target.add(wmcListError);
						
						List<RobFormB4> list = b4ListingForm.getListDataProviderRobFormB4Owners();
						
						String town="",state=""; 
						
						SSMDropDownChoice townDD = (SSMDropDownChoice) form.get("addrTownTmp");
						String postcodeTownState = townDD.getDefaultModelObject().toString();
						if(StringUtils.isNotBlank(postcodeTownState)&&postcodeTownState.indexOf(":")>2){
							town = StringUtils.split(postcodeTownState,":")[1];
							state = StringUtils.split(postcodeTownState,":")[2];
						}
						
						String errorJs="";
						
						if(ownersIdx==-1){//mean new owners
							RobFormB4 newOwnerses = ownersFromForm;
							newOwnerses.setAddr(ownersFromForm.getAddr());
							newOwnerses.setAddr2(ownersFromForm.getAddr2());
							newOwnerses.setAddr3(ownersFromForm.getAddr3());
							newOwnerses.setAddrPostcode(ownersFromForm.getAddrPostcode());
							newOwnerses.setAddrTown(town);
							newOwnerses.setAddrState(state);
							newOwnerses.setTelNo(ownersFromForm.getTelNo());
							newOwnerses.setMobileNo(ownersFromForm.getMobileNo());
							
							newOwnerses.setAmmendmentDate(ownersFromForm.getAmmendmentDate());
							newOwnerses.setAmmendmentType(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NEW);
							list.add(newOwnerses);
							
						}else{
							RobFormB4 selectedOwners = list.get(ownersIdx);
							selectedOwners.setAddr(ownersFromForm.getAddr());
							selectedOwners.setAddr2(ownersFromForm.getAddr2());
							selectedOwners.setAddr3(ownersFromForm.getAddr3());
							selectedOwners.setAddrPostcode(ownersFromForm.getAddrPostcode());
							selectedOwners.setAddrTown(town);
							selectedOwners.setAddrState(state);
							
							selectedOwners.setTelNo(ownersFromForm.getTelNo());
							selectedOwners.setMobileNo(ownersFromForm.getMobileNo());
							
							selectedOwners.setAmmendmentDate(ownersFromForm.getAmmendmentDate());
							if(selectedOwners.getCbsOwnerId()!=null) {//mean existing owner
								 if(lodgerLoginName.equals(owners.getEzbizLoginName())){//Lodger
									 selectedOwners.setAmmendmentType(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_EDITED);
								 }else {
									 selectedOwners.setAmmendmentType(ownersFromForm.getAmmendmentType());
								 }
								
							}
							if(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_DECEASED.equals(ownersFromForm.getAmmendmentType()) && supportingDoc!=null) {
								selectedOwners.setSupportingDocData(supportingDoc);
							}
							
							list.set(ownersIdx, selectedOwners);
						}

						b4ListingForm.resetViewListDataProviderRobFormB4Owners(list, target);
						
						
						
						Component b4Form = getParent().getParent().getParent();
						
						Component b4DetPanel = b4Form.get("editRobFormB4DetPanel");
						b4DetPanel.setVisible(false);
						target.add(b4Form);
						
						
						Component wmcNewOwnerB4 = b4Form.get("wmcNewOwnerB4");
						
						if(Parameter.ROB_BIZ_TYPE_TRADE.equals(robFormB.getBizType())) {
							wmcNewOwnerB4.setVisible(true);
						}
						target.add(wmcNewOwnerB4);
					}
					
					
				}
			};
			add(updateOwners);
			if(isQuery&&owners.getRobFormB4Id()==0){
				updateOwners.setVisible(false);
			}else{
				updateOwners.setVisible(true);
			}
			
			
			if(ownIdx==-1) {
				updateOwners.setLabelKey(getPrefixLabelKey()+"add");
			}
			
		}

	}
}
