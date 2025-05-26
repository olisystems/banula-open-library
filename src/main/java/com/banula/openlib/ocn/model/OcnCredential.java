package com.banula.openlib.ocn.model;

import lombok.Data;

@Data
public class OcnCredential {
    private String id;
    private String tokenA;
    private String tokenC;
    private String tokenB;
    private String countryCode;
    private String partyId;
}
