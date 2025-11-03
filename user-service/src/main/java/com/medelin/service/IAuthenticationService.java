package com.medelin.service;

import com.medelin.dto.AuthenticationRequest;
import com.medelin.dto.AuthenticationResponse;
import com.medelin.dto.CreateUserRequest;

public interface IAuthenticationService
{
    public AuthenticationResponse createUser(CreateUserRequest request);
    public AuthenticationResponse  authenticate(AuthenticationRequest request);
}
