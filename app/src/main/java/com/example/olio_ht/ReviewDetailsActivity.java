package com.example.olio_ht;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReviewDetailsActivity extends AppCompat {
    Button returnButton;
    TextView movienameTextView, starsTextView, commentTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);
        returnButton = (Button) findViewById(R.id.returnButton);
        movienameTextView = (TextView) findViewById(R.id.nameTextView);
        starsTextView = (TextView) findViewById(R.id.starsTextView);
        commentTextview = (TextView) findViewById(R.id.commentTextview);
        Intent detailsExtras = getIntent();
        if(detailsExtras.getExtras() != null) {
            movienameTextView.setText(detailsExtras.getStringExtra("reviewMoviename"));
            starsTextView.setText(detailsExtras.getStringExtra("reviewStars"));
            commentTextview.setText(detailsExtras.getStringExtra("reviewComment"));
        }
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnMove();
            }
        });
    }

    /*Moves to ReviewActivity */
    private void returnMove() {
        Intent intent = new Intent(this, ReviewActivity.class);
        startActivity(intent);
    }
}