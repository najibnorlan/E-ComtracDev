package com.ssm.template;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;


public class BSTemplatePage extends WebPage{

	    private static final long serialVersionUID = 5218374612129395403L;

	    public static final String CONTENT_ID = "contentComponent";

	    private Component headerPanel;
	    private Component menuPanel;
	    private Component footerPanel;

	    public BSTemplatePage(){
	        add(headerPanel = new BSHeaderPanel("headerPanel"));
	        add(footerPanel = new BSFooterPanel("footerPanel"));
//	        add(new Label(CONTENT_ID, "Put your content here"));
	    }

	    
	    public Component getHeaderPanel() {
	        return headerPanel;
	    }

	    public Component getMenuPanel() {
	        return menuPanel;
	    }

	    public Component getFooterPanel() {
	        return footerPanel;
	    }
}
