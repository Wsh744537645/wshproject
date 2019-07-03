package com.mpool.common;

import java.util.Properties;

import org.springframework.stereotype.Component;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/**
 * 验证码
 * 
 * @author chenjian2
 *
 */
@Component
public class MpoolKaptcha extends DefaultKaptcha {

	public MpoolKaptcha() {
		super();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "no");
		// properties.setProperty("kaptcha.border.color", "105,179,90");
		// properties.setProperty("kaptcha.textproducer.font.color", "blue");
		properties.setProperty("kaptcha.image.width", "120");
		properties.setProperty("kaptcha.image.height", "30");
		properties.setProperty("kaptcha.textproducer.font.size", "25");
		properties.setProperty("kaptcha.session.key", "code");
		//properties.setProperty("kaptcha.textproducer.char.length", "4");
		properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
		Config config = new Config(properties);
		setConfig(config);
	}

}
