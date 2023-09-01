package com.examserver.service.impl;


import com.examserver.entity.Question;
import com.examserver.entity.Quiz;
import com.examserver.repository.QuestionRepository;
import com.examserver.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Question addQuestion(Question question) {
        return this.questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Question question) {
        return this.questionRepository.save(question);
    }

    @Override
    public Set<Question> getQuestions() {
        return new HashSet<>(this.questionRepository.findAll());
    }

    @Override
    public Question getQuestion(Long questionId) {
        return this.questionRepository.findById(questionId).get();
    }

    @Override
    public Set<Question> getQuestionsOfQuiz(Quiz quiz) {
        return this.questionRepository.findByQuiz(quiz);
    }

    @Override
    public boolean deleteQuestion(Long quesId) {

//        Question question = new Question();
//        question.setQuesId(quesId);
//        this.questionRepository.delete(question);
        Optional<Question> question = questionRepository.findById(quesId);
        if(question.isPresent()) {
            questionRepository.deleteById(quesId);
            return true;
        }
        else
            return false;
    }

    @Override
    public Question get(Long questionId) {
        return this.questionRepository.getOne(questionId);
    }
}
