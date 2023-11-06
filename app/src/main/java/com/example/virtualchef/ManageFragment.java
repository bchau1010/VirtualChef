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

}