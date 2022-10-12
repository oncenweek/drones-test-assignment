package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.time.Instant;

import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "DRONE_STATE_HISTORY")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneStateHistory {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = SEQUENCE, generator = "DRONES_HISTORY_SEQUENCE")
    @SequenceGenerator(name = "DRONES_HISTORY_SEQUENCE", sequenceName = "DRONES_HISTORY_SEQUENCE", allocationSize = 1)
    private Long id;

    @Column(name = "DRONE_ID")
    private Long droneId;

    @Column(name = "BATTERY_LEVEL")
    private Integer batteryLevel;

    @Column(name = "DATE")
    private Instant date;
}
