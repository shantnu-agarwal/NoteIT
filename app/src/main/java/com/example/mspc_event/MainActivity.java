package com.example.mspc_event;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG,"Main Activity has been setup, welcome to the Noted - The Notes App.");

    }

    public void next(View view) {
        Log.d(TAG, "PRESSED THE BUTTON");
        Intent i = new Intent(MainActivity.this, NotesList.class);
        startActivity(i);
    }
}
