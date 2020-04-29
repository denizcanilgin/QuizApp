package com.tansuyegen.quizapp.Models;

public class Quiz {

    String title;
    String id;
    long startTimeInSec;
    long endTimeInSec;
    int numberOfQuestions;
    Boolean isActive;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Quiz(String title, String id, long startTimeInSec, long endTimeInSec, int numberOfQuestions, Boolean isActive) {
        this.title = title;
        this.startTimeInSec = startTimeInSec;
        this.endTimeInSec = endTimeInSec;
        this.isActive = isActive;
        this.numberOfQuestions = numberOfQuestions;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public long getStartTimeInSec() {
        return startTimeInSec;
    }

    public void setStartTimeInSec(long startTimeInSec) {
        this.startTimeInSec = startTimeInSec;
    }

    public long getEndTimeInSec() {
        return endTimeInSec;
    }

    public void setEndTimeInSec(long endTimeInSec) {
        this.endTimeInSec = endTimeInSec;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
