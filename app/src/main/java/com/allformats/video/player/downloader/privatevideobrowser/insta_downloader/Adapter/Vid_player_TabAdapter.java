package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Vid_player_TabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList();
    private final List<String> mFragmentTitleList = new ArrayList();

    public Vid_player_TabAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i) {
        return this.mFragmentList.get(i);
    }

    public void addFragment(Fragment fragment, String str) {
        this.mFragmentList.add(fragment);
        this.mFragmentTitleList.add(str);
    }

    @Override 
    public CharSequence getPageTitle(int i) {
        return this.mFragmentTitleList.get(i);
    }

    @Override 
    public int getCount() {
        return this.mFragmentList.size();
    }
}
