package com.example.olio_ht;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {
    private Context context;
    private SharedPreferences sharedPreferences;
    private static LanguageHelper lh = null;

    /* Singleton */
    private LanguageHelper() {}

    public static LanguageHelper getInstance() {
        if(lh == null) {
            lh = new LanguageHelper();
        }
        return lh;
    }

    /* Following 2 methods are used to change the language of the application, input langCode is the language we want to change to */
    public void updateLanguage(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        setLanguage(langCode);
    }

    private void setLanguage(String langCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang", langCode);
        editor.commit();
    }

    public void setContext(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
    }

    public String getLanguage() { return sharedPreferences.getString("lang", "en"); }
}
