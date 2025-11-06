package com.fitness.AIService.models;

import lombok.Data;
import java.util.List;

@Data
public class GeminiRequestBody {
    private List<Content> contents;
}
