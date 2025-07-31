package com.schneider.App.controller;

import com.schneider.App.dto.DailtyProductionDto;
import com.schneider.App.model.SolarPanel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.schneider.App.service.SolarPanelService;
import com.schneider.App.dto.SolarPanelDto;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/solar")
public class SolarPanelController {

    private final SolarPanelService solarPanelService;

    @PostMapping
    public ResponseEntity<SolarPanel> createSolarPanel(@RequestBody SolarPanelDto dto, Principal principal) {
        SolarPanel savedPanel = solarPanelService.savePanelForUser(dto, principal);
        return ResponseEntity.ok(savedPanel);
    }

    /// ////////////////////////////////////
    @PostMapping("/data")
    public ResponseEntity<String> addDailySolarDataManually() {
        solarPanelService.addDailySolarData();
        return ResponseEntity.ok("Dnevni solarni podaci su dodati");
    }

    @GetMapping("/daily")
    public List<DailtyProductionDto> getDailyProduction(Principal principal) {
        String username = principal.getName();
        return solarPanelService.getDailyProduction(username);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Integer>> getSolarProductionSummary(Principal principal) {
        String username = principal.getName();
        Map<String, Integer> summaryStats = solarPanelService.getSolarProductionSummaryStats(username);
        return ResponseEntity.ok(summaryStats);
    }

//    @PostMapping
//    public ResponseEntity<SolarPanel> createSolarPanel(@RequestBody SolarPanel panel, Principal principal) {
//        SolarPanel savedPanel = solarPanelService.savePanelForUser(panel, principal);
//        return ResponseEntity.ok(savedPanel);
//    }

}
