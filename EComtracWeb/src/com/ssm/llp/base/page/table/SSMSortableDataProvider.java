package com.ssm.llp.base.page.table;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.page.WicketApplication;


public class SSMSortableDataProvider<T, S> extends SortableDataProvider<T, S>{
	private int sizeResult; 
	private SearchCriteria sc ;
	private String serviceId;
	
	public SSMSortableDataProvider(String sortBy, SearchCriteria sc, Class<BaseService> serviceClass) {
		setSort((S)sortBy, SortOrder.ASCENDING);
		this.sc = sc;
//		baseService = WicketApplication.getServiceNew(serviceId);
		serviceId = serviceClass.getSimpleName();
		if(sc==null){
			sizeResult = 0;
		}else{
			sizeResult = WicketApplication.getServiceNew(serviceId).findByCriteriaCount(sc).intValue();
		}
	}
	
	public SSMSortableDataProvider(String sortBy, SortOrder sortOrder, SearchCriteria sc, Class<BaseService> serviceClass) {
		setSort((S)sortBy, sortOrder);
		this.sc = sc;
//		baseService = WicketApplication.getServiceNew(serviceId);
		serviceId = serviceClass.getSimpleName();
		if(sc==null){
			sizeResult = 0;
		}else{
			sizeResult = WicketApplication.getServiceNew(serviceId).findByCriteriaCount(sc).intValue();
		}
	}
	
	
	@Override
	public Iterator<? extends T> iterator(long first, long count) {
		sc.setStartAtIndex(new Long(first).intValue());
    	String asc = SearchCriteria.ASC;
    	if(!getSort().isAscending()){
    		asc  = SearchCriteria.DESC;
    	}
    	sc.addOrderBy((String) getSort().getProperty(), asc);
    	sc.setEndAtIndex(new Long(first+count).intValue());
    	
    	
    	List listResult = WicketApplication.getServiceNew(serviceId).findByCriteria(sc).getList();
    	
    	return listResult.iterator();
	}

	@Override
	public IModel<T> model(T arg0) {
		return new SSMLoadableDetachableModel<T>(arg0);
	}

	@Override
	public long size() {
		return sizeResult;
	}


	public void setSc(SearchCriteria sc) {
		this.sc = sc;
		if(sc==null){
			sizeResult = 0;
		}else{
			sizeResult = WicketApplication.getServiceNew(serviceId).findByCriteriaCount(sc).intValue();
		}
	}
}
