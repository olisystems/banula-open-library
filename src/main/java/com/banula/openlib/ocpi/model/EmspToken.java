package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.banula.openlib.ocpi.model.enums.ProfileType;
import com.banula.openlib.ocpi.model.enums.TokenType;
import com.banula.openlib.ocpi.model.enums.WhitelistType;
import com.banula.openlib.ocpi.model.vo.EnergyContract;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeDeserializer;
import com.banula.openlib.ocpi.util.OCPILocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@ToString
@EqualsAndHashCode
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EmspToken {

    private String uid;
    private String countryCode;
    private String partyId;
    private TokenType type;
    private String contractId;
    private String visualNumber;
    private String issuer;
    private String groupId;
    private Boolean valid;
    private WhitelistType whitelist;
    private String language;
    private ProfileType defaultProfileType;
    private EnergyContract energyContract;
    @JsonDeserialize(using = OCPILocalDateTimeDeserializer.class)
    @JsonSerialize(using = OCPILocalDateTimeSerializer.class)
    private LocalDateTime lastUpdated;

}
