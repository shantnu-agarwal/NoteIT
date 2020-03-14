/*
Written By: MSPCSRM
Find us on Instagram: @mspcsrm
Follow us for the fastest updates on our events.
This activity can:
1. Add a new note
2. Open an existing note and allow it to be edited.
 */

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
    private int noteID; //keeps track of the ID of the note which is being edited/created.

    boolean is_it_new;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Log.d(TAG,"User has successfully reached the Create Note Screen");
        noteID = getIntent().getExtras().getInt("NotesID"); //Read the value of the KEY: NotesID, this was sent as a part of the intent msg from the NotesList Activity.
        Log.d(TAG,"Looking up contents of Note Number: " + noteID);
        loadInfo(noteID); //custom function, will get the details of the Note using the ID that was sent in the Intent msg.


    }
    @Override
    protected void onStop(){            //ANDROID OS WILL CALL THIS FUNCTION WHEN THE ACTIVITY IS CLOSING. PURPOSE: AUTO SAVE FUNCTIONALITY.
        super.onStop();
        dbRef = FirebaseDatabase.getInstance().getReference();

        Log.d(TAG,"STOPPING ACTIVITY NOW, TIME TO SAVE THE NOTES TO THE cloud");
        EditText et = findViewById(1);      //Out of the two edit text field that we have on this activity, ID=1 is for heading of the note (defined in Line 87)
        String heading = et.getText().toString();      //Read the text that the user might have entered in this field.
        Log.d(TAG,"NEW Heading = \t" + heading);
        et = findViewById(2);                  //Out of the two edit text field that we have on this activity, ID=2 is for body of the note  (defined in Line 88)
        String body = et.getText().toString();      //Read the text that the user might have entered in this field.
        if(heading.length()==0 || body.length()==0){        //If the body or the heading is empty, then we will ignore this note (and thus, it will not be saved into the DB). In other words, the notes needs to have both heading and the body, only then it can be saved.
            Log.d(TAG,"Discarding the note, it appears to be empty AF");
            return;
        }

        Log.d(TAG,"NEW data = \t" + body);
        dbRef.child("Notes").child(noteID + "").child("Heading").setValue(heading);     //save the data on the tree
        dbRef.child("Notes").child(noteID + "").child("Data").setValue(body);       //save the data on the tree
        if(is_it_new==true)
            dbRef.child("Notes").child("Total").setValue(noteID);
    }

    protected void loadInfo(final int noteID){
        dbRef = FirebaseDatabase.getInstance().getReference();
        final String noteid = noteID + "";
        linearLayout = findViewById(R.id.linearLayout);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                Context context = getApplicationContext();
                EditText et_body = new EditText(context);   //On this activity, the user should be able to edit the text that has been opened so we are using the one EditText object for note heading and one for note body.
                EditText et_heading = new EditText(context);
                Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                        Toolbar.LayoutParams.MATCH_PARENT,
                        Toolbar.LayoutParams.MATCH_PARENT
                );
                et_heading.setId(1);        //the ID for the heading text field is 1. This ID will be used when we need to save the data, because at that time we will need a unique identifier to find this text field.
                et_body.setId(2);            //the ID for the body of the note text field is 2. This ID will be used when we need to save the data, because at that time we will need a unique identifier to find this text field.

                et_heading.setHint("Heading");      //Hint is similar to the Placeholder in HTML
                et_body.setHint("Write your note here"); //Hint is similar to the Placeholder in HTML

                et_heading.setLayoutParams(params);
                et_heading.setGravity(Gravity.START); //Tells where to start writing from, left side/right side/center. It is used for arabic languages. "START" property means that it will use the Device's settings. (thus making it helpful for all languages)

                et_body.setLayoutParams(params);
                et_body.setGravity(Gravity.START);
                String noteHeading;
                String noteText;
                try{
//                    This will try to get the data from the cloud, it will be a success if the noteid already exists.
                    noteHeading = dataSnapshot.child("Notes").child(noteid).child("Heading").getValue().toString();
                    noteText = dataSnapshot.child("Notes").child(noteid).child("Data").getValue().toString();
                    et_heading.setText(noteHeading);
                    et_body.setText(noteText);
                    is_it_new=false; //FALSE means that the note is not new
                }
                catch (Exception e){
//                    however, if the noteid is new, it will have to make a new note and thus show empty EditText fields.
                    noteHeading = "";
                    noteText = "";
                    is_it_new = true;
                    Log.d(TAG,"APPEARS TO BE A NEW NOTE, TRYING TO SET THINGS UP.");
                }

                Log.d(TAG,"HEADING:\t" + noteHeading);
                Log.d(TAG,"BODY:\t" + noteText);
                linearLayout.addView(et_heading);   //add the heading's text field to the layout
                linearLayout.addView(et_body);      //add the body's text field to the layout

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {//This function declaration is required, but its content can be blank.

            }
        });

    }
}
