package org.example.repository;

import org.example.domain.Drone;
import org.example.domain.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DroneRepository extends CrudRepository<Drone, Long> {

    Optional<Drone> findBySerialNumber(String serialNumber);

    List<Drone> findByStateInAndBatteryCapacityGreaterThanEqual(Set<DroneState> states, Integer batteryCapacity);


    @Modifying
    @Query(nativeQuery = true, value = "insert into DRONE_STATE_HISTORY(ID, DRONE_ID, BATTERY_LEVEL, DATE) "
     + "select (NEXT VALUE FOR DRONES_HISTORY_SEQUENCE), ID, BATTERY_CAPACITY, CURRENT_TIMESTAMP() from DRONES")
    void saveAuditData();
}
