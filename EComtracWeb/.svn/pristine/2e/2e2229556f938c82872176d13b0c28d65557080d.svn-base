package com.ssm.common.mobile;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.protocol.http.WebApplication;


@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class RssFeedMainPageMobile extends BasePageMobile{
	
	public RssFeedMainPageMobile() {
		try {
			ServletContext servletContext = WebApplication.get().getServletContext();
			File fileRss = new File(servletContext.getRealPath("")+"/rss.xml");
			URL url = new URL("http://www.ssm.com.my/en/2ssm-portal-feed");
			FileUtils.copyURLToFile(url, fileRss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		FileUtils.copyURLToFile(URL, File);
	}

	@Override
	public String getPageTitle() {
		String titleKey = "page.title.rssFeed";
		return titleKey;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
	    super.renderHead(response);
	    String cPath = "'';";
	    try {
			ServletContext servletContext = WebApplication.get().getServletContext();
			cPath = "'"+servletContext.getServletContextName()+"';";
		} catch (Exception e) {
			e.printStackTrace();
		}
	    response.render(JavaScriptHeaderItem.forScript("var cPath="+cPath, "cPath"));
	}
	
}
