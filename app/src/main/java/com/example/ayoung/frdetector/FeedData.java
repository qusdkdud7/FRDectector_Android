package com.example.ayoung.frdetector;

/**
 * Created by AYoung on 2017-06-10.
 */
public class FeedData {
    String pName;
    String message;
    long time;

    public FeedData() {
    }

    public FeedData(String pName, String message) {
        this.pName = pName;
        this.message = message;
    }
}
