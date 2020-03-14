/*
Written By: MSPCSRM
Find us on Instagram: @mspcsrm
Follow us for the fastest updates on our events.
 */

package com.example.mspc_event;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Constructor for the super class (Ref: Inheritence)
        setContentView(R.layout.activity_main); //Links the Java Class to a layout (XML).

        Log.i(TAG,"Main Activity has been setup, welcome to the Noted - The Notes App."); //Log message, printed in the logcat at the bottom left of android studio IDE.


    }

    public void next(View view) { //This function is called when the button is pressed, we have defined an "onclick" property on the button in the XML for this Activity.
        Log.d(TAG, "PRESSED THE BUTTON"); //Again, a Log message which will tell us that the button was pressed and this function execution has begun.
        Intent i = new Intent(MainActivity.this, NotesList.class);  //This Intent can take us to the NotesList Activity, we have declared the intent here, now we have to execute it in the next line.
        startActivity(i);   //execute the intent that we declared above. Control will now shift to the NotesList activity.
    }
}
