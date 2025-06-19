package com.Linkdin.linkdinbackend.features.authentication.controller;

import com.Linkdin.linkdinbackend.features.authentication.dto.AuthenticationOauthRequestBody;
import com.Linkdin.linkdinbackend.features.authentication.dto.AuthenticationRequestBody;
import com.Linkdin.linkdinbackend.features.authentication.dto.AuthenticationResponseBody;
import com.Linkdin.linkdinbackend.features.authentication.model.User;
import com.Linkdin.linkdinbackend.features.authentication.service.AuthenticationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationUserService;

    public AuthenticationController(AuthenticationService authenticationUserService) {
        this.authenticationUserService = authenticationUserService;
    }

    @PostMapping("/login")
    public AuthenticationResponseBody loginPage(@Valid @RequestBody AuthenticationRequestBody loginRequestBody) {
        return authenticationUserService.login(loginRequestBody);
    }

    @PostMapping("/register")
    public AuthenticationResponseBody registerPage(@Valid @RequestBody AuthenticationRequestBody registerRequestBody) {
        return authenticationUserService.register(registerRequestBody);
    }

    @GetMapping("/user")
    public User getUser(@RequestAttribute("authenticatedUser") User user) {
        return user;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestAttribute("authenticatedUser") User user) {
        authenticationUserService.deleteUser(user.getId());
        return ResponseEntity.ok("🗑️ User deleted successfully.");
    }

    @PutMapping("/validate-email-verification-token")
    public ResponseEntity<String> verifyEmail(
            @RequestParam String token,
            @RequestAttribute("authenticatedUser") User user) {
        authenticationUserService.validateEmailVerificationToken(token, user.getEmail());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("✅ Email verified successfully!");
    }

    @GetMapping("/send-email-verification-token")
    public ResponseEntity<String> sendEmailVerificationToken(@RequestAttribute("authenticatedUser") User user) {
        authenticationUserService.sendEmailVerificationToken(user.getEmail());
        return ResponseEntity.ok("📨 Email verification token sent successfully!");
    }

    @PutMapping("/send-password-reset-token")
    public ResponseEntity<String> sendPasswordResetToken(@RequestParam String email) {
        authenticationUserService.sendPasswordResetToken(email);
        return ResponseEntity.ok("🔐 Password reset token sent to " + email);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String newPassword,
            @RequestParam String token,
            @RequestParam String email) {
        authenticationUserService.resetPassword(email, newPassword, token);
        return ResponseEntity.ok("🔑 Password reset successful!");
    }

}
