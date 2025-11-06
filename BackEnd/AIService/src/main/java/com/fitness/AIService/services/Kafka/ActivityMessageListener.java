package com.fitness.AIService.services.Kafka;

import com.fitness.AIService.models.Activity;

public interface ActivityMessageListener {

    void getRecommendationForActivity(Activity activity);

}
