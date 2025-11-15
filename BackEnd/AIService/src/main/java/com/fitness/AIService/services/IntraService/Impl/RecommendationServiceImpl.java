package com.fitness.AIService.services.IntraService.Impl;

import com.fitness.AIService.models.Recommendation;
import com.fitness.AIService.repository.RecommendationsRepository;
import com.fitness.AIService.services.InterService.ActivityValidationService;
import com.fitness.AIService.services.InterService.UserValidationService;
import com.fitness.AIService.services.IntraService.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final RecommendationsRepository recommendationsRepository;

    private final ActivityValidationService activityValidationService;

    private final UserValidationService userValidationService;

    @Override
    public List<Recommendation> getAllRecommendationsForAUser(String userId) {

        boolean isValidUserId = userValidationService.validateUserByUserId(userId);

        boolean isValidKeyCloakId = userValidationService.validateUserByKeyCloakId(userId);

        if (!isValidUserId) {
            throw new RuntimeException("Invalid user. No Such UserId exists !! " + userId);
        }

        List<Recommendation> recommendations = recommendationsRepository.findByUserId(userId);

        if (recommendations.isEmpty()) {
            log.info("No recommendations found for this User !");
        }

        return recommendations;
    }

    @Override
    public Recommendation getRecommendationForAActivity(String activityId) {

        boolean isValidActivityId = activityValidationService.validateActivityId(activityId);

        if (!isValidActivityId) {
            throw new RuntimeException("Invalid activity. No Such activityId exists !! " + activityId);
        }
        return recommendationsRepository
                .findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("No Recommendation Not Found for this Activity !"));
    }
}
