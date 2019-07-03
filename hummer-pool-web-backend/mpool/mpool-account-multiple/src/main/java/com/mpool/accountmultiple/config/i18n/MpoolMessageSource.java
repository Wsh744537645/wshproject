package com.mpool.accountmultiple.config.i18n;

import com.mpool.accountmultiple.mapper.ResourceMapper;
import com.mpool.common.model.account.i18n.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Component
@Primary
public class MpoolMessageSource implements MessageSource {
	@Autowired
	private ResourceMapper resourceMapper;

	@Override
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		Resource resource = resourceMapper.findByCodeAndLocale(code, locale.toString());
		if (resource == null) {
			resource = new Resource();
			resource.setCode(code);
			resource.setLocale(locale.toString());
			resource.setMessage(defaultMessage);
			resourceMapper.insert(resource);
			return defaultMessage;
		}
		return resource.getMessage();
	}

	@Override
	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		Resource resource = resourceMapper.findByCodeAndLocale(code, locale.toString());
		if (resource == null) {
			resource = new Resource();
			resource.setCode(code);
			resource.setLocale(locale.toString());
			resourceMapper.insert(resource);
			return code;
		}
		return resource.getMessage();
	}

	@Override
	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		String[] codes = resolvable.getCodes();
		for (String code : codes) {
			Resource resource = resourceMapper.findByCodeAndLocale(code, locale.toString());
			if (resource == null) {
				resource = new Resource();
				resource.setCode(code);
				resource.setLocale(locale.toString());
				resourceMapper.insert(resource);
			} else {
				return resource.getMessage();
			}
		}

		throw new NoSuchMessageException(Arrays.toString(codes), locale);
	}

}
