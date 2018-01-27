package com.example.yelnur.greatspeech;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.yelnur.greatspeech.Adapters.ViewPagerAdapter;
import com.example.yelnur.greatspeech.Fragments.AnalysingFragment;
import com.example.yelnur.greatspeech.Fragments.RecognitionFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private AnalysingFragment analysingFragment;
    private RecognitionFragment recognitionFragment;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.performClick();
        setupViewPager(viewPager);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_recognition:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_analysing:
                                viewPager.setCurrentItem(1);
                                break;
                        }
                        return true;
                    }
                });
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        recognitionFragment = new RecognitionFragment();
        analysingFragment = new AnalysingFragment();
        adapter.addFragment(recognitionFragment);
        adapter.addFragment(analysingFragment);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);


    }
}
