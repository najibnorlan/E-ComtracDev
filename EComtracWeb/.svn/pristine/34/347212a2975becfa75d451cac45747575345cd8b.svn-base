/*
 * This software is the confidential and proprietary information of SSM
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with SSM.
 */
package com.ssm.llp.base.common.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Search Record which will be return by the search
 *
 * @author hhf
 * @version 1.0
 */
public class SearchResult {
    /**
     * Array containnig the search result. specified by the searchCriteria e.g. max record, start
     * and end index
     */
    private List list;

    /** Total record found, */
    private int totalRecordCount;

    /**
     * Creates a new SearchResult object.
     */
    public SearchResult() {
        list = new ArrayList();
        totalRecordCount = list.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return result from the search criteria
     */
    public List getList() {
        return list;
    }

    /**
     * DOCUMENT ME!
     *
     * @return total record count
     */
    public int getTotalRecordCount() {
        return totalRecordCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param l set search result
     */
    public void setList(List l) {
        this.list = l;

        if (l != null) {
            totalRecordCount = this.list.size();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i record total for the search criteria
     */
    public void setTotalRecordCount(int i) {
        totalRecordCount = i;
    }
}
