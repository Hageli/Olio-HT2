package com.example.olio_ht;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ResultsDetailsActivity extends AppCompat implements View.OnClickListener {
    TextView nameText, infoText;
    Button returnButton, buyButton;
    ArrayList<MovieObject> resultsDetailsMovies;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_details);
        nameText = (TextView) findViewById(R.id.nameTextView);
        infoText = (TextView) findViewById(R.id.timeTextView);
        returnButton = (Button) findViewById(R.id.returnButton);
        buyButton = (Button) findViewById(R.id.buyButton);
        returnButton.setOnClickListener(this);
        buyButton.setOnClickListener(this);
        Intent extras = getIntent();
        if(extras.getExtras() != null) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
            LocalDateTime timeTemp = (LocalDateTime) extras.getSerializableExtra("moviedate");
            nameText.setText(extras.getStringExtra("moviename"));
            String infoTemp = extras.getStringExtra("auditorium") + "\n" + timeTemp.format(format) + "\n" + getString(R.string.length) + " " + extras.getStringExtra("length") + "min";
            infoText.setText(infoTemp);
            resultsDetailsMovies = (ArrayList<MovieObject>) extras.getSerializableExtra("movielist");
        }
    }

    /* We do this instead of making different onClickListeners for each button */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buyButton:
                buyTicket(resultsDetailsMovies);
                break;
            case R.id.returnButton:
                returnMove(resultsDetailsMovies);
                break;
            default:
                break;
        }
    }

    /* Moves to ResultsActivity, passes the arraylist that has been temporarily stored in this activity back */
    private void returnMove(ArrayList<MovieObject> resultsDetailsMovies) {
        Intent intent = new Intent(this, ResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movielist", resultsDetailsMovies);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /* Writes the moviename of the movie the user decided to buy and the buytime to ht_buyhistory.csv. Moves back to ResultsActivity */
    private void buyTicket(ArrayList<MovieObject> resultsDetailsMovies) {
        context = this;
        try {
            OutputStreamWriter buyHistoryFileWrite = new OutputStreamWriter(context.openFileOutput("ht_buyhistory.csv", Context.MODE_APPEND));
            DateTimeFormatter format = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
            LocalDateTime timeTemp = LocalDateTime.now();
            buyHistoryFileWrite.write(getIntent().getStringExtra("moviename") + ";" + timeTemp.format(format) + "\n");
            buyHistoryFileWrite.close();
            Intent intent = new Intent(this, ResultsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("movielist", resultsDetailsMovies);
            intent.putExtras(bundle);
            startActivity(intent);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

        }
    }

}