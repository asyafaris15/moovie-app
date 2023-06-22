package com.example.pemmob_d;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserUbahData extends AppCompatActivity implements View.OnClickListener {
    private EditText user_name, user_username, user_number;
    private TextView user_email;
    private Button simpan;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String key;

    NoteUser note;

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ubahdatauser);
        user_name = findViewById(R.id.EditText_Name);
        user_username = findViewById(R.id.EditText_Username);
        user_number = findViewById(R.id.EditText_Number);
        user_email = findViewById(R.id.textView_Email);
        simpan = findViewById(R.id.button_SimpanData);
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        simpan.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_SimpanData:
                simpanData();
                break;
        }
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String user_id = currentUser.getUid();
        String user_e = currentUser.getEmail();
        if (databaseReference.child("Users").child(user_id) == null){
            user_name.setText("");
            user_email.setText(user_e);
            user_username.setText("");
            user_number.setText("");
        }else {
            databaseReference.child("Users").child(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    NoteUser user = snapshot.getValue(NoteUser.class);
                    key = snapshot.getKey();
                    user_name.setText(user.getUser_name());
                    user_email.setText(user.getUser_email());
                    user_username.setText(user.getUser_username());
                    user_number.setText(user.getUser_number());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
    }
    public void simpanData(){
        if(!validateForm()){
            return;
        }

        String name = user_name.getText().toString();
        String username = user_username.getText().toString();
        String email = user_email.getText().toString();
        String number = user_number.getText().toString();

        //Toast.makeText(UserUbahData.this, key, Toast.LENGTH_SHORT).show();
        NoteUser note = new NoteUser(name, username, email , number);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String user = currentUser.getUid();
        if (databaseReference.child("Users").child(user) != null ){
                databaseReference.child("Users").child(user).child(key).setValue(note).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserUbahData.this,"Data Berhasil disimpan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserUbahData.this, UserData.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserUbahData.this,"Data Tidak Berhasil disimpan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserUbahData.this, UserData.class);
                        startActivity(intent);
                    }
                });
        }
    }

    private boolean validateForm() {
        boolean result = true;
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


