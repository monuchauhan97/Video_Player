package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Adapter.Vid_player_TabAdapter;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Fragment.Vid_player_DownloadFragment;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Fragment.Vid_player_HomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_InstaMainActivity extends BaseActivity {
    ImageView need_help;
    TabLayout tabLayout;
    String[] tabs;
    ViewPager viewPager;
    Vid_player_TabAdapter tabadapter = new Vid_player_TabAdapter(getSupportFragmentManager());
    ArrayList<String> downloaded_list = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_insta_main);
        findid();
        init();
        onclick();

        ((ImageView) findViewById(R.id.img_inta_sponsored)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Vid_player_InstaMainActivity instaMainActivity = Vid_player_InstaMainActivity.this;
                    instaMainActivity.startActivity(instaMainActivity.getPackageManager().getLaunchIntentForPackage("com.instagram.android"));
                } catch (Exception unused) {
                    Vid_player_InstaMainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.instagram.android")));
                }
            }
        });
    }

    private void findid() {
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.tabLayout = (TabLayout) findViewById(R.id.tablayout);
        this.need_help = (ImageView) findViewById(R.id.need_help);
    }

    private void init() {
        downloaded_list.clear();
        getimagesfrompath();
        getvideosfrompath();
        this.tabadapter.addFragment(new Vid_player_HomeFragment(this), "");
        this.tabadapter.addFragment(new Vid_player_DownloadFragment(this.downloaded_list, this), "");
        this.viewPager.setAdapter(this.tabadapter);
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.tabadapter.notifyDataSetChanged();
        this.viewPager.setAdapter(this.tabadapter);
        String[] strArr = {"Home", "Downloads"};
        this.tabs = strArr;
        this.tabadapter.notifyDataSetChanged();
        this.viewPager.setAdapter(this.tabadapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        this.tabLayout = tabLayout;
        tabLayout.setupWithViewPager(this.viewPager);
        for (int i = 0; i < this.tabLayout.getTabCount(); i++) {
            this.tabLayout.getTabAt(i).setCustomView(getTabViewUn(i));
        }
        setupTabIcons();
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showInterstitial(new BaseCallback() {
                    @Override
                    public void completed() {
                        Vid_player_InstaMainActivity.this.viewPager.setCurrentItem(tab.getPosition());
                        TabLayout.Tab tabAt = Vid_player_InstaMainActivity.this.tabLayout.getTabAt(tab.getPosition());
                        tabAt.setCustomView((View) null);
                        tabAt.setCustomView(Vid_player_InstaMainActivity.this.getTabView(tab.getPosition()));
                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabLayout.Tab tabAt = Vid_player_InstaMainActivity.this.tabLayout.getTabAt(tab.getPosition());
                tabAt.setCustomView((View) null);
                tabAt.setCustomView(Vid_player_InstaMainActivity.this.getTabViewUn(tab.getPosition()));
            }
        });
    }

    private void onclick() {
        this.need_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_InstaMainActivity.this.tabadapter.notifyDataSetChanged();
                Vid_player_InstaMainActivity.this.viewPager.setAdapter(Vid_player_InstaMainActivity.this.tabadapter);
            }
        });
    }

    public void getimagesfrompath() {
        this.downloaded_list.clear();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "YoInsta_");
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            Log.i("TAG", "getimagesfrompath: ");
            for (File file2 : listFiles) {
                this.downloaded_list.add(String.valueOf(file2));
            }
        }
    }

    public void getvideosfrompath() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "YoInsta_");
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            Log.i("TAG", "getimagesfrompath: ");
            for (File file2 : listFiles) {
                this.downloaded_list.add(String.valueOf(file2));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        downloaded_list.clear();
        getimagesfrompath();
        getvideosfrompath();
    }

    private void setupTabIcons() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.vid_player_custom_tab, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.tab);
        textView.setText(this.tabs[0]);
        textView.setTextColor(getResources().getColor(R.color.tab_txt_press));
        ImageView View = (ImageView) inflate.findViewById(R.id.select_notation);
        View.setBackgroundResource(R.drawable.thisds);
        textView.setLayoutParams(new FrameLayout.LayoutParams((getResources().getDisplayMetrics().widthPixels * 400) / 1080, (getResources().getDisplayMetrics().heightPixels * 100) / 1920));
        TabLayout.Tab tabAt = this.tabLayout.getTabAt(0);
        tabAt.setCustomView((View) null);
        tabAt.setCustomView(inflate);
    }

    public View getTabView(int i) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.vid_player_custom_tab, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.tab);
        textView.setText(this.tabs[i]);
        textView.setTextColor(getResources().getColor(R.color.tab_txt_press));
        ImageView View = (ImageView) inflate.findViewById(R.id.select_notation);
        View.setBackgroundResource(R.drawable.thisds);
        textView.setLayoutParams(new FrameLayout.LayoutParams((getResources().getDisplayMetrics().widthPixels * 400) / 1080, (getResources().getDisplayMetrics().heightPixels * 100) / 1920));
        return inflate;
    }

    public View getTabViewUn(int i) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.vid_player_custom_tab, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.tab);
        textView.setText(this.tabs[i]);
        textView.setTextColor(getResources().getColor(R.color.white));
        ImageView View = (ImageView) inflate.findViewById(R.id.select_notation);
        View.setBackgroundResource(android.R.color.transparent);
        textView.setLayoutParams(new FrameLayout.LayoutParams((getResources().getDisplayMetrics().widthPixels * 400) / 1080, (getResources().getDisplayMetrics().heightPixels * 100) / 1920));
        return inflate;
    }

    public void setPress(ImageView imageView, TextView textView, int i) {
        imageView.setColorFilter(ContextCompat.getColor(this, i), PorterDuff.Mode.SRC_IN);
        textView.setTextColor(getResources().getColor(i));
    }

    @Override
    public void onBackPressed() {
        if (AdsUtility.config.adOnBack) {
            showInterstitial(new BaseCallback() {
                @Override
                public void completed() {
                    Vid_player_InstaMainActivity.super.onBackPressed();
                }
            });
        } else {
            Vid_player_InstaMainActivity.super.onBackPressed();
        }
    }
}
