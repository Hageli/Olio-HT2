package com.example.olio_ht.otherClasses;

import java.time.LocalDateTime;

public class PurchaseObject {
    private String moviename;
    private String timestamp;

    public PurchaseObject(String moviename, String timestamp) {
        this.moviename = moviename;
        this.timestamp = timestamp;
    }

    public String getMoviename() {
        return moviename;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
