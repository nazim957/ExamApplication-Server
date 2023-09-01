package com.examserver.controller;


import com.examserver.entity.user.JwtRequest;
import com.examserver.entity.user.JwtResponse;
import com.examserver.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private RestTemplate restTemplate;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    private ResponseEntity<?> validateToken(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

//        return restTemplate.exchange(
//                "http://localhost:8080/validate",
//                HttpMethod.GET,
//                entity,
//                Void.class
        return restTemplate.exchange(
                "http://localhost:8080/validate",
                HttpMethod.GET,
                entity,
                Void.class
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) throws Exception {
//        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(
//                "http://localhost:8080/generate-token", jwtRequest, JwtResponse.class);
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(
                "http://localhost:8080/generate-token", jwtRequest, JwtResponse.class);

        JwtResponse jwtResponse = response.getBody();
      //jwtTokenService.setJwtToken(jwtResponse.getToken());
        logger.info("JWT TOKEN: {}", jwtResponse.getToken());
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){

      //  ResponseEntity<User> userResponseEntity = restTemplate.postForEntity("http://localhost:8080/user/", user, User.class);
        ResponseEntity<User> userResponseEntity = restTemplate.postForEntity("http://localhost:8080/user/", user, User.class);
        HttpStatus statusCode = userResponseEntity.getStatusCode();
        logger.info("Status Code: {}",statusCode);
        User user1 = userResponseEntity.getBody();
        if(statusCode.equals(HttpStatus.CREATED)) {
            logger.info("User Registered Successfully");
            return ResponseEntity.ok(user1);
        }
        else{
            logger.info("Error in Registering User");
            return new ResponseEntity<String>("Error in Registering User Please Try Again",HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String jwtToken) {
        ResponseEntity<?> forEntity = validateToken(jwtToken);

        if (forEntity.getStatusCode().equals(HttpStatus.OK)) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwtToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {};
//            ResponseEntity<Map<String, Object>> userEntity = restTemplate.exchange(
//                    "http://localhost:8080/current-user",
//                    HttpMethod.GET,
//                    entity,
//                    responseType
//            );
            ResponseEntity<Map<String, Object>> userEntity = restTemplate.exchange(
                    "http://localhost:8080/current-user",
                    HttpMethod.GET,
                    entity,
                    responseType
            );

            logger.info("USER: {}", userEntity.getBody());
            return ResponseEntity.ok(userEntity.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
        }
    }

}
