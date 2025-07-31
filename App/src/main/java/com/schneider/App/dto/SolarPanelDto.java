package com.schneider.App.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolarPanelDto{

    private Integer userId;
    private double panelPowerKw;
    private LocalDate installationDate;

}
