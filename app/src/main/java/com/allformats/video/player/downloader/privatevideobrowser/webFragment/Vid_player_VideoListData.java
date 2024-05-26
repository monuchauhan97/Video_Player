package com.allformats.video.player.downloader.privatevideobrowser.webFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.privatevideobrowser.saveFragment.Vid_player_VideoDownloadManagerService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.saveFragment.Vid_player_DownloadPermissionHandler;
import com.allformats.video.player.downloader.privatevideobrowser.saveFragment.Vid_player_DownloadVideo;
import com.allformats.video.player.downloader.privatevideobrowser.saveFragment.lists.Vid_player_DownloadQueuesData;
import com.allformats.video.player.downloader.privatevideobrowser.utils.Vid_player_RenameDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;

public abstract class Vid_player_VideoListData {
    private BaseActivity activity;
    private List<Video> videos = Collections.synchronizedList(new ArrayList());
    private RecyclerView view;

    public Vid_player_VideoListData(BaseActivity activity, RecyclerView recyclerView) {
        this.activity = activity;
        this.view = recyclerView;
        recyclerView.setAdapter(new VideoListAdapter(Vid_player_VideoListData.this.activity, AdsUtility.config.listNativeCount));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
    }

    abstract void onItemDeleted();

    public void recreateVideoList(RecyclerView recyclerView) {
        this.view = recyclerView;
        recyclerView.setAdapter(new VideoListAdapter(Vid_player_VideoListData.this.activity, AdsUtility.config.listNativeCount));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        recyclerView.setHasFixedSize(true);
    }

    public void addItem(String str, String str2, String str3, String str4, String str5, boolean z, String str6, boolean z2) {
        Video video = new Video();
        video.size = str;
        video.type = str2;
        video.link = str3;
        video.name = str4;
        video.page = str5;
        video.chunked = z;
        video.website = str6;
        video.audio = z2;
        if (!z2) {
            boolean z3 = false;
            ListIterator<Video> listIterator = this.videos.listIterator();
            while (true) {
                if (listIterator.hasNext()) {
                    if (listIterator.next().link.equals(video.link)) {
                        z3 = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!z3) {
                this.videos.add(video);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Vid_player_VideoListData.this.view.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public int getSize() {
        return this.videos.size();
    }

    public void deleteAllItems() {
        while (this.videos.size() > 0) {
            this.videos.remove(0);
        }
        ((VideoListAdapter) this.view.getAdapter()).expandedItem = -1;
        this.view.getAdapter().notifyDataSetChanged();
    }

    public class Video {
        boolean audio;
        String link;
        String name;
        String page;
        String size;
        String type;
        String website;
        boolean chunked = false;

        Video() {
        }
    }

    public class VideoListAdapter extends NativeAdsAdapter {
        int expandedItem = -1;

        VideoListAdapter(BaseActivity activity, int listNativeCount) {
            super(activity, listNativeCount);

        }

        @Override
        public void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int i) {
            VideoItem videoItem = (VideoItem) baseHolder;
            videoItem.bind((Video) Vid_player_VideoListData.this.videos.get(i));
            videoItem.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        new Vid_player_DownloadPermissionHandler(Vid_player_VideoListData.this.activity) {
                            @Override
                            public void onPermissionGranted() {
                                startDownload(i);
                            }
                        }.checkPermissions("android.permission.WRITE_EXTERNAL_STORAGE", 4444);
                    } else {
                        startDownload(i);
                    }
                }
            });
            videoItem.rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Vid_player_RenameDialog(Vid_player_VideoListData.this.activity, videoItem.name.getText().toString()) {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                        }
                        @Override
                        public void onOK(String str) {
                            ((Video) Vid_player_VideoListData.this.videos.get(i)).name = str;
                            VideoListAdapter.this.notifyItemChanged(i);
                        }
                    };
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType) {
            return new VideoItem(LayoutInflater.from(Vid_player_VideoListData.this.activity).inflate(R.layout.vid_player_videos_found_item, viewGroup, false));
        }

        @Override
        public int itemCount() {
            return Vid_player_VideoListData.this.videos.size();
        }

        public class VideoItem extends RecyclerView.ViewHolder {
            ImageView download;
            TextView name;
            ImageView rename;
            TextView size;
            ImageView videoFoundThumb;

            VideoItem(View view) {
                super(view);
                this.size = (TextView) view.findViewById(R.id.videoFoundSize);
                this.name = (TextView) view.findViewById(R.id.videoFoundName);
                this.videoFoundThumb = (ImageView) view.findViewById(R.id.videoFoundThumb);
                this.download = (ImageView) view.findViewById(R.id.videoFoundDownload);
                this.rename = (ImageView) view.findViewById(R.id.videoFoundRename);
            }

            void bind(Video video) {
                if (video.size != null) {
                    this.size.setText(Formatter.formatShortFileSize(Vid_player_VideoListData.this.activity, Long.parseLong(video.size)));
                } else {
                    this.size.setText(" ");
                }
                this.name.setText(video.name);
                Glide.with(Vid_player_VideoListData.this.activity).load(video.link).transform(new RoundedCorners(5)).into(this.videoFoundThumb);
            }
        }

        void startDownload(int i) {
            Video video = (Video) Vid_player_VideoListData.this.videos.get(i);
            Vid_player_DownloadQueuesData load = Vid_player_DownloadQueuesData.load(Vid_player_VideoListData.this.activity);
            load.insertToTop(video.size, video.type, video.link, video.name, video.page, video.chunked, video.website);
            load.save(Vid_player_VideoListData.this.activity);
            Vid_player_DownloadVideo topVideo = load.getTopVideo();
            Intent downloadService = Vid_player_DS_Helper.getInstance().getDownloadService();
            Vid_player_VideoDownloadManagerService.stop();
            downloadService.putExtra("link", topVideo.link);
            downloadService.putExtra("name", topVideo.name);
            downloadService.putExtra("type", topVideo.type);
            downloadService.putExtra("size", topVideo.size);
            downloadService.putExtra("page", topVideo.page);
            downloadService.putExtra("chunked", topVideo.chunked);
            downloadService.putExtra("website", topVideo.website);
            Vid_player_DS_Helper.getInstance().startService(downloadService);
            Vid_player_VideoListData.this.videos.remove(i);
            VideoListAdapter.this.expandedItem = -1;
            VideoListAdapter.this.notifyDataSetChanged();
            Vid_player_VideoListData.this.onItemDeleted();
            Toast.makeText(Vid_player_VideoListData.this.activity, "Downloading video in the background..", Toast.LENGTH_LONG).show();
        }
    }
}
