package com.schneider.App.model;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "solar_data")
@Getter
@Setter
public class SolarData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solar_panel_id", nullable = false)
    private SolarPanel solarPanel;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "energy_produced_kwh")
    private Double energyProducedKwh;


}
