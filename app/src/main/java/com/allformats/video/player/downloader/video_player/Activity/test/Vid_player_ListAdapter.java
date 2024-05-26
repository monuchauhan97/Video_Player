package com.allformats.video.player.downloader.video_player.Activity.test;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.SimpleExoPlayer;
import  com.allformats.video.player.downloader.R;
import  com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;

import java.util.List;

public class Vid_player_ListAdapter extends RecyclerView.Adapter<Vid_player_ListAdapter.holder> {
    private final Activity ctx;
    private final List<Vid_player_MediaData> list;
    public final SimpleExoPlayer player;

    public class holder extends RecyclerView.ViewHolder {
        final ImageView i1;
        int position;
        final TextView t1;
        final TextView t2;

        holder(View view) {
            super(view);
            this.i1 = (ImageView) view.findViewById(R.id.thumb);
            this.t1 = (TextView) view.findViewById(R.id.name);
            this.t2 = (TextView) view.findViewById(R.id.size);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public final void onClick(View view2) {
                    Vid_player_ListAdapter.this.player.seekTo(holder.this.position, 0L);
                }
            });
        }
    }

    public Vid_player_ListAdapter(Activity activity, List<Vid_player_MediaData> list, SimpleExoPlayer simpleExoPlayer) {
        this.ctx = activity;
        this.list = list;
        this.player = simpleExoPlayer;
    }

    @Override
    public holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new holder(LayoutInflater.from(this.ctx).inflate(R.layout.vid_player_playlist_card, viewGroup, false));
    }

    public void onBindViewHolder(holder holderVar, int i) {
        holderVar.position = i;
        Glide.with(this.ctx).load(this.list.get(i).getPath()).into(holderVar.i1);
        holderVar.t1.setText(this.list.get(i).getName());
        holderVar.t2.setText(this.list.get(i).getLength());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
