package com.medelin.controller.auth;

import com.medelin.dto.*;
import com.medelin.service.AuthenticationService;
import com.medelin.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "A. Authentication Management", description = "APIs for managing authentication")
public class AuthenticationController
{
    private final IAuthenticationService IAuthenticationService;

    @Operation(
            summary = "1. Register a new user",
            description = "Creates a new user with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data (e.g., missing fields, invalid format)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already in use",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid CreateUserRequest request)
    {
        AuthenticationResponse created = IAuthenticationService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "2. Authenticates the registered user",
            description = "Checks if the users details used to log in are correct then authenticates the user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User authenticated",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data (e.g., missing fields, invalid format)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
    {
        AuthenticationResponse authenticated = IAuthenticationService.authenticate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticated);
    }

    @Operation(
            summary = "3. Helps the user to reset their password",
            description = "Gets the user email address then sends a reset password link"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Forgot password reset link sent successfully",
                    content = @Content(schema = @Schema(implementation = ForgotPasswordResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data (e.g., missing fields, invalid format)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    )
    {
        ForgotPasswordResponse forgotPassword = IAuthenticationService.forgotPassword(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(forgotPassword);
    }

    @Operation(
            summary = "4. Updates the user password to the new one",
            description = "Gets the user email address and the new password then updates the user password"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset successfully",
                    content = @Content(schema = @Schema(implementation = ResetPasswordResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data (e.g., missing fields, invalid format)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @RequestBody ResetPasswordRequest request
    )
    {
        ResetPasswordResponse resetPassword = IAuthenticationService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resetPassword);
    }

    @Operation(
            summary = "5. Get a refresh token",
            description = "Gets the user email address then sends a new access token and a refresh token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Access token sent successfully",
                    content = @Content(schema = @Schema(implementation = RefreshTokenResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data (e.g., missing fields, invalid format)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest request
    )
    {
        return ResponseEntity.ok(IAuthenticationService.refreshToken(request));
    }

}
