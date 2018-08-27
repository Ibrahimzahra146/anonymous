package com.example.rabee.breath.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.rabee.breath.fragments.HomeFragment;
import com.example.rabee.breath.fragments.NotificationFragment;
import com.example.rabee.breath.fragments.SettingsFragment;

import java.util.ArrayList;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    static ArrayList<Fragment> list = new ArrayList<Fragment>();

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (list.size() < 1) {
                    HomeFragment homeFragment = new HomeFragment();
                    list.add(0, homeFragment);
                    return list.get(0);
                } else {
                    Log.d("Size ",list.size()+"");
                    return list.get(0);
                }

            case 1:
                if (list.size() < 2 && list.size() > 0) {
                    NotificationFragment notificationFragment = new NotificationFragment();
                    list.add(1, notificationFragment);
                    return list.get(1);
                } else return list.get(1);
            case 2:
                if (list.size() < 3 && list.size() > 1) {
                    SettingsFragment settingsFragment = new SettingsFragment();
                    list.add(2, settingsFragment);
                    return list.get(2);
                } else return list.get(2);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
