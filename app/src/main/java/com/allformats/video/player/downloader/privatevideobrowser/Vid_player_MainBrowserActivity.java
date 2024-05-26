package com.allformats.video.player.downloader.privatevideobrowser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.privatevideobrowser.historyFragment.Vid_player_VideoHistoryFragmentVidplayer;
import com.allformats.video.player.downloader.privatevideobrowser.webFragment.Vid_player_VideoBrowserManagerFragmentVidplayer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity.Vid_player_InstaMainActivity;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Vid_player_ChatStatusSaverActivity;
import com.allformats.video.player.downloader.view.Vid_player_DS_SearchVidplayerActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_MainBrowserActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    public static Activity activity;
    String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    int MULTIPLE_PERMISSIONS = 100;
    private Uri appLinkData;
    private ImageView btn_search;
    private ImageView btn_search_cancel;
    private FragmentManager manager;
    private BottomNavigationView navView;
    private ActivityCompat.OnRequestPermissionsResultCallback onRequestPermissionsResultCallback;
    private EditText searchTextBar;
    private Vid_player_VideoBrowserManagerFragmentVidplayer vidplayerVideoBrowserManagerFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            navView.getMenu().findItem(R.id.navigation_Settings).setIcon(R.drawable.ic_baseline_music_note_24);
            navView.getMenu().findItem(R.id.navigation_history).setIcon(R.drawable.ic_history_black_24dp);
            navView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.ic_home);
            switch (menuItem.getItemId()) {
                case R.id.navigation_Settings:
                    menuItem.setIcon(R.drawable.selected_videoss);
                    Vid_player_MainBrowserActivity.this.settingsClicked();
                    return true;
                case R.id.navigation_history:
                    menuItem.setIcon(R.drawable.seleted_his);
                    Vid_player_MainBrowserActivity.this.historyClicked();
                    return true;
                case R.id.navigation_home:
                    menuItem.setIcon(R.drawable.selected_homme);
                    Vid_player_MainBrowserActivity.this.homeClicked();
                    return true;
                default:
                    return false;
            }
        }
    };

    public void setUserPrefSettings() {
        Vid_player_DS_Helper.PATH_VIDEO_DOWNLOADER = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "Video Downloader";
        File file = new File(Vid_player_DS_Helper.PATH_VIDEO_DOWNLOADER);
        if (!file.exists()) {
            file.mkdirs();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("seettingPrefMan", 0);
        String string = sharedPreferences.getString("collectionPath", "");
        String string2 = sharedPreferences.getString("starterPage", "");
        String string3 = sharedPreferences.getString("searchManager", "");
        if (string.equals("")) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("collectionPath", Vid_player_DS_Helper.PATH_VIDEO_DOWNLOADER);
            edit.apply();
        }
        if (string2.equals("")) {
            SharedPreferences.Editor edit2 = sharedPreferences.edit();
            edit2.putString("starterPage", getString(R.string.DEFAULT));
            edit2.apply();
        }
        if (string3.equals("")) {
            SharedPreferences.Editor edit3 = sharedPreferences.edit();
            edit3.putString("searchManager", "Google");
            edit3.apply();
        }
    }

    String parse;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_browser_main);
        ((ImageView) findViewById(R.id.btn_home)).setVisibility(View.GONE);
        activity = this;
        parse = getIntent().getStringExtra("downUrl");
        checkPermissions();
        this.appLinkData = getIntent().getData();
        this.manager = getSupportFragmentManager();
        Vid_player_VideoBrowserManagerFragmentVidplayer vidplayerVideoBrowserManagerFragment = (Vid_player_VideoBrowserManagerFragmentVidplayer) getSupportFragmentManager().findFragmentByTag("BM");
        this.vidplayerVideoBrowserManagerFragment = vidplayerVideoBrowserManagerFragment;
        if (vidplayerVideoBrowserManagerFragment == null) {
            FragmentTransaction beginTransaction = this.manager.beginTransaction();
            Vid_player_VideoBrowserManagerFragmentVidplayer vidplayerVideoBrowserManagerFragment2 = new Vid_player_VideoBrowserManagerFragmentVidplayer();
            this.vidplayerVideoBrowserManagerFragment = vidplayerVideoBrowserManagerFragment2;
            beginTransaction.add(vidplayerVideoBrowserManagerFragment2, "BM").commit();
        }
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        this.navView = bottomNavigationView;
        bottomNavigationView.setOnNavigationItemSelectedListener(this.mOnNavigationItemSelectedListener);
        bottomNavigationView.setItemIconTintList(null);
        navView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.selected_homme);
        setUserPrefSettings();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Vid_player_MainBrowserActivity.this.setUpVideoSites();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }, 2000);
        setUPBrowserToolbarView();
        try {
            if (!parse.isEmpty()) {
                this.searchTextBar.setText(parse);
                ((ImageView) findViewById(R.id.btn_home)).setVisibility(View.GONE);
                Vid_player_VideoBrowserManagerFragmentVidplayer vidplayerVideoBrowserManagerFragment2 = new Vid_player_VideoBrowserManagerFragmentVidplayer();
                this.vidplayerVideoBrowserManagerFragment = vidplayerVideoBrowserManagerFragment2;
                new Vid_player_WebConnect(this.searchTextBar, this).connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUPBrowserToolbarView() {
        ImageView imageView = (ImageView) findViewById(R.id.btn_search_cancel);
        this.btn_search_cancel = imageView;
        imageView.setOnClickListener(this);
        this.btn_search = (ImageView) findViewById(R.id.btn_search);
        this.searchTextBar = (EditText) findViewById(R.id.et_search_bar);
        this.searchTextBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().trim().length() == 0) {
                    Vid_player_MainBrowserActivity.this.btn_search_cancel.setVisibility(View.GONE);
                } else {
                    Vid_player_MainBrowserActivity.this.btn_search_cancel.setVisibility(View.VISIBLE);
                }
            }
        });
        this.searchTextBar.setOnEditorActionListener(this);
        this.btn_search.setOnClickListener(this);
        ((ImageView) findViewById(R.id.btn_home)).setOnClickListener(this);
    }

    public void setUpVideoSites() throws IOException {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_Video_SitesList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(new Vid_player_VideoSitesList(this, ((ImageView) findViewById(R.id.btn_home))));
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                ((ImageView) findViewById(R.id.btn_home)).setVisibility(View.GONE);
                this.searchTextBar.getText().clear();
                getVideoBrowserManagerFragment().closeAllWindow();
                return;
            case R.id.btn_search:
                hideKeyboard(this);
                ((ImageView) findViewById(R.id.btn_home)).setVisibility(View.VISIBLE);
                new Vid_player_WebConnect(this.searchTextBar, this).connect();
                return;
            case R.id.btn_search_cancel:
                this.searchTextBar.getText().clear();
                return;
            default:
                return;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 2) {
            return false;
        }
        ((ImageView) findViewById(R.id.btn_home)).setVisibility(View.VISIBLE);
        new Vid_player_WebConnect(this.searchTextBar, this).connect();
        return false;
    }

    @Override
    public void onBackPressed() {
        if (this.manager.findFragmentByTag("VideoDownloadsFragment") != null || this.manager.findFragmentByTag("VideoHistoryFragmentVidplayer") != null || this.manager.findFragmentByTag("WhatsappVideoFragment") != null || this.manager.findFragmentByTag("SettingsActivity") != null) {
            Vid_player_DS_Helper.getInstance().getOnBackPressedListener().onBackpressed();
            this.vidplayerVideoBrowserManagerFragment.resumeCurrentWindow();
            this.navView.setSelectedItemId(R.id.navigation_home);
        } else if (Vid_player_DS_Helper.getInstance().getOnBackPressedListener() != null) {
            ((ImageView) findViewById(R.id.btn_home)).setVisibility(View.GONE);
            Vid_player_DS_Helper.getInstance().getOnBackPressedListener().onBackpressed();
        } else {
            exitActivity();
        }
    }

    public Vid_player_VideoBrowserManagerFragmentVidplayer getVideoBrowserManagerFragment() {
        return this.vidplayerVideoBrowserManagerFragment;
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        Vid_player_DS_Helper.getInstance().setOnBackPressedListener(onBackPressedListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        Uri uri = this.appLinkData;
        if (uri != null) {
            this.vidplayerVideoBrowserManagerFragment.newWindow(uri.toString());
        }
        this.vidplayerVideoBrowserManagerFragment.updateAdFilters();
    }

    public void browserClicked() {
        this.vidplayerVideoBrowserManagerFragment.unhideCurrentWindow();
    }

    public void whatsappClicked() {
        Vid_player_MainBrowserActivity.this.showInterstitial(new Intent(Vid_player_MainBrowserActivity.this, Vid_player_ChatStatusSaverActivity.class));
    }

    public void instaClicked() {
        Vid_player_MainBrowserActivity.this.showInterstitial(new Intent(Vid_player_MainBrowserActivity.this, Vid_player_InstaMainActivity.class));
    }

    public void historyClicked() {
        closeWhatsapp();
        if (this.manager.findFragmentByTag("VideoHistoryFragmentVidplayer") == null) {
            this.vidplayerVideoBrowserManagerFragment.hideCurrentWindow();
            this.vidplayerVideoBrowserManagerFragment.pauseCurrentWindow();
            showInterstitial(new BaseCallback() {
                @Override
                public void completed() {
                    Vid_player_MainBrowserActivity.this.manager.beginTransaction().add(R.id.main_content, new Vid_player_VideoHistoryFragmentVidplayer(), "VideoHistoryFragmentVidplayer").commit();
                }
            });
        }
    }


    public void settingsClicked() {
//        closeDownloads();
        closeHistory();
        closeWhatsapp();
        showInterstitial(new Intent(this, Vid_player_DS_SearchVidplayerActivity.class));
    }

    public void homeClicked() {
        this.vidplayerVideoBrowserManagerFragment.unhideCurrentWindow();
        this.vidplayerVideoBrowserManagerFragment.resumeCurrentWindow();
        navView.getMenu().findItem(R.id.navigation_Settings).setIcon(R.drawable.ic_baseline_music_note_24);
        navView.getMenu().findItem(R.id.navigation_history).setIcon(R.drawable.ic_history_black_24dp);
        navView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.selected_homme);
        closeHistory();
        closeWhatsapp();
    }

    private void closeHistory() {
        Fragment findFragmentByTag = this.manager.findFragmentByTag("VideoHistoryFragmentVidplayer");
        if (findFragmentByTag != null) {
            this.manager.beginTransaction().remove(findFragmentByTag).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((ImageView) findViewById(R.id.btn_home)).setVisibility(View.GONE);
        this.navView = (BottomNavigationView) findViewById(R.id.nav_view);

        if (Vid_player_DS_Helper.aBoolean) {
            navView.getMenu().findItem(R.id.navigation_Settings).setIcon(R.drawable.ic_baseline_music_note_24);
            navView.getMenu().findItem(R.id.navigation_history).setIcon(R.drawable.ic_history_black_24dp);
            navView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.selected_homme);
            Vid_player_DS_Helper.aBoolean = false;
        }
    }

    private void closeWhatsapp() {
        Fragment findFragmentByTag = this.manager.findFragmentByTag("WhatsappVideoFragment");
        if (findFragmentByTag != null) {
            this.manager.beginTransaction().remove(findFragmentByTag).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    public void setOnRequestPermissionsResultListener(ActivityCompat.OnRequestPermissionsResultCallback onRequestPermissionsResultCallback) {
        this.onRequestPermissionsResultCallback = onRequestPermissionsResultCallback;
    }

    private boolean checkPermissions() {
        String[] strArr;
        ArrayList arrayList = new ArrayList();
        for (String str : this.permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), str) != 0) {
                arrayList.add(str);
            }
        }
        if (arrayList.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, (String[]) arrayList.toArray(new String[arrayList.size()]), this.MULTIPLE_PERMISSIONS);
        return false;
    }

    private void exitActivity() {
        super.onBackPressed();
    }

    public interface OnBackPressedListener {
        void onBackpressed();
    }
}
