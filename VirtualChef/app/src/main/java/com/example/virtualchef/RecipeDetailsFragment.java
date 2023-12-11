package com.example.virtualchef;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import androidx.fragment.app.Fragment;

public class RecipeDetailsFragment extends Fragment {
    private Button saveRecipeButton;
    private Button makeRecipeButton;
    private TextView textViewRecipeDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipedetail, container, false);

        // GET THE TEXT RESPONSE (NOT PARSE) FROM API FROM SUGGESTRECIPEFRAGMENT
        String recipeInfo = getArguments().getString("recipeInfo");

        // Display nutrient information in the TextView
        TextView textViewNutrients = view.findViewById(R.id.recipeDetailTextView);

        // Make specified titles bold
        SpannableString spannableString = new SpannableString(recipeInfo);

        makeBold(spannableString, "Recipe Name");
        makeBold(spannableString, "Ingredients");
        makeBold(spannableString, "Instructions");
        makeBold(spannableString, "Calories");
        makeBold(spannableString, "Protein");
        makeBold(spannableString, "Carbs");
        makeBold(spannableString, "Fat");
        makeBold(spannableString, "Substitution");
        makeBold(spannableString, "Dietary Restriction");

        textViewNutrients.setText(spannableString);
        textViewNutrients.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


        makeRecipeButton = view.findViewById(R.id.makeRecipeAddButton);
        makeRecipeButton.setVisibility(View.GONE);

        saveRecipeButton = view.findViewById(R.id.saveRecipeButton);

        saveRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //the parsing should be handled by python
                JSONObject sendRequest = createRecipeJson(recipeInfo);
                sendJsonToServer(sendRequest);
                Toast.makeText(requireContext(), "Recipe Saved", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void makeBold(SpannableString spannableString, String target) {
        int start = spannableString.toString().indexOf(target);
        int end = start + target.length();
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    // ADD TO DB
    public void sendJsonToServer(JSONObject jsonObject) {
        String serverUrl = "http://10.0.2.2:5000/user/saveRecipe";
        // Get the context of the associated activity
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

    private static JSONObject createRecipeJson(String recipeInfo) {
        JSONObject recipeJson = new JSONObject();
        try {
            recipeJson.put("recipeInfo", recipeInfo.trim());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.d("PARSE SEND JSON", recipeJson.toString());
        return recipeJson;
    }
}