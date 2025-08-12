package com.banula.openlib.ocpi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PlatformRequest {

  String countryHeader() default "countryCode";

  String partyHeader() default "partyId";

  boolean required() default true;
}
