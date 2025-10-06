package com.fitness.ActivityService.controller;

import com.fitness.ActivityService.dto.ActivityRequestDTO;
import com.fitness.ActivityService.dto.ActivityResponseDTO;
import com.fitness.ActivityService.services.IntraService.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {

    private ActivityService activityService;

    @PostMapping("/addActivity")
    public ResponseEntity<ActivityResponseDTO> addActivity(@RequestBody ActivityRequestDTO request) {

        ActivityResponseDTO activityResponseDTO = activityService.addActivity(request);

        return new ResponseEntity<>(activityResponseDTO, HttpStatus.OK);
    }

}
