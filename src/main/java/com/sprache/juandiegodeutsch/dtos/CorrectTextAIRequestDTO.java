package com.sprache.juandiegodeutsch.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CorrectTextAIRequestDTO {
    private String level;
    private String topic;
    private String text;


}
