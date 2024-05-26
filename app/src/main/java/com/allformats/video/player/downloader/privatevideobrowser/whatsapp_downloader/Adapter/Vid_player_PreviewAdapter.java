package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Vid_player_VideoPlayerActivity;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.model.Vid_player_StatusModel;

import java.io.File;
import java.util.ArrayList;

import plugin.adsdk.service.BaseActivity;

public class Vid_player_PreviewAdapter extends PagerAdapter {
    BaseActivity activity;
    private File file;
    ArrayList<Vid_player_StatusModel> imageList;

    public Vid_player_PreviewAdapter(BaseActivity activity, ArrayList<Vid_player_StatusModel> arrayList) {
        this.activity = activity;
        this.imageList = arrayList;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, final int i) {
        View inflate = LayoutInflater.from(this.activity).inflate(R.layout.vid_player_preview_list_item, viewGroup, false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView);
        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.iconplayer);
        File file = new File(this.imageList.get(i).getFilePath());
        this.file = file;
        if (!file.isDirectory()) {
            if (!Vid_player_Utils.getBack(this.imageList.get(i).getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                try {
                    imageView2.setVisibility(View.VISIBLE);
                    Glide.with(this.activity).load(this.imageList.get(i).getFilePath()).into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (!Vid_player_Utils.getBack(this.imageList.get(i).getFilePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
                imageView2.setVisibility(View.GONE);
                Glide.with(this.activity).load(this.imageList.get(i).getFilePath()).into(imageView);
            }
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_PreviewAdapter.this.lambda$instantiateItem$0$PreviewAdapter(i, view);
            }
        });
        viewGroup.addView(inflate);
        return inflate;
    }

    public void lambda$instantiateItem$0$PreviewAdapter(int i, View view) {
        if (!Vid_player_Utils.getBack(this.imageList.get(i).getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            Vid_player_Utils.mPath = this.imageList.get(i).getFilePath();
            this.activity.showInterstitial(new Intent(this.activity, Vid_player_VideoPlayerActivity.class));
        }
    }

    @Override
    public int getCount() {
        return this.imageList.size();
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((RelativeLayout) obj);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((RelativeLayout) obj);
    }
}
