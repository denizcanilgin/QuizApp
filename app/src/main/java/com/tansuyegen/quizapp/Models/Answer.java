package com.tansuyegen.quizapp.Models;

public class Answer {

    String letter;
    String desc;
    Boolean isCorrectAnswer;
    Boolean isSelectedAnswer;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getIsCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setIsCorrectAnswer(Boolean isCorrectAnswer) {
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public Boolean getSelectedAnswer() {
        return isSelectedAnswer;
    }

    public void setSelectedAnswer(Boolean selectedAnswer) {
        isSelectedAnswer = selectedAnswer;
    }

    public Answer(String letter, String desc, Boolean isCorrectAnswer, Boolean isSelectedAnswer) {
        this.letter = letter;
        this.desc = desc;
        this.isCorrectAnswer = isCorrectAnswer;
        this.isSelectedAnswer = isSelectedAnswer;
    }
}
