package com.banula.openlib.ocpi.validation;

/**
 * Interface for validating platform requests.
 * Implement this interface in your validation components.
 */
public interface PlatformRequestValidator {

  /**
   * Validates if a platform party is registered using tenant identifier.
   * 
   * @param tenant the tenant identifier to validate
   * @return true if the platform party is registered, false otherwise
   */
  boolean validatePlatformRequest(String tenant);
}
