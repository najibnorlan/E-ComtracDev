package com.ssm.llp.base.page;

import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class SSMMessagesFeedbackPanel extends FeedbackPanel{

	  private SSMMessageFilter filter = new SSMMessageFilter();

	  public SSMMessagesFeedbackPanel(String id){
	    super(id);
	    setFilter(filter);
	  }


	  @Override
	  protected void onBeforeRender(){
	    super.onBeforeRender();
	    // clear old messages
	    filter.clearMessages();
	  }
}
