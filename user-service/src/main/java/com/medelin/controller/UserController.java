package com.medelin.controller;

import com.medelin.dto.*;
import com.medelin.util.IdHasherUtil;
import com.medelin.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "B. User Management", description = "APIs for managing users")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class UserController
{
    private final IUserService IUserService;
    private final IdHasherUtil idHasherUtil;

    @Operation(
            summary = "Get all users",
            description = "Retrieves all users information based on pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserDetailResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<UserDetailResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sort
    )
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<UserDetailResponse> users = IUserService.getUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves all users information based on pagination"
    )
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<UserDetailResponse>> searchUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "name") String sort
    )
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<UserDetailResponse> users = IUserService.searchUsers(name,email,pageable);
        return ResponseEntity.ok(PaginatedResponse.from(users));
    }

    @Operation(
            summary = "Get a user by ID",
            description = "Retrieves a user's information based on their ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> getUser(
            @PathVariable String id
    )
    {
        Long userId = idHasherUtil.decode(id);
        UserDetailResponse user = IUserService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(
            summary = "Update user details",
            description = "Retrieves a user's information based on their ID then updates"
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailResponse> updateUser(
            @PathVariable String id,
            @RequestBody @Valid UpdateUserRequest request
    )
    {
        Long userId = idHasherUtil.decode(id);
        UserDetailResponse updatedUser = IUserService.updateUser(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary="Delete user profile",
            description = "Deletes user's information based on their ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String id
    )
    {
        Long userId = idHasherUtil.decode(id);
        IUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
