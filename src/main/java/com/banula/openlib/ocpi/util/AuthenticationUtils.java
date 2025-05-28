package com.banula.openlib.ocpi.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public final class AuthenticationUtils {

    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader.contains("Token")) {
            try {
                String token = authorizationHeader.replace("Token ", "");
                return new String(Base64.getDecoder().decode(token));
            } catch (IllegalArgumentException e) {
                log.error("Error converting Base64 value: " + e.getLocalizedMessage());
                return authorizationHeader;
            }
        }

        return authorizationHeader;
    }

}
