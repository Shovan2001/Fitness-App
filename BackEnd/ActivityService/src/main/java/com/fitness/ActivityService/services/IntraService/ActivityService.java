package com.fitness.ActivityService.services.IntraService;

import com.fitness.ActivityService.dto.ActivityRequestDTO;
import com.fitness.ActivityService.dto.ActivityResponseDTO;

public interface ActivityService {
    ActivityResponseDTO addActivity(ActivityRequestDTO activity);

    boolean validateActivityId(String activityId);
}
