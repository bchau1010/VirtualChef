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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IngredientNutrients_delete extends Fragment {
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


        //FORMATED STRING
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
        textViewNutrients.setLineSpacing(16f, 1.0f);
        textViewNutrients.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        // Display nutrient information in the TextView
        //TextView textViewNutrients = view.findViewById(R.id.nutrientsTextView);
        //textViewNutrients.setText(nutrientsInfo);

        // Quantity EditText
        EditText quantityEditText = view.findViewById(R.id.ingredientQuantityTextBox);

        // Add Button centered at the bottom
        Button addButton = view.findViewById(R.id.ingredientAddButton);
        addButton.setText("Delete From Fridge");

        // Set a click listener for the button
        String itemName = extractItemName(nutrientsInfo);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click, send a request to the Python server
                String quantity = quantityEditText.getText().toString();
                if (quantity.isEmpty()) {
                    quantity = "1";
                }
                //Log.d("ADD TO DB STRING",nutrientsInfo);
                deleteIngredientDB(quantity,itemName);
                Toast.makeText(requireContext(), "Ingredient Count Removed", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //WRONG
    private void deleteIngredientDB(String quantity, String name) {
        //TRANSFORM THE STRING FROM REQUEST INTO A JSON FOR HTTP REQUEST
        try {
            JSONObject requestJson = new JSONObject();
            quantity =  "-" + quantity;
            requestJson.put("Item",name);
            requestJson.put("quantity", quantity);
            Log.d("SEND JSON",requestJson.toString());
            // MAKE CALL TO PYTHON SERVER TO ADD TO DB
            sendJsonToServer(requestJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String extractItemName(String input) {
        // Define the regular expression pattern
        String pattern = "Item: (.+)";
        Pattern regex = Pattern.compile(pattern);

        // Create a matcher with the input string
        Matcher matcher = regex.matcher(input);

        // Check if the pattern is found
        if (matcher.find()) {
            // Extract the value associated with "Item"
            return matcher.group(1);
        }

        // Return null if the pattern is not found
        return null;
    }

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
}