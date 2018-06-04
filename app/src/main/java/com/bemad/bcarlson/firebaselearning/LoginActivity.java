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

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailLogin, mPasswordLogin,
            mEmailRegistration, mPasswordRegistration;
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
                String email = mEmailRegistration.getText().toString();
                String password = mPasswordRegistration.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                    Toast.makeText(LoginActivity.this,
                                            "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    //message.hide();
                                }
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
