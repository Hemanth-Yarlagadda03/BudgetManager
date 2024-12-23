package com.example.test;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {

    EditText etDate, etCategory, etAmount, etDescription;
    Button btnSave;
    DatabaseHelper db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        etDate = findViewById(R.id.etDate);
        etCategory = findViewById(R.id.etCategory);
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        db = new DatabaseHelper(this);

        btnSave.setOnClickListener(v -> {
            String date = etDate.getText().toString();
            if (!isValidDate(date)) {
                Toast.makeText(this, "Invalid date. Please enter a valid date in the format yyyy-MM-dd.", Toast.LENGTH_SHORT).show();
                return;
            }

            String category = etCategory.getText().toString();
            double amount;
            try {
                amount = Double.parseDouble(etAmount.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                return;
            }
            String description = etDescription.getText().toString();

            sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String username = sharedPreferences.getString("loggedInUser", "Guest");

            boolean inserted = db.insertTransaction(username, date, category, amount, description);
            if (inserted) {
                Toast.makeText(this, "Transaction added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setLenient(false);
        try {
            Date parsedDate = dateFormat.parse(date);
            // Check if the date is not in the future
            if (parsedDate.after(new Date())) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}