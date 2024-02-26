package org.example.russianlanguage.repository;

import org.example.russianlanguage.model.Proverb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProverbRepository extends MongoRepository<Proverb, String> {
    List<Proverb> getProverbsByCategory(String category);

}
