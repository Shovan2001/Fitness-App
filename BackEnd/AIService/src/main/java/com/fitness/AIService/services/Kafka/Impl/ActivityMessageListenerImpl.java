package com.fitness.AIService.services.Kafka.Impl;

import com.fitness.AIService.models.Activity;
import com.fitness.AIService.services.Kafka.ActivityMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListenerImpl implements ActivityMessageListener {

    @Override
    @KafkaListener(topics = "${kafka.activity.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void processActivity(Activity activity) {
        log.info("Received Activity for processing with Activity ID: {}", activity.getActivityId());

    }
}
