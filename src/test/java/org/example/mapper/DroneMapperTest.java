package org.example.mapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.example.domain.Drone;
import org.example.domain.DroneClass;
import org.example.domain.DroneState;
import org.example.dto.RegisterDroneRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;

public class DroneMapperTest {

    private DroneMapper mapper = Mappers.getMapper(DroneMapper.class);

    @Test
    public void mapRequestTest() {
        var request = RegisterDroneRequest.builder()
                .model(DroneClass.values()[RandomUtils.nextInt(0, DroneClass.values().length)])
                .weightLimit(RandomUtils.nextInt())
                .serialNumber(RandomStringUtils.randomAlphanumeric(6, 36))
                .build();

        var res = mapper.map(request);

        Assertions.assertEquals(request.getSerialNumber(), res.getSerialNumber());
        Assertions.assertEquals(request.getWeightLimit(), res.getWeightLimit());
        Assertions.assertEquals(request.getModel(), res.getModel());
    }

    @Test
    public void mapToDtoTest() {
        var drone = Drone.builder()
                .id(RandomUtils.nextLong())
                .batteryCapacity(RandomUtils.nextInt(1, 101))
                .state(DroneState.values()[RandomUtils.nextInt(0, DroneState.values().length)])
                .model(DroneClass.values()[RandomUtils.nextInt(0, DroneClass.values().length)])
                .weightLimit(RandomUtils.nextInt())
                .serialNumber(RandomStringUtils.randomAlphanumeric(6, 36))
                .build();

        var res = mapper.map(drone);

        Assertions.assertEquals(drone.getId(), res.getId());
        Assertions.assertEquals(drone.getState(), res.getState());
        Assertions.assertEquals(drone.getBatteryCapacity(), res.getBatteryCapacity());
        Assertions.assertEquals(drone.getSerialNumber(), res.getSerialNumber());
        Assertions.assertEquals(drone.getWeightLimit(), res.getWeightLimit());
        Assertions.assertEquals(drone.getModel(), res.getModel());
    }
}
