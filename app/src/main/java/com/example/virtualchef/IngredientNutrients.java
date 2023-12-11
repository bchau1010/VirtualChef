package com.example.virtualchef;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class IngredientNutrients extends Fragment {
    private RequestQueue requestQueue;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Initialize the RequestQueue in onAttach or another appropriate lifecycle method
        requestQueue = Volley.newRequestQueue(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredient_nutrients, container, false);

        // Retrieve nutrient information from the arguments
        String nutrientsInfo = getArguments().getString("nutrientsInfo");
        String display = nutrientsInfo;

        //DO IT HERE
        String[] lines = nutrientsInfo.split("\n");
        StringBuilder formattedText = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] keyValue = line.split(": ");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];

                // Make specific keys bold and black
                if (key.equals("Item") || key.equals("Calories") || key.equals("Serving Quantity")
                        || key.equals("Serving Unit") || key.equals("Serving Weight in grams")
                        || key.equals("Total Fat") || key.equals("Saturated Fat")
                        || key.equals("Cholesterol") || key.equals("Sodium")
                        || key.equals("Total Carbohydrate") || key.equals("Dietary Fiber")
                        || key.equals("Sugars") || key.equals("Protein") || key.equals("Potassium")
                        || key.equals("Phosphorus")) {
                    key = "<b>" + key + "</b>";
                }
                formattedText.append(key).append(": ").append(value);

                // Add line break for all lines except the last one
                if (i < lines.length - 1) {
                    formattedText.append("<br>");
                }
            }
        }


        // Display nutrient information in the TextView
        TextView textViewNutrients = view.findViewById(R.id.nutrientsTextView);
        textViewNutrients.setText(HtmlCompat.fromHtml(formattedText.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        textViewNutrients.setLineSpacing(16f, 1.0f); // Adjust the line spacing as needed
        textViewNutrients.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        //textViewNutrients.setText(nutrientsInfo);

        // Quantity EditText
        EditText quantityEditText = view.findViewById(R.id.ingredientQuantityTextBox);

        // Add Button centered at the bottom
        Button addButton = view.findViewById(R.id.ingredientAddButton);

        // Set a click listener for the button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click, send a request to the Python server
                String quantity = quantityEditText.getText().toString();
                if (quantity.isEmpty()) {
                    quantity = "1";
                }
                //Log.d("ADD TO DB STRING",nutrientsInfo);
                postIngredientDB(quantity,nutrientsInfo);
                Toast.makeText(requireContext(), "Ingredient Added", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private void postIngredientDB(String quantity, String nutrientsInfo) {

        //TRANSFORM THE STRING FROM REQUEST INTO A JSON FOR HTTP REQUEST
        try {
            String[] lines = nutrientsInfo.split("\n");
            JSONObject nutrientsJson = new JSONObject();
            for (String line : lines) {
                String[] keyValue = line.split(": ");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    nutrientsJson.put(key, value);
                }
            }

            // Include quantity and nutrient information directly in the main JSON object
            nutrientsJson.put("quantity", quantity);
            // Log the JSON object before sending the request
            Log.d("IngredientNutrients", "JSON Request: " + nutrientsJson.toString());

            // MAKE CALL TO PYTHON SERVER TO ADD TO DB
            sendJsonToServer(nutrientsJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //ADD TO DB
    public void sendJsonToServer(JSONObject jsonObject) {
        String serverUrl = "http://10.0.2.2:5000/user/addIngredients";
        String secretKey = "WHATEVER YOU WANT";
        Context context = getContext();

        if (context != null) {
            // Create a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            // Create a JSON request with custom headers
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    serverUrl,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle the server response
                            Log.d("VolleyResponse", response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors
                            Log.e("VolleyError", "Error: " + error.toString());
                        }
                    }
            );
            // Add the request to the queue
            requestQueue.add(jsonObjectRequest);
        }
    }

    private String displayNutrientInformation(String nutrientsInfo) {
        // Customize the appearance of number values in the TextView
        String[] lines = nutrientsInfo.split("\n");
        StringBuilder formattedText = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] keyValue = line.split(": ");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];

                // Make specific keys bold and black
                if (key.equals("Item") || key.equals("Calories") || key.equals("Serving Quantity")
                        || key.equals("Serving Unit") || key.equals("Serving Weight in Grams")
                        || key.equals("Total Fat") || key.equals("Saturated Fat")
                        || key.equals("Cholesterol") || key.equals("Sodium")
                        || key.equals("Total Carbohydrate") || key.equals("Dietary Fiber")
                        || key.equals("Sugars") || key.equals("Protein") || key.equals("Potassium")
                        || key.equals("Phosphorus")) {
                    key = "<div style='text-align:center;'><b><big>" + key + "</big></b></div>";
                }
                formattedText.append(key).append(": ").append(value);

                // Add line break for all lines except the last one
                if (i < lines.length - 1) {
                    formattedText.append("<br>");
                }
            }
        }
        return null;
    }

}
/*
public class IngredientNutrients extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_nutrients);

        //TextView nutrientsTextView = findViewById(R.id.nutrientsTextView);

        // Retrieve nutrient information from the intent
        String nutrientsInfo = getIntent().getStringExtra("nutrientsInfo");

        // Display nutrient information in the TextView
        TextView textViewNutrients = findViewById(R.id.nutrientsTextView);
        textViewNutrients.setText(nutrientsInfo);
    }
}
*/