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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

//    @GetMapping
//    public ResponseEntity<String> getRecommendation(@RequestParam Integer userId) {
//        String message = recommendationService.getRecommendation(userId);
//        return ResponseEntity.ok(message);
//    }

    @GetMapping
    public ResponseEntity<String> getRecommendation(@AuthenticationPrincipal UserDetails user) {
        UserEntity entity = userRepository.findByUsername(user.getUsername());

        String message = recommendationService.getRecommendation(entity.getId());
        return ResponseEntity.ok(message);
    }

//    @GetMapping("/hourly")
//    public ResponseEntity<?> getHourlyRecommendations() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        System.out.println("Prijavljen korisnik (username): " + username);
//
//        UserEntity user = userRepository.findByUsername(username);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Korisnik nije pronađen");
//        }
//
//        if (user.getCity() == null) {
//            return ResponseEntity.badRequest().body("Korisnik nema podešen grad");
//        }
//
//        List<HourlyRecommendationDto> list = recommendationService.getHourlyRecommendations(user.getCity());
//        return ResponseEntity.ok(list);
//    }

}
