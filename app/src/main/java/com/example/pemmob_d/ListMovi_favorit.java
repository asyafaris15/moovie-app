package com.example.pemmob_d;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListMovi_favorit extends AppCompatActivity implements View.OnClickListener {
    private TextView main_title;
    private ImageView set_user;
    private RecyclerView recyclerView;
    private Button favorit, semua_list;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ArrayList<NoteMovi> listmovi = new ArrayList<>();
    private ArrayList<NoteStatus> listid = new ArrayList<>();
    private MoviAdaptrWtp moviAdaptrWtp;
    private String users;
    private ChildEventListener childEventListener;

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listmovi);

        recyclerView = findViewById(R.id.recycle_listMovi);
        recyclerView.setHasFixedSize(true);
        main_title = findViewById(R.id.title);
        set_user = findViewById(R.id.textView_Set);
        favorit = findViewById(R.id.button_favorit);
        semua_list = findViewById(R.id.button_wtp);
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        users = currentUser.getUid();

        set_user.setOnClickListener(v -> {
            Intent intent = new Intent(ListMovi_favorit.this, UserData.class);
            startActivity(intent);
        });

        favorit.setOnClickListener(v -> {
            Intent intent = new Intent(ListMovi_favorit.this, ListMovi_Wtp.class);
            startActivity(intent);
        });

        semua_list.setOnClickListener(view -> {
            Intent intent = new Intent(ListMovi_favorit.this, ListMovi.class);
            startActivity(intent);
        });
    }

    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userse = currentUser.getEmail();
        main_title.setText(userse);

        // Mengubah tulisan button favorit menjadi button semua movi
        if (semua_list.getText().toString().equals("favorite")) {
            semua_list.setText("Semua Movi");
        }

        moviAdaptrWtp = new MoviAdaptrWtp(listmovi, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(moviAdaptrWtp);

        // Hapus listener sebelumnya (jika ada)
        if (childEventListener != null) {
            databaseReference.child("Status").removeEventListener(childEventListener);
        }

        childEventListener = databaseReference.child("Status").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NoteStatus note = snapshot.getValue(NoteStatus.class);
                if (note != null && note.getUser_id() != null && note.getMovi_status() != null) {
                    if (note.getUser_id().equals(users) && note.getMovi_status().equals("Favorite")) {
                        databaseReference.child("Movi").child(note.getMovi_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                NoteMovi notemovi = snapshot.getValue(NoteMovi.class);
                                if (notemovi != null) {
                                    listmovi.add(notemovi);
                                    moviAdaptrWtp.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle onCancelled
                            }
                        });
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle onChildChanged
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle onChildRemoved
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle onChildMoved
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    protected void onStop() {
        super.onStop();

        // Hapus listener saat aktivitas berakhir
        if (childEventListener != null) {
            databaseReference.child("Status").removeEventListener(childEventListener);
        }
    }

    @Override
    public void onClick(View view) {
        // Implementasi onClick (jika diperlukan)
    }

    // Implementasikan metode lain yang diperlukan
}
