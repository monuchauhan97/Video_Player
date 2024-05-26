package com.allformats.video.player.downloader.video_player.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.allformats.video.player.downloader.video_player.Fragment.Vid_player_FolderFragment;
import com.allformats.video.player.downloader.video_player.Fragment.Vid_player_RecentFragment;
import com.allformats.video.player.downloader.video_player.Fragment.Vid_player_VideoFragment;

public class Vid_player_HomeAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public Vid_player_HomeAdapter(FragmentManager fragmentManager, int i) {
        super(fragmentManager);
        this.totalTabs = i;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new Vid_player_FolderFragment();
        }
        if (i == 1) {
            return new Vid_player_VideoFragment();
        }
        if (i == 2) {
            return new Vid_player_RecentFragment();
        }
        if (i == 3) {
            return new Fragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.totalTabs;
    }
}
