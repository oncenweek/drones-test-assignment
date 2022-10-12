package org.example.service;

import org.example.domain.Drone;
import org.example.domain.DroneClass;
import org.example.domain.DroneState;
import org.example.domain.Medication;
import org.example.dto.RegisterDroneRequest;
import org.example.exceptions.RegistrationException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repository.DroneRepository;
import org.example.repository.MedicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private DroneServiceImpl droneService;

    @Test
    public void registerDroneTest() {
        var request = getRegisterDroneRequest();

        var domain = Drone.builder()
                .model(DroneClass.LIGHTWEIGHT)
                .serialNumber("NCC-74656")
                .weightLimit(400)
                .batteryCapacity(100)
                .state(DroneState.IDLE)
                .build();

        Mockito.when(droneRepository.findBySerialNumber(request.getSerialNumber())).thenReturn(Optional.empty());
        Mockito.when(droneRepository.save(domain)).thenReturn(domain.toBuilder().id(5L).build());

        var res = droneService.registerDrone(request);

        Assertions.assertEquals(5, res);
    }

    @Test
    public void registerAlreadyRegisteredDroneTest() {
        var request = getRegisterDroneRequest();

        var domain = Drone.builder()
                .id(5L)
                .model(DroneClass.LIGHTWEIGHT)
                .serialNumber("NCC-74656")
                .weightLimit(400)
                .batteryCapacity(100)
                .state(DroneState.IDLE)
                .build();

        Mockito.when(droneRepository.findBySerialNumber(request.getSerialNumber())).thenReturn(Optional.of(domain));

        Assertions.assertThrows(RegistrationException.class, () -> droneService.registerDrone(request));

        Mockito.verify(droneRepository, never()).save(any());
    }

    private RegisterDroneRequest getRegisterDroneRequest() {
        return RegisterDroneRequest.builder()
                .model(DroneClass.LIGHTWEIGHT)
                .serialNumber("NCC-74656")
                .weightLimit(400)
                .build();
    }

    @Test
    public void loadDroneTestWithNoDrone() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> droneService.load(1L, 1L));
    }

    @Test
    public void loadDroneTestWithNoMedication() {
        var domain = Drone.builder().build();
        Mockito.when(droneRepository.findById(1L)).thenReturn(Optional.of(domain));
        Assertions.assertThrows(ResourceNotFoundException.class, () -> droneService.load(1L, 1L));

        Mockito.verify(droneRepository, never()).save(any());
    }

    @Test
    public void loadDroneTestWhenBatteryIsTooLow() {
        var domain = Drone.builder().batteryCapacity(24).build();
        var medication = Medication.builder().build();
        Mockito.when(droneRepository.findById(1L)).thenReturn(Optional.of(domain));
        Mockito.when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        Assertions.assertThrows(IllegalStateException.class, () -> droneService.load(1L, 1L));

        Mockito.verify(droneRepository, never()).save(any());
    }

    @Test
    public void loadDroneTestOverweight() {
        Drone.DroneBuilder builder = Drone.builder();
        builder.batteryCapacity(100);
        builder.payload(List.of(
                Medication.builder().weight(50).build(),
                Medication.builder().weight(250).build()
        ));
        var domain = builder
                .weightLimit(500)
                .build();
        var medication = Medication.builder().weight(250).build();
        when(droneRepository.findById(1L)).thenReturn(Optional.of(domain));
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        Assertions.assertThrows(IllegalArgumentException.class, () -> droneService.load(1L, 1L));

        Mockito.verify(droneRepository, never()).save(any());
    }

    @Test
    public void loadDroneTestPartiallyLoaded() {
        var drone = Drone.builder()
                .id(1L)
                .serialNumber("NCC-1701-D")
                .payload(new ArrayList<>(List.of(
                        Medication.builder().id(2L).weight(250).build()
                )))
                .weightLimit(512)
                .batteryCapacity(95)
                .state(DroneState.IDLE)
                .build();
        var medication = Medication.builder().id(1L).weight(250).build();

        when(droneRepository.save(argThat(a -> a.getId() == 1L))).thenAnswer( a -> a.getArgument(0));
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));

        var res = droneService.load(1L, 1L);

        verify(droneRepository).save(argThat(a -> a.getPayload().size() == 2 &&
                a.getPayload().get(0).getId() == 2L &&
                a.getPayload().get(1).getId() == 1L));

        Assertions.assertEquals(DroneState.LOADING, res.getState());
        Assertions.assertEquals(1L, res.getId());
        Assertions.assertEquals("NCC-1701-D", res.getSerialNumber());
        Assertions.assertEquals(95, res.getBatteryCapacity());
        Assertions.assertEquals(512, res.getWeightLimit());
    }

    @Test
    public void loadDroneTestFullyLoaded() {
        var drone = Drone.builder()
                .id(1L)
                .serialNumber("NCC-1701-D")
                .payload(new ArrayList<>(List.of(
                    Medication.builder().id(2L).weight(250).build()
                )))
                .weightLimit(500)
                .batteryCapacity(95)
                .state(DroneState.IDLE)
                .build();
        var medication = Medication.builder().id(1L).weight(250).build();

        when(droneRepository.save(argThat(a -> a.getId() == 1L))).thenAnswer( a -> a.getArgument(0));
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));

        var res = droneService.load(1L, 1L);

        verify(droneRepository).save(argThat(a -> a.getPayload().size() == 2 &&
                a.getPayload().get(0).getId() == 2L &&
                a.getPayload().get(1).getId() == 1L));

        Assertions.assertEquals(DroneState.LOADED, res.getState());
        Assertions.assertEquals(1L, res.getId());
        Assertions.assertEquals("NCC-1701-D", res.getSerialNumber());
        Assertions.assertEquals(95, res.getBatteryCapacity());
        Assertions.assertEquals(500, res.getWeightLimit());
    }

    @Test
    public void getDroneBatteryLevelTestNoDroneFound() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> droneService.batteryLevel(1L));
    }

    @Test
    public void getDroneBatteryLevelTest() {
        var drone = Drone.builder().batteryCapacity(66).build();
        Mockito.when(droneRepository.findById(5L)).thenReturn(Optional.of(drone));

        Assertions.assertEquals(66, droneService.batteryLevel(5L));
    }

    @Test
    public void getDronePayloadTestNoDroneFound() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> droneService.getDronePayload(1L));
    }

    @Test
    public void getDronePayloadTestEmptyPayload() {
        var drone = Drone.builder()
                .id(1L)
                .serialNumber("NCC-1701-D")
                .weightLimit(500)
                .batteryCapacity(95)
                .build();

        Mockito.when(droneRepository.findById(38L)).thenReturn(Optional.of(drone));
        var res = droneService.getDronePayload(38L);

        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    public void getDronePayloadTest() {
        var drone = Drone.builder()
                .id(1L)
                .serialNumber("NCC-1701-D")
                .payload(new ArrayList<>(List.of(
                        Medication.builder()
                                .id(2L)
                                .name("RAD-X")
                                .weight(250)
                                .code("666")
                                .image("img/rad-x.jpg")
                                .build()
                )))
                .weightLimit(500)
                .batteryCapacity(95)
                .build();

        Mockito.when(droneRepository.findById(33L)).thenReturn(Optional.of(drone));
        var res = droneService.getDronePayload(33L);

        Assertions.assertEquals(1, res.size());
        Assertions.assertEquals(2L, res.get(0).getId());
        Assertions.assertEquals("RAD-X", res.get(0).getName());
        Assertions.assertEquals(250, res.get(0).getWeight());
        Assertions.assertEquals("666", res.get(0).getCode());
        Assertions.assertEquals("img/rad-x.jpg", res.get(0).getImage());
    }

    @Test
    public void availableDronesTest() {
        var drones = List.of(
                Drone.builder().id(1L).build(),
                Drone.builder().id(2L).build(),
                Drone.builder().id(3L).build()
        );

        Mockito.when(droneRepository.findByStateInAndBatteryCapacityGreaterThanEqual(Set.of(DroneState.IDLE), 25))
                .thenReturn(drones);

        var res = droneService.getAvailableDrones();
        Assertions.assertEquals(3, res.size());
        Assertions.assertEquals(1L, res.get(0).getId());
        Assertions.assertEquals(2L, res.get(1).getId());
        Assertions.assertEquals(3L, res.get(2).getId());
    }
}
