package com.yair.tonywidget.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yair.tonywidget.R;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    public static boolean bAutoUpdateOnStartUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bAutoUpdateOnStartUp = false;
    }
}
