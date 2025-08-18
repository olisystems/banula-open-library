package com.banula.openlib.ocpi.annotation.processor;

import com.banula.openlib.ocpi.annotation.PlatformRequest;
import com.banula.openlib.ocpi.exception.MissingPlatformHeaderException;
import com.banula.openlib.ocpi.exception.PlatformPartyNotRegistered;
import com.banula.openlib.ocpi.model.PlatformRequestValues;
import com.banula.openlib.ocpi.validation.PlatformRequestValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

/**
 * Resolves method parameters annotated with {@link PlatformRequest} into
 * {@link PlatformRequestValues} by extracting values from URL path parameters.
 */
public class PlatformHeadersResolver implements HandlerMethodArgumentResolver {

  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public boolean supportsParameter(@NonNull MethodParameter parameter) {
    return parameter.hasParameterAnnotation(PlatformRequest.class)
        && PlatformRequestValues.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(@NonNull MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      @NonNull NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory) {
    PlatformRequest annotation = parameter.getParameterAnnotation(PlatformRequest.class);
    if (annotation == null) {
      return null;
    }

    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    if (request == null) {
      return null;
    }

    String countryCode;
    String partyId;

    // Try to extract from URL path parameters first
    @SuppressWarnings("unchecked")
    Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(
        HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    if (pathVariables != null) {
      // Extract from URL path parameters
      countryCode = pathVariables.get(annotation.countryPath());
      partyId = pathVariables.get(annotation.partyPath());
    } else {
      // Fallback to headers for backward compatibility
      String countryHeaderName = annotation.countryHeader();
      String partyHeaderName = annotation.partyHeader();
      countryCode = request.getHeader(countryHeaderName);
      partyId = request.getHeader(partyHeaderName);
    }

    if (annotation.required()) {
      if (countryCode == null || partyId == null) {
        throw new MissingPlatformHeaderException(annotation.countryPath(), annotation.partyPath());
      }
    }

    // Validate platform request if validation component exists
    if (countryCode != null && partyId != null) {
      validatePlatformRequest(countryCode, partyId);
    }

    return new PlatformRequestValues(countryCode, partyId);
  }

  /**
   * Validates platform request by checking if a validation component exists
   * and calling its validatePlatformRequest method.
   * 
   * @param countryCode the country code to validate
   * @param partyId     the party ID to validate
   * @throws PlatformPartyNotRegistered if validation fails
   */
  private void validatePlatformRequest(String countryCode, String partyId) {
    try {
      // Look for any component that implements PlatformRequestValidator
      String[] beanNames = applicationContext.getBeanNamesForType(PlatformRequestValidator.class);

      if (beanNames.length > 0) {
        // Get the first validator (you could also iterate through all if needed)
        PlatformRequestValidator validator = applicationContext.getBean(beanNames[0], PlatformRequestValidator.class);

        if (!validator.validatePlatformRequest(countryCode, partyId)) {
          throw new PlatformPartyNotRegistered(countryCode, partyId);
        }
      }
      // If no validation component found, assume valid (optional validation)
    } catch (Exception e) {
      if (e instanceof PlatformPartyNotRegistered) {
        throw e;
      }
      // For any other exception, throw PlatformPartyNotRegistered
      throw new PlatformPartyNotRegistered(countryCode, partyId);
    }
  }
}
