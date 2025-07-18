package com.schneider.App.service;

import com.schneider.App.dto.UsageDataDto;
import com.schneider.App.model.DeviceType;
import com.schneider.App.model.UsageDatum;
import com.schneider.App.model.UserEntity;
import com.schneider.App.repository.DeviceTypeRepository;
import com.schneider.App.repository.UsageDatumRepository;
import com.schneider.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.schneider.App.dto.DeviceConsumptionDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsageDataService {

    private final UsageDatumRepository usageDataRepository;
    private final UserRepository userRepository;
    private final DeviceTypeRepository deviceTypeRepository;

    public UsageDatum saveUsageData(UsageDataDto request, String username) {
        UserEntity userEntity = userRepository.findByUsername(username);

        DeviceType device = deviceTypeRepository.findById(request.getDeviceTypeId())
                .orElseThrow(() -> new RuntimeException("Uredjaj ne postoji"));

        UsageDatum data = new UsageDatum();
        data.setUserEntity(userEntity);
        data.setDeviceType(device);
        data.setDate(request.getDate());
        data.setUsageHours(request.getUsageHours());
        data.setNote(request.getNote());

        int potrosnja = device.getConsumptionPerHour() * request.getUsageHours();
        data.setEnergyConsumedKwh(potrosnja);

        return usageDataRepository.save(data);
    }

    public List<DeviceConsumptionDto> getDeviceConsumptionForUser(Integer userId) {
        return usageDataRepository.findTotalConsumptionPerDeviceTypeByUserId(userId);
    }

}
