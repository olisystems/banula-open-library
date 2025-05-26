package com.banula.openlib.ocpi.model.vo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class DisplayText {

    /**
     * Language Code ISO 639-1.
     */
    @NotEmpty(message = "Language cannot be empty.")
    private String language;

    /**
     * Text to be displayed to an end user. No markup, HTML, etc. allowed.
     */
    @NotEmpty(message = "Text cannot be empty.")
    private String text;

    public DisplayText(String language, String text) {
        this.language = language;
        this.text = text;
    }
}
