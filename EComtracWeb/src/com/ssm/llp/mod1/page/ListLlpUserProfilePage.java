package com.ssm.llp.mod1.page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.annotations.OrderBy;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.SyncLlpUserProfilePage;
import com.ssm.llp.base.page.UserApprovalPage;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({"all"})
public class ListLlpUserProfilePage extends LlpUserProfileBasePage {
	@SpringBean(name = "LlpUserProfileService")
	private LlpUserProfileService llpUserProfileService;
	
	
	public String getPageTitle() {
		String titleKey = "page.title.userprofile.list";
		return titleKey;
	}
	
	public ListLlpUserProfilePage() {
		this(null, null, null, null, null, null, null, null, null, null, null, null);
	}

	public ListLlpUserProfilePage(String userRefNo, String idType, String idNo,String name, String userStatus, Date dtFrom, Date dtTo, Date dtApproveTo, Date dtApproveFrom, String approveChannel, String approveBy, String email) {
	
		super(userRefNo, idType, idNo, name, userStatus, dtFrom, dtTo, dtApproveTo, dtApproveFrom, approveChannel, approveBy, email);
		SearchCriteria sc = getSearchCriteria(userRefNo, idType, idNo, name,userStatus, dtFrom, dtTo, dtApproveTo, dtApproveFrom, approveChannel, approveBy, email);
	
		SSMSortableDataProvider dp = new SSMSortableDataProvider("createDt", SortOrder.DESCENDING,sc, LlpUserProfileService.class);
	
		final SSMDataView<LlpUserProfile> dataView = new SSMDataView<LlpUserProfile>("sorting", dp) {
			private static final long serialVersionUID = 1L;
	
			protected void populateItem(final Item<LlpUserProfile> item) {
				final LlpUserProfile llpUserProfile = item.getModelObject();
				
				item.add(new SSMLabel("userRefNo", llpUserProfile.getUserRefNo()));
				item.add(new SSMLabel("loginId", llpUserProfile.getLoginId()));
				item.add(new SSMLabel("name", llpUserProfile.getName()));
				item.add(new SSMLabel("idNo", llpUserProfile.getIdNo()));
				item.add(new SSMLabel("approvedBy", llpUserProfile.getApproveBy()));
				item.add(new SSMLabel("dateApproved", llpUserProfile.getApproveDt()));				
				item.add(new SSMLabel("userStatus", llpUserProfile.getUserStatus(), Parameter.USER_STATUS));
				item.add(new SmartLinkLabel("email", llpUserProfile.getEmail()));
	
				item.add(new Link("edit", item.getDefaultModel()) {
					public void onClick() {
						LlpUserProfile c = (LlpUserProfile) getModelObject();
						setResponsePage(new EditLlpUserProfilePage(c.getUserRefNo()));
					}
				});
				item.add(new Link("resetPassword", item.getDefaultModel()) {
					public void onClick() {
						LlpUserProfile c = (LlpUserProfile) getModelObject();
						
						setResponsePage(new LlpUserResetPasswordPage(llpUserProfile));
					}
				});
	
				item.add(new Link("syncName", item.getDefaultModel()) {
					public void onClick() {
						LlpUserProfile c = (LlpUserProfile) getModelObject();
						
						setResponsePage(new SyncLlpUserProfilePage(llpUserProfile));
					}
				});
				
				Link link = new Link("approveUser", item.getDefaultModel()) {
					public void onClick() {
						LlpUserProfile c = (LlpUserProfile) getModelObject();
						
						setResponsePage(new UserApprovalPage(llpUserProfile));
					}
				};
				item.add(link);
				
				item.add(AttributeModifier.replace("class",new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;
	
							@Override
							public String getObject() {
								return (item.getIndex() % 2 == 1) ? "even"
										: "odd";
							}
						}));
			}
		};
	
		dataView.setItemsPerPage(50L);
	
		add(dataView);
		add(new SSMPagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
		// add(new BookmarkablePageLink("newData",
		// EditLlpUserProfilePage.class));
	}

	private final SearchCriteria getSearchCriteria(String userRefNo,String idType, String idNo, String name, String userStatus, Date dtFrom, Date dtTo, Date dtApproveTo, Date dtApproveFrom, String approveChannel, String approveBy, String email) {
		SearchCriteria sc = null;

		SimpleDateFormat fom = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat pars = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (StringUtils.isNotBlank(userRefNo)) {
			sc = SearchCriteria.andIfNotNull(sc, "userRefNo",SearchCriteria.EQUAL, userRefNo);
		}

		if (StringUtils.isNotBlank(idType)) {
			sc = SearchCriteria.andIfNotNull(sc, "idType",SearchCriteria.EQUAL, idType);
		}

		if (StringUtils.isNotBlank(idNo)) {
			sc = SearchCriteria.andIfNotNull(sc, "idNo", SearchCriteria.EQUAL,idNo);
		}

		if (StringUtils.isNotBlank(name)) {
			sc = SearchCriteria.andIfNotNull(sc, "name", SearchCriteria.LIKE, name);
		}

		if (StringUtils.isNotBlank(userStatus)) {
			sc = SearchCriteria.andIfNotNull(sc, "userStatus",SearchCriteria.EQUAL, userStatus);
		}
		
		if (StringUtils.isNotBlank(approveChannel)) {
			sc = SearchCriteria.andIfNotNull(sc, "approveChannel",SearchCriteria.EQUAL, approveChannel);
		}
		
		if (StringUtils.isNotBlank(approveBy)) {
			String[] splitString = approveBy.split(",");
			List<String> temp = new ArrayList<String>();
			for(String i : splitString){
				temp.add("SSM:"+ i.trim());
			}
			String[] arr = new String[temp.size()];
			temp.toArray(arr);
			
			sc = SearchCriteria.andIfInNotNull(sc, "approveBy", arr, false);
		}

		if(dtFrom != null){
			if(sc != null){
				try {
					sc = sc.andIfNotNull("createDt", SearchCriteria.GREATER_EQUAL, pars.parse(fom.format(dtFrom) + " 00:00:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				try {
					sc = new SearchCriteria("createDt", SearchCriteria.GREATER_EQUAL, pars.parse(fom.format(dtFrom) + " 00:00:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(dtTo != null){
			if(sc != null){
				try {
					sc = sc.andIfNotNull("createDt", SearchCriteria.LESS_EQUAL, pars.parse(fom.format(dtTo) + " 23:59:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				try {
					sc = new SearchCriteria("createDt", SearchCriteria.LESS_EQUAL, pars.parse(fom.format(dtTo) + " 23:59:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(dtApproveFrom != null){
			if(sc != null){
				try {
					sc = sc.andIfNotNull("approveDt", SearchCriteria.GREATER_EQUAL, pars.parse(fom.format(dtApproveFrom) + " 00:00:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				try {
					sc = new SearchCriteria("approveDt", SearchCriteria.GREATER_EQUAL, pars.parse(fom.format(dtApproveFrom) + " 00:00:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(dtApproveTo != null){
			if(sc != null){
				try {
					sc = sc.andIfNotNull("approveDt", SearchCriteria.LESS_EQUAL, pars.parse(fom.format(dtApproveTo) + " 23:59:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				try {
					sc = new SearchCriteria("approveDt", SearchCriteria.LESS_EQUAL, pars.parse(fom.format(dtApproveTo) + " 23:59:00"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		if (StringUtils.isNotBlank(email)) {
			sc = SearchCriteria.andIfNotNull(sc, "email",SearchCriteria.EQUAL, email);
		}

		return sc;
	}
	
}
