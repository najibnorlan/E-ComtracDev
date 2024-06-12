package com.ssm.llp.base.page.table;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * detachable model for an instance of contact
 * 
 * @author igor
 * 
 */
@SuppressWarnings("serial")
public class SSMLoadableDetachableModel<T> extends LoadableDetachableModel<T>
{
	private final T t;
    /**
     * @see org.apache.wicket.model.LoadableDetachableModel#load()
     */
    @Override
    protected T load()
    {
        return t;
    }
	
    public SSMLoadableDetachableModel(T t2)
    {
        this.t=t2;
    }
    
    /**
     * used for dataview with ReuseIfModelsEqualStrategy item reuse strategy
     * 
     * @see org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        Class clazz = t.getClass();
    	if (obj == this)
        {
            return true;
        }
        else if (obj == null)
        {
            return false;
        }else if (obj.getClass().getName().equals(clazz.getName()))
        {
            
            return obj.equals(t);
        }
        return false;
    }
}
