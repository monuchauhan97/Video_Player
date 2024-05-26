package com.allformats.video.player.downloader.video_player.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_MyEvent;
import com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.video_player.Adapter.Vid_player_VideoAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;

public class Vid_player_RecentFragment extends Fragment {
    public static ImageView ivnodata;
    public static RecyclerView recentrecycler;
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
        this.view = layoutInflater.inflate(R.layout.vid_player_fragment_recent, viewGroup, false);
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            activeNetworkInfo.isConnected();
        }
        initView();
        getVideo();
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
            recentrecycler.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Vid_player_RecentFragment.this.getVideo();
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
        } else if (type == 3) {
            this.progress.setVisibility(View.VISIBLE);
            recentrecycler.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Vid_player_RecentFragment.this.getVideo();
                }
            }).start();
        }
    }

   /* @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.EventBus eventBus) {
        int type = eventBus.getType();
        if (type == 0) {
            initAdapter(this.mediadataslist, eventBus.getValue());
        } else if (type == 1) {
            this.progress.setVisibility(View.VISIBLE);
            recentrecycler.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override 
                public void run() {
                    Vid_player_RecentFragment.this.getVideo();
                }
            }).start();
        } else if (type == 2) {
            int value = eventBus.getValue();
            if (value == 0) {
                Collections.sort(this.mediadataslist, new Comparator<Vid_player_MediaData>() {
                    public int compare(Vid_player_MediaData mediaData, Vid_player_MediaData mediaData2) {
                        return Long.compare(Long.parseLong(mediaData2.getLength()), Long.parseLong(mediaData.getLength()));
                    }
                });
                initAdapter(this.mediadataslist, Vid_player_DS_Helper.getViewBy());
            } else if (value == 1) {
                Collections.sort(this.mediadataslist, new Comparator<Vid_player_MediaData>() {
                    public int compare(Vid_player_MediaData mediaData, Vid_player_MediaData mediaData2) {
                        return Long.compare(Integer.parseInt(mediaData2.getDuration()), Integer.parseInt(mediaData.getDuration()));
                    }
                });
                initAdapter(this.mediadataslist, Vid_player_DS_Helper.getViewBy());
            } else if (value == 2) {
                Collections.sort(this.mediadataslist, new Comparator<Vid_player_MediaData>() {
                    public int compare(Vid_player_MediaData mediaData, Vid_player_MediaData mediaData2) {
                        return mediaData.getName().compareTo(mediaData2.getName());
                    }
                });
                initAdapter(this.mediadataslist, Vid_player_DS_Helper.getViewBy());
            } else if (value == 3) {
                Collections.sort(this.mediadataslist, new Comparator<Vid_player_MediaData>() {
                    public int compare(Vid_player_MediaData mediaData, Vid_player_MediaData mediaData2) {
                        return mediaData2.getModifieddate().compareTo(mediaData.getModifieddate());
                    }
                });
                initAdapter(this.mediadataslist, Vid_player_DS_Helper.getViewBy());
            }
        } else if (type == 3) {
            this.progress.setVisibility(View.VISIBLE);
            recentrecycler.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override 
                public void run() {
                    Vid_player_RecentFragment.this.getVideo();
                }
            }).start();
        }
    }*/

    public void getVideo() {
        this.mediadataslist.clear();
        if (!TextUtils.isEmpty(Vid_player_DS_Helper.getRecentPlay())) {
            final ArrayList arrayList = (ArrayList) new Gson().fromJson(Vid_player_DS_Helper.getRecentPlay(), new TypeToken<List<Vid_player_MediaData>>() {
            }.getType());
            Collections.reverse(arrayList);
            this.mediadataslist.addAll(arrayList);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Vid_player_RecentFragment.this.initAdapter(arrayList, Vid_player_DS_Helper.getViewBy());
                }
            });
            return;
        }
        this.progress.setVisibility(View.GONE);
        recentrecycler.setVisibility(View.GONE);
        ivnodata.setVisibility(View.VISIBLE);
    }

    public void initAdapter(ArrayList<Vid_player_MediaData> arrayList, int i) {
        this.progress.setVisibility(View.GONE);
        if (arrayList.size() > 0) {
            recentrecycler.setVisibility(View.VISIBLE);
            ivnodata.setVisibility(View.GONE);
            Vid_player_VideoAdapter vidplayerVideoAdapter = new Vid_player_VideoAdapter(((BaseActivity) getActivity()), arrayList, i, 2, AdsUtility.config.listNativeCount);
            recentrecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            recentrecycler.setAdapter(vidplayerVideoAdapter);
            return;
        }
        recentrecycler.setVisibility(View.GONE);
        ivnodata.setVisibility(View.VISIBLE);
    }

    private void initView() {
        recentrecycler = (RecyclerView) this.view.findViewById(R.id.recent_recycler);
        this.progress = (ProgressBar) this.view.findViewById(R.id.progress);
        ivnodata = (ImageView) this.view.findViewById(R.id.iv_nodata);
    }
}
