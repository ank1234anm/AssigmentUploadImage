package com.example.ankit.assigment.adapter;

import com.example.ankit.assigment.fragment.UploadedList;
import com.example.ankit.assigment.fragment.UploadImage;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ScreenAdapter extends FragmentStatePagerAdapter {
String [] screenList ;
    public ScreenAdapter(FragmentManager fm, String [] screenList) {
        super(fm);
        this.screenList = screenList ;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                UploadImage uploadImage = new UploadImage();
                return uploadImage;

            case 1:
                UploadedList uploadedList = new UploadedList();
                return uploadedList;

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return screenList[position];
    }


    @Override
    public int getCount() {
        return screenList.length ;
    }

}
