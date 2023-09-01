package com.examserver.usermanagement.ExamServerUserManagement.service.impl;


import com.examserver.usermanagement.ExamServerUserManagement.entity.User;
import com.examserver.usermanagement.ExamServerUserManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if(user==null)
        {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("No user found!!");
        }
        return user;
    }
}
