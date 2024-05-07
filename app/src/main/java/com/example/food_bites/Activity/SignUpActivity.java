package com.example.food_bites.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_bites.R;
import com.example.food_bites.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignUpActivity extends BaseActivity {

    ActivitySignUpBinding binding;
    TextView Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Login = findViewById(R.id.signUp);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        setVariable();
    }

    private void setVariable() {
    binding.signupBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = binding.userEdit.getText().toString();
            String password = binding.passEdt.getText().toString();

            if(password.length() < 6) {
                Toast.makeText(SignUpActivity.this, "Your password must be 6 character", Toast.LENGTH_SHORT).show();
                return;
            }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Log.i(TAG, "Complete");
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        } else {
                            Log.i(TAG, "failure : "+task.getException());
                            Toast.makeText(SignUpActivity.this, "Authetication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    });
    }
}