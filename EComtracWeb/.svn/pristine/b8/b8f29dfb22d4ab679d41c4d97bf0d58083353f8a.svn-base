package com.ssm.llp.mod1.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.ssm.llp.base.page.SecBasePanel;

public class LlpRegistrationTabPanel extends SecBasePanel {

	public static int LLP_PANEL = 0;
	public static int CO_PANEL = 1;
	public static int PARTNER_LINK_PANEL = 2;
	public static int BUSINESS_LINK_PANEL = 3;
	

	public LlpRegistrationTabPanel(String panelId, int tabIdToLoad) {
		super(panelId);

		setDefaultModel(new Model<String>("tabpanel4"));

		// 2nd layer Tab
		List<ITab> tabs = new ArrayList<ITab>();

		tabs.add(new AbstractTab(new Model<String>("LLP")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new EditLlpRegistrationPanel(panelId);
			}

		});

		tabs.add(new AbstractTab(new Model<String>("Compliance Officer")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new EditCORegistrationPanel(panelId);

			}

		});

		tabs.add(new AbstractTab(new Model<String>("Partner")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				// return new EditPartnerRegistrationPanel(panelId);
				return new ListPartnerRegistrationPanel(panelId);
			}

		});

		/*
		 * tabs.add(new AbstractTab(new Model<String>("Lodger")) { private
		 * static final long serialVersionUID = 1L;
		 * 
		 * @Override public Panel getPanel(String panelId) { return new
		 * EditLlpUserProfilePanel(panelId); } });
		 */

		tabs.add(new AbstractTab(new Model<String>("Business Code")) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new LlpBusinessCodePanel(panelId);
			}

		});

		// add the new tabbed panel, attribute modifier only used to switch
		// between different css variations
		final TabbedPanel<ITab> tabbedPanel = new TabbedPanel<ITab>("tabs", tabs);
//		tabbedPanel.add(AttributeModifier.replace("class", this.getDefaultModel()));
		tabbedPanel.setSelectedTab(tabIdToLoad); // utk lepas save kekal stay kat page tersebut
		add(tabbedPanel);
	}

}
