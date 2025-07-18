package com.schneider.App.controller;

import com.schneider.App.dto.UsageDataDto;
import com.schneider.App.model.UsageDatum;
import com.schneider.App.model.UserEntity;
import com.schneider.App.repository.UserRepository;
import com.schneider.App.service.UsageDataService;
import com.schneider.App.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.schneider.App.dto.DeviceConsumptionDto;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class UsageDataController {

    private final UsageDataService usageDataService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> addUsage(@RequestBody UsageDataDto request, Principal principal) {
        try {
            UsageDatum saved = usageDataService.saveUsageData(request, principal.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/consumption-by-device")
    public ResponseEntity<?> getDeviceConsumption(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Korisnik nije pronadjen");
        }

        List<DeviceConsumptionDto> result = usageDataService.getDeviceConsumptionForUser(user.getId());
        return ResponseEntity.ok(result);
    }


}
