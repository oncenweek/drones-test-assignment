package org.example.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class MedicationDto {

    private Long id;

    private String name;

    private Integer weight;

    private String code;

    private String image;
}
