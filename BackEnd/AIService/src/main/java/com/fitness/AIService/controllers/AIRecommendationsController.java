package com.fitness.AIService.controllers;


import com.fitness.AIService.models.Recommendation;
import com.fitness.AIService.services.IntraService.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class AIRecommendationsController {

    private final RecommendationService recommendationService;

    @GetMapping("/getAllRecommendations/{userId}")
    public ResponseEntity<List<Recommendation>> getAllRecommendationsForAUser(@PathVariable String userId) {

        List<Recommendation> recommendationsList=recommendationService.getAllRecommendationsForAUser(userId);
        return new ResponseEntity<>(recommendationsList, HttpStatus.OK);
    }

    @GetMapping("/getRecommendation/{activityId}")
    public ResponseEntity<Recommendation> getRecommendationForAActivity(@PathVariable String activityId) {

        Recommendation recommendation=recommendationService.getRecommendationForAActivity(activityId);
        return new ResponseEntity<>(recommendation, HttpStatus.OK);
    }
}
