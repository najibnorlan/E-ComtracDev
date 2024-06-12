package com.ssm.common.menu;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.string.AppendingStringBuffer;

import com.ssm.llp.base.page.HomePage;

public class SSMMenuLink extends Panel
{
	private static final long serialVersionUID = 1L;
	private RobMenu robMenu;
	private boolean stripTags;
	public SSMMenuLink(String id, RobMenu robMenu) {
		super(id);
		this.robMenu = robMenu;
		setRenderBodyOnly(true);
		stripTags =  Application.get().getMarkupSettings().getStripWicketTags();
	}

	
	@Override
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		super.onComponentTagBody(markupStream, openTag);
		final AppendingStringBuffer buffer = new AppendingStringBuffer();
		
		buffer.append("<span>Ko wat ape tu</span>");
		System.out.println(buffer.toString());
		replaceComponentTagBody(markupStream, openTag, buffer);
	}
	@Override
	protected void onComponentTag(ComponentTag tag) {
		// TODO Auto-generated method stub
		super.onComponentTag(tag);
		System.out.println(tag);
	}

	@Override
	protected void onBeforeRender() {
		Application.get().getMarkupSettings().setStripWicketTags(true);
		super.onBeforeRender();
		
	}
	@Override
	protected void onAfterRender() {
		Application.get().getMarkupSettings().setStripWicketTags(stripTags);
		super.onAfterRender();
	}
	@Override
	protected void onInitialize() {
		super.onInitialize();
		BookmarkablePageLink link = new BookmarkablePageLink("link", HomePage.class);
		link.setRenderBodyOnly(true);
		add(link);
		link.setVisible(false);
		WebMarkupContainer menuGroup = new WebMarkupContainer("menuGroup");
		add(menuGroup);
		menuGroup.setVisible(false);
		menuGroup.setRenderBodyOnly(true);
		
		if(StringUtils.isNotBlank(robMenu.getPageClassName())){
			try {
				link.setVisible(true);
				link = new BookmarkablePageLink("link", Class.forName(robMenu.getPageClassName()));
				replace(link);
				Label menuLinkLabel = new Label("linkLabel",robMenu.getMenuName());
				menuLinkLabel.setRenderBodyOnly(true);
				link.add(menuLinkLabel);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			menuGroup.setVisible(true);
			menuGroup.setRenderBodyOnly(true);
			
			Label menuName = new Label("menuName",robMenu.getMenuName());
			menuName.setRenderBodyOnly(true);
			menuGroup.add(menuName);
			
			Label listviewDummy = new Label("listview",robMenu.getMenuName());
			listviewDummy.setVisible(false);
			listviewDummy.setRenderBodyOnly(true);
			menuGroup.add(listviewDummy);
			
			if(robMenu.getListChild().size()>0){
				listviewDummy.setVisible(true);
				List<RobMenu> listMenu = robMenu.getListChild();
				
				ListView listview = new ListView("listview", listMenu) {
				    protected void populateItem(ListItem item) {
				    	RobMenu robMenu = (RobMenu) item.getDefaultModelObject();
				    	SSMMenuLink menuLink =new SSMMenuLink("ssmMenuLink",robMenu);
				    	menuLink.setRenderBodyOnly(true);
				    	item.add(menuLink);
				    	item.setRenderBodyOnly(true);
				    }
				};
				listview.setRenderBodyOnly(true);
				menuGroup.replace(listview);
			}
		}
		
		
	}
	
	public void generateLink(RobMenu robMenu){
		
	}
	

}
