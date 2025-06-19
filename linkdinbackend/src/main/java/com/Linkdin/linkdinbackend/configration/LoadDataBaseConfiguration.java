package com.Linkdin.linkdinbackend.configration;

import com.Linkdin.linkdinbackend.features.authentication.model.User;
import com.Linkdin.linkdinbackend.features.authentication.repository.UserRepository;
import com.Linkdin.linkdinbackend.features.authentication.utils.Encoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDataBaseConfiguration {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, Encoder encoder) {
        return args -> {
            // Create a properly initialized User object
            User user = new User("test@example.com", encoder.encode("password123"));
            user.setEmailVerified(true);
            userRepository.save(user);
        };
    }
}