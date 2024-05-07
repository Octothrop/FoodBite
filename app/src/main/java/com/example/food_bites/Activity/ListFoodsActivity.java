package com.example.food_bites.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.food_bites.Adapter.CategoryAdapter;
import com.example.food_bites.Adapter.FoodListAdapter;
import com.example.food_bites.Domain.Foods;
import com.example.food_bites.R;
import com.example.food_bites.databinding.ActivityListFoodBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodsActivity extends BaseActivity {

    ActivityListFoodBinding binding;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initList();
        setVariable();
    }

    private void setVariable() {
    }


    private void initList() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.pb.setVisibility(View.VISIBLE);

        ArrayList<Foods> list = new ArrayList<>();
        Query query;
        if(isSearch) {
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText+'\uf8ff');
        } else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(Foods.class));
                    }
                    if (list.size() > 0) {
                        binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this, 2));
                        FoodListAdapter adapterListFood = new FoodListAdapter(list);
                        binding.foodListView.setAdapter(adapterListFood);
                    }

                    binding.pb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 1);
        categoryName = getIntent().getStringExtra("CategoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        binding.tittleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }
}