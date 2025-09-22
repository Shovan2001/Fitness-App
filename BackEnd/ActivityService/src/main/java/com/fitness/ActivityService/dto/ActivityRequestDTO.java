package com.fitness.ActivityService.dto;

import com.fitness.ActivityService.models.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequestDTO {

    //each activity must be associated with a user
    private String userId; // ID of the User

    private ActivityType activityType;

    private Integer duration;
    private Integer caloriesBurned;

    private LocalDateTime startTime;

    private Map<String, Object> additionalMetrics;

}
