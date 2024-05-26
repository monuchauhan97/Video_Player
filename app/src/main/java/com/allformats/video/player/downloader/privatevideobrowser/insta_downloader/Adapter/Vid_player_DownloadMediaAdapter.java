package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Adapter;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.JsonArrayRootModel;

import java.util.ArrayList;
import java.util.List;

import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;

public class Vid_player_DownloadMediaAdapter extends NativeAdsAdapter {
    BaseActivity context;
    List<JsonArrayRootModel> downloadimagelist = new ArrayList();
    List<JsonArrayRootModel> finalresponse;
    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onClick(List<JsonArrayRootModel> list);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Vid_player_DownloadMediaAdapter(List<JsonArrayRootModel> list, BaseActivity context, int listNativeCount) {
        super(context, listNativeCount);
        this.finalresponse = list;
        this.context = context;
    }

    @Override
    public void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int i) {
        ViewHolder viewHolder = (ViewHolder) baseHolder;
        Glide.with(this.context).load(this.finalresponse.get(i).getUrl().get(0).getUrl()).into(viewHolder.imageView);
        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.checkbox.isChecked()) {
                    Vid_player_DownloadMediaAdapter.this.downloadimagelist.add(Vid_player_DownloadMediaAdapter.this.finalresponse.get(i));
                } else if (Vid_player_DownloadMediaAdapter.this.downloadimagelist.contains(Vid_player_DownloadMediaAdapter.this.finalresponse.get(i))) {
                    Vid_player_DownloadMediaAdapter.this.downloadimagelist.remove(Vid_player_DownloadMediaAdapter.this.finalresponse.get(i));
                }
                Vid_player_DownloadMediaAdapter.this.onClickListener.onClick(Vid_player_DownloadMediaAdapter.this.downloadimagelist);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.vid_player_item_insta_download_fragment, viewGroup, false));
    }

    @Override
    public int itemCount() {
        return this.finalresponse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
            DisplayMetrics displayMetrics = Vid_player_DownloadMediaAdapter.this.context.getResources().getDisplayMetrics();
            ViewGroup.LayoutParams layoutParams = this.imageView.getLayoutParams();
            layoutParams.width = displayMetrics.widthPixels / 2;
            layoutParams.height = displayMetrics.widthPixels / 2;
            this.imageView.setLayoutParams(layoutParams);
        }
    }
}
