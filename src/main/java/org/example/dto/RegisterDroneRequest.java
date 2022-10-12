package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.domain.DroneClass;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegisterDroneRequest {

    @NotBlank
    @Size(max = 100)
    private String serialNumber;

    private DroneClass model;

    @Positive
    @Max(500)
    private Integer weightLimit;
}
