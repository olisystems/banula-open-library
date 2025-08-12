package com.banula.openlib.ocpi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class PlatformRequestValues {
  private final String countryCode;
  private final String partyId;
}
