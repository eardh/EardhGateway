package com.eardh.gateway.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.util.Map;

/**
 * @author eardh
 * @date 2023/4/22 14:45
 */
public class JwtUtils {

    private static final String secret = "eardh-gateway-666";

    private static final Algorithm algorithm = Algorithm.HMAC512(secret);

    public static String createToken(Map<String, Object> payload) {
        Instant now = Instant.now();
        Instant end = now.plusSeconds(60 * 60 * 24 * 60);
        String token = JWT.create()
                .withIssuedAt(now)
                .withExpiresAt(end)
                .withPayload(payload)
                .sign(algorithm);
        return token;
    }

    public static boolean verify(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        if (decodedJWT.getExpiresAtAsInstant().isBefore(Instant.now())
                || decodedJWT.getIssuedAtAsInstant().isAfter(Instant.now())) {
            return false;
        }
        return true;
    }
}
