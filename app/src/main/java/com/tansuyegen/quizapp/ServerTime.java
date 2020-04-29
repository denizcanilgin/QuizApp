package com.tansuyegen.quizapp;

public interface ServerTime {
    void onSuccess(long timestamp);

    void onFailed();
}


