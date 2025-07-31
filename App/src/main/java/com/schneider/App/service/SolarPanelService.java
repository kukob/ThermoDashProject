package com.schneider.App.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schneider.App.dto.DailtyProductionDto;
import com.schneider.App.dto.SolarPanelDto;
import com.schneider.App.model.SolarData;
import com.schneider.App.model.SolarPanel;
import com.schneider.App.model.UserEntity;
import com.schneider.App.repository.SolarDataRepository;
import com.schneider.App.repository.SolarPanelRepository;
import com.schneider.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolarPanelService {

    private final SolarPanelRepository solarPanelRepository;
    private final SolarDataRepository solarDataRepository;
    private final UserRepository userRepository;


    public SolarPanel savePanelForUser(SolarPanelDto dto, Principal principal) {
        UserEntity user = userRepository.findByUsername(principal.getName());

        SolarPanel panel = new SolarPanel();
        panel.setUserEntity(user);
        panel.setPanelPowerKw(dto.getPanelPowerKw());
        panel.setInstallationDate(dto.getInstallationDate());

        return solarPanelRepository.save(panel);
    }

    //////////////////////////////////////
    @Transactional
    //@Scheduled(cron = "0 0 0 * * ?", zone = "Europe/Belgrade")
    @Scheduled(cron = "0 33 19 * * ?", zone = "Europe/Belgrade")
    public void addDailySolarData() {
        LocalDate today = LocalDate.now();

        List<SolarPanel> panels = solarPanelRepository.findAll();

        for (SolarPanel panel : panels) {
            boolean exists = solarDataRepository.existsBySolarPanelAndDate(panel, today);

            if (!exists) {
                SolarData newData = new SolarData();
                newData.setSolarPanel(panel);
                newData.setDate(today);
                Double energy = panel.getPanelPowerKw() * 4;
                newData.setEnergyProducedKwh(energy);

                solarDataRepository.save(newData);
            }
        }
    }

    public List<DailtyProductionDto> getDailyProduction(String username) {
        UserEntity user = userRepository.findByUsername(username);

        List<SolarData> data = solarDataRepository.findAllBySolarPanel_UserEntity(user);

        Map<LocalDate, Double> grouped = new TreeMap<>();
        for (SolarData d : data) {
            double kwh = d.getEnergyProducedKwh() != null ? d.getEnergyProducedKwh() : 0.0;
            grouped.put(d.getDate(), grouped.getOrDefault(d.getDate(), 0.0) + kwh);
        }

        return grouped.entrySet().stream()
                .map(entry -> new DailtyProductionDto(entry.getKey().toString(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getSolarProductionSummaryStats(String username) {
        UserEntity user = userRepository.findByUsername(username);

        List<SolarData> data = solarDataRepository.findAllBySolarPanel_UserEntity(user);

        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        LocalDate monthAgo = today.minusMonths(1);
        LocalDate yearAgo = today.minusYears(1);

        int weekTotal = (int) data.stream()
                .filter(d -> !d.getDate().isBefore(weekAgo))
                .mapToDouble(SolarData::getEnergyProducedKwh)
                .sum();

        int monthTotal = (int) data.stream()
                .filter(d -> !d.getDate().isBefore(monthAgo))
                .mapToDouble(SolarData::getEnergyProducedKwh)
                .sum();

        int yearTotal = (int) data.stream()
                .filter(d -> !d.getDate().isBefore(yearAgo))
                .mapToDouble(SolarData::getEnergyProducedKwh)
                .sum();

        return Map.of(
                "weekly", weekTotal,
                "monthly", monthTotal,
                "yearly", yearTotal
        );
    }

//    @Value("${weather.api.key}")
//    private String apiKey;
//
//    @Value("${weather.api.url}")
//    private String apiUrl;
//
//    private final RestTemplate restTemplate = new RestTemplate();

//    public double getSunHours(double lat, double lon) {
//        String url = String.format(
//                "%s?lat=%f&lon=%f&exclude=minutely,hourly,alerts&appid=%s",
//                apiUrl, lat, lon, apiKey
//        );
//
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode root = mapper.readTree(response.getBody());
//
//                long sunrise = root.at("/daily/0/sunrise").asLong();
//                long sunset = root.at("/daily/0/sunset").asLong();
//
//                return (sunset - sunrise) / 3600.0;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return 4.0;
//    }


//    public List<DailtyProductionDto> getDailyProduction(String username) {
//        UserEntity user = userRepository.findByUsername(username);
//
//        List<SolarData> data = solarDataRepository.findAllBySolarPanelUserEntity(user);
//
//        Map<LocalDate, Double> grouped = new TreeMap<>();
//        for (SolarData d : data) {
//            double kwh = d.getEnergyProducedKwh() != null ? d.getEnergyProducedKwh() : 0.0;
//            grouped.put(d.getDate(), grouped.getOrDefault(d.getDate(), 0.0) + kwh);
//        }
//
//        return grouped.entrySet().stream()
//                .map(entry -> new DailtyProductionDto(entry.getKey().toString(), entry.getValue()))
//                .collect(Collectors.toList());
//    }


//    public SolarPanel savePanelForUser(SolarPanel panel, Principal principal) {
//        String username = principal.getName();
//        UserEntity user = userRepository.findByUsername(username);
//        panel.setUserEntity(user);
//        return solarPanelRepository.save(panel);
//    }


}
