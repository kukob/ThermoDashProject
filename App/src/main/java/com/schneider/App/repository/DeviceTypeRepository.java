package com.schneider.App.repository;

import com.schneider.App.model.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTypeRepository extends JpaRepository<DeviceType, Integer> {
}