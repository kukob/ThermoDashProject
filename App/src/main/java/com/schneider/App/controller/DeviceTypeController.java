package com.schneider.App.controller;


import com.schneider.App.model.DeviceType;
import com.schneider.App.repository.DeviceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
public class DeviceTypeController {

    private final DeviceTypeRepository deviceTypeRepository;

    @GetMapping
    public ResponseEntity<List<DeviceType>> getAllDeviceTypes() {
        return ResponseEntity.ok(deviceTypeRepository.findAll());
    }
}
