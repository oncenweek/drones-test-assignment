package org.example.mapper;

import org.example.domain.Drone;
import org.example.dto.DroneDto;
import org.example.dto.RegisterDroneRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = MedicationMapper.class)
public interface DroneMapper {

    DroneDto map(Drone domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "payload", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "batteryCapacity", ignore = true)
    Drone map(RegisterDroneRequest request);
}
