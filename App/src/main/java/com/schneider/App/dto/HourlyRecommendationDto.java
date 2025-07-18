package com.schneider.App.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRecommendationDto {

    private String timeRange;
    private String recommendation;
}
