package com.example.food_bites.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.food_bites.R;
import com.example.food_bites.databinding.ActivityDetailBinding;
import com.example.food_bites.databinding.ActivitySuccessBinding;

public class SuccessActivity extends BaseActivity {

    ActivitySuccessBinding binding ;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(view -> startActivity(new Intent(SuccessActivity.this, MainActivity.class)));

    }
}