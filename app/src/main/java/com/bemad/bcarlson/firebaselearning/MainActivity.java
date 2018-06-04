package com.bemad.bcarlson.firebaselearning;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    /**
     * Learning how to use Google Firebase: https://firebase.google.com
     * Following SimCoder YouTube Tutorial:
     *      Channel: https://www.youtube.com/channel/UCQ5xY26cw5Noh6poIE-VBog
     *      Episode 1: https://www.youtube.com/watch?v=9rNpfu187wg&list=PLxabZQCAe5fgLqi5im08UYz7R5nfBQYBs
     */
    private EditText mChildValueEditText;
    private Button mAddButton, mRemoveButton;
    private TextView mChildValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChildValueEditText = findViewById(R.id.childValueEditText);
        mAddButton = findViewById(R.id.addButton);
        mRemoveButton = findViewById(R.id.removeButton);
        mChildValueTextView = findViewById(R.id.childValueTextView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = database.getReference("ben0");

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String childValue = mChildValueEditText.getText().toString();
                mRef.setValue(childValue);
            }
        });

        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.removeValue();
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String childValue = String.valueOf(dataSnapshot.getValue());
                mChildValueTextView.setText(childValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //On case of error when writing to mRef
            }
        });
    }
}
