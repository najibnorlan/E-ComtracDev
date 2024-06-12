/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.service.impl.BaseServiceImpl;
import com.ssm.llp.base.common.dao.LlpRegTransactionDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpRegTransaction;
import com.ssm.llp.base.common.service.LlpRegTransactionService;
import com.ssm.llp.mod1.model.LlpRegistration;

@Service
public class LlpRegTransactionServiceImpl extends BaseServiceImpl<LlpRegTransaction,  Long> implements LlpRegTransactionService{
	@Autowired 
	LlpRegTransactionDao llpRegTransactionDao;

	@Override
	public BaseDao getDefaultDao() {
		return llpRegTransactionDao;
	}

	@Override
	public LlpRegTransaction findByReserverNameRefNo(String nsRefNo) {
		SearchCriteria sc = new SearchCriteria("nsRefNo",SearchCriteria.EQUAL,nsRefNo);
		List<LlpRegTransaction> listResult = findByCriteria(sc).getList();
		if(listResult.size()>0){
			
			LlpRegTransaction llpRegTransaction = listResult.get(0);
			try {
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(llpRegTransaction.getRegData()));
				llpRegTransaction.setLlpRegistration((LlpRegistration) ois.readObject());
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return listResult.get(0);
		}
		return null;
	}
	
	public void convertRegToByte(LlpRegTransaction llpRegTransaction){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(llpRegTransaction.getLlpRegistration());
			oos.close();
			
			llpRegTransaction.setRegData(baos.toByteArray());
			
			baos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional
	public void insert(LlpRegTransaction entity) {
		convertRegToByte(entity);
		super.insert(entity);
	}
	
	
	@Override
	@Transactional
	public void update(LlpRegTransaction entity) {
		convertRegToByte(entity);
		super.update(entity);
	}
}
