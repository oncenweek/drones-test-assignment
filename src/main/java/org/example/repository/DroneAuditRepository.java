package org.example.repository;

import org.example.domain.DroneStateHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneAuditRepository extends JpaRepository<DroneStateHistory, Long> {

    Page<DroneStateHistory> findByDroneId(Long droneId, Pageable pageable);
}
