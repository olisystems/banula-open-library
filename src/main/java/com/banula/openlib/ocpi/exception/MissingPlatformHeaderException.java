package com.banula.openlib.ocpi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingPlatformHeaderException extends RuntimeException {
  public MissingPlatformHeaderException(String countryParam, String partyParam) {
    super("Missing required platform parameters: '" + countryParam + "' and/or '" + partyParam + "'");
  }
}
