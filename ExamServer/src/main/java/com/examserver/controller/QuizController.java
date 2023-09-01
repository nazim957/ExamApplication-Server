package com.examserver.controller;


import com.examserver.config.ResponseHandler;
import com.examserver.entity.Category;
import com.examserver.entity.Quiz;
import com.examserver.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private RestTemplate restTemplate;

    private ResponseEntity<?> validateToken(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

//        return restTemplate.exchange(
//                "http://localhost:8080/validate",
//                HttpMethod.GET,
//                entity,
//                Void.class
//        );
        return restTemplate.exchange(
                "https://usermanagementserver-j4xg.onrender.com/validate",
                HttpMethod.GET,
                entity,
                Void.class
        );
    }

    //add quiz service
    @PostMapping("/")
    public ResponseEntity<?> add(@RequestHeader("Authorization") String jwtToken, @RequestBody Quiz quiz){

        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.quizService.addQuiz(quiz));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //update quiz
    @PutMapping("/")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String jwtToken,@RequestBody Quiz quiz){
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.quizService.updateQuiz(quiz));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //get quiz
    @GetMapping("/")
    public ResponseEntity<?> quizzes(@RequestHeader("Authorization") String jwtToken){
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.quizService.getQuizzes());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //get single quiz
    @GetMapping("/{qid}")
    public ResponseEntity<?> quiz(@RequestHeader("Authorization") String jwtToken,@PathVariable("qid") Long qid){
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.quizService.getQuiz(qid));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //delete quiz
    @DeleteMapping("/{qid}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String jwtToken, @PathVariable("qid") Long qid){
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            boolean flag = this.quizService.deleteQuiz(qid);
            if(flag){
              //  return ResponseEntity.ok("Quiz Deleted Successfully");
                return ResponseHandler.generateResponse("Quiz deleted successfully",HttpStatus.OK);
            }
           else return ResponseHandler.generateResponse("Id Not Exists!!",HttpStatus.NOT_FOUND);
         //   else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id Not Exists");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    @GetMapping("/category/{cid}")
    public ResponseEntity<?> getQuizzesOfCategory(@RequestHeader("Authorization") String jwtToken,@PathVariable("cid") Long cid)
    {
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            Category category = new Category();
            category.setCid(cid);
            return ResponseEntity.ok(this.quizService.getQuizzesOfCategory(category));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //get active quizzes
    @GetMapping("/active")
    public ResponseEntity<?> getActiveQuizzes(@RequestHeader("Authorization") String jwtToken)
    {
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.quizService.getActiveQuizzes());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //get active quizzes of category
    @GetMapping("/category/active/{cid}")
    public ResponseEntity<?> getActiveQuizzes(@RequestHeader("Authorization") String jwtToken,@PathVariable("cid") Long cid)
    {
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            Category category = new Category();
            category.setCid(cid);
            return ResponseEntity.ok(this.quizService.getActiveQuizzesOfcategory(category));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }
}

