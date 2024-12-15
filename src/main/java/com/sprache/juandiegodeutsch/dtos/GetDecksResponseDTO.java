package com.sprache.juandiegodeutsch.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetDecksResponseDTO {
    private Long id;
    private String name;
    private String description;

}
