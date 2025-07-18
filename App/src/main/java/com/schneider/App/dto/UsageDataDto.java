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
public class UsageDataDto {

    private Integer userId;
    private Integer deviceTypeId;
    private LocalDate date;
    private Integer usageHours;
    private String note;

}
