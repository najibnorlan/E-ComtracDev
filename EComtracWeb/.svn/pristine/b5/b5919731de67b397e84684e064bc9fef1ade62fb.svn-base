package com.ssm.llp.base.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ssm.base.common.util.DateUtil;

public class TestOnly {
	public static void main(String[] args) throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String expDtStr = "29/02/2020";
		Date expDt = sdf.parse(expDtStr);
		

		Calendar calDt = Calendar.getInstance();
		calDt.setTime(sdf.parse("01/03/2019"));// 
		
		int totalBalYear = 0;
		while(calDt.getTime().before(expDt)){
			totalBalYear = totalBalYear+1;
			calDt.add(Calendar.YEAR, 1);
			if(totalBalYear==10){
				break;
			}
		}
				
		System.out.println(totalBalYear);
				
//		System.out.println(sdf.parse(dt));
//		int diff = DateUtil.getMonthDifferent(new Date(), sdf.parse(dt));
//		int balYear = diff/12;
		
//		System.out.println(diff);
	}

	private static void test123() {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		System.out.println(methodName);
		
//		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//		System.out.println(stackTraceElements[0].getMethodName());
		testAbu();
	}
	
	private static void testAbu() {
//		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}
