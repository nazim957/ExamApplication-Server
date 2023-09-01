package com.examserver.usermanagement.ExamServerUserManagement.controller;

import com.examserver.usermanagement.ExamServerUserManagement.config.JwtHelper;
import com.examserver.usermanagement.ExamServerUserManagement.entity.AuthResponse;
import com.examserver.usermanagement.ExamServerUserManagement.entity.JwtRequest;
import com.examserver.usermanagement.ExamServerUserManagement.entity.JwtResponse;
import com.examserver.usermanagement.ExamServerUserManagement.entity.User;
import com.examserver.usermanagement.ExamServerUserManagement.helper.UserNotFoundException;
import com.examserver.usermanagement.ExamServerUserManagement.repository.UserRepository;
import com.examserver.usermanagement.ExamServerUserManagement.service.impl.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

@RestController
@CrossOrigin("*")
public class AuthenticateController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtHelper jwtUtils;

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateController.class);


    //generate token

    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {

        try {

            authenticate(jwtRequest.getEmail(), jwtRequest.getPassword());


        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new Exception("User not found ");
        }

        /////////////authenticate

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = this.jwtUtils.generateToken(userDetails);
        //System.out.println(token);
        return ResponseEntity.ok(new JwtResponse(token));


    }


    private void authenticate(String username, String password) throws Exception {

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (DisabledException e) {
            throw new Exception("USER DISABLED " + e.getMessage());
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid Credentials " + e.getMessage());
        }
    }

    //return the details of current user
    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        UserDetails userDetails = getUserDetailsFromToken(token);
        return ResponseEntity.ok((User) userDetails);
    }


    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7); // Removing "Bearer " prefix

            UserDetails userDetails = getUserDetailsFromToken(token);

            if (jwtUtils.validateToken(token, userDetails)) {
                AuthResponse res = new AuthResponse();
               // res.setUid(userDetails.getUsername());
                res.setValid(true);
               // res.setName(userRepository.findByEmail(userDetails.getUsername()).getEmail());
                return ResponseEntity.ok(true);
            } else {
                AuthResponse res = new AuthResponse();
                res.setValid(false);
                LOGGER.error("Token Has Expired or is Invalid");
                return ResponseEntity.ok(false);
            }
        } catch (Exception e) {
            LOGGER.error("Error validating token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private UserDetails getUserDetailsFromToken(String token) {
        String usernameFromToken = jwtUtils.getUsernameFromToken(token);
        return userDetailsService.loadUserByUsername(usernameFromToken);
    }

    @GetMapping("/expire")
    public ResponseEntity<Object> expToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Removing "Bearer " prefix

       // UserDetails userDetails = getUserDetailsFromToken(token);
        Date expirationDateFromToken = jwtUtils.getExpirationDateFromToken(token);
        return ResponseEntity.ok(expirationDateFromToken);
    }

}
