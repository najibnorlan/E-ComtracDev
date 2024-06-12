package com.ssm.ezbiz.healthCheck;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;

import com.ssm.llp.base.page.SecBasePage;

@SuppressWarnings("serial")
public class StatCheck extends SecBasePage {
	
	final String[] webServices = {"ezBiz", "Google"};
	String[] webHosts = { "http://ezbiz.ssm.com.my", "http://www.google.com"};
	
	String[] dbServices = { "SSM DB Server [Dev]", "EzBiz DB Server [Dev]"};
	String[] dbHosts = { "10.7.34.95", "10.7.34.129"};
	
	public StatCheck(){
		List<HostData> hostDatas = new ArrayList<HostData>();
		
		for (int i=0; i<webServices.length; i++){
			
			HostData host = new HostData();
			host.setHostname(webServices[i]);
			host.setAddress(webHosts[i]);
			
			try {
				URL u = new URL(host.getAddress());
				HttpURLConnection huc = (HttpURLConnection) u.openConnection();
				huc.setInstanceFollowRedirects(false);
				huc.setRequestMethod("HEAD");
				huc.connect();
				host.setStatus(huc.getResponseCode() + " - " + huc.getResponseMessage());
			} catch (Exception e) {
				host.setStatus(e.toString().trim().split(":")[1]);
			}
			
			hostDatas.add(host);
		}
		
		for (int i=0; i<dbServices.length; i++){
			
			HostData host = new HostData();
			host.setHostname(dbServices[i]);
			host.setAddress(dbHosts[i]);
			
			try {
				InetAddress inet = InetAddress.getByName(host.getAddress());
				if (inet.isReachable(5000)){
					host.setStatus("Online");
				} else {
					host.setStatus("Offline");
				}
			} catch (IOException e) {
				host.setStatus("Status: Error");
			}
			
			hostDatas.add(host);
		}
		
		add(new ListView<HostData>("hostDatas", hostDatas) {
			@Override
			protected void populateItem(ListItem<HostData> data) {
				data.add(new Label("svrhostname", data.getModelObject().getHostName()));
				data.add(new Label("svrhostaddr", data.getModelObject().getAddress()));
				data.add(new Label("svrstatus", data.getModelObject().getStatus()));
			}
		});
		
		Link refreshpage = new Link("refreshpage") {
			@Override
			public void onClick() {
				setResponsePage(StatCheck.class);
			}
		};
		add(refreshpage);
	}
	
	public class HostData implements Serializable{
		String hostName;
		String address;
		String status;
		
		public String getHostName() {
			return hostName;
		}

		public void setHostname(String HostName) {
			this.hostName = HostName;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getStatus() {
			return status;
		}
		
		public void setStatus(String status) {
			this.status = status;
		}
	}

	@Override
	public String getPageTitle() {
		return "ezbiz.healthcheck.msg1";
	}
}
