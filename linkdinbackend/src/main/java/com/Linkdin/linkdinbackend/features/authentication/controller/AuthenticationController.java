package com.Linkdin.linkdinbackend.features.authentication.controller;

import com.Linkdin.linkdinbackend.features.authentication.dto.AuthenticationRequestBody;
import com.Linkdin.linkdinbackend.features.authentication.dto.AuthenticationResponseBody;
import com.Linkdin.linkdinbackend.features.authentication.model.AuthenticationUser;
import com.Linkdin.linkdinbackend.features.authentication.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * 🔑 AUTHENTICATION GATEWAY - Handles all user identity operations
 *
 * 📍 Endpoint Base: /api/v1/authentication
 *
 * Features:
 * ✅ Login/Registration
 * ✅ Email Verification
 * ✅ Password Reset
 * ✅ Profile Management
 * ✅ Account Deletion
 */
@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    // 🔌 Service layer connection (auto-wired by Spring)
    private final AuthenticationService authenticationUserService;

    // ⚡ Constructor injection
    public AuthenticationController(AuthenticationService authenticationUserService) {
        this.authenticationUserService = authenticationUserService;
    }

    /**
     * 🔑 LOGIN ENDPOINT
     * POST /api/v1/authentication/login
     *
     * @param loginRequestBody - Contains email and password
     * @return JWT token + user data if successful
     */
    @PostMapping("/login")
    public AuthenticationResponseBody loginPage(
            @Valid @RequestBody AuthenticationRequestBody loginRequestBody) {
        return authenticationUserService.login(loginRequestBody);
    }

    /**
     * 📝 REGISTRATION ENDPOINT
     * POST /api/v1/authentication/register
     *
     * @param registerRequestBody - Contains email, password, etc.
     * @return JWT token + user data if successful
     */
    @PostMapping("/register")
    public AuthenticationResponseBody registerPage(
            @Valid @RequestBody AuthenticationRequestBody registerRequestBody) {
        return authenticationUserService.register(registerRequestBody);
    }

    /**
     * 🗑️ ACCOUNT DELETION
     * DELETE /api/v1/authentication/delete
     *
     * 🔒 Secured by @RequestAttribute (user must be authenticated)
     * @param user - Automatically injected from security context
     * @return Success message
     */
    @DeleteMapping("/delete")
    public String deleteUser(
            @RequestAttribute("authenticatedUser") AuthenticationUser user) {
        authenticationUserService.deleteUser(user.getId());
        return "User deleted successfully.";
    }

    /**
     * 👤 GET CURRENT USER PROFILE
     * GET /api/v1/authentication/user
     *
     * @param user - Injected from security context
     * @return Full user profile
     */
    @GetMapping("/user")
    public AuthenticationUser getUser(
            @RequestAttribute("authenticatedUser") AuthenticationUser user) {
        return user;
    }

    /**
     * ✉️ EMAIL VERIFICATION
     * PUT /api/v1/authentication/validate-email-verification-token?token=XXX
     *
     * @param token - Verification token sent via email
     * @param user - Current user (must match token email)
     * @return Success message
     */
    @PutMapping("/validate-email-verification-token")
    public String verifyEmail(
            @RequestParam String token,
            @RequestAttribute("authenticatedUser") AuthenticationUser user) {
        authenticationUserService.validateEmailVerificationToken(token, user.getEmail());
        return "Email verified successfully.";
    }

    /**
     * 🔄 RESEND VERIFICATION EMAIL
     * GET /api/v1/authentication/send-email-verification-token
     *
     * @param user - Current user
     * @return Success message
     */
    @GetMapping("/send-email-verification-token")
    public String sendEmailVerificationToken(
            @RequestAttribute("authenticatedUser") AuthenticationUser user) {
        authenticationUserService.sendEmailVerificationToken(user.getEmail());
        return "Email verification token sent successfully.";
    }

    /**
     * 🔐 INITIATE PASSWORD RESET
     * PUT /api/v1/authentication/send-password-reset-token?email=XXX
     *
     * @param email - User's registered email
     * @return Success message
     */
    @PutMapping("/send-password-reset-token")
    public String sendPasswordResetToken(@RequestParam String email) {
        authenticationUserService.sendPasswordResetToken(email);
        return "Password reset token sent successfully.";
    }

    /**
     * 🔄 COMPLETE PASSWORD RESET
     * PUT /api/v1/authentication/reset-password?newPassword=XXX&token=XXX&email=XXX
     *
     * @param newPassword - New password to set
     * @param token - One-time reset token
     * @param email - User's email
     * @return Success message
     */
    @PutMapping("/reset-password")
    public String resetPassword(
            @RequestParam String newPassword,
            @RequestParam String token,
            @RequestParam String email) {
        authenticationUserService.resetPassword(email, newPassword, token);
        return "Password reset successfully.";
    }

    /**
     * 🖋️ UPDATE USER PROFILE
     * PUT /api/v1/authentication/profile/{id}
     *
     * 🔒 User can only update their own profile
     * @param user - Current user (for permission check)
     * @param id - Profile ID to update
     * @param firstName - (Optional) New first name
     * @param lastName - (Optional) New last name
     * @param company - (Optional) New company
     * @param position - (Optional) New position
     * @param location - (Optional) New location
     * @return Updated user profile
     */
    @PutMapping("/profile/{id}")
    public AuthenticationUser updateUserProfile(
            @RequestAttribute("authenticatedUser") AuthenticationUser user,
            @PathVariable Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String location) {

        // 🛡️ Security check - prevent users editing others' profiles
        if (!user.getId().equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User does not have permission to update this profile.");
        }

        return authenticationUserService.updateUserProfile(
                id, firstName, lastName, company, position, location);
    }
}