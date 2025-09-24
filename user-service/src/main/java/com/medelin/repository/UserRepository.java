package com.medelin.repository;

import com.medelin.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User>
{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    //Page<T> findAll(Specification<T> spec, Pageable pageable);

}
