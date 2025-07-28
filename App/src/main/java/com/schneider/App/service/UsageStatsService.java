package com.schneider.App.service;

import com.schneider.App.dto.DeviceConsumptionDto;
import com.schneider.App.dto.MonthlyConsumptionDto;
import com.schneider.App.model.UserEntity;
import com.schneider.App.repository.UsageDatumRepository;
import com.schneider.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.schneider.App.model.UsageDatum;
import com.schneider.App.dto.DailyConsumptionDto;

import java.time.LocalDate;
import java.util.*;
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

   

//
//    public Map<String, Integer> getMonthlyConsumption(String username) {
//        UserEntity user = userRepository.findByUsername(username);
//
//        LocalDate currentMonthStart = LocalDate.now().withDayOfMonth(1);
//        LocalDate nextMonthStart = currentMonthStart.plusMonths(1);
//
//        List<UsageDatum> data = usageDatumRepository.findAllByUserEntity(user);
//
//        int monthTotal = data.stream()
//                .filter(d -> !d.getDate().isBefore(currentMonthStart) && d.getDate().isBefore(nextMonthStart))
//                .mapToInt(d -> d.getEnergyConsumedKwh() != null ? d.getEnergyConsumedKwh() : 0)
//                .sum();
//
//        return Map.of("monthly", monthTotal);
//    }


//    public List<MonthlyConsumptionDto> getMonthlyConsumption(String username) {
//        UserEntity user = userRepository.findByUsername(username);
//        List<UsageDatum> data = usageDatumRepository.findAllByUserEntity(user);
//
//        String currentMonth = LocalDate.now().withDayOfMonth(1).toString().substring(0, 7);
//
//        LocalDate currentMonthStart = LocalDate.now().withDayOfMonth(1);
//        LocalDate nextMonthStart = currentMonthStart.plusMonths(1);
//
//        List<UsageDatum> filtered = data.stream()
//                .filter(d -> !d.getDate().isBefore(currentMonthStart) && d.getDate().isBefore(nextMonthStart))
//                .collect(Collectors.toList());
//
//        Map<String, Integer> grouped = new HashMap<>();
//
//        for (UsageDatum d : filtered) {
//            String deviceType = d.getDeviceType().getName().toLowerCase();
//            int kwh = d.getEnergyConsumedKwh() != null ? d.getEnergyConsumedKwh() : 0;
//
//            grouped.merge(deviceType, kwh, Integer::sum);
//        }
//
//        List<MonthlyConsumptionDto> result = new ArrayList<>();
//        for (Map.Entry<String, Integer> entry : grouped.entrySet()) {
//            result.add(new MonthlyConsumptionDto(currentMonth, entry.getKey(), entry.getValue()));
//        }
//
//        return result;
//    }


//    public List<MonthlyConsumptionDto> getMonthlyConsumption(String username) {
//        UserEntity user = userRepository.findByUsername(username);
//        List<UsageDatum> data = usageDatumRepository.findAllByUserEntity(user);
//
//        Map<String, Map<String, Integer>> grouped = new TreeMap<>();
//
//        for (UsageDatum d : data) {
//            String month = d.getDate().withDayOfMonth(1).toString().substring(0, 7);
//            String deviceType = d.getDeviceType().getName().toLowerCase();
//            int kwh = d.getEnergyConsumedKwh() != null ? d.getEnergyConsumedKwh() : 0;
//
//            grouped
//                    .computeIfAbsent(month, m -> new HashMap<>())
//                    .merge(deviceType, kwh, Integer::sum);
//        }
//
//        List<MonthlyConsumptionDto> result = new ArrayList<>();
//        for (Map.Entry<String, Map<String, Integer>> monthEntry : grouped.entrySet()) {
//            String month = monthEntry.getKey();
//            for (Map.Entry<String, Integer> deviceEntry : monthEntry.getValue().entrySet()) {
//                result.add(new MonthlyConsumptionDto(month, deviceEntry.getKey(), deviceEntry.getValue()));
//            }
//        }
//
//        return result;
//    }



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
