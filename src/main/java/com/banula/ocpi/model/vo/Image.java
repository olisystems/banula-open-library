package com.banula.ocpi.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.banula.ocpi.model.enums.ImageCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Image {

    @JsonIgnore
    @NotNull(message = "url must not be null")
    @NotEmpty(message = "url must not be empty")
    @Size(max = 255, message = "url must be at most 255 characters")
    private String url;

    @JsonIgnore
    @Size(max = 255, message = "thumbnail must be at most 255 characters")
    private String thumbnail;

    @JsonIgnore
    @NotNull(message = "category must not be null")
    private ImageCategory category;

    @JsonIgnore
    @NotNull(message = "type must not be null")
    @NotEmpty(message = "type must not be empty")
    @Size(max = 4, message = "type must be at most 4 characters")
    private String type;

    private String width;

    private String height;


}
