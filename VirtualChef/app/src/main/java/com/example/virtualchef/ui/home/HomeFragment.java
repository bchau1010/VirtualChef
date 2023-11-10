package com.example.virtualchef.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.virtualchef.LibraryFragment;
import com.example.virtualchef.ManageFragment;
import com.example.virtualchef.R;
import com.example.virtualchef.SavedFragment;
import com.example.virtualchef.SearchFragment;
import com.example.virtualchef.SettingsFragment;
import com.example.virtualchef.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Button savedButton;
    Button manageButton;
    Button settingsButton;
    Button searchButton;
    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        manageButton = root.findViewById(R.id.manage_button);
        savedButton = root.findViewById(R.id.saved_button);
        settingsButton = root.findViewById(R.id.settings_button);
        searchButton = root.findViewById(R.id.search_button);

        textView = root.findViewById(R.id.text_savedbutton);
        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                //SavedFragment savedFragment = new SavedFragment();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home,savedFragment).commit();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                //transaction.setReorderingAllowed(true);
                transaction.replace(R.id.frame_layout, SavedFragment.class, null);
                transaction.commit();
            }
        });
        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ManageFragment());
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SearchFragment());
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SettingsFragment());
            }
        });
        //replaceFragment(new HomeFragment());



        /*
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                //SavedFragment savedFragment = new SavedFragment();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home,savedFragment).commit();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                //transaction.setReorderingAllowed(true);
                transaction.replace(R.id.text_home, SavedFragment.class, null);
                transaction.commit();
            }
        });

         */




        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText)
        return root;
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}