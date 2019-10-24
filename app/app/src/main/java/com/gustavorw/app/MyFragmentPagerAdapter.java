package com.gustavorw.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gustavorw.app.Fragments.LiveFragment;
import com.gustavorw.app.Fragments.NewsFragment;

public class MyFragmentPagerAdapter  extends FragmentPagerAdapter {
    private String[] arrayTab;
    public MyFragmentPagerAdapter(@NonNull FragmentManager fm, String[] arrayTab) {
        super(fm);
        this.arrayTab = arrayTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LiveFragment liveFragment = new LiveFragment();
                return liveFragment;
            case 1:
                NewsFragment newsFragment = new NewsFragment();
                return newsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.arrayTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.arrayTab[position];
    }
}
