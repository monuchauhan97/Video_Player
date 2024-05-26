package com.allformats.video.player.downloader.privatevideobrowser;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Vid_player_VideoSitesList extends RecyclerView.Adapter<Vid_player_VideoSitesList.VideoStreamingSiteItem> {
    private Vid_player_MainBrowserActivity activity;
    private List<Site> sites;
    ImageView imageView;
    public class Site {
        int color;
        Bitmap drawable;
        String title;
        String url;
        Site(Bitmap bitmap, int i, String str, String str2) {
            this.drawable = bitmap;
            this.title = str;
            this.url = str2;
            this.color = i;
        }
    }

    public Vid_player_VideoSitesList(Vid_player_MainBrowserActivity vidplayerMainBrowserActivity,ImageView imageView) throws IOException {
        this.imageView = imageView;
        this.activity = vidplayerMainBrowserActivity;
        this.sites = new ArrayList();
        this.activity = vidplayerMainBrowserActivity;
        this.sites.add(new Site(drawableToBitmap(R.drawable.instagram, vidplayerMainBrowserActivity), R.color.transparent, "Instagram", "https://www.instagram.com"));
        this.sites.add(new Site(drawableToBitmap(R.drawable.whatsapp, vidplayerMainBrowserActivity), R.color.transparent, "Whatsapp", "https://web.whatsapp.com/"));
        this.sites.add(new Site(drawableToBitmap(R.drawable.youtube, vidplayerMainBrowserActivity), R.color.transparent, "Youtube", "https://m.youtube.com/"));
        this.sites.add(new Site(drawableToBitmap(R.drawable.facebook, vidplayerMainBrowserActivity), R.color.transparent, "Facebook", "https://m.facebook.com"));
        this.sites.add(new Site(drawableToBitmap(R.drawable.snapchat, vidplayerMainBrowserActivity), R.color.transparent, "Snapchat", "https://www.snapchat.com/"));
        this.sites.add(new Site(drawableToBitmap(R.drawable.twitter, vidplayerMainBrowserActivity), R.color.transparent, "Twitter", "https://mobile.twitter.com"));
        this.sites.add(new Site(drawableToBitmap(R.drawable.linkedin, vidplayerMainBrowserActivity), R.color.transparent, "Linkedin", "https://in.linkedin.com/"));
        this.sites.add(new Site(drawableToBitmap(R.drawable.vlive, vidplayerMainBrowserActivity), R.color.transparent, "Vlive", "https://m.vlive.tv"));
        this.sites.add(new Site(drawableToBitmap(R.drawable.tumblr, vidplayerMainBrowserActivity), R.color.transparent, "Tumblr", "https://www.tumblr.com"));
        this.sites.add(new Site(drawableToBitmap(R.drawable.dailymotion, vidplayerMainBrowserActivity), R.color.transparent, "Dailymotion", "https://www.dailymotion.com"));
    }

    @Override
    public VideoStreamingSiteItem onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoStreamingSiteItem(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vid_player_video_sites_item, viewGroup, false));
    }

    public void onBindViewHolder(VideoStreamingSiteItem videoStreamingSiteItem, int i) {
        videoStreamingSiteItem.bind(this.sites.get(i));
        videoStreamingSiteItem.layout.setBackgroundResource(this.sites.get(i).color);
    }

    @Override
    public int getItemCount() {
        return this.sites.size();
    }

    public class VideoStreamingSiteItem extends RecyclerView.ViewHolder {
        private ImageView icon;
        private LinearLayout layout;
        private TextView title;

        VideoStreamingSiteItem(View view) {
            super(view);
            this.icon = (ImageView) view.findViewById(R.id.iv_videoSite_icon);
            this.title = (TextView) view.findViewById(R.id.iv_VideoSite_Name);
            this.layout = (LinearLayout) view.findViewById(R.id.image_Background);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if (((Site) Vid_player_VideoSitesList.this.sites.get(VideoStreamingSiteItem.this.getAdapterPosition())).title.equals("Whatsapp")) {
                        Vid_player_VideoSitesList.this.activity.whatsappClicked();
                    } else if (((Site) Vid_player_VideoSitesList.this.sites.get(VideoStreamingSiteItem.this.getAdapterPosition())).title.equals("Instagram")) {
                        Vid_player_VideoSitesList.this.activity.instaClicked();
                    } else if (VideoStreamingSiteItem.this.getAdapterPosition() > 9) {
                        Vid_player_VideoSitesList.this.AppLinkClick(Vid_player_VideoSitesList.this.activity, ((Site) Vid_player_VideoSitesList.this.sites.get(VideoStreamingSiteItem.this.getAdapterPosition())).url, ((Site) Vid_player_VideoSitesList.this.sites.get(VideoStreamingSiteItem.this.getAdapterPosition())).title);
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        ((EditText) Vid_player_VideoSitesList.this.activity.findViewById(R.id.et_search_bar)).setText(((Site) Vid_player_VideoSitesList.this.sites.get(VideoStreamingSiteItem.this.getAdapterPosition())).url);
                        Vid_player_VideoSitesList.this.activity.getVideoBrowserManagerFragment().newWindow(((Site) Vid_player_VideoSitesList.this.sites.get(VideoStreamingSiteItem.this.getAdapterPosition())).url);
                    }
                }
            });
        }

        void bind(Site site) {
            this.icon.setImageBitmap(site.drawable);
            this.title.setText(site.title);
        }
    }

    public void AppLinkClick(final Context context, final String str, String str2) {
        AlertDialog.Builder title = new AlertDialog.Builder(context).setTitle(str2);
        title.setMessage("Do you want to open " + str2 + " App in Play Store?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + str)));
                } catch (ActivityNotFoundException unused) {
                    Toast.makeText(context, "unable to find market app", Toast.LENGTH_LONG).show();
                }
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public static Bitmap drawableToBitmap(int i, Context context) {
        return BitmapFactory.decodeResource(context.getResources(), i);
    }
}