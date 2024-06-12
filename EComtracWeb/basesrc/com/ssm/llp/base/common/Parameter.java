package com.ssm.llp.base.common;

import java.io.File;
import java.text.SimpleDateFormat;

public class Parameter {
	
	//CODE TYPE
	/*Login tyoe digunakan untuk login */
	public static String LOGIN_TYPE = "LOGIN_TYPE";
	public static String ID_TYPE = "ID_TYPE";
	public static String ID_TYPE_FOR_CMP_OFFICER = "ID_TYPE_FOR_CMP_OFFICER";
	public static String GENDER = "GENDER";
	public static String NATIONALITY_TYPE = "NATIONALITY_TYPE";
	public static String NATIONALITY_TYPE_FOR_CMP_OFFICER = "NATIONALITY_TYPE_FOR_CMP_OFFICER";
	public static String LLP_STATUS = "LLP_STATUS";
	public static String LLP_STATUS_existing = "E";
	public static String COUNTRY_CODE = "COUNTRY_CODE";
	public static String STATE_CODE = "STATE_CODE";
	public static String PROF_BODY_TYPE = "PROF_BODY_TYPE";
	public static String PROF_BODY_CODE = "PROF_BODY_CODE";
	public static String SALUTATION = "SALUTATION";
	public static String USER_TYPE = "USER_TYPE";
	public static String RACE = "RACE";
	public static String RACE_OTHERS = "O";
	public static String YES_NO = "YES_NO";	
	public static String RESERVE_NAME_STATUS = "RESERVE_NAME_STATUS";
	public static String CONTACT_GROUP = "CONTACT_GROUP";
	public static String USER_STATUS = "USER_STATUS";
	public static String OKU_CATEGORY = "OKU_CATEGORY";
	public static String OKU_REGISTRATION_STATUS = "OKU_REGISTRATION_STATUS";
	public static String OKU_REGISTRATION_STATUS_APPROVE = "A";
	public static String OKU_REGISTRATION_STATUS_PENDING = "P";
	public static String OKU_REGISTRATION_STATUS_QUERY = "Q";
	public static String OKU_REGISTRATION_STATUS_REJECT = "R";
	public static String OKU_REGISTRATION_STATUS_CANCEL = "C";
	public static String OKU_REGISTRATION_STATUS_REVOKE = "B";
	public static String OKU_REGISTRATION_STATUS_WITHDRAW = "W";
	public static String OKU_REGISTRATION_STATUS_CANCELREVOKE = "CB";
	public static String BUSINESS_CODE_STATUS = "BUSINESS_CODE_STATUS";
	public static String PAYMENT_TYPE = "PAYMENT_TYPE";
	public static String LLP_REG_TYPE = "LLP_REG_TYPE";
	public static String PAYMENT_STATUS = "PAYMENT_STATUS";
	public static String CONVERSION_TYPE = "CONVERSION_TYPE";
	public static String EMAIL_STATUS = "EMAIL_STATUS";
	public static String PAYMENT_CONFIG = "PAYMENT_CONFIG";
	public static String LOCALE_TYPE = "LOCALE_TYPE";
	public static final String FILE_STATUS = "FILE_STATUS";
	public static final String LLP_CONFIG = "LLP_CONFIG";
	public static final String WS_URL = "WS_URL";
	public static final String WS_URL_DEFAULT = "DEFAULT";
	public static final String PARAMETER_STATUS = "PARAMETER_STATUS";
	public static final String REG_TRANSACTION_STATUS = "REG_TRANSACTION_STATUS";
	public static final String DEPLOYMENT_TYPE = "DEPLOYMENT_TYPE";
	public static final String SUPPLY_INFO_HDR_STATUS="SUPPLY_INFO_HDR_STATUS";
	public static final String SUPPLY_INFO_DTL_ATTACHMENT_STATUS="SUPPLY_INFO_DTL_ATTACHMENT_STATUS";
	public static final String SUPPLY_INFO_CONFIG="SUPPLY_INFO_CONFIG";
	public static final String BALANCING_STATUS="BALANCING_STATUS";
	public static final String PAYMENT_RECEIPT_ISCANCEL="PAYMENT_RECEIPT_ISCANCEL";
	public static final String INVOICE_RUNNING_NO="INVOICE_RUNNING_NO";
	public static final String COMTRAC_RUNNING_NO ="COMTRAC_RUNNING_NO";
	public static final String COMTRAC_FIELDCODE="COMTRAC";
	public static final String INVOICE_FIELDCODE="EBT";
	public static final String RECEIPT_RUNNING_NO="RECEIPT_RUNNING_NO";
	public static final String RECEIPT_FIELDCODE="EB";
	public static final String ROB_FORM_A_INCENTIVE ="ROB_FORM_A_INCENTIVE";
	public static final String ROB_FORM_A1_INCENTIVE ="ROB_FORM_A1_INCENTIVE";
	public static final String BRANCH_CODE ="BRANCH_CODE";
	public static final String FLOOR_LVL ="FLOOR_LVL";
	public static final String PAYMENT_CONFIG_ALLOW_OTC_PAYMENT ="ALLOW_OTC_PAYMENT";
	public static final String SURVEY_SCORE ="SURVEY_SCORE";
	public static final String MONTH_LIST ="MONTH_LIST";
	public static final String PAYMENT_COUNTER_TYPE ="PAYMENT_COUNTER_TYPE";
	public static final String PAYMENT_CONFIG_IS_CHECKING_STATUS_CN ="IS_CHECKING_STATUS_CN";
	public static final String PAYMENT_CONFIG_ALLOW_INCENTIVE ="ALLOW_INCENTIVE";
	public static final String SEARCH_BY_DATE_PAYMENT ="SEARCH_BY_DATE_PAYMENT";
	public static final String DOWNLOAD_VALIDITY_PERIOD ="DOWNLOAD_VALIDITY_PERIOD";
	public static final String COMTRAC_PAYMENT_CHANNEL ="COMTRAC_PAYMENT_CHANNEL";
	public static final String COMTRAC_REGISTRATION_CHANNEL ="COMTRAC_REGISTRATION_CHANNEL";
	public static final String COMTRAC_FEE_TYPE = "COMTRAC_FEE_TYPE";
	public static String COMTRAC_TRANSACTION_STATUS = "COMTRAC_TRANSACTION_STATUS";
	public static String COMTRAC_FINAL_LISTING_STATUS = "COMTRAC_FINAL_LISTING_STATUS";
	public static String COMTRAC_TRAINING_TYPE = "COMTRAC_TRAINING_TYPE";
	public static String REMARK_ABSENT = "REMARK_ABSENT";
	
	//VALUE
	
	public static final String COMTRAC_DESIGNATION = "COMTRAC_DESIGNATION";
	
	public static Integer YEAR_GAP = 5;
	
	public static Integer MAX_UPLOAD_SIZE = 3145728;
	
	public static String YES_NO_yes = "Y";
	public static String YES_NO_no = "N";
	
	public static String HEALTH_CHECK_ok = "OK";
	public static String HEALTH_CHECK_fail = "FAIL";
	
	public static String COMTRAC_TRANSACTION_STATUS_data_entry = "DE";
	public static String COMTRAC_TRANSACTION_STATUS_pending_payment = "PP";
	public static String COMTRAC_TRANSACTION_STATUS_payment_success = "PS";
	public static String COMTRAC_TRANSACTION_STATUS_cert_generated = "CR";
	public static String COMTRAC_TRANSACTION_STATUS_cancel = "C";
	//new status for LPO
	public static String COMTRAC_TRANSACTION_STATUS_in_process = "IP";
	public static String COMTRAC_TRANSACTION_STATUS_reject = "R";
//	public static String COMTRAC_TRANSACTION_STATUS_approved = "A";

	
	public static String COMTRAC_FINAL_LISTING_STATUS_pending = "P";
	public static String COMTRAC_FINAL_LISTING_STATUS_query = "Q";
	public static String COMTRAC_FINAL_LISTING_STATUS_approved = "A";
	public static String COMTRAC_FINAL_LISTING_STATUS_acknowledge = "AK";
	
	public static String COMTRAC_PAYMENT_CHANNEL_ezbiz = "ezbiz";
	public static String COMTRAC_PAYMENT_CHANNEL_ccs = "ccs";
	public static String COMTRAC_PAYMENT_CHANNEL_bank_transfer = "bt";
	public static String COMTRAC_PAYMENT_CHANNEL_post = "post";
	public static String COMTRAC_PAYMENT_CHANNEL_unpaid = "UNPAID";
	
	public static String COMTRAC_PAYMENT_MODE_CC = "CC";
	public static String COMTRAC_PAYMENT_MODE_DD = "DD";
	
	public static String COMTRAC_REGISTRATION_CHANNEL_ezbiz = "ezbiz";
	public static String COMTRAC_REGISTRATION_CHANNEL_email = "email";
	public static String COMTRAC_REGISTRATION_CHANNEL_fax = "fax";
	public static String COMTRAC_REGISTRATION_CHANNEL_ecomtrac = "ecomtrac";
	
	public static String COMTRAC_FEE_TYPE_standard = "ST";
	public static String COMTRAC_FEE_TYPE_special = "SP";
	public static String COMTRAC_FEE_TYPE_license_sec = "LS";
	public static String COMTRAC_FEE_TYPE_member = "MEMBER";
	public static String COMTRAC_FEE_TYPE_reprint_cert = "REPRINT_CERTIFICATE";
	
	public static String COMTRAC_prefix = "CP-";
	
	public static String SCHEDULER_ITEM_TYPE_app = "app";
	public static String SCHEDULER_ITEM_TYPE_scheduler = "scheduler";
	
	public static String ALERT_TYPE_error = "error";
	public static String ALERT_TYPE_success = "success";
	public static String ALERT_TYPE_alert = "alert";
	public static String ALERT_TYPE_info = "info";
	public static String ALERT_TYPE_warning = "warning";
	
	public static String ROB_FORM_A_INCENTIVE_student ="STU";
	public static String ROB_FORM_A_INCENTIVE_oku ="OKU";
	public static String ROB_FORM_A_INCENTIVE_no ="N";
	public static String ROB_FORM_A1_INCENTIVE_oku ="OKU";
	public static String ROB_FORM_A1_INCENTIVE_no ="N";
	
	public static String BALANCING_STATUS_no = "No";
	public static String BALANCING_STATUS_data_entry = "DE";
	public static String BALANCING_STATUS_new = "N";
	public static String BALANCING_STATUS_approved = "A";
	public static String BALANCING_STATUS_verified = "V";
	public static String BALANCING_STATUS_rejected = "R"; 
	
	public static Integer COLLECTION_COUNTER_IS_OPEN_no = 0;
	public static Integer COLLECTION_COUNTER_IS_OPEN_yes= 1;
	
	public static String PAYMENT_MODE = "PAYMENT_MODE";
	public static String PAYMENT_MODE_CASH = "CASH";
	public static String PAYMENT_MODE_CC = "CREDIT CARD";
	public static String PAYMENT_MODE_CN = "CREDIT NOTE";
	public static String PAYMENT_MODE_fpx = "fpx";
	public static String PAYMENT_MODE_incentive = "INCENTIVE";
	public static String PAYMENT_MODE_OTC_CARD="OTC_CARD";
	public static String PAYMENT_MODE_OTC_CARD_MANUAL="OTC_CARD_MANUAL";
			
	public static String EGHL_PAYMENT_MODE_CC = "CC";
	public static String EGHL_PAYMENT_MODE_DD = "DD";
	
	public static Integer PAYMENT_RECEIPT_ISCANCEL_no = 0;
	public static Integer PAYMENT_RECEIPT_ISCANCEL_yes = 1;
	
	public static String PAYMENT_RECEIPT_IS_OFFICIAL_RECEIPT = "OFFICIAL RECEIPT";
	public static String PAYMENT_RECEIPT_IS_TAX_INVOICE = "TAX INVOICE";
	
	public static String PAYMENT_DETAIL_OTC = "OTC";
	
	public static String LLP_REG_TYPE_local = "L";
	public static String LLP_REG_TYPE_foreign = "F";
	public static String LLP_REG_TYPE_conversion = "C";
	
	public static String LOGIN_TYPE_normal = "NORMAL";
	public static String LOGIN_TYPE_internal = "INTERNAL";
	public static String LOGIN_TYPE_second_level = "SECOND_LEVEL";		
	public static String LOGIN_TYPE_interface = "INTERFACE";	
	public static final String LOGIN_TYPE_mykad = "MYKAD";
	
	public static final String LOGIN_SOURCE_MSIA_BIZ = "MSIA_BIZ";
	public static String RESERVE_NAME_STATUS_approved = "A";
	public static String RESERVE_NAME_STATUS_pending_payment = "P";
	
	public static String LICENSE_SECRETARY_CO="License_Secretary_Co";
	
	public static String USER_STATUS_active = "A";
	public static String USER_STATUS_deactive = "D";	
	public static String USER_STATUS_pending = "P";	
	public static String USER_STATUS_suspend = "S";	
	public static String USER_STATUS_deceased = "DEC";	
	
	public static String USER_STATUS_active_desc = "Active";
	public static String USER_STATUS_deactive_desc = "Deactive";	
	public static String USER_STATUS_pending_desc = "Pending";	
	public static String USER_STATUS_suspend_desc = "Suspended";	
	
	public static String BUSINESS_CODE_STATUS_active = "A";
	public static String BUSINESS_CODE_STATUS_inactive = "I";
	
	public static String USER_TYPE_partner = "PT";
	public static String USER_TYPE_complianceOfficer = "CO";
	public static String USER_TYPE_publicUser = "PU";
	public static String USER_TYPE_specialOfficer = "SO";
	public static String PARTNER_LINK_STATUS = "PARTNER_LINK_STATUS";
	public static String PARTNER_LINK_STATUS_active = "A";
	public static String PARTNER_LINK_STATUS_inactive = "I";
	
	public static String ROC_BLACKLIST_TYPE="ROC_BLACKLIST_TYPE";


	public static String ROC_BLACKLIST_TYPE_company="01";
	public static String ROC_BLACKLIST_TYPE_business="02";
	public static String ROC_BLACKLIST_TYPE_newIc="03";
	public static String ROC_BLACKLIST_TYPE_passport="04";
	public static String ROC_BLACKLIST_TYPE_oldIc="07";
	public static String ROC_BLACKLIST_TYPE_policeid="10";
	public static String ROC_BLACKLIST_TYPE_auditfirm="11";
	public static String ROC_BLACKLIST_TYPE_auditor="12";
	public static String ROC_BLACKLIST_TYPE_lsNumber="08";
	
	public static String ROB_BLACKLIST_TYPE_newIc="03";


	public static String ID_TYPE_newic = "01";
	public static String ID_TYPE_oldic = "02";
	public static String ID_TYPE_army = "03";
	public static String ID_TYPE_police = "04";
	public static String ID_TYPE_passport = "05";

	public static final String GENERAL_NAME = "GN";
	public static final String PROF_BODY_TYPE_cs = "CS";
	public static final String PROF_BODY_TYPE_ca = "CA";
	public static final String PROF_BODY_TYPE_law = "LW";
	
	public static String PAYMENT_RESERVED_NAME = "P1";
	public static String PAYMENT_LLP_REGISTRATION = "P2";
	public static String PAYMENT_LLP_PROFILE = "P3";
	public static String PAYMENT_LLP_CERT = "P4";
	
	public static String PAYMENT_STATUS_CANCEL = "C";
	public static String PAYMENT_STATUS_PENDING = "P";
	public static String PAYMENT_STATUS_FAIL = "F";
	public static String PAYMENT_STATUS_SUCCESS = "S";
	public static String PAYMENT_STATUS_NO_STATUS = "NS";
	public static String PAYMENT_STATUS_ALL = "ALL";
	
	
	public static String PAYMENT_GST_CODE= "PAYMENT_GST_CODE";
	public static String PAYMENT_GST_CODE_SR= "SR";
	public static String PAYMENT_GST_CODE_OS= "OS";
//	public static double PAYMENT_GST_CODE_SR_RATE= 0.06;
//	public static Double PAYMENT_GST_ZERO= 0.0;
	
	public static String NAME_SEARCH_TYPE_PLT = "P";
	public static String NAME_SEARCH_TYPE_OFFENCE = "O";
	public static String NAME_SEARCH_TYPE_REGISTERED_NAME = "E";
	public static String NAME_SEARCH_TYPE_RESERVED_NAME = "R";
	public static String NAME_SEARCH_TYPE_GAZZETED = "G";
	public static String NAME_SEARCH_TYPE_CONTROL = "C";
	
	public static String COUNTRY_CODE_malaysia="MYS";
	
	public static String CONVERSION_TYPE_roc= "ROC";
	public static String CONVERSION_TYPE_rob= "ROB";
	
	public static String CONVERSION_STATUS_ROB="B";
	public static String CONVERSION_STATUS_ROC="B";
	public static String KEYWORD="KEYWORD";
	
	public static String LOCALE_TYPE_default="";
	
	public static String EMAIL_STATUS_success = "S";
	public static String EMAIL_STATUS_pending = "P";
	public static String EMAIL_STATUS_fail = "F";
	
	public static String NRIC_COLOUR = "NRIC_COLOUR";
	public static String NRIC_COLOUR_blue = "B";
	public static String NRIC_COLOUR_red = "R";
	public static String NRIC_COLOUR_other = "O";
	public static String NRIC_COLOUR_none = "N";	
	
	public static final String FILE_STATUS_active = "A";
	public static final String FILE_STATUS_inactive = "I";
	
	public static final String PARAMETER_STATUS_active = "A";
	public static final String PARAMETER_STATUS_inactive = "I";
	
	
//	public static final String LLP_CONFIG_webis_url = "WEBIS_URL";
	public static final String LLP_CONFIG_WEBIS2_URL = "WEBIS2_URL";
	public static final String LLP_CONFIG_deployment_type = "DEPLOYMENT_TYPE";
	public static final String LLP_CONFIG_PG_PAY_TRAN_TYPE = "PG_PAY_TRAN_TYPE";
	public static final String LLP_CONFIG_PG_WS_URL = "PG_WS_URL";
	public static final String LLP_CONFIG_PG_LANDING_PAGE = "PG_LANDING_PAGE";
	public static final String LLP_CONFIG_HIDE_ADS = "HIDE_ADS";
	public static final String LLP_CONFIG_IS_FILTER_USER_FORM_B = "IS_FILTER_USER_FORM_B";
	public static final String LLP_CONFIG_FILTER_USER_LIST_FORM_B = "FILTER_USER_LIST_FORM_B";
	public static final String LLP_CONFIG_IS_FILTER_USER_FORM_C = "IS_FILTER_USER_FORM_C";
	public static final String LLP_CONFIG_IS_FILTER_USER_LIST_NOT_OTC = "IS_FILTER_USER_LIST_NOT_OTC";
	public static final String LLP_CONFIG_FILTER_USER_LIST_FORM_C = "FILTER_USER_LIST_FORM_C";
	public static final String LLP_CONFIG_IS_FILTER_USER_LIST_CMP = "IS_FILTER_USER_LIST_CMP";
	public static final String LLP_CONFIG_FILTER_USER_LIST_CMP = "FILTER_USER_LIST_CMP";
	public static final String LLP_CONFIG_FILTER_USER_LIST_NOT_OTC = "FILTER_USER_LIST_NOT_OTC";
	public static final String LLP_CONFIG_PROXY_URL = "PROXY_URL";
	public static final String LLP_CONFIG_PROXY_PORT = "PROXY_PORT";
	public static final String LLP_CONFIG_RSS_URL = "RSS_URL";
	public static final String LLP_CONFIG_IS_FILTER_MYKAD_IP = "IS_FILTER_MYKAD_IP";
	public static final String LLP_CONFIG_IS_FILTER_USER_LIST_COMTRAC = "IS_FILTER_USER_LIST_COMTRAC";
	public static final String LLP_CONFIG_FILTER_USER_LIST_COMTRAC = "FILTER_USER_LIST_COMTRAC";
	public static final String LLP_CONFIG_FILTER_USER_LIST_FINANCE = "FILTER_USER_LIST_FINANCE";
	public static final String LLP_CONFIG_FILTER_USER_LIST_HOS_COMTRAC = "FILTER_USER_LIST_HOS_COMTRAC";
	public static final String LLP_CONFIG_MYKAD_PWD = "MYKAD_PWD";
	public static final String LLP_CONFIG_TURN_OFF_USER_STATUS_UPDATE = "TURN_OFF_USER_STATUS_UPDATE";
	public static final String LLP_CONFIG_IS_LODGE_BY_IP_CLIENT = "IS_LODGE_BY_IP_CLIENT";
	public static final String LLP_CONFIG_IS_FILTER_USER_LIST_SYS_ADMIN ="IS_FILTER_USER_LIST_SYS_ADMIN";
	public static final String LLP_CONFIG_USER_LIST_SYS_ADMIN ="USER_LIST_SYS_ADMIN";
	public static final String LLP_CONFIG_LIST_SVR_IP_ADDR = "LIST_SVR_IP_ADDR";
	public static final String LLP_CONFIG_IS_OTC_PAYMENT_USING_THREAD = "IS_OTC_PAYMENT_USING_THREAD";
	public static final String LLP_CONFIG_USER_VERIFY_2ND_LAYER ="FILTER_USER_LIST_2ND_LAYER";
	public static final String LLP_CONFIG_LIST_SYS_POWER_USER ="FILTER_SYS_POWER_USER_LIST";
	public static final String LLP_CONFIG_BUSY_VEIL = "BUSY_VEIL";
	public static final String LLP_CONFIG_ALLOW_EXT_EMAIL_LIST="ALLOW_EXT_EMAIL_LIST";
	
	
	public static final String DIETARY_TYPE = "DIETARY_TYPE";
	public static final String LLP_CONFIG_AD_URL = "AD_URL";
	
	public static final String WEBIS_SUCCESS_CODE = "00";
	public static final String REG_TRANSACTION_STATUS_data_entry = "DE";
	public static final String REG_TRANSACTION_STATUS_pending_payment = "PP";
	public static final String REG_TRANSACTION_STATUS_payment_success = "PS";
	public static final String REG_TRANSACTION_STATUS_registered = "RE";
	public static final String REG_TRANSACTION_STATUS_otc = "OTC";
	
	
	
	public static final String DEPLOYMENT_TYPE_development = "DEVELOPMENT";
	public static final String DEPLOYMENT_TYPE_staging = "STAGING";
	public static final String DEPLOYMENT_TYPE_production = "PRODUCTION";
	
	public static final String SUPPLY_INFO_HDR_STATUS_pending_payment = "PP";
	public static final String SUPPLY_INFO_HDR_STATUS_in_process = "IP";
	public static final String SUPPLY_INFO_HDR_STATUS_complete = "C";
	
	public static final String SUPPLY_INFO_DTL_ATTACHMENT_STATUS_not_selected = "NS";
	public static final String SUPPLY_INFO_DTL_ATTACHMENT_STATUS_pending_payment = "PP";
	public static final String SUPPLY_INFO_DTL_ATTACHMENT_STATUS_in_process = "IP";
	public static final String SUPPLY_INFO_DTL_ATTACHMENT_STATUS_complete = "C";
	
	public static final String SUPPLY_INFO_DTL_UPLOAD_TYPE_profile = "PROFILE";
	public static final String SUPPLY_INFO_DTL_UPLOAD_TYPE_cert = "CERT";
	
	public static final String SUPPLY_INFO_CONFIG_expired_days_in="Expired_Days_In";
	public static final String SUPPLY_INFO_CONFIG_bccList = "bcc_list";
	
	
	//ROB
	public static final String ROB_BIZ_TYPE = "ROB_BUSINESS_TYPE";
	public static final String ROB_BIZ_TYPE_TRADE = "T";
	public static final String ROB_BIZ_TYPE_PERSONAL = "P";
	public static final String ROB_RENEWAL_YEAR = "ROB_RENEWAL_YEAR";
	public static String PAYMENT_TYPE_ROB_RENEWAL_PERSONAL = "R1";
	public static String PAYMENT_TYPE_ROB_RENEWAL_TRADE = "R2";
	public static String PAYMENT_TYPE_ROB_RENEWAL_BRANCHES = "R3";
	public static String PAYMENT_TYPE_ROB_RENEWAL_COMPOUND = "R4";
	public static String PAYMENT_TYPE_ROB_FORM_A_PERSONAL = "FORM_A_PERSONAL";
	public static String PAYMENT_TYPE_ROB_FORM_A_TRADE = "FORM_A_TRADE";
	public static String PAYMENT_TYPE_ROB_FORM_A_BRANCHES = "FORM_A_BRANCH";
	public static String PAYMENT_TYPE_ROB_FORM_B = "ROB_FORM_B";
	public static String PAYMENT_TYPE_ROB_FORM_B_BRANCHES = "ROB_FORM_B_BRANCH";
	public static String PAYMENT_TYPE_ROB_FORM_B_CMP = "ROB_FORM_B_CMP";
	public static String PAYMENT_TYPE_BUSINESS_INFO = "BUSINESS_INFO";
	public static final String ROB_AGENCY_ID = "EZBIZ";
	public static final String ROB_AGENCY_BRANCH_CODE = "KL";
	public static final String EZBIZ_BRANCH_CODE = "MB";
	public static final String ROB_RENEWAL_STATUS = "ROB_RENEWAL_STATUS";
	public static final String ROB_RENEWAL_STATUS_DATA_ENTRY = "DE";
	public static final String ROB_RENEWAL_STATUS_OTC = "OTC";
	public static final String ROB_RENEWAL_STATUS_SUCCESS = "S";
	public static final String ROB_RENEWAL_STATUS_FAIL = "F";
	public static final String ROB_RENEWAL_STATUS_CANCEL = "C";
	public static final String ROB_RENEWAL_STATUS_PAYMENT_SUCCESS = "PS";
	public static final String ROB_RENEWAL_STATUS_PENDING_PAYMENT = "PP";
	public static final int ROB_RENEWAL_EXP_DOWNLOAD_DAYS = 14;
	public static final String ROB_A1_FORM_TEMPLATE = "ROB_A1_FORM_TEMPLATE";
	
	//INCENTIVE
	public static String PAYMENT_INCENTIVE_PERSONAL_STUDENT = "INCNTV_PERSONAL_STUD";
	public static String PAYMENT_INCENTIVE_TRADE_STUDENT = "INCNTV_TRADE_STUD";
	public static String PAYMENT_INCENTIVE_PERSONAL_OKU = "INCNTV_PERSONAL_OKU";
	public static String PAYMENT_INCENTIVE_TRADE_OKU = "INCNTV_TRADE_OKU";
	public static String PAYMENT_INCENTIVE_BINFO_STUDENT = "INCNTV_BINFO_STUD";
	public static String PAYMENT_INCENTIVE_BINFO_OKU = "INCNTV_BINFO_OKU";
	
	public static final String UAM_CBS_MENU_TYPE_MY_SSM_ONLINE = "LLP";
	public static final String UAM_CBS_MENU_TYPE_EZBIZ_MAIN = "EZBIZ01";
	
	public static final String GAF_DELIMITER = "|";
	
	
	public static final String GAF_CONFIG = "GAF_CONFIG";
	public static final String GAF_CONFIG_GAF_OP_BAL = "GAF_OP_BAL";
	public static final String GAF_CONFIG_GST_CTRL_ACCOUNT = "GAF_GST_CTRL_ACCOUNT";
	public static final String GAF_CONFIG_GAF_CHART_ACC_CODE_DEBIT = "GAF_CHART_ACC_DEBIT";
	public static final String GAF_CONFIG_CASH_MOVE_ACCOUNT_CODE = "CASH_MOVE_ACCOUNT_CODE";
	public static final String GAF_CONFIG_CASH_MOVE_ACCOUNT_DEBIT = "CASH_MOVE_ACCOUNT_CODE_DB";
	public static final String GAF_CONFIG_CASH_MOVE_ACCOUNT_CREDIT = "CASH_MOVE_ACCOUNT_CODE_CR";
	public static final String GAF_CONFIG_CASH_MOVE_SRC_TYPE = "CASH_MOVE_SRC_TYPE";
	public static final String GAF_CONFIG_CASH_MOVE_TRANS_DESC = "CASH_MOVE_TRANS_DESC";
	public static final String GAF_CONFIG_GAF_COMTRAC_ACC_CODE = "GAF_COMTRAC_ACC_CODE";
	
	public static final String GAF_CONFIG_ACC_CODE_CR = "GAF_CONFIG_ACC_CODE_CR";
	public static final String GAF_CONFIG_ACC_PAY_MODE = "GAF_CONFIG_ACC_PAY_MODE";
	public static final String GAF_CONFIG_ACC_CODE_DEBIT = "GAF_CONFIG_ACC_CODE_DEBIT";

	public static final String ROB_RENEWAL_CONFIG = "ROB_RENEWAL_CONFIG";
	public static final String ROB_RENEWAL_CONFIG_DOWN_CERT_VALID_DAYS = "DOWN_CERT_VALID_DAYS";
	public static final String ROB_RENEWAL_CONFIG_DAYS_ALLOW_RENEW_BEFORE_EXPIRY = "DAYS_ALLOW_RENEW_BEFORE_EXPIRY";
	
	public static final String ENTITY_TYPE = "ENTITY_TYPE";
	public static final String ENTITY_TYPE_ROB = "ROB";
	public static final String ENTITY_TYPE_ROC = "ROC";
	public static final String ENTITY_TYPE_LLP = "LLP";
	
	public static final String ROC_STATUS = "ROC_STATUS";
	

	public static final String NATIONALITY_TYPE_CITIZEN = "1";
	public static final String NATIONALITY_TYPE_PERMANENT_RESIDENT = "2";
	public static final String NATIONALITY_TYPE_TEMP_RESIDENT = "5";
	public static final String NATIONALITY_TYPE_FOREIGNERS = "3";

	public static final String ROB_NAME_TYPE = "ROB_NAME_TYPE";
	public static final String ROB_NAME_TYPE_TRADE = "T";
	public static final String ROB_NAME_TYPE_PERSONAL = "P";
	
	public static final String ROB_OWNER_VERI_STATUS = "ROB_OWNER_VERI_STATUS";
	public static final String ROB_OWNER_VERI_STATUS_PENDING = "PV";
	public static final String ROB_OWNER_VERI_STATUS_VERIFIED = "V";
	public static final String ROB_OWNER_VERI_STATUS_REJECT = "R";
	public static final String ROB_OWNER_VERI_STATUS_PENDING_ADD_UPDATE = "PA";
	public static final String ROB_OWNER_VERI_STATUS_AUTO_VERIFIED_DECEASED = "AVD";
	
	
	public static final String ROB_FORM_A_STATUS = "ROB_FORM_A_STATUS";
	public static final String ROB_FORM_A_STATUS_DATA_ENTRY = "DE";
	public static final String ROB_FORM_A_STATUS_PENDING_PAYMENT = "PP";
	public static final String ROB_FORM_A_STATUS_PENDING_VERIFICATION = "PV";
	public static final String ROB_FORM_A_STATUS_INCENTIVE_QUERY = "IQ";
	public static final String ROB_FORM_A_STATUS_OTC = "OTC";
	public static final String ROB_FORM_A_STATUS_PAYMENT_SUCCESS = "PS";
	public static final String ROB_FORM_A_STATUS_IN_PROCESS = "IP";
	public static final String ROB_FORM_A_STATUS_APPROVED = "A";
	public static final String ROB_FORM_A_STATUS_REJECT = "R";
	public static final String ROB_FORM_A_STATUS_QUERY = "Q";
	public static final String ROB_FORM_A_STATUS_CANCEL = "C";
	public static final String ROB_FORM_A_STATUS_CANCEL_INVALID = "CI";
	
	
	public static final String CBS_ROB_FORM_A1_STATUS = "CBS_ROB_FORM_A1_STATUS";
	public static final String CBS_ROB_FORM_A1_STATUS_VERIFY = "V";
	public static final String CBS_ROB_FORM_A1_STATUS_APPROVED = "L";
	public static final String CBS_ROB_FORM_A1_STATUS_REJECT = "T";
	public static final String CBS_ROB_FORM_A1_STATUS_INVALID = "INVALID";
	
	public static final String CBS_ROB_FORM_A_STATUS = "CBS_ROB_FORM_A_STATUS";
	public static final String CBS_ROB_FORM_A_STATUS_NEW = "B";
	public static final String CBS_ROB_FORM_A_STATUS_VERIFY = "V";
	public static final String CBS_ROB_FORM_A_STATUS_APPROVED = "L";
	public static final String CBS_ROB_FORM_A_STATUS_REJECT = "T";
	public static final String CBS_ROB_FORM_A_STATUS_QUERY = "K";
	public static final String CBS_ROB_FORM_A_STATUS_INVALID = "INVALID";

	public static final String CBS_ROB_FORM_B_STATUS = "CBS_ROB_FORM_B_STATUS";
	public static final String CBS_ROB_FORM_B_STATUS_NEW = "B";
	public static final String CBS_ROB_FORM_B_STATUS_VERIFY = "V";
	public static final String CBS_ROB_FORM_B_STATUS_APPROVED = "L";
	public static final String CBS_ROB_FORM_B_STATUS_REJECT = "T";
	public static final String CBS_ROB_FORM_B_STATUS_QUERY = "K";
	public static final String CBS_ROB_FORM_B_STATUS_INVALID = "INVALID";
	
	public static final String CBS_ROB_STATE = "CBS_ROB_STATE";
	
	public static final String CBS_BUSINESS_STATUS_ACTIVE = "A";
	public static final String CBS_BUSINESS_STATUS_EXPIRED = "L";
	public static final String CBS_BUSINESS_STATUS_TERMINATED = "T";
	public static final String CBS_BUSINESS_STATUS_CONVERSION = "B";
	
	public static final String STATE_CODE_TO_CBS_CODE = "STATE_CODE_TO_CBS_CODE";
	public static final String NATIONALITY_TYPE_CBS_MAPPING = "NATIONALITY_TYPE_CBS_MAPPING";
	
	public static final String ROB_BUSINESS_CODE = "ROB_BUSINESS_CODE";
	public static final String ROB_ALLOW_REG_STATE="ROB_ALLOW_REG_STATE";
	
	public static final String USER_APPROVED_CHANNEL = "USER_APPROVED_CHANNEL";
	public static final String USER_APPROVED_CHANNEL_COUNTER = "COUNTER";
	public static final String USER_APPROVED_CHANNEL_SSMMYKAD = "SSMMYKAD";
	public static final String USER_APPROVED_CHANNEL_SSMMYKADWEB = "SSMMYKADWEB";
	public static final String USER_APPROVED_CHANNEL_SSMKIOSK = "SSMKIOSK";
	public static final String ROB_A_FORM_TEMPLATE = "ROB_A_FORM_TEMPLATE";
	
	public static final String ROB_OWNERSHIP = "ROB_OWNERSHIP";
	public static final String ROB_OWNERSHIP_SOLE = "T";
	public static final String ROB_OWNERSHIP_PARTNERSHIP = "U";
	
	public static final String ROB_FORM_C_REASON="ROB_FORM_C_REASON";
	
	public static final String ROB_FORM_B_STATUS = "ROB_FORM_B_STATUS";
	public static final String ROB_FORM_B_STATUS_DISCARD = "DISCARD";
	public static final String ROB_FORM_B_STATUS_DATA_ENTRY = "DE";
	public static final String ROB_FORM_B_STATUS_QUERY = "Q";
	public static final String ROB_FORM_B_STATUS_PAYMENT_SUCCESS = "PS";
	public static final String ROB_FORM_B_STATUS_PENDING_PAYMENT = "PP";
	public static final String ROB_FORM_B_STATUS_OTC = "OTC";
	public static final String ROB_FORM_B_STATUS_IN_PROCESS = "IP";
	public static final String ROB_FORM_B_STATUS_REJECT = "R";
	public static final String ROB_FORM_B_STATUS_APPROVED = "A";
	public static final String ROB_FORM_B_STATUS_CANCEL = "C";
	public static final String ROB_FORM_B_STATUS_CANCEL_INVALID = "CI";
	
	public static final String ROB_FORM_B_TYPE = "ROB_FORM_B_TYPE";
	public static final String ROB_FORM_B_TYPE_B1 = "B1";
	public static final String ROB_FORM_B_TYPE_B2 = "B2";
	public static final String ROB_FORM_B_TYPE_B3 = "B3";
	public static final String ROB_FORM_B_TYPE_B4 = "B4";
	
	public static final String ROB_OWNER_B_C_VERI_STATUS = "ROB_OWNER_B_C_VERI_STATUS";
	public static final String ROB_OWNER_B_C_VERI_STATUS_PENDING_VERIFICATION = "PV";
	public static final String ROB_OWNER_B_C_VERI_STATUS_VERIFIED = "V";
	public static final String ROB_OWNER_B_C_VERI_STATUS_REJECT = "R";
	public static final String ROB_OWNER_B_C_VERI_STATUS_PENDING_ADD_UPDATE = "PA";
	public static final String ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_ACTIVATION = "PEA";
	public static final String ROB_OWNER_B_C_VERI_STATUS_PENDING_EZBIZ_REGISTER = "PER";
	public static final String ROB_OWNER_B_C_VERI_STATUS_PENDING="P";
	public static final String ROB_OWNER_B_C_VERI_STATUS_AUTO_VERIFIED_DECEASED = "AVD";
	
	public static String PAYMENT_TYPE_ROB_FORM_C_COMPOUND = "FORM_C_CMP";
	
	public static final String CBS_ROB_FORM_C_STATUS = "CBS_ROB_FORM_C_STATUS";
	public static final String CBS_ROB_FORM_C_STATUS_VERIFY = "B";
	public static final String CBS_ROB_FORM_C_STATUS_APPROVED = "L";
	public static final String CBS_ROB_FORM_C_STATUS_REJECT = "T";
	public static final String CBS_ROB_FORM_C_STATUS_QUERY = "K";
	public static final String CBS_ROB_FORM_C_STATUS_INVALID = "INVALID";
	
	
	public static final String ROB_FORM_C_STATUS = "ROB_FORM_C_STATUS";
	public static final String ROB_FORM_C_STATUS_DATA_ENTRY = "DE";
	public static final String ROB_FORM_C_STATUS_SUCCESS = "S";
	public static final String ROB_FORM_C_STATUS_FAIL = "F";
	public static final String ROB_FORM_C_STATUS_QUERY = "Q";
	public static final String ROB_FORM_C_STATUS_IN_PROCESS = "IP";
	public static final String ROB_FORM_C_STATUS_APPROVED = "A";
	public static final String ROB_FORM_C_STATUS_REJECT = "R";
	public static final String ROB_FORM_C_STATUS_PAYMENT_SUCCESS = "PS";
	public static final String ROB_FORM_C_STATUS_PENDING_PAYMENT = "PP";
	public static final String ROB_FORM_C_STATUS_OTC = "OTC";
	public static final String ROB_FORM_C_STATUS_WITHOUT_PAYMENT = "PW";
	public static final String ROB_FORM_C_STATUS_CANCEL = "C";
	public static final String ROB_FORM_C_STATUS_CANCEL_INVALID = "CI";
	public static final String ROB_FORM_C_STATUS_DISCARD = "DISCARD";
	//COMPOUND

	public static final String COMPOUND = "COMPOUND";
	public static final String COMPOUND_TYPE_ROB = "ROB";
	public static final String COMPOUND_TYPE_ROC = "ROC";
	public static final String CMP_STATUS_PAID = "07";
	public static final String CMP_PAYMENT_DATA_ENTRY = "DE";
	public static final String CMP_PAYMENT_PENDING_PAYMENT = "PP";
	public static final String CMP_PAYMENT_OTC = "OTC";
	public static final String CMP_PAYMENT_PAYMENT_SUCCESS = "PS";
	public static final String CMP_PAYMENT_WITHOUT_PAYMENT = "PW";
	public static final String CMP_PAYMENT_CMP_CBS_PAID = "S";
	public static final String PAYMENT_TYPE_COMPOUND_ROB = "CMP_ROB";
	public static final String PAYMENT_TYPE_COMPOUND_ROC = "CMP_ROC";
	public static final String CMP_STATUS = "CMP_STATUS";
	public static final String CMP_ENTITY_TYPE = "CMP_ENTITY_TYPE";
	public static final String CMP_TRANSACTION_STATUS = "CMP_TRANSACTION_STATUS";
	
	//B3_AMENDMENT_TYPE
	public static final String ROB_FORM_B3_AMENDMENT_TYPE = "B3_AMENDMENT_TYPE";
	public static final String ROB_FORM_B3_AMENDMENT_TYPE_NEW = "B";
	public static final String ROB_FORM_B3_AMENDMENT_TYPE_CLOSE = "T";
	public static final String ROB_FORM_B3_AMENDMENT_TYPE_NO_CHANGES = "N";
	
	
	//B2_AMENDMENT_TYPE
	public static final String ROB_FORM_B2_AMENDMENT_TYPE = "B2_AMENDMENT_TYPE";
	public static final String ROB_FORM_B2_AMENDMENT_TYPE_NEW = "B";
	public static final String ROB_FORM_B2_AMENDMENT_TYPE_REMOVE = "R";
	public static final String ROB_FORM_B2_AMENDMENT_TYPE_NO_CHANGES = "N";
	//public static final String ROB_FORM_C_REASON_B="B";
	//public static final String ROB_FORM_C_REASON_F="F";
	public static final String ROB_FORM_C_REASON_H="H";
	//public static final String ROB_FORM_C_REASON_L="L";
	//public static final String ROB_FORM_C_REASON_M="M";
	//public static final String ROB_FORM_C_REASON_P="P";
	//public static final String ROB_FORM_C_REASON_R="R";
	
	
	public static final String ROB_FORM_C_CMP_FORM_CODE_C="C";
	
	//ROB_FORM_TYPE
	public static final String ROB_FORM_TYPE = "ROB_FORM_TYPE";
	public static final String ROB_FORM_TYPE_A = "A";
	public static final String ROB_FORM_TYPE_RENEWAL = "A1";
	public static final String ROB_FORM_TYPE_B = "B";
	public static final String ROB_FORM_TYPE_C = "C";
	public static final String ROB_FORM_TYPE_CMP = "CMP";
	public static final String ROB_FORM_TYPE_USER_ACTIVATION = "UA";
	
	//ROB_OWNER_VER_EMAIL_STATUS
	public static final String ROB_OWNER_VER_EMAIL_STATUS = "ROB_OWNER_VER_EMAIL_STATUS";
	public static final String ROB_OWNER_VER_EMAIL_STATUS_SEND = "S";
	public static final String ROB_OWNER_VER_EMAIL_STATUS_UNSEND = "F";
	public static final String ROB_OWNER_VER_EMAIL_STATUS_NOT_REQ = "NR";
	
	
	public static final String ROB_C_FORM_TEMPLATE = "ROB_C_FORM_TEMPLATE";
	
	public static final String ROB_FORM_C_CMP = "ROB_FORM_C_CMP";
	
	public static final String EGHL_CONFIG = "EGHL_CONFIG";
	public static final String EGHL_CONFIG_SERVICE_ID = "SERVICE_ID";
	public static final String EGHL_CONFIG_MERCH_NAME = "MERCH_NAME";
	public static final String EGHL_CONFIG_RETURN_URL = "RETURN_URL";
	public static final String EGHL_CONFIG_RETURN_URL_COMTRAC = "COMTRAC_RETURN_URL";
	public static final String EGHL_CONFIG_HASH_ID = "HASH_ID";
	public static final String EGHL_CONFIG_PG_URL = "PG_URL";
	
	public static final String MYKAD_CONFIG_IP = "MYKAD_CONFIG_IP";
	public static final String MY_KAD_STATE_MAPPING = "MY_KAD_STATE_MAPPING";
	
	public static final String URL_TYPE_dev = "ezbizdev.ssm.com.my";
	public static final String URL_TYPE_dev_comtrac = "e-comtracdev.ssm.com.my";
	public static final String URL_TYPE_stg = "ezbizstg.ssm.com.my";
	public static final String URL_TYPE_local = "localhost";
	public static final String IP_SEGMENT_BY_BRANCH = "IP_SEGMENT_BY_BRANCH";
	public static final String LLP_CONFIG_IS_PRINT_DETAIL_SCHEDULER = "IS_PRINT_DETAIL_SCHEDULER";
	public static final String COUNTRY_CODE_ESERVICES_MAPPING = "COUNTRY_CODE_ESERVICES_MAPPING";
	public static final String LLP_CONFIG_GET_IP_BY_JS = "GET_IP_BY_JS";
	public static final String LLP_CONFIG_NS_ALLOWED_SYMBOL="NS_ALLOWED_SYMBOL";
	public static final String POSTCODE_MAPPING = "POSTCODE_MAPPING";
	public static final String NS_NOT_ALLOW_PREFIX = "NS_NOT_ALLOW_PREFIX";
	public static final String NS_NOT_ALLOW_POSTFIX = "NS_NOT_ALLOW_POSTFIX";
	public static final String LLP_CONFIG_FORM_A_INTERNET_CODE = "FORM_A_INTERNET_CODE";
	public static final String LLP_CONFIG_CANCEL_A_DRAFT_DAYS = "CANCEL_A_DRAFT_DAYS";
	public static final String LLP_CONFIG_CANCEL_A_PENDING_PAYMENT_DAYS = "CANCEL_A_PENDING_PAYMENT_DAYS";
	public static final String DISTRIBUTION_CONFIG = "DISTRIBUTION_CONFIG";
	public static final String LLP_CONFIG_HQ_POOL = "HQ_POOL";
	
	public static String ESERVICE_TO_EZBIZ_COUNTRY_CODE = "ESERVICE_TO_EZBIZ_COUNTRY_CODE";
	public static String GST_CODE_TYPE = "GST_CODE_TYPE";
	public static String BRANCH_CONTACT = "BRANCH_CONTACT";
	
	public static final String GAF_CONFIG_SALES_SR_MAPPING_IF_GST_0 ="SALES_SR_MAPPING_IF_GST_0";
	public static final String GAF_CONFIG_LEDGER_SR_MAPPING_IF_GST_0="LEDGER_SR_MAPPING_IF_GST_0";
	public static final String LLP_CONFIG_MYDATA_URL = "MYDATA_URL";
	public static final String LLP_CONFIG_MYDATA_CALLBACK_URL = "MYDATA_CALLBACK_URL";
	public static final String LLP_CONFIG_ENABLE_CTC_MYDATA = "ENABLE_CTC_MYDATA";
	public static final String LLP_CONFIG_IS_ALLOW_USE_SAME_PASS = "IS_ALLOW_USE_SAME_PASS";
	public static final String PAYMENT_GROUP = "PAYMENT_GROUP";
	public static final String PAYMENT_GROUP_REFNO_PREFIX = "PAYMENT_GROUP_REFNO_PREFIX";
	public static final String IS_ALLOW_CREDIT_CARD = "IS_ALLOW_CREDIT_CARD";
	public static final String PAYMENT_CONFIG_EMV_DEVICE_NAME = "EMV_DEVICE_NAME";
	public static final String PAYMENT_MODE_SEARCH = "PAYMENT_MODE_SEARCH";
	public static final String PAYMENT_MODE_SEARCH_MAPPING = "PAYMENT_MODE_SEARCH_MAPPING";
	
	
	
	
	public static String AUD_CLASSNAME = "AUD_CLASSNAME";
	public static String LLP_CONFIG_ENABLE_DASHBOARD = "ENABLE_DASHBOARD";
	
	public static String LLP_CONFIG_DATE_SST_RECEIPT_FORMAT = "DATE_SST_RECEIPT_FORMAT";
	
	public static String PAYMENT_CARD_TYPE = "PAYMENT_CARD_TYPE";
	public static String PAYMENT_CARD_BANK = "PAYMENT_CARD_BANK";
	
	public static String SUPPLY_INFO_PROD_LOCALE = "SUPPLY_INFO_PROD_LOCALE";
	public static String SUPPLY_INFO_PROD_LOCALE_BOTH = "BOTH";
	public static String SUPPLY_INFO_PROD_LOCALE_ENG = "ENG";
	public static String SUPPLY_INFO_PROD_LOCALE_BM = "BM";
	public static String SUPPLY_INFO_PROD_TYPE_PERSONAL = "SUPPLY_INFO_PROD_TYPE_PERSONAL";
	
	public static String SUPPLY_INFO_TRANS_STATUS = "SUPPLY_INFO_TRANS_STATUS";
	public static String SUPPLY_INFO_TRANS_STATUS_DATA_ENTRY = "DE";
	public static String SUPPLY_INFO_TRANS_STATUS_PENDING_PAYMENT = "PP";
	public static String SUPPLY_INFO_TRANS_STATUS_PAYMENT_SUCCESS = "PS";
	public static String SUPPLY_INFO_TRANS_STATUS_SUCCESS = "S";
	public static String SUPPLY_INFO_TRANS_STATUS_OTC = "OTC";
	
	
	
	//MAMPU
	public static final String EXT_CONFIG_MAMPU = "EXT_CONFIG_MAMPU";
	public static final String EXT_CONFIG_MAMPU_PAIR_REDIRECT_URL = "PAIR_REDIRECT_URL";
	public static final String EXT_CONFIG_MAMPU_RESET_PAIR_REDIRECT_URL = "RESET_PAIR_REDIRECT_URL";
	public static final String EXT_CONFIG_ENABLE_IFACE = "ENABLE_IFACE";
	
	public static final String EXT_USER_PAIRING_STATUS = "EXT_USER_PAIRING_STATUS";
	public static final String EXT_USER_PAIRING_STATUS_ACTIVE = "A";
	public static final String EXT_USER_PAIRING_STATUS_RESET_BY_USER = "RBU";
	public static final String EXT_USER_PAIRING_STATUS_RESET_BY_OFFICER = "RBO";
	
	
	public static final String MYKAD_THUMBPRINT_STATUS="MYKAD_THUMBPRINT_STATUS";
	public static final String MYKAD_THUMBPRINT_STATUS_VERIFIED="Y";
	public static final String MYKAD_THUMBPRINT_STATUS_FAILED="N";
	
	

	//B4_AMENDMENT_TYPE
	
	public static final String ROB_FORM_B4_AMMENDMENT_TYPE_ALL = "B4_AMMENDMENT_TYPE_ALL";
	public static final String ROB_FORM_B4_AMMENDMENT_TYPE = "B4_AMMENDMENT_TYPE";
	public static final String ROB_FORM_B4_AMMENDMENT_TYPE_NEW = "NEW";
	public static final String ROB_FORM_B4_AMMENDMENT_TYPE_EDITED = "EDITED";
	public static final String ROB_FORM_B4_AMMENDMENT_TYPE_NO_CHANGES = "NO_CHANGE";
	public static final String ROB_FORM_B4_AMMENDMENT_TYPE_PULLOUT = "PULL-OUT";
	public static final String ROB_FORM_B4_AMMENDMENT_TYPE_DECEASED = "DECEASED";
	
	
	public static final String ACTIVATION_TRAY_STATUS = "ACTIVATION_TRAY_STATUS";
	public static final String ACTIVATION_TRAY_STATUS_IN_PROCESS = "IP";
	public static final String ACTIVATION_TRAY_STATUS_IN_PROCESS_RESUBMISSION = "RS";
	public static final String ACTIVATION_TRAY_STATUS_QUERY = "Q";
	public static final String ACTIVATION_TRAY_STATUS_APPROVED = "A";
	public static final String ACTIVATION_TRAY_STATUS_REJECT = "R";
	public static final String ACTIVATION_TRAY_STATUS_DISCARD = "D";
	public static final String LLP_CONFIG_IS_USER_ACTIVATION_NOTICE_UP = "IS_USER_ACTIVATION_NOTICE_UP";
	
	public static final String USER_ACTIVATION_NOTICE_PROCESS_QUATA = "UAT_QUOTA";
	
	
	public static final String MW_ACCESS = "MW_ACCESS";
	public static final String MW_ACCESS_USER_NAME = "MW_ACCESS_USER_NAME";
	public static final String MW_ACCESS_PWD = "MW_ACCESS_PWD";
	

	public static final String MW_URL = "MW_URL";
	public static final String MW_URL_METHOD_QR_GENERATE = "METHOD_QR_GENERATE";
	public static final String LLP_CONFIG_SHOW_IMAGE_VIEWER = "SHOW_IMAGE_VIEWER";
	public static final String ECOMTRAC_CERTIFICATE = "ECOMTRAC_CERTIFICATE";
	public static final String COMTRAC_CERT_EXPIRED = "COMTRAC_CERT_EXPIRED";
	public static final String ECOMTRAC_CERTIFICATE_SSMNC = "ECOMTRAC_CERTIFICATE_SSMNC";
	
	
	public static String LLP_CONFIG_CHECK_CLASH_TRAINING_COMTRAC = "CHECK_CLASH_TRAINING_COMTRAC";
	
	public static String LLP_CONFIG_ENABLE_USER_TNC_COMTRAC = "ENABLE_USER_TNC_COMTRAC";
	public static String TNC_TYPE_USER_COMTRAC = "USER_COMTRAC";
	public static String TNC_IS_AGREE_yes = "Y";
	public static String TNC_IS_AGREE_no = "N";
	public static String TNC_AGREEMENT_STATUS_active = "ACTIVE";
	public static String TNC_AGREEMENT_STATUS_expired = "EXPIRED";
	
	public static final String DATEFORMATSHORT = "dd/MM/yyyy";
	public static final SimpleDateFormat SDF = new SimpleDateFormat(DATEFORMATSHORT);
	
}



