package com.fitness.AIService.service;

import com.fitness.AIService.models.Recommendation;

import java.util.List;

public interface RecommendationService {
    List<Recommendation> getAllRecommendationsForAUser(String userId);

    Recommendation getRecommendationForAActivity(String activityId);
}
