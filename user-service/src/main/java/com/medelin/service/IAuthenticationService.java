package com.medelin.service;

import com.medelin.dto.*;

public interface IAuthenticationService
{
    public AuthenticationResponse createUser(CreateUserRequest request);
    public AuthenticationResponse  authenticate(AuthenticationRequest request);
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request);
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}
