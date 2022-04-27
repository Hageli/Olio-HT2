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
import com.example.olio_ht.otherClasses.Review;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReviewActivity extends AppCompat {
    Context context;
    String line;
    ListView listView;
    Button returnButton;
    ArrayList<Review> memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        listView = (ListView) findViewById(R.id.listView);
        returnButton = (Button) findViewById(R.id.returnButton);
        memory = reviewReadCSV();
        if(memory.size() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.emptylist), Toast.LENGTH_SHORT).show();
        }
        listView.setAdapter(new ArrayAdapterReview(this, memory));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String reviewMoviename = memory.get(position).getMoviename();
                String reviewStars = memory.get(position).getStars();
                String reviewComment = memory.get(position).getComment();
                detailsMove(reviewMoviename, reviewStars, reviewComment);
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnMove();
            }
        });
    }

    /* Moves to ArchiveActivity */
    private void returnMove() {
        Intent intent = new Intent(this, ArchiveActivity.class);
        startActivity(intent);
    }

    /* Moves to ReviewDetailsActivity, passes the necessary values from the selected movie to the next activity */
    private void detailsMove(String name, String stars, String comment) {
        Intent intent = new Intent(this, ReviewDetailsActivity.class);
        intent.putExtra("reviewMoviename", name);
        intent.putExtra("reviewStars", stars);
        intent.putExtra("reviewComment", comment);
        startActivity(intent);
    }

    /* Reads contents of ht_reviewed.csv and creates an arraylist, this arraylist is returned and used in the listview */
    private ArrayList<Review> reviewReadCSV() {
        ArrayList<Review> reviewAL = new ArrayList<>();
        context = this;
        try {
            InputStream is = context.openFileInput("ht_reviewed.csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reviewFileRead = new BufferedReader(isr);
            while((line = reviewFileRead.readLine()) != null) {
                /* Ignoring the first line */
                if(!line.equals("moviename;stars;comment")) {
                    String[] tokens = line.split(";");
                    Review review = new Review(tokens[0],tokens[1],tokens[2]);
                    reviewAL.add(review);
                }
            }
            is.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return reviewAL;
    }

    /* This is a custom arrayadapter because we want to use a custom list item in the adapter */
    class ArrayAdapterReview extends ArrayAdapter<Review> {

        ArrayAdapterReview(Context context, ArrayList<Review> list) {
            super(context, 0, list);

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String text = getItem(position).getMoviename();
            String text2 = getItem(position).getStars();
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.listview_item2, parent, false);
            /* Here we set the text for our custom list item */
            TextView nameText = row.findViewById(R.id.nameText);
            nameText.setText(text);
            TextView infoText = row.findViewById(R.id.starsText);
            infoText.setText(text2);
            return row;
        }
    }
}