package com.sprache.juandiegodeutsch.dtos;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TemplateRequestDTO {

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "description cannot be empty")
    private String description;

    private String LenguageLevel;

}
