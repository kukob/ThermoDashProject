package com.schneider.App.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solar_panels")
@Getter
@Setter
public class SolarPanel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Column(name = "panel_power_kw", nullable = false)
    private Double panelPowerKw;

    @Column(name = "installation_date")
    private LocalDate installationDate;

    @OneToMany(mappedBy = "solarPanel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolarData> solarDataList = new ArrayList<>();
}
