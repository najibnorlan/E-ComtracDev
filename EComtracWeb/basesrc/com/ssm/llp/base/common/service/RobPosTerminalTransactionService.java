package com.ssm.llp.base.common.service;

import com.ssm.llp.ezbiz.model.RobPosTerminalTransaction;

public interface RobPosTerminalTransactionService extends BaseService<RobPosTerminalTransaction, Integer>{

	void processAndUpdate(String cmd, byte byteResponse[], RobPosTerminalTransaction posTerminalTransaction)throws Exception;

}
