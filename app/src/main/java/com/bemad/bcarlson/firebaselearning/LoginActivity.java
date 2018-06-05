package com.bemad.bcarlson.firebaselearning;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailLogin, mPasswordLogin,
            mEmailRegistration, mPasswordRegistration,
            mNameRegistration, mAgeRegistration, mSexRegistration,
            mUsernameRegistration;
    private Button mButtonLogin, mButtonRegistration;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailLogin = findViewById(R.id.emailLogin);
        mPasswordLogin = findViewById(R.id.passwordLogin);
        mEmailRegistration = findViewById(R.id.emailRegistration);
        mPasswordRegistration = findViewById(R.id.passwordRegistration);
        mUsernameRegistration = findViewById(R.id.usernameRegistration);

        mNameRegistration = findViewById(R.id.nameRegistration);
        mAgeRegistration = findViewById(R.id.ageRegistration);
        mSexRegistration = findViewById(R.id.sexRegistration);

        mButtonLogin = findViewById(R.id.buttonLogin);
        mButtonRegistration = findViewById(R.id.buttonRegistration);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mButtonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmailRegistration.getText().toString();
                final String password = mPasswordRegistration.getText().toString();
                final String username = mUsernameRegistration.getText().toString();

                Query usernameQuery = FirebaseDatabase
                                            .getInstance()
                                            .getReference()
                                            .child("Users")
                                            .orderByChild("username")
                                            .equalTo(username);
                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Toast.makeText(LoginActivity.this,
                                    "Username taken, please choose another", Toast.LENGTH_SHORT).show();
                        } else {
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                                    LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                String userID = mAuth.getCurrentUser().getUid();
                                                final DatabaseReference currentUserDB =
                                                        FirebaseDatabase
                                                                .getInstance()
                                                                .getReference()
                                                                .child("Users")
                                                                .child(userID);
                                                HashMap<String, String> newPost = new HashMap<>();

                                                final String name = mNameRegistration.getText().toString();
                                                final String age = mAgeRegistration.getText().toString();
                                                final String sex = mSexRegistration.getText().toString();

                                                newPost.put("username", username);
                                                newPost.put("name", name);
                                                newPost.put("age", age);
                                                newPost.put("sex", sex);

                                                currentUserDB.setValue(newPost);
                                            } else {
                                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                                Toast.makeText(LoginActivity.this,
                                                        "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                //message.hide();
                                            }
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailLogin.getText().toString();
                String password = mPasswordLogin.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this,
                                            "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
