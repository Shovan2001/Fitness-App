package com.fitness.AIService.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "AIRecommendation")
public class Recommendation {

    @Id
    private String recommendationId;
    private String activityId;
    private String userId;
    private String recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safetyMeasures;

    @CreatedDate
    private LocalDateTime creationAt;


}
