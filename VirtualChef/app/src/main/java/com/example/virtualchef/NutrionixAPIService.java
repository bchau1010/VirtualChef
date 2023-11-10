package com.example.virtualchef;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NutrionixAPIService {
    private static final String API_URL = "https://trackapi.nutritionix.com/v2/search/instant";
    private static final String API_KEY = "63903389f8ee97d4aeb8bd5f51dd923f"; // Replace with your actual API key
    private static final String API_ID = "c4296d51";
    private RequestQueue requestQueue;

    public NutrionixAPIService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getSearchSuggestions(String query, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = API_URL + "?query=" + query + "&branded=false";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-app-id", API_ID);
                headers.put("x-app-key", API_KEY);
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
