<<<<<<< Updated upstream:VirtualChef/app/src/main/java/com/example/virtualchef/ManageFragment.java
package com.example.virtualchef;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import android.widget.Button;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ManageFragment() {
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
    public static ManageFragment newInstance(String param1, String param2) {
        ManageFragment fragment = new ManageFragment();
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
        //START FROM HERE
        /*
        View root = inflater.inflate(R.layout.your_fragment_layout, container, false);
        Button buttonAPI = root.findViewById(R.id.mange_button);

        buttonAPI.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               fetchDataFromAPI();
           }
        });

         */
        return inflater.inflate(R.layout.fragment_managebutton, container, false);
    }



    private void fetchDataFromAPI() {
        // Code to make an HTTP request to your Python API
        // You can use libraries like Retrofit, OkHttp, etc., to make the request
        // Replace "YOUR_API_ENDPOINT" with the actual endpoint of your Python API

        /*
        String apiEndpoint = "http://your-python-api-endpoint";
        // Use AsyncTask or any other method to perform network operations in the background
        // For simplicity, here we use AsyncTask, but consider using more modern alternatives for real projects

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                // Make an HTTP request to your Python API here
                // You can use libraries like Retrofit, OkHttp, etc.
                // Return the response from the API

                // Example using HttpURLConnection (you may need to handle exceptions and other details)
                try {
                    URL url = new URL(apiEndpoint);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    // Use BufferedReader or any other method to read the input stream
                    // Extract and return the data from the response

                    // For simplicity, we are using a Scanner to read the InputStream
                    Scanner scanner = new Scanner(in).useDelimiter("\\A");
                    return scanner.hasNext() ? scanner.next() : "";
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                // Handle the result from the API
                if (result != null) {
                    // Parse the result, update UI, etc.
                    // For simplicity, we print the result to log
                    Log.d("API_RESPONSE", result);
                } else {
                    // Handle the case where there was an error fetching data
                    Log.e("API_ERROR", "Error fetching data from API");
                }
            }
        }.execute();

         */
    }

=======
package com.example.virtualchef;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class ManageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ManageFragment() {
        // Required empty public constructor
    }
    public static ManageFragment newInstance(String param1, String param2) {
        ManageFragment fragment = new ManageFragment();
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
    ListView ingredientList;
    //String[] listItemPopulate = {"Item1","Item2","Item3"};
    ArrayAdapter<String> adapter;
    //LIST OF INGREDIENT CLASS
    private List<String> listItemPopulate;
    private List<String> fullNutrients;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_managebutton, container, false);
        ingredientList = view.findViewById(R.id.ingredientList);


        adapter = new ArrayAdapter<>(requireContext(), R.layout.ingredient_card, R.id.listText, new ArrayList<>());
        ingredientList.setAdapter(adapter);

        // Fetch data from the server and update the adapter
        fetchIngredientsFromServer();


        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //GET THE STRING INSIDE THE ITEM
                // Create an instance of the fragment
                String nutrientsInfo = fullNutrients.get(position);

                IngredientNutrients_delete fragment = new IngredientNutrients_delete();
                // Pass data to the fragment using arguments
                Bundle args = new Bundle();
                args.putString("nutrientsInfo", nutrientsInfo);
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
        String url = "http://10.0.2.2:5000/user/listIngredients"; // Replace with your actual server URL
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
                JSONObject foodItem = jsonArray.getJSONObject(i);
                String foodName = foodItem.optString("Item","");
                double serving_qty = foodItem.optDouble("Serving Quantity");
                double phosphorus = foodItem.optDouble("Phosphorus");
                String serving_unit = foodItem.optString("Serving Unit","");
                double serving_weight_grams = foodItem.optDouble("Serving Weight in grams");
                double calories = foodItem.optDouble("Calories");
                double totalFat = foodItem.optDouble("Total Fat");
                double saturatedFat = foodItem.optDouble("Saturated Fat");
                double cholesterol = foodItem.optDouble("Cholesterol");
                double sodium = foodItem.optDouble("Sodium");
                double totalCarbohydrate = foodItem.optDouble("Total Carbohydrate");
                double dietaryFiber = foodItem.optDouble("Dietary Fiber");
                double sugars = foodItem.optDouble("Sugars");
                double protein = foodItem.optDouble("Protein");
                double potassium = foodItem.optDouble("Potassium");


                String nutrientsInfo = "Item: " + foodName +
                        "\nCalories: " + calories +
                        "\nServing Quantity: " + serving_qty +
                        "\nServing Unit: " + serving_unit +
                        "\nServing Weight in grams: " + serving_weight_grams +
                        "\nTotal Fat: " + totalFat +
                        "\nSaturated Fat: " + saturatedFat +
                        "\nCholesterol: " + cholesterol +
                        "\nSodium: " + sodium +
                        "\nTotal Carbohydrate: " + totalCarbohydrate +
                        "\nDietary Fiber: " + dietaryFiber +
                        "\nSugars: " + sugars +
                        "\nProtein: " + protein +
                        "\nPotassium: " + potassium +
                        "\nPhosphorus: " + phosphorus;
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

                String item = jsonObject.optString("Item", "");
                String servingUnit = jsonObject.optString("Serving Unit", "");
                int quantity = jsonObject.optInt("quantity", 0);

                String result = "Name: " + item + " \nQuantity: " + quantity + " " + servingUnit ;
                ingredients.add(result);
                //fullNutrients.add(jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredients;
    }


>>>>>>> Stashed changes:app/src/main/java/com/example/virtualchef/ManageFragment.java
}