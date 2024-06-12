package com.ssm.ezbiz.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssm.ezbiz.comtrac.SearchComtracList.SearchComtracModel;
import com.ssm.ezbiz.dao.RobTrainingTransactionDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.ezbiz.model.RobTrainingTransaction;

@Repository
public class RobTrainingTransactionDaoImpl extends BaseDaoImpl<RobTrainingTransaction, Integer> implements RobTrainingTransactionDao{

	@Override
	public RobTrainingTransaction findByTransactionCodeWithParticipant(String transactionCode) {
		String hql = "select a from RobTrainingTransaction a "
				+ "left join fetch a.robTrainingParticipantList b "
				+ "where a.transactionCode = ?";
				
		List<RobTrainingTransaction> list = getHibernateTemplate().find(hql, transactionCode);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<RobTrainingTransaction> searchComtractTransactions( SearchComtracModel searchComtracModel) {
		
		ArrayList<String> param = new ArrayList<String>();
		
		String hql = "select a from RobTrainingTransaction a "
				+ " join fetch a.robTrainingParticipantList b ";
		
		if(searchComtracModel.getTransactionNo() != null || searchComtracModel.getLodgerId() != null || searchComtracModel.getInvoiceNo() != null || 
				searchComtracModel.getReceiptNo() != null || searchComtracModel.getLouloaNo() != null || searchComtracModel.getStatus() != null || 
				searchComtracModel.getDtFrom() != null || searchComtracModel.getDtTo() != null || searchComtracModel.getCreatedBy() != null || searchComtracModel.getParticipantIc() != null){
			hql += " where ";
		
			if(searchComtracModel.getTransactionNo() != null){
				hql += " a.transactionCode = ?";
				param.add(searchComtracModel.getTransactionNo());
				
				if(searchComtracModel.getLodgerId() != null || searchComtracModel.getInvoiceNo() != null || 
						searchComtracModel.getReceiptNo() != null || searchComtracModel.getLouloaNo() != null || searchComtracModel.getStatus() != null || 
						searchComtracModel.getDtFrom() != null || searchComtracModel.getDtTo() != null || searchComtracModel.getCreatedBy() != null || searchComtracModel.getParticipantIc() != null){
					hql += " AND";
				}
			}
			
			if(searchComtracModel.getLodgerId() != null){
				hql += " a.lodgerId = ?";
				param.add(searchComtracModel.getLodgerId());
				
				if(searchComtracModel.getInvoiceNo() != null || searchComtracModel.getReceiptNo() != null || searchComtracModel.getLouloaNo() != null || searchComtracModel.getStatus() != null || 
						searchComtracModel.getDtFrom() != null || searchComtracModel.getDtTo() != null || searchComtracModel.getCreatedBy() != null || searchComtracModel.getParticipantIc() != null){
					hql += " AND";
				}
			}
			
			if(searchComtracModel.getInvoiceNo() != null){
				hql += " a.invoiceNo = ?";
				param.add(searchComtracModel.getInvoiceNo());
				
				if(searchComtracModel.getReceiptNo() != null || searchComtracModel.getLouloaNo() != null || searchComtracModel.getStatus() != null || 
						searchComtracModel.getDtFrom() != null || searchComtracModel.getDtTo() != null || searchComtracModel.getCreatedBy() != null || searchComtracModel.getParticipantIc() != null){
					hql += " AND";
				}
			}
			
			if(searchComtracModel.getReceiptNo() != null){
				hql += " a.receiptNo = ?";
				param.add(searchComtracModel.getReceiptNo());
				
				if(searchComtracModel.getLouloaNo() != null || searchComtracModel.getStatus() != null || searchComtracModel.getDtFrom() != null || 
						searchComtracModel.getDtTo() != null || searchComtracModel.getCreatedBy() != null || searchComtracModel.getParticipantIc() != null){
					hql += " AND";
				}
			}
			
			if(searchComtracModel.getLouloaNo() != null){
				hql += " a.louLoaRefNo = ?";
				param.add(searchComtracModel.getLouloaNo());
				
				if(searchComtracModel.getStatus() != null || searchComtracModel.getDtFrom() != null ||  searchComtracModel.getDtTo() != null || 
						searchComtracModel.getCreatedBy() != null || searchComtracModel.getParticipantIc() != null){
					hql += " AND";
				}
			}
			
			if(searchComtracModel.getStatus() != null){
				hql += " a.status = ?";
				param.add(searchComtracModel.getStatus());
				
				if(searchComtracModel.getDtFrom() != null ||  searchComtracModel.getDtTo() != null || 
						searchComtracModel.getCreatedBy() != null || searchComtracModel.getParticipantIc() != null){
					hql += " AND";
				}
			}
			
			if(searchComtracModel.getDtFrom() != null){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				hql += " to_char(a.createDt, '%Y-%m-%d') >= ? ";
//				hql += " DATE_FORMAT(a.createDt, '%Y-%m-%d') >= ? ";//MYSQL
				param.add(df.format(searchComtracModel.getDtFrom()));
				
				if(searchComtracModel.getDtTo() != null || 
						searchComtracModel.getCreatedBy() != null || searchComtracModel.getParticipantIc() != null){
					hql += " AND";
				}
			}
			
			if(searchComtracModel.getDtTo() != null){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				hql += " to_char(a.createDt, '%Y-%m-%d') <= ? ";
//				hql += " DATE_FORMAT(a.createDt, '%Y-%m-%d') <= ? ";//MYSQL
				param.add(df.format(searchComtracModel.getDtTo()));
				
				if(searchComtracModel.getCreatedBy() != null || searchComtracModel.getParticipantIc() != null){
					hql += " AND";
				}
			}
			
			if(searchComtracModel.getCreatedBy() != null){
				hql += " a.createBy=?";
				param.add(searchComtracModel.getCreatedBy());
				
				if(searchComtracModel.getParticipantIc() != null){
					hql += " AND";
				}
			}
			
			if(searchComtracModel.getParticipantIc() != null){
				hql += " b.icNo = ?";
				param.add(searchComtracModel.getParticipantIc());
			}
		}
		
		String[] paramArray = new String[ param.size() ];
		param.toArray( paramArray );
		
		List<RobTrainingTransaction> result = getHibernateTemplate().find(hql, paramArray);
			
		return result;
	}

}
