package com.banula.openlib.ocpi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PlatformPartyNotRegistered extends RuntimeException {

  public PlatformPartyNotRegistered(String tenant) {
    super("Platform party not registered: tenant='" + tenant + "'");
  }
}
