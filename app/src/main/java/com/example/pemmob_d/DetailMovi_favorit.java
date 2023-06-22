package com.example.pemmob_d;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class DetailMovi_favorit extends AppCompatActivity implements View.OnClickListener {
    TextView Judul, Thn, Desc, id;
    Button Hapus;
    private String id_movi;
    Spinner pilih;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_datamovinonspiner);
        Judul = findViewById(R.id.textView_JudulMovi);
        Thn = findViewById(R.id.textView_thnMovi);
        Desc = findViewById(R.id.textView_descMovi);
        Hapus = findViewById(R.id.button_simpan);
        id = findViewById(R.id.textView_id);
        pilih = findViewById(R.id.pilih);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        if (Hapus.getText().toString().equals("Simpan")){
            Hapus.setText("Hapus");
        }

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
            id_movi = dataId;
        }

        Hapus.setOnClickListener(v -> {
            FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
//            databaseReference.child("Status").child("movi_id").equalTo(id_movi).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    NoteStatus status = snapshot.getValue(NoteStatus.class);
//                    String user_id = status.getUser_id();
//                    String key;
//                    key = snapshot.getKey();
////                    for (DataSnapshot data : snapshot.getChildren()){
////                        key = data.getKey();
//                        if(user_id.equals(User.getUid()) ){
//                            databaseReference.child("Status").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    Intent intent = new Intent(DetailMovi_favorit.this, ListMovi_favorit.class);
//                                    startActivity(intent);
//                                }
//                            });
//                        }
////                    }
//
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(DetailMovi_favorit.this, "Data Tidak Terhapus", Toast.LENGTH_SHORT).show();
//                }
//            });

            databaseReference.child("Status").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    NoteStatus note = snapshot.getValue(NoteStatus.class);
                    String key = snapshot.getKey();
                    if (note.getUser_id().equals(User.getUid()) && note.getMovi_id().equals(id_movi)) {
                        databaseReference.child("Status").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                                Intent intent = new Intent(DetailMovi_favorit.this, ListMovi_favorit.class);
                                startActivity(intent);
                            }

                        });
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
        });

    }

    @Override
    public void onClick(View view) {

    }
}
