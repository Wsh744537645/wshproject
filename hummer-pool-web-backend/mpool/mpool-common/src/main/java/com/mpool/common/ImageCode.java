package com.mpool.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageCode {

	private static char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private String code;

	private BufferedImage image;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void generateImage(int width, int height) {

		int x = 0, fontHeight = 0, codeY = 0;
		int red = 0, green = 0, blue = 0;

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics gd = image.createGraphics();
		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);
		fontHeight = height / 2;
		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		// 设置字体。
		gd.setFont(font);
		// 画边框。
		gd.setColor(Color.red);
		gd.drawRect(0, 0, width - 1, height - 1);

		// 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
		Random random = new Random();
		gd.setColor(Color.BLACK);
		for (int i = 0; i < 30; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width / 8);
			int ye = ys + random.nextInt(height / 8);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			gd.setColor(new Color(red, green, blue));
			gd.drawLine(xs, ys, xe, ye);
		}
		if ("".equals(code) || null == code) {
			initCode(4);
		}
		x = width / (code.length() + 2);// 每个字符的宽度
		fontHeight = height - 2;// 字体的高度
		codeY = height - 4;
		for (int i = 0; i < code.length(); i++) {
			String c = code.charAt(i) + "";
			System.out.println(c);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			gd.setColor(new Color(red, green, blue));
			System.out.println(gd.getColor());
			gd.drawString(c, (i + 1) * x, codeY);
		}
	}

	public void initCode(int length) {
		Random random = new Random();
		StringBuffer flag = new StringBuffer();
		for (int j = 0; j < length; j++) {
			flag.append(codeSequence[random.nextInt(codeSequence.length)]);
		}
		code = flag.toString();
	}

}
