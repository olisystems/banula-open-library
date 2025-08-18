package com.banula.openlib.ocpi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PlatformPartyNotRegistered extends RuntimeException {
  public PlatformPartyNotRegistered(String countryCode, String partyId) {
    super("Platform party not registered: countryCode='" + countryCode + "', partyId='" + partyId + "'");
  }
}
