package com.ssm.llp.base.page.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.page.WicketApplication;


public class SSMSessionSortableDataProvider<T, S> extends SortableDataProvider<T, S>{
	private List<T> listResult;
	private int sizeResult;
	
	public SSMSessionSortableDataProvider(String sortBy, List<T> listResult) {
		if(listResult==null){
			this.sizeResult = 0;
			this.listResult = new ArrayList();
		}else{
			this.sizeResult = listResult.size();
			this.listResult = listResult;
		}
	}
	
	public SSMSessionSortableDataProvider(List<T> listResult) {
		if(listResult==null){
			this.sizeResult = 0;
			this.listResult = new ArrayList();
		}else{
			this.sizeResult = listResult.size();
			this.listResult = listResult;
		}
	}
	
	
	@Override
	public Iterator<? extends T> iterator(long first, long count) {
		List<T> listNew = new ArrayList<T>();
    	if(listResult.size()>0){
	    	if(first>=sizeResult){
	    		first=sizeResult;
	    	}
	    	
	    	long last = first+count;
	    	if(last>=sizeResult){
	    		last=sizeResult;
	    	}
	    	
	    	for (int i = (int)first; i < (int)last; i++) {
				listNew.add(listResult.get(i));
			}
    	}
    	
    	return listNew.iterator();
	}

	@Override
	public IModel<T> model(T arg0) {
		return new SSMLoadableDetachableModel<T>(arg0);
	}

	@Override
	public long size() {
		return sizeResult;
	}
	
	public void resetView(List<T> listResult) {
		this.sizeResult = listResult.size();
        this.listResult = listResult;
	}

	public List<T> getListResult() {
		return listResult;
	}
}
