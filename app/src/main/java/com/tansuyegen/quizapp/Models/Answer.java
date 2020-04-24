package com.tansuyegen.quizapp.Models;

public class Answer {

    String letter;
    String desc;

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

    public Answer(String letter, String desc) {
        this.letter = letter;
        this.desc = desc;
    }
}
