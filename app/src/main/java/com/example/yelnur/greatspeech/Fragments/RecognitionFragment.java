package com.example.yelnur.greatspeech.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yelnur.greatspeech.Function;
import com.example.yelnur.greatspeech.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecognitionFragment extends Fragment implements RecognitionListener{

    private ViewGroup category, recognition;
    private ListView category_list;
    private ListView question_list;
    private TextView returnedText;
    Button recordbtn;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private ImageButton back_btn;

    private int cnt = 0;
    private String allSpeech = "";

    static final int REQUEST_PERMISSION_KEY = 1;

    private DatabaseReference rootRef;

    final ArrayList<String> ct_list = new ArrayList<String>();
    final ArrayList<String> qt_list = new ArrayList<String>();



    public RecognitionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recognition, container, false);

        rootRef = FirebaseDatabase.getInstance().getReference();
        category = (ViewGroup) view.findViewById(R.id.category);
        recognition = (ViewGroup) view.findViewById(R.id.recognition_start);

        category.setVisibility(View.VISIBLE);
        recognition.setVisibility(View.INVISIBLE);

        category_list = (ListView) view.findViewById(R.id.category_list);

        String[] categories = new String[] {"Образования", "Бизнес"};
        final String[] q1 = new String[] {"Как вам образование нашей страны?", "Нравится ли вам методы обучения?", "Как вы учились?"};
        final String[] q2 = new String[] {"Какая сфера бизнеса вас интересует?", "На какие мастер-классы вы ходите?", "Можете рассказать о каком-то бизнес тренере?"};



        for (int i = 0; i < categories.length; i++){
            ct_list.add(i + 1 + "." + categories[i]);
        }
        final ArrayAdapter ct_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ct_list);
        category_list.setAdapter(ct_adapter);

        question_list = (ListView) view.findViewById(R.id.question_list);
        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category.setVisibility(View.INVISIBLE);
                recognition.setVisibility(View.VISIBLE);
                switch (position){
                    case 0:
                        for (int i = 0; i < q1.length; i++){
                            qt_list.add(i + 1 + "." + q1[i]);
                        }
                        break;
                    case 1:
                        for (int i = 0; i < q2.length; i++){
                            qt_list.add(i + 1 + "." + q2[i]);
                        }
                        break;
                }
                final ArrayAdapter qt_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, qt_list);
                question_list.setAdapter(qt_adapter);

                Log.d("Count", String.valueOf(cnt));

            }
        });


        back_btn = (ImageButton) view.findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setVisibility(View.VISIBLE);
                recognition.setVisibility(View.INVISIBLE);
                qt_list.clear();
                returnedText.setText("");
                cnt = 0;
                recordbtn.setEnabled(true);
            }
        });
        returnedText = (TextView) view.findViewById(R.id.textView1);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        recordbtn = (Button) view.findViewById(R.id.mainButton);

        String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO};
        if(!Function.hasPermissions(getActivity(), PERMISSIONS)){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSION_KEY);
        }


        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getActivity().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        /*
        Minimum time to listen in millis. Here 5 seconds
         */
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
        recognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);




        recordbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View p1)
            {
                progressBar.setVisibility(View.VISIBLE);
                speech.startListening(recognizerIntent);
                question_list.getChildAt(cnt).setBackgroundColor(Color.parseColor("#e6ffe6"));
                if (cnt > 0) {
                    question_list.getChildAt(cnt - 1).setBackgroundColor(Color.TRANSPARENT);
                }
                cnt++;
                recordbtn.setText("Ответить" + "(" + cnt + ")");
                recordbtn.setEnabled(false);

                /*To stop listening
                    progressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                    recordbtn.setEnabled(true);
                 */
            }


        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d("Log", "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("Log", "onBeginningOfSpeech");
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d("Log", "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d("Log", "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.d("Log", "onEndOfSpeech");
        progressBar.setVisibility(View.INVISIBLE);
        recordbtn.setEnabled(true);
    }

    @Override
    public void onError(int error) {
        String errorMessage = getErrorText(error);
        Log.d("Log", "FAILED " + errorMessage);
        progressBar.setVisibility(View.INVISIBLE);
        returnedText.setText(errorMessage);
        recordbtn.setEnabled(true);
    }

    @Override
    public void onResults(Bundle results) {

        Log.d("Log", "onResults");
        String text = returnedText.getText().toString().toLowerCase();
        if (cnt == 1){
            allSpeech += text;
        }else {
            allSpeech += " " + text;
        }
        rootRef.child("user_filler").push().setValue(text.toLowerCase());

        if (cnt == 3){
            recordbtn.setEnabled(false);
            Log.d("AllSpeech", allSpeech);
            String[] splitAllSpeech = allSpeech.split(" ");
            final Map<String, Integer> wordCount = new HashMap<>();
            for (String word: splitAllSpeech){
                if (wordCount.containsKey(word)){
                    wordCount.put(word, wordCount.get(word) + 1);
                }else {
                    wordCount.put(word, 1);
                }
            }

            rootRef.child("popular_filler").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String def_fill = "";
                    String filler = "";
                    int sum = 0;
                    def_fill = dataSnapshot.getValue(String.class);
                    Log.d("default_filler", def_fill);
                    String[] splitDefFill = def_fill.split(",");
                    for (int i = 0; i < splitDefFill.length; i++){
                        Log.d("splitDefFill", splitDefFill[i]);
                    }

                    for (Map.Entry<String, Integer> entry: wordCount.entrySet()){
                        Log.d("Counts","Count of: " + entry.getKey() + " in sentence = " + entry.getValue());
                        sum += entry.getValue();
                        for (int i = 0; i < splitDefFill.length; i++){
                            if (filler == "" && entry.getValue() >= 3 && splitDefFill[i].equals(entry.getKey())){
                                filler += entry.getKey();
                            }else if (filler != "" && entry.getValue() >= 3 && splitDefFill[i].equals(entry.getKey())){
                                filler += ", " + entry.getKey();
                            }
                        }

                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Общее количество слов: " + String.valueOf(sum) + "\n" +
                            "Слова паразиты: " + filler).setCancelable(false).setPositiveButton("Подробнее", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            category.setVisibility(View.VISIBLE);
                            recognition.setVisibility(View.INVISIBLE);
                            qt_list.clear();
                            returnedText.setText("");
                            cnt = 0;

                            recordbtn.setText("Ответить");
                            recordbtn.setEnabled(true);
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    allSpeech = "";
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("DatabaseError", databaseError.toString());
                }
            });


        }


    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d("Log", "onPartialResults");

        ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        text = matches.get(0);
        returnedText.setText(text);

    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d("Log", "onEvent");
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
