package com.example.olio_ht;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MovieObject implements Serializable {
    private String movieNameFi;
    private String movieNameEn;
    private LocalDateTime movieDate;
    private String length;
    private String auditorium;

    public MovieObject() {}

    public String getLength() {
        return length;
    }

    public String getMovieNameFi() {
        return movieNameFi;
    }

    public String getMovieNameEn() { return movieNameEn; }

    public LocalDateTime getMovieDate() {
        return movieDate;
    }

    public String getAuditorium() {
        return auditorium;
    }
    public void setLength(String length) {
        this.length = length;
    }

    public void setAuditorium(String auditorium) {
        this.auditorium = auditorium;
    }

    public void setMovieNameFi(String movieNameFi) {
        this.movieNameFi = movieNameFi;
    }

    public void setMovieNameEn(String movieNameEn) {
        this.movieNameEn = movieNameEn;
    }

    public void setMovieDate(LocalDateTime movieDate) {
        this.movieDate = movieDate;
    }
}
