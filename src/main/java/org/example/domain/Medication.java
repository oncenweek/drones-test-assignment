package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "MEDICATIONS")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Medication {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = SEQUENCE, generator = "MEDICATIONS_SEQUENCE")
    @SequenceGenerator(name = "MEDICATIONS_SEQUENCE", sequenceName = "MEDICATIONS_SEQUENCE", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "WEIGHT", nullable = false)
    private Integer weight;

    @Column(name = "CODE", nullable = false)
    private String code;

    //assume that image field is a link, image data must be contained in some kind of file storage or as static image on web server
    @Column(name = "IMAGE", nullable = false)
    private String image;
}