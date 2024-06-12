package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;

import com.ssm.ezbiz.comtrac.SelectComtracTraining;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.WicketApplication;

@Entity
@Table(name = "rob_training_config")
@Audited
public class RobTrainingConfig implements Serializable {

	private Integer trainingId;
	private String trainingCode;
	private String trainingName;
	private String trainingDesc;
	private Date trainingStartDt;
	private Date trainingEndDt;
	private Date regClosingDt;
	private String trainingStartTime;
	private String trainingEndTime;
	private Integer cpePoint;
	private String trainingVenue;
	private Double standardFee;
	private Double specialFee;
	private Double standardFeeGst;
	private Double specialFeeGst;
	private String gstCode;
	private Integer maxPax;
	private Integer currentPax;
	private Boolean isActive = Boolean.TRUE;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private List<RobTrainingTransaction> robTrainingTransactionList;
	private Integer trainingListSeq;
	private String trainingType;

	public RobTrainingConfig() {
		super();
	}

	public RobTrainingConfig(Integer trainingId, String trainingCode, String trainingName, String trainingDesc,
			Date trainingStartDt, Date trainingEndDt, Date regClosingDt, String trainingStartTime,
			String trainingEndTime, Integer cpePoint, String trainingVenue, Double standardFee, Double specialFee,
			Integer maxPax, Boolean isActive, Date createDt, String createBy, Date updateDt, String updateBy,
			List<RobTrainingTransaction> robTrainingTransactionList, Integer trainingListSeq, String trainingType) {
		super();
		this.trainingId = trainingId;
		this.trainingCode = trainingCode;
		this.trainingName = trainingName;
		this.trainingDesc = trainingDesc;
		this.trainingStartDt = trainingStartDt;
		this.trainingEndDt = trainingEndDt;
		this.regClosingDt = regClosingDt;
		this.trainingStartTime = trainingStartTime;
		this.trainingEndTime = trainingEndTime;
		this.cpePoint = cpePoint;
		this.trainingVenue = trainingVenue;
		this.standardFee = standardFee;
		this.specialFee = specialFee;
		this.maxPax = maxPax;
		this.isActive = isActive;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
		this.robTrainingTransactionList = robTrainingTransactionList;
		this.trainingListSeq = trainingListSeq;
		this.trainingType = trainingType;
	}

	@Id
	@Column(name = "training_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(Integer trainingId) {
		this.trainingId = trainingId;
	}

	@Column(name = "training_code")
	public String getTrainingCode() {
		return trainingCode;
	}

	public void setTrainingCode(String trainingCode) {
		this.trainingCode = trainingCode;
	}

	@Column(name = "training_name")
	public String getTrainingName() {
		return trainingName;
	}

	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}

	@Column(name = "training_desc")
	public String getTrainingDesc() {
		return trainingDesc;
	}

	public void setTrainingDesc(String trainingDesc) {
		this.trainingDesc = trainingDesc;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "training_start_dt")
	public Date getTrainingStartDt() {
		return trainingStartDt;
	}

	public void setTrainingStartDt(Date trainingStartDt) {
		this.trainingStartDt = trainingStartDt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "training_end_dt")
	public Date getTrainingEndDt() {
		return trainingEndDt;
	}

	public void setTrainingEndDt(Date trainingEndDt) {
		this.trainingEndDt = trainingEndDt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "reg_closing_dt")
	public Date getRegClosingDt() {
		return regClosingDt;
	}

	public void setRegClosingDt(Date regClosingDt) {
		this.regClosingDt = regClosingDt;
	}

	@Column(name = "training_start_time")
	public String getTrainingStartTime() {
		return trainingStartTime;
	}

	public void setTrainingStartTime(String trainingStartTime) {
		this.trainingStartTime = trainingStartTime;
	}

	@Column(name = "training_end_time")
	public String getTrainingEndTime() {
		return trainingEndTime;
	}

	public void setTrainingEndTime(String trainingEndTime) {
		this.trainingEndTime = trainingEndTime;
	}

	@Column(name = "cpe_point")
	public Integer getCpePoint() {
		return cpePoint;
	}

	public void setCpePoint(Integer cpePoint) {
		this.cpePoint = cpePoint;
	}

	@Column(name = "training_venue")
	public String getTrainingVenue() {
		return trainingVenue;
	}

	public void setTrainingVenue(String trainingVenue) {
		this.trainingVenue = trainingVenue;
	}

	@Column(name = "standard_fee")
	public Double getStandardFee() {
		return standardFee;
	}

	public void setStandardFee(Double standardFee) {
		this.standardFee = standardFee;
	}

	@Column(name = "special_fee")
	public Double getSpecialFee() {
		return specialFee;
	}

	public void setSpecialFee(Double specialFee) {
		this.specialFee = specialFee;
	}

	@Column(name = "max_pax")
	public Integer getMaxPax() {
		return maxPax;
	}

	public void setMaxPax(Integer maxPax) {
		this.maxPax = maxPax;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 3594)
	public Date getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	@Column(name = "create_by", nullable = false, length = 50)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", nullable = false, length = 3594)
	public Date getUpdateDt() {
		return this.updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	@Column(name = "update_by", nullable = false, length = 50)
	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@OneToMany(mappedBy = "trainingId", fetch = FetchType.LAZY)
	public List<RobTrainingTransaction> getRobTrainingTransactionList() {
		return robTrainingTransactionList;
	}

	public void setRobTrainingTransactionList(List<RobTrainingTransaction> robTrainingTransactionList) {
		this.robTrainingTransactionList = robTrainingTransactionList;
	}

	@Column(name = "is_active")
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name = "current_pax")
	public Integer getCurrentPax() {
		return currentPax;
	}

	public void setCurrentPax(Integer currentPax) {
		this.currentPax = currentPax;
	}

	@Column(name = "standard_fee_gst")
	public Double getStandardFeeGst() {
		return standardFeeGst;
	}

	public void setStandardFeeGst(Double standardFeeGst) {
		this.standardFeeGst = standardFeeGst;
	}

	@Column(name = "special_fee_gst")
	public Double getSpecialFeeGst() {
		return specialFeeGst;
	}

	public void setSpecialFeeGst(Double specialFeeGst) {
		this.specialFeeGst = specialFeeGst;
	}

	@Transient
	public Double getStandardFeeWithGst() {
		return getStandardFee() + getStandardFeeGst();
	}

	@Transient
	public Double getSpecialFeeWithGst() {
		return getSpecialFee() + getSpecialFeeGst();
	}

	@Transient
	public Double getStandardFeeWithoutGst() {
		if (Parameter.PAYMENT_GST_CODE_SR.equals(getGstCode())) {
			return getStandardFee() / (1 + WicketApplication.get().getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
		}

		return getStandardFee();
	}

	@Transient
	public Double getSpecialFeeWithoutGst() {
		if (Parameter.PAYMENT_GST_CODE_SR.equals(getGstCode())) {
			return getSpecialFee() / (1 + WicketApplication.get().getGSTRate(Parameter.PAYMENT_GST_CODE_SR));
		}
		return getSpecialFee();
	}

	@Column(name = "gst_code")
	public String getGstCode() {
		return gstCode;
	}

	public void setGstCode(String gstCode) {
		this.gstCode = gstCode;
	}

	@Column(name = "training_list_seq")
	public Integer getTrainingListSeq() {
		return trainingListSeq;
	}

	public void setTrainingListSeq(Integer trainingListSeq) {
		this.trainingListSeq = trainingListSeq;
	}

	@Column(name = "training_type")
	public String getTrainingType() {
		return trainingType;
	}

	public void setTrainingType(String trainingType) {
		this.trainingType = trainingType;
	}
}
