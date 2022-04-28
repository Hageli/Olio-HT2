package com.example.olio_ht;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class CommentActivity extends AppCompat implements View.OnClickListener{
    Button cancel, addComment;
    EditText commentEdit;
    SeekBar starBar;
    TextView starText, movieText;
    float starsFloat;
    String starsDisplay, comment;
    ProgressBar progressStars;
    int progress;
    Intent extras;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        cancel = (Button) findViewById(R.id.cancelButton);
        addComment = (Button) findViewById(R.id.addCommentButton);
        commentEdit = (EditText) findViewById(R.id.commentEdit);
        starBar = (SeekBar) findViewById(R.id.seekBar);
        starText = (TextView) findViewById(R.id.starsText);
        progressStars = (ProgressBar) findViewById(R.id.starsBar);
        movieText = (TextView) findViewById(R.id.movieNameText);
        cancel.setOnClickListener(this);
        addComment.setOnClickListener(this);
        extras = getIntent();
        if(extras.getExtras() != null) {
            movieText.setText(extras.getStringExtra("movieName"));
        }
        /* Setting the values we want for the seekbar */
        starBar.setMax(500);
        starBar.setProgress(0);
        progressStars.setProgress(0);
        starBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                /* Calculating values and setting the value into TextView to show user the amount of stars selected */
                starsFloat = (float) (i / 100.0);
                starsDisplay = String.format("%.1f", starsFloat);
                progress = i/5;
                starText.setText(starsDisplay);
                progressStars.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /* We do this instead of making different onClickListeners for each button */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelButton:
                returnMove();
                break;
            case R.id.addCommentButton:
                addComment();
                break;
            default:
                break;
        }
    }
    /* Moves to ArchiveActivity  */
    private void returnMove() {
        Intent intent = new Intent(this, ArchiveActivity.class);
        startActivity(intent);
    }

    /* Adds the review given by the user to ht_reviewed.csv file and moves to ReviewActivity (list of user reviews) */
    private void addComment() {
        context = this;
        try {
            OutputStreamWriter commentFileWrite = new OutputStreamWriter(context.openFileOutput("ht_reviewed.csv", Context.MODE_APPEND));
            /* Setting value if user doesn't input anything */
            if(starBar.getProgress() == 0) {
                starsDisplay = "0.0";
            }
            /* Same here */
            if(commentEdit.getText().toString().isEmpty()) {
                comment = "...";
            } else {
                comment = commentEdit.getText().toString();
            }
            commentFileWrite.write(extras.getStringExtra("movieName") + ";" +  starsDisplay + ";" + comment + "\n");
            commentFileWrite.close();
            Intent intent = new Intent(this, ReviewActivity.class);
            startActivity(intent);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

    }
}