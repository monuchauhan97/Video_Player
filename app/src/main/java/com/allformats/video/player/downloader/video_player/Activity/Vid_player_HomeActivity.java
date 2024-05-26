package com.allformats.video.player.downloader.video_player.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.Vid_player_MainBrowserActivity;
import com.allformats.video.player.downloader.video_player.Adapter.Vid_player_HomeAdapter;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_MyEvent;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_HomeActivity extends BaseActivity implements View.OnClickListener {
    public static Activity activity;
    public ViewPager viewpager;
    int pos = 0;
    private ImageView refresh;
    private TabLayout tablay;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_home);
        activity = this;

        initView();
        initListener();
        initTab();
    }

    private void initTab() {
        TabLayout tabLayout = this.tablay;
        tablay.addTab(tabLayout.newTab().setIcon(R.drawable.video_home));
        TabLayout tabLayout2 = this.tablay;
        tabLayout2.addTab(tabLayout2.newTab().setIcon(R.drawable.video_all));
        TabLayout tabLayout3 = this.tablay;
        tabLayout3.addTab(tabLayout3.newTab().setIcon(R.drawable.video_history));
        TabLayout tabLayout4 = this.tablay;
        tabLayout4.addTab(tabLayout4.newTab().setIcon(R.drawable.ic_ds_incognito));
        this.tablay.setBackgroundResource(R.drawable.tab_bg);
        this.tablay.setTabGravity(0);
        this.viewpager.setAdapter(new Vid_player_HomeAdapter( getSupportFragmentManager(), this.tablay.getTabCount()));
        this.tablay.setTabIconTint(null);
        tablay.getTabAt(tablay.getSelectedTabPosition()).setIcon(R.drawable.folder_selected);
        this.viewpager.setOffscreenPageLimit(4);
        this.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tablay));
        this.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                Vid_player_HomeActivity.this.pos = i;

            }
        });
        this.tablay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tablay.getTabAt(tablay.getSelectedTabPosition()).setIcon(R.drawable.folder_selected);
                } else if (tab.getPosition() == 1) {
                    tablay.getTabAt(tablay.getSelectedTabPosition()).setIcon(R.drawable.video_selected);
                } else if (tab.getPosition() == 2) {
                    tablay.getTabAt(tablay.getSelectedTabPosition()).setIcon(R.drawable.history_selected);
                } else {
                    tablay.getTabAt(tablay.getSelectedTabPosition()).getIcon().setTint(getResources().getColor(R.color.purple_500));
                }

                if (tab.getPosition() == 3) {
                    startActivity(new Intent(Vid_player_HomeActivity.this, Vid_player_MainBrowserActivity.class));
                }

                showInterstitial(new BaseCallback() {
                    @Override
                    public void completed() {
                        Vid_player_HomeActivity.this.viewpager.setCurrentItem(tab.getPosition());
                        Vid_player_HomeActivity.this.pos = tab.getPosition();
                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tablay.getTabAt(tab.getPosition()).setIcon(R.drawable.video_home);
                } else if (tab.getPosition() == 1) {
                    tablay.getTabAt(tab.getPosition()).setIcon(R.drawable.video_all);
                } else if (tab.getPosition() == 2) {
                    tablay.getTabAt(tab.getPosition()).setIcon(R.drawable.video_history);
                } else {
                    tablay.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_ds_incognito);
                }
            }
        });
    }

    private void initListener() {
        this.refresh.setOnClickListener(this);
    }

    private void initView() {
        this.refresh = (ImageView) findViewById(R.id.refresh);
        this.tablay = (TabLayout) findViewById(R.id.tab_lay);
        this.viewpager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.refresh) {
            Vid_player_MyEvent eventBus = new Vid_player_MyEvent();
            eventBus.setType(1);
            eventBus.setValue(0);
            org.greenrobot.eventbus.EventBus.getDefault().post(eventBus);
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 100) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                String str = "";
                for (String str2 : strArr) {
                    str = str + "\n" + str2;
                }
                Toast.makeText(this, "Please Allow Permission First", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            Vid_player_MyEvent eventBus = new Vid_player_MyEvent();
            eventBus.setType(1);
            eventBus.setValue(0);
            org.greenrobot.eventbus.EventBus.getDefault().post(eventBus);
        }
    }

    @Override
    public void onBackPressed() {
        if (AdsUtility.config.adOnBack) {
            if (AdsUtility.startScreenCount <= 0) {
                showThankYouSheet();
            } else {
                showInterstitial(new BaseCallback() {
                    @Override
                    public void completed() {
                        Vid_player_HomeActivity.super.onBackPressed();
                    }
                });
            }
        } else {
            if (AdsUtility.startScreenCount <= 0) {
                showThankYouSheet();
                return;
            }
            Vid_player_HomeActivity.super.onBackPressed();
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        int tabpos = this.tablay.getSelectedTabPosition();
        if (tabpos == 3) {
            Vid_player_HomeActivity.this.viewpager.setCurrentItem(0);
        }
        Vid_player_MyEvent eventBus = new Vid_player_MyEvent();
        eventBus.setType(1);
        eventBus.setValue(0);
        org.greenrobot.eventbus.EventBus.getDefault().post(eventBus);
    }

    public void myStartActivity(final Intent intent) {
        Vid_player_HomeActivity.this.showInterstitial(intent);
    }
}
