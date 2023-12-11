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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class deleteRecipe extends Fragment {
    private RequestQueue requestQueue;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Initialize the RequestQueue in onAttach or another appropriate lifecycle method
        requestQueue = Volley.newRequestQueue(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipedetail, container, false);



        // Retrieve nutrient information from the arguments
        String recipeInfo = getArguments().getString("recipeInfo");


        String[] lines = recipeInfo.split("\n");
        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] keyValue = line.split(": ");

            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];

                // Make specific keys bold and black
                if (key.equals("Recipe Name") || key.equals("Ingredients") || key.trim().equals("Instructions")
                        || key.equals("Calories") || key.equals("Carbs")
                        || key.equals("Fat") || key.equals("Protein")
                        || key.equals("Substitutions") || key.equals("Dietary Restriction")) {
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
        TextView textViewNutrients = view.findViewById(R.id.recipeDetailTextView);
        textViewNutrients.setText(HtmlCompat.fromHtml(formattedText.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        textViewNutrients.setLineSpacing(16f, 1.0f);
        textViewNutrients.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


        /* This is the string
        String nutrientsInfo = "Recipe Name: " + RecipeName +
                        "\nIngredients: " + Ingredients +
                        "\nInstructions: " + Instructions +
                        "\nCalories: " + Calories +
                        "\nCarbs: " + Carbs +
                        "\nFat: " + Fat +
                        "\nProtein: " + Protein +
                        "\nSubstitutions: " + Substitutions +
                        "\nDietary Restriction: " + DietaryRestriction;
         */

        // Display nutrient information in the TextView
        //TextView textViewNutrients = view.findViewById(R.id.recipeDetailTextView);
        //textViewNutrients.setText(recipeInfo);

        Button addButton = view.findViewById(R.id.saveRecipeButton);
        addButton.setText("Delete Recipe");

        Button deleteIngredientsFromRecipe = view.findViewById(R.id.makeRecipeAddButton);

        String ingredientsString = extractRawIngredients(recipeInfo);
        List<Map<String, String>> ingredientsList = parseIngredients(ingredientsString);

        //EXTRACT THE NAME FROM THE STRING
        String itemName = extractItemName(recipeInfo);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("ADD TO DB STRING",nutrientsInfo);
                deleteRecipeDB(itemName);
                Toast.makeText(requireContext(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        //FINISH THIS
        deleteIngredientsFromRecipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Log.d("RAW INGREDIENT",ingredientsList.toString());
                deleteIngredientsDB(ingredientsList);
                Toast.makeText(requireContext(), "Ingredients Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //DONE
    private void deleteRecipeDB(String name) {
        //TRANSFORM THE STRING FROM REQUEST INTO A JSON FOR HTTP REQUEST
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("recipeName",name);
            Log.d("SEND JSON",requestJson.toString());
            // MAKE CALL TO PYTHON SERVER TO ADD TO DB
            sendJsonToServer(requestJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //DONE
    public static String extractItemName(String recipeInfo) {
        String[] lines = recipeInfo.split("\n");

        for (String line : lines) {
            if (line.startsWith("Recipe Name:")) {
                // Assuming the recipe name follows the "Recipe Name:" prefix
                String result = "" + line.substring("Recipe Name:".length()).trim();
                return result;
            }
        }

        // Return null if no recipe name is found
        return null;
    }

    //DONE
    public void sendJsonToServer(JSONObject jsonObject) {
        String serverUrl = "http://10.0.2.2:5000/user/deleteRecipe";
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

    //DONE
    public static String extractRawIngredients(String recipeInfo) {
        String[] lines = recipeInfo.split("\n");

        for (String line : lines) {
            if (line.startsWith("Ingredients:")) {
                //THIS IS THE RAW INGREDIENTS STRING WITH NAME*QUANTITY
                String rawIngredients = line.substring("Ingredients:".length()).trim();
                return rawIngredients;
            }
        }

        // Return null if no recipe name is found
        return null;
    }

    public static List<Map<String, String>> parseIngredients(String ingredientsString) {
        List<Map<String, String>> ingredientsList = new ArrayList<>();
        // Regular expression to match a number at the beginning of the string
        Pattern pattern = Pattern.compile("(\\d+)");
        String[] items = ingredientsString.split(", ");

        for (String item : items) {
            String[] parts = item.split("\\*");

            if (parts.length == 2) {
                String itemName = parts[0].trim();
                String quantity = parts[1].trim();

                // Extract the first digit using regular expression
                Matcher matcher = pattern.matcher(quantity);
                if (matcher.find()) {
                    quantity = matcher.group(1);
                }

                Map<String, String> ingredientMap = new HashMap<>();
                quantity = quantity.substring(0,1);
                quantity = "-"+quantity;
                itemName = itemName.toLowerCase();
                ingredientMap.put("Item", itemName);
                ingredientMap.put("quantity", quantity);
                ingredientsList.add(ingredientMap);
            }
        }
        return ingredientsList;
    }

    //NOT DONE, PUT JSON REQUEST, SHOULD FORMAT START WITH INGREDIENTS, ITEM AND QUANTITY
    private void deleteIngredientsDB(List<Map<String, String>> dataList) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (Map<String, String> data : dataList) {
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
                jsonArray.put(jsonObject);
            }

            JSONObject resultObject = new JSONObject();
            resultObject.put("ingredients", jsonArray);
            Log.d("RESULT OBJECT",resultObject.toString());
            sendIngredientsJsonToServer(resultObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // NOT DONE, SEND TO SERVER
    public void sendIngredientsJsonToServer(JSONObject jsonObject) {
        String serverUrl = "http://10.0.2.2:5000/user/deleteIngredients";
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
