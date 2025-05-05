package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.banula.ocpi.model.enums.TokenType;
import lombok.*;

/**
 * Defines the set of values that identify a token to which a Location might be published.
 * At least one of the following fields SHALL be set: uid, visual_number, or group_id.
 * When uid is set, type SHALL also be set.
 * When visual_number is set, issuer SHALL also be set.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PublishTokenType {

    /**
     * Unique ID by which this Token can be identified.
     */
    @JsonProperty("uid")
    private String uid;
    /**
     * Type of the token.
     */
    @JsonProperty("type")
    private TokenType type;
    /**
     * Visual readable number/identification as printed on the Token (RFID card).
     */
    @JsonProperty("visual_number")
    private String visualNumber;
    /**
     * Issuing company, most of the times the name of the company printed on the token (RFID card),
     * not necessarily the eMSP
     */
    @JsonProperty("issuer")
    private String issuer;
    /**
     * This ID groups a couple of tokens. This can be used to make two or more tokens work as one.
     */
    @JsonProperty("group_id")
    private String groupId;

}
