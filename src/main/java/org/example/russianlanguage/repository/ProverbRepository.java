package org.example.russianlanguage.repository;

import org.example.russianlanguage.model.Proverb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProverbRepository extends MongoRepository<Proverb, String> {

}
