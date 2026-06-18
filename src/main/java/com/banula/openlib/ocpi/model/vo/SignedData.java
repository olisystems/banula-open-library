package com.banula.openlib.ocpi.model.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignedData {

    @JsonProperty("encoding_method")
    @NotEmpty(message = "Encoding method cannot be empty")
    @Size(max = 36, message = "Encoding method cannot be longer than 36 characters")
    private String encodingMethod;

    @JsonProperty("encoding_method_version")
    private Integer encodingMethodVersion;

    @JsonProperty("public_key")
    private String publicKey;

    @JsonProperty("signed_values")
    @NotNull(message = "Signed values cannot be null")
    @Size(min = 1, message = "At least one signed value must be present")
    private List<SignedValue> signedValues;

    @JsonProperty("url")
    @Size(max = 512, message = "URL cannot be longer than 512 characters")
    private String url;

}
