package com.fitness.UserService.repository;

import com.fitness.UserService.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,String> {

    boolean existsByEmail(String email);
}
