package com.examserver.service;


import com.examserver.entity.Question;
import com.examserver.entity.Quiz;

import java.util.Set;

public interface QuestionService {

    public Question addQuestion(Question question);

    public Question updateQuestion(Question question);

    public Set<Question> getQuestions();

    public Question getQuestion(Long questionId);

    public Set<Question> getQuestionsOfQuiz(Quiz quiz);

    public boolean deleteQuestion(Long quesId);

    public Question get(Long questionId);
}
