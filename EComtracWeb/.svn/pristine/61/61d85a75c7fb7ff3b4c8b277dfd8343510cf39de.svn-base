package com.ssm.llp.base.wicket.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.wicket.request.resource.DynamicImageResource;

public class SSMCaptchaResource extends DynamicImageResource{

	public String captchaWord;
	private byte[] imageData;
	
	public SSMCaptchaResource(){
		captchaWord = randomString(4, 6);
//		System.out.println(captchaWord);
		generateImage();
	}
	private void generateImage(){
		int width = 230;
        int height = 50;
        int fontSize = 30;
        int xGap = 30 ;
        int yGap = 25 ;
        String fontName = "Arial" ;
        Color gradiantStartColor = new Color(90, 90, 90); // dark grey
        Color gradiantEndColor = new Color(150, 150, 150); // light grey
        Color textColor =  new Color(255, 153, 0); // orange

        String[] newData = {captchaWord};//, "orlando", "global", "publish", "looky"}; // you add more words or read them from db or something...

        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        GradientPaint gp = new GradientPaint(0, 0, gradiantStartColor , 0, height / 2, gradiantEndColor, true);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        Random r = new Random();

        for (int i = 0; i < width - 10; i = i + 25) {
            int q = Math.abs(r.nextInt()) % width;
            int colorIndex = Math.abs(r.nextInt()) % 200;
            g2d.setColor(new Color(colorIndex, colorIndex, colorIndex));
            g2d.drawLine(i, q, width, height);
            g2d.drawLine(q, i, i, height);
        }

        g2d.setColor(textColor);

        int index = Math.abs(r.nextInt()) % newData.length;

        String captcha = newData[index];
//        request.getSession().setAttribute("captcha", captcha);

        int x = 0;
        int y = 0;

        for (int i = 0; i < newData[index].length(); i++) {
            Font font = new Font(fontName , Font.BOLD, fontSize);
            g2d.setFont(font);
            x += xGap + (Math.abs(r.nextInt()) % 15);
            y = yGap + Math.abs(r.nextInt()) % 20;

            g2d.drawChars(newData[index].toCharArray(), i, 1, x, y);
        }

        for (int i = 0; i < width - 10; i = i + 25) {
            int p = Math.abs(r.nextInt()) % width;
            int q = Math.abs(r.nextInt()) % width;
            int colorIndex = Math.abs(r.nextInt()) % 200;
            g2d.setColor(new Color(colorIndex, colorIndex, colorIndex));
            g2d.drawLine(p, 0, i + p, q);
            g2d.drawLine(p, 0, i + 25, height);
        }

        g2d.dispose();

//        response.setContentType("image/png");
//        OutputStream os = response.getOutputStream();
//        FileOutputStream fos = new FileOutputStream("test.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
//        fos.close();
        imageData = baos.toByteArray();
	}
	
	@Override
	protected byte[] getImageData(Attributes arg0) {
		return imageData;
	}
	
	/**
	 * Causes the image to be redrawn the next time its requested.
	 */
	public final void redraw()
	{
		imageData = null;
		captchaWord = randomString(4, 6);
//		System.out.println(captchaWord);
		generateImage();
	}
	
    private static int randomInt(int min, int max)
	{
		return (int)(Math.random() * (max - min) + min);
	}

	private static String randomString(int min, int max)
	{
		int num = randomInt(min, max);
		byte b[] = new byte[num];
		for (int i = 0; i < num; i++)
			b[i] = (byte)randomInt('a', 'z');
		return new String(b).toUpperCase();
	}

	public String getCaptchaWord() {
		return captchaWord;
	}

	public void setCaptchaWord(String captchaWord) {
		this.captchaWord = captchaWord;
	}

}
