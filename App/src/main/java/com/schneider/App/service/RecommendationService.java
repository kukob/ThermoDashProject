package com.schneider.App.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.schneider.App.dto.HourlyRecommendationDto;
import com.schneider.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.schneider.App.model.UserEntity;
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
            return "Preporuka: Ukljucite klimu.";
        } else if (temp <= 18) {
            return "Preporuka: Ukljucite grejanje.";
        } else {
            return "Preporuka: Nema potrebe za ukljucivanjem klime/grejanja.";
        }
    }

//    public List<HourlyRecommendationDto> getHourlyRecommendations(String city) {
//        List<HourlyRecommendationDto> result = new ArrayList<>();
//
////        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city +
////                "&appid=" + apiKey +
////                "&units=metric";
//
//        JsonNode response = restTemplate.getForObject(apiUrl, JsonNode.class);
//        JsonNode list = response.get("list");
//
//        for (JsonNode item : list) {
//            String time = item.get("dt_txt").asText();
//            double temp = item.get("main").get("temp").asDouble();
//
//            String timeRange = time.substring(11, 16) + " – " + addHoursToTime(time.substring(11, 16), 3);
//            String recommendation;
//
//            if (temp > 26) {
//                recommendation = "Uključi klimu ❄️";
//            } else if (temp < 18) {
//                recommendation = "Uključi grejanje 🔥";
//            } else {
//                recommendation = "Nije potrebna potrošnja 🌤️";
//            }
//
//            result.add(new HourlyRecommendationDto(timeRange, recommendation));
//        }
//
//        return result;
//    }
//
//    private String addHoursToTime(String time, int hoursToAdd) {
//        LocalTime t = LocalTime.parse(time);
//        return t.plusHours(hoursToAdd).toString();
//    }
}
