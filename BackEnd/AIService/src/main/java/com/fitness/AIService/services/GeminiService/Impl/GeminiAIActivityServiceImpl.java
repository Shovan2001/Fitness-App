package com.fitness.AIService.services.GeminiService.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.AIService.models.*;
import com.fitness.AIService.services.GeminiService.GeminiAIActivityService;
import com.fitness.AIService.services.GeminiService.GeminiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiAIActivityServiceImpl implements GeminiAIActivityService {

    private final GeminiService geminiService;

    public Recommendation getResponseFromGemini(Activity activity) {
        String prompt = generatePromptForActivity(activity);
        GeminiRequestBody geminiRequestBody = buildGeminiRequest(prompt);

        String geminiResponse = geminiService.getGeminiResponse(geminiRequestBody);
        log.info("Gemini's Response: {}", geminiResponse);

        return convertGeminiResponseToRecommendationObject(activity, geminiResponse);
    }

    private Recommendation convertGeminiResponseToRecommendationObject(Activity activity, String geminiResponse) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(geminiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String processedGeminiResponse = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("```", "")
                    .replaceAll("\\n```", "")
                    .trim();

            log.info("Processed Gemini's Response: {}", processedGeminiResponse);

            JsonNode responseNode = objectMapper.readTree(processedGeminiResponse);

            JsonNode analysisNode = responseNode.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories Burned:");

            JsonNode improvementsNode = responseNode.path("improvements");
            List<String> improvements = extractImprovements(improvementsNode);

            JsonNode suggestionsNode = responseNode.path("suggestions");
            List<String> suggestions = extractSuggestions(suggestionsNode);

            JsonNode safetyNode = responseNode.path("safety");
            List<String> safety = extractSafety(safetyNode);

            return Recommendation.builder()
                    .activityId(activity.getActivityId())
                    .userId(activity.getUserId())
                    .activityType(activity.getActivityType().toString())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safetyMeasures(safety)
                    .creationAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("Error converting GeminiResponse to Recommendation {}", e.getMessage(), e);
            return defaultRecommendation(activity);
        }
    }

    private Recommendation defaultRecommendation(Activity activity) {

        return Recommendation.builder()
                .activityId(activity.getActivityId())
                .userId(activity.getUserId())
                .activityType(activity.getActivityType().toString())
                .recommendation("Unable to get detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine.."))
                .suggestions(Collections.singletonList("Please contact a fitness coach !!"))
                .safetyMeasures(Arrays.asList("Always warm up", "Stay Hydrated", "Sleep aleast 8 hours a day"))
                .creationAt(LocalDateTime.now())
                .build();
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {

        if (!analysisNode.path(key).isMissingNode()) {
            fullAnalysis.append(prefix).append(analysisNode.path(key).asText()).append("\n\n");
        }
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {

        List<String> improvements = new ArrayList<>();

        if (improvementsNode.isArray()) {
            for (JsonNode improvementNode : improvementsNode) {

                String area = improvementNode.path("area").asText();
                String recommendation = improvementNode.path("recommendation").asText();

                improvements.add(String.format("%s: %s", area, recommendation));
            }
        }

        return (improvements.isEmpty()) ?
                Collections.singletonList("No specific improvements available !!") : improvements;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {

        List<String> suggestions = new ArrayList<>();

        if (suggestionsNode.isArray()) {
            for (JsonNode suggestionNode : suggestionsNode) {

                String workout = suggestionNode.path("workout").asText();
                String description = suggestionNode.path("description").asText();

                suggestions.add(String.format("%s: %s", workout, description));
            }
        }

        return (suggestions.isEmpty()) ?
                Collections.singletonList("No specific suggestions available !!") : suggestions;
    }

    private List<String> extractSafety(JsonNode safetyNode) {

        List<String> safety = new ArrayList<>();

        for (JsonNode node : safetyNode) {
            safety.add(node.asText());
        }

        return safety.isEmpty() ? Collections.singletonList("No specific safety tips available !!") : safety;
    }

    private String generatePromptForActivity(Activity activity) {

        return String.format("""
                        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
                        {
                           "analysis": {
                             "overall": "Overall analysis here",
                             "pace": "Pace analysis here",
                             "heartRate": "Heart rate analysis here",
                             "caloriesBurned": "Calories analysis here"
                           },
                           "improvements": [
                              {
                               "area": "Area name",
                               "recommendation": "Detailed recommendation"
                               }
                           ],
                           "suggestions": [
                               {
                                "workout": "Workout name",
                                "description": "Detailed workout description"
                               }
                           ],
                           "safety": [
                               "Safety Point 1",
                               "Safety Point 2",
                           ]
                        }
                        
                        Analyze this activity:
                           Activity Type: %s
                           Duration: %d minutes
                           Calories Burned: %d
                           Additional Metrics: %s
                        
                        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
                        Ensure the response follows the EXACT same JSON format shown above.
                        """,
                activity.getActivityType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }


    private GeminiRequestBody buildGeminiRequest(String prompt) {

        Part part = new Part();
        part.setText(prompt);
        List<Part> parts = List.of(part);

        Content content = new Content();
        content.setParts(parts);
        List<Content> contents = List.of(content);

        GeminiRequestBody geminiRequestBody = new GeminiRequestBody();
        geminiRequestBody.setContents(contents);

        return geminiRequestBody;
    }

}
