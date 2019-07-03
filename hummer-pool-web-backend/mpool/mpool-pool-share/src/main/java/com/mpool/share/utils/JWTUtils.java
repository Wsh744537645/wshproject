package com.mpool.share.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/11 17:46
 */
public class JWTUtils {

    //过期时间
    private static final long EXPIRE_TIME = 30 * 60 * 1000;

    /**
     * 校验token是否正确
     * @param token
     * @param username
     * @param secret
     * @return
     */
    public static boolean verify(String token, String username, String secret){
        try{
            //先验证token是否被篡改，然后解码token。解密后的DecodedJWT对象，可以读取token中的数据。
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                                        .withClaim("username", username)
                                        .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 获取token中包含的用户名
     * @param token
     * @return
     */
    public static String getUsername(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        }catch (JWTDecodeException e){
            return null;
        }
    }

    public static Long getExpTime(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("exp").asLong();
        }catch (JWTDecodeException e){
            return null;
        }
    }

    /**
     * token是否过期
     * @param token
     * @return
     */
    public static boolean isTokenExpired(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("exp").asLong() * 1000 < new Date().getTime();
        }catch (JWTDecodeException e){
            return true;
        }
    }

    /**
     * 生成签名
     * @param username
     * @param secret
     * @return
     */
    public static String sign(String username, String secret){
        try{
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder jwt = JWT.create();
            Map<String, Object> headerClaims = new HashMap<>();
            headerClaims.put("alg", "HS256");
            headerClaims.put("typ", "JWT");
            jwt.withHeader(headerClaims);
            return jwt.withClaim("username", username)
                                .withExpiresAt(date)
                                .sign(algorithm);
        }catch (UnsupportedEncodingException e){
            return null;
        }
    }

}
