package com.example.yelnur.greatspeech.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yelnur.greatspeech.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysingFragment extends Fragment {


    public AnalysingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysing, container, false);
        return view;
    }

}
