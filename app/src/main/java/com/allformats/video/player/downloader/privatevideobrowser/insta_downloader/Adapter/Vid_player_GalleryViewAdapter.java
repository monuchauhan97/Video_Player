package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Adapter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity.Vid_player_OpenFileActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;

public class Vid_player_GalleryViewAdapter extends NativeAdsAdapter {
    BaseActivity activity;
    ArrayList<String> filelist;
    LinearLayout emptylayout;

    public Vid_player_GalleryViewAdapter(LinearLayout emptylayout, ArrayList<String> arrayList, BaseActivity fragmentActivity, int listNativeCount) {
        super(fragmentActivity, listNativeCount);
        this.filelist = arrayList;
        this.activity = fragmentActivity;
        this.emptylayout = emptylayout;
    }

    @Override
    public void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int i) {
        ViewHolder viewHolder = (ViewHolder) baseHolder;
        if (this.filelist.get(i).endsWith(".mp4")) {
            viewHolder.videoView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.videoView.setVideoPath(this.filelist.get(i));
            viewHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setVolume(0.0f, 0.0f);
                    mediaPlayer.setLooping(true);
                }
            });
            viewHolder.videoView.start();
        } else {
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.videoView.setVisibility(View.GONE);
            Glide.with(this.activity).load(this.filelist.get(i)).into(viewHolder.imageView);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vid_player_GalleryViewAdapter.this.activity, Vid_player_OpenFileActivity.class);
                intent.putExtra("CURRENTPOSSITION", i);
                intent.putStringArrayListExtra("FILELIST", Vid_player_GalleryViewAdapter.this.filelist);
                activity.showInterstitial(intent);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.activity).inflate(R.layout.vid_player_item_insta_gallery_fragment, viewGroup, false));
    }

    @Override
    public int itemCount() {
        return this.filelist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        ImageView imageView;
        VideoView videoView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
            this.videoView = (VideoView) view.findViewById(R.id.videoView);
            this.cardview = (CardView) view.findViewById(R.id.cardview);
            DisplayMetrics displayMetrics = Vid_player_GalleryViewAdapter.this.activity.getResources().getDisplayMetrics();
            ViewGroup.LayoutParams layoutParams = this.imageView.getLayoutParams();
            layoutParams.width = displayMetrics.widthPixels / 2;
            layoutParams.height = displayMetrics.widthPixels / 2;
            this.imageView.setLayoutParams(layoutParams);
            DisplayMetrics displayMetrics2 = Vid_player_GalleryViewAdapter.this.activity.getResources().getDisplayMetrics();
            ViewGroup.LayoutParams layoutParams2 = this.videoView.getLayoutParams();
            layoutParams2.width = displayMetrics2.widthPixels / 2;
            layoutParams2.height = displayMetrics2.widthPixels / 2;
            this.videoView.setLayoutParams(layoutParams2);
        }
    }

    public void isDownlodEmpty() {
        if (filelist.isEmpty() || filelist.size() == 0) {
            this.emptylayout.setVisibility(View.VISIBLE);
        } else {
            this.emptylayout.setVisibility(View.GONE);
        }
    }
}
