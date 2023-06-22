package com.example.pemmob_d;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListMovi extends AppCompatActivity implements View.OnClickListener {
    private TextView main_title;
    private ImageView set;
    private RecyclerView recyclerView;
    private Button favorit, wtp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ArrayList<NoteMovi> listmovi = new ArrayList<>();
    private MoviAdaptr moviAdaptr;


    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listmovi);

        recyclerView = findViewById(R.id.recycle_listMovi);
        recyclerView.setHasFixedSize(true);
        main_title = findViewById(R.id.title);
        set = findViewById(R.id.textView_Set);
        favorit = findViewById(R.id.button_favorit);
        wtp = findViewById(R.id.button_wtp);
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Movi");
        moviAdaptr = new MoviAdaptr(listmovi, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        moviAdaptr = new MoviAdaptr(listmovi,this);
        recyclerView.setAdapter(moviAdaptr);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NoteMovi note = dataSnapshot.getValue(NoteMovi.class);
                    listmovi.add(note);
                }
                moviAdaptr.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userEmail = currentUser.getEmail();
            main_title.setText(userEmail);
        }



//        simpan.setOnClickListener(this);
        set.setOnClickListener(v -> {
            Intent intent = new Intent(ListMovi.this, UserData.class);
            startActivity(intent);
        });

        favorit.setOnClickListener(view -> {
            Intent intent = new Intent(ListMovi.this, ListMovi_favorit.class);
            startActivity(intent);
        });

        wtp.setOnClickListener(view -> {
            Intent intent = new Intent(ListMovi.this, ListMovi_Wtp.class);
            startActivity(intent);
        });

    }

    private void tampilData(){
        databaseReference.child("Movi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listmovi.clear();
                for(DataSnapshot item : snapshot.getChildren()){
                    NoteMovi movi = item.getValue(NoteMovi.class);
                    listmovi.add(movi);
                }
                moviAdaptr.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

}


