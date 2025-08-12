package com.banula.openlib.ocpi.annotation.processor;

import com.banula.openlib.ocpi.annotation.PlatformRequest;
import com.banula.openlib.ocpi.exception.MissingPlatformHeaderException;
import com.banula.openlib.ocpi.model.PlatformRequestValues;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Resolves method parameters annotated with {@link PlatformRequest} into
 * {@link PlatformRequestValues}.
 */
public class PlatformHeadersResolver implements HandlerMethodArgumentResolver {

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

    String countryHeaderName = annotation.countryHeader();
    String partyHeaderName = annotation.partyHeader();

    String countryCode = request.getHeader(countryHeaderName);
    String partyId = request.getHeader(partyHeaderName);

    if (annotation.required()) {
      if (countryCode == null || partyId == null) {
        throw new MissingPlatformHeaderException(countryHeaderName, partyHeaderName);
      }
    }

    return new PlatformRequestValues(countryCode, partyId);
  }
}
