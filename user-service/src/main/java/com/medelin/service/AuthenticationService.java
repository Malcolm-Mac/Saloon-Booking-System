package com.medelin.service;

import com.medelin.dto.*;
import com.medelin.exception.AuthenticationException;
import com.medelin.exception.UserNotFoundException;
import com.medelin.util.IdHasherUtil;
import com.medelin.security.JwtService;
import com.medelin.exception.DuplicateEmailException;
import com.medelin.mapper.CreateUserRequestMapper;
import com.medelin.model.User;
import com.medelin.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class AuthenticationService implements IAuthenticationService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CreateUserRequestMapper createUserRequestMapper;
    private final JwtService jwtService;
    private final IdHasherUtil idHasherUtil;
    private final AuthenticationManager  authenticationManager;
    private final ICommunicationService communicationService;

    public AuthenticationResponse createUser(CreateUserRequest request)
    {
        if(userRepository.existsByEmail(request.email()))
        {
            throw new DuplicateEmailException("Email already exists: " + request.email());
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = createUserRequestMapper.toEntity(request, encodedPassword);

        User savedUser = userRepository.save(user);

        var jwtToken = jwtService.generateToken(savedUser);
        var jwtRefreshToken = jwtService.generateRefreshToken(savedUser.getEmail());
        var expiresIn = jwtService.getExpirationTime();
        String hashedId = idHasherUtil.encode(savedUser.getId());

        return AuthenticationResponse
                .builder()
                .hashedId(hashedId)
                .expiresIn(expiresIn)
                .accessToken(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public AuthenticationResponse  authenticate(AuthenticationRequest request)
    {
        try
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        }catch (AuthenticationException ex)
        {
            log.error("Authentication Exception: ", ex);
            throw new AuthenticationException("Invalid email or password");
        }

        var user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new AuthenticationException("Invalid email or password")
        );

        String hashedId = idHasherUtil.encode(user.getId());

        var jwtToken = jwtService.generateToken(user);

        var jwtRefreshToken = jwtService.generateRefreshToken(user.getEmail());

        var expiresIn = jwtService.getExpirationTime();

        return AuthenticationResponse
                .builder()
                .hashedId(hashedId)
                .expiresIn(expiresIn)
                .accessToken(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request)
    {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        String token = jwtService.generateResetPasswordToken(user);

        String resetLink = "http://localhost:5001/reset-password?token=" + token;

        communicationService.sendPasswordResetEmail(user.getEmail(), resetLink);

         return new ForgotPasswordResponse("Reset link sent to email");
    }

    public ResetPasswordResponse resetPassword(ResetPasswordRequest request)
    {
        String email = jwtService.extractEmailFromResetToken(request.token());

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        return new ResetPasswordResponse("Password reset successful");
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request)
    {
        String refreshToken = request.refreshToken();

        if (!jwtService.isRefreshTokenValid(refreshToken))
        {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String email = jwtService.extractEmailFromRefreshToken(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(email);

        return new RefreshTokenResponse(newAccessToken, newRefreshToken);
    }
}
