package com.fitness.ActivityService.services;

import com.fitness.ActivityService.dto.ActivityRequestDTO;
import com.fitness.ActivityService.dto.ActivityResponseDTO;

public interface ActivityService {
    ActivityResponseDTO addActivity(ActivityRequestDTO activity);
}
