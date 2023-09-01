package com.examserver.usermanagement.ExamServerUserManagement.repository;


import com.examserver.usermanagement.ExamServerUserManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);



}
