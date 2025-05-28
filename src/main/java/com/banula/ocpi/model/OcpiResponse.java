package com.banula.ocpi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.ocn.client.OcnClient;
import com.banula.ocpi.exception.OCPICustomException;
import com.banula.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.ocpi.util.OCPILocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OcpiResponse<T> {
    private T data;
    private int status_code;
    private String status_message;
    private String ocn_signature;

    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime timestamp;

    public OcpiResponse(T data,  int statusCode, String statusMessage) {
        this.data = data;
        this.status_code = statusCode;
        this.status_message = statusMessage;
        this.timestamp = LocalDateTime.now();

        try {
            this.ocn_signature = OcnClient.signResponse(this);
        } catch (Exception ex) {
            throw new OCPICustomException("Error happened while signing the response", 2001);
        }
    }

    public OcpiResponse(T data) {
        this.data = data;
        this.status_code = 1000;
        this.status_message = "";
        this.timestamp = LocalDateTime.now();

        try {
            this.ocn_signature = OcnClient.signResponse(this);
        } catch (Exception ex) {
            throw new OCPICustomException("Error happened while signing the response", 2001);
        }
    }
}
