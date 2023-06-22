package com.example.pemmob_d;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserUbahPass extends AppCompatActivity implements View.OnClickListener {
    private EditText user_passLama, user_passBaru1, user_passBaru2;
    private Button simpan;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ubahpassuser);
        user_passLama = findViewById(R.id.editView_PasswordLama);
        user_passBaru1 = findViewById(R.id.editText_PasswordBaru1);
        user_passBaru2 = findViewById(R.id.editText_PasswordBaru2);
        simpan = findViewById(R.id.button_Simpan);
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        simpan.setOnClickListener(v -> {
            simpanData();
        });

    }

    public void simpanData(){
        if(!validateForm()){
            return;
        }

        String passLama = user_passLama.getText().toString();
        String passBaru1 = user_passBaru1.getText().toString();
//        String email = user_email.getText().toString();
        String passBaru2 = user_passBaru2.getText().toString();
//        note = new NoteUser(name, username, email , number);

        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential cre = EmailAuthProvider.getCredential(User.getEmail(),passLama);
        if (passBaru1.equals(passBaru2)){
            User.reauthenticate(cre).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        User.updatePassword(passBaru1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(UserUbahPass.this, "Password Diganti", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserUbahPass.this, UserData.class);
                                startActivity(intent);
                            }
                        });
                    }else{
                        Log.d(TAG, "Erorr Auth");
                    }
                }
            });
        }else {
            Toast.makeText(UserUbahPass.this, "Password Baru Tidak Sama", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(user_passLama.getText().toString())) {
            user_passLama.setError("Required");
            result = false;
        } else {
            user_passLama.setError(null);
        }
        if (TextUtils.isEmpty(user_passBaru1.getText().toString())) {
            user_passBaru1.setError("Required");
            result = false;
        } else {
            user_passBaru1.setError(null);
        }
        if (TextUtils.isEmpty(user_passBaru2.getText().toString())) {
            user_passBaru2.setError("Required");
            result = false;
        } else {
            user_passBaru2.setError(null);
        }
        return result;
    }

    @Override
    public void onClick(View view) {

    }
}


