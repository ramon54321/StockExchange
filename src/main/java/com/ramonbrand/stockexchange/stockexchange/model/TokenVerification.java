package com.ramonbrand.stockexchange.stockexchange.model;

import java.io.UnsupportedEncodingException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenVerification {

    public static boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("projectbox")).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            Claim payload = jwt.getClaim("id");
            System.out.println("Token used with userId: " + payload.asInt());
            return true;
        }  catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
            return false;
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            return false;
        }
    }

    public static long getTokenUserId(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("projectbox")).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            Claim payload = jwt.getClaim("id");
            return (long) payload.asInt();
        }  catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
            return -1;
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            return -1;
        }
    }

}
