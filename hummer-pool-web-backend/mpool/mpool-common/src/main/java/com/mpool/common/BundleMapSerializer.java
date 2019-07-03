package com.mpool.common;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BundleMapSerializer implements JsonSerializer<Map<String, String>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleMapSerializer.class);

	@Override
	public JsonElement serialize(Map<String, String> src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject resultJson = new JsonObject();

		for (final String key : src.keySet()) {
			try {
				createFromBundleKey(resultJson, key, src.get(key));
			} catch (final IOException e) {
				LOGGER.error("Bundle map serialization exception: ", e);
			}
		}
		return resultJson;
	}

	private static JsonObject createFromBundleKey(final JsonObject resultJson, final String key, final String value)
			throws IOException {
		if (!key.contains(".")) {
			resultJson.addProperty(key, value);
			return resultJson;
		}

		final String currentKey = firstKey(key);
		if (currentKey != null) {
			final String subRightKey = key.substring(currentKey.length() + 1, key.length());
			final JsonObject childJson = getJsonIfExists(resultJson, currentKey);

			resultJson.add(currentKey, createFromBundleKey(childJson, subRightKey, value));
		}

		return resultJson;
	}

	private static String firstKey(final String fullKey) {
		final String[] splittedKey = fullKey.split("\\.");

		return (splittedKey.length != 0) ? splittedKey[0] : fullKey;
	}

	private static JsonObject getJsonIfExists(final JsonObject parent, final String key) {
		if (parent == null) {
			LOGGER.warn("Parent json parameter is null!");
			return null;
		}

		if (parent.get(key) != null && !(parent.get(key) instanceof JsonObject)) {
			throw new IllegalArgumentException("Invalid key \'" + key + "\' for parent: " + parent
					+ "\nKey can not be JSON object and property or array in one time");
		}

		if (parent.getAsJsonObject(key) != null) {
			return parent.getAsJsonObject(key);
		} else {
			return new JsonObject();
		}
	}

}
