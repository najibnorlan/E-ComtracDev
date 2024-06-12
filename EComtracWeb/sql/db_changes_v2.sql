-- 15/03/2017
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("C", "CANCEL", "PAYMENT_STATUS", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("14", "Days", "DOWNLOAD_VALIDITY_PERIOD", "A", current, "TRAFIDAH", current, "TRAFIDAH");

-- 17/03/2017
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("SEARCH_BY_DATE_PAYMENT", "Y", "GENERAL_CONFIG", "A", current, "RIZAL", current, "RIZAL");

--update rob_parameters set code = "ALLOW_INCENTIVE", code_desc = "Y", code_type = "GENERAL_CONFIG" where code_type = "ALLOW_INCENTIVE";
--update rob_parameters set code = "IS_CHECKING_STATUS_CN", code_desc = "Y", code_type = "GENERAL_CONFIG" where code_type = "IS_CHECKING_STATUS_CN";
--update rob_parameters set code = "ALLOW_OTC_PAYMENT", code_desc = "Y", code_type = "GENERAL_CONFIG" where code_type = "ALLOW_OTC_PAYMENT";
--update rob_parameters set code = "DOWNLOAD_VALIDITY_PERIOD", code_desc = "14", code_type = "GENERAL_CONFIG" where code_type = "DOWNLOAD_VALIDITY_PERIOD";

--20/03/2017
update rob_parameters set code = "ALLOW_INCENTIVE", code_desc = "N", code_type = "PAYMENT_CONFIG" where code = "ALLOW_INCENTIVE";
update rob_parameters set code = "IS_CHECKING_STATUS_CN", code_desc = "Y", code_type = "PAYMENT_CONFIG" where code = "IS_CHECKING_STATUS_CN";
update rob_parameters set code = "ALLOW_OTC_PAYMENT", code_desc = "Y", code_type = "PAYMENT_CONFIG" where code = "ALLOW_OTC_PAYMENT";
update rob_parameters set code = "DOWNLOAD_VALIDITY_PERIOD", code_desc = "14", code_type = "LLP_CONFIG" where code = "DOWNLOAD_VALIDITY_PERIOD";