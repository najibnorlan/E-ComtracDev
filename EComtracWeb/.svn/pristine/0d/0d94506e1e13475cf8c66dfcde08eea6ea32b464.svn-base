package com.ssm.llp.base.utils;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ssm.llp.base.common.sec.UserEnvironmentHelper;

public class SSMErrorStream extends PrintStream {
	private static final SSMErrorStream INSTANCE = new SSMErrorStream();

	public static void activate() {
		System.setErr(INSTANCE);
	}

	private SSMErrorStream() {
		super(System.err);
	}

	@Override
	public void println(Object x) {
		showLocation();
		super.println(x);
	}

	@Override
	public void println(String x) {
		showLocation();
		super.println(x);
	}

	private void showLocation() {
		Thread currentThread = Thread.currentThread();
		StackTraceElement element = currentThread.getStackTrace()[3];
//		StackTraceElement element = Thread.currentThread().getStackTrace()[4];
//		StackTraceElement el[] = Thread.currentThread().getStackTrace();
//		for (int i = 0; i < el.length; i++) {
////			super.println(">>>"+el[i].getClassName());
//		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss ");
		super.print(MessageFormat.format("{0}[{1}:{2}:{3}:{4, number,#}] : ", sdf.format(new Date()), currentThread.getName()+":"+ UserEnvironmentHelper.getLoginName(), element.getFileName(), element.getMethodName(),element.getLineNumber() ));
	}
}
