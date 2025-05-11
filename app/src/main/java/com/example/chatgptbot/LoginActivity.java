package com.example.chatgptbot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText editText;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.editTextName);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(v -> {
            String name = editText.getText().toString();
            SharedPreferences prefs = getSharedPreferences("ChatGPTPrefs", MODE_PRIVATE);
            prefs.edit().putString("username", name).apply();
            startActivity(new Intent(LoginActivity.this, ChatActivity.class));
            finish();
        });
    }
}