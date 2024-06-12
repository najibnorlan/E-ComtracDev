package com.ssm.common.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

public class SSMMenuGroupLink extends Panel
{
	private static final long serialVersionUID = 1L;
	List<RobMenu> listMenuLink = new ArrayList<RobMenu>();
	
	public SSMMenuGroupLink(String id, List<RobMenu> listMenuLink) {
		super(id);
		this.listMenuLink = listMenuLink;
	}

	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		for (int i = 0; i < listMenuLink.size(); i++) {
			
		}
	}
	

}
