package com.mpool.common.util;

import com.mpool.common.ImageCode;

public class CodeUtil {
	public static ImageCode generateCodeImage() {
		ImageCode imageCode = new ImageCode();
		imageCode.initCode(1);
		imageCode.generateImage(20 * 6, 30);
		return imageCode;
	}
}
