package com.example.olio_ht.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.olio_ht.R;
import com.example.olio_ht.otherClasses.Theatre;
import com.example.olio_ht.otherClasses.AppCompat;
import com.example.olio_ht.otherClasses.MovieDataManager;
import com.example.olio_ht.otherClasses.MovieObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;


public class SearchActivity extends AppCompat implements View.OnClickListener{
    Button cancelButton, searchButton;
    Spinner theatreList;
    EditText dateEdit, nameEdit;
    ArrayList<Theatre> theatreAL = new ArrayList<>();
    String id, url, timeTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        theatreList = (Spinner) findViewById(R.id.theatreList);
        dateEdit = (EditText) findViewById(R.id.dayEdit);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        searchButton = (Button) findViewById(R.id.searchButton);
        cancelButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        /* Adding the theatres to arraylist */
        theatreAL.add(new Theatre("ESPOO: OMENA", "1039"));
        theatreAL.add(new Theatre("ESPOO: SELLO", "1038"));
        theatreAL.add(new Theatre("HELSINKI: ITIS", "1045"));
        theatreAL.add(new Theatre("HELSINKI: KINOPALATSI", "1031"));
        theatreAL.add(new Theatre("HELSINKI: MAXIM","1032"));
        theatreAL.add(new Theatre("HELSINKI: TENNISPALATSI", "1033"));
        theatreAL.add(new Theatre("VANTAA: FLAMINGO", "1013"));
        theatreAL.add(new Theatre("JYVÄSKYLÄ: FANTASIA", "1015"));
        theatreAL.add(new Theatre("KUOPIO: SCALA", "1016"));
        theatreAL.add(new Theatre("LAHTI: KUVAPALATSI", "1017"));
        theatreAL.add(new Theatre("LAPPEENRANTA: STRAND", "1041"));
        theatreAL.add(new Theatre("OULU: PLAZA", "1018"));
        theatreAL.add(new Theatre("PORI: PROMENADI", "1019"));
        theatreAL.add(new Theatre("TAMPERE: CINE ATLAS", "1034"));
        theatreAL.add(new Theatre("TAMPERE: PLEVNA", "1035"));
        theatreAL.add(new Theatre("TURKU: KINOPALATSI", "1022"));
        theatreAL.add(new Theatre("RAISIO: LUXE MYLLY", "1046"));
        ArrayAdapter<Theatre> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, theatreAL);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        theatreList.setAdapter(adapter);
        theatreList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id = theatreAL.get(i).getTheatreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                id = theatreAL.get(0).getTheatreID();
            }
        });
    }

    /* We do this instead of making different onClickListeners for each button */
    @Override
    public void onClick(View view) {
        LocalDateTime dtm;
        switch (view.getId()) {
            case R.id.cancelButton:
                returnMove();
                break;
            case R.id.searchButton:
                try {
                    DateTimeFormatter format = new DateTimeFormatterBuilder()
                            .appendPattern("d.M.yyyy")
                            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                            .toFormatter();
                    timeTemp = dateEdit.getText().toString();
                    /* If the user doesn't input anything, we use today as the default value */
                    if (timeTemp.length() == 0) {
                        dtm = LocalDateTime.now();
                    } else {
                        dtm = LocalDateTime.parse(timeTemp, format);
                    }
                } catch (DateTimeParseException e) {
                    dtm = LocalDateTime.now();
                }
                String nameFilter = nameEdit.getText().toString();
                /* Here we create the url where we get the moviedata from,, adding 0 to the date if needed */
                if(dtm.getDayOfMonth() < 10 && dtm.getMonthValue() < 10) {
                    url = "https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=0" + dtm.getDayOfMonth() + ".0" + dtm.getMonthValue() + "." + dtm.getYear();
                } else if(dtm.getDayOfMonth() < 10 && dtm.getMonthValue() > 10) {
                    url = "https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=0" + dtm.getDayOfMonth() + "." + dtm.getMonthValue() + "." + dtm.getYear();
                } else if(dtm.getDayOfMonth() > 10 && dtm.getMonthValue() < 10) {
                    url = "https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=" + dtm.getDayOfMonth() + ".0" + dtm.getMonthValue() + "." + dtm.getYear();
                } else {
                    url = "https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=" + dtm.getDayOfMonth() + "." + dtm.getMonthValue() + "." + dtm.getYear();
                }
                MovieDataManager mdm = MovieDataManager.getInstance();
                ArrayList<MovieObject> searchMovies = mdm.getMovies(nameFilter, url);
                resultMove(searchMovies);
            default:
                break;
        }
    }

    /* Moves to MainActivity */
    private void returnMove() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /* Moves to ResultsActivity, passes the list of movies to the next activity */
    private void resultMove(ArrayList<MovieObject> allMovies) {
        Intent intent = new Intent(this, ResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movielist", allMovies);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}