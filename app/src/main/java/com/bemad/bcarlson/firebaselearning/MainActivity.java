package com.bemad.bcarlson.firebaselearning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    /**
     * Learning how to use Google Firebase: https://firebase.google.com
     * Following SimCoder YouTube Tutorial:
     *      Channel: https://www.youtube.com/channel/UCQ5xY26cw5Noh6poIE-VBog
     *      Episode 1: https://www.youtube.com/watch?v=9rNpfu187wg&list=PLxabZQCAe5fgLqi5im08UYz7R5nfBQYBs
     */
    private EditText mChildValueEditText;
    private Button mAddButton, mRemoveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChildValueEditText = (EditText) findViewById(R.id.childValueEditText);
        mAddButton = (Button) findViewById(R.id.addButton);
        mRemoveButton = (Button) findViewById(R.id.removeButton);

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
    }
}
