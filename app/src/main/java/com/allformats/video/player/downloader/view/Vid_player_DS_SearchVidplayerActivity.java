package com.allformats.video.player.downloader.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.Vid_player_ListHelper;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_DS_SearchHistory;
import com.allformats.video.player.downloader.ds_tube_android_util.Vid_player_ConnectUtil;
import com.allformats.video.player.downloader.ds_tube_android_util.Vid_player_IntentUtil;
import com.allformats.video.player.downloader.ds_tube_android_util.classes.Vid_player_ActivityCompat;
import com.allformats.video.player.downloader.ds_tube_android_util.classes.Vid_player_ToastCompat;
import com.allformats.video.player.downloader.ds_tube_android_util.view.Vid_player_DS_SearchEditText;
import com.allformats.video.player.downloader.ds_tube_android_util.view.Vid_player_ViewPopupMenu;
import com.allformats.video.player.downloader.ds_tube_android_util.view.Vid_player_WaitVidplayerDialog;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_AudioItem;
import com.allformats.video.player.downloader.ds_tube_connect.Vid_player_SearchConnector;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.schabi.newpipe.extractor.stream_info.StreamInfo;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_DS_SearchVidplayerActivity extends Vid_player_ActivityCompat {
    private static final int RW_PERMISSIONS_REQUEST_CODE = 2135;
    public Vid_player_AudioItem load_more_item;
    protected StreamInfo result;
    Vid_player_SearchConnector.Callbacks2 callbacks;
    String main_search_str;
    Button retryBtn;
    private Vid_player_DS_TubeItemAdapter adapter;
    private String currentSearch;
    private ProgressDialog dialogLoading;
    private Vid_player_DS_SearchEditText edtSearch;
    private ImageView img_audio_search;
    private ImageView img_history;
    private boolean isSearchOpen;
    private ArrayList<Vid_player_AudioItem> items;
    private LinearLayout lay_buttons;
    private LinearLayout lay_search;
    private LinearLayout lay_title;
    private LinearLayoutManager layout_manager;
    private Vid_player_ViewPopupMenu popup_menu_history;
    private SharedPreferences preferences;
    private RecyclerView rec_view;
    private int request_rw_download_index;
    private Vid_player_SearchConnector search_connector;
    private Vid_player_DS_SearchHistory search_history;
    private TextView txt_rec_holder;
    private Vid_player_WaitVidplayerDialog wait_dialog;

    private static void initStorageVideo(Context context) {
        initStorageVideoAudio(context, R.string.download_path_video_key, Environment.DIRECTORY_DOWNLOADS);
    }

    private static void initStorageAudio(Context context) {
        initStorageVideoAudio(context, R.string.download_path_audio_key, Environment.DIRECTORY_DOWNLOADS);
    }

    private static void initStorageVideoAudio(Context context, int i, String str) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = context.getString(i);
        String string2 = defaultSharedPreferences.getString(string, null);
        if (string2 == null || string2.isEmpty()) {
            SharedPreferences.Editor edit = defaultSharedPreferences.edit();
            edit.putString(string, new File(getDir(str), "Music Downloader").toURI().toString());
            edit.apply();
        }
    }

    private static File getDir(String str) {
        return new File(Environment.getExternalStorageDirectory(), str);
    }

    private boolean isOnline() {
        try {
            return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception unused) {
            return false;
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_ds_search_list);
        initStorageVideo(getApplicationContext());
        initStorageAudio(getApplicationContext());
        ProgressDialog progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        this.dialogLoading = progressDialog;
        this.dialogLoading.setIndeterminate(true);
        this.dialogLoading.setCanceledOnTouchOutside(false);
        this.dialogLoading.dismiss();
        this.retryBtn = (Button) findViewById(R.id.btn_retry);
        this.edtSearch = (Vid_player_DS_SearchEditText) findViewById(R.id.edt_search);
        this.img_audio_search = (ImageView) findViewById(R.id.img_audio_search_btn_ds);
        this.img_history = (ImageView) findViewById(R.id.img_history);
        this.lay_buttons = (LinearLayout) findViewById(R.id.layout_buttons);
        this.lay_search = (LinearLayout) findViewById(R.id.layout_search);
        this.lay_title = (LinearLayout) findViewById(R.id.title_layout);
        this.txt_rec_holder = (TextView) findViewById(R.id.txt_rec_holder);
        this.wait_dialog = new Vid_player_WaitVidplayerDialog(this);
        this.preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        this.rec_view = (RecyclerView) findViewById(R.id.rec_view);
        ArrayList<Vid_player_AudioItem> arrayList = new ArrayList<>();
        this.items = arrayList;
        this.adapter = new Vid_player_DS_TubeItemAdapter(this, arrayList, AdsUtility.config.listNativeCount);
        this.load_more_item = new Vid_player_AudioItem();
        this.request_rw_download_index = -1;
        Vid_player_DS_SearchHistory dS_SearchHistoryVidplayer = new Vid_player_DS_SearchHistory(50);
        this.search_history = dS_SearchHistoryVidplayer;
        dS_SearchHistoryVidplayer.loadHistory(getApplicationContext());
        this.search_connector = new Vid_player_SearchConnector((WebView) findViewById(R.id.webView), new AnonymousClass4(), new Vid_player_SearchConnector.OnInitErrorCallback() {
            @Override
            public final void onInitError(Exception exc) {
                Vid_player_DS_SearchVidplayerActivity.this.lambda$onCreate$0$DS_SearchActivity(exc);
            }
        });
        this.callbacks = new AnonymousClass5();
        this.retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.GONE);
                Vid_player_DS_SearchVidplayerActivity dS_SearchActivity = Vid_player_DS_SearchVidplayerActivity.this;
                dS_SearchActivity.retry_search(dS_SearchActivity.main_search_str);
            }
        });
        this.adapter.setOnBindViewHolderListener(new Vid_player_DS_TubeItemAdapter.OnBindViewHolderListener() {
            @Override
            public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                Vid_player_DS_SearchVidplayerActivity.this.lambda$onCreate$2$DS_SearchActivity(viewHolder, i);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.layout_manager = linearLayoutManager;
        this.rec_view.setLayoutManager(linearLayoutManager);
        this.rec_view.setItemAnimator(new DefaultItemAnimator());
        RecyclerView recyclerView = this.rec_view;
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), 0));
        this.rec_view.setAdapter(this.adapter);
        this.edtSearch.getEditText().setHint(getString(R.string.keywords));
        this.edtSearch.setOnQueryTextListener(new Vid_player_DS_SearchEditText.OnQueryTextListener() {
            @Override
            public void onQueryTextChange(String str) {
            }

            @Override
            public boolean onQueryTextSubmit(String str) {
                if (str.length() <= 0) {
                    return false;
                }
                Vid_player_DS_SearchVidplayerActivity.this.search(str);
                Vid_player_DS_SearchVidplayerActivity.this.main_search_str = str;
                Vid_player_DS_SearchVidplayerActivity.this.search_history.search(str);
                return true;
            }
        });
        this.img_audio_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_DS_SearchVidplayerActivity.this.lambda$onCreate$3$DS_SearchActivity(view);
            }
        });
        Vid_player_ViewPopupMenu vidplayerViewPopupMenu = new Vid_player_ViewPopupMenu(this, this.img_history);
        this.popup_menu_history = vidplayerViewPopupMenu;
        vidplayerViewPopupMenu.setOnShowPopupMenuListener(new Vid_player_ViewPopupMenu.OnShowPopupMenuListener() {
            @Override
            public final boolean onShowPopupMenu(Vid_player_ViewPopupMenu vidplayerViewPopupMenu2) {
                return Vid_player_DS_SearchVidplayerActivity.this.lambda$onCreate$4$DS_SearchActivity(vidplayerViewPopupMenu2);
            }
        });
        this.popup_menu_history.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return Vid_player_DS_SearchVidplayerActivity.this.lambda$onCreate$5$DS_SearchActivity(menuItem);
            }
        });
    }

    public void lambda$onCreate$0$DS_SearchActivity(Exception exc) {
        Log.e(Vid_player_SearchConnector.class.getName(), Log.getStackTraceString(exc));
        if (exc.getMessage().contains("WebView") || exc.getMessage().contains("webview")) {
            Vid_player_ToastCompat.showText(getApplicationContext(), getString(R.string.webview_init_error), 1);
            Vid_player_IntentUtil.intentGooglePlay(getApplicationContext(), getString(R.string.webview_package_name));
        } else {
            Vid_player_ToastCompat.showText(getApplicationContext(), exc.getLocalizedMessage(), 1);
        }
        finish();
    }

    public void lambda$onCreate$2$DS_SearchActivity(final RecyclerView.ViewHolder viewHolder, int i) {
        if (this.items.get(i).getId() == null) {
            Vid_player_DS_TubeItemAdapter.lay_Item.setVisibility(View.GONE);
            Vid_player_DS_TubeItemAdapter.lay_LoadMore.setVisibility(View.VISIBLE);
        } else {
            Vid_player_DS_TubeItemAdapter.lay_Item.setVisibility(View.VISIBLE);
            Vid_player_DS_TubeItemAdapter.lay_LoadMore.setVisibility(View.GONE);
        }

        Vid_player_DS_TubeItemAdapter.img_audio_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_DS_SearchVidplayerActivity.this.lambda$onCreate$1$DS_SearchActivity(i, view);
            }
        });
        Vid_player_DS_TubeItemAdapter.txt_audio_name.setText(this.items.get(i).getTitle());
        Picasso.get().load(this.items.get(i).getPath()).into(Vid_player_DS_TubeItemAdapter.img_audio_thumb);
    }

    public void lambda$onCreate$1$DS_SearchActivity(int i, View view) {
        downloadPlayFile(i);
    }

    public void lambda$onCreate$3$DS_SearchActivity(View view) {
        openSearchView();
    }

    public boolean lambda$onCreate$4$DS_SearchActivity(Vid_player_ViewPopupMenu vidplayerViewPopupMenu) {
        List<String> history = this.search_history.getHistory(this);
        if (history.size() > 0) {
            vidplayerViewPopupMenu.getMenu().clear();
            for (int i = 0; i < history.size(); i++) {
                vidplayerViewPopupMenu.getMenu().add(history.get(i));
            }
            return true;
        }
        Vid_player_ToastCompat.showText(getApplicationContext(), getString(R.string.no_search_history_available), 0);
        return false;
    }

    public boolean lambda$onCreate$5$DS_SearchActivity(MenuItem menuItem) {
        String valueOf = String.valueOf(menuItem.getTitle());
        this.edtSearch.getEditText().setText(valueOf);
        search(valueOf);
        this.main_search_str = valueOf;
        this.search_history.search(valueOf);
        return true;
    }

    public void search(final String str) {
        this.wait_dialog.show();
        Vid_player_ConnectUtil.checkInternetConnection(10000, new Vid_player_ConnectUtil.InternetConnectionCheckCallback() {
            @Override
            public final void onResult(boolean z) {
                Vid_player_DS_SearchVidplayerActivity.this.lambda$search$7$DS_SearchActivity(str, z);
            }
        });
    }

    public void lambda$search$7$DS_SearchActivity(final String str, final boolean z) {
        runOnUiThreadCompat(new Runnable() {
            @Override
            public final void run() {
                Vid_player_DS_SearchVidplayerActivity.this.lambda$search$6$DS_SearchActivity(z, str);
            }
        });
    }

    public void lambda$search$6$DS_SearchActivity(boolean z, String str) {
        if (z) {
            this.search_connector.search(str);
            this.currentSearch = str;
            this.items.clear();
            this.adapter.notifyDataSetChanged();
            this.txt_rec_holder.setVisibility(View.GONE);
            closeSearchView();
            return;
        }
        this.wait_dialog.dismiss();
        Vid_player_ToastCompat.showText(getApplicationContext(), getString(R.string.unable_to_connect_internet), 1);
    }

    public void downloadFile(int i) {
        if (i >= 0 && i < this.items.size()) {
            String title = this.items.get(i).getTitle();
            String id = this.items.get(i).getId();
            this.items.get(i).getPath();
            if (title != null && id != null) {
                downloadYoutube(new String(Base64.decode("aHR0cHM6Ly9tLnlvdXR1YmUuY29tL3dhdGNoP3Y9", 0), StandardCharsets.UTF_8) + id);
            }
        }
    }

    public void downloadYoutube(String str) {

        this.dialogLoading.setMessage("please wait..");
        this.dialogLoading.show();
        this.dialogLoading.dismiss();
        if (str.contains("?list")) {
            if (str.contains("&v=")) {
                String str2 = str.split("&v=")[1];
                String str3 = null;
                try {
                    str3 = new String(Base64.decode("aHR0cHM6Ly9tLnlvdXR1YmUuY29tL3dhdGNoP3Y9", 0), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (str2.contains("&")) {
                    str = str3 + str2.split("&")[0];
                } else {
                    str = str3 + str2;
                }
            }
        } else if (str.contains("?v=")) {
            str = str.split("&")[0];
        }
    }

    private void downloadPlayFile(int i) {
        if (i >= 0 && i < this.items.size()) {
            String title = this.items.get(i).getTitle();
            String id = this.items.get(i).getId();
            if (title != null && id != null) {
                try {
                    downloadPlayYoutube(new String(Base64.decode("aHR0cHM6Ly9tLnlvdXR1YmUuY29tL3dhdGNoP3Y9", 0), "UTF-8") + id);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void downloadPlayYoutube(String str) {
        this.dialogLoading.setMessage("please wait..");
        Intent intent = new Intent(Vid_player_DS_SearchVidplayerActivity.this, Vid_player_PlayerActivity.class);
        intent.putExtra("videoUrl", str);
        Vid_player_DS_SearchVidplayerActivity.this.showInterstitial(intent);
        if (str.contains("?list")) {
            if (str.contains("&v=")) {
                String str2 = str.split("&v=")[1];
                String str3 = null;


                try {
                    str3 = new String(Base64.decode("aHR0cHM6Ly9tLnlvdXR1YmUuY29tL3dhdGNoP3Y9", 0), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (str2.contains("&")) {
                    str = str3 + str2.split("&")[0];
                } else {
                    str = str3 + str2;
                }
            }
        } else if (str.contains("?v=")) {
            str = str.split("&")[0];
        }
    }

    public void downloadPlayYoutube(StreamInfo streamInfo) throws Exception {
        this.result = streamInfo;
        if (!isDestroyed()) {
            this.dialogLoading.dismiss();
        }
        showDirectDialogDownloadPlay();
    }

    public void showDirectDialogDownloadPlay() {
        StreamInfo streamInfo = this.result;
        if (streamInfo != null) {
            ArrayList arrayList = new ArrayList(Vid_player_ListHelper.getSortedStreamVideosList(this, streamInfo.video_streams, null, false));
            int defaultResolutionIndex = Vid_player_ListHelper.getDefaultResolutionIndex(this, arrayList);
            if (defaultResolutionIndex == -1) {
                Toast.makeText(this, "video_streams_empty", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private void loadThumpYt(ImageView imageView, int i) {
        if (i >= 0 && i < this.items.size()) {
            String title = this.items.get(i).getTitle();
            String id = this.items.get(i).getId();
            if (title != null && id != null) {
                try {
                    String str = new String(Base64.decode("aHR0cHM6Ly9tLnlvdXR1YmUuY29tL3dhdGNoP3Y9", 0), "UTF-8") + id;
                    if (str.contains("?list")) {
                        if (str.contains("&v=")) {
                            String str2 = str.split("&v=")[1];
                            String str3 = null;
                            try {
                                str3 = new String(Base64.decode("aHR0cHM6Ly9tLnlvdXR1YmUuY29tL3dhdGNoP3Y9", 0), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            if (str2.contains("&")) {
                                str = str3 + str2.split("&")[0];
                            } else {
                                str = str3 + str2;
                            }
                        }
                    } else if (str.contains("?v=")) {
                        str = str.split("&")[0];
                    }
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private void closeSearchView() {
        this.edtSearch.close();
        this.lay_title.setVisibility(View.VISIBLE);
        this.lay_buttons.setVisibility(View.VISIBLE);
        this.lay_search.setVisibility(View.GONE);
        this.isSearchOpen = false;
    }

    private void openSearchView() {
        this.lay_title.setVisibility(View.GONE);
        this.lay_buttons.setVisibility(View.GONE);
        this.lay_search.setVisibility(View.VISIBLE);
        this.edtSearch.open();
        this.edtSearch.getEditText().setSelection(this.edtSearch.getEditText().getText().length());
        this.isSearchOpen = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.search_history.saveHistory(getApplicationContext());
    }

    @Override
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 82) {
            return super.onKeyUp(i, keyEvent);
        }
        startActivity(new Intent(this, Vid_player_DS_SearchVidplayerActivity.class));
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == RW_PERMISSIONS_REQUEST_CODE && iArr.length > 0 && iArr[0] == 0) {
            int i2 = this.request_rw_download_index;
            if (i2 >= 0 && i2 < this.items.size()) {
                downloadFile(this.request_rw_download_index);
                return;
            }
            return;
        }
        this.request_rw_download_index = -1;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public void retry_search(final String str) {
        this.wait_dialog.show();
        Vid_player_ConnectUtil.checkInternetConnection(10000, new Vid_player_ConnectUtil.InternetConnectionCheckCallback() {
            @Override
            public final void onResult(boolean z) {
                Vid_player_DS_SearchVidplayerActivity.this.lambda$retry_search$10$DS_SearchActivity(str, z);
            }
        });
    }

    public void lambda$retry_search$10$DS_SearchActivity(final String str, final boolean z) {
        runOnUiThreadCompat(new Runnable() {
            @Override
            public final void run() {
                Vid_player_DS_SearchVidplayerActivity.this.lambda$retry_search$9$DS_SearchActivity(z, str);
            }
        });
    }

    public void lambda$retry_search$9$DS_SearchActivity(boolean z, String str) {
        if (z) {
            tryagain_search(str);
            this.items.clear();
            this.adapter.notifyDataSetChanged();
            this.txt_rec_holder.setVisibility(View.GONE);
            closeSearchView();
            return;
        }
        this.wait_dialog.dismiss();
        Vid_player_ToastCompat.showText(getApplicationContext(), getString(R.string.unable_to_connect_internet), 1);
    }

    private void tryagain_search(String str) {
        new searchInvidio(this.callbacks).execute(str, String.valueOf(1));
    }

    class AnonymousClass4 implements Vid_player_SearchConnector.Callbacks {
        AnonymousClass4() {
        }

        @Override
        public void onSearchComplete(final ArrayList<Vid_player_AudioItem> arrayList) {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass4.this.lambda$onSearchComplete$0$DS_SearchActivity$4(arrayList);
                }
            });
        }

        public void lambda$onSearchComplete$0$DS_SearchActivity$4(ArrayList arrayList) {
            Vid_player_DS_SearchVidplayerActivity.this.rec_view.scrollToPosition(0);
            Vid_player_DS_SearchVidplayerActivity.this.items.clear();
            Vid_player_DS_SearchVidplayerActivity.this.items.addAll(arrayList);
            if (Vid_player_DS_SearchVidplayerActivity.this.search_connector.canLoadMore()) {
                Vid_player_DS_SearchVidplayerActivity.this.items.add(Vid_player_DS_SearchVidplayerActivity.this.load_more_item);
            }
            Vid_player_DS_SearchVidplayerActivity.this.adapter.notifyDataSetChanged();
            Vid_player_DS_SearchVidplayerActivity.this.txt_rec_holder.setVisibility(View.GONE);
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            if (Vid_player_DS_SearchVidplayerActivity.this.retryBtn.getVisibility() == View.VISIBLE) {
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSearchError() {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass4.this.lambda$onSearchError$1$DS_SearchActivity$4();
                }
            });
        }

        public void lambda$onSearchError$1$DS_SearchActivity$4() {
            if (Vid_player_DS_SearchVidplayerActivity.this.items.size() == 0) {
                Vid_player_DS_SearchVidplayerActivity.this.txt_rec_holder.setText(Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.an_error_occurred_try_again));
                Vid_player_DS_SearchVidplayerActivity.this.txt_rec_holder.setVisibility(View.VISIBLE);
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.VISIBLE);
            }
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.VISIBLE);
            Vid_player_ToastCompat.showText(Vid_player_DS_SearchVidplayerActivity.this.getApplicationContext(), Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.an_error_occurred_try_again), 1);
        }

        @Override

        public void onNoResultsFound() {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass4.this.lambda$onNoResultsFound$2$DS_SearchActivity$4();
                }
            });
        }

        public void lambda$onNoResultsFound$2$DS_SearchActivity$4() {
            if (Vid_player_DS_SearchVidplayerActivity.this.items.size() == 0) {
                Vid_player_DS_SearchVidplayerActivity.this.txt_rec_holder.setText(Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.no_results_found));
                Vid_player_DS_SearchVidplayerActivity.this.txt_rec_holder.setVisibility(View.VISIBLE);
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.VISIBLE);
            }
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            Vid_player_ToastCompat.showText(Vid_player_DS_SearchVidplayerActivity.this.getApplicationContext(), Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.no_results_found), 1);
            Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadMoreComplete(final ArrayList<Vid_player_AudioItem> arrayList) {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass4.this.lambda$onLoadMoreComplete$3$DS_SearchActivity$4(arrayList);
                }
            });
        }

        public void lambda$onLoadMoreComplete$3$DS_SearchActivity$4(ArrayList arrayList) {
            Vid_player_DS_SearchVidplayerActivity.this.items.remove(Vid_player_DS_SearchVidplayerActivity.this.load_more_item);
            Vid_player_DS_SearchVidplayerActivity.this.items.addAll(arrayList);
            if (Vid_player_DS_SearchVidplayerActivity.this.search_connector.canLoadMore()) {
                Vid_player_DS_SearchVidplayerActivity.this.items.add(Vid_player_DS_SearchVidplayerActivity.this.load_more_item);
            }
            Vid_player_DS_SearchVidplayerActivity.this.adapter.notifyDataSetChanged();
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            if (Vid_player_DS_SearchVidplayerActivity.this.retryBtn.getVisibility() == View.VISIBLE) {
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoadMoreError() {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass4.this.lambda$onLoadMoreError$4$DS_SearchActivity$4();
                }
            });
        }

        public void lambda$onLoadMoreError$4$DS_SearchActivity$4() {
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            Vid_player_ToastCompat.showText(Vid_player_DS_SearchVidplayerActivity.this.getApplicationContext(), Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.an_error_occurred_try_again), 1);
            if (Vid_player_DS_SearchVidplayerActivity.this.retryBtn.getVisibility() == View.VISIBLE) {
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNoMoreResults() {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass4.this.lambda$onNoMoreResults$5$DS_SearchActivity$4();
                }
            });
        }

        public void lambda$onNoMoreResults$5$DS_SearchActivity$4() {
            Vid_player_DS_SearchVidplayerActivity.this.items.remove(Vid_player_DS_SearchVidplayerActivity.this.load_more_item);
            Vid_player_DS_SearchVidplayerActivity.this.adapter.notifyDataSetChanged();
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            if (Vid_player_DS_SearchVidplayerActivity.this.retryBtn.getVisibility() == View.VISIBLE) {
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.GONE);
            }
            Vid_player_ToastCompat.showText(Vid_player_DS_SearchVidplayerActivity.this.getApplicationContext(), Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.no_more_results), 1);
        }
    }

    class AnonymousClass5 implements Vid_player_SearchConnector.Callbacks2 {
        AnonymousClass5() {
        }

        @Override
        public void onSearchComplete(final ArrayList<Vid_player_AudioItem> arrayList) {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass5.this.lambda$onSearchComplete$0$DS_SearchActivity$5(arrayList);
                }
            });
        }

        public void lambda$onSearchComplete$0$DS_SearchActivity$5(ArrayList arrayList) {
            Vid_player_DS_SearchVidplayerActivity.this.rec_view.scrollToPosition(0);
            Vid_player_DS_SearchVidplayerActivity.this.items.clear();
            Vid_player_DS_SearchVidplayerActivity.this.items.addAll(arrayList);
            Vid_player_DS_SearchVidplayerActivity.this.adapter.notifyDataSetChanged();
            Vid_player_DS_SearchVidplayerActivity.this.txt_rec_holder.setVisibility(View.GONE);
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            if (Vid_player_DS_SearchVidplayerActivity.this.retryBtn.getVisibility() == View.VISIBLE) {
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSearchError() {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass5.this.lambda$onSearchError$1$DS_SearchActivity$5();
                }
            });
        }

        public void lambda$onSearchError$1$DS_SearchActivity$5() {
            if (Vid_player_DS_SearchVidplayerActivity.this.items.size() == 0) {
                Vid_player_DS_SearchVidplayerActivity.this.txt_rec_holder.setText(Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.an_error_occurred_try_again));
                Vid_player_DS_SearchVidplayerActivity.this.txt_rec_holder.setVisibility(View.VISIBLE);
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.VISIBLE);
            }
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.VISIBLE);
            Vid_player_ToastCompat.showText(Vid_player_DS_SearchVidplayerActivity.this.getApplicationContext(), Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.an_error_occurred_try_again), 1);
        }

        @Override
        public void onNoResultsFound() {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass5.this.lambda$onNoResultsFound$2$DS_SearchActivity$5();
                }
            });
        }

        public void lambda$onNoResultsFound$2$DS_SearchActivity$5() {
            if (Vid_player_DS_SearchVidplayerActivity.this.items.size() == 0) {
                Vid_player_DS_SearchVidplayerActivity.this.txt_rec_holder.setText(Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.no_results_found));
                Vid_player_DS_SearchVidplayerActivity.this.txt_rec_holder.setVisibility(View.VISIBLE);
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.VISIBLE);
            }
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            Vid_player_ToastCompat.showText(Vid_player_DS_SearchVidplayerActivity.this.getApplicationContext(), Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.no_results_found), 1);
            Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadMoreComplete(final ArrayList<Vid_player_AudioItem> arrayList) {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass5.this.lambda$onLoadMoreComplete$3$DS_SearchActivity$5(arrayList);
                }
            });
        }

        public void lambda$onLoadMoreComplete$3$DS_SearchActivity$5(ArrayList arrayList) {
            Vid_player_DS_SearchVidplayerActivity.this.items.addAll(arrayList);
            Vid_player_DS_SearchVidplayerActivity.this.adapter.notifyDataSetChanged();
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            if (Vid_player_DS_SearchVidplayerActivity.this.retryBtn.getVisibility() == View.VISIBLE) {
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoadMoreError() {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass5.this.lambda$onLoadMoreError$4$DS_SearchActivity$5();
                }
            });
        }

        public void lambda$onLoadMoreError$4$DS_SearchActivity$5() {
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            Vid_player_ToastCompat.showText(Vid_player_DS_SearchVidplayerActivity.this.getApplicationContext(), Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.an_error_occurred_try_again), 1);
            if (Vid_player_DS_SearchVidplayerActivity.this.retryBtn.getVisibility() == View.VISIBLE) {
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNoMoreResults() {
            Vid_player_DS_SearchVidplayerActivity.this.runOnUiThreadCompat(new Runnable() {
                @Override
                public final void run() {
                    AnonymousClass5.this.lambda$onNoMoreResults$5$DS_SearchActivity$5();
                }
            });
        }

        public void lambda$onNoMoreResults$5$DS_SearchActivity$5() {
            Vid_player_DS_SearchVidplayerActivity.this.wait_dialog.dismiss();
            if (Vid_player_DS_SearchVidplayerActivity.this.retryBtn.getVisibility() == View.VISIBLE) {
                Vid_player_DS_SearchVidplayerActivity.this.retryBtn.setVisibility(View.GONE);
            }
            Vid_player_ToastCompat.showText(Vid_player_DS_SearchVidplayerActivity.this.getApplicationContext(), Vid_player_DS_SearchVidplayerActivity.this.getString(R.string.no_more_results), 1);
        }
    }

    public class searchInvidio extends AsyncTask<String, Void, String> {
        final StringBuilder builder = new StringBuilder();
        Vid_player_SearchConnector.Callbacks2 callbacks1;

        public searchInvidio(Vid_player_SearchConnector.Callbacks2 callbacks2) {
            this.callbacks1 = callbacks2;
        }

        public String doInBackground(String... strArr) {
            String str;
            try {
                str = new String(Base64.decode("aHR0cHM6Ly9pbnZpZGlvLnVzL3NlYXJjaD9xPQ==", 0), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                str = "";
            }
            try {
                Document document = Jsoup.connect(str + strArr[0] + " music&page=" + strArr[1]).timeout(15000).userAgent("Mozila/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36").get();
                String title = document.title();
                Elements select = document.select("div.h-box p a");
                StringBuilder sb = this.builder;
                sb.append(title);
                sb.append("\n");
                Iterator<Element> it = select.iterator();
                ArrayList<Vid_player_AudioItem> arrayList = new ArrayList<>();
                arrayList.clear();
                while (it.hasNext()) {
                    Element next = it.next();
                    if (next.attr("href").contains("watch?v=")) {
                        String text = next.text();
                        String replace = next.attr("href").replace("https://invidio.us/watch?", "").replace("/watch?v=", "");
                        arrayList.add(new Vid_player_AudioItem(replace, text, "https://invidio.us/vi/" + replace + "/mqdefault.jpg"));
                    }
                    Log.e("Music Array Size", String.valueOf(arrayList.size()));
                    if (arrayList.size() == 0) {
                        this.callbacks1.onSearchError();
                    } else {
                        this.callbacks1.onSearchComplete(arrayList);
                    }
                }
                return null;
            } catch (IOException unused) {
                this.callbacks1.onSearchError();
                return null;
            }
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        public void onPostExecute(String str) {
            super.onPostExecute(str);
        }
    }

    @Override
    public void onBackPressed() {
        Vid_player_DS_Helper.aBoolean = true;
        if (AdsUtility.config.adOnBack) {
            showInterstitial(new BaseCallback() {
                @Override
                public void completed() {
                    Vid_player_DS_SearchVidplayerActivity.super.onBackPressed();
                }
            });
        } else {
            Vid_player_DS_SearchVidplayerActivity.super.onBackPressed();
        }
    }
}
