package com.examserver.usermanagement.ExamServerUserManagement.controller;


import com.examserver.usermanagement.ExamServerUserManagement.entity.Role;
import com.examserver.usermanagement.ExamServerUserManagement.entity.User;
import com.examserver.usermanagement.ExamServerUserManagement.entity.UserRole;
import com.examserver.usermanagement.ExamServerUserManagement.helper.UserFoundException;
import com.examserver.usermanagement.ExamServerUserManagement.helper.UserNotFoundException;
import com.examserver.usermanagement.ExamServerUserManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //creating user
    @PostMapping("/")
    //data user me aaega
    public ResponseEntity<?> createUser(@RequestBody User user) throws Exception {

              user.setProfile("Normal.org");
              //encoding password with bcrypt password encoder
                user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

              Set<UserRole> roles=new HashSet<>();
              Role role=new Role();
              role.setRoleId(45L);
              role.setRoleName("NORMAL");

              UserRole userRole =new UserRole();
              userRole.setUser(user);
              userRole.setRole(role);
              roles.add(userRole);
        User user1 = this.userService.createUser(user, roles);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    @GetMapping("/{email}")
    public User getUser(@PathVariable("email") String email)
    {
        return this.userService.getUser(email);
    }

    //delete the user by id
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId)
    {
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK) ;
    }

    //update the user by email

   @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> exceptionHandler(UserNotFoundException ex){
        return ResponseEntity.ok(ex.getMessage());
    }

    @ExceptionHandler(UserFoundException.class)
    public ResponseEntity<?> exceptionHandler(UserFoundException ex){
        return ResponseEntity.ok(ex.getMessage());
    }

}
