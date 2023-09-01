package com.examserver.controller;

import com.examserver.config.ResponseHandler;
import com.examserver.entity.Question;
import com.examserver.entity.Quiz;

import com.examserver.entity.user.AuthResponse;
import com.examserver.service.QuestionService;
import com.examserver.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private RestTemplate restTemplate;

    private ResponseEntity<?> validateToken(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://localhost:8080/validate",
                HttpMethod.GET,
                entity,
                Void.class
        );
    }

    //add question
    @PostMapping("/")
    public ResponseEntity<?> add(@RequestHeader("Authorization") String jwtToken,@RequestBody Question question){

        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.questionService.addQuestion(question));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //update question
    @PutMapping("/")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String jwtToken,@RequestBody Question question){

        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.questionService.updateQuestion(question));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //get  all question of any quiz
    @GetMapping("/quiz/{qid}")
    public ResponseEntity<?> getQuestionOfQuiz(@RequestHeader("Authorization") String jwtToken, @PathVariable("qid") Long qid){
        //it will return all questions
//        Quiz quiz = new Quiz();
//        quiz.setqId(qid);
//        Set<Question> questionsOfQuiz = this.questionService.getQuestionsOfQuiz(quiz);
//        return ResponseEntity.ok(questionsOfQuiz);

        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            Quiz quiz = this.quizService.getQuiz(qid);
            Set<Question> questions = quiz.getQuestions();
            List<Question> list = new ArrayList(questions);
            if (list.size() > Integer.parseInt(quiz.getNumberOfQuestions())) {
                list = list.subList(0, Integer.parseInt(quiz.getNumberOfQuestions() + 1));
            }

            list.forEach((q) -> {
                q.setAnswer("");
            });
            Collections.shuffle(list);
            return ResponseEntity.ok(list);
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");

    }

    @GetMapping("/quiz/all/{qid}")
    public ResponseEntity<?> getQuestionOfQuizAdmin(@RequestHeader("Authorization") String jwtToken,@PathVariable("qid") Long qid){

        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            //it will return all questions
            Quiz quiz = new Quiz();
            quiz.setQId(qid);
            Set<Question> questionsOfQuiz = this.questionService.getQuestionsOfQuiz(quiz);
            return ResponseEntity.ok(questionsOfQuiz);
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //get single question
    @GetMapping("/{quesId}")
    public ResponseEntity<?> get(@RequestHeader("Authorization") String jwtToken,@PathVariable("quesId") Long quesId)
    {

        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.questionService.getQuestion(quesId));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //delete question
    @DeleteMapping("/{quesId}")
    public ResponseEntity delete(@RequestHeader("Authorization") String jwtToken, @PathVariable("quesId") Long quesId){

        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
          //  System.out.println("INSIDE");
            boolean flag = this.questionService.deleteQuestion(quesId);
            if (flag) {
                return ResponseHandler.generateResponse("Question deleted successfully",HttpStatus.OK);
             //   return ResponseEntity.ok("Deleted Successfully");
            } else {
             //   System.out.println("ELSE DELETED BLOCK");
                return ResponseHandler.generateResponse("Id Not Exists!!",HttpStatus.NOT_FOUND);
              //  return new ResponseEntity<>("Id Not Exists!!",HttpStatus.NOT_FOUND);
            }
        }
//        System.out.println("*************");
//        System.out.println(forEntity.getBody());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(forEntity.getBody());

    }

    //eval quiz
    @PostMapping("/eval-quiz")
    public ResponseEntity<?> evalQuiz(@RequestHeader("Authorization") String jwtToken,@RequestBody List<Question> questions) {

        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if (forEntity.getStatusCode().equals(HttpStatus.OK)) {
            System.out.println(questions);
            double marksGot = 0;
            int correctAnswer = 0;
            int attempted = 0;
            for (Question q : questions) {
                //single questions
                Question question = this.questionService.get(q.getQuesId());
                if (question.getAnswer().equals((q.getGivenAnswer()))) {
                    //correct
                    correctAnswer++;

                    double marksSingle = Double.parseDouble(questions.get(0).getQuiz().getMaxMarks()) / questions.size();
                    marksGot += marksSingle;
                }

                if (q.getGivenAnswer() != null) {
                    attempted++;
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            //Map<String, Object> map =
            map.put("marksGot", marksGot);
            map.put("correctAnswers", correctAnswer);
            map.put("attempted", attempted);

            return ResponseEntity.ok(map);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }
}
