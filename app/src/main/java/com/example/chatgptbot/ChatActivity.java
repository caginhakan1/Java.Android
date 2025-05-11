package com.example.chatgptbot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    EditText inputMessage;
    ImageButton sendButton;
    RecyclerView chatRecyclerView;
    ChatAdapter chatAdapter;
    List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("ChatGPTPrefs", MODE_PRIVATE);
        String theme = prefs.getString("theme", "light");
        setTheme(theme.equals("dark") ? R.style.Theme_ChatGPTBot_Dark : R.style.Theme_ChatGPTBot_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        inputMessage = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSend);
        chatRecyclerView = findViewById(R.id.recyclerView);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages, this);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> {
            String message = inputMessage.getText().toString();
            chatMessages.add(new ChatMessage(message, true));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            inputMessage.setText("");

            new ChatGPTRequest(response -> {
                chatMessages.add(new ChatMessage(response, false));
                chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            }).execute(message);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_toggle_theme) {
            SharedPreferences prefs = getSharedPreferences("ChatGPTPrefs", MODE_PRIVATE);
            String current = prefs.getString("theme", "light");
            prefs.edit().putString("theme", current.equals("dark") ? "light" : "dark").apply();
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}