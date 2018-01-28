package com.example.yelnur.greatspeech.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yelnur.greatspeech.Adapters.CustomAdapter;
import com.example.yelnur.greatspeech.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysingFragment extends Fragment {
    private DatabaseReference databaseReference;

    ViewGroup mainFullInf;
    ViewGroup listGroup;

    private TextView allWordsQnt;
    private TextView fillerWordsQnt;
    private TextView clearWords;
    private ImageButton fullFillerInf;
    private ImageButton back;
    private ListView listView;

    public AnalysingFragment() {
        // Required empty public constructor
    }

    String allSpeech = "";
    int cnt = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysing, container, false);
        mainFullInf = (ViewGroup) view.findViewById(R.id.mainFullInf);
        listGroup = (ViewGroup) view.findViewById(R.id.listGroup);
        mainFullInf.setVisibility(View.VISIBLE);
        listGroup.setVisibility(View.INVISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_filler");
        allWordsQnt = (TextView) view.findViewById(R.id.allWordsQnt);
        fillerWordsQnt = (TextView) view.findViewById(R.id.fillerWordsQnt);
        fullFillerInf = (ImageButton) view.findViewById(R.id.fullFillerInf);
        clearWords = (TextView) view.findViewById(R.id.clearWords);
        listView = (ListView) view.findViewById(R.id.filler_lists);

        back = (ImageButton) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFullInf.setVisibility(View.VISIBLE);
                listGroup.setVisibility(View.INVISIBLE);
            }
        });

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DataSnapshot", dataSnapshot.toString());
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("ds", ds.getValue().toString());
                    if (cnt > 0){
                        allSpeech += " " + ds.getValue().toString();
                    }else {
                        allSpeech += ds.getValue().toString();
                    }
                    cnt++;
                }
                Log.d("allSpeech", allSpeech);
                String[] splitAll = allSpeech.split(" ");
                final Map<String, Integer> wordCount = new HashMap<>();
                for (String word: splitAll){
                    if (wordCount.containsKey(word)){
                        wordCount.put(word, wordCount.get(word) + 1);
                    }else {
                        wordCount.put(word, 1);
                    }
                }
                int sum = 0;
                String[] filler = new String[wordCount.size()];
                for (Map.Entry<String, Integer> entry: wordCount.entrySet()){
                    Log.d("Counts","Count of: " + entry.getKey() + " in sentence = " + entry.getValue());
                    sum += entry.getValue();
                }
                Log.d("AllWordsQNT", String.valueOf(sum));
                allWordsQnt.setText(String.valueOf(sum));
                int k = 0;
                int[] fillerQnt = new int[wordCount.size()];
                for (Map.Entry<String, Integer> entry: wordCount.entrySet()){
                    if (sum / entry.getValue() <= 4){
                        filler[k] = entry.getKey();
                        fillerQnt[k] = entry.getValue();
                        k++;
                    }
                }
                fillerWordsQnt.setText(String.valueOf(k));
                float sumFiller = 0;
                CustomAdapter adapter = new CustomAdapter(getActivity(), filler, fillerQnt);
                listView.setAdapter(adapter);
                fullFillerInf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainFullInf.setVisibility(View.INVISIBLE);
                        listGroup.setVisibility(View.VISIBLE);
                    }
                });


                for (int i = 0; i < k; i++){
                    sumFiller += fillerQnt[i];
                }
                clearWords.setText(100 - sumFiller*100/sum + "%");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(eventListener);


        return view;
    }

}
