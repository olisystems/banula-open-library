package com.banula.openlib.ocpi.annotation.processor;

import com.banula.openlib.ocpi.annotation.PlatformRequest;
import com.banula.openlib.ocpi.exception.MissingPlatformHeaderException;
import com.banula.openlib.ocpi.exception.PlatformPartyNotRegistered;
import com.banula.openlib.ocpi.model.PlatformRequestValues;
import com.banula.openlib.ocpi.validation.PlatformRequestValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

/**
 * Resolves method parameters annotated with {@link PlatformRequest} into
 * {@link PlatformRequestValues} by extracting tenant identifier from URL path
 * parameters.
 */
@Component
@RequiredArgsConstructor
public class PlatformHeadersResolver implements HandlerMethodArgumentResolver {

  private final ApplicationContext applicationContext;

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

    String tenant;

    // Extract from URL path parameters
    @SuppressWarnings("unchecked")
    Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(
        HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    if (pathVariables != null) {
      tenant = pathVariables.get(annotation.tenantPath());
    } else {
      tenant = null;
    }

    if (annotation.required() && tenant == null) {
      throw new MissingPlatformHeaderException(annotation.tenantPath(), "tenant");
    }

    // Validate platform request if validation component exists
    if (tenant != null) {
      validatePlatformRequest(tenant);
    }

    return new PlatformRequestValues(tenant);
  }

  /**
   * Validates platform request by checking if a validation component exists
   * and calling its validatePlatformRequest method.
   * 
   * @param tenant the tenant identifier to validate
   * @throws PlatformPartyNotRegistered if validation fails
   */
  private void validatePlatformRequest(String tenant) {
    try {
      // Look for any component that implements PlatformRequestValidator
      String[] beanNames = applicationContext.getBeanNamesForType(PlatformRequestValidator.class);

      if (beanNames.length > 0) {
        // Get the first validator (you could also iterate through all if needed)
        PlatformRequestValidator validator = applicationContext.getBean(beanNames[0], PlatformRequestValidator.class);

        if (!validator.validatePlatformRequest(tenant)) {
          throw new PlatformPartyNotRegistered(tenant);
        }
      }
      // If no validation component found, assume valid (optional validation)
    } catch (Exception e) {
      if (e instanceof PlatformPartyNotRegistered) {
        throw e;
      }
      // For any other exception, throw PlatformPartyNotRegistered
      throw new PlatformPartyNotRegistered(tenant);
    }
  }
}
