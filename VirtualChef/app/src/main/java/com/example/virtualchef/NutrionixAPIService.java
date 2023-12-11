<<<<<<< Updated upstream:VirtualChef/app/src/main/java/com/example/virtualchef/NutrionixAPIService.java
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
=======
package com.example.virtualchef;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import android.net.Uri;
import android.util.Log;


public class NutrionixAPIService {
    private static final String API_URL_SEARCH_INSTANT = "https://trackapi.nutritionix.com/v2/search/instant";
    private static final String API_URL_NATURAL_NUTRIENTS = "https://trackapi.nutritionix.com/v2/natural/nutrients";

    private static final String API_KEY = "63903389f8ee97d4aeb8bd5f51dd923f";
    private static final String API_ID = "c4296d51";
    private RequestQueue requestQueue;

    public NutrionixAPIService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    //MULTIPLE ITEM
    public void getSearchSuggestions(String query, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = API_URL_SEARCH_INSTANT + "?query=" + query + "&branded=false";
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

    //ONLY 1 ITEM
    public void getNutrients(String foodName, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        //String url = API_URL_NATURAL_NUTRIENTS + "?query=" + URLEncoder.encode(foodName, "UTF-8");
        //Uri.Builder builder = Uri.parse(API_URL_NATURAL_NUTRIENTS).buildUpon();
        //builder.appendQueryParameter("foodItem", foodName );
        //String url = builder.build().toString();

        String url = API_URL_NATURAL_NUTRIENTS;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("query", foodName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-app-id", API_ID);
                headers.put("x-app-key", API_KEY);
                headers.put("Content-Type", "application/json");  // Specify content type as JSON
                return headers;
            }
        };

        requestQueue.add(request);
    }
}
>>>>>>> Stashed changes:app/src/main/java/com/example/virtualchef/NutrionixAPIService.java
