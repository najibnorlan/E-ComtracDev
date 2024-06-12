package com.ssm.llp.base.common.service.impl;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.RssFeedSchedulerService;

@Service
//@EnableScheduling
public class RssFeedSchedulerServiceImpl implements RssFeedSchedulerService {

	@Autowired
    ServletContext context; 

	@Autowired 
	LlpParametersService llpParametersService;
	
	@Override
	@Scheduled(fixedDelay=18000000)//5hours
	public void getSSMPortalFeed() {
		try {
			File fileRss = new File(context.getRealPath("")+"/ssm-portal-feed.xml");
//			String urlPath = "http://ssmportal/_layouts/15/listfeed.aspx?List=%7B232808A9-DD24-4D8F-8090-57DE0DE5A03E%7D&Source=http%3A%2F%2Fssmportal%2FLists%2FAnnouncement%2FAllItems%2Easpx";
			String urlPath = "http://www.ssm.com.my/en/2ssm-portal-feed";
//			String urlPath = "http://www.ssm.com.my/contact-us-rss-feed";
//			String urlPath = "http://ssmportalen/_layouts/15/listfeed.aspx?List=%7b5527748E-571C-4628-A900-3C4D2657A09D%7d&Source=http%3a//ssmportalen/Lists/Contact%2520Us/AllItems.aspx";
			URL url = new URL(urlPath);
			
			
//			ssm-portal-feed.xml
			String urlParam = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_RSS_URL);
			if(StringUtils.isNotBlank(urlParam)){
				url = new URL(urlParam);
			}
//			System.out.println(fileRss.getCanonicalPath());
			FileUtils.copyURLToFile(url, fileRss);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
