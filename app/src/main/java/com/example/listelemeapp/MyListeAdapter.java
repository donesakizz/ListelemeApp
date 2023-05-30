package com.example.listelemeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyListeAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<VeriClass> veriList;

    public MyListeAdapter(Context context, List<VeriClass> veriList) {
        this.context = context;
        this.veriList = veriList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item , parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(veriList.get(position).getDataResim()).into(holder.recycImage);
        holder.recycBaslik.setText(veriList.get(position).getDataBaslik());
        holder.recycNot.setText(veriList.get(position).getDataTanim());
        holder.recycTarih.setText(veriList.get(position).getDataTarih());

        holder.recycCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetaylarActivity.class);
                intent.putExtra("Resim" ,veriList.get(holder.getAdapterPosition()).getDataResim());
                intent.putExtra("Not" ,veriList.get(holder.getAdapterPosition()).getDataTanim());
                intent.putExtra("Başlık" ,veriList.get(holder.getAdapterPosition()).getDataBaslik());
                intent.putExtra("Key",veriList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Tarih" ,veriList.get(holder.getAdapterPosition()).getDataTarih());

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return veriList.size();
    }

    public  void aramaVeriList(ArrayList<VeriClass> aramaList) {
        veriList = aramaList;
        notifyDataSetChanged();;
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recycImage;
    TextView recycBaslik, recycNot,recycTarih;
    CardView recycCardView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recycImage =itemView.findViewById(R.id.recycResim);
        recycCardView =itemView.findViewById(R.id.recycCardView);
        recycNot =itemView.findViewById(R.id.recycNot);
        recycTarih =itemView.findViewById(R.id.recycTarih);
        recycBaslik =itemView.findViewById(R.id.recycBaslik);
    }


}
