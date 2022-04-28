package com.example.olio_ht;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ArchiveActivity extends AppCompat implements View.OnClickListener{
    ListView listView;
    Button cancelButton, ratedButton;
    ArrayList<String> memory;
    String toComment,line;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        listView = (ListView) findViewById(R.id.listView);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        ratedButton = (Button) findViewById(R.id.ratedButton);
        memory = archiveReadCSV();
        if(memory.size() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.emptylist), Toast.LENGTH_SHORT).show();
        }
        listView.setAdapter(new ArrayAdapterArchive(this, memory));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                toComment = memory.get(position);
                commentMove(toComment);
            }
        });
        ratedButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    /* We do this instead of making different onClickListeners for each button */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ratedButton:
                reviewMove();
                break;
            case R.id.cancelButton:
                returnMove();
                break;
            default:
                break;
        }
    }

    /* Reads contents of ht_archive.csv and creates an arraylist, this arraylist is returned and used in the listview */
    private ArrayList<String> archiveReadCSV() {
        ArrayList<String> movieAL = new ArrayList<>();
        context = this;
        try {
            InputStream is = context.openFileInput("ht_archive.csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader archiveFileRead = new BufferedReader(isr);
            while((line = archiveFileRead.readLine()) != null) {
                /* ignoring the first line */
                if(!line.equals("moviename")) {
                    movieAL.add(line);
                }
            }
            is.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return movieAL;
    }

    /* Moves to MainActivity */
    private void returnMove() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /* Moves to CommentActivity, passes the name of the movie that the user wants to comment on to CommentActivity */
    private void commentMove(String movieName) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("movieName", movieName);
        startActivity(intent);
    }
    /* Moves to ReviewActivity */
    private void reviewMove() {
        Intent intent = new Intent(this, ReviewActivity.class);
        startActivity(intent);
    }

    /* This is a custom arrayadapter because we want to use a custom list item in the adapter */
    class ArrayAdapterArchive extends ArrayAdapter<String> {

        ArrayAdapterArchive(Context context, ArrayList<String> list) {
            super(context, 0, list);

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.listview_item1, parent, false);
            /* Here we set the text for our custom list item */
            TextView nameText = row.findViewById(R.id.nameText);
            String textTemp = getItem(position);
            nameText.setText(textTemp);
            return row;
        }
    }
}