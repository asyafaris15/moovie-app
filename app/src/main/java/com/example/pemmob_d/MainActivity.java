package com.example.pemmob_d;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText user_Email, user_Password;
    private Button login, show, register;
    private FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        user_Email = findViewById(R.id.input_TextEmail);
        user_Password = findViewById(R.id.input_TextPassword);
        login = findViewById(R.id.button_Login);
        show = findViewById(R.id.button_Show);
        register = findViewById(R.id.button_Regis);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(this);
        show.setOnClickListener(this);
        register.setOnClickListener(this);

        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, ListMovi.class);
            startActivity(intent);
            finish();
        }



    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_Login:
                login(user_Email.getText().toString(),user_Password.getText().toString());
                break;
            case R.id.button_Show:
                showPassword(true);
                break;
            case R.id.button_Regis:
                Intent intent = new Intent(MainActivity.this, UserCreate.class);
                startActivity(intent);
                break;
        }
    }
    public void login(String user_Email, String user_Password){
        if(!validateForm()){
            return;
        };
        mAuth.signInWithEmailAndPassword(user_Email, user_Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ListMovi.class);
                    startActivity(intent);
                    finish();
                    //updateUI(user);
                } else {
                    // If sign in fails, display a messageto the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(user_Email.getText().toString())) {
            user_Email.setError("Required");
            result = false;
        } else {
            user_Email.setError(null);
        }
        if (TextUtils.isEmpty(user_Password.getText().toString())) {
            user_Password.setError("Required");
            result = false;
        } else {
            user_Password.setError(null);
        }
        return result;
    }

    public void showPassword(boolean i){
        if(i = true){
            if(show.getText().toString().equals("Show")){
                show.setText("Hide");
                user_Password.setTransformationMethod(null);
            } else {
                show.setText("Show");
                user_Password.setTransformationMethod(new PasswordTransformationMethod());
            }
        }
    }
}