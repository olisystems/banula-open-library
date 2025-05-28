package com.banula.openlib.ocpi.util;

public final class Constants {

    public static final String ASCII_REGEXP = "^[\\p{ASCII}]*$";
    public static final int STATUS_CODE_INVALID_OR_MISSING_PARAMETERS = 2001;
    public static final int STATUS_CODE_AUTHORIZATION_EXCEPTION_CODE = 2002;
    public static final int STATUS_CODE_NOT_ENOUGH_INFORMATION = 2002;
    public static final int STATUS_CODE_UNKNOWN_LOCATION = 2003;
    public static final int STATUS_CODE_INVALID_TOKEN_ERROR = 2004;
    public static final int STATUS_CODE_OK = 1000;
    public static final int STATUS_CODE_GENERIC_SERVER_ERROR = 3000;

    private Constants() {
    }
}
