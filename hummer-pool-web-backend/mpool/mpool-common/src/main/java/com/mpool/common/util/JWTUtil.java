package com.mpool.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtil {

	private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);

	// 过期时间2分钟
	private static final int EXPIRE_TIME = 2;

	private static final String secret = "Asfasafasfwqfqwfrqe";

	static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<>();

	/**
	 * 校验token是否正确
	 * 
	 * @param token
	 *            密钥
	 * @param secret
	 *            用户的密码
	 * @return 是否正确
	 */
	public static boolean verify(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).build();
			verifier.verify(token);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 * 
	 * @return token中包含的用户名
	 */
	public static Map<String, String> getTokenValue(String token) {
		try {
			Map<String, String> map = threadLocal.get();
			if (map != null) {
				return map;
			}
			DecodedJWT jwt = JWT.decode(token);
			Map<String, Claim> claims = jwt.getClaims();
			Map<String, String> tokenValue = new HashMap<>();
			for (Entry<String, Claim> entry : claims.entrySet()) {
				tokenValue.put(entry.getKey(), entry.getValue().asString());
			}
			threadLocal.set(tokenValue);
			return tokenValue;
		} catch (JWTDecodeException e) {
			log.error("token error");
			return null;
			// throw new RuntimeException("token error");
		}
	}

	/**
	 * 生成Jwt,5min后过期
	 * 
	 * @param username
	 *            用户名
	 * @param secret
	 *            用户的密码
	 * @return 加密的token
	 */
	public static String sign(Map<String, String> playload) {
		Date issuedAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Calendar nowDate = Calendar.getInstance();
		nowDate.add(Calendar.MINUTE, EXPIRE_TIME);
		Date expiresAt = nowDate.getTime();
		Algorithm algorithm;
		try {
			algorithm = Algorithm.HMAC256(secret);
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			log.error("Algorithm ERROR", e);
			throw new RuntimeException("Algorithm ERROR");
		}
		Map<String, Object> headerClaims = new HashMap<>();
		headerClaims.put("alg", "HS256");
		headerClaims.put("typ", "JWT");
		Builder jwt = JWT.create();
		jwt.withHeader(headerClaims);
		for (Entry<String, String> mapEntry : playload.entrySet()) {
			String name = mapEntry.getKey();
			String value = mapEntry.getValue();
			jwt.withClaim(name, value);
		}
		jwt.withExpiresAt(expiresAt);
		jwt.withIssuedAt(issuedAt);
		String token = jwt.sign(algorithm);
		return token;
	}
}
