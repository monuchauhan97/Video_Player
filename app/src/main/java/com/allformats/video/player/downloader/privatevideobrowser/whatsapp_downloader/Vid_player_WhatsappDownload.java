package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Adapter.Vid_player_GalleryViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;

public class Vid_player_WhatsappDownload extends Fragment {
    CardView cardView;
    CardView card_view_bottom;
    BaseActivity context;
    ArrayList<String> download_list = new ArrayList<>();
    FrameLayout fl_adplaceholder_bottom;
    FrameLayout frameLayout;
    Vid_player_GalleryViewAdapter vidplayerGalleryViewAdapter;
    RecyclerView recyclerView;
    LinearLayout Emptylayout;
    View inflate;

    public void getvideosfrompath() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "Video Player" + "/WhatsApp Status");
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            Log.i("TAG", "getimagesfrompath: ");
            for (File file2 : listFiles) {
                this.download_list.add(String.valueOf(file2));
            }
        }
    }

    public Vid_player_WhatsappDownload(BaseActivity context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.vid_player_fragment_insta_download, viewGroup, false);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_gallery_fragment);
        this.frameLayout = (FrameLayout) inflate.findViewById(R.id.fl_adplaceholder);
        this.Emptylayout = (LinearLayout) inflate.findViewById(R.id.noDownloads);
        this.cardView = (CardView) inflate.findViewById(R.id.card_view);
        download_list.clear();
        getvideosfrompath();
        this.fl_adplaceholder_bottom = (FrameLayout) inflate.findViewById(R.id.fl_adplaceholder_bottom);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_gallery_fragment);
        this.card_view_bottom = (CardView) inflate.findViewById(R.id.card_view_bottom);
        this.inflate = inflate;
        adapgtercall(inflate);
        return inflate;
    }

    private void adapgtercall(View view) {
        this.recyclerView = (RecyclerView) view.findViewById(R.id.rv_gallery_fragment);
        Collections.reverse(this.download_list);
        this.download_list.size();
        if (download_list.isEmpty() || download_list.size() == 0) {
            this.Emptylayout.setVisibility(View.VISIBLE);
        } else {
            this.Emptylayout.setVisibility(View.GONE);
            this.vidplayerGalleryViewAdapter = new Vid_player_GalleryViewAdapter(Emptylayout, this.download_list, this.context, AdsUtility.config.listNativeCount);
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = vidplayerGalleryViewAdapter.getItemViewType(position);
                    if (itemViewType == NativeAdsAdapter.AD) {
                        return 2;
                    }
                    return 1;
                }
            });
            this.recyclerView.setLayoutManager(layoutManager);
            this.recyclerView.setAdapter(this.vidplayerGalleryViewAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        download_list.clear();
        getvideosfrompath();
        if (inflate != null) {
            adapgtercall(inflate);
        }
    }
}
