package com.example.ifetimerecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.fragment_container) != null) {
            if(savedInstanceState != null) {
                return;
            }
            //MainFragment
            MainFragment mainFragment = new MainFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainFragment, null).commit();

        }
    }
}