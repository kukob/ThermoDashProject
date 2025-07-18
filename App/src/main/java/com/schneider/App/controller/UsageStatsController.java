package com.schneider.App.controller;


import com.schneider.App.dto.DeviceConsumptionDto;
import com.schneider.App.model.UserEntity;
import com.schneider.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.schneider.App.service.UsageStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import com.schneider.App.dto.DailyConsumptionDto;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage/stats")
public class UsageStatsController {

    private final UsageStatsService usageStatsService;
    private final UserRepository userRepository;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Integer>> getSummary(Principal principal) {
        return ResponseEntity.ok(usageStatsService.getSummaryStats(principal.getName()));
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyConsumptionDto>> getDailyStats(Principal principal) {
        return ResponseEntity.ok(usageStatsService.getDailyConsumption(principal.getName()));
    }


//    @GetMapping("/daily-device")
//    public ResponseEntity<List<Map<String, Object>>> getDailyDeviceStats(Principal principal) {
//        return ResponseEntity.ok(usageStatsService.getDailyDeviceStats(principal.getName()));
//    }


}

