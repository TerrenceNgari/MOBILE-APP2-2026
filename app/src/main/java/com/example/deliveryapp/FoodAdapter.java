package com.example.deliveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    Context context;
    ArrayList<FoodModal> arrayList;

    public FoodAdapter(Context context, ArrayList<FoodModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.restaurent_items_single, parent, false);

        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull FoodViewHolder holder, int position) {

        FoodModal food = arrayList.get(position);

        // Food name
        holder.foodName.setText(food.getFoodName());

        // Food price
        holder.foodPrice.setText(
                String.format(Locale.CANADA, "$%.2f", food.getPrice())
        );

        // Calories
        holder.foodCalories.setText(
                food.getCalories() + " kcal"
        );

        // Get local image resource from its name
        int imageId = context.getResources().getIdentifier(
                food.getImageName(),
                "drawable",
                context.getPackageName()
        );

        if (imageId != 0) {
            holder.image.setImageResource(imageId);
        } else {
            holder.image.setImageResource(
                    R.drawable.ic_launcher_background
            );
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ItemPage.class);

                intent.putExtra("foodName", food.getFoodName());
                intent.putExtra("imageName", food.getImageName());
                intent.putExtra("price", food.getPrice());
                intent.putExtra("calories", food.getCalories());

                intent.putExtra("description", food.getDescription());
                intent.putExtra("carbs", food.getCarbs());
                intent.putExtra("protein", food.getProtein());
                intent.putExtra("fats", food.getFats());
                intent.putExtra("discount", food.getDiscount());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView foodName;
        TextView foodPrice;
        TextView foodCalories;
        ImageView image;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodCalories = itemView.findViewById(R.id.foodCalories);
            image = itemView.findViewById(R.id.foodImg);

            itemView.findViewById(R.id.foodAddBtn)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemView.performClick();
                        }
                    });
        }
    }
}
