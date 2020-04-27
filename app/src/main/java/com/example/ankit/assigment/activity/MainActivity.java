package com.example.ankit.assigment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.ankit.assigment.R;
import com.example.ankit.assigment.adapter.ScreenAdapter;
import com.example.ankit.assigment.fragment.UploadedList;
import com.example.ankit.assigment.fragment.UploadImage;
import com.example.ankit.assigment.utilities.CustomTabLayout;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements UploadImage.OnFragmentInteractionListener, UploadedList.OnFragmentInteractionListener {
    private TabLayout tabs;
    private RelativeLayout rlCard;
    private ViewPager viewPager;
    private String[] screenName = {"Image Upload", "Uploaded Image"};

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2020-02-16 13:49:07 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setTab();
        setListner();

    }

    private void setTab() {

        tabs.setSelectedTabIndicatorColor(Color.parseColor("#3cb9aa"));
        tabs.setTabTextColors(Color.parseColor("#1d1e1f"), Color.parseColor("#FFFFFF"));
    }

    private void setListner() {
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() ==0) {
                    replaceFragment(UploadImage.newInstance());
                }else {
                    replaceFragment(UploadedList.newInstance());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.rlCard, fragment);
        transaction.commitAllowingStateLoss();
    }



    private void findViews() {
        tabs = (CustomTabLayout) findViewById(R.id.tabs);
        rlCard = (RelativeLayout) findViewById(R.id.rlCard);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ScreenAdapter screenAdapter = new ScreenAdapter(getSupportFragmentManager(), screenName);
        viewPager.setAdapter(screenAdapter);
        tabs.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
