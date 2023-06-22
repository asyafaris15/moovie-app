package com.example.pemmob_d;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MoviAdaptrFavorit extends RecyclerView.Adapter<MoviAdaptrFavorit.MyViewHolder> {

    private List<NoteMovi> listMovi;
    private Context context;
    private DatabaseReference data = FirebaseDatabase.getInstance().getReference();
    private static ClickListener clickListener;

    public MoviAdaptrFavorit(List<NoteMovi> listMovi, Context activity) {
        this.listMovi = listMovi;
        this.context = activity;
    }

    @NonNull
    @Override
    public MoviAdaptrFavorit.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movi,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviAdaptrFavorit.MyViewHolder holder, int position) {
        NoteMovi mdata = listMovi.get(position);
        holder.judul.setText(mdata.getMovi_Judul());
        holder.thn.setText(String.valueOf(mdata.getMovi_Thn()));
        String Desc = mdata.getMovi_Desc();
        String DescOut;
        if(Desc.length() > 80){
            DescOut = Desc.substring(0,79);
            holder.desc.setText(DescOut + "...");
        }else {
            holder.desc.setText(mdata.getMovi_Desc());
        }

        if(mdata.getMovi_Thn() < 2020){
            holder.lauch.setText("Lama");
        }else{
            holder.lauch.setText("Baru");
        }
        holder.baru.setText(mdata.getMovi_id());

        holder.moviLayout.setOnClickListener(v -> {
            String dataId = holder.baru.getText().toString();
            String dataJudul = holder.judul.getText().toString();
            String dataThn = holder.thn.getText().toString();
            String dataDesc = mdata.getMovi_Desc();
            Intent intent = new Intent(context, DetailMovi_favorit.class);
            Bundle bundle = new Bundle();
            bundle.putString("dataId", dataId);
            bundle.putString("dataJudul", dataJudul);
            bundle.putString("dataThn", dataThn);
            bundle.putString("dataDesc", dataDesc);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listMovi.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout moviLayout;
        TextView judul, thn, desc, lauch, baru;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            moviLayout = itemView.findViewById(R.id.movi_layout);
            judul = itemView.findViewById(R.id.textView_JudulMovi);
            thn = itemView.findViewById(R.id.textView_thnMovi);
            desc = itemView.findViewById(R.id.textView_descMovi);
            lauch = itemView.findViewById(R.id.textView_status);
            baru = itemView.findViewById(R.id.textView_baru);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), itemView);
        }
    }

    public void setOnItemClickListener(MoviAdaptrFavorit.ClickListener clickListener) {
        MoviAdaptrFavorit.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
