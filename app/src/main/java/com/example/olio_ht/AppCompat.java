package com.example.olio_ht;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/* This class is created to avoid writing this same code to every single activity*/
public class AppCompat extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageHelper lh = LanguageHelper.getInstance();
        lh.setContext(this);
        lh.updateLanguage(lh.getLanguage());
    }
}
