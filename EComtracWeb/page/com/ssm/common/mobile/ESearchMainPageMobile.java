package com.ssm.common.mobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ssm.client.ssmportal.SSMEntity;
import com.ssm.client.ssmportal.SSMPortalClient;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSessionSortableDataProvider;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.mod1.model.LlpUserProfile;
import com.ssm.llp.mod1.page.EditLlpUserProfilePage;
import com.ssm.llp.mod1.service.LlpUserProfileService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class ESearchMainPageMobile extends ESearchMainPageMobileBasePage{
	@SpringBean(name = "LlpParametersService")
	private LlpParametersService llpParametersService;
	
	public ESearchMainPageMobile() {
		this(null, null);
	}
	
	
	public ESearchMainPageMobile(String entityType, String entityNo) {
		super(entityType, entityNo);
		
		String url = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_WEBIS2_URL);
//		String url = "http://10.7.31.38:9763/services/SSMPortalServices/";
		
		List<SSMEntity> listEntity = new ArrayList<SSMEntity>();
		try {
			listEntity = SSMPortalClient.findEntity(url , entityType, entityNo);
		} catch (Exception e) {
			ssmError(e.getMessage());
			e.printStackTrace();
		}
		SSMSessionSortableDataProvider dp = new SSMSessionSortableDataProvider(listEntity);

		final SSMDataView<SSMEntity> dataView = new SSMDataView<SSMEntity>("sorting", dp) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(final Item<SSMEntity> item) {
				final SSMEntity ssmEntity = item.getModelObject();
				String status = ssmEntity.getEntityStatus();
				item.add(new SSMLabel("entityNo", ssmEntity.getFullEntityNo()));
				item.add(new SSMLabel("entityName", ssmEntity.getEntityName()));
				if(Parameter.ENTITY_TYPE_ROB.equals(ssmEntity.getEntityType())){
					status = llpParametersService.findByCodeTypeValue(Parameter.BUSINESS_CODE_STATUS, ssmEntity.getEntityStatus());
				}
				if(Parameter.ENTITY_TYPE_ROC.equals(ssmEntity.getEntityType())){
					status = llpParametersService.findByCodeTypeValue(Parameter.ROC_STATUS, ssmEntity.getEntityStatus());
				}
				if(Parameter.ENTITY_TYPE_LLP.equals(ssmEntity.getEntityType())){
					status = llpParametersService.findByCodeTypeValue(Parameter.LLP_STATUS, ssmEntity.getEntityStatus());
				}
				item.add(new SSMLabel("entityStatus", status));//LLP_STATUS
				item.add(new MultiLineLabel("gstNo", StringUtils.join(ssmEntity.getEntityGsts(),"\n")));

				
				item.add(AttributeModifier.replace("class",
						new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								return (item.getIndex() % 2 == 1) ? "even"
										: "odd";
							}
						}));
			}
		};

		dataView.setItemsPerPage(10L);

		

		add(dataView);
		add(new PagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
		// add(new BookmarkablePageLink("newData",
		// EditLlpUserProfilePage.class));
	}

	private final SearchCriteria getSearchCriteria(String userRefNo,String idType, String idNo, String name, String userStatus) {
		SearchCriteria sc = null;

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
			sc = SearchCriteria.andIfNotNull(sc, "name", SearchCriteria.EQUAL,name);
		}

		if (StringUtils.isNotBlank(userStatus)) {
			sc = SearchCriteria.andIfNotNull(sc, "userStatus",SearchCriteria.EQUAL, userStatus);
		}

		if (sc == null) {
			sc = new SearchCriteria();
		}

		return sc;
	}
	
}
