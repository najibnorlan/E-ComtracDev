package com.ssm.llp.base.common.db;

/*
 * This software is the confidential and proprietary information of SSM
 * (&quot;Confidential Information&quot;).
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with SSM.
 */

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchCriteria which support multiple class/table<br>
 * <br>
 * Example: <br>
 * <code> SearchCriteria sc = new SearchCriteria("secUser.userlvl", SearchCriteria.EQUAL,
 * "LEVEL1"); sc.setOrderBy("secUser.loginName");  MultiEntitySearchCriteria meSc = new
 * MultiEntitySearchCriteria(); meSc.addReturnField("secUser");
 * meSc.setDistinctReturnFields(true); meSc.setMainClassName(SecGrpMember.class.getName());
 * meSc.setMainAliasName("secgrpMember"); meSc.addAssociatedClass("secgrpMember.secUser",
 * MultiEntitySearchCriteria.ENTITY_JOIN_TYPE_RIGHT, "secUser"); meSc.setSearchCriteria(sc);  </code><br>
 * <br>
 * Between mainClass and associatedClass, there must be an hibernate association defined. <br>
 * SearchResult content will depends on the returnFields. If the returnFields had more than 1
 * field, the result return will be a list of Object[].
 *
 * @author llk
 * @version 1.0
 */
public class MultiEntitySearchCriteria {
    /** Constant for left Join type */
    public static final String ENTITY_JOIN_TYPE_LEFT = "LEFT JOIN";

    /** Constant for right join Type */
    public static final String ENTITY_JOIN_TYPE_RIGHT = "RIGHT JOIN";

    /** Constant for inner join type */
    public static final String ENTITY_JOIN_TYPE_INNER = "JOIN";

    /** Main Class Name, either full or abbreviate */
    private String mainClassName;

    /** Alias for the main class */
    private String mainAliasName;

    /** List of associated class object */
    private List associatedClasses;

    /** List of fields to be returned. Can be actual field or or whole object */
    private List returnFields;

    /** where to select tby distinct or not */
    private boolean distinctReturnFields = false;

    /** Search criteria */
    private SearchCriteria searchCriteria;

    /**
     * Getter
     *
     * @return mainClassName
     */
    public String getMainClassName() {
        return mainClassName;
    }

    /**
     * Setter
     *
     * @param mainClassName mainClassName
     */
    public void setMainClassName(String mainClassName) {
        this.mainClassName = mainClassName;
    }

    /**
     * Getter
     *
     * @return mainAliasName
     */
    public String getMainAliasName() {
        return mainAliasName;
    }

    /**
     * Setter
     *
     * @param mainAliasName mainAliasName
     */
    public void setMainAliasName(String mainAliasName) {
        this.mainAliasName = mainAliasName;
    }

    /**
     * Getter
     *
     * @return associatedClasses
     */
    public List getAssociatedClasses() {
        return associatedClasses;
    }

    /**
     * Add Associated class
     *
     * @param className Associated Class Name. Should be the property name as defined under main
     *        class.
     * @param joinType association type
     * @param aliasName Alias name for the associated class
     */
    public void addAssociatedClass(String className, String joinType, String aliasName) {
        if (this.associatedClasses == null) {
            this.associatedClasses = new ArrayList();
        }

        AssociatedClass associatedClass = new AssociatedClass(className, joinType, aliasName);
        this.associatedClasses.add(associatedClass);
    }

    /**
     * Getter
     *
     * @return associatedClasses
     */
    public String getAssociatedClassesAsString() {
        if ((this.associatedClasses != null) && !this.associatedClasses.isEmpty()) {
            return StringUtils.join(this.associatedClasses.iterator(), " ");
        }

        return "";
    }

    /**
     * Setter
     *
     * @param associatedClasses associatedClasses
     */
    public void setAssociatedClasses(List associatedClasses) {
        this.associatedClasses = associatedClasses;
    }

    /**
     * getter
     *
     * @return returnFields
     */
    public List getReturnFields() {
        return returnFields;
    }

    /**
     * Setter
     *
     * @param returnFields returnFields
     */
    public void setReturnFields(List returnFields) {
        this.returnFields = returnFields;
    }

    /**
     * Add field to be return as SearchResult. Can be a actual field in a object or the
     * whole object
     *
     * @param returnField returnField
     */
    public void addReturnField(String returnField) {
        if (this.returnFields == null) {
            this.returnFields = new ArrayList();
        }

        this.returnFields.add(returnField);
    }

    /**
     * Return distinctReturnFields
     *
     * @return distinctReturnFields
     */
    public boolean isDistinctReturnFields() {
        return distinctReturnFields;
    }

    /**
     * Setter
     *
     * @param distinctReturnFields distinctReturnFields
     */
    public void setDistinctReturnFields(boolean distinctReturnFields) {
        this.distinctReturnFields = distinctReturnFields;
    }

    /**
     * Getter
     *
     * @return searchCriteria
     */
    public SearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * Setter
     *
     * @param searchCriteria searchCriteria
     */
    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    /**
     * Class to keep Associated Class, its alias name and association type
     *
     * @author llk
     * @version 1.0
     */
    private class AssociatedClass {
        /** Associated Class Name. Should be the property name as defined under main class */
        private String className;

        /** Join Type */
        private String joinType;

        /** Alias Name */
        private String aliasName;

/**
         * Creates a new AssociatedClass object.
         *
         * @param className Associated Class Name. Should be the property name as defined under main class
         * @param joinType join Type
         * @param aliasName Alias Name
         */
        public AssociatedClass(String className, String joinType, String aliasName) {
            this.className = className;
            this.joinType = joinType;
            this.aliasName = aliasName;
        }

        /**
         * 
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return this.joinType + " " + this.className + " " + this.aliasName;
        }
    }
}
