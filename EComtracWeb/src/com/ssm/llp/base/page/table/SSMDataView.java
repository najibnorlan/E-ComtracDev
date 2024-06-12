package com.ssm.llp.base.page.table;

import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;



public abstract class SSMDataView<T> extends DataView<T>{

	protected SSMDataView(String id, IDataProvider<T> dataProvider) {
		super(id, dataProvider);
		setItemsPerPage(25L);
	}
	
}


