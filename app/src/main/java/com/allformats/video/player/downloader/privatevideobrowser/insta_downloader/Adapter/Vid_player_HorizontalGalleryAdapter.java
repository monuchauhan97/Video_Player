package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Adapter;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import plugin.adsdk.service.BaseActivity;

public class Vid_player_HorizontalGalleryAdapter extends RecyclerView.Adapter<Vid_player_HorizontalGalleryAdapter.ViewHolder> {
    BaseActivity context;
    ArrayList<String> filelist;
    private OnClickListener onClickListener;

    public Vid_player_HorizontalGalleryAdapter(ArrayList<String> arrayList, BaseActivity context, int listNativeCount) {

        this.filelist = arrayList;
        this.context = context;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.vid_player_item_insta_horizontal_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
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
            Glide.with(this.context).load(this.filelist.get(i)).into(viewHolder.imageView);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_HorizontalGalleryAdapter.this.onClickListener.onClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.filelist.size();
    }

    public interface OnClickListener {
        void onClick(int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        VideoView videoView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
            this.videoView = (VideoView) view.findViewById(R.id.videoView);
        }
    }
}
