package com.banula.ocpi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.ocn.client.OcnClient;
import com.banula.ocpi.exception.OCPICustomException;
import com.banula.ocpi.util.Constants;
import com.banula.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.ocpi.util.OCPILocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class OcpiErrorResponse {
    private int status_code;
    private String status_message;
    private String ocn_signature;
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime timestamp;

    public OcpiErrorResponse(String errorMessage) {
        this.status_code = Constants.STATUS_CODE_GENERIC_SERVER_ERROR; //by default, we keep this OCPI error code, which means internal server error.
        this.status_message = errorMessage;
        this.timestamp = LocalDateTime.now();

        try {
            this.ocn_signature = OcnClient.signErrorResponse(this);
        } catch (Exception ex) {
            throw new OCPICustomException("Error happened while signing the response", 2001);
        }
    }

    public OcpiErrorResponse(String errorMessage, int statusCode) {
        this.status_code = statusCode;
        this.status_message = errorMessage;
        this.timestamp = LocalDateTime.now();
        try {
            this.ocn_signature = OcnClient.signErrorResponse(this);
        } catch (Exception ex) {
            throw new OCPICustomException("Error happened while signing the response", 2001);
        }
    }
}
