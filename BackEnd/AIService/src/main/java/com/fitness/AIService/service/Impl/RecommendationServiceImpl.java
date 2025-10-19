package com.fitness.AIService.service.Impl;

import com.fitness.AIService.models.Recommendation;
import com.fitness.AIService.repository.RecommendationsRepository;
import com.fitness.AIService.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationsRepository recommendationsRepository;

    @Override
    public List<Recommendation> getAllRecommendationsForAUser(String userId) {

        return recommendationsRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Recommendations not found for this User !"));
    }

    @Override
    public Recommendation getRecommendationForAActivity(String activityId) {
        return recommendationsRepository
                .findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("No Recommendation Not Found for this Activity !"));
    }
}
