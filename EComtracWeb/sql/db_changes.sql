-- change PAY to PAY ONLINE
update ezbiz:rob_locale_message
set msg = "PAY ONLINE"
where key = "paymentdetails.page.payButton";

-- create rob_counter_collection table
CREATE TABLE ezbiz:informix.rob_counter_collection (
	ip_address VARCHAR(50) NOT NULL,
	floor_level VARCHAR(50),
	counter_type VARCHAR(50),
	branch VARCHAR(10),
	create_dt DATETIME YEAR to SECOND NOT NULL,
	create_by VARCHAR(50) NOT NULL,
	update_dt DATETIME YEAR to SECOND NOT NULL,
	update_by VARCHAR(50),
	PRIMARY KEY (ip_address)
);
-- create rob_counter_session table
create table rob_counter_session(
	session_id serial8 not null,
	counter_ip_address varchar(50) not null,
	user_id varchar(50) not null,
	checkin_date datetime year to second not null,
	checkout_date datetime year to second,
	checkout_by varchar(50),
	is_open INTEGER NOT NULL DEFAULT 0,
	floor_level varchar(50),
	balancing_status varchar(5),
	approve_by varchar(50),
	approve_dt DATETIME YEAR to SECOND,
	create_dt DATETIME YEAR to SECOND NOT NULL,
	create_by VARCHAR(50) NOT NULL,
    verify_by varchar(50),
	verify_dt DATETIME YEAR to SECOND,
	balancing_by varchar(50),
	balancing_dt DATETIME YEAR to SECOND,
	counter_bankin_slip_no varchar(50),
	primary key (session_id)
);

-- create rob_counter_balancing
CREATE TABLE rob_counter_balancing (
	balancing_id SERIAL8 NOT NULL,
	bank_note MONEY(10,2) NOT NULL DEFAULT 0.00                                                                                                                                                                                                                                                          ,
	quantity INTEGER NOT NULL,
	amount MONEY(10,2) NOT NULL DEFAULT 0.00                                                                                                                                                                                                                                                          ,
	counter_session_id INTEGER NOT NULL,
	create_dt DATETIME YEAR to SECOND NOT NULL,
	create_by VARCHAR(50) NOT NULL,
	update_dt DATETIME YEAR to SECOND NOT NULL,
	update_by VARCHAR(50),
	PRIMARY KEY (balancing_id) 
);

-- create table rob_counter_bankin_slip
CREATE TABLE rob_counter_bankin_slip (
	bankin_slip_no varchar(50) NOT NULL,
	branch VARCHAR(10),
	amount MONEY(10,2) NOT NULL DEFAULT 0.00 ,
	create_dt DATETIME YEAR to SECOND NOT NULL,
	create_by VARCHAR(50) NOT NULL,
	update_dt DATETIME YEAR to SECOND NOT NULL,
	update_by VARCHAR(50),
	year_created VARCHAR(10),
	PRIMARY KEY (bankin_slip_no) 
);

-- alter rob_counter_collection
ALTER TABLE rob_counter_collection ADD counter_name varchar(50);

-- insert balancing parameter
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values
("100", "100.00", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL"),
("50", "50.00", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL"),
("20", "20.00", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL"),
("10", "10.00", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL"),
("5", "5.00", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL"),
("2", "2.00", "COLLECTION_BALANCING_NOTE", "N", current, "RIZAL", current, "RIZAL"),
("1", "1.00", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL"),
("0.5", "0.50", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL"),
("0.2", "0.20", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL"),
("0.1", "0.10", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL"),
("0.05", "0.05", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL"),
("0.01", "0.01", "COLLECTION_BALANCING_NOTE", "A", current, "RIZAL", current, "RIZAL")

-- create rob_payment_credit_note
CREATE TABLE rob_payment_credit_note (
	credit_note_no varchar(50) NOT NULL,
	payment_receipt_no varchar(50) NOT null,
	amount MONEY(10,2) NOT NULL DEFAULT 0.00                                                                                                                                                                                                                                                          ,
	counter_session_id INTEGER NOT NULL,
	create_dt DATETIME YEAR to SECOND NOT NULL,
	create_by VARCHAR(50) NOT NULL,
	update_dt DATETIME YEAR to SECOND NOT NULL,
	update_by VARCHAR(50) NOT NULL,
	PRIMARY KEY (credit_note_no) 
);

--add tax_invoice_no
ALTER TABLE ezbiz:informix.rob_payment_receipt_aud ADD tax_invoice_no varchar(50);

--insert into parameters
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("C", "Cancel", "ROB_FORM_A_STATUS", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("C", "Cancel", "ROB_FORM_B_STATUS", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("C", "Cancel", "ROB_FORM_C_STATUS", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("C", "Cancel", "ROB_RENEWAL_STATUS", "A", current, "RIZAL", current, "RIZAL");

--insert online seller
alter table rob_form_a_aud
add is_online_seller char(1) ;

--insert parameter
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values
("1", "Yes", "PAYMENT_RECEIPT_ISCANCEL", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values
("0", "No", "PAYMENT_RECEIPT_ISCANCEL", "A", current, "RIZAL", current, "RIZAL")

--insert parameter
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values
("GEN", "GENERAL", "ROB_FORM_A_BUSINESS_TYPE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values
("INC", "INCUBATOR", "ROB_FORM_A_BUSINESS_TYPE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values
("STU", "STUDENT", "ROB_FORM_A_BUSINESS_TYPE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values
("OKU", "OKU", "ROB_FORM_A_BUSINESS_TYPE", "A", current, "RIZAL", current, "RIZAL");

--alter table rob_form_a to support dynamic fee
alter table rob_form_a
add biz_type varchar(50);

--insert parameter
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("Y", "Allow otc payment", "ALLOW_OTC_PAYMENT", "A", current, "RIZAL", current, "RIZAL");

--insert survey score
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("1", "1", "SURVEY_SCORE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("2", "2", "SURVEY_SCORE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("3", "3", "SURVEY_SCORE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("4", "4", "SURVEY_SCORE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("5", "5", "SURVEY_SCORE", "A", current, "RIZAL", current, "RIZAL");

--INSERT MONTH list
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("1", "January", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("2", "February", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("3", "March", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("4", "April", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("5", "May", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("6", "June", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("7", "July", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("8", "August", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("9", "September", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("10", "October", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("11", "November", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("12", "December", "MONTH_LIST", "A", current, "RIZAL", current, "RIZAL");

--CREATE rob_survey_config
create table rob_survey_config(
	survey_id serial8 not null,
	value varchar(250) not null,
	parent_id integer,
	value_type varchar(50),
	is_ans_score char(1),
	is_active char(1) default "Y",
	create_dt DATETIME YEAR to SECOND NOT NULL,
	create_by VARCHAR(50) NOT NULL,
	update_dt DATETIME YEAR to SECOND NOT NULL,
	update_by VARCHAR(50) NOT NULL,
	primary key (survey_id)
);

-- create rob_surver_ans
create table rob_survey_ans(
	ans_id serial8 not null,
	survey_id integer not null,
	answer varchar(100),
	user_id varchar(50),
	create_dt DATETIME YEAR to SECOND NOT NULL,
	create_by VARCHAR(50) NOT NULL,
	update_dt DATETIME YEAR to SECOND NOT NULL,
	update_by VARCHAR(50) NOT NULL,
	primary key (survey_id)
);

--ADD is_ans_survey
ALTER TABLE ezbiz:informix.rob_user_profile ADD is_ans_survey char(1);
ALTER TABLE ezbiz:informix.rob_user_profile_aud ADD is_ans_survey char(1);

--ADD floor
alter table rob_counter_bankin_slip
add floor_level varchar(50);

alter table rob_counter_bankin_slip_aud
add floor_level varchar(50);

-- ADD soft_delete AND is_active
alter table rob_counter_collection
add soft_delete char(1) default "N",
add is_active char(1) default "Y";

alter table rob_counter_collection_aud
add soft_delete char(1) default "N",
add is_active char(1) default "Y";

-- ADD counter TYPE
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("INT", "Internal", "PAYMENT_COUNTER_TYPE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("MOB", "Mobile Counter", "PAYMENT_COUNTER_TYPE", "A", current, "RIZAL", current, "RIZAL");

--ADD reason AND approveby 
alter table rob_payment_credit_note
add reason varchar(255),
add approve_by varchar(50),
add approve_dt DATETIME YEAR to SECOND

-- ADD switch TO PARAMETER
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("Y", "Y/N", "IS_CHECKING_STATUS_CN", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("PV", "Pending Verification", "ROB_FORM_A_STATUS", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("V", "Verified", "ROB_FORM_A_STATUS", "A", current, "RIZAL", current, "RIZAL");

--INSERT incentive
insert into rob_payment_fee (payment_code, payment_fee, status, gst_code)
values("INCNTV_PERSONAL_STUDENT", 30, "A", "OS");

insert into rob_payment_fee (payment_code, payment_fee, status, gst_code)
values("INCNTV_TRADE_STUDENT", 60, "A", "OS");

insert into rob_payment_fee (payment_code, payment_fee, status, gst_code)
values("INCNTV_PERSONAL_OKU", 30, "A", "OS");

insert into rob_payment_fee (payment_code, payment_fee, status, gst_code)
values("INCNTV_TRADE_OKU", 60, "A", "OS");

insert into rob_payment_fee (payment_code, payment_fee, status, gst_code)
values("INCNTV_BINFO_STUDENT", 10, "A", "SR");

insert into rob_payment_fee (payment_code, payment_fee, status, gst_code)
values("INCNTV_BINFO_OKU", 0, "A", "SR");

--insert parameter

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("INCNTV_PERSONAL_STUDENT", "INCENTIVE FOR STUDENT", "PAYMENT_TYPE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("INCNTV_TRADE_STUDENT", "INCENTIVE FOR STUDENT", "PAYMENT_TYPE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("INCNTV_PERSONAL_OKU", "INCENTIVE FOR OKU", "PAYMENT_TYPE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("INCNTV_TRADE_OKU", "INCENTIVE FOR OKU", "PAYMENT_TYPE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("INCNTV_BINFO_STUDENT", "INCENTIVE FOR STUDENT", "PAYMENT_TYPE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("INCNTV_BINFO_OKU", "INCENTIVE FOR OKU", "PAYMENT_TYPE", "A", current, "RIZAL", current, "RIZAL");

-- payment mode

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("CASH", "CASH", "PAYMENT_MODE", "A", current, "RIZAL", current, "RIZAL");

insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("CREDIT CARD", "CREDIT CARD", "PAYMENT_MODE", "A", current, "RIZAL", current, "RIZAL");

-- create rob_form_a_stat_report
create table rob_form_a_stat_report(
	stat_id serial8 not null,
	stat_month varchar(50) not null,
	stat_year varchar(50) not null,
	online_seller integer not null default 0,
	student integer not null default 0,
	oku integer not null default 0,
	incubator integer not null default 0,
	stat_total integer not null default 0,
	create_dt datetime year to second not null,
	create_by varchar(50) not null,
	update_dt datetime year to second not null,
	update_by varchar(50) not null,
	primary key(stat_id)
)

--insert parameter
insert into rob_parameters (code, code_desc, code_type, status, create_dt, create_by, update_dt, update_by)
values ("Y", "Allow incentive", "ALLOW_INCENTIVE", "A", current, "RIZAL", current, "RIZAL");

-- renewal incentive
ALTER TABLE ezbiz:informix.rob_renewal ADD renewal_incentive varchar(50);
