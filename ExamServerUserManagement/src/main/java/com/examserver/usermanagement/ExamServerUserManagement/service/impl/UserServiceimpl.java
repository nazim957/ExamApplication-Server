package com.examserver.usermanagement.ExamServerUserManagement.service.impl;


import com.examserver.usermanagement.ExamServerUserManagement.entity.User;
import com.examserver.usermanagement.ExamServerUserManagement.entity.UserRole;
import com.examserver.usermanagement.ExamServerUserManagement.helper.UserFoundException;
import com.examserver.usermanagement.ExamServerUserManagement.repository.RoleRepository;
import com.examserver.usermanagement.ExamServerUserManagement.repository.UserRepository;
import com.examserver.usermanagement.ExamServerUserManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userrepository;

    @Autowired
    private RoleRepository roleRepository;

    //creating user
     @Override
    public User createUser(User user, Set<UserRole> userRole) throws Exception {

        User local=this.userrepository.findByEmail(user.getEmail());
        if(local!=null)
        {
            System.out.println("User is already there!!");
            throw new UserFoundException("User is already there!!");
        }
        else {
            //user create
            for(UserRole ur:userRole)
            {
                roleRepository.save(ur.getRole()); //save roles
            }

            user.getUserRoles().addAll(userRole);  //assign all roles to user
            local = this.userrepository.save(user);

        }
        return local;
    }

    //getting user by username
    @Override
    public User getUser(String email) {
        return this.userrepository.findByEmail(email);
    }

    @Override
    public void deleteUser(Long userId) {
        this.userrepository.deleteById(userId);
    }
}
