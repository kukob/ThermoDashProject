package com.schneider.App.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.schneider.App.model.DeviceType;
import com.schneider.App.repository.DeviceTypeRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceTypeService {

    private final DeviceTypeRepository deviceTypeRepository;

    public List<DeviceType> getAllDeviceTypes() {
        return deviceTypeRepository.findAll();
    }
}
