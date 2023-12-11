package com.example.virtualchef;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SuggestRecipeFragment extends Fragment{
    private Spinner spinnerCuisine;
    private Spinner spinnerFoodCat;
    private Button generateRecipeButton;
    //private CardView cardView1;
    private String recipe1;
    private JSONObject recipe1Json;
    private RequestQueue requestQueue;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestrecipe, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());
        spinnerCuisine = view.findViewById(R.id.spinnerCuisine);
        spinnerFoodCat = view.findViewById(R.id.spinnerFoodCat);
        generateRecipeButton = view.findViewById(R.id.GenerateRecipeButton);
        //cardView1 = view.findViewById(R.id.cardView1);


        //set the spinner for cuisine
        ArrayAdapter<CharSequence> cuisineAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.cuisine_array,
                android.R.layout.simple_spinner_item
        );
        cuisineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCuisine.setAdapter(cuisineAdapter);

        //set the spinner for food category
        ArrayAdapter<CharSequence> foodCatAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.food_category_array,
                android.R.layout.simple_spinner_item
        );
        foodCatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFoodCat.setAdapter(foodCatAdapter);



        //CALL TO SERVER TO GET JSON AND DISPLAY RECIPE NAME ON THE CARD
        generateRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected values from spinners
                String selectedCuisine = spinnerCuisine.getSelectedItem().toString();
                String selectedFoodCat = spinnerFoodCat.getSelectedItem().toString();
                /////////////////////////////////////////////////////////////////
                JSONObject send = new JSONObject();
                try {
                    send.put("FoodCategory", selectedFoodCat);
                    send.put("Cuisine", selectedCuisine);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //START OF SERVER REQUEST
                String serverUrl = "http://10.0.2.2:5000/user/recipe";
                Log.d("HEADER", "REQUEST HEADER: " + send.toString());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        serverUrl,
                        send,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Extract the "text" field from the response JSON
                                Toast.makeText(requireContext(), "LOADING RECIPE...", Toast.LENGTH_SHORT).show();
                                try {
                                    Log.d("makeApiCall", "Generated Successful: " + response.toString());
                                    JSONArray choicesArray = response.getJSONArray("choices");
                                    recipe1 = choicesArray
                                            .getJSONObject(0)
                                            .getString("text");
                                    Log.d("ORIGINAL API TEXT RESPONSE", "ORIGINAL API TEXT RESPONSE: " + recipe1);

                                    recipe1Json = parseRecipeString(recipe1);
                                    Log.d("PARSE JSON", "PARSED RESULT: " + recipe1Json);


                                    // Create an instance of the fragment
                                    RecipeDetailsFragment fragment = new RecipeDetailsFragment();
                                    // Pass data to the fragment using arguments
                                    Bundle args = new Bundle();
                                    args.putString("recipeInfo", recipe1);
                                    fragment.setArguments(args);
                                    // Add the fragment to the activity
                                    FragmentManager fragmentManager = getParentFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frame_layout, fragment); // R.id.fragmentContainer is the ID of a FrameLayout in your activity_main.xml
                                    fragmentTransaction.commit();

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                // Call the callback with the extracted text
                                //callback.onSuccess(text);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Call the callback with the error
                                //callback.onError(error);

                            }
                        });

                //SET THE REQUEST TO ONLY 1, NO RETRYING
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);
                ///////////////////////////////////////////////////////////////////////////////////////////
            }
        });

        //WHEN CLICK ON CARD, RETURN THE FULL API RESPONSE TO A NEW FRAGMENT TO DISPLAY IT
        // ONLY DO AFTER requestRecipe
        /*
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a new fragment to display details (replace with your fragment transition code)
                //openRecipeDetailsFragment();
            }
        });
         */
        return view;
    }

    // TURN THE STRING INTO A JSON FOR RECIPE FOR DB INSERT
    private JSONObject parseRecipeString(String recipeString) throws JSONException {
        JSONObject recipeJson = new JSONObject();

        // Split the input string into lines
        String[] lines = recipeString.split("\n");

        // Variables to store temporary values
        JSONArray ingredientsArray = new JSONArray();
        JSONArray substitutionsArray = new JSONArray();
        JSONArray instructionsArray = new JSONArray();

        // Flag to determine the current section
        String currentSection = "";

        for (String line : lines) {
            // Split each line into key and value
            String[] keyValue = line.split(": ", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                // Check the key to determine the current section
                switch (key) {
                    case "Recipe Name":
                    case "Calories":
                    case "Protein":
                    case "Carbs":
                    case "Fat":
                    case "Dietary Restriction":
                        recipeJson.put(key, value);
                        break;
                    case "Ingredients":
                        currentSection = "Ingredients";
                        break;
                    case "Substitutions":
                        currentSection = "Substitutions";
                        break;
                    case "Instructions":
                        currentSection = "Instructions";
                        break;
                    default:
                        // Check the current section and add the value accordingly
                        switch (currentSection) {
                            case "Ingredients":
                                ingredientsArray.put(value);
                                break;
                            case "Substitutions":
                                substitutionsArray.put(value);
                                break;
                            case "Instructions":
                                instructionsArray.put(value);
                                break;
                        }
                }
            }
        }
        // Add arrays to the main JSON object
        recipeJson.put("Ingredients", ingredientsArray);
        recipeJson.put("Substitutions", substitutionsArray);
        recipeJson.put("Instructions", instructionsArray);

        return recipeJson;
    }

    //EXTRACT THE RECIPENAME FROM THE API RESPONSE
    //WORKING
    private String extractRecipeName(String jsonResponse){
        String recipeName = "";

        // Find the index of "Recipe Name:"
        int recipeNameIndex = jsonResponse.indexOf("Recipe Name:");

        // If "Recipe Name:" is found, extract the substring after it
        if (recipeNameIndex != -1) {
            // Find the start index of the recipe name
            int startIndex = recipeNameIndex + "Recipe Name:".length();

            // Find the end index of the recipe name (up to the next newline character)
            int endIndex = jsonResponse.indexOf("\n", startIndex);

            // Extract the recipe name substring
            if (endIndex != -1) {
                recipeName = jsonResponse.substring(startIndex, endIndex).trim();
            }
        }
        return recipeName;
    }

    //DISPLAY THE RECIPE NAME ON THE TEXTVIEW OF CARDVIEW
    //WORKING
    private void setCardViewText(CardView cardView, int textViewResourceId, String text) {
        TextView textView = cardView.findViewById(textViewResourceId);
        if (textView != null) {
            textView.setText(text);
        }
    }

    //OPEN A NEW FRAGMENT TO DISPLAY THE DETAILS OF THE RECIPE, FOR CARDVIEW ONCLICK EVENT
    //NOT WORKING
    private void openRecipeDetailsFragment() {
        // Replace with your fragment transition code
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, recipeDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

}
