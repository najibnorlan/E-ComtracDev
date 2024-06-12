package com.ssm.ezbiz.robFormB;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.iterator.ComponentHierarchyIterator;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.hibernate.LazyInitializationException;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import com.ssm.ezbiz.robFormB.EditRobFormB4Panel.RobFormB4Form;
import com.ssm.ezbiz.service.RobFormBService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.model.LlpFileUpload;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.common.service.LlpFileUploadService;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.AlertPanel;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.wicket.SSMDownloadLink;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMDateTextField;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.base.wicket.component.SSMTextField;
import com.ssm.llp.ezbiz.model.RobFormB;
import com.ssm.llp.ezbiz.model.RobFormB4;
import com.ssm.llp.ezbiz.model.RobFormOwnerVerification;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;
import com.ssm.webis.param.RobFormOwnerInfo;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EditRobFormB4Panel extends RobFormBBasePanel{
	

	@SpringBean(name = "RobFormBService")
	private RobFormBService robFormBService;

	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;


	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;
	
	final RobFormB4Form robFormB4SummaryForm;
	public EditRobFormB4Panel(String panelId, final RobFormB robFormB) {
		super(panelId);
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
//            	RobFormB4 robFormB4 = new RobFormB4();
//            	robFormB4.setAmmendmentDate(robFormB.getB4AmmendmendDt());
            	return robFormB;
            }
        }));
		robFormB4SummaryForm = new RobFormB4Form("robFormB4SummaryForm", getDefaultModel(), robFormB);
		add(robFormB4SummaryForm);
	}
	
	@Override
	public void refreshPanel(AjaxRequestTarget target) {
		super.refreshPanel(target);
		robFormB4SummaryForm.reCheckChanges(target);
	}
	
	
	
	public class RobFormB4Form extends Form implements Serializable {
		final RobFormB robFormB;
		final boolean isQuery;
		final SSMAjaxButton b4Next;
		private String newIcNoForOwners;
		final WebMarkupContainer wmcOwners;
		
		public List<RobFormB4> getListDataProviderRobFormB4Owners() {
			WebMarkupContainer wmcOwners1 = (WebMarkupContainer) get("wmcOwners");
			SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB4>)wmcOwners1.get("sortingRobFormB4Owners")).getDataProvider();
			List<RobFormB4>  list = dpProvider.getListResult();
//			System.out.println("ListParent:"+list.get(0).getAddr());
			return list;
		}
		
		public void resetViewListDataProviderRobFormB4Owners(List<RobFormB4> listB4Owner, AjaxRequestTarget target) {
			WebMarkupContainer wmcOwners = (WebMarkupContainer) get("wmcOwners");
			SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB4>)wmcOwners.get("sortingRobFormB4Owners")).getDataProvider();
			dpProvider.resetView(listB4Owner);
			target.add(wmcOwners);
			
			
			reCalculateB4AmendmentDate(target);
			
			SSMLabel b4AmmendmendDtLabel = new SSMLabel("b4AmmendmendDt", robFormB.getB4AmmendmendDt(), "dd MMM yyyy");
			replace(b4AmmendmendDtLabel);
			target.add(b4AmmendmendDtLabel);
			
			
			reCheckChanges(target);
		}
		
		void reCalculateB4AmendmentDate(AjaxRequestTarget target){
			if(target==null) {
				return;
			}
			List<RobFormB4> listB4Owner = getListDataProviderRobFormB4Owners();
			Date longestBackDate = null;
			for (int i = 0; i < listB4Owner.size(); i++) {
				RobFormB4 formB4 = listB4Owner.get(i);
				if(!Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NO_CHANGES.equals(formB4.getAmmendmentType())) {
					if(formB4.getAmmendmentDate()==null) {
						target.prependJavaScript("Please update ammendment date for "+formB4.getName());
						break;
					}
					if(longestBackDate==null) {
						longestBackDate = formB4.getAmmendmentDate();
					}else {
						if(longestBackDate.after(formB4.getAmmendmentDate())) {
							longestBackDate = formB4.getAmmendmentDate();
						}
					}
				}
			}
			if(longestBackDate!=null) {
				robFormB.setB4AmmendmendDt(longestBackDate);
			}
		}
		
		public RobFormB4Form(String id, IModel m, final RobFormB robFormB) {
			super(id, m);
			
			final RobFormB4Form thisForm = this;
			setPrefixLabelKey("page.lbl.ezbiz.robFormB4.");
			
			if(Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
				isQuery = true;
			}else{
				isQuery = false;
			}
			
			this.robFormB = robFormB;
			
			wmcOwners = new WebMarkupContainer("wmcOwners");
			wmcOwners.setOutputMarkupId(true);
			wmcOwners.setOutputMarkupPlaceholderTag(true);
			add(wmcOwners);
			
			
			EditRobFormB4WithdrawPanel editRobFormB4WithdrawPanel = new EditRobFormB4WithdrawPanel("editRobFormB4WithdrawPanel");
			editRobFormB4WithdrawPanel.setPrefixLabelKey(getPrefixLabelKey());
			editRobFormB4WithdrawPanel.setOutputMarkupId(true);
			editRobFormB4WithdrawPanel.setOutputMarkupPlaceholderTag(true);
			add(editRobFormB4WithdrawPanel);
			
			final EditRobFormB4DetPanel editRobFormB4DetPanel = new EditRobFormB4DetPanel("editRobFormB4DetPanel", robFormB, thisForm);
			editRobFormB4DetPanel.setPrefixLabelKey(getPrefixLabelKey());
			editRobFormB4DetPanel.setOutputMarkupId(true);
			editRobFormB4DetPanel.setOutputMarkupPlaceholderTag(true);
			editRobFormB4DetPanel.setVisible(false);
			add(editRobFormB4DetPanel);
			

			final WebMarkupContainer wmcNewOwnerB4 = new WebMarkupContainer("wmcNewOwnerB4");
			wmcNewOwnerB4.setPrefixLabelKey(getPrefixLabelKey());
			wmcNewOwnerB4.setOutputMarkupId(true);
			wmcNewOwnerB4.setOutputMarkupPlaceholderTag(true);
			wmcNewOwnerB4.setVisible(false);
			add(wmcNewOwnerB4);
			
			
			final SSMTextField newIcNoForOwners = new SSMTextField("newIcNoForOwners",new PropertyModel(this, "newIcNoForOwners"));
			wmcNewOwnerB4.add(newIcNoForOwners);

			final SSMLabel newIcNoForOwnersError = new SSMLabel("newIcNoForOwnersError","");
			newIcNoForOwnersError.setEscapeModelStrings(true);
			newIcNoForOwnersError.setVisible(false);
			wmcNewOwnerB4.add(newIcNoForOwnersError);
			
			
			

//			final WebMarkupContainer b4AmmendmendDtPanel = new WebMarkupContainer("b4AmmendmendDtPanel");
//			b4AmmendmendDtPanel.setOutputMarkupId(true);
//			b4AmmendmendDtPanel.setPrefixLabelKey("page.lbl.ezbiz.robFormB4.");
//			b4AmmendmendDtPanel.setOutputMarkupPlaceholderTag(true);
//			add(b4AmmendmendDtPanel);
			
//			final SSMDateTextField b4AmmendmendDt = new SSMDateTextField("b4AmmendmendDt");
//			b4AmmendmendDtPanel.add(b4AmmendmendDt);
//			if(!robFormB.getIsB4()){
//				b4AmmendmendDtPanel.setVisible(false);
//			}
			
//			b4AmmendmendDtPanel.setVisible(robFormB.getIsB4());
			if(Parameter.ROB_BIZ_TYPE_TRADE.equals(robFormB.getBizType()) && !isQuery ) {
				wmcNewOwnerB4.setVisible(robFormB.getIsB4());
			}
			
			
			
			final SSMLabel b4AmmendmendDtLabel = new SSMLabel("b4AmmendmendDt",robFormB.getB4AmmendmendDt(), "dd MMM yyyy");
			b4AmmendmendDtLabel.setOutputMarkupId(true);
			b4AmmendmendDtLabel.setOutputMarkupPlaceholderTag(true);
			add(b4AmmendmendDtLabel);
			
			
			
			final List listOwnerses = robFormB.getListRobFormB4();
			final SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider("", listOwnerses);
			final SSMDataView<RobFormB4> dataView = new SSMDataView<RobFormB4>("sortingRobFormB4Owners", dp) {

				protected void populateItem(final Item<RobFormB4> item) {
					final RobFormB4 robFormB4Owners = item.getModelObject();

					item.add(new SSMLabel("ownersNo", item.getIndex()+1));
					
					String ownerAddr = robFormB4Owners.getAddr();
					if (StringUtils.isNotBlank(robFormB4Owners.getAddr2())) {
						ownerAddr +=  "\n"+ robFormB4Owners.getAddr2();
					}
					if (StringUtils.isNotBlank(robFormB4Owners.getAddr3())) {
						ownerAddr += "\n"+robFormB4Owners.getAddr3() ;
					}

					ownerAddr += "\n" + robFormB4Owners.getAddrPostcode() + " " + robFormB4Owners.getAddrTown().toUpperCase();
					ownerAddr += "\n" + getCodeTypeWithValue(Parameter.CBS_ROB_STATE, robFormB4Owners.getAddrState());
					
					
					item.add(new SSMLabel("name", robFormB4Owners.getName()));
					item.add(new SSMLabel("idNo", robFormB4Owners.getNewicno()));
					item.add(new MultiLineLabel("ownersAddress", ownerAddr));
					
					
					item.add(new SSMLabel("ammendmentType", robFormB4Owners.getAmmendmentType(), Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_ALL));
					item.add(new SSMLabel("ammendmentDate", robFormB4Owners.getAmmendmentDate(), "dd MMM yyyy"));
					
					AjaxLink editOwners = new AjaxLink("editOwners", item.getDefaultModel()) {
						public void onClick(AjaxRequestTarget target) {
							List<RobFormB4> list = getListDataProviderRobFormB4Owners();
							RobFormB4 robFormB4Tmp = list.get(item.getIndex());
							
							EditRobFormB4DetPanel editRobFormB4DetPanel2 = new EditRobFormB4DetPanel("editRobFormB4DetPanel", robFormB ,robFormB4Tmp, item.getIndex(), thisForm);
							editRobFormB4DetPanel2.setPrefixLabelKey(getPrefixLabelKey());
							editRobFormB4DetPanel2.setOutputMarkupId(true);
							editRobFormB4DetPanel2.setOutputMarkupPlaceholderTag(true);
							
							
							Form parentForm = findParent(Form.class);
							parentForm.addOrReplace(editRobFormB4DetPanel2);
							editRobFormB4DetPanel2.setVisible(true);
							target.add(editRobFormB4DetPanel2);
//							editRobFormB4DetPanel2.showPanel(target);
							
							newIcNoForOwnersError.setDefaultModelObject("");
							newIcNoForOwners.setModelObject("");
							target.add(newIcNoForOwners);
							wmcNewOwnerB4.setVisible(false);
							target.add(wmcNewOwnerB4);
							
						}
					};
					item.add(editOwners);
					
					SSMAjaxLink undoOwners = new SSMAjaxLink("undoOwners", item.getDefaultModel(), true) {
						public void onClick(AjaxRequestTarget target) {
							List<RobFormB4> listB4Owner = getListDataProviderRobFormB4Owners();
							RobFormB4 robFormB4 = listB4Owner.get(item.getIndex());
//							
							
							RobFormOwnerInfo[] originalOwners = robFormB.getBizProfileDetResp().getActiveOwnerInfo();
							for (int i = 0; i < originalOwners.length; i++) {
								if(originalOwners[i].getOwnerId()==robFormB4.getCbsOwnerId()) {
									//OwnerB4
									robFormB4.setCbsOwnerId(originalOwners[i].getOwnerId());
									robFormB4.setAmmendmentType(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NO_CHANGES);
									robFormB4.setAmmendmentDate(null);
									robFormB4.setNewicno(originalOwners[i].getIcNo());
									robFormB4.setName(originalOwners[i].getName());
									robFormB4.setDob(originalOwners[i].getDob());
									robFormB4.setNationality(originalOwners[i].getNationality());
									robFormB4.setCountryoforiginifpr(originalOwners[i].getCountryoforiginifpr());
									robFormB4.setGender(originalOwners[i].getGender());
									robFormB4.setRace(originalOwners[i].getRace());
									robFormB4.setOtherrace(originalOwners[i].getOtherRace());
									robFormB4.setTelNo(originalOwners[i].getTelNo());
									robFormB4.setMobileNo(originalOwners[i].getMobileNo());
									robFormB4.setEmail(originalOwners[i].getEmail());
									robFormB4.setOwnershiptype(originalOwners[i].getOwnershipType());
									robFormB4.setAddr(originalOwners[i].getAddr());
									robFormB4.setAddr2(originalOwners[i].getAddr2());
									robFormB4.setAddr3(originalOwners[i].getAddr3());
									robFormB4.setAddrTown(originalOwners[i].getAddrTown());
									robFormB4.setAddrPostcode(originalOwners[i].getAddrPostcode());
									robFormB4.setAddrState(originalOwners[i].getAddrState());
									break;
								}
							}
							
							
							resetViewListDataProviderRobFormB4Owners(listB4Owner, target);
						}
					};
					undoOwners.setVisible(false);
					undoOwners.setConfirmQuestion(resolve("page.confirm.robFormB4.undo"));
					item.add(undoOwners);
					
					SSMAjaxLink deleteLink = new SSMAjaxLink("deleteOwners", item.getDefaultModel(), true) {
							public void onClick(AjaxRequestTarget target) {
								
								SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB4>)wmcOwners.get("sortingRobFormB4Owners")).getDataProvider();
								List<RobFormB4> list = dpProvider.getListResult();
								RobFormB4 robFormB4Tmp = list.get(item.getIndex());
								list.remove(robFormB4Tmp);
								dpProvider.resetView(list);
								target.add(wmcOwners);
							}
					};
					deleteLink.setConfirmQuestion(resolve("page.confirm.robFormB4.deleteOwners"));
					item.add(deleteLink);
					
					if(robFormB4Owners.getCbsOwnerId()!=null){
						deleteLink.setVisible(false);
					}
					
					SSMLink downloadSupportingDoc = new SSMLink("downloadSupportingDoc") {
						@Override
						public void onClick() {
							try {
								robFormB4Owners.getSupportingDocData().getFileData();
							} catch (LazyInitializationException e) {
								LlpFileData llpFileData = llpFileDataService.findById(robFormB4Owners.getSupportingDocData().getFileDataId());
								robFormB4Owners.setSupportingDocData(llpFileData);
							}
							
							final byte byteData[] = robFormB4Owners.getSupportingDocData().getFileData();
							IResourceStream resourceStream = new AbstractResourceStreamWriter() {
							      @Override 
							      public void write(OutputStream output) {
							    	  try {
										output.write(byteData);
									} catch (IOException e) {
										e.printStackTrace();
									}
							      }

							      @Override
							      public String getContentType() {                        
							        return "pdf";
							      }
							};
							
							
							ResourceStreamRequestHandler rs = new ResourceStreamRequestHandler(resourceStream).setFileName("12supp_doc.pdf");
							rs.setCacheDuration(Duration.NONE);
							getRequestCycle().scheduleRequestHandlerAfterCurrent(rs);
						}
					};
					downloadSupportingDoc.setVisible(false);
					item.add(downloadSupportingDoc);
					
					
					
					
					if(robFormB4Owners.getCbsOwnerId()!=null){
						if(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NO_CHANGES.equals(robFormB4Owners.getAmmendmentType())){
							undoOwners.setVisible(false);
						}else {
							undoOwners.setVisible(true);
						}
					}
					
					if(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_DECEASED.equals(robFormB4Owners.getAmmendmentType())){
						downloadSupportingDoc.setVisible(true);
					}
					
					
					if(isQuery){
						deleteLink.setVisible(false);
						undoOwners.setVisible(false);
					}
					
					
					
					if(!robFormB.getIsB4()){
						editOwners.setVisible(false);
						deleteLink.setVisible(false);
						undoOwners.setVisible(false);
					}
					
					item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject() {
							return (item.getIndex() % 2 == 1) ? "even" : "odd";
						}
					}));
				}

			};

			wmcOwners.add(dataView);
			wmcOwners.add(new SSMPagingNavigator("navigatorRobFormB4", dataView));
			wmcOwners.add(new NavigatorLabel("navigatorLabelRobFormB4", dataView));
			
			SSMAjaxCheckBox isB4 = new SSMAjaxCheckBox("isB4", new PropertyModel(robFormB, "isB4") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if (String.valueOf(true).equals(getValue())) {
						robFormB.setIsB4(true);
					}else{
						robFormB.setIsB4(false);
					}
//					b4AmmendmendDtPanel.setVisible(robFormB.getIsB4());
					if(Parameter.ROB_BIZ_TYPE_TRADE.equals(robFormB.getBizType())) {
						wmcNewOwnerB4.setVisible(robFormB.getIsB4());
					}
					
					
//					target.add(b4AmmendmendDtPanel);
					target.add(wmcNewOwnerB4);
					
					
//					if(!robFormB.getIsB4()) {//mean untick
						SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB4>)wmcOwners.get("sortingRobFormB4Owners")).getDataProvider();
						List<RobFormB4> list = dpProvider.getListResult();
						List listToRemove = new ArrayList();
						for (int i = 0; i < list.size(); i++) {
							RobFormB4 robFormB4Tmp = list.get(i);
							if(robFormB4Tmp.getAmmendmentType().equals(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NEW)){
								listToRemove.add(robFormB4Tmp);
							}else{
								robFormB4Tmp.setAmmendmentType(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NO_CHANGES);
								robFormB4Tmp.setAmmendmentDate(null);
							}
						}
						list.removeAll(listToRemove);
						dpProvider.resetView(list);
						target.add(wmcOwners);
						robFormB.setB4AmmendmendDt(null);
//					}
					
					reCheckChanges(target);
				}
				@Override
			    protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
			    {
			        super.updateAjaxAttributes( attributes );
			        String confirmTitle = resolve("page.lbl.ezbiz.robFormB4.confirmResetTitle");
			        String confirmDesc = resolve("page.lbl.ezbiz.robFormB4.confirmResetDesc");		
			        AjaxCallListener ajaxCallListener = generateAjaxConfirm(this,confirmTitle,confirmDesc,true);
			        attributes.getAjaxCallListeners().add( ajaxCallListener );
			        
			    }
			};
			add(isB4);
			isB4.setEnabled(false);
			
			
			final String showOwnerValidationJS = "showOwnerValidation";
			String showOwnerValidationField[] = new String[]{"newIcNoForOwners"};
			String showOwnerValidationFieldRules[] = new String[]{"empty"};
			setSemanticJSValidation(this, showOwnerValidationJS, showOwnerValidationField,showOwnerValidationFieldRules);
			
			SSMAjaxButton showOwnersPanel = new SSMAjaxButton("showOwnersPanel", showOwnerValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					String newIc =  newIcNoForOwners.getInput();
					SSMSessionSortableDataProvider dpProvider = (SSMSessionSortableDataProvider) ((SSMDataView<RobFormB>)wmcOwners.get("sortingRobFormB4Owners")).getDataProvider();
					List<RobFormB4> list = dpProvider.getListResult();
					
					if(list.size() > 20){
						String ownerMore20 = resolve("page.lbl.ezbiz.robFormA.partnerCannotMore20");
						newIcNoForOwnersError.setDefaultModelObject(ownerMore20);
						newIcNoForOwnersError.setVisible(true);
						target.add(wmcNewOwnerB4);
						return;
					}
					
					for (int i = 0; i < list.size(); i++) {
						if(newIc.equals(list.get(i).getNewicno())){
							String ownerAlreadyAddMsg = resolve("page.lbl.ezbiz.robFormA.robFormAOwners.ownerAlreadyAdd",newIc);
							newIcNoForOwnersError.setDefaultModelObject(ownerAlreadyAddMsg);
							newIcNoForOwnersError.setVisible(true);
							target.add(wmcNewOwnerB4);
							return;
						}
					}
					
					LlpUserProfile llpUserProfile =llpUserProfileService.findByIdTypeAndIdNo(Parameter.ID_TYPE_newic, newIc);
					if(llpUserProfile==null){
						String fillICMsg = resolve("page.lbl.ezbiz.robFormA.robFormAOwners.mustRegisterEzBiz",newIc);
						newIcNoForOwnersError.setDefaultModelObject(fillICMsg);
						newIcNoForOwnersError.setVisible(true);
						target.add(wmcNewOwnerB4);
						return;
					}else{
						
						if(Parameter.USER_STATUS_deceased.equals(llpUserProfile.getUserStatus())) {
							String fillICMsg = resolve("page.lbl.ezbiz.robFormA.robFormAOwners.userAlreadyDeceased",newIc);
							newIcNoForOwnersError.setDefaultModelObject(fillICMsg);
							newIcNoForOwnersError.setVisible(true);
							target.add(wmcNewOwnerB4);
							return;
						}
						
						final RobFormB4 robFormB4 = new RobFormB4();
						copyFromUserProfileToRobFormB4(llpUserProfile, robFormB4 );
						LocalDate birthdate = new LocalDate(robFormB4.getDob());
						Years age = Years.yearsBetween(birthdate, new LocalDate());
						if(age.getYears()<18){
							String ownerBelow18 = resolve("page.lbl.ezbiz.robFormA.robFormAOwners.ownerBelow18", robFormB4.getName());
							newIcNoForOwnersError.setDefaultModelObject(ownerBelow18);
							newIcNoForOwnersError.setVisible(true);
							target.add(wmcNewOwnerB4);
							return;
						}else{
							
							EditRobFormB4DetPanel editRobFormB4DetPanel2 = new EditRobFormB4DetPanel("editRobFormB4DetPanel", robFormB ,robFormB4, -1, thisForm);
							editRobFormB4DetPanel2.setPrefixLabelKey(getPrefixLabelKey());
							editRobFormB4DetPanel2.setOutputMarkupId(true);
							editRobFormB4DetPanel2.setOutputMarkupPlaceholderTag(true);
							
							Form parentForm = findParent(Form.class);
							parentForm.addOrReplace(editRobFormB4DetPanel2);
							editRobFormB4DetPanel2.setVisible(true);
							target.add(editRobFormB4DetPanel2);
							
							
							
							newIcNoForOwnersError.setDefaultModelObject("");
							newIcNoForOwners.setModelObject("");
							wmcNewOwnerB4.setVisible(false);
							target.add(wmcNewOwnerB4);
							
							
						}
					}
						
					
				}

				

			};
			showOwnersPanel.setDefaultFormProcessing(false);
			wmcNewOwnerB4.add(showOwnersPanel);
			
			
			
			
			if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())){
				isB4.setEnabled(true);
			}

			final String b4ValidationJS = "b4Validation";
			String b4FieldToValidate[] = new String[]{"b4AmmendmendDt"};
			String b4FieldToValidateRules[] = new String[]{"empty"};
			setSemanticJSValidation(this, "b4Validation", b4FieldToValidate, b4FieldToValidateRules);
			
			
			SSMAjaxButton b4Prev = new SSMAjaxButton("b4Prev", b4ValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					hideAndShowSegment(target, 2);//mean show segment 3 - B1
				}
			};
			add(b4Prev);
			
			b4Next = new SSMAjaxButton("b4Next", b4ValidationJS) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					List<RobFormB4> listRobFormB4 = getListDataProviderRobFormB4Owners();
					
					robFormB.setListRobFormB4(listRobFormB4);
					
					//RobFormB4 robFormB4Tmp = (RobFormB4) form.getDefaultModelObject();
					//robFormB.setB4AmmendmendDt(robFormB4Tmp.getAmmendmentDate());
					
					
					if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus()) || Parameter.ROB_FORM_B_STATUS_QUERY.equals(robFormB.getStatus())){
						robFormBService.insertUpdateAll(robFormB);
					}
					if(Parameter.ROB_FORM_B_STATUS_DATA_ENTRY.equals(robFormB.getStatus())){
						robFormBService.sendEmailToAllPartner(robFormB);
					}
					robFormB.setListRobFormB4(listRobFormB4);
					hideAndShowSegment(target, 4);//mean show segment 3 - B4
				}
				
				@Override
			    protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
			    {
			        super.updateAjaxAttributes( attributes );
			        List<RobFormOwnerVerification> listVer = robFormB.getListRobFormOwnerVerification();
			        boolean allVerify = true;
			        for (int i = 0; i < listVer.size(); i++) {
						if(!Parameter.ROB_OWNER_B_C_VERI_STATUS_VERIFIED.equals(listVer.get(i).getStatus())){
							allVerify = false;
							break;
						}
					}
			        if(!allVerify){
				        AjaxCallListener ajaxCallListener = new AjaxCallListener();
				        String text = resolve("page.confirm.robFormB4.sendNotificationEmailAlert");
				        ajaxCallListener.onPrecondition( "return alert('" + text + "');" );
				        attributes.getAjaxCallListeners().add( ajaxCallListener );
			        }
			    }
			};
			b4Next.setOutputMarkupId(true);
			add(b4Next);
			reCheckChanges(null);
			
			generateDiscardButton(this, robFormB);
		}
		
		private void copyFromUserProfileToRobFormB4(LlpUserProfile llpUserProfile, RobFormB4 robFormB4) {
			Map<String,String> mapNationalityCodeEzbizToCBS = llpParametersService.findActiveCodeTypeAsMap(Parameter.NATIONALITY_TYPE_CBS_MAPPING);
			
			
			// TODO Auto-generated method stub
			robFormB4.setRobFormBCode(robFormB.getRobFormBCode());
			
			robFormB4.setEzbizUserRefNo(llpUserProfile.getUserRefNo());
			robFormB4.setEzbizLoginName(llpUserProfile.getLoginId());
			
			robFormB4.setAmmendmentType(Parameter.ROB_FORM_B4_AMMENDMENT_TYPE_NO_CHANGES);
			robFormB4.setAmmendmentDate(null);
			robFormB4.setNewicno(llpUserProfile.getIdNo());
			robFormB4.setName(llpUserProfile.getName());
			robFormB4.setDob(llpUserProfile.getDob());
			robFormB4.setNationality(mapNationalityCodeEzbizToCBS.get(llpUserProfile.getNationality()));
			robFormB4.setCountryoforiginifpr(llpUserProfile.getCountry());
			robFormB4.setGender(llpUserProfile.getGender());
			robFormB4.setRace(llpUserProfile.getRace());
			robFormB4.setOtherrace(llpUserProfile.getOthersRace());
			robFormB4.setEmail(llpUserProfile.getEmail());
			
//			robFormB4.setTelNo(llpUserProfile.gette);
//			robFormB4.setMobileNo(llpUserProfile.getHpNo());
			
//			robFormB4.setAddr(llpUserProfile.getAdd1());
//			robFormB4.setAddr2(llpUserProfile.getAdd2());
//			robFormB4.setAddr3(llpUserProfile.getAdd3());
//			robFormB4.setAddrTown(llpUserProfile.getCity());
//			robFormB4.setAddrPostcode(llpUserProfile.getPostcode());
//			robFormB4.setAddrState(llpUserProfile.getState());
			
			
			//Owner Verification
//			RobFormOwnerVerification ownerVerification = new RobFormOwnerVerification();
//			ownerVerification.setCbsOwnerInfo(owner[i]);
//			ownerVerification.setName(owner[i].getName());
//			ownerVerification.setIdNo(owner[i].getIcNo());
//			ownerVerification.setIdType("01");
//			ownerVerification.setRobFormCode(robFormB.getRobFormBCode());
//			ownerVerification.setRobFormType(Parameter.ROB_FORM_TYPE_B);
//			ownerVerification.setEmailStatus(Parameter.ROB_OWNER_VER_EMAIL_STATUS_UNSEND);//by default
//			ownerVerification.setStatus(Parameter.ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION);
			
			
				
		}
		
		
		void reCheckChanges(AjaxRequestTarget target){
			boolean isChanges = false;
			if(robFormB.getIsB1()){
				isChanges = true;
			}else if(robFormB.getIsB2()){
				isChanges = true;
			}else if(robFormB.getIsB3()){
				isChanges = true;
			}else if(robFormB.getIsB4()){
				if(robFormB.getB4AmmendmendDt()!=null) {
					isChanges = true;
				}else {
					reCalculateB4AmendmentDate(target);
					if(robFormB.getB4AmmendmendDt()!=null) {
						isChanges = true;
					}
				}
			}
			
			
			b4Next.setEnabled(isChanges);
			if(target!=null){
				target.add(b4Next);
			}
			
		}
	}
}