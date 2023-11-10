package com.example.virtualchef;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import com.example.virtualchef.NutrionixAPIService;

import android.text.Editable;
import android.text.TextWatcher;



public class SearchFragment extends Fragment {
    private NutrionixAPIService nutritionixAPIService;
    private ArrayAdapter<String> adapter;
    private List<String> searchSuggestions;
    private ListView listView;
    private AutoCompleteTextView autoCompleteTextView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_searchbutton, container, false);

        //USE NUTRITIONIXAPI CALL WHEN USER TYPE IN SEARCH BAR
        nutritionixAPIService = new NutrionixAPIService(requireContext());
        //GET SEARCHBUTTON SEARCH BAR AND IMPLEMENT SERVICE CALL

        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);

        //listView = view.findViewById(R.id.listView);

        searchSuggestions = new ArrayList<>();

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, searchSuggestions);

        autoCompleteTextView.setAdapter(adapter);


        // Handle item click in the list, you can add your logic here
        /*
        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedSuggestion = (String) parent.getItemAtPosition(position);
            //String selectedSuggestion = (String) searchSuggestions.get(position);
            // Add your logic for handling the selected suggestion
        });
         */

        // WHEN USER TYPE, SUGGEST INGREDIENTS
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is called to notify you that somewhere within charSequence, the text has been changed.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is called to notify you that somewhere within charSequence, the text has been changed.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is called to notify you that somewhere within editable, the text has been changed.
                String newText = editable.toString();
                getSearchSuggestions(newText);
            }
        });
        return view;


    }
    private void getSearchSuggestions (String query){
        nutritionixAPIService.getSearchSuggestions(query, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    searchSuggestions.clear(); // Clear previous suggestions

                    // Parse the JSON response to get suggestions
                    JSONArray commonArray = response.getJSONArray("common");
                    for (int i = 0; i < commonArray.length(); i++) {
                        JSONObject suggestionObject = commonArray.getJSONObject(i);
                        String suggestion = suggestionObject.getString("food_name");
                        searchSuggestions.add(suggestion);

                        //System.out.println("Suggestion: " + suggestion);
                    }

                    // Notify the adapter that the data set has changed
                    autoCompleteTextView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    // If you are using AutoCompleteTextView for dropdown, set the adapter
                    AutoCompleteTextView autoCompleteTextView = requireView().findViewById(R.id.autoCompleteTextView);
                    autoCompleteTextView.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, searchSuggestions));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error here
                System.out.print("API LOOK UP ERROR");
            }
        });
    }
}

/*
add img add quantity add unit add
add to db via python via http request
 */