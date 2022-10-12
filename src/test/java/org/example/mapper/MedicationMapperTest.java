package org.example.mapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.example.domain.Medication;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class MedicationMapperTest {

    private final MedicationMapper mapper = Mappers.getMapper(MedicationMapper.class);

    @Test
    public void mapToDtoTest() {
        var med = Medication.builder()
                .id(RandomUtils.nextLong())
                .image(RandomStringUtils.randomAlphanumeric(16, 128))
                .code(RandomStringUtils.randomAlphanumeric(4, 16).toUpperCase())
                .weight(RandomUtils.nextInt(100, 500))
                .build();

        var res = mapper.map(med);

        assertEquals(med.getId(), res.getId());
        assertEquals(med.getWeight(), res.getWeight());
        assertEquals(med.getCode(), res.getCode());
        assertEquals(med.getName(), res.getName());
        assertEquals(med.getImage(), res.getImage());
    }
}
