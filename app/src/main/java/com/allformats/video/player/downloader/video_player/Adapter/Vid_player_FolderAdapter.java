package com.allformats.video.player.downloader.video_player.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.video_player.Activity.Vid_player_VideolistActivity;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_Folder;

import java.util.ArrayList;

import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;

public class Vid_player_FolderAdapter extends NativeAdsAdapter {
    BaseActivity activity;
    ArrayList<Vid_player_Folder> vidplayerFolders;
    int i;
    LayoutInflater inflater;

    public Vid_player_FolderAdapter(BaseActivity fragmentActivity, ArrayList<Vid_player_Folder> arrayList, int i, int adspos) {
        super(fragmentActivity, adspos);
        this.activity = fragmentActivity;
        this.vidplayerFolders = arrayList;
        this.i = i;
        this.inflater = LayoutInflater.from(fragmentActivity);
    }

    @Override
    public void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int position) {
        ViewHolder viewHolder = (ViewHolder) baseHolder;
        viewHolder.foldername.setText(this.vidplayerFolders.get(position).getName());
        TextView textView = viewHolder.videocount;
        if (this.vidplayerFolders.get(position).getMedia_data().size() == 1) {
            textView.setText(this.vidplayerFolders.get(position).getMedia_data().size() + " Video");
        } else
            textView.setText(this.vidplayerFolders.get(position).getMedia_data().size() + " Videos");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_FolderAdapter.this.lambda$onBindViewHolder$0$FolderAdapter(position, view);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType) {
        View view = this.inflater.inflate(R.layout.vid_player_item_folder, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int itemCount() {
        return this.vidplayerFolders.size();
    }

    public void lambda$onBindViewHolder$0$FolderAdapter(int viewHolder, View view) {
        final Intent intent = new Intent(this.activity, Vid_player_VideolistActivity.class);
        intent.putExtra("data", this.vidplayerFolders.get(viewHolder));
        Vid_player_FolderAdapter.this.activity.showInterstitial(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foldername;
        TextView videocount;
        public ViewHolder(View view) {
            super(view);
            this.foldername = (TextView) view.findViewById(R.id.folder_name);
            this.videocount = (TextView) view.findViewById(R.id.video_count);
        }
    }
}
