package com.example.virtualchef;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class IngredientNutrients extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_nutrients);

        TextView nutrientsTextView = findViewById(R.id.nutrientsTextView);

        // Retrieve nutrient information from the intent
        String nutrientsInfo = getIntent().getStringExtra("nutrientsInfo");

        // Display nutrient information in the TextView
        nutrientsTextView.setText(nutrientsInfo);
    }
}
