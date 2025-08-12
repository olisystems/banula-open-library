package com.banula.openlib.ocpi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingPlatformHeaderException extends RuntimeException {
  public MissingPlatformHeaderException(String countryHeader, String partyHeader) {
    super("Missing required headers: '" + countryHeader + "' and/or '" + partyHeader + "'");
  }
}
