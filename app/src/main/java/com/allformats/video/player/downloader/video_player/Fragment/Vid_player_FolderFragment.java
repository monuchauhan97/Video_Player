package com.allformats.video.player.downloader.video_player.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_MyEvent;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_Folder;
import com.allformats.video.player.downloader.video_player.Adapter.Vid_player_FolderAdapter;
import com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;
import com.allformats.video.player.downloader.video_player.Util.Vid_player_Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;


public class Vid_player_FolderFragment extends Fragment {
    ArrayList<Vid_player_Folder> folderlist = new ArrayList<>();
    private RecyclerView folderrecycler;
    private ImageView ivnodata;
    private ProgressBar progress;
    private View view;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.vid_player_fragment_folder, viewGroup, false);
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            activeNetworkInfo.isConnected();
        }
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Vid_player_FolderFragment.this.getVideo();
            }
        }).start();
        return this.view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Vid_player_MyEvent eventBus) {
        int type = eventBus.getType();
        if (type == 0) {
            initAdapter(this.folderlist, eventBus.getValue());
        } else if (type == 1) {
            this.progress.setVisibility(View.VISIBLE);
            this.folderrecycler.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Vid_player_FolderFragment.this.getVideo();
                }
            }).start();
        } else if (type == 2) {
            int value = eventBus.getValue();
            if (value == 0) {
                Collections.sort(this.folderlist, new Comparator<Vid_player_Folder>() {
                    public int compare(Vid_player_Folder vidplayerFolder, Vid_player_Folder vidplayerFolder2) {
                        return Long.compare(vidplayerFolder2.getMedia_data().size(), vidplayerFolder.getMedia_data().size());
                    }
                });
                initAdapter(this.folderlist, Vid_player_DS_Helper.getViewBy());
            } else if (value == 2) {
                Collections.sort(this.folderlist, new Comparator<Vid_player_Folder>() {
                    public int compare(Vid_player_Folder vidplayerFolder, Vid_player_Folder vidplayerFolder2) {
                        return vidplayerFolder.getName().compareTo(vidplayerFolder2.getName());
                    }
                });
                initAdapter(this.folderlist, Vid_player_DS_Helper.getViewBy());
            } else if (value == 3) {
                Collections.sort(this.folderlist, new Comparator<Vid_player_Folder>() {
                    public int compare(Vid_player_Folder vidplayerFolder, Vid_player_Folder vidplayerFolder2) {
                        return Long.compare(new File(vidplayerFolder2.getMedia_data().get(0).getPath()).getParentFile().lastModified(), new File(vidplayerFolder.getMedia_data().get(0).getPath()).getParentFile().lastModified());
                    }
                });
                initAdapter(this.folderlist, Vid_player_DS_Helper.getViewBy());
            }
        }
    }

    private void initView() {
        this.folderrecycler = (RecyclerView) this.view.findViewById(R.id.folder_recycler);
        this.progress = (ProgressBar) this.view.findViewById(R.id.progress);
        this.ivnodata = (ImageView) this.view.findViewById(R.id.iv_nodata);
    }

    @SuppressLint("Range")
    public void getVideo() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.clear();
        arrayList2.clear();
        this.folderlist.clear();
        String[] strArr = {"_data", "title", "date_modified", "bucket_display_name", "_size", "date_added", "duration", "resolution"};
        Cursor query = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, null, null, "datetaken DESC");
        char c = 0;
        if (query != null) {
            File file = null;
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex(strArr[c]));
                String string2 = query.getString(query.getColumnIndex(strArr[1]));
                if (string2 == null) {
                    string2 = "";
                }
                String string3 = query.getString(query.getColumnIndex(strArr[2]));
                String string4 = query.getString(query.getColumnIndex(strArr[3]));
                long j = query.getLong(4);
                String string5 = query.getString(query.getColumnIndex(strArr[5]));
                String string6 = query.getString(query.getColumnIndex(strArr[6]));
                String string7 = query.getString(7);
                try {
                    file = new File(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!arrayList.contains(string4) && string4 != null) {
                    arrayList.add(string4);
                }
                if (file != null && file.exists()) {
                    Vid_player_MediaData vidplayerMediaData = new Vid_player_MediaData();
                    vidplayerMediaData.setName(string2);
                    vidplayerMediaData.setPath(string);
                    vidplayerMediaData.setFolder(string4);
                    vidplayerMediaData.setLength(String.valueOf(j));
                    vidplayerMediaData.setAddeddate(string5);
                    vidplayerMediaData.setModifieddate(string3);
                    if (string6 == null) {
                        string6 = null;
                    } else if (!string6.contains(":")) {
                        string6 = Vid_player_Utils.makeShortTimeString(getActivity(), Long.parseLong(string6) / 1000);
                    }
                    if (string7 == null || TextUtils.isEmpty(string7)) {
                        vidplayerMediaData.setResolution("0");
                    } else {
                        vidplayerMediaData.setResolution(string7);
                    }
                    if (string6 != null) {
                        vidplayerMediaData.setDuration(string6);
                        arrayList2.add(vidplayerMediaData);
                    }
                }
                c = 0;
            }
            query.close();
        }
        final ArrayList arrayList3 = new ArrayList();
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                Vid_player_Folder vidplayerFolder = new Vid_player_Folder();
                vidplayerFolder.setName((String) arrayList.get(i));
                ArrayList<Vid_player_MediaData> arrayList4 = new ArrayList<>();
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    if (((Vid_player_MediaData) arrayList2.get(i2)).getFolder() != null && ((Vid_player_MediaData) arrayList2.get(i2)).getFolder().equals(arrayList.get(i))) {
                        arrayList4.add((Vid_player_MediaData) arrayList2.get(i2));
                    }
                }
                vidplayerFolder.setMedia_data(arrayList4);
                if (arrayList4.size() != 0) {
                    arrayList3.add(vidplayerFolder);
                }
            }
        }
        this.folderlist.addAll(arrayList3);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Vid_player_FolderFragment.this.initAdapter(arrayList3, Vid_player_DS_Helper.getViewBy());
            }
        });
    }

    public void initAdapter(ArrayList<Vid_player_Folder> arrayList, int i) {
        this.progress.setVisibility(View.GONE);
        if (arrayList.size() > 0) {
            this.folderrecycler.setVisibility(View.VISIBLE);
            this.ivnodata.setVisibility(View.GONE);
            Vid_player_FolderAdapter vidplayerFolderAdapter = new Vid_player_FolderAdapter(((BaseActivity) getActivity()), arrayList, i, AdsUtility.config.listNativeCount);
            this.folderrecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            this.folderrecycler.setAdapter(vidplayerFolderAdapter);
            return;
        }
        this.folderrecycler.setVisibility(View.GONE);
        this.ivnodata.setVisibility(View.VISIBLE);
    }
}
