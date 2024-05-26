package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Fragment.Vid_player_RecentWapp;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Fragment.Vid_player_RecentWappBus;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.util.Vid_player_SharedPrefs;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.util.Vid_player_Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_ChatStatusSaverActivity extends BaseActivity {
    Dialog dialog;
    TabLayout tabLayout;
    String[] tabs;
    ViewPager viewPager;
    ImageView whatsIV;
    boolean isOpenWapp = false;
    boolean isOpenWbApp = false;
    private ViewPagerAdapter adapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Vid_player_Utils.setLanguage(this, Vid_player_SharedPrefs.getLang(this));
        setContentView(R.layout.vid_player_activity_gbchat_status_saver);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.viewPager = viewPager;
        setupViewPager(viewPager);
        String[] strArr = new String[3];
        this.tabs = strArr;
        strArr[0] = getResources().getString(R.string.wapp);
        this.tabs[1] = getResources().getString(R.string.wbapp);
        this.tabs[2] = "Downloads";
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
                Vid_player_ChatStatusSaverActivity.this.viewPager.setCurrentItem(tab.getPosition());
                TabLayout.Tab tabAt = Vid_player_ChatStatusSaverActivity.this.tabLayout.getTabAt(tab.getPosition());
                tabAt.setCustomView((View) null);
                tabAt.setCustomView(Vid_player_ChatStatusSaverActivity.this.getTabView(tab.getPosition()));
                if (tab.getPosition() == 0 && Vid_player_ChatStatusSaverActivity.this.isOpenWapp) {
                    Vid_player_ChatStatusSaverActivity.this.isOpenWapp = false;
                    if (!Vid_player_SharedPrefs.getWATree(Vid_player_ChatStatusSaverActivity.this).equals("")) {
                        FragmentManager supportFragmentManager = Vid_player_ChatStatusSaverActivity.this.getSupportFragmentManager();
                        ((Vid_player_RecentWapp) supportFragmentManager.findFragmentByTag("android:switcher:" + Vid_player_ChatStatusSaverActivity.this.viewPager.getId() + ":" + tab.getPosition())).populateGrid();
                    }
                }
                if (tab.getPosition() == 1 && Vid_player_ChatStatusSaverActivity.this.isOpenWbApp) {
                    Vid_player_ChatStatusSaverActivity.this.isOpenWbApp = false;
                    if (!Vid_player_SharedPrefs.getWBTree(Vid_player_ChatStatusSaverActivity.this).equals("")) {
                        FragmentManager supportFragmentManager2 = Vid_player_ChatStatusSaverActivity.this.getSupportFragmentManager();
                        ((Vid_player_RecentWappBus) supportFragmentManager2.findFragmentByTag("android:switcher:" + Vid_player_ChatStatusSaverActivity.this.viewPager.getId() + ":" + tab.getPosition())).populateGrid();
                    }
                }
                if (tab.getPosition() == 2 && Vid_player_ChatStatusSaverActivity.this.isOpenWbApp) {
                    Vid_player_ChatStatusSaverActivity.this.isOpenWbApp = false;
                    if (!Vid_player_SharedPrefs.getWBTree(Vid_player_ChatStatusSaverActivity.this).equals("")) {
                        FragmentManager supportFragmentManager2 = Vid_player_ChatStatusSaverActivity.this.getSupportFragmentManager();
                        ((Vid_player_RecentWappBus) supportFragmentManager2.findFragmentByTag("android:switcher:" + Vid_player_ChatStatusSaverActivity.this.viewPager.getId() + ":" + tab.getPosition())).populateGrid();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                showInterstitial(new BaseCallback() {
                    @Override
                    public void completed() {
                        TabLayout.Tab tabAt = Vid_player_ChatStatusSaverActivity.this.tabLayout.getTabAt(tab.getPosition());
                        tabAt.setCustomView((View) null);
                        tabAt.setCustomView(Vid_player_ChatStatusSaverActivity.this.getTabViewUn(tab.getPosition()));
                    }
                });
            }
        });
        wAppAlert();
        ImageView imageView = (ImageView) findViewById(R.id.img_whatsa_sponsored);
        this.whatsIV = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_ChatStatusSaverActivity.this.dialog.show();
            }
        });


    }



    public void wAppAlert() {
        BottomSheetDialog  dialog = new BottomSheetDialog(this,R.style.CustomBottomSheetDialogTheme);
        this.dialog = dialog;
        dialog.setContentView(R.layout.vid_player_popup_lay);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        ((RelativeLayout) this.dialog.findViewById(R.id.btn_wapp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_ChatStatusSaverActivity.this.lambda$wAppAlert$2$StatusSaverActivity(view);
            }
        });
        ((RelativeLayout) this.dialog.findViewById(R.id.btn_wapp_bus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_ChatStatusSaverActivity.this.lambda$wAppAlert$3$StatusSaverActivity(view);
            }
        });
    }

    public void lambda$wAppAlert$2$StatusSaverActivity(View view) {
        try {
            this.isOpenWapp = true;
            startActivity(getPackageManager().getLaunchIntentForPackage("com.whatsapp"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please Install WhatsApp For Download Status!!!!!", Toast.LENGTH_LONG).show();
        }
        this.dialog.dismiss();
    }

    public void lambda$wAppAlert$3$StatusSaverActivity(View view) {
        try {
            this.isOpenWbApp = true;
            startActivity(getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please Install WhatsApp Business For Download Status!!!!!", Toast.LENGTH_LONG).show();
        }
        this.dialog.dismiss();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.adapter = viewPagerAdapter;
        viewPagerAdapter.addFragment(new Vid_player_RecentWapp(), "Whatsapp");
        this.adapter.addFragment(new Vid_player_RecentWappBus(), "WA Business");
        this.adapter.addFragment(new Vid_player_WhatsappDownload(this), "Downloads");
        viewPager.setAdapter(this.adapter);
    }

    private void setupTabIcons() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.vid_player_custom_tab, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.tab);
        textView.setText(this.tabs[0]);
        textView.setTextColor(getResources().getColor(R.color.tab_txt_press));
        ImageView View = (ImageView) inflate.findViewById(R.id.select_notation);
        View.setBackgroundResource(R.drawable.thisds);
        textView.setLayoutParams(new FrameLayout.LayoutParams((getResources().getDisplayMetrics().widthPixels * 320) / 1080, (getResources().getDisplayMetrics().heightPixels * 100) / 1920));
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
        textView.setLayoutParams(new FrameLayout.LayoutParams((getResources().getDisplayMetrics().widthPixels * 320) / 1080, (getResources().getDisplayMetrics().heightPixels * 100) / 1920));
        return inflate;
    }

    public View getTabViewUn(int i) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.vid_player_custom_tab, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.tab);
        textView.setText(this.tabs[i]);
        textView.setTextColor(getResources().getColor(R.color.white));
        ImageView View = (ImageView) inflate.findViewById(R.id.select_notation);
        View.setBackgroundResource(0);
//        textView.setBackgroundResource(R.drawable.insta_unpress_tab);
        textView.setLayoutParams(new FrameLayout.LayoutParams((getResources().getDisplayMetrics().widthPixels * 320) / 1080, (getResources().getDisplayMetrics().heightPixels * 100) / 1920));
        return inflate;
    }

    @Override
    public void onBackPressed() {
        if (AdsUtility.config.adOnBack) {
            showInterstitial(new BaseCallback() {
                @Override
                public void completed() {
                    Vid_player_ChatStatusSaverActivity.super.onBackPressed();
                }
            });
        } else {
            Vid_player_ChatStatusSaverActivity.super.onBackPressed();
        }
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();
        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            return this.mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String str) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(str);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return this.mFragmentTitleList.get(i);
        }
    }
}
