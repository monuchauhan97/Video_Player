package com.allformats.video.player.downloader.privatevideobrowser.webFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.Vid_player_DS_BrowserFragment;
import com.allformats.video.player.downloader.privatevideobrowser.Vid_player_MainBrowserActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Vid_player_VideoBrowserManagerFragmentVidplayer extends Vid_player_DS_BrowserFragment {
    private List<Vid_player_VideoBrowserWindowFragmentVidplayer> windows;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        String str = "debug";
        Log.d(str, "Browser Manager added");
        this.windows = new ArrayList();
        File file = new File(getActivity().getFilesDir(), "ad_filters.dat");
        try {
            if (file.exists()) {
                Log.d(str, "file exists");
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                objectInputStream.close();
                fileInputStream.close();
            } else {
                Log.d(str, "file not exists");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.close();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newWindow(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("url", str);
        Fragment videoBrowserWindowFragment = new Vid_player_VideoBrowserWindowFragmentVidplayer();
        videoBrowserWindowFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add((int) R.id.home_content, videoBrowserWindowFragment, null).commit();
        this.windows.add((Vid_player_VideoBrowserWindowFragmentVidplayer) videoBrowserWindowFragment);
        getVDActivity().setOnBackPressedListener((Vid_player_MainBrowserActivity.OnBackPressedListener) videoBrowserWindowFragment);
        if (this.windows.size() > 1) {
            List list = this.windows;
            Vid_player_VideoBrowserWindowFragmentVidplayer vidplayerVideoBrowserWindowFragment2 = (Vid_player_VideoBrowserWindowFragmentVidplayer) list.get(list.size() - 2);
            if (vidplayerVideoBrowserWindowFragment2 != null && vidplayerVideoBrowserWindowFragment2.getView() != null) {
                vidplayerVideoBrowserWindowFragment2.getView().setVisibility(View.GONE);
                vidplayerVideoBrowserWindowFragment2.onPause();
            }
        }
    }

    public void closeWindow(Vid_player_VideoBrowserWindowFragmentVidplayer vidplayerVideoBrowserWindowFragment) {
        EditText editText = (EditText) getVDActivity().findViewById(R.id.et_search_bar);
        this.windows.remove(vidplayerVideoBrowserWindowFragment);
        getFragmentManager().beginTransaction().remove(vidplayerVideoBrowserWindowFragment).commit();
        if (this.windows.size() > 0) {
            List list = this.windows;
            vidplayerVideoBrowserWindowFragment = (Vid_player_VideoBrowserWindowFragmentVidplayer) list.get(list.size() - 1);
            if (!(vidplayerVideoBrowserWindowFragment == null || vidplayerVideoBrowserWindowFragment.getView() == null)) {
                vidplayerVideoBrowserWindowFragment.onResume();
                vidplayerVideoBrowserWindowFragment.getView().setVisibility(View.VISIBLE);
            }
            editText.setText(vidplayerVideoBrowserWindowFragment.getUrl());
            editText.setSelection(editText.getText().length());
            getVDActivity().setOnBackPressedListener(vidplayerVideoBrowserWindowFragment);
            return;
        }
        editText.getText().clear();
        getVDActivity().setOnBackPressedListener(null);
    }

    public void closeAllWindow() {
        if (this.windows.size() > 0) {
            Iterator it = this.windows.iterator();
            while (it.hasNext()) {
                getFragmentManager().beginTransaction().remove((Vid_player_VideoBrowserWindowFragmentVidplayer) it.next()).commit();
                it.remove();
            }
            getVDActivity().setOnBackPressedListener(null);
            return;
        }
        getVDActivity().setOnBackPressedListener(null);
    }

    public void hideCurrentWindow() {
        if (this.windows.size() > 0) {
            List list = this.windows;
            Vid_player_VideoBrowserWindowFragmentVidplayer vidplayerVideoBrowserWindowFragment = (Vid_player_VideoBrowserWindowFragmentVidplayer) list.get(list.size() - 1);
            if (vidplayerVideoBrowserWindowFragment.getView() != null) {
                vidplayerVideoBrowserWindowFragment.getView().setVisibility(View.GONE);
            }
        }
    }

    public void unhideCurrentWindow() {
        if (this.windows.size() > 0) {
            List list = this.windows;
            Vid_player_VideoBrowserWindowFragmentVidplayer vidplayerVideoBrowserWindowFragment = (Vid_player_VideoBrowserWindowFragmentVidplayer) list.get(list.size() - 1);
            if (vidplayerVideoBrowserWindowFragment.getView() != null) {
                vidplayerVideoBrowserWindowFragment.getView().setVisibility(View.VISIBLE);
                getVDActivity().setOnBackPressedListener(vidplayerVideoBrowserWindowFragment);
                return;
            }
            return;
        }
        getVDActivity().setOnBackPressedListener(null);
    }

    public void pauseCurrentWindow() {
        if (this.windows.size() > 0) {
            List list = this.windows;
            Vid_player_VideoBrowserWindowFragmentVidplayer vidplayerVideoBrowserWindowFragment = (Vid_player_VideoBrowserWindowFragmentVidplayer) list.get(list.size() - 1);
            if (vidplayerVideoBrowserWindowFragment.getView() != null) {
                vidplayerVideoBrowserWindowFragment.onPause();
            }
        }
    }

    public void resumeCurrentWindow() {
        if (this.windows.size() > 0) {

            List list = this.windows;
            Vid_player_VideoBrowserWindowFragmentVidplayer vidplayerVideoBrowserWindowFragment = (Vid_player_VideoBrowserWindowFragmentVidplayer) list.get(list.size() - 1);
            if (vidplayerVideoBrowserWindowFragment.getView() != null) {
                vidplayerVideoBrowserWindowFragment.onResume();
                getVDActivity().setOnBackPressedListener(vidplayerVideoBrowserWindowFragment);
                return;
            }
            return;
        }
        getVDActivity().setOnBackPressedListener(null);
    }

    public void updateAdFilters() {
    }

    public boolean checkUrlIfAds(String str) {

        return false;
    }
}
