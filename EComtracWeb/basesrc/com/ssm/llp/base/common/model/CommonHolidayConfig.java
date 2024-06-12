package com.ssm.llp.base.common.model;

import java.util.Date;

public class CommonHolidayConfig {
	private int eventYear;
	private Date eventDate;
	private String eventName;
	private String eventType;
	private String eventState;
	
	public CommonHolidayConfig() {
		
	}
	
	public int getEventYear() {
		return eventYear;
	}
	public void setEventYear(int eventYear) {
		this.eventYear = eventYear;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventState() {
		return eventState;
	}
	public void setEventState(String eventState) {
		this.eventState = eventState;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
}
