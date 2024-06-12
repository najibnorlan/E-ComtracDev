package com.ssm.ezbiz.biztrust;

import java.io.FileInputStream;

import org.apache.wicket.request.resource.DynamicImageResource;

import com.ssm.llp.base.utils.QRCodeGenerator;

public class SSMQRResource extends DynamicImageResource{

	public String captchaWord;
	private byte[] imageData = new byte[0];
	
	public SSMQRResource(){
//		System.out.println(captchaWord);
//		generateImage("");
	}
	public  byte[] generateImage(String qrText, String entityNo, String entityName){
//		try {
//
//			FileInputStream fis = new FileInputStream("d:/barcode.jpg");
//			byte byteData[] = new byte[fis.available()];
//			fis.read(byteData);
//			fis.close();
//			
//			imageData = byteData;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		try {
			byte byteData[] = QRCodeGenerator.generateBarcode(qrText, entityNo, entityName);
			imageData = byteData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageData;
//        response.setContentType("image/png");
//        OutputStream os = response.getOutputStream();
//        FileOutputStream fos = new FileOutputStream("test.png");
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//			ImageIO.write(bufferedImage, "png", baos);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
////        fos.close();
//        imageData = baos.toByteArray();
	}
	
	@Override
	protected byte[] getImageData(Attributes arg0) {
		return imageData;
	}
	
//	/**
//	 * Causes the image to be redrawn the next time its requested.
//	 */
//	public final void redraw(String qrText)
//	{
//		imageData = null;
////		System.out.println(captchaWord);
//		generateImage(qrText);
//	}
	
}
