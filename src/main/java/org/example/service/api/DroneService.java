package org.example.service.api;

import org.example.domain.DroneStateHistory;
import org.example.dto.DroneDto;
import org.example.dto.MedicationDto;
import org.example.dto.RegisterDroneRequest;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface DroneService {
    Integer batteryLevel(@NotNull Long droneId);

    List<MedicationDto> getDronePayload(@NotNull Long droneId);

    Long registerDrone(@NotNull @Valid RegisterDroneRequest request);

    List<DroneDto> getAvailableDrones();

    DroneDto load(@NotNull Long droneId, @NotNull Long medicationId);

    List<MedicationDto> getAllMedications();

    Page<DroneStateHistory> getAuditData(Long droneId, Integer maxValues);
}
