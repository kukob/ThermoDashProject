package com.schneider.App.repository;

import com.schneider.App.model.SolarData;
import com.schneider.App.model.SolarPanel;
import com.schneider.App.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SolarDataRepository extends JpaRepository<SolarData, Integer> {

    boolean existsBySolarPanelAndDate(SolarPanel solarPanel, LocalDate date);

    List<SolarData> findAllBySolarPanelUserEntity(UserEntity user);
    List<SolarData> findAllBySolarPanel_UserEntity(UserEntity user);

}