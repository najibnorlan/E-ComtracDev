package com.ssm.llp.base.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeGenerator {

	public static String WORK_DIR = "D:/QR/";

	public static BufferedImage logoBi;

	public static void main(String[] args) throws Exception {

		String content = "1.1:PTBuSUM5a1Vpb2pJbEJYZVVsSGRwUm5ibEpDTGlNVE14SUROd01ETTVFRE15SWlPaWNYWk85bVQ1UlhhMDVXWml3aUlVMUNPeFVUTnprak13QWpJNkl5Yk9sSGRwUm5ibEp5ZQ==";

		for (int i = 0; i < 1; i++) {
			byte[] biWithLogo = generateBarcode(content,"200503074807 (SA0010875 - X)","KOPERASI KEUSAHAWANAN KAMPUNG PERMATANG PASIR KUALA LANGAT SELANGOR BE");//KOPERASI KEUSAHAWANAN KAMPUNG PERMATANG PASIR KUALA LANGAT SELANGOR BE
			FileOutputStream fos = new FileOutputStream(WORK_DIR + "test" + i + ".png");
			fos.write(biWithLogo);
			fos.close();
		}

		// buffToFile(biWithLogo, "biWithLogoNew.png");

		System.out.println("Done");
	}

	private static void buffToFile(BufferedImage buffImage, String fileName) throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(buffImage, "jpg", baos);
		byte[] bytes = baos.toByteArray();

		FileOutputStream fos = new FileOutputStream(WORK_DIR + fileName);
		fos.write(baos.toByteArray());
		fos.close();

		baos.close();

	}

	public static byte[] generateBarcode(String content, String entityNo, String entityName) throws Exception {
		BufferedImage biWithLogo = generateBarcodeBufferedImage(content, entityNo, entityName);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(biWithLogo, "jpg", baos);
		byte[] bytes = baos.toByteArray();
		baos.close();
		return bytes;
	}

	private static BufferedImage generateBarcodeBufferedImage(String content, String entityNo, String entityName) throws Exception {

		QRCodeWriter barcodeWriter = new QRCodeWriter();

		HashMap<EncodeHintType, Object> hintMap = new HashMap<EncodeHintType, Object>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
		hintMap.put(EncodeHintType.MARGIN, 0);

		int width = 400;
		int height = 400;
		BitMatrix bitMatrix = barcodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hintMap);

		BufferedImage qrCode = MatrixToImageWriter.toBufferedImage(bitMatrix);

		// Add White Border
		int borderSize = 10;
		BufferedImage qrCodeWithBorder = new BufferedImage(qrCode.getWidth(), qrCode.getHeight(), qrCode.getType());
		Graphics gBorder = qrCodeWithBorder.createGraphics();
		// Fill With White
		gBorder.setColor(Color.WHITE);
		gBorder.fillRect(0, 0, qrCode.getWidth(), qrCode.getHeight());

		gBorder.drawImage(qrCode.getScaledInstance(qrCodeWithBorder.getWidth() - borderSize, qrCodeWithBorder.getHeight() - borderSize,
				BufferedImage.SCALE_SMOOTH), borderSize / 2, borderSize / 2, null);
		gBorder.dispose();

		// Scale Logo
		if (logoBi == null) {
			logoBi = ImageIO.read(QRCodeGenerator.class.getResource("logoSSM.png"));
		}
		double SCALE_PARAM = 4.5;
		double scalePercent = (qrCodeWithBorder.getHeight() / SCALE_PARAM) / logoBi.getHeight();

		Integer scaledWidth = (int) (logoBi.getWidth() * scalePercent);
		Integer scaledHeight = (int) (logoBi.getHeight() * scalePercent);

		BufferedImage logoBiScale = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics gLogo = logoBiScale.createGraphics();
		gLogo.drawImage(logoBi.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH), 0, 0, new Color(0, 0, 0), null);
		gLogo.dispose();

		// Merge QR with Logo
		BufferedImage combined = new BufferedImage(qrCodeWithBorder.getWidth(), qrCodeWithBorder.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) combined.getGraphics();

		int xPos = (qrCodeWithBorder.getWidth() / 2) - (logoBiScale.getWidth() / 2);
		int yPos = (qrCodeWithBorder.getHeight() / 2) - (logoBiScale.getHeight() / 2);

		g2.drawImage(qrCodeWithBorder, 0, 0, null);

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.75));

		g2.drawImage(logoBiScale, xPos, yPos, null);
		g2.dispose();

		// Add Text
		int textExtraHeight = 60;
		BufferedImage qrCodeWithText = new BufferedImage(combined.getWidth(), combined.getHeight() + textExtraHeight, combined.getType());
		Graphics2D gText = qrCodeWithText.createGraphics();
		// Fill With White
		gText.setColor(Color.WHITE);
		gText.fillRect(0, 0, combined.getWidth(), combined.getHeight() + textExtraHeight);

		gText.drawImage(combined, 0, 0, null);

		int textSize = 15;
		gText.setColor(Color.BLACK);
		gText.setFont(new Font("Verdana", Font.CENTER_BASELINE, textSize));
//		gText.drawString("TEST 1 2 3", 10, combined.getHeight() + 20);

		String qrTextArray[] = StringUtils.splitByWholeSeparator(entityName, " ");
		
		String firstLine = "";
		int idx = 0;
		for (idx = 0; idx < qrTextArray.length; idx++) {
			if( (firstLine.length()+ qrTextArray[idx].length()) >40) {
				break;
			}
			firstLine+=qrTextArray[idx]+" ";
		}
		String secondLine = "";
		for (; idx < qrTextArray.length; idx++) {
			secondLine+=qrTextArray[idx]+" ";
		}
		
		Font scaleFontFirstLine = scaleFontToFit(firstLine,qrCodeWithText.getWidth(), gText, gText.getFont());
		if(scaleFontFirstLine.getSize() <gText.getFont().getSize()) {
			gText.setFont(scaleFontFirstLine);
		}
		
		Font scaleFontSecondLine = scaleFontToFit(secondLine,qrCodeWithText.getWidth(),gText,gText.getFont());
		if(scaleFontSecondLine.getSize()<gText.getFont().getSize()) {
			gText.setFont(scaleFontSecondLine);
		}
		
		
		int txtYPos = combined.getHeight() + 15;
		setTextCenter(gText, firstLine.trim(), qrCodeWithText, txtYPos);
		txtYPos+=20;
		
		if(StringUtils.isNotBlank(secondLine)) {
			setTextCenter(gText, secondLine.trim(), qrCodeWithText, txtYPos);
			txtYPos+=20;
		}
		setTextCenter(gText, entityNo, qrCodeWithText, txtYPos);

		gText.dispose();

		return qrCodeWithText;
	}
	
	public static Font scaleFontToFit(String text, int width, Graphics g, Font pFont)
	{
	    float fontSize = pFont.getSize();
	    float fWidth = g.getFontMetrics(pFont).stringWidth(text);
	    if(fWidth <= width)
	        return pFont;
	    fontSize = ((float)width / fWidth) * fontSize;
	    fontSize = (float) Math.floor(fontSize);//because Font Size is Int
	    return pFont.deriveFont(fontSize);
	}

	private static void setTextCenter(Graphics2D graphics2DImage, String string, BufferedImage bgImage, int yPos) {
		int stringWidthLength = (int) graphics2DImage.getFontMetrics().getStringBounds(string, graphics2DImage).getWidth();
		int stringHeightLength = (int) graphics2DImage.getFontMetrics().getStringBounds(string, graphics2DImage).getHeight();

		int horizontalCenter = bgImage.getWidth() / 2 - stringWidthLength / 2;
		int verticalCenter = bgImage.getHeight() / 2 - stringHeightLength / 2;
		graphics2DImage.drawString(string, horizontalCenter, yPos);
	}

}
