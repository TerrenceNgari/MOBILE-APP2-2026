package com.example.deliveryapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderPage extends AppCompatActivity {

    ArrayList<FoodModal> arrLst = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_page);

        RecyclerView recView = findViewById(R.id.restItemsRecView);

        arrLst.add(new FoodModal("Chicken",R.drawable.ic_launcher_background));
        arrLst.add(new FoodModal("beef",R.drawable.ic_launcher_background));
        arrLst.add(new FoodModal("pork",R.drawable.ic_launcher_background));
        arrLst.add(new FoodModal("milk",R.drawable.ic_launcher_background));

        //Set Adapter
        FoodAdapter adapter = new FoodAdapter(this,arrLst);
        recView.setAdapter(adapter);

        //layout manager
        recView.setLayoutManager(new GridLayoutManager(this, 2));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}