package com.fitness.ActivityService.services.IntraService.Impl;

import com.fitness.ActivityService.dto.ActivityRequestDTO;
import com.fitness.ActivityService.dto.ActivityResponseDTO;
import com.fitness.ActivityService.models.Activity;
import com.fitness.ActivityService.repository.ActivityRepository;
import com.fitness.ActivityService.services.InterService.UserValidationService;
import com.fitness.ActivityService.services.IntraService.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    private final UserValidationService userValidationService;

    @Override
    public ActivityResponseDTO addActivity(ActivityRequestDTO request) {

        boolean isValidUser=userValidationService.validateUser(request.getUserId());

        if(!isValidUser){
            throw new RuntimeException("Invalid user. No Such UserId exists !! "+request.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .activityType(request.getActivityType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity=activityRepository.save(activity);

        return getActivityResponseDTOFromActivity(savedActivity);
    }

    private ActivityResponseDTO getActivityResponseDTOFromActivity(Activity activity)
    {
        ActivityResponseDTO activityResponseDTO=new  ActivityResponseDTO();

        activityResponseDTO.setActivityId(activity.getActivityId());
        activityResponseDTO.setUserId(activity.getUserId());
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
