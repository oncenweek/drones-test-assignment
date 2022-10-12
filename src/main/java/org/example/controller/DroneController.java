package org.example.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.DroneStateHistory;
import org.example.dto.DroneDto;
import org.example.dto.MedicationDto;
import org.example.dto.RegisterDroneRequest;
import org.example.service.api.DroneService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The service should allow:
 *
 * registering a drone;
 * loading a drone with medication items;
 * checking loaded medication items for a given drone;
 * checking available drones for loading;
 * check drone battery level for a given drone;
 */
@RestController
@AllArgsConstructor
@RequestMapping("/drone")
@Slf4j
public class DroneController {

    private DroneService droneService;

    @PostMapping(value = "register")
    public Long registerDrone(@RequestBody RegisterDroneRequest request) {
        return droneService.registerDrone(request);
    }

    @PostMapping(value = "load/{droneId}")
    public DroneDto loadDrone(@PathVariable Long droneId, @RequestBody Long medicationId) {
        return droneService.load(droneId, medicationId);
    }

    @GetMapping(value = "battery-level/{droneId}")
    public Integer getDroneBatteryLevel(@PathVariable Long droneId) {
        return droneService.batteryLevel(droneId);
    }

    @GetMapping(value = "payload/{droneId}")
    public List<MedicationDto> getDronePayload(@PathVariable Long droneId) {
        return droneService.getDronePayload(droneId);
    }

    @GetMapping(value = "available-for-loading")
    public List<DroneDto> availableDrones() {
        return droneService.getAvailableDrones();
    }

    /**
     * Helper method, return all medications
     */
    @GetMapping(value = "medications")
    public List<MedicationDto> availableMedications() {
        return droneService.getAllMedications();
    }

    /**
     * Helper method for obtaining audit data from periodic task, quite rude because just for test
     * @param droneId id of drone
     * @param maxRecords max audit records in results (audit table can be too large)
     * @return
     */
    @GetMapping(value = "audit/{droneId}/{maxRecords}")
    public Page<DroneStateHistory> getAudit(@PathVariable Long droneId, @PathVariable Integer maxRecords) {
        return droneService.getAuditData(droneId, maxRecords);
    }
}
