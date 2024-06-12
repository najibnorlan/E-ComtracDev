package com.cooldatasoft.horizontal.dropdown.multiLevelCss;
import java.util.List;

import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import com.cooldatasoft.common.MenuItem;

/**
 * http://www.dynamicdrive.com/style/csslibrary/item/ jquery_multi_level_css_menu_2/
 * 
 * @author Fatih Mehmet UCAR - fmucar@gmail.com
 * 
 */
public class MultiLevelCssMenu extends Panel implements IHeaderContributor {

	private static final long serialVersionUID = 1L;

	private final static ResourceReference DOWN_GIF = new PackageResourceReference(MultiLevelCssMenu.class, "js/down.gif");
	private final static ResourceReference RIGHT_GIF = new PackageResourceReference(MultiLevelCssMenu.class, "js/right.gif");

	private final static ResourceReference MENU_CSS = new CssResourceReference(MultiLevelCssMenu.class,
			"css/MultiLevelCssMenu.css");
	private final static ResourceReference JQUERY_MIN_JAVASCRIPT = new JavaScriptResourceReference(MultiLevelCssMenu.class,
			"js/jqueryMin.js");
	private final static ResourceReference MENU_JAVASCRIPT = new JavaScriptResourceReference(MultiLevelCssMenu.class,
			"js/MultiLevelCssMenu.js");

	@Override
	public void renderHead(IHeaderResponse response) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("var downGifRelativeLocation='").append(RequestCycle.get().urlFor(DOWN_GIF, null)).append("';");
		buffer.append("var rightGifRelativeLocation='").append(RequestCycle.get().urlFor(RIGHT_GIF, null)).append("';");

		response.getResponse().write(JavaScriptUtils.SCRIPT_OPEN_TAG);
		response.getResponse().write(buffer.toString());
		response.getResponse().write(JavaScriptUtils.SCRIPT_CLOSE_TAG);

		
		response.render(JavaScriptHeaderItem.forReference(JQUERY_MIN_JAVASCRIPT));
		response.render(JavaScriptHeaderItem.forReference(MENU_JAVASCRIPT));
		response.render(CssHeaderItem.forReference(MENU_CSS));
	}

	public MultiLevelCssMenu(String id, List<MenuItem> menuItemList) {
		super(id);
		setRenderBodyOnly(true);
		MultiLevelMenu multiLevelMenu = new MultiLevelMenu("multiLevelMenu", menuItemList);
		multiLevelMenu.setRenderBodyOnly(true);
		add(multiLevelMenu);
	}

	private void processResponse(MenuItem menuItem) {
		switch (menuItem.getDestinationType()) {
		case EXTERNAL_LINK:
			// forward to external link
			break;
		case WEB_PAGE_CLASS:
			setResponsePage(menuItem.getResponsePageClass());
			break;
		case WEB_PAGE_INSTANCE:
			setResponsePage(menuItem.getResponsePage());
			break;
		case AJAX_TARGET:
			menuItem.getAjaxLink().onClick();
			break;
		case NONE:
			// TODO throw exception
			break;
		}
	}

	class MultiLevelMenu extends Panel {

		private static final long serialVersionUID = 1L;

		public MultiLevelMenu(String id, List<MenuItem> menuItemList) {
			super(id);
			if (menuItemList == null || menuItemList.size() == 0) {
				return;
			}
			ListView<MenuItem> menu = buildMultiLevelMenu("menuList", menuItemList);
			menu.setReuseItems(true);
			add(menu);
		}

		private ListView<MenuItem> buildMultiLevelMenu(String id, List<MenuItem> menuItemList) {
			return new ListView<MenuItem>(id, menuItemList) {
				private static final long serialVersionUID = 1L;

				public void populateItem(final ListItem<MenuItem> item) {
					final MenuItem menuItem = ((MenuItem) item.getModelObject());
					Link<MenuItem> link = new Link<MenuItem>("menuLink") {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							if (menuItem != null) {
								processResponse(menuItem);
							}
						}
					};

					Label linkText = new Label("menuLinkText", menuItem.getMenuText());
					linkText.setRenderBodyOnly(true);
					link.add(linkText);
					item.add(link);

					List<MenuItem> submenuItemList = menuItem.getSubMenuItemList();
					// INFO If submenu exists, output it to html. If not, add
					// empty markup container and hide it.
					if (submenuItemList != null && submenuItemList.size() > 0) {
						MultiLevelMenu subLevelMenu = new MultiLevelMenu("submenuListContainer", submenuItemList);
						subLevelMenu.setRenderBodyOnly(true);
						item.add(subLevelMenu);
					} else {
						WebMarkupContainer submenuMarkupContainer = new WebMarkupContainer("submenuListContainer");
						submenuMarkupContainer.setRenderBodyOnly(true);
						item.add(submenuMarkupContainer);
					}
				}
			};
		}
	}
}
