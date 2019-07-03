package com.mpool.account.service;

import com.mpool.common.model.account.Language;

import java.util.Map;

public interface LanguageService extends BaseService<Language> {
	Map<String, Object> getUserLang(String langType);
}
