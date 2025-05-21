package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * This class contains the signed and the plain/unsigned data. By decoding the data, the receiver can check
 * if the content has not been altered.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignedValue {

    /**
     * Nature of the value, in other words, the event this value belongs to.
     * Possible values at the moment of writing:
     * - Start (value at the start of the Session)
     * - End (signed value at the end of the Session)
     * - Intermediate (signed values taken during the Session, after Start, before End)
     * Others might be added later.
     */
    @NotEmpty(message = "Nature must not be empty")
    @Size(max = 32, message = "Nature length must be at most 32 characters")
    @JsonProperty("nature")
    private String nature;

    /**
     * The un-encoded string of data. The format of the content depends on the EncodingMethod field.
     */
    @NotEmpty(message = "Plain data must not be empty")
    @Size(max = 512, message = "Plain data length must be at most 512 characters")
    @JsonProperty("plain_data")
    private String plainData;

    /**
     * Blob of signed data, base64 encoded. The format of the content depends on the EncodingMethod field.
     */
    @NotEmpty(message = "Signed data must not be empty")
    @Size(max = 5000, message = "Signed data length must be at most 5000 characters")
    @JsonProperty("signed_data")
    private String signedData;

}
