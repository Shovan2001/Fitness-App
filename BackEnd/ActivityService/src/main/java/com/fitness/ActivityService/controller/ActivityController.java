package com.fitness.ActivityService.controller;

import com.fitness.ActivityService.dto.ActivityRequestDTO;
import com.fitness.ActivityService.dto.ActivityResponseDTO;
import com.fitness.ActivityService.services.IntraService.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{activityId}/validate")
    public ResponseEntity<Boolean> validateActivityId(@PathVariable String activityId) {

        boolean isValidActivityId=activityService.validateActivityId(activityId);

        return new ResponseEntity<>(isValidActivityId, HttpStatus.OK);
    }

}
