package com.fitness.AIService.repository;

import com.fitness.AIService.models.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationsRepository extends MongoRepository<Recommendation,String> {

    Optional<List<Recommendation>> findByUserId(String userId);

    Optional<Recommendation> findByActivityId(String activityId);
}
