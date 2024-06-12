RENAME COLUMN ssm_my_kad.email TO ezbiz_email
GO
RENAME COLUMN ssm_my_kad.phone_no TO ezbiz_phone_no
GO
RENAME COLUMN ssm_my_kad.login_id TO ezbiz_login_id
GO

ALTER TABLE ssm_my_kad
	ADD ezbiz_address1 VARCHAR(150),
    ADD ezbiz_address2 VARCHAR(150),
    ADD ezbiz_address3 VARCHAR(150),
    ADD ezbiz_postcode VARCHAR(10),
    ADD ezbiz_city VARCHAR(150),
    ADD ezbiz_state VARCHAR(4),
    ADD ezbiz_country VARCHAR(4)
GO

