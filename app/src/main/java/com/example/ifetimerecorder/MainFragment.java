package com.example.ifetimerecorder;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    Button addRecBtn;
    Button checkCarBtn;
    Button addTrainBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        final Activity activity = getActivity();
        addRecBtn = view.findViewById(R.id.add_rec_btn);
        checkCarBtn = view.findViewById(R.id.check_car_btn);
        addTrainBtn = view.findViewById(R.id.add_train_btn);

        addRecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillFragment fillFragment = new FillFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fillFragment, "Fill_Fragment")
                        .setReorderingAllowed(true)
                        .addToBackStack("Main")
                        .commit();
            }
        });

        checkCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckCarFragment checkCarFragment = new CheckCarFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, checkCarFragment, "Check_Car_Fragment")
                        .setReorderingAllowed(true)
                        .addToBackStack("Main")
                        .commit();
            }
        });

        addTrainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCarFragment addCarFragment = new AddCarFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addCarFragment, "Add_Car_Fragment")
                        .setReorderingAllowed(true)
                        .addToBackStack("Main")
                        .commit();
            }
        });

        return view;
    }
}