package com.example.olio_ht;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class SettingsActivity extends AppCompat implements View.OnClickListener{

    Button englishButton, finnishButton, cancelButton, emptyReviews, emptyBuyHistory;
    Context context = null;
    String header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        LanguageHelper lh = LanguageHelper.getInstance();
        lh.setContext(this);
        englishButton = (Button) findViewById(R.id.englishButton);
        finnishButton = (Button) findViewById(R.id.finnishButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        emptyReviews = (Button) findViewById(R.id.ratingEmpty);
        emptyBuyHistory = (Button) findViewById(R.id.buyHistoryEmpty);
        /* Here we highlight the text on a button based on the language selected */
        if(lh.getLanguage().equals("fi")) {
            finnishButton.setTextColor(getResources().getColor(R.color.gold));
            englishButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            finnishButton.setTextColor(getResources().getColor(R.color.white));
            englishButton.setTextColor(getResources().getColor(R.color.gold));
        }
        englishButton.setOnClickListener(this);
        finnishButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        emptyReviews.setOnClickListener(this);
        emptyBuyHistory.setOnClickListener(this);
    }

    /* We do this instead of making different onClickListeners for each button */
    @Override
    public void onClick(View view) {
        LanguageHelper lh = LanguageHelper.getInstance();
        lh.setContext(this);
        switch (view.getId()) {
            case R.id.englishButton:
                lh.updateLanguage("en");
                recreate();
                break;
            case R.id.finnishButton:
                lh.updateLanguage("fi");
                recreate();
                break;
            case R.id.cancelButton:
                returnMove();
                break;
            case R.id.ratingEmpty:
                clearRatings();
                break;
            case R.id.buyHistoryEmpty:
                clearBuyHistory();
                break;
            default:
                break;
        }
    }

    /* Moves to MainActivity */
    private void returnMove() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void clearBuyHistory() {
        try {
            context = this;
            OutputStreamWriter settingsFileClear1 = new OutputStreamWriter(this.openFileOutput("ht_buyhistory.csv", Context.MODE_PRIVATE));
            header = "moviename;timebought\n";
            settingsFileClear1.write(header);
            settingsFileClear1.close();
            Toast.makeText(getApplicationContext(), getString(R.string.message), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearRatings() {
        try {
            context = this;
            OutputStreamWriter settingsFileClear2 = new OutputStreamWriter(this.openFileOutput("ht_reviewed.csv", Context.MODE_PRIVATE));
            header = "moviename;stars;comment\n";
            settingsFileClear2.write(header);
            settingsFileClear2.close();
            Toast.makeText(getApplicationContext(),getString(R.string.message),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

}