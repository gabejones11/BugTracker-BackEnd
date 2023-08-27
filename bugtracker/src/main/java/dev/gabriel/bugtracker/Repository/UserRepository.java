package dev.gabriel.bugtracker.Repository;

import dev.gabriel.bugtracker.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {


    Optional<User> findByEmail(String email);
    Optional<User> findByUserID(String userID);

}
