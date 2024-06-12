package com.ssm.llp.mod1.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class Page1 extends BasePage {
	public Page1() {

		add(new SSMLabel("label1", "This is in the subclass Page1"));
		setDefaultModel(new Model<String>("tabpanel4"));

		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model<String>("first tab")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new SearchPanel(panelId);
			}

		});

		tabs.add(new AbstractTab(new Model<String>("second tab")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new SearchPanel(panelId);
			}

		});

		tabs.add(new AbstractTab(new Model<String>("third tab")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new SearchPanel(panelId);
			}

		});

		// add the new tabbed panel, attribute modifier only used to switch
		// between different css variations
		final TabbedPanel<ITab> tabbedPanel = new TabbedPanel<ITab>("tabs", tabs);
		tabbedPanel.add(AttributeModifier.replace("class", Page1.this.getDefaultModel()));
		add(tabbedPanel);

	}

	@Override
	public String getPageTitle() {
		return null;
	}

}
