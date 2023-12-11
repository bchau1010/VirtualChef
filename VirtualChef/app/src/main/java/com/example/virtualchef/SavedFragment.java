<<<<<<< Updated upstream:VirtualChef/app/src/main/java/com/example/virtualchef/SavedFragment.java
package com.example.virtualchef;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SavedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedFragment newInstance(String param1, String param2) {
        SavedFragment fragment = new SavedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_savedbutton, container, false);
    }
=======
package com.example.virtualchef;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SavedFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public SavedFragment() {
        // Required empty public constructor
    }
    public static SavedFragment newInstance(String param1, String param2) {
        SavedFragment fragment = new SavedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }






    //LIST VIEW
    ListView recipeList;
    //String[] listItemPopulate = {"Item1","Item2","Item3"};
    TextView recipeTextView;
    ArrayAdapter<String> adapter;
    private List<String> listItemPopulate;
    private List<String> fullNutrients;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_managebutton, container, false);
        recipeList = view.findViewById(R.id.ingredientList);
        recipeTextView = view.findViewById(R.id.textViewFridgeTitle);

        recipeTextView.setText("Your Saved Recipes");

        adapter = new ArrayAdapter<>(requireContext(), R.layout.ingredient_card, R.id.listText, new ArrayList<>());
        recipeList.setAdapter(adapter);

        // Fetch data from the server and update the adapter
        fetchIngredientsFromServer();


        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recipeInfo = fullNutrients.get(position);

                deleteRecipe fragment = new deleteRecipe();
                // Pass data to the fragment using arguments
                Bundle args = new Bundle();
                args.putString("recipeInfo", recipeInfo);
                fragment.setArguments(args);

                // Add the fragment to the activity
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment); // R.id.fragmentContainer is the ID of a FrameLayout in your activity_main.xml
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    //SEND REQUEST TO SERVER AND RECIEVE A JSON OF ALL INGREDIENT
    //WORKING
    private void fetchIngredientsFromServer() {
        String url = "http://10.0.2.2:5000/user/listRecipes"; // Replace with your actual server URL
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        fullNutrients = fullParseJsonResponse(response);
                        listItemPopulate = parseJsonResponse(response);

                        //CREATE NEW ADAPTER AND SET ADAPTER
                        //ingredientAdapter = new IngredientAdapter(ingredients);
                        //recyclerView.setAdapter(ingredientAdapter);
                        Log.d("RESPONSE",response.toString());
                        Log.d("LISTITEMPOPULATE",listItemPopulate.toString());
                        Log.d("FULL NUTRIENTS",fullNutrients.toString());

                        adapter.clear();
                        adapter.addAll(listItemPopulate);
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error fetching data from server", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private List<String> fullParseJsonResponse(JSONArray jsonArray) {
        List<String> full = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeItem = jsonArray.getJSONObject(i);
                String Calories = recipeItem.optString("Calories","");
                String Carbs = recipeItem.optString("Carbs","");
                String DietaryRestriction = recipeItem.optString("Dietary Restriction");
                String Fat = recipeItem.optString("Fat","");
                String Ingredients = recipeItem.optString("Ingredients","");
                String Instructions = recipeItem.optString("Instructions","");
                String Protein = recipeItem.optString("Protein","");
                String RecipeName = recipeItem.optString("Recipe Name","");
                String Substitutions = recipeItem.optString("Substitutions","");

                String nutrientsInfo = "Recipe Name: " + RecipeName +
                        "\nIngredients: " + Ingredients +
                        "\nInstructions: " + Instructions +
                        "\nCalories: " + Calories +
                        "\nCarbs: " + Carbs +
                        "\nFat: " + Fat +
                        "\nProtein: " + Protein +
                        "\nSubstitutions: " + Substitutions +
                        "\nDietary Restriction: " + DietaryRestriction;

                full.add(nutrientsInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return full;
    }

    //THIS SHOULD RETURN ARRAY OF STRING
    //WORKING
    private List<String> parseJsonResponse(JSONArray jsonArray) {
        List<String> ingredients = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String item = jsonObject.optString("Recipe Name", "");

                String result = "Name: " + item;
                ingredients.add(result);
                //fullNutrients.add(jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ingredients;
    }
>>>>>>> Stashed changes:app/src/main/java/com/example/virtualchef/SavedFragment.java
}