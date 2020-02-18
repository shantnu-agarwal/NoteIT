package com.example.mspc_event;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddNote extends AppCompatActivity {


    private static final String TAG = AddNote.class.getSimpleName();
    private DatabaseReference dbRef;
    LinearLayout linearLayout;
    private int noteID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Log.d(TAG,"User has successfully reached the Create Note Screen");
        noteID = getIntent().getExtras().getInt("NotesID");
        Log.d(TAG,"Looking up contents of Note Number: " + noteID);
        loadInfo(noteID);
    }
    @Override
    protected void onStop(){
        super.onStop();
        dbRef = FirebaseDatabase.getInstance().getReference();

        Log.d(TAG,"STOPPING ACTIVITY NOW, TIME TO SAVE THE NOTES TO THE cloud");
        EditText et = findViewById(1);
        String heading = et.getText().toString();
        Log.d(TAG,"NEW Heading = \t" + heading);
        et = findViewById(2);
        String body = et.getText().toString();
        Log.d(TAG,"NEW data = \t" + body);

        dbRef.child("Notes").child(noteID + "").child("Heading").setValue(heading);
        dbRef.child("Notes").child(noteID + "").child("Data").setValue(body);

    }

    protected void loadInfo(int noteID){
        dbRef = FirebaseDatabase.getInstance().getReference();
        final String noteid = noteID + "";
        linearLayout = findViewById(R.id.linearLayout);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Context context = getApplicationContext();
                EditText et_body = new EditText(context);
                EditText et_heading = new EditText(context);
                Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                        Toolbar.LayoutParams.MATCH_PARENT,
                        Toolbar.LayoutParams.MATCH_PARENT
                );
                et_heading.setId(1);
                et_body.setId(2);

                et_heading.setLayoutParams(params);
                et_heading.setGravity(Gravity.LEFT);

                et_body.setLayoutParams(params);
                et_body.setGravity(Gravity.LEFT);

                String noteHeading = dataSnapshot.child("Notes").child(noteid).child("Heading").getValue().toString();
                String noteText = dataSnapshot.child("Notes").child(noteid).child("Data").getValue().toString();
                et_heading.setText(noteHeading);
                et_body.setText(noteText);
                Log.d(TAG,"HEADING:\t" + noteHeading);
                Log.d(TAG,"BODY:\t" + noteText);
                linearLayout.addView(et_heading);
                linearLayout.addView(et_body);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
