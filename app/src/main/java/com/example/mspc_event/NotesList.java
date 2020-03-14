/*
Written By: MSPCSRM
Find us on Instagram: @mspcsrm
Follow us for the fastest updates on our events.
This is the activity where all the notes are being displayed after being fetched from the cloud. You can tap the notes to edit them or you can click the floating action button to make a new note.
 */

package com.example.mspc_event;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotesList extends AppCompatActivity {

    private static final String TAG = NotesList.class.getSimpleName();
    private DatabaseReference dbRef; //this object is out local reference to the online database.
    private Context context;    //context object, needed when making UI elements (like cards) dynamically.
    LinearLayout layout;    //an object to select and play with the layout through Java.
    static int total_notes=0; //counts the total number of notes that we have.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        showAllNotes(); //this function connects to online database (DB) and shows all the notes.


        FloatingActionButton fab = findViewById(R.id.fab);  //select the floating action button that is on the bottom right of the screen.

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NotesList.this,AddNote.class);    //Takes us to the same activity as where we would go when we want to edit the note. But since we sending a new ID in the next line, the text fields will be blank.
                Log.d(TAG,"User wants to create a new Note.");
                i.putExtra("NotesID",total_notes+1);        //total notes + 1 means that the note is being made is a new one. (because (total notes+1) will definitely not exist in our DB tree)
                startActivity(i);
            }
        });

    }
    protected void showAllNotes (){

        Log.d(TAG,"Attempting to show all the notes from the cloud");
        dbRef = FirebaseDatabase.getInstance().getReference(); //initialise a local reference to the online DB.
        context = getApplicationContext(); //built-in function provides value to the context object.

        dbRef.addValueEventListener(new ValueEventListener() { //This event listener will execute its body when there is any change in the VALUE of the dbRef object.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                layout = findViewById(R.id.linearlayout); //select the layout from the Interface. (In this project, the layout is LinearLayout).
                layout.removeAllViews(); //removes everything that might already be existing in the layout, to make space in the layout for all the notes.
                for(int i = 1;i <= Integer.parseInt(dataSnapshot.child("Notes").child("Total").getValue().toString()); i++){ //a FOR loop to traverse the entire notes tree structure and get ALL the notes in the DB.
                    total_notes=i;  //keeps a track of total number of notes that have been drawn on the UI.
                    String cnt = i + ""; //converts the INTEGER value of i to STRING, we are doing so because STRING is the datatype in the firebase DB everywhere. (explained this in the workshop as well)

                    String heading = dataSnapshot.child("Notes").child(cnt).child("Heading").getValue().toString();     //Gets the corresponding value for KEY: HEADING of the note which has ID = i
                    String data = dataSnapshot.child("Notes").child(cnt).child("Data").getValue().toString();           ///Get the corresponding value for KEY: Data of the note which has ID  = i


                    CardView card = new CardView(context);  //We are displaying one note in each card, so make a new Card object in each iteration of the FOR loop.
                    card.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(     //Makes the card follow the margins of the parent object in the UI (in our case: Linear Layout is the parent)
                            Toolbar.LayoutParams.MATCH_PARENT,
                            Toolbar.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins(10,00,10,20);  //margins of the card from the left, top, right and bottom respectively (in pixels).
                    params.height = 600;    //card height in pixels
                    card.setLayoutParams(params); //apply all the above defined UI parameters/properties to the card.
                    card.setRadius(20); //radius for rounded corners of the card.
                    card.setId(i); //store the ID of the note as the ID of the card, will make it very easy to tap and edit the card.
                    final int x = i;
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {            //This event listener will execute its code when the card is clicked/pressed/tapped.
                            Intent intent = new Intent(NotesList.this,AddNote.class);   //take us to the activity where we can edit this particular note.
                            intent.putExtra("NotesID",x);   //Like an email, we are attaching extra information with the intent. Allows us to put the ID of the note in the intent message. This will allow us to open THIS particular note in the next screen.
                            startActivity(intent);  //run the above defined intent, would also send the attached KEY:VALUE pair (NotesID) with the intent msg.
                        }
                    });


                    // Initialize a new TextView to put in CardView
                    TextView tv = new TextView(context);        //Create a non-user-editable text field where we can show the heading of the note.
                    params.setMargins(30,20,30,20); //apply UI properties, similar to what we did to the card above.
                    tv.setLayoutParams(params);
                    tv.setText(heading);    // Define the text that needs to be shown in this text view. "heading" object was defined above in line 78.
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);
                    tv.setTextColor(Color.BLUE);
                    tv.setSingleLine(); //If heading is very long, it will NOT come to the next line.

                    card.addView(tv); //add this text view element to the Card.
                    params.setMargins(30,150,30,0);
                    TextView tv2 = new TextView(context); //new text view, this time it is for the data/body of the note.
                    tv2.setLayoutParams(params);

                    tv2.setText(data); // Define the text that needs to be shown in this text view. "data" object was defined above in line 79.
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    tv2.setTextColor(Color.BLACK);
                    card.addView(tv2);  //add this text view element to the Card.

                    Typeface typeface = getResources().getFont(R.font.segoeui); //set the font to Segoe UI. REMOVE OR COMMENT THIS LINE IF YOU ARE USING ANYTHING BELOW ANDROID 7.

                    tv2.setTypeface(typeface);  //apply the font property to the body/data.
                    tv.setTypeface(typeface);   //apply the font property to the heading

                    layout.addView(card);   //finally, add the card (which already has two text fields) to the layout.

                    Log.d(TAG,heading);     //LOG messages help in debugging the app.
                    Log.d(TAG,data);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { //This function declaration is required, but its content can be blank.
            }
        });
    }

}
