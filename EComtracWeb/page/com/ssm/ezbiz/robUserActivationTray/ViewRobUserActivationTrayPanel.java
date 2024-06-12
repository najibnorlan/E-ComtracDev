package com.ssm.ezbiz.robUserActivationTray;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.hibernate.LazyInitializationException;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.sec.UserEnvironmentHelper;
import com.ssm.llp.base.common.service.LlpFileDataService;
import com.ssm.llp.base.page.BasePanel;
import com.ssm.llp.base.sec.LlpUserEnviroment;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.ezbiz.model.RobFormNotes;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;
import com.ssm.llp.mod1.model.LlpUserProfile;

public class ViewRobUserActivationTrayPanel extends BasePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@SpringBean(name = "LlpFileDataService")
	private LlpFileDataService llpFileDataService;

	public ViewRobUserActivationTrayPanel(String panelId, final RobUserActivationTray activationTray) {
		super(panelId);
	

		LlpUserProfile llpUserProfile = activationTray.getLlpUserProfile();

		add(new SSMLabel("appRefNo", activationTray.getAppRefNo()));
		add(new SSMLabel("userRefNo", llpUserProfile.getUserRefNo()));
		add(new SSMLabel("loginId", llpUserProfile.getLoginId()));
		add(new SSMLabel("name", llpUserProfile.getName()));
		
		
		
		String idNo = llpUserProfile.getIdNo();
		if(UserEnvironmentHelper.isInternalUser()) {
			if(Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS.equals(activationTray.getStatus()) || Parameter.ACTIVATION_TRAY_STATUS_IN_PROCESS_RESUBMISSION.equals(activationTray.getStatus())){
				idNo = "";//Hide ID NO
			}
		}
			
		add(new SSMLabel("idNo", idNo));
		add(new SSMLabel("idType", llpUserProfile.getIdType(), Parameter.ID_TYPE_FOR_CMP_OFFICER));
		add(new SSMLabel("icColour", llpUserProfile.getIcColour(), Parameter.NRIC_COLOUR));
		add(new SSMLabel("email", llpUserProfile.getEmail()));
		
		
		add(new SSMLabel("race", llpUserProfile.getRace(), Parameter.RACE));
		add(new SSMLabel("othersRace", llpUserProfile.getOthersRace()));
		
		add(new SSMLabel("nationality", llpUserProfile.getNationality(), Parameter.NATIONALITY_TYPE_FOR_CMP_OFFICER));

		String ownerAddr = llpUserProfile.getAdd1();
		if (StringUtils.isNotBlank(llpUserProfile.getAdd2())) {
			ownerAddr += "\n" + llpUserProfile.getAdd2();
		}
		if (StringUtils.isNotBlank(llpUserProfile.getAdd3())) {
			ownerAddr += "\n" + llpUserProfile.getAdd3();
		}

		ownerAddr += "\n" + llpUserProfile.getPostcode() + " " + llpUserProfile.getCity().toUpperCase();
		ownerAddr += "\n" + getCodeTypeWithValue(Parameter.STATE_CODE, llpUserProfile.getState());

		add(new MultiLineLabel("ownersAddress", ownerAddr));

		SSMLink downlodMykadDoc = generateDownloadLink("downlodMykadDoc", activationTray.getMykadDocId(), "MyKad");
		add(downlodMykadDoc);

		SSMLink downlodSelfieDoc = generateDownloadLink("downlodSelfieDoc", activationTray.getSelfieDocId(), "SelfieDoc");
		add(downlodSelfieDoc);

		SSMLink downlodSupportingDoc = generateDownloadLink("downlodSupportingDoc", activationTray.getSupportingDocId(), "SupportingDoc");
		add(downlodSupportingDoc);
		
		add(new SSMLabel("status", activationTray.getStatus(), Parameter.ACTIVATION_TRAY_STATUS)) ;
		add(new MultiLineLabel("processNote", activationTray.getProcessNote())) ;
		
		
		final WebMarkupContainer wmcQuery = new WebMarkupContainer("wmcQuery");
		wmcQuery.setPrefixLabelKey(getPrefixLabelKey());
		wmcQuery.setAutoCompleteForm(false);	
		wmcQuery.setOutputMarkupId(true);
		wmcQuery.setOutputMarkupPlaceholderTag(true);
		wmcQuery.setVisible(false);
		add(wmcQuery);
		
		
		final WebMarkupContainer wmcOfficeUser = new WebMarkupContainer("wmcOfficeUser");
		wmcQuery.setPrefixLabelKey(getPrefixLabelKey());
		wmcOfficeUser.setAutoCompleteForm(false);	
		wmcOfficeUser.setOutputMarkupId(true);
		wmcOfficeUser.setOutputMarkupPlaceholderTag(true);
		wmcOfficeUser.setVisible(false);
		add(wmcOfficeUser);
		
		ListView<RobFormNotes> listQueryView = new ListView("listQueryView", activationTray.getListRobFormNotes()) {
			@Override
			protected void populateItem(ListItem item) {
				RobFormNotes robormNotes = (RobFormNotes) item.getModelObject();
				item.add(new Label("queryBy", robormNotes.getQueryBy()));
				item.add(new MultiLineLabel("notes", robormNotes.getNotes()));
				item.add(new MultiLineLabel("notesAnswer", robormNotes.getNotesAnswer()));
				Date updateDt = null;
				if(robormNotes.getUpdateDt()!=null && !robormNotes.getUpdateDt().equals(robormNotes.getCreateDt())){
					updateDt = robormNotes.getUpdateDt();
				}
				item.add(new SSMLabel("createDt", robormNotes.getCreateDt(),"dd/MM/yyyy hh:mm:ss a"));
				item.add(new SSMLabel("updateDt", updateDt,"dd/MM/yyyy hh:mm:ss a"));
			}
		    
		};
		listQueryView.setVisible(false);
		wmcQuery.add(listQueryView);
		
		
		if(UserEnvironmentHelper.isInternalUser()) {
			if(activationTray.getListRobFormNotes().size()>0 ) {
				RobFormNotes latestNotes = activationTray.getListRobFormNotes().get(activationTray.getListRobFormNotes().size()-1);
				wmcQuery.add(new MultiLineLabel("queryText", latestNotes.getNotes()));
				wmcQuery.add(new MultiLineLabel("queryAnswer", latestNotes.getNotesAnswer()));
				wmcQuery.setVisible(true);
				if(activationTray.getListRobFormNotes().size()>1) {
					listQueryView.setVisible(true);
				}
			}
			if(StringUtils.isNotBlank(activationTray.getProcessBy() )) {
				
				wmcOfficeUser.add(new SSMLabel("processBy", activationTray.getProcessBy()));
				wmcOfficeUser.add(new SSMLabel("processBranch",  getCodeTypeWithValue(Parameter.BRANCH_CODE, activationTray.getProcessBranch() )));
				wmcOfficeUser.add(new SSMLabel("processDt", activationTray.getProcessDt(), "dd/MM/yyyy hh:mm:ss a"));
				wmcOfficeUser.setVisible(true);
			}
		}
		

	}

	private SSMLink generateDownloadLink(String downloadId, final LlpFileData llpFileData, final String fileName) {
		return new SSMLink(downloadId) {
			@Override
			public void onClick() {
				LlpFileData fileDataTmp = null;
				try {
					llpFileData.getFileData();
					fileDataTmp = llpFileData;
				} catch (LazyInitializationException e) {
					fileDataTmp = llpFileDataService.findById(llpFileData.getFileDataId());

				}

				final byte byteData[] = fileDataTmp.getFileData();
				final String contentType = fileDataTmp.getFileDataType();

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
						return contentType;
					}
				};
				String fileExt = "";

				MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
				try {
					MimeType type = allTypes.forName(contentType);
					fileExt = type.getExtension();

				} catch (Exception e) {

				}

				if (StringUtils.isBlank(fileExt)) {
					if (contentType.indexOf("pdf") != -1) {
						fileExt = ".pdf";
					}
					if (contentType.indexOf("image") != -1) {
						fileExt = "." + StringUtils.split(contentType, "/")[1];
					}
					if (contentType.indexOf("text") != -1) {
						fileExt = ".txt";
					}
				}

				ResourceStreamRequestHandler rs = new ResourceStreamRequestHandler(resourceStream).setFileName(fileName + fileExt);
				rs.setCacheDuration(Duration.NONE);
				getRequestCycle().scheduleRequestHandlerAfterCurrent(rs);
			}
		};
	}

}