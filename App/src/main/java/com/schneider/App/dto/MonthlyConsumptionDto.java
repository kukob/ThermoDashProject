package com.schneider.App.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyConsumptionDto {

    private String month;
    private String deviceType;
    private double consumption;

}
