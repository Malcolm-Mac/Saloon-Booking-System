package com.medelin.service;

import com.medelin.exception.AuthenticationException;
import com.medelin.exception.UserNotFoundException;
import com.medelin.util.IdHasherUtil;
import com.medelin.security.JwtService;
import com.medelin.dto.AuthenticationRequest;
import com.medelin.dto.AuthenticationResponse;
import com.medelin.dto.CreateUserRequest;
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
public class AuthenticationService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CreateUserRequestMapper createUserRequestMapper;
    private final JwtService jwtService;
    private final IdHasherUtil idHasherUtil;
    private final AuthenticationManager  authenticationManager;

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
        var expiresIn = jwtService.getExpirationTime();
        String hashedId = idHasherUtil.encode(savedUser.getId());

        return AuthenticationResponse
                .builder()
                .hashedId(hashedId)
                .expiresIn(expiresIn)
                .token(jwtToken)
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
                () -> new AuthenticationException("User not found")
        );

        String hashedId = idHasherUtil.encode(user.getId());

        var jwtToken = jwtService.generateToken(user);

        var expiresIn = jwtService.getExpirationTime();

        return AuthenticationResponse
                .builder()
                .hashedId(hashedId)
                .expiresIn(expiresIn)
                .token(jwtToken)
                .build();
    }
}
