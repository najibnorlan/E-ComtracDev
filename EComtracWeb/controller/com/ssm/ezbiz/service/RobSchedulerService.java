package com.ssm.ezbiz.service;


public interface RobSchedulerService {

	public void checkHealthAll();
	public void runSchedulerByMethodName(String methodName);
	public void runAllScheduler();
}
