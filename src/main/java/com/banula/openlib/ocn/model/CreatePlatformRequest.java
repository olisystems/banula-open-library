package com.banula.openlib.ocn.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePlatformRequest {

    private List<BasicRole> roles;

    private String tokenA;

    private Boolean handshakeSelfInitiated;

    private String platformVersionsUrl;
}
