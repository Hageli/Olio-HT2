package com.example.olio_ht.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olio_ht.R;
import com.example.olio_ht.otherClasses.AppCompat;
import com.example.olio_ht.otherClasses.LanguageHelper;
import com.example.olio_ht.otherClasses.MovieObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ResultsActivity extends AppCompat implements View.OnClickListener {
    Button returnButton, mainmenuButton;
    ListView movieList;
    ArrayList<MovieObject> resultsMovies;
    Context context;
    String line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent extras = getIntent();
        if(extras.getExtras() != null) {
            resultsMovies = (ArrayList<MovieObject>) extras.getSerializableExtra("movielist");
        }
        if(resultsMovies.size() == 0) {
            Toast.makeText(this, getString(R.string.emptylist), Toast.LENGTH_SHORT).show();
        }
        archiveUpdateRead(resultsMovies);
        returnButton = (Button) findViewById(R.id.returnButton);
        mainmenuButton = (Button) findViewById(R.id.mainmenuButton);
        returnButton.setOnClickListener(this);
        mainmenuButton.setOnClickListener(this);
        movieList = (ListView) findViewById(R.id.listView);
        movieList.setAdapter(new ArrayAdapterResults(this, resultsMovies));
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* If the user has finnish language settings active, we use the finnish movie name */
                if(LanguageHelper.getInstance().getLanguage().equals("fi")) {
                    detailsMove(resultsMovies.get(i).getMovieNameFi(), resultsMovies.get(i).getMovieDate(), resultsMovies.get(i).getLength(), resultsMovies.get(i).getSpokenLanguage(), resultsMovies.get(i).getAuditorium(), resultsMovies);
                } else {
                    detailsMove(resultsMovies.get(i).getMovieNameEn(), resultsMovies.get(i).getMovieDate(), resultsMovies.get(i).getLength(), resultsMovies.get(i).getSpokenLanguage(), resultsMovies.get(i).getAuditorium(), resultsMovies);
                }
            }
        });
    }

    /* We do this instead of making different onClickListeners for each button */
    @Override
    public void onClick(View view) {
            switch (view.getId()) {
                case R.id.returnButton:
                    returnMove();
                    break;
                case R.id.mainmenuButton:
                    mainMenuMove();
                    break;
                default:
                    break;
            }
    }

    /* Moves to SearchActivity */
    private void returnMove() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /* Moves to MainActivity */
    private void mainMenuMove() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /* Moves to ResultsDetailsActivity, details from selected movie are passed to next activity, also the movielist is passed to avoid loading the same data multiple times */
    private void detailsMove(String moviename, LocalDateTime movieDate, String length, String language, String auditorium, ArrayList<MovieObject> resultsMovies) {
        Intent intent = new Intent(this, ResultsDetailsActivity.class);
        intent.putExtra("moviename", moviename);
        intent.putExtra("moviedate", movieDate);
        intent.putExtra("length", length);
        intent.putExtra("language", language);
        intent.putExtra("auditorium", auditorium);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movielist", resultsMovies);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /* This method checks if some of the searched movies aren't listed in ht_archive.csv and adds them if necessary */
    private void archiveUpdateRead(ArrayList<MovieObject> resultsMovies) {
        ArrayList<String> archiveMovies = new ArrayList<>();
        ArrayList<String> appendStrings = new ArrayList<>();
        context = this;
        try {
            /* Getting all movies from the archive to an arraylist */
            InputStream is = context.openFileInput("ht_archive.csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reviewFileRead = new BufferedReader(isr);
            while((line = reviewFileRead.readLine()) != null) {
                archiveMovies.add(line);
            }
            /* Here we check if the archive contains the searched movies. If not, the moviename is added to a separate arraylist that is later passed to a method that writes the strings to the archive */
            for(int i = 0; i < resultsMovies.size(); i++) {
                if(!archiveMovies.contains(resultsMovies.get(i).getMovieNameEn())) {
                    appendStrings.add(resultsMovies.get(i).getMovieNameEn());
                }
            }
            is.close();
            archiveUpdateWrite(appendStrings);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    /* This method writes the input strings to the archive */
    private void archiveUpdateWrite(ArrayList<String> appendStrings) {
        context = this;
        try {
            OutputStreamWriter archiveFileWrite = new OutputStreamWriter(this.openFileOutput("ht_archive.csv", Context.MODE_APPEND));
            for(int i = 0; i < appendStrings.size(); i++) {
                archiveFileWrite.write(appendStrings.get(i) + "\n");
            }
            archiveFileWrite.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

    }

    /* This is a custom arrayadapter because we want to use a custom list item in the adapter */
    class ArrayAdapterResults extends ArrayAdapter<MovieObject> {

        ArrayAdapterResults(Context context, ArrayList<MovieObject> list) {
            super(context, 0, list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String resultsLanguage = LanguageHelper.getInstance().getLanguage();
            String nameString, timeString;
            DateTimeFormatter format = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
            if(resultsLanguage.equals("fi")) {
                nameString = getItem(position).getMovieNameFi();
            } else {
                nameString = getItem(position).getMovieNameEn();
            }
            timeString = getItem(position).getMovieDate().format(format);
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.listview_item2, parent, false);
            /* Here we set the text for our custom list item */
            TextView nameText = row.findViewById(R.id.nameText);
            nameText.setText(nameString);
            nameText.setTextSize(18);
            TextView timeText = row.findViewById(R.id.starsText);
            timeText.setText(timeString);
            timeText.setTextSize(14);
            return row;
        }
    }
}
