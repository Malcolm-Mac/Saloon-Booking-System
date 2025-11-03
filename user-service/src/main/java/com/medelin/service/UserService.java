package com.medelin.service;

import com.medelin.dto.UpdateUserRequest;
import com.medelin.mapper.UpdateUserRequestMapper;
import com.medelin.specification.UserSpecification;
import com.medelin.util.IdHasherUtil;
import com.medelin.dto.UserDetailResponse;
import com.medelin.exception.UserNotFoundException;
import com.medelin.model.User;
import com.medelin.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements IUserService
{
    private final UserRepository userRepository;
    private final IdHasherUtil idHasherUtil;
    private final UpdateUserRequestMapper updateUserRequestMapper;

    public Page<UserDetailResponse> getUsers(Pageable pageable)
    {
        return userRepository
                .findAll(pageable)
                .map(user -> UserDetailResponse.from(user,idHasherUtil));
    }

    public UserDetailResponse getUser(Long id)
    {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent())
        {
            return UserDetailResponse.from(user.get(), idHasherUtil);
        }

        throw new UserNotFoundException("User with this ID: "+idHasherUtil.encode(id)+" does not exists");
    }

    public void deleteUser(Long id)
    {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with this ID: " + idHasherUtil.encode(id) + " does not exist"));

        userRepository.deleteById(id);
    }

    public UserDetailResponse updateUser(Long userId, UpdateUserRequest request)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with this ID: " + userId));

        updateUserRequestMapper.updateEntity(request, user);

        User updatedUser = userRepository.save(user);
        return UserDetailResponse.from(updatedUser, idHasherUtil);
    }

    public Page<UserDetailResponse> searchUsers(String name, String email, Pageable pageable)
    {
        Specification<User> spec = (root, query, cb) -> cb.conjunction();

        if (name != null && !name.trim().isEmpty()) {
            spec = spec.and(UserSpecification.nameContains(name));
        }

        if (email != null && !email.trim().isEmpty()) {
            spec = spec.and(UserSpecification.emailContains(email));
        }

        Page<User> users = userRepository.findAll(spec, pageable);

        return users.map(user -> UserDetailResponse.from(user, idHasherUtil));
    }
}
