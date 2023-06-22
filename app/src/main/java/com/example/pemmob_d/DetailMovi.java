package com.example.pemmob_d;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.CloseGuard;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailMovi extends AppCompatActivity  {
    TextView Judul, Thn, Desc, id;
    Button Simpan;
    String status_pilih;
    Spinner pilih;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_datamovi);
        Judul = findViewById(R.id.textView_JudulMovi);
        Thn = findViewById(R.id.textView_thnMovi);
        Desc = findViewById(R.id.textView_descMovi);
        Simpan = findViewById(R.id.button_simpan);
        id = findViewById(R.id.textView_id);
        pilih = findViewById(R.id.pilih);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.status,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pilih.setAdapter(adapter);
        pilih.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if( i > 0 ){
                    status_pilih = adapterView.getItemAtPosition(i).toString();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Simpan.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String userId = currentUser.getUid();
            String movieId = id.getText().toString();
            String status = status_pilih;
            NoteStatus statusNote = new NoteStatus(userId, movieId, status);

            databaseReference.child("Status")
                    .orderByChild("userId_moviId")
                    .equalTo(userId + "_" + movieId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(DetailMovi.this, "Data Sudah ada", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("Status").push().setValue(statusNote)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(DetailMovi.this, "Add data", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(DetailMovi.this, ListMovi.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(DetailMovi.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DetailMovi.this, "Failed to Add data", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String dataJudul = bundle.getString("dataJudul");
            String dataThn = bundle.getString("dataThn");
            String dataDesc = bundle.getString("dataDesc");
            String dataId = bundle.getString("dataId");
            id.setText(dataId);
            Judul.setText(dataJudul);
            Thn.setText(String.valueOf(dataThn));
            Desc.setText(dataDesc);
        }
    }

}
