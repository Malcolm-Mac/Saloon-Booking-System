package com.medelin.service;

import com.medelin.dto.UpdateUserRequest;
import com.medelin.dto.UserDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService
{
    public Page<UserDetailResponse> getUsers(Pageable pageable);
    public UserDetailResponse getUser(Long id);
    public void deleteUser(Long id);
    public UserDetailResponse updateUser(Long userId, UpdateUserRequest request);
    public Page<UserDetailResponse> searchUsers(String name, String email, Pageable pageable);
}
