package com.example.tinylink.repo;

import com.example.tinylink.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface LinkRepository extends MongoRepository<Link, String> {
    Optional<Link> findByCode(String code);
    boolean existsByCode(String code);
    void deleteByCode(String code);
}
