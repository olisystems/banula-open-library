package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * BusinessDetails class representing the business details including name, website, and logo.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDetails {

    /**
     * Name of the operator.
     */
    @JsonProperty("name")
    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    /**
     * Link to the operator’s website.
     */
    @JsonProperty("website")
    @Size(max = 255, message = "Website URL must be less than or equal to 255 characters.")
    private String website;

    /**
     * Image link to the operator’s logo.
     */
    @JsonProperty("logo")
    private Image logo;

    public BusinessDetails(String name, String website) {
        this.name = name;
        this.website = website;
    }

}
