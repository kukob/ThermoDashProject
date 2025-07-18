package com.schneider.App.controller;


import com.schneider.App.dto.HourlyRecommendationDto;
import com.schneider.App.model.UserEntity;
import com.schneider.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.schneider.App.service.RecommendationService;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<String> getRecommendation(@AuthenticationPrincipal UserDetails user) {
        UserEntity entity = userRepository.findByUsername(user.getUsername());

        String message = recommendationService.getRecommendation(entity.getId());
        return ResponseEntity.ok(message);
    }

    @GetMapping("/hourly")
    public ResponseEntity<?> getHourlyRecommendations(Principal principal) {
        try {
            List<HourlyRecommendationDto> result = recommendationService.getHourlyRecommendations(principal);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
