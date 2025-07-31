package com.schneider.App.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schneider.App.dto.HourlyRecommendationDto;
import com.schneider.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.schneider.App.model.UserEntity;

import java.security.Principal;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public String getRecommendation(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Korisnik ne postoji"));

        String city = user.getCity();
        String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        double temp = response.getBody()
                .get("list").get(0)
                .get("main").get("temp").asDouble();

        return generateMessage(temp);
    }

    private String generateMessage(double temp) {
        if (temp >= 26) {
            return "Ukljucite klimu.";
        } else if (temp <= 18) {
            return "Ukljucite grejanje.";
        } else {
            return "Ugasite klimu/grejanje";
        }
    }

public List<HourlyRecommendationDto> getHourlyRecommendations(Principal principal) {
    UserEntity user = userRepository.findByUsername(principal.getName());
    System.out.println("Principal name: " + principal.getName());

    String city = user.getCity();
    if (city == null || city.isBlank()) {
        throw new RuntimeException("Korisnik nema podesen grad.");
    }

    String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";
    JsonNode response = restTemplate.getForObject(url, JsonNode.class);
    JsonNode list = response.get("list");

    List<HourlyRecommendationDto> result = new ArrayList<>();
    for (JsonNode item : list) {
        String time = item.get("dt_txt").asText();
        double temp = item.get("main").get("temp").asDouble();
        String date = time.substring(0, 10);
        String startTime = time.substring(11, 16);
        String endTime = addHoursToTime(startTime, 3);
        String recommendation;

        if (temp >= 26) {
            recommendation = "Ukljuci klimu";
        } else if (temp <= 18) {
            recommendation = "Ukljuci grejanje";
        } else {
            recommendation = "Nije potrebna potrosnja";
        }

        result.add(new HourlyRecommendationDto(date, startTime + " – " + endTime, recommendation));
    }

    return result;
}

    private String addHoursToTime(String time, int hoursToAdd) {
        LocalTime t = LocalTime.parse(time);
        return t.plusHours(hoursToAdd).toString();
    }





}
















