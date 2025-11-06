package com.fitness.AIService.services.GeminiService;

import com.fitness.AIService.models.Activity;
import com.fitness.AIService.models.Recommendation;

public interface GeminiAIActivityService {

    Recommendation getResponseFromGemini(Activity activity);
}
