package com.logicea.cards.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
public class CardDTO {
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Color should be in the format '#xxxxxx' where x is a hexadecimal character")
    private String color;

    private String status;
}