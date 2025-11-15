package com.fitness.ActivityService.services.IntraService.Impl;

import com.fitness.ActivityService.dto.ActivityRequestDTO;
import com.fitness.ActivityService.dto.ActivityResponseDTO;
import com.fitness.ActivityService.models.Activity;
import com.fitness.ActivityService.repository.ActivityRepository;
import com.fitness.ActivityService.services.InterService.UserValidationService;
import com.fitness.ActivityService.services.IntraService.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl implements ActivityService {

//    private static final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);
    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.activity.topic.name}")
    private String activityTopicName;

    @Override
    public ActivityResponseDTO addActivity(ActivityRequestDTO request) {

        boolean isValidUserId=userValidationService.validateUserByUserId(request.getUserId());

        boolean isValidKeyCloakId=userValidationService.validateUserByKeyCloakId(request.getKeycloakId());

        if(!isValidUserId || !isValidKeyCloakId) {
            throw new RuntimeException("Invalid user. No Such UserId exists !! "+request.getUserId());
        }

        log.info("Valid userId: {}", request.getUserId());
        log.info("Valid keyCloakId: {}", request.getKeycloakId());

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .keyCloakId(request.getKeycloakId())
                .activityType(request.getActivityType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity=activityRepository.save(activity);

        log.info("Activity Saved to DB with Activity ID: {}", savedActivity.getActivityId());

        //Send the activity to AI service as well to generate AI recommendations via Kafka
        kafkaTemplate.send(activityTopicName, savedActivity.getUserId() ,savedActivity);

        return getActivityResponseDTOFromActivity(savedActivity);
    }

    @Override
    public boolean validateActivityId(String activityId) {

        return activityRepository.existsById(activityId);
    }

    private ActivityResponseDTO getActivityResponseDTOFromActivity(Activity activity)
    {
        ActivityResponseDTO activityResponseDTO=new  ActivityResponseDTO();

        activityResponseDTO.setActivityId(activity.getActivityId());
        activityResponseDTO.setUserId(activity.getUserId());
        activityResponseDTO.setKeyCloakId(activity.getKeyCloakId());
        activityResponseDTO.setActivityType(activity.getActivityType());
        activityResponseDTO.setDuration(activity.getDuration());
        activityResponseDTO.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponseDTO.setAdditionalMetrics(activity.getAdditionalMetrics());
        activityResponseDTO.setStartTime(activity.getStartTime());
        activityResponseDTO.setCreatedAt(activity.getCreatedAt());
        activityResponseDTO.setUpdatedAt(activity.getUpdatedAt());

        return activityResponseDTO;
    }
}
