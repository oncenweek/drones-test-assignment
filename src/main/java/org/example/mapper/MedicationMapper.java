package org.example.mapper;

import org.example.domain.Medication;
import org.example.dto.MedicationDto;
import org.mapstruct.Mapper;

@Mapper
public interface MedicationMapper {

    MedicationDto map (Medication domain);
}
