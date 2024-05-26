package com.allformats.video.player.downloader.privatevideobrowser;

import androidx.fragment.app.Fragment;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;


public class Vid_player_DS_BrowserFragment extends Fragment {
    public Vid_player_MainBrowserActivity getVDActivity() {
        return (Vid_player_MainBrowserActivity) getActivity();
    }

    public Vid_player_DS_Helper getVDApp() {
        return (Vid_player_DS_Helper) getActivity().getApplication();
    }
}
