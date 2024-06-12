package com.ssm.ezbiz.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;

import org.springframework.stereotype.Repository;
import org.hibernate.jdbc.Work;

import com.ssm.ezbiz.dao.RobParticipantRegTrainingDao;
import com.ssm.llp.base.common.dao.impl.BaseDaoImpl;
import com.ssm.llp.ezbiz.model.RobParticipantRegTraining;

@Repository
public class RobParticipantRegTrainingDaoImpl extends BaseDaoImpl<RobParticipantRegTraining, Integer> implements RobParticipantRegTrainingDao{
	
	@Override
	public List<RobParticipantRegTraining> getParticipantCurrentTrainingByStartDtEndDt(Date startDt, Date endDt, String ic){ //to-check clash date training..
		List<RobParticipantRegTraining> listRegisteredTraining = new ArrayList<RobParticipantRegTraining>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		getSession().doWork(new Work() {
		    @Override
		    public void execute(Connection connection) throws SQLException {
		    	String nativeSqlQuery = "select c.participant_id,c.name,c.ic_no,c.ls_no,c.membership_no,c.designation,c.company,c.create_dt,c.create_by,c.update_dt,c.update_by,"
				        + " a.training_id,a.training_code,a.training_name,a.training_desc,a.training_start_dt,a.training_end_dt,"
				        + " b.transaction_code,b.status,b.lodger_id,b.lodger_name"
						+ " from rob_training_config a, rob_training_transaction b, rob_training_participant c"
						+ " where a.training_id=b.training_id"
				        + " and b.transaction_code=c.transaction_code"
				       // + " and b.status = 'PS'" //payment success
				        + " and b.status <> 'C'" //not cancel
				        + " and ("
				        + " (date(a.training_start_dt)>='"+sdf.format(startDt)+"' and date(a.training_start_dt)<='"+sdf.format(endDt)+"')"
				        + " or"
				        + " (date(a.training_end_dt)>='"+sdf.format(startDt)+"' and date(a.training_end_dt)<='"+sdf.format(endDt)+"')"
				        + " or"
				        + " (date(a.training_start_dt)<='"+sdf.format(startDt)+"' and date(a.training_end_dt)>='"+sdf.format(endDt)+"')"
				        + " )"
				        + " and c.ic_no = ?";
		    	
		    	 /* 
			      * 2022-04-03 = start date
			      * 2022-04-06 = end date
			      * 
			        + " (date(a.training_start_dt)>='2022-04-03' and date(a.training_start_dt)<='2022-04-06')"
			        + " or"
			        + " (date(a.training_end_dt)>='2022-04-03' and date(a.training_end_dt)<='2022-04-06')"
			        + " or"
			        + " (date(a.training_start_dt)<='2022-04-03' and date(a.training_end_dt)>='2022-04-06')" */
		    	
		    	 PreparedStatement ps = connection.prepareStatement(nativeSqlQuery);
		    	 ps.setString(1, ic); 
		    	 ResultSet rs = ps.executeQuery();
		    	 while(rs.next()) {
		    		 RobParticipantRegTraining regTraining = new RobParticipantRegTraining();
		    		 regTraining.setParticipantId(rs.getInt(1));
		    		 regTraining.setName(rs.getString(2));
		    		 regTraining.setIcNo(rs.getString(3));
		    		 regTraining.setLsNo(rs.getString(4));
		    		 regTraining.setMembershipNo(rs.getString(5));
		    		 regTraining.setDesignation(rs.getString(6));
		    		 regTraining.setCompany(rs.getString(7));
		    		 regTraining.setCreateDt(rs.getDate(8));
		    		 regTraining.setCreateBy(rs.getString(9));
		    		 regTraining.setUpdateDt(rs.getDate(10));
		    		 regTraining.setUpdateBy(rs.getString(11));
		    		 regTraining.setTrainingId(rs.getInt(12));
		    		 regTraining.setTrainingCode(rs.getString(13));
		    		 regTraining.setTrainingName(rs.getString(14));
		    		 regTraining.setTrainingDesc(rs.getString(15));
		    		 regTraining.setTrainingStartDt(rs.getDate(16));
		    		 regTraining.setTrainingEndDt(rs.getDate(17));
		    		 regTraining.setTransactionCode(rs.getString(18));
		    		 regTraining.setTrainingTransactionStatus(rs.getString(19));
		    		 regTraining.setLodgerId(rs.getString(20));
		    		 regTraining.setLodgerName(rs.getString(21));
		    		 
		    		 listRegisteredTraining.add(regTraining);
		    	 }
		    	//System.out.println("nativeSQL = "+nativeSqlQuery);
		    }
		});
		
		
//		String hql = "select a from " + RobTrainingParticipant.class.getName() + " a"
//		        + " join fetch a.robTrainingTransaction b"
//		        + " join fetch b.trainingId c"
//		        + " where a.icNo = ?";
//				//+ " where c.trainingId = ? and a.icNo = ?";
//		List<RobTrainingParticipant> list = getHibernateTemplate().find(hql, new Object[]{trainingId, icNo});
		
		return listRegisteredTraining;
	}
	
}




