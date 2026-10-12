package com.example.deliveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    @NonNull
    Context context;
    ArrayList<FoodModal> arrayList;

    FoodAdapter(Context context, ArrayList<FoodModal> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurent_items_single,parent,false);
        FoodViewHolder fvh = new FoodViewHolder(view);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.foodName.setText(arrayList.get(position).foodName);
        holder.image.setImageResource(arrayList.get(position).foodImg);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder{
        TextView foodName;
        ImageView image;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            image = itemView.findViewById(R.id.foodImg);
        }
    }

}


