package com.ssm.ezbiz.errorlog;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.page.CouldNotLockPageException;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;

import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.page.HomePage;

public class MyInternalErrorPage extends BasePage{

//	public static LimitedSizeQueue<SSMErrorLog> limitedSizeQueue =  new LimitedSizeQueue<SSMErrorLog>(500);
	
//	public static ObjectOutputStream oos = null;
	public static File file = new File("errorLog.txt");
//	static {
//		try {
//			FileOutputStream fos =new FileOutputStream(file);
//			System.out.println("FileErrorLogCreated:"+file.getCanonicalPath());
//			oos = new ObjectOutputStream(fos);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
	@Override
	public String getPageTitle() {
		return "err.page";
	}
	public MyInternalErrorPage() {
		HttpSession session = ((ServletWebRequest)RequestCycle.get().getRequest()).getContainerRequest().getSession();
		Throwable ex = (Exception) session.getAttribute("errorExp");
		processError(ex);
	}
	public MyInternalErrorPage(Throwable ex) {
		if(ex instanceof CouldNotLockPageException) {
//			getSession().invalidate();
//			throw new RestartResponseAtInterceptPageException(HomePage.class);
		}
		processError(ex);
	}
	
	void processError(Throwable ex){
		
//		SSMErrorLog errorLog = new SSMErrorLog();
////		String errorMsg = "ERRORID:"+errorLog.getErrorId()+"<br>"+resolve("err.lbl.internalErrorMsg");
//		String errorMsg = resolve("err.lbl.internalErrorMsg");
//		
//		if(ex!=null){
//			try {
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				ex.printStackTrace(new PrintStream(baos));
//				String detailError = baos.toString();
//				baos.close();
//				errorLog.setErrorMsg(detailError);
//			} catch (Exception e) {
//			}
//			
//			
//			String errorMsgDetail = ex.getCause().toString();
//			while (ex.getCause() != null){
//		        ex = ex.getCause();
//		        errorMsgDetail += "\n"+ ex.getMessage();
//			}
//			
////			if(ex.getMessage().indexOf("Row was updated or deleted by another transaction")!=-1){
//			if(ex instanceof StaleObjectStateException){	
//				errorMsg +="<br>"+resolve("err.lbl.updateByOtherTransaction");
//			}
//			if(ex instanceof ConnectException){
//				errorMsg +="<br>"+resolve("err.lbl.connection");
//			}
//			
//			if(UserEnvironmentHelper.isInternalUser()){
//				errorMsg +="<br>"+ex.toString();
//			}
//		}
//		try {
//			oos.writeObject(errorLog);
//			oos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		error(ex);
	}
}
