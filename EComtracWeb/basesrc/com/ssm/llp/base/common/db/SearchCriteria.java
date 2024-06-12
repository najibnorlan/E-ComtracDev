/*
 * This software is the confidential and proprietary information of SSM
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with SSM.
 */
package com.ssm.llp.base.common.db;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller will pass this object to Boc and then Dao to search for records. The criteria<BR>
 * can be nested inside a criteria. The program should loop all the search criteria under this<BR>
 * until isNested() return false (which means no more nested inside)Record return must<BR> no
 * exceed the maximum number set at the highest /1st level of  SearchCriteria object<BR>
 *
 * @author hhf
 * @version 1.0
 */
public class SearchCriteria implements Serializable{
    /** Equal to the value */
    public static final String EQUAL = "=";

    /** not equal to the value */
    public static final String NOT_EQUAL = "!=";

    /** Greter then the value supplied */
    public static final String GREATER = ">";

    /** Greter then or equal to the value supplied */
    public static final String GREATER_EQUAL = ">=";

    /** Less then the value supplied */
    public static final String LESS = "<";

    /** Less then or equal to the value supplied */
    public static final String LESS_EQUAL = "<=";

    /** The field has not null value */
    public static final String IS_NOT_NULL = "IS NOT NULL";

    /** The field has null value */
    public static final String IS_NULL = "IS NULL";

    /**
     * Similarity search, the value supplied must be a string <BR> e.g. %myname or %myname%
     * or my?name
     */
    public static final String LIKE = "LIKE";
    
    public static final String NOT_LIKE = "NOT LIKE";

    /** OR join of 2 criteria */
    public static final String OR = "OR";

    /** And join of 2 criteria */
    public static final String AND = "AND";

    /** order asc */
    public static final String ASC = "ASC";

    /** order desc */
    public static final String DESC = "DESC";

    /** between */
    public static final String BETWEEN = "BETWEEN";

    /** IN */
    public static final String IN = "IN";

    /** NOT IN */
    public static final String NOT_IN = "NOT IN";

    /** Deleted record status */
    public static final String DELETE = "D";

    /** Function name for Length */
    public static final String FUNCTION_LENGTH = "length";

    /** Function name for Length */
    public static final String FUNCTION_UPPER = "upper";

    /** Maximum number of record return */

    //private int maxRecord;
    /** Search operator */
    private String operator;

    /** search value */
    private Object value;

    /** 1st search criteria */
    private SearchCriteria searchCriteria1;

    /** 2nd search criteria */
    private SearchCriteria searchCriteria2;

    /** Joint type of the criteria */
    private String jointType = "0";

    /** maximum number of record for the search result */
    private String fieldName;

    /** index of the first record */
    private int startAtIndex;

    /** index of the last record */
    private int endAtIndex;

    /** Object values to be used for between comparing criteria */
    private Object[] values;

    /** Flag to determine whether is numeric */
    private boolean isNumeric;

    /** SQL function */
    private String function;

    /** SQL for value function */
    private String valueFunction;

    /** Order by map */
    private Map orderBy = new LinkedHashMap();

/**
     * Creates a new SearchCriteria object.
     */
    public SearchCriteria() {
    }

/**
     * Creates a new SearchCriteria object based on various search criteria
     *
     * @param fieldname Database field name
     * @param operatorType EQUAL,LESS,LESS_EQUAL,LARGER,LARGER_EQUAL,LIKE
     * @param obj Search criteria value
     */
    public SearchCriteria(String fieldname, String operatorType, Object obj) {
        this.fieldName = fieldname;
        this.operator = operatorType;
        this.value = obj;
    }

/**
     * Creates a new SearchCriteria object based on various search criteria
     *
     * @param fieldname Database field name
     * @param operatorType EQUAL,LESS,LESS_EQUAL,LARGER,LARGER_EQUAL,LIKE
     * @param obj1 Object value to be compared from between criteria.
     * @param obj2 Object value to be compared until between criteria.
     */
    public SearchCriteria(String fieldname, String operatorType, Object obj1, Object obj2) {
        this.fieldName = fieldname;
        this.operator = operatorType;
        this.values = new Object[] { obj1, obj2 };
    }

/**
     * Creates a new SearchCriteria object based on various search criteria
     *
     * @param fieldname Database field name
     * @param operatorType EQUAL,LESS,LESS_EQUAL,LARGER,LARGER_EQUAL,LIKE
     * @param objs Search criteria values
     * @param isNumeric Flag to determine whether is numeric.
     */
    public SearchCriteria(String fieldname, String operatorType, Object[] objs, boolean isNumeric) {
        this.fieldName = fieldname;
        this.operator = operatorType;
        this.values = objs;
        this.isNumeric = isNumeric;
    }

/**
     * Creates a new SearchCriteria object.
     *
     * @param fieldname Database field name
     * @param operatorType IS_NOT_NULL, IS_NULL
     */
    public SearchCriteria(String fieldname, String operatorType) {
        this.fieldName = fieldname;
        this.operator = operatorType;
    }

/**
     * Creates a new SearchCriteria object.
     *
     * @param criteria1 First Search Criteria
     * @param joint AND,OR
     * @param criteria2 Second search criteria
     *
     * @throws IllegalArgumentException Exception occured after illegal argument exception.
     */
    public SearchCriteria(SearchCriteria criteria1, String joint, SearchCriteria criteria2) {
        if ((criteria1 == null) || (criteria2 == null)) {
            throw new IllegalArgumentException("Search Criteria cannot be null");
        }

        this.searchCriteria1 = criteria1;
        this.jointType = joint;
        this.searchCriteria2 = criteria2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Return  1st Search Criteria
     */
    public SearchCriteria getSearchCriteria1() {
        return searchCriteria1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return 2nd Search Criteria
     */
    public SearchCriteria getSearchCriteria2() {
        return searchCriteria2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return true if this is nested, this criteria contains other criteria
     */
    public boolean isNested() {
        return !"0".equals(jointType);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Maximum record return for the search
     */
    public int getMaxRecord() {
        return endAtIndex - startAtIndex;
    }

    /**
     * DOCUMENT ME!
     *
     * @return String - Fieldname
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Object - value
     */
    public Object getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return String - operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return String - join type
     */
    public String getJointType() {
        return jointType;
    }

    /**
     * The last index of the search (inclusive)
     *
     * @return last index
     */
    public int getEndAtIndex() {
        return endAtIndex;
    }

    /**
     * The first index of the search (inclusive)
     *
     * @return first index
     */
    public int getStartAtIndex() {
        return startAtIndex;
    }

    /**
     * set The last index of the search (inclusive)
     *
     * @param i last index
     */
    public void setEndAtIndex(int i) {
        endAtIndex = i;
    }

    /**
     * set the first index
     *
     * @param i first index
     */
    public void setStartAtIndex(int i) {
        startAtIndex = i;
    }

    /**
     * return ASC or DESC
     *
     * @return ASC or DESC
     */
    public String getAsc() {
        for (Iterator iterator = orderBy.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();

            return (String) orderBy.get(key);
        }

        return null;
    }

    /**
     * get the order by field
     *
     * @return the field for the order
     */
    public String getOrderBy() {
        for (Iterator iterator = orderBy.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();

            return key;
        }

        return null;
    }

    /**
     * set teh order by field
     *
     * @param string object field which need the ordering
     * @param asc TODO DOCUMENTTHIS
     */
    public void addOrderBy(String string, String asc) {
        orderBy.put(string, asc);
    }

    /**
     * get the order by field
     *
     * @return the field for the order
     */
    public Map getOrderByMap() {
        return orderBy;
    }

    /**
     * Combine the search Crieteria with this object. Only combile when value is not null
     * and not empty. Else will return this object back<P>combined using as SearchCriteria
     * sc1 = new SearchCriteria(fieldName, operator, value); SearchCriteria sc2 = new
     * SearchCriteria(this, AND, sc1);</p>
     *  <P></p>
     *
     * @param fieldNm Field Name
     * @param opt Comparison operator between the field and the value
     * @param val Object value, can be any database field type
     *
     * @return Same object if value is null or empty, or combined with and AND search Criteria if
     *         not null
     */
    public SearchCriteria andIfNotNull(String fieldNm, String opt, Object val) {
        if (val == null) {
            return this;
        }

        if (val instanceof String) {
            if (val.toString().trim().length() == 0) {
                return this;
            }
        }

        SearchCriteria sc1 = new SearchCriteria(fieldNm, opt, val);
        SearchCriteria sc2 = new SearchCriteria(this, AND, sc1);

        return sc2;
    }

    /**
     * Combine the search Crieteria with this object. Only combile when value is not null
     * and not empty. Else will return this object back<P>combined using as SearchCriteria
     * sc1 = new SearchCriteria(fieldName, operator, value); SearchCriteria sc2 = new
     * SearchCriteria(this, AND, sc1);</p>
     *  Take the SQL function.<P></p>
     *
     * @param function SQL function
     * @param fieldNm Field Name
     * @param opt Comparison operator between the field and the value
     * @param val Object value, can be any database field type
     *
     * @return Same object if value is null or empty, or combined with and AND search Criteria if
     *         not null
     */
    public SearchCriteria andIfNotNull(String function, String fieldNm, String opt, Object val) {
        if (val == null) {
            return this;
        }

        if (val instanceof String) {
            if (val.toString().trim().length() == 0) {
                return this;
            }
        }

        SearchCriteria sc1 = new SearchCriteria(fieldNm, opt, val);
        sc1.setFunction(function);

        SearchCriteria sc2 = new SearchCriteria(this, AND, sc1);

        return sc2;
    }

    /**
     * Used to create search criteria to compare by using between scenario.
     *
     * @param fieldNm Field name to be compared.
     * @param val1 Object value to be compared from between scenario.
     * @param val2 Object value to be compared until between scenario.
     *
     * @return Created search criteria.
     */
    public SearchCriteria andIfBetweenNotNull(String fieldNm, Object val1, Object val2) {
        if ((val1 == null) || (val2 == null)) {
            return this;
        }

        SearchCriteria sc1 = new SearchCriteria(fieldNm, BETWEEN, val1, val2);
        SearchCriteria sc2 = new SearchCriteria(this, AND, sc1);

        return sc2;
    }

    /**
     * Used to create search criteria to compare by using IN (....) scenario.
     *
     * @param fieldNm Field name to be compared.
     * @param vals Object values to be compared from IN (....) scenario.
     * @param isNumeric Flag to determine whether it is numeric values.
     *
     * @return Created search criteria.
     */
    public SearchCriteria andIfInNotNull(String fieldNm, Object[] vals, boolean isNumeric) {
        if ((vals == null) || (vals.length == 0)) {
            return this;
        }

        SearchCriteria sc1 = new SearchCriteria(fieldNm, IN, vals, isNumeric);
        SearchCriteria sc2 = new SearchCriteria(this, AND, sc1);

        return sc2;
    }

    /**
     * Used to create search criteria to compare by using OR NOT IN (....) scenario.
     *
     * @param fieldNm Field name to be compared.
     * @param vals Object values to be compared from IN (....) scenario.
     * @param isNumeric Flag to determine whether it is numeric values.
     *
     * @return Created search criteria.
     */
    public SearchCriteria orIfNotInNotNull(String fieldNm, Object[] vals, boolean isNumeric) {
        if ((vals == null) || (vals.length == 0)) {
            return this;
        }

        SearchCriteria sc1 = new SearchCriteria(fieldNm, NOT_IN, vals, isNumeric);
        SearchCriteria sc2 = new SearchCriteria(this, OR, sc1);

        return sc2;
    }

    /**
     * Used to create search criteria to compare by using OR NOT IN (....) scenario.
     *
     * @param fieldNm Field name to be compared.
     * @param vals Object values to be compared from IN (....) scenario.
     * @param isNumeric Flag to determine whether it is numeric values.
     *
     * @return Created search criteria.
     */
    public SearchCriteria andIfNotInNotNull(String fieldNm, Object[] vals, boolean isNumeric) {
        if ((vals == null) || (vals.length == 0)) {
            return this;
        }

        SearchCriteria sc1 = new SearchCriteria(fieldNm, NOT_IN, vals, isNumeric);
        SearchCriteria sc2 = new SearchCriteria(this, AND, sc1);

        return sc2;
    }

    /**
     * Combine the search Crieteria with this object. Only combile when value is not null
     * and not empty. Else will return this object back<P>combined using as SearchCriteria
     * sc1 = new SearchCriteria(fieldName, LIKE, value.toString() + "%"); SearchCriteria sc2 = new
     * SearchCriteria(this, AND, sc1);</p>
     *  <P></p>
     *
     * @param fieldNm Field Name
     * @param val Value of the field, used in the LIKE 'value%' search. the % will be appended
     *        automatically in this function
     * @param isCaseSensitive TODO DOCUMENTTHIS
     *
     * @return Same object if value is null or empty, or combined with and AND search Criteria if
     *         not null
     */
    public SearchCriteria andLikeIfNotNull(String fieldNm, String val, boolean isCaseSensitive) {
        if (val == null) {
            return this;
        }

        if (val.toString().trim().length() == 0) {
            return this;
        }

        SearchCriteria sc1 = new SearchCriteria(fieldNm, LIKE, val.toString() + "%");

        if (!isCaseSensitive) {
            sc1.setFunction(FUNCTION_UPPER);
            sc1.setValueFunction(FUNCTION_UPPER);
        }

        SearchCriteria sc2 = new SearchCriteria(this, AND, sc1);

        return sc2;
    }

    /**
     * same as object level andLikeIfNotNull. but with the flexibility to create if the
     * value is not null
     *
     * @param sc can be null. If is null and value is not null, a new Search Crieteria will be
     *        return. if is not null, search criteria will be combined.
     * @param fieldName Field Name
     * @param value value Object value, can be any database field type
     *
     * @return can be null, combined search criteria or single search criteria if sc is null
     */
    public static SearchCriteria andLikeIfNotNull(
        SearchCriteria sc, String fieldName, String value) {
        return andLikeIfNotNull(sc, fieldName, value, false);
    }

    /**
     * same as object level andLikeIfNotNull. but with the flexibility to create if the
     * value is not null
     *
     * @param sc can be null. If is null and value is not null, a new Search Crieteria will be
     *        return. if is not null, search criteria will be combined.
     * @param fieldName Field Name
     * @param value value Object value, can be any database field type
     * @param isCaseSensitive set to true if want to check case sensitive
     *
     * @return can be null, combined search criteria or single search criteria if sc is null
     */
    public static SearchCriteria andLikeIfNotNull(
        SearchCriteria sc, String fieldName, String value, boolean isCaseSensitive) {
        if (sc != null) {
            return sc.andLikeIfNotNull(fieldName, value, isCaseSensitive);
        }

        if (value == null) {
            return null;
        }

        if (value.toString().trim().length() == 0) {
            return null;
        }

        SearchCriteria scNew = new SearchCriteria(fieldName, LIKE, value + "%");

        if (!isCaseSensitive) {
            scNew.setFunction(FUNCTION_UPPER);
            scNew.setValueFunction(FUNCTION_UPPER);
        }

        return scNew;
    }

    /**
     * same as object level andIfNotNull. but with the flexibility to create if the value
     * is not null
     *
     * @param sc can be null. If is null and value is not null, a new Search Crieteria will be
     *        return. if is not null, search criteria will be combined.
     * @param fieldName Field Name
     * @param operator Comparison operator between the field and the value
     * @param value value Object value, can be any database field type
     *
     * @return can be null, combined search criteria or single search criteria if sc is null
     */
    public static SearchCriteria andIfNotNull(
        SearchCriteria sc, String fieldName, String operator, Object value) {
        if (sc != null) {
            return sc.andIfNotNull(fieldName, operator, value);
        }

        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            if (value.toString().trim().length() == 0) {
                return null;
            }
        }

        return new SearchCriteria(fieldName, operator, value);
    }

    /**
     * same as object level andIfNotNull. but with the flexibility to create if the value
     * is not null and to set the SQL function
     *
     * @param sc can be null. If is null and value is not null, a new Search Crieteria will be
     *        return. if is not null, search criteria will be combined.
     * @param function SQL function
     * @param fieldName Field Name
     * @param operator Comparison operator between the field and the value
     * @param value value Object value, can be any database field type
     *
     * @return can be null, combined search criteria or single search criteria if sc is null
     */
    public static SearchCriteria andIfNotNull(
        SearchCriteria sc, String function, String fieldName, String operator, Object value) {
        if (sc != null) {
            return sc.andIfNotNull(fieldName, operator, value);
        }

        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            if (value.toString().trim().length() == 0) {
                return null;
            }
        }

        return new SearchCriteria(fieldName, operator, value);
    }

    /**
     * Used to create search criteria by using between scenario with other nexted search
     * criteria.
     *
     * @param sc Nexted search criteria.
     * @param fieldNm Field name to be compared
     * @param val1 Object value to be compared from between scenario.
     * @param val2 Object value to be compared until between scenario.
     *
     * @return Created search criteria.
     */
    public static SearchCriteria andIfBetweenNotNull(
        SearchCriteria sc, String fieldNm, Object val1, Object val2) {
        if (sc != null) {
            return sc.andIfBetweenNotNull(fieldNm, val1, val2);
        }

        if (val1 == null) {
            return null;
        }

        if (val2 == null) {
            return null;
        }

        return new SearchCriteria(fieldNm, BETWEEN, val1, val2);
    }

    /**
     * Used to create search criteria by using IN (....) scenario with other nexted search
     * criteria.
     *
     * @param sc Nexted search criteria.
     * @param fieldNm Field name to be compared
     * @param vals Object values to be compared from IN (....) scenario.
     * @param isNumeric Flag to determine whether is numeric.
     *
     * @return Created search criteria.
     */
    public static SearchCriteria andIfInNotNull(
        SearchCriteria sc, String fieldNm, Object[] vals, boolean isNumeric) {
        if (sc != null) {
            return sc.andIfInNotNull(fieldNm, vals, isNumeric);
        }

        if ((vals == null) || (vals.length == 0)) {
            return null;
        }

        return new SearchCriteria(fieldNm, IN, vals, isNumeric);
    }

    /**
     * Used to create search criteria by using IN (....) scenario with other nexted search
     * criteria.
     *
     * @param sc Nexted search criteria.
     * @param fieldNm Field name to be compared
     * @param vals Object values to be compared from IN (....) scenario.
     * @param isNumeric Flag to determine whether is numeric.
     *
     * @return Created search criteria.
     */
    public static SearchCriteria orIfNotInNotNull(
        SearchCriteria sc, String fieldNm, Object[] vals, boolean isNumeric) {
        if (sc != null) {
            return sc.orIfNotInNotNull(fieldNm, vals, isNumeric);
        }

        if ((vals == null) || (vals.length == 0)) {
            return null;
        }

        return new SearchCriteria(fieldNm, NOT_IN, vals, isNumeric);
    }

    /**
     * Used to create search criteria by using IN (....) scenario with other nexted search
     * criteria.
     *
     * @param sc Nexted search criteria.
     * @param fieldNm Field name to be compared
     * @param vals Object values to be compared from IN (....) scenario.
     * @param isNumeric Flag to determine whether is numeric.
     *
     * @return Created search criteria.
     */
    public static SearchCriteria andIfNotInNotNull(
        SearchCriteria sc, String fieldNm, Object[] vals, boolean isNumeric) {
        if (sc != null) {
            return sc.andIfNotInNotNull(fieldNm, vals, isNumeric);
        }

        if ((vals == null) || (vals.length == 0)) {
            return null;
        }

        return new SearchCriteria(fieldNm, NOT_IN, vals, isNumeric);
    }

    /**
     * Check the fieldname whether it is equal to zero or is null.
     *
     * @param sc SearchCriteria
     * @param fieldName Field name to be compared
     *
     * @return Created search criteria.
     */
    public static SearchCriteria andCheckEmptyOrNull(SearchCriteria sc, String fieldName) {
        SearchCriteria sc1 = new SearchCriteria("locale", SearchCriteria.EQUAL, new Integer(0));
        sc1.setFunction(FUNCTION_LENGTH);

        SearchCriteria sc2 = new SearchCriteria("locale", SearchCriteria.IS_NULL);
        SearchCriteria sc3 = new SearchCriteria(sc1, SearchCriteria.OR, sc2);

        if (sc != null) {
            return new SearchCriteria(sc, SearchCriteria.AND, sc3);
        } else {
            return sc3;
        }
    }

    /**
     * Get object values to be used for between scenario.
     *
     * @return Object values to be used for between scenario.
     */
    public Object[] getValues() {
        return values;
    }

    /**
     * Set object values to be used for between scenario.
     *
     * @param objects Object values to be used for between scenario.
     */
    public void setValues(Object[] objects) {
        values = objects;
    }

    /**
     * Get flag whether determine it is numeric.
     *
     * @return
     */
    public boolean isNumeric() {
        return isNumeric;
    }

    /**
     * Get the SQL function
     *
     * @return Returns the function.
     */
    public String getFunction() {
        return function;
    }

    /**
     * Set the SQL function
     *
     * @param function The function to set.
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * TODO DOCUMENTTHIS
     *
     * @return TODO DOCUMENTTHIS
     */
    public String getValueFunction() {
        return valueFunction;
    }

    /**
     * TODO DOCUMENTTHIS
     *
     * @param valueFunction TODO DOCUMENTME!
     */
    public void setValueFunction(String valueFunction) {
        this.valueFunction = valueFunction;
    }
}
