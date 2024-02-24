package org.example.russianlanguage.repository;

import org.example.russianlanguage.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findUserByPasswordAndUsername(String password, String username);
    User findUserByUsername(String username);
}
