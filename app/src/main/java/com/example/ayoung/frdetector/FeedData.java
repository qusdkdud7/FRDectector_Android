package com.example.ayoung.frdetector;

/**
 * Created by AYoung on 2017-06-10.
 */
public class FeedData {
    String pName;
    String message;
    String time;

    public FeedData() {
    }

    public FeedData(String pName, String message, String time) {
        this.pName = pName;
        this.message = message;
        this.time = time;
    }
}
