package org.example.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "DRONES")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Drone {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = SEQUENCE, generator = "DRONES_SEQUENCE")
    @SequenceGenerator(name = "DRONES_SEQUENCE", sequenceName = "DRONES_SEQUENCE", allocationSize = 1)
    private Long id;

    @Column(name = "SERIAL_NUMBER", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "MODEL", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DroneClass model;

    @Column(name = "WEIGHT_LIMIT", nullable = false)
    private Integer weightLimit;

    @Column(name = "BATTERY_CAPACITY", nullable = false)
    private Integer batteryCapacity;

    @Column(name = "STATE", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DroneState state;

    @Builder.Default
    @OneToMany(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "DRONE_MEDICATIONS",
            joinColumns = @JoinColumn(name = "DRONE_ID"),
            inverseJoinColumns = @JoinColumn(name = "MEDICATION_ID"))
    private List<Medication> payload = new ArrayList<>();
}