package dev.gabriel.bugtracker.Service;

import dev.gabriel.bugtracker.Model.User;
import dev.gabriel.bugtracker.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
