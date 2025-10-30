package com.fitness.AIService.services.IntraService;

import com.fitness.AIService.models.Recommendation;

import java.util.List;

public interface RecommendationService {
    List<Recommendation> getAllRecommendationsForAUser(String userId);

    Recommendation getRecommendationForAActivity(String activityId);
}
