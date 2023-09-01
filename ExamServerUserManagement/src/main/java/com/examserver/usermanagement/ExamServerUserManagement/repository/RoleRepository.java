package com.examserver.usermanagement.ExamServerUserManagement.repository;


import com.examserver.usermanagement.ExamServerUserManagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {


}
