package com.ssm.llp.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LlpDateUtils {
	private static SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat full = new SimpleDateFormat("dd MMMM yyyy");
	private static SimpleDateFormat simpleTime = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	private static SimpleDateFormat fullTime = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss a");

	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(formatDate(date));// 24-12-2012
		System.out.println(formatDateFull(date));// 24 Disember 2012
		System.out.println(formatDateTime(date));// 24-12-2012 06:30:32 PM
		System.out.println(formatDateTimeFull(date));// 24 Disember 2012 06:30:32 PM
	}

	/**
	 * formatDate to format 24/12/2012
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		if (date != null) {
			return simple.format(date);
		}
		return "";
	}

	/**
	 * formatDate to format 24 Disember 2012
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateFull(Date date) {
		if (date != null) {
			return full.format(date);
		}
		return "";
	}

	/**
	 * formatDate to format 24/12/2012 06:30:32 PM
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		if (date != null) {
			return simpleTime.format(date);
		}
		return "";
	}

	/**
	 * formatDate to format 24 Disember 2012 06:30:32 PM
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTimeFull(Date date) {
		if (date != null) {
			return fullTime.format(date);
		}
		return "";
	}

	public static Date generateDobFromIdNo(String idNo, String datePattern) {
		try {
			String idNoStr = idNo;
			String Year = idNoStr.substring(0, 2);
			String Month = idNoStr.substring(2, 4);
			String Day = idNoStr.substring(4, 6);
			Integer cutoff = Calendar.getInstance().get(Calendar.YEAR) - 2000;
			String dob = Day + "/" + Month + "/" + (Integer.valueOf(Year) > Math.abs(cutoff) ? 19 : 20) + Year;
			return new SimpleDateFormat(datePattern).parse(dob);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
