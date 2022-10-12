package org.example.dto;

import lombok.Data;
import org.example.domain.DroneClass;
import org.example.domain.DroneState;

@Data
public class DroneDto {

    private Long id;

    private String serialNumber;

    private DroneClass model;

    private Integer weightLimit;

    private Integer batteryCapacity;

    private DroneState state;
}
