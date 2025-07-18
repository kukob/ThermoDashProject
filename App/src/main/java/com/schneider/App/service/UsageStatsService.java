package com.schneider.App.service;

import com.schneider.App.model.UserEntity;
import com.schneider.App.repository.UsageDatumRepository;
import com.schneider.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.schneider.App.model.UsageDatum;
import com.schneider.App.dto.DailyConsumptionDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Service
@RequiredArgsConstructor
public class UsageStatsService {

    private final UsageDatumRepository usageDatumRepository;
    private final UserRepository userRepository;

    public Map<String, Integer> getSummaryStats(String username) {
        UserEntity user = userRepository.findByUsername(username);

        List<UsageDatum> data = usageDatumRepository.findAllByUserEntity(user);

        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        LocalDate monthAgo = today.minusMonths(1);
        LocalDate yearAgo = today.minusYears(1);

        int weekTotal = data.stream()
                .filter(d -> !d.getDate().isBefore(weekAgo))
                .mapToInt(UsageDatum::getEnergyConsumedKwh)
                .sum();

        int monthTotal = data.stream()
                .filter(d -> !d.getDate().isBefore(monthAgo))
                .mapToInt(UsageDatum::getEnergyConsumedKwh)
                .sum();

        int yearTotal = data.stream()
                .filter(d -> !d.getDate().isBefore(yearAgo))
                .mapToInt(UsageDatum::getEnergyConsumedKwh)
                .sum();

        return Map.of(
                "weekly", weekTotal,
                "monthly", monthTotal,
                "yearly", yearTotal
        );
    }

    public List<DailyConsumptionDto> getDailyConsumption(String username) {
        UserEntity user = userRepository.findByUsername(username);
        List<UsageDatum> data = usageDatumRepository.findAllByUserEntity(user);

        Map<LocalDate, Integer> grouped = new TreeMap<>();
        for (UsageDatum d : data) {
            int kwh = d.getEnergyConsumedKwh() != null ? d.getEnergyConsumedKwh() : 0;
            grouped.put(d.getDate(), grouped.getOrDefault(d.getDate(), 0) + kwh);
        }

        return grouped.entrySet().stream()
                .map(entry -> new DailyConsumptionDto(entry.getKey().toString(), entry.getValue()))
                .collect(Collectors.toList());
    }





//    public List<Map<String, Object>> getDailyDeviceStats(String username) {
//        UserEntity user = userRepository.findByUsername(username);
//        List<UsageDatum> data = usageDatumRepository.findAllByUserEntity(user);
//
//        return data.stream()
//                .collect(groupingBy(
//                        d -> Map.of(
//                                "date", d.getDate().toString(),
//                                "device", d.getDeviceType().getName()
//                        ),
//                        summingInt(UsageDatum::getEnergyConsumedKwh)
//                ))
//                .entrySet().stream()
//                .map(entry -> {
//                    Map<String, Object> result = new HashMap<>(entry.getKey());
//                    result.put("consumption", entry.getValue());
//                    return result;
//                })
//                .toList();
//    }

}
