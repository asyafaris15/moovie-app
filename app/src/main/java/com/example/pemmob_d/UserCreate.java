package com.example.pemmob_d;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserCreate extends AppCompatActivity implements View.OnClickListener {
    private EditText user_name, user_username, user_email, user_number, user_password;
    private Button register, login;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    NoteUser note;

    //@SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create);
        user_name = findViewById(R.id.input_TextName);
        user_username = findViewById(R.id.input_TextUsername);
        user_number = findViewById(R.id.input_TextNumber);
        user_email = findViewById(R.id.input_TextEmail);
        user_password = findViewById(R.id.input_TextPassword);
        register = findViewById(R.id.button_Regis);
        login = findViewById(R.id.button_Login);
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        register.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_Login:
                Intent intent = new Intent(UserCreate.this, MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.button_Regis:
//                RegisterAuth(user_email.getText().toString(),user_password.getText().toString());
                    RegisterAuth(
                            user_name.getText().toString(),
                            user_username.getText().toString(),
                            user_email.getText().toString(),
                            user_number.getText().toString(),
                            user_password.getText().toString()
                    );
                break;
        }
    }

    private void RegisterAuth(String user_name, String user_username, String user_email,String user_number, String user_password) {
        if(!validateForm()){
            return;
        }
        mAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG,"createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    Toast.makeText(UserCreate.this, user.toString(), Toast.LENGTH_SHORT).show();
                    Register(user_name,user_username,user_email,user_number);
                } else {
                    // If sign in fails, display a messageto the user.
                    Log.w(TAG,"createUserWithEmail:failure", task.getException());
                    Toast.makeText(UserCreate.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });
    }
    private void Register(String user_name, String user_username, String user_email, String user_number) {
        NoteUser baru = new NoteUser(user_name, user_username,user_email,user_number);
        databaseReference.child("Users").child(mAuth.getUid()).push().setValue(baru).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UserCreate.this, "Add data", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserCreate.this, ListMovi.class);
//                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserCreate.this, "Failed to Add data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(user_email.getText().toString())) {
            user_email.setError("Required");
            result = false;
        } else {
            user_email.setError(null);
        }
        if (TextUtils.isEmpty(user_password.getText().toString())) {
            user_password.setError("Required");
            result = false;
        } else {
            user_password.setError(null);
        }
        if (TextUtils.isEmpty(user_username.getText().toString())) {
            user_username.setError("Required");
            result = false;
        } else {
            user_username.setError(null);
        }
        if (TextUtils.isEmpty(user_name.getText().toString())) {
            user_name.setError("Required");
            result = false;
        } else {
            user_name.setError(null);
        }
        if (TextUtils.isEmpty(user_number.getText().toString())) {
            user_number.setError("Required");
            result = false;
        } else {
            user_number.setError(null);
        }
        return result;
    }
}


