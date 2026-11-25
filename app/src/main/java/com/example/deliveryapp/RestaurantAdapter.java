package com.example.deliveryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>{

    private Context context;
    private ArrayList<RestaurantModal> arrayList;

    public RestaurantAdapter(Context context, ArrayList<RestaurantModal> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        RestaurantModal restaurant = arrayList.get(position);

        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantDescription.setText(restaurant.getDescription());
        holder.restaurantAddress.setText(restaurant.getAddress());

        int imageId = context.getResources().getIdentifier(
                restaurant.getImageName(),
                "drawable",
                context.getPackageName()
        );

        if (imageId != 0){
            holder.restaurantImage.setImageResource(imageId);
        }

        else {
            holder.restaurantImage.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.findViewById(R.id.textButtonRestaurantMenu)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(holder.itemView.getContext(), OrderPage.class);
                        intent.putExtra("restaurantId", restaurant.getId());
                        intent.putExtra("restaurantName", restaurant.getName());
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class RestaurantViewHolder extends RecyclerView.ViewHolder{
        TextView restaurantName;
        TextView restaurantDescription;
        TextView restaurantAddress;
        ImageView restaurantImage;

        public RestaurantViewHolder(@NonNull View itemView){
            super(itemView);

            restaurantName = itemView.findViewById(R.id.textRestaurantName);
            restaurantDescription = itemView.findViewById(R.id.textRestaurantDescription);
            restaurantAddress = itemView.findViewById(R.id.textRestaurantAddress);
            restaurantImage = itemView.findViewById(R.id.imageRestaurant);
        }
    }
}
