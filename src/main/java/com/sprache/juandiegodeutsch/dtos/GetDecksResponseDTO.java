package com.sprache.juandiegodeutsch.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class GetDecksResponseDTO implements Serializable {
    private Long id;
    private String name;
    private String description;

}
