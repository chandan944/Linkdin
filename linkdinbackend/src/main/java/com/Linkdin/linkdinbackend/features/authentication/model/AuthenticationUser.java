package com.Linkdin.linkdinbackend.features.authentication.model;
import com.Linkdin.linkdinbackend.features.feed.model.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 🧑💼 CORE USER ENTITY - Represents a user account in the system.
 *
 * 📌 Key Responsibilities:
 * - Stores all user authentication data
 * - Maintains profile information
 * - Manages email verification state
 * - Handles password reset tokens
 * - Tracks profile completion status
 *
 * 🔗 Relationships:
 * - One-to-Many with Post (user authored posts)
 *
 * 🏷️ JPA Annotation: @Entity(name = "users") → Maps to "users" table in DB
 */
@Getter
@Entity(name = "users")
public class AuthenticationUser {

    // 🔑 ID ACCESSOR
    // 🔑 PRIMARY KEY - Auto-generated unique identifier
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 📧 EMAIL FIELD - Must be unique and properly formatted
    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    // ✅ EMAIL VERIFICATION SYSTEM
    private Boolean emailVerified = false; // Tracks verification status
    private String emailVerificationToken = null; // Stores hashed verification token
    private LocalDateTime emailVerificationTokenExpiryDate = null; // Token expiration

    // 🔒 PASSWORD FIELD - Never exposed in JSON responses
    @JsonIgnore
    private String password;

    // 🔄 PASSWORD RESET SYSTEM
    private String passwordResetToken = null; // Stores hashed reset token
    private LocalDateTime passwordResetTokenExpiryDate = null; // Reset token expiration

    // 👤 PROFILE INFORMATION
    private String firstName = null;
    private String lastName = null;
    private String company = null;
    private String position = null;
    private String location = null;
    // 🖼️ PROFILE PICTURE ACCESSORS
    private String profilePicture = null;

    // 📊 PROFILE COMPLETION ACCESSOR
    // 📊 PROFILE COMPLETION TRACKING
    private Boolean profileComplete = false; // Automatically calculated

    // 📝 POSTS RELATIONSHIP ACCESSORS
    // 📝 POST RELATIONSHIP - All posts created by this user
    @JsonIgnore // Prevent circular references in JSON
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    // 🏗️ CONSTRUCTORS

    /**
     * 🔨 Primary constructor for user creation
     * @param email - User's unique email address
     * @param password - Pre-hashed password
     */
    public AuthenticationUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * ⚠️ Default constructor required by JPA
     */
    public AuthenticationUser() {
    }

    // 🔄 PROFILE COMPLETION LOGIC

    /**
     * 📊 Updates the profileComplete flag based on current fields
     * Called automatically when relevant fields are updated
     */
    public void updateProfileCompletionStatus() {
        this.profileComplete = (this.firstName != null
                && this.lastName != null
                && this.company != null
                && this.position != null
                && this.location != null);
    }

    // ⚙️ GETTERS AND SETTERS (with additional behavior where needed)

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public void setEmailVerificationTokenExpiryDate(LocalDateTime emailVerificationTokenExpiryDate) {
        this.emailVerificationTokenExpiryDate = emailVerificationTokenExpiryDate;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public void setPasswordResetTokenExpiryDate(LocalDateTime passwordResetTokenExpiryDate) {
        this.passwordResetTokenExpiryDate = passwordResetTokenExpiryDate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 🧑🤝🧑 PROFILE FIELD SETTERS (with auto-completion tracking)

    /**
     * Sets first name AND updates profile completion status
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateProfileCompletionStatus();
    }

    /**
     * Sets last name AND updates profile completion status
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateProfileCompletionStatus();
    }

    /**
     * Sets location AND updates profile completion status
     */
    public void setLocation(String location) {
        this.location = location;
        updateProfileCompletionStatus();
    }

    /**
     * Sets position AND updates profile completion status
     */
    public void setPosition(String position) {
        this.position = position;
        updateProfileCompletionStatus();
    }

    /**
     * Sets company AND updates profile completion status
     */
    public void setCompany(String company) {
        this.company = company;
        updateProfileCompletionStatus();
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}