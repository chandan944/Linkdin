package com.Linkdin.linkdinbackend.configration;

import com.Linkdin.linkdinbackend.features.authentication.model.AuthenticationUser;
import com.Linkdin.linkdinbackend.features.authentication.repository.AuthenticationUserRepository;
import com.Linkdin.linkdinbackend.features.authentication.utils.Encoder;
import com.Linkdin.linkdinbackend.features.feed.model.Post;
import com.Linkdin.linkdinbackend.features.feed.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * 🏗️ This class is responsible for INITIALIZING THE DATABASE with sample data
 * when the application starts. It creates:
 * - 5 sample user profiles
 * - 10 sample social media posts
 * - Random post likes and associations
 *
 * 🔧 How it works:
 * 1. Spring detects this @Configuration class at startup
 * 2. Creates the CommandLineRunner bean
 * 3. Executes the initialization logic after app context loads
 */
@Configuration
public class LoadDatabaseConfiguration {

    // 🔐 Password encoder for securely hashing user passwords
    private final Encoder encoder;

    // 💉 Constructor with dependency injection (Spring automatically provides the Encoder)
    public LoadDatabaseConfiguration(Encoder encoder) {
        this.encoder = encoder;
    }

    /**
     * 🏭 MAIN INITIALIZATION METHOD that Spring will execute automatically
     * @param authenticationUserRepository - Database access for users (auto-injected)
     * @param postRepository - Database access for posts (auto-injected)
     * @return CommandLineRunner that contains our initialization logic
     */
    @Bean
    public CommandLineRunner initDatabase(AuthenticationUserRepository authenticationUserRepository,
                                          PostRepository postRepository) {
        return args -> {
            // PHASE 1️⃣: Create 5 realistic user profiles
            List<AuthenticationUser> users = createUsers(authenticationUserRepository);

            // PHASE 2️⃣: Create 10 social posts with random associations
            createPosts(postRepository, users);

            // 🎉 At this point, database has:
            // - 5 users with complete profiles
            // - 10 posts with:
            //   - Random authors
            //   - Varied like counts (1-3)
            //   - 1 post with an image
        };
    }

    /**
     * 👥 USER CREATION FACTORY - Builds 5 realistic user profiles
     * @param authenticationUserRepository - Where to save the users
     * @return List of created users
     */
    private List<AuthenticationUser> createUsers(AuthenticationUserRepository authenticationUserRepository) {
        // 📋 Create list of sample users with complete professional profiles
        List<AuthenticationUser> users = List.of(
                // Format: email, password, firstName, lastName, position, company, location, profilePic
                createUser("john.doe@example.com", "john", "John", "Doe", "Software Engineer", "Docker Inc.",
                        "San Francisco, CA", "https://images.unsplash.com/photo-1633332755192-727a05c4013d"),
                createUser("anne.claire@example.com", "anne", "Anne", "Claire", "HR Manager", "eToro",
                        "Paris, Fr", "https://images.unsplash.com/photo-1494790108377-be9c29b29330"),
                createUser("arnauld.manner@example.com", "arnauld", "Arnauld", "Manner", "Product Manager", "Arc",
                        "Dakar, SN", "https://images.unsplash.com/photo-1640960543409-dbe56ccc30e2"),
                createUser("moussa.diop@example.com", "moussa", "Moussa", "Diop", "Software Engineer", "Orange",
                        "Bordeaux, FR", "https://images.unsplash.com/photo-1586297135537-94bc9ba060aa"),
                createUser("awa.diop@example.com", "awa", "Awa", "Diop", "Data Scientist", "Zoho",
                        "New Delhi, IN", "https://images.unsplash.com/photo-1640951613773-54706e06851d")
        );

        // 💾 Save all users to database in one batch operation
        authenticationUserRepository.saveAll(users);
        return users;
    }

    /**
     * 🛠️ USER BUILDER - Constructs a single user with complete profile
     * @param email - User's email (also serves as username)
     * @param password - Plain text password (will be encoded)
     * @param firstName - User's first name
     * @param lastName - User's last name
     * @param position - Job title
     * @param company - Employer
     * @param location - Geographic location
     * @param profilePicture - URL to profile image
     * @return Fully configured AuthenticationUser object
     */
    private AuthenticationUser createUser(String email, String password, String firstName, String lastName,
                                          String position, String company, String location, String profilePicture) {
        // 🔄 Create user with encoded password (security best practice)
        AuthenticationUser user = new AuthenticationUser(email, encoder.encode(password));

        // 🧑‍💼 Set professional profile details
        user.setEmailVerified(true); // Mark email as verified
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPosition(position);
        user.setCompany(company);
        user.setLocation(location);
        user.setProfilePicture(profilePicture);

        return user;
    }

    /**
     * 📝 POST GENERATOR - Creates 10 sample social media posts
     * @param postRepository - Where to save posts
     * @param users - List of potential authors and likers
     */
    private void createPosts(PostRepository postRepository, List<AuthenticationUser> users) {
        Random random = new Random(); // 🎲 Random number generator

        // 🔄 Create 10 posts with varying characteristics
        for (int j = 1; j <= 10; j++) {
            // ✍️ Create basic post with:
            // - Sample text content
            // - Random author selected from our users
            Post post = new Post("Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    users.get(random.nextInt(users.size())));

            // ❤️ Add social proof (likes from random users)
            post.setLikes(generateLikes(users, j, random));

            // 🖼️ Special treatment for first post (gets an image)
            if (j == 1) {
                post.setPicture("https://images.unsplash.com/photo-1731176497854-f9ea4dd52eb6");
            }

            // 💾 Save post to database
            postRepository.save(post);
        }
    }

    /**
     * ❤️ LIKE GENERATOR - Creates realistic engagement patterns
     * @param users - Pool of potential likers
     * @param postNumber - Used to create variation in like counts
     * @param random - Random number generator
     * @return Set of users who liked the post
     */
    private HashSet<AuthenticationUser> generateLikes(List<AuthenticationUser> users, int postNumber, Random random) {
        HashSet<AuthenticationUser> likes = new HashSet<>();

        // 🏆 First post gets special treatment (3 likes)
        if (postNumber == 1) {
            while (likes.size() < 3) {
                likes.add(users.get(random.nextInt(users.size())));
            }
        } else {
            // 📊 Vary like counts based on post number for realism
            int likesCount = switch (postNumber % 5) {
                case 0 -> 3;  // Every 5th post gets 3 likes
                case 2, 3 -> 2; // Posts 2,3,7,8 get 2 likes
                default -> 1;  // Others get 1 like
            };

            // 👥 Add random users as likers
            for (int i = 0; i < likesCount; i++) {
                likes.add(users.get(random.nextInt(users.size())));
            }
        }
        return likes;
    }
}
