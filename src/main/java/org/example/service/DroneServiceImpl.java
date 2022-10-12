package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.Drone;
import org.example.domain.DroneState;
import org.example.domain.DroneStateHistory;
import org.example.domain.Medication;
import org.example.dto.DroneDto;
import org.example.dto.MedicationDto;
import org.example.dto.RegisterDroneRequest;
import org.example.exceptions.RegistrationException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.DroneMapper;
import org.example.mapper.MedicationMapper;
import org.example.repository.DroneAuditRepository;
import org.example.repository.DroneRepository;
import org.example.repository.MedicationRepository;
import org.example.service.api.DroneService;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Validated
@AllArgsConstructor
public class DroneServiceImpl implements DroneService {

    private static final int MIN_BATTERY_CAPACITY = 25;
    private final DroneRepository repository;

    private final MedicationRepository medicationRepository;

    private final DroneAuditRepository droneAuditRepository;

    private final DroneMapper mapper = Mappers.getMapper(DroneMapper.class);

    private final MedicationMapper medicationMapper = Mappers.getMapper(MedicationMapper.class);

    @Override
    @Transactional(readOnly = true)
    public Integer batteryLevel(@NotNull Long droneId) {
        return repository.findById(droneId)
                .map(Drone::getBatteryCapacity)
                .orElseThrow(() -> new ResourceNotFoundException("DRONE", droneId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicationDto> getDronePayload(@NotNull Long droneId) {
        return repository.findById(droneId)
                .map(Drone::getPayload)
                .map(m -> m.stream().map(medicationMapper::map).collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException("DRONE", droneId));
    }

    @Override
    @Transactional
    public Long registerDrone(@NotNull @Valid RegisterDroneRequest request) {
        repository.findBySerialNumber(request.getSerialNumber())
                .ifPresent((a) -> {
                    throw new RegistrationException("Drone with serial " + request.getSerialNumber() + " already registered");
                });
        var drone = mapper.map(request);

        drone.setState(DroneState.IDLE);
        drone.setBatteryCapacity(100);

        return repository.save(drone).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DroneDto> getAvailableDrones() {
        return repository.findByStateInAndBatteryCapacityGreaterThanEqual(Set.of(DroneState.IDLE), MIN_BATTERY_CAPACITY)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DroneDto load(@NotNull Long droneId, @NotNull Long medicationId) {
        final var drone  = repository.findById(droneId).orElseThrow(() -> new ResourceNotFoundException("DRONE", droneId));
        final var medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new ResourceNotFoundException("MEDICATION", droneId));

        if(drone.getBatteryCapacity() < 25) {
            throw new IllegalStateException("DRONE battery level is too low");
        }

        final var loadedWeight = drone.getPayload().stream().map(Medication::getWeight).mapToInt(Integer::intValue).sum();
        if(loadedWeight + medication.getWeight() > drone.getWeightLimit()) {
            throw new IllegalArgumentException("Too big weight for drone");
        }

        drone.getPayload().add(medication);
        drone.setState(DroneState.LOADING);
        if(loadedWeight + medication.getWeight() == drone.getWeightLimit()) {
            drone.setState(DroneState.LOADED);
        }

        return mapper.map(repository.save(drone));
    }

    @Override
    public List<MedicationDto> getAllMedications() {
        return StreamSupport.stream(medicationRepository.findAll().spliterator(), false)
                .map(medicationMapper::map)
                .collect(Collectors.toList());
    }

    public Page<DroneStateHistory> getAuditData(Long droneId, Integer maxValues) {
        PageRequest page = PageRequest.of(0, maxValues, Sort.by(Sort.Direction.DESC, "date"));
        return droneAuditRepository.findByDroneId(droneId, page);
    }
}
