package com.example.food_bites.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_bites.Adapter.CartAdapter;
import com.example.food_bites.Helper.ChangeNumberItemsListener;
import com.example.food_bites.Helper.ManagmentCart;
import com.example.food_bites.R;
import com.example.food_bites.databinding.ActivityCartBinding;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class CartActivity extends BaseActivity implements PaymentResultListener {

    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;

    Button pay, app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        pay = findViewById(R.id.payNow);
        app = findViewById(R.id.button);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePayment();
            }
        });

        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CartActivity.this, "Invalid Coupon code", Toast.LENGTH_SHORT).show();
            }
        });


        setVariable();
        calculateCart();
        initList();
    }

    private void makePayment() {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_1zoNrXXfHcx4oC");
        checkout.setImage(R.drawable.logo_nobg);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();

            options.put("name", "FBites.Ltd");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "USD");
            double amount = calculateCart() * 100;
            options.put("amount", String.valueOf(amount));//amt * 100 because they ll divide amt by 100
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact", "9606630467");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

        private void initList () {
            if (managmentCart.getListCart().isEmpty()) {
                binding.emptyTxt.setVisibility(View.VISIBLE);
                binding.scrollViewCart.setVisibility(View.GONE);
            } else {
                binding.emptyTxt.setVisibility(View.GONE);
                binding.scrollViewCart.setVisibility(View.VISIBLE);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.cardView.setLayoutManager(linearLayoutManager);
            adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
            binding.cardView.setAdapter(adapter);
        }

        private double calculateCart () {
            double percentTax = 0.02;
            double delivery = 10;

            tax = Math.round(managmentCart.getTotalFee() * percentTax * 100) / 100;

            double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
            double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

            binding.totalFeeTxt.setText("$ " + itemTotal);
            binding.taxTxt.setText("$ " + tax);
            binding.deliveryTxt.setText("$ " + delivery);
            binding.totalTxt.setText("$ " + total);

            return total;
        }

        private void setVariable () {
            binding.backBtn.setOnClickListener(view -> finish());
        }

        @Override
        public void onPaymentSuccess (String s){
            Intent intent = new Intent(CartActivity.this, SuccessActivity.class);
            managmentCart.clearCart();
            startActivity(intent);
        }

        @Override
        public void onPaymentError(int i, String s) {
            Toast.makeText(this, "Payment failed. Please try again", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Payment error: " + s);
        }



}