package com.example.olio_ht.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.olio_ht.otherClasses.AppCompat;
import com.example.olio_ht.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimeZone;

public class MainActivity extends AppCompat implements View.OnClickListener{
    Button browse, buy, rate, settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createFiles();
        browse = (Button) findViewById(R.id.browse_movies);
        buy = (Button) findViewById(R.id.buy_history);
        rate = (Button) findViewById(R.id.ratings);
        settings = (Button) findViewById(R.id.settings);
        browse.setOnClickListener(this);
        buy.setOnClickListener(this);
        rate.setOnClickListener(this);
        settings.setOnClickListener(this);
        /* Setting the default timezone to correct one */
        TimeZone tzone = TimeZone.getTimeZone("Europe/Helsinki");
        TimeZone.setDefault(tzone);
    }

    /* We do this instead of making different onClickListeners for each button */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.browse_movies:
                searchMove();
                break;
            case R.id.buy_history:
                buyHistoryMove();
                break;
            case R.id.ratings:
                archiveMove();
                break;
            case R.id.settings:
                settingsMove();
                break;
            default:
                break;
        }

    }

    /* Moves to SettingsActivity */
    private void settingsMove() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /* Moves to SearchActivity */
    private void searchMove() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /* Moves to ArchiveActivity */
    private void archiveMove() {
        Intent intent = new Intent(this, ArchiveActivity.class);
        startActivity(intent);
    }

    /* Moves to BuyHistoryActivity */
    private void buyHistoryMove() {
        Intent intent = new Intent(this, BuyHistoryActivity.class);
        startActivity(intent);
    }

    /* This method creates the files used to store values inside this application if they don't exist yet */
    private void createFiles() {
        File archiveFile = new File(this.getFilesDir(), "ht_archive.csv");
        File buyHistoryFile = new File(this.getFilesDir(), "ht_buyhistory.csv");
        File reviewedFile = new File(this.getFilesDir(), "ht_reviewed.csv");
        try {
            if (!archiveFile.exists()) {
                FileWriter outputArchive = new FileWriter(archiveFile);
                outputArchive.write("moviename\n");
                outputArchive.close();
            }
            if (!buyHistoryFile.exists()) {
                FileWriter outputHistory = new FileWriter(buyHistoryFile);
                outputHistory.write("moviename;timebought\n");
                outputHistory.close();
            }
            if (!reviewedFile.exists()) {
                FileWriter outputReviewed = new FileWriter(reviewedFile);
                outputReviewed.write("moviename;stars;comment\n");
                outputReviewed.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}