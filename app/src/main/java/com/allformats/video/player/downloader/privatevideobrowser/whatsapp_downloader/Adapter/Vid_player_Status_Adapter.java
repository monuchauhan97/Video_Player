package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Vid_player_PreviewActivity;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.model.Vid_player_StatusModel;
import com.bumptech.glide.Glide;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;

public class Vid_player_Status_Adapter extends NativeAdsAdapter {
    List<Vid_player_StatusModel> arrayList;
    BaseActivity context;
    LayoutInflater inflater;
    public OnCheckboxListener onCheckboxListener;
    int width;
    CheckBox selectAll;

    public interface OnCheckboxListener {
        void onCheckboxListener(View view, List<Vid_player_StatusModel> list);
    }

    public Vid_player_Status_Adapter(CheckBox selectAll, BaseActivity fragment, ArrayList<Vid_player_StatusModel> list, OnCheckboxListener onCheckboxListener, int listNativeCount) {
        super(fragment, listNativeCount);
        this.context = fragment;
        this.arrayList = list;
        this.selectAll = selectAll;
        this.inflater = (LayoutInflater) fragment.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.width = fragment.getResources().getDisplayMetrics().widthPixels;
        this.onCheckboxListener = onCheckboxListener;

    }

    @Override
    public void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int i) {
        Holder holder = (Holder) baseHolder;

        if (isVideoFile(this.arrayList.get(i).getFilePath())) {
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        Glide.with(this.context).load(this.arrayList.get(i).getFilePath()).into(holder.gridImage);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                Vid_player_Status_Adapter.this.arrayList.get(i).setSelected(z);
                if (Vid_player_Status_Adapter.this.onCheckboxListener != null) {
                    Vid_player_Status_Adapter.this.onCheckboxListener.onCheckboxListener(compoundButton, Vid_player_Status_Adapter.this.arrayList);
                }
                if (!z) {
                    selectAll.setChecked(z);
                }
            }
        });
        if (this.arrayList.get(i).isSelected()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                Intent intent = new Intent(Vid_player_Status_Adapter.this.context, Vid_player_PreviewActivity.class);
                intent.putParcelableArrayListExtra("images", (ArrayList) Vid_player_Status_Adapter.this.arrayList);
                intent.putExtra("position", i);
                intent.putExtra("statusdownload", "");
                Vid_player_Status_Adapter.this.context.showInterstitial(intent);
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType) {
        View view = this.inflater.inflate(R.layout.vid_player_row_recent, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public int itemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageView, gridImage;
        CheckBox checkBox;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.play);
            gridImage = (ImageView) itemView.findViewById(R.id.gridImage);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        Log.d("MyAdapter", "onActivityResult");
    }

    public boolean isVideoFile(String str) {
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(str);
        return guessContentTypeFromName != null && guessContentTypeFromName.startsWith("video");
    }
}