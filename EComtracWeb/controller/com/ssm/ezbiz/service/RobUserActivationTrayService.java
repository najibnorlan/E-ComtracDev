/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.ezbiz.service;

import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.base.common.service.BaseService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.ezbiz.model.RobUserActivationTray;

public interface RobUserActivationTrayService extends BaseService<RobUserActivationTray, String>{

	void lock(RobUserActivationTray robUserActivationTray);

	void unlock(RobUserActivationTray robUserActivationTray);

	void processApplication(RobUserActivationTray activationTray, String status, String processNote)throws SSMException;

	void reSubmit(RobUserActivationTray activationTray, String queryAnswer, LlpFileData myKadDoc, LlpFileData selfieDoc, LlpFileData supportingDoc);

	void submit(RobUserActivationTray activationTray, LlpFileData myKadDoc, LlpFileData selfieDoc, LlpFileData supportingDoc);

	boolean hasPendingApplication(String userRefNo);

	void discardNonPendingApp();
	
	void sendEmailAfterProcess(RobUserActivationTray activationTray);
}

