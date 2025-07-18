package com.schneider.App.repository;

import com.schneider.App.dto.DeviceConsumptionDto;
import com.schneider.App.model.UsageDatum;
import com.schneider.App.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsageDatumRepository extends JpaRepository<UsageDatum, Integer> {

    List<UsageDatum> findAllByUserEntity(UserEntity user);

    @Query("SELECT new com.schneider.App.dto.DeviceConsumptionDto(d.name, SUM(u.energyConsumedKwh)) " +
            "FROM UsageDatum u JOIN u.deviceType d " +
            "WHERE u.userEntity.id = :userId " +
            "GROUP BY d.name")
    List<DeviceConsumptionDto> findTotalConsumptionPerDeviceTypeByUserId(Integer userId);


}