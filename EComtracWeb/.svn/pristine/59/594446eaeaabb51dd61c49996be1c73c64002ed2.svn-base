package com.ssm.common.htmleditor;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.service.MailService;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class HtmlEditorPanel extends Panel {


	private WebMarkupContainer wmcEditor;
	
	
	public HtmlEditorPanel(String id) {
		super(id);
		
		wmcEditor = new WebMarkupContainer("wmcEditor");
		wmcEditor.setOutputMarkupId(true);
		wmcEditor.setOutputMarkupPlaceholderTag(true);
		add(wmcEditor);
//		
//		String htmlStr="SHHDSHDH <b>ZAMZAM</b>";
//		String htmlStrJS = " var htmlStr= '"+htmlStr+"';";
//		add(new SSMLabel("jsScriptParam",htmlStrJS));
	}
	
	
	public HtmlEditorPanel(String id, String htmlStr) {
		super(id);
		
		wmcEditor = new WebMarkupContainer("wmcEditor");
		wmcEditor.setOutputMarkupId(true);
		wmcEditor.setOutputMarkupPlaceholderTag(true);
		add(wmcEditor);
		
		String htmlStrJS = " var htmlStr= '"+htmlStr+"';";
		add(new SSMLabel("jsScriptParam",htmlStrJS));
	}


	public String getWmcEditorId() {
		return wmcEditor.getMarkupId();
	}

}
