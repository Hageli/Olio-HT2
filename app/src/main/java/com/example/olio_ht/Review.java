package com.example.olio_ht;

public class Review {
    private String moviename;
    private String stars;
    private String comment;

    public Review(String moviename, String stars, String comment) {
        this.moviename = moviename;
        this.stars = stars;
        this.comment = comment;
    }

    public String getMoviename() {
        return moviename;
    }

    public String getStars() {
        return stars;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return moviename;
    }
}
