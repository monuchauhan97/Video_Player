package com.allformats.video.player.downloader.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_AudioItem;

import java.util.List;

import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;


public class Vid_player_DS_TubeItemAdapter extends NativeAdsAdapter {
    public static ImageView img_audio_play;
    public static ImageView img_audio_thumb;
    public static LinearLayout lay_Item;
    public static LinearLayout lay_LoadMore;
    public static TextView txt_audio_name;
    int count = 0;
    private BaseActivity context;
    private List<Vid_player_AudioItem> items;
    private OnBindViewHolderListener onBindViewHolderListener;

    public Vid_player_DS_TubeItemAdapter(BaseActivity activity, List<Vid_player_AudioItem> list, int listNativeCount) {
        super(activity,listNativeCount);
        this.context = activity;
        this.items = list;
    }
/*

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }
*/

    @Override
    public void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int i) {
        ViewHolder viewHolder = (ViewHolder) baseHolder;
        OnBindViewHolderListener onBindViewHolderListener = this.onBindViewHolderListener;
        if (onBindViewHolderListener != null) {
            onBindViewHolderListener.onBindViewHolder(viewHolder, i);
        }
    }

    @Override
    public RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType) {
       this.count++;
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.vid_player_item_layout, viewGroup, false));
    }

    @Override
    public int itemCount() {
        return this.items.size();
    }

    public void setOnBindViewHolderListener(OnBindViewHolderListener onBindViewHolderListener) {
        this.onBindViewHolderListener = onBindViewHolderListener;
    }


    public interface OnBindViewHolderListener {
        void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            Vid_player_DS_TubeItemAdapter.txt_audio_name = (TextView) view.findViewById(R.id.txt_audio_name);
            Vid_player_DS_TubeItemAdapter.img_audio_play = (ImageView) view.findViewById(R.id.img_audio_play_btn_ds);
            Vid_player_DS_TubeItemAdapter.img_audio_thumb = (ImageView) view.findViewById(R.id.img_audio_thumb);
            Vid_player_DS_TubeItemAdapter.lay_LoadMore = (LinearLayout) view.findViewById(R.id.layout_load_more_item);
            Vid_player_DS_TubeItemAdapter.lay_Item = (LinearLayout) view.findViewById(R.id.main_layout);
        }
    }
}
