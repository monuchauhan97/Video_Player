package com.allformats.video.player.downloader.video_player.Fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
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
import com.allformats.video.player.downloader.video_player.Adapter.Vid_player_VideoAdapter;
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

public class Vid_player_VideoFragment extends Fragment {
    public static ImageView ivnodata;
    public static RecyclerView videorecycler;
    ArrayList<Vid_player_MediaData> mediadataslist = new ArrayList<>();
    private ProgressBar progress;
    private View view;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.vid_player_fragment_video, viewGroup, false);
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Vid_player_VideoFragment.this.getVideo();
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
            initAdapter(this.mediadataslist, eventBus.getValue());
        } else if (type == 1) {
            this.progress.setVisibility(View.VISIBLE);
            videorecycler.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Vid_player_VideoFragment.this.getVideo();
                }
            }).start();
        } else if (type == 2) {
            int value = eventBus.getValue();
            if (value == 0) {
                Collections.sort(this.mediadataslist, new Comparator<Vid_player_MediaData>() {
                    public int compare(Vid_player_MediaData vidplayerMediaData, Vid_player_MediaData vidplayerMediaData2) {
                        return Long.compare(Long.parseLong(vidplayerMediaData2.getLength()), Long.parseLong(vidplayerMediaData.getLength()));
                    }
                });
                initAdapter(this.mediadataslist, Vid_player_DS_Helper.getViewBy());
            } else if (value == 1) {
                Collections.sort(this.mediadataslist, new Comparator<Vid_player_MediaData>() {
                    public int compare(Vid_player_MediaData vidplayerMediaData, Vid_player_MediaData vidplayerMediaData2) {
                        return Long.compare(Integer.parseInt(vidplayerMediaData2.getDuration()), Integer.parseInt(vidplayerMediaData.getDuration()));
                    }
                });
                initAdapter(this.mediadataslist, Vid_player_DS_Helper.getViewBy());
            } else if (value == 2) {
                Collections.sort(this.mediadataslist, new Comparator<Vid_player_MediaData>() {
                    public int compare(Vid_player_MediaData vidplayerMediaData, Vid_player_MediaData vidplayerMediaData2) {
                        return vidplayerMediaData.getName().compareTo(vidplayerMediaData2.getName());
                    }
                });
                initAdapter(this.mediadataslist, Vid_player_DS_Helper.getViewBy());
            } else if (value == 3) {
                Collections.sort(this.mediadataslist, new Comparator<Vid_player_MediaData>() {
                    public int compare(Vid_player_MediaData vidplayerMediaData, Vid_player_MediaData vidplayerMediaData2) {
                        return vidplayerMediaData2.getModifieddate().compareTo(vidplayerMediaData.getModifieddate());
                    }
                });
                initAdapter(this.mediadataslist, Vid_player_DS_Helper.getViewBy());
            }
        }
    }

    public void initAdapter(ArrayList<Vid_player_MediaData> arrayList, int i) {
        this.progress.setVisibility(View.GONE);
        if (arrayList.size() > 0) {
            videorecycler.setVisibility(View.VISIBLE);
            ivnodata.setVisibility(View.GONE);
            Vid_player_VideoAdapter vidplayerVideoAdapter = new Vid_player_VideoAdapter(((BaseActivity) getActivity()), arrayList, i, 0, AdsUtility.config.listNativeCount);
            videorecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            videorecycler.setAdapter(vidplayerVideoAdapter);
            return;
        }
        videorecycler.setVisibility(View.GONE);
        ivnodata.setVisibility(View.VISIBLE);
    }

    private void initView() {
        videorecycler = (RecyclerView) this.view.findViewById(R.id.video_recycler);
        this.progress = (ProgressBar) this.view.findViewById(R.id.progress);
        ivnodata = (ImageView) this.view.findViewById(R.id.iv_nodata);
    }

    @SuppressLint("Range")
    public void getVideo() {
        ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        arrayList.clear();
        arrayList2.clear();
        this.mediadataslist.clear();
        String[] strArr = {"_data", "title", "date_modified", "bucket_display_name", "_size", "date_added", "duration", "resolution"};
        Cursor query = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, null, null, "datetaken DESC");
        if (query != null) {
            File file = null;
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex(strArr[0]));
                String string2 = query.getString(query.getColumnIndex(strArr[1]));
                if (string2 == null) {
                    string2 = "";
                }
                String string3 = query.getString(query.getColumnIndex(strArr[2]));
                String string4 = query.getString(query.getColumnIndex(strArr[3]));
                long j = query.getLong(4);
                String string5 = query.getString(query.getColumnIndex(strArr[5]));
                String string6 = query.getString(query.getColumnIndex(strArr[6]));
                int i = query.getInt(6);
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
                    vidplayerMediaData.setVideoDuration(i);
                    vidplayerMediaData.setLayoutType(2);
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
                strArr = strArr;
            }
            query.close();
            this.mediadataslist.addAll(arrayList2);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Vid_player_VideoFragment.this.initAdapter(arrayList2, Vid_player_DS_Helper.getViewBy());
            }
        });
    }
}
