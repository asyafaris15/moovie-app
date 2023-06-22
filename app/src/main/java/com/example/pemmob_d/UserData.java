package com.example.pemmob_d;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserData extends AppCompatActivity implements View.OnClickListener {
    private TextView user_name, user_username, user_email, user_number, ptw, favorite;
    private ImageView hapusData, Ubah, logout, UbahPassword;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private List<NoteUser>  data;

    NoteUser note = new NoteUser();

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user);
        user_name = findViewById(R.id.textView_Name);
        user_username = findViewById(R.id.textView_Username);
        user_number = findViewById(R.id.textView_Number);
        user_email = findViewById(R.id.textView_Email);
        hapusData = findViewById(R.id.button_DeleteData);
        Ubah = findViewById(R.id.button_ChangeData);
        UbahPassword = findViewById(R.id.button_ChangePass);
        logout = findViewById(R.id.button_Logout);
        favorite = findViewById(R.id.favorite);
        ptw = findViewById(R.id.ptw);
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Ubah.setOnClickListener(this);
        UbahPassword.setOnClickListener(this);
        hapusData.setOnClickListener(this);
        logout.setOnClickListener(this);
        favorite.setOnClickListener(this);
        ptw.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_ChangeData:
                Intent intent = new Intent(UserData.this, UserUbahData.class);
                startActivity(intent);
                break;
            case R.id.button_ChangePass:
                Intent intent1 = new Intent(UserData.this, UserUbahPass.class);
                startActivity(intent1);
                break;
            case R.id.button_DeleteData:
                deletedata();
                break;
            case R.id.button_Logout:
                logout();
                break;
            case R.id.favorite:
                Intent intent2 = new Intent(UserData.this, ListMovi_favorit.class);
                startActivity(intent2);
                break;
            case R.id.ptw:
                Intent intent3 = new Intent(UserData.this, ListMovi_Wtp.class);
                startActivity(intent3);
                break;
        }
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String users = currentUser.getUid();
        String userse = currentUser.getEmail();

            databaseReference.child("Users").child(users).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    NoteUser user = snapshot.getValue(NoteUser.class);
                    if(user_email.getText().toString() == ""){
                        user_email.setText(userse);
                    }else {
//                      user_name.setText((CharSequence) snapshot.getValue(NoteUser.class));
                        user_name.setText(user.getUser_name());
                        user_email.setText(user.getUser_email());
                        user_username.setText(user.getUser_username());
                        user_number.setText(user.getUser_number());
                    }
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

    public void deletedata(){
        AlertDialog.Builder perhatian = new AlertDialog.Builder(this);
        perhatian.setTitle("Hapus User Anda ?");
        perhatian.setMessage("Pilih ya jika ingin menghapus data")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                        databaseReference.child("Users").child(User.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                User.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(UserData.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog perhatikan = perhatian.create();
        perhatikan.show();
    }

    public void logout(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            mAuth.signOut();
            Intent intent = new Intent(UserData.this, MainActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Belum Login", Toast.LENGTH_SHORT).show();
        }

    }

}


