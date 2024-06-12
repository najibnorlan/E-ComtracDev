package com.ssm.common.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RobMenu implements Serializable {
	private String menuCode;
	private String menuName;
	private String pageClassName;
	private String parentClassName;
	private List<RobMenu> listChild = new ArrayList<RobMenu>();
	
	public RobMenu(String menuCode, String menuName, String pageClassName, String parentClassName){
		this.menuCode = menuCode;
		this.menuName = menuName;
		this.pageClassName=pageClassName;
		this.parentClassName = parentClassName;
	}
	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getPageClassName() {
		return pageClassName;
	}
	public void setPageClassName(String pageClassName) {
		this.pageClassName = pageClassName;
	}
	public String getParentClassName() {
		return parentClassName;
	}
	public void setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public List<RobMenu> getListChild() {
		return listChild;
	}

	public void setListChild(List<RobMenu> listChild) {
		this.listChild = listChild;
	}
	
	
	
}
