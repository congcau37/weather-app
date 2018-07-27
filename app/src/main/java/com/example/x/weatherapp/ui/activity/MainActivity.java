package com.example.x.weatherapp.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.x.weatherapp.R;
import com.example.x.weatherapp.ui.fragment.HomeFragment;
//4715ffc62d8574f6276c43801c8988e7
public class MainActivity extends AppCompatActivity {
    private HomeFragment homeFragment;
    public static final String TAG = "HOME_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag
            homeFragment = (HomeFragment)
                    getSupportFragmentManager().findFragmentByTag(TAG);
        } else if (homeFragment == null) {
            // only create fragment if they haven't been instantiated already
            homeFragment = new HomeFragment();
        }
        if (!homeFragment.isInLayout()) {
            nextFragment(R.id.myLayout, homeFragment, TAG);
        }
    }

    public void nextFragment(int id, Fragment fragment, String TAG) {
        String name = fragment.getClass().getName();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, TAG);
        ft.addToBackStack(name);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
