package org.example.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.repository.DroneRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DroneStateLogJob {

    private final DroneRepository repository;

    @Scheduled(fixedRateString = "${job.audit.periodMs}")
    @Transactional
    public void saveDroneStatesToAuditTable() {
        log.info("saving audit date for drones");
        repository.saveAuditData();
    }
}
