package com.fitness.AIService.services.Kafka.Impl;

import com.fitness.AIService.models.Activity;
import com.fitness.AIService.models.Recommendation;
import com.fitness.AIService.repository.RecommendationsRepository;
import com.fitness.AIService.services.GeminiService.GeminiAIActivityService;
import com.fitness.AIService.services.Kafka.ActivityMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListenerImpl implements ActivityMessageListener {

    private final GeminiAIActivityService geminiAIActivityService;
    private final RecommendationsRepository recommendationsRepository;

    @Override
    @KafkaListener(topics = "${kafka.activity.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void getRecommendationForActivity(Activity activity) {

        log.info("Received Activity for processing with Activity ID: {}", activity.getActivityId());

        Recommendation recommendation=geminiAIActivityService.getResponseFromGemini(activity);

        recommendationsRepository.save(recommendation);

    }
}
