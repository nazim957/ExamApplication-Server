package com.examserver.usermanagement.ExamServerUserManagement.service;



import com.examserver.usermanagement.ExamServerUserManagement.entity.User;
import com.examserver.usermanagement.ExamServerUserManagement.entity.UserRole;

import java.util.Set;

public interface UserService {

    //creating user
     User createUser(User user, Set<UserRole> userRole) throws Exception;

    //get user by username
     User getUser(String email);

    //delete user by id
     void deleteUser(Long userId);
}
