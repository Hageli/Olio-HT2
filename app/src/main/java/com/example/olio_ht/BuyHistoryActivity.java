package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BuyHistoryActivity extends AppCompat {
    Context context;
    Button returnButton;
    ListView listView;
    ArrayList<PurchaseObject> buyHistoryList;
    String line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_history);
        returnButton = (Button) findViewById(R.id.returnButton);
        listView = (ListView) findViewById(R.id.listView);
        buyHistoryList = buyHistoryReadCSV();
        if(buyHistoryList.size() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.emptylist), Toast.LENGTH_SHORT).show();
        }
        listView.setAdapter(new ArrayAdapterBuyHistory(this, buyHistoryList));
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnMove();
            }
        });
    }

    /* Moves to MainActivity */
    private void returnMove() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /* Reads contents of ht_buyhistory.csv and creates an arraylist, this arraylist is returned and used in the listview */
    private ArrayList<PurchaseObject> buyHistoryReadCSV() {
        ArrayList<PurchaseObject> listTemp = new ArrayList<>();
        context = this;
        try {
            InputStream is = context.openFileInput("ht_buyhistory.csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reviewFileRead = new BufferedReader(isr);
            while((line = reviewFileRead.readLine()) != null) {
                /* Ignoring the first line */
                if(!line.equals("moviename;timebought")) {
                    String[] tokens = line.split(";");
                    PurchaseObject receipt = new PurchaseObject(tokens[0],tokens[1]);
                    listTemp.add(receipt);
                }
            }
            is.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return listTemp;
    }

    /* This is a custom arrayadapter because we want to use a custom list item in the adapter */
    class ArrayAdapterBuyHistory extends ArrayAdapter<PurchaseObject> {

        ArrayAdapterBuyHistory(Context context, ArrayList<PurchaseObject> list) {
            super(context, 0, list);

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.listview_item2, parent, false);
            String text = getItem(position).getMoviename();
            String text2 = getItem(position).getTimestamp();
            /* Here we set the text for our custom list item and make the icon inside our list item invisible */
            TextView nameText = row.findViewById(R.id.nameText);
            nameText.setText(text);
            TextView infoText = row.findViewById(R.id.starsText);
            infoText.setText(getString(R.string.buyTime) + " " + text2);
            ImageView image = row.findViewById(R.id.imageView);
            image.setVisibility(View.GONE);
            return row;
        }
    }
}