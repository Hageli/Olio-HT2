package com.example.olio_ht;

public class Theatre {
    private String theatreName;
    private String theatreID;

    public Theatre(String theatreName, String theatreID) {
        this.theatreName = theatreName;
        this.theatreID = theatreID;
    }

    public String getTheatreName() { return theatreName; }

    public String getTheatreID() { return theatreID; }

    public void setTheatreID(String theatreID) {
        this.theatreID = theatreID;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    /*ArrayAdapter uses toString for the names, we want to have the names in the listing*/
    @Override
    public String toString() { return theatreName; }
}
