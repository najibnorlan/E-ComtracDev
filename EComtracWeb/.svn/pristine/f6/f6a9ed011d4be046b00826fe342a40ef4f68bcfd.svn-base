package com.ssm.ezbiz.test.page;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.basic.SmartLinkLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.ezbiz.service.TestZamZamService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.SSMPagingNavigator;
import com.ssm.llp.base.page.table.SSMDataView;
import com.ssm.llp.base.page.table.SSMSortableDataProvider;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMLink;
import com.ssm.llp.ezbiz.model.TestZamZam;
import com.ssm.llp.mod1.model.Contact;
import com.ssm.llp.mod1.page.EditContact;
import com.ssm.llp.mod1.page.ListContacts;
import com.ssm.llp.mod1.service.ContactService;

public class TestZamZamPage extends BasePage{


	@SpringBean(name="TestZamZamService")
	private TestZamZamService testZamZamService;
	
	public TestZamZamPage() {
		System.out.println("masuk page zamzam");
		
		
		TestZamZam model = new TestZamZam();
		model.setName("zam"+System.currentTimeMillis());
        model.setAge(20);
        
//        testZamZamService.insert(model);
//        
//        
        TestZamZam modelID24 = testZamZamService.findById((long) 24);
//        modelID24.setName("abu");
//        testZamZamService.update(modelID24);
        
//        testZamZamService.delete(modelID24);
//        
//        testZamZamService.update(s);
//        
        
//        SearchCriteria sc = new SearchCriteria("name",SearchCriteria.LIKE,"%");
//        List<TestZamZam> list  = testZamZamService.findByCriteria(sc).getList();
//        
//        for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i).getName());
//		}
        
        
        
        SearchCriteria sc = new SearchCriteria("age", SearchCriteria.GREATER_EQUAL, 1);
		SSMSortableDataProvider dp = new SSMSortableDataProvider("id", sc, TestZamZamService.class);
		final SSMDataView<TestZamZam> dataView = new SSMDataView<TestZamZam>("sorting", dp) {

			protected void populateItem(final Item<TestZamZam> item) {
				TestZamZam testZamZam = item.getModelObject();
				
				item.add(new SSMLabel("id", String.valueOf(testZamZam.getId())));
				item.add(new SSMLabel("name",  testZamZam.getName()    ));
				item.add(new SSMLabel("age",  testZamZam.getAge()    ));
				
				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		
		dataView.setItemsPerPage(20L);

		add(dataView);
		add(new SSMPagingNavigator("navigator", dataView));
		add(new NavigatorLabel("navigatorLabel", dataView));
		
	}
	
	
	@Override
	public String getPageTitle() {
		return "Zam Zam Page sjdsjdj";
	}

}
