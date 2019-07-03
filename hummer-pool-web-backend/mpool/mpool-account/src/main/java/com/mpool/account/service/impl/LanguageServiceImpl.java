package com.mpool.account.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpool.account.mapper.LanguageMapper;
import com.mpool.account.service.LanguageService;
import com.mpool.common.model.account.Language;

@Service
public class LanguageServiceImpl implements LanguageService {
	@Autowired
	private LanguageMapper languageMapper;

	@Override
	public Map<String, Object> getUserLang(String langType) {
		// TODO Auto-generated method stub
		List<Language> langs = languageMapper.findByLangType(langType);
		Map<String, String> src = new HashMap<>();
		for (Language sysLang : langs) {
			src.put(sysLang.getLangKey(), sysLang.getLangValue());
		}

		final Map<String, Object> resultmap = new BundleMap().serialize(src);
		return resultmap;
	}

	@Override
	public void insert(Language entity) {
		languageMapper.insert(entity);
	}

	@Override
	public void inserts(List<Language> entitys) {
		for (Language language : entitys) {
			languageMapper.insert(language);
		}
	}

	@Override
	public void update(Language entity) {
		languageMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		languageMapper.deleteById(id);
	}

	@Override
	public Language findById(Serializable id) {
		return languageMapper.selectById(id);
	}

}

class BundleMap {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleMap.class);

	public Map<String, Object> serialize(Map<String, String> src) {
		final Map<String, Object> resultMap = new HashMap<>();

		for (final String key : src.keySet()) {
			try {
				createFromBundleKey(resultMap, key, src.get(key));
			} catch (final IOException e) {
				LOGGER.error("Bundle map serialization exception: ", e);
			}
		}
		return resultMap;
	}

	private static Map<String, Object> createFromBundleKey(final Map<String, Object> resultMap, final String key,
			final String value) throws IOException {
		if (!key.contains(".")) {
			resultMap.put(key, value);
			return resultMap;
		}

		final String currentKey = firstKey(key);
		if (currentKey != null) {
			final String subRightKey = key.substring(currentKey.length() + 1, key.length());
			final Map<String, Object> childMap = getMapIfExists(resultMap, currentKey);

			resultMap.put(currentKey, createFromBundleKey(childMap, subRightKey, value));
		}

		return resultMap;
	}

	private static String firstKey(final String fullKey) {
		final String[] splittedKey = fullKey.split("\\.");

		return (splittedKey.length != 0) ? splittedKey[0] : fullKey;
	}

	private static Map<String, Object> getMapIfExists(final Map<String, Object> parent, final String key) {
		if (parent == null) {
			LOGGER.warn("Parent map parameter is null!");
			return null;
		}

		if (parent.get(key) != null && !(parent.get(key) instanceof Map)) {
			throw new IllegalArgumentException("Invalid key \'" + key + "\' for parent: " + parent
					+ "\nKey can not be Map object and value or array in one time");
		}

		if (parent.containsKey(key)) {
			return (Map<String, Object>) parent.get(key);
		} else {
			return new HashMap<String, Object>();
		}
	}

}
