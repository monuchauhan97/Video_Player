package com.allformats.video.player.downloader.video_player.Adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity.Vid_player_OpenFileActivity;
import com.allformats.video.player.downloader.video_player.Activity.Vid_player_HomeActivity;
import com.allformats.video.player.downloader.video_player.Activity.Vid_player_VideoPlayerActivity;
import com.allformats.video.player.downloader.video_player.Activity.Vid_player_VideolistActivity;
import com.allformats.video.player.downloader.video_player.Database.Vid_player_Database;
import com.allformats.video.player.downloader.video_player.Dialog.Vid_player_VideoDetailsDialog;
import com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;
import com.allformats.video.player.downloader.video_player.Fragment.Vid_player_RecentFragment;
import com.allformats.video.player.downloader.video_player.Fragment.Vid_player_VideoFragment;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_MyEvent;
import com.allformats.video.player.downloader.video_player.Util.Vid_player_Utils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;

public class Vid_player_VideoAdapter extends NativeAdsAdapter {
    public final Vid_player_Database vidplayerDatabase;
    Activity activity;
    int i;
    public ArrayList<Uri> removableList = new ArrayList<>();
    public String removableLists = new String();
    LayoutInflater inflater;
    public ArrayList<Vid_player_MediaData> mediadata;
    int type;
    public int currentpossition;

    public Vid_player_VideoAdapter(BaseActivity fragmentActivity, ArrayList<Vid_player_MediaData> arrayList, int i, int i2, int adspos) {
        super(fragmentActivity, adspos);
        this.activity = fragmentActivity;
        this.mediadata = arrayList;
        this.i = i;
        this.type = i2;
        this.vidplayerDatabase = new Vid_player_Database(fragmentActivity);
        this.inflater = LayoutInflater.from(fragmentActivity);
    }

    @Override
    public void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int position) {
        ViewHolder viewHolder = (ViewHolder) baseHolder;
        if (this.i == 1) {
            int dimensionPixelSize = this.activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            viewHolder.videothumb.getLayoutParams().width = (displayMetrics.widthPixels - dimensionPixelSize) / 2;
            viewHolder.videothumb.getLayoutParams().height = (displayMetrics.widthPixels - dimensionPixelSize) / 5;
        }

        Glide.with(this.activity).load(this.mediadata.get(position).getPath()).into(viewHolder.videothumb);
        viewHolder.videotitle.setText(this.mediadata.get(position).getName());
        viewHolder.videosize.setText(Vid_player_Utils.formateSize(Long.parseLong(this.mediadata.get(position).getLength())));
        viewHolder.duration.setText(this.mediadata.get(position).getDuration());
        viewHolder.videooption.setOnClickListener(new View$OnClickListenerC0983AnonymousClass1(position, viewHolder));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean z = true;
                Vid_player_VideoPlayerActivity.setarray(Vid_player_VideoAdapter.this.mediadata);
                if (Vid_player_VideoAdapter.this.type == 0) {
                    ((Vid_player_HomeActivity) Vid_player_VideoAdapter.this.activity).myStartActivity(Vid_player_VideoPlayerActivity.getIntent(Vid_player_VideoAdapter.this.activity, Vid_player_VideoAdapter.this.mediadata, position));
                } else if (Vid_player_VideoAdapter.this.type == 1) {
                    ((Vid_player_VideolistActivity) Vid_player_VideoAdapter.this.activity).myStartActivity(Vid_player_VideoPlayerActivity.getIntent(Vid_player_VideoAdapter.this.activity, Vid_player_VideoAdapter.this.mediadata, position));
                } else {
                    ((Vid_player_HomeActivity) Vid_player_VideoAdapter.this.activity).myStartActivity(Vid_player_VideoPlayerActivity.getIntent(Vid_player_VideoAdapter.this.activity, Vid_player_VideoAdapter.this.mediadata, position));
                }
                List list = (List) new Gson().fromJson(Vid_player_DS_Helper.getRecentPlay(), new TypeToken<List<Vid_player_MediaData>>() {
                }.getType());
                if (list != null) {
                    int i2 = 0;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= list.size()) {
                            z = false;
                            break;
                        } else if (((Vid_player_MediaData) list.get(i3)).getPath().equals(Vid_player_VideoAdapter.this.mediadata.get(position).getPath())) {
                            i2 = i3;
                            break;
                        } else {
                            i3++;
                        }
                    }
                    if (z) {
                        list.remove(i2);
                        list.add(Vid_player_VideoAdapter.this.mediadata.get(position));
                        Vid_player_DS_Helper.putRecentPlay(new Gson().toJson(list));
                    } else {
                        list.add(Vid_player_VideoAdapter.this.mediadata.get(position));
                        Vid_player_DS_Helper.putRecentPlay(new Gson().toJson(list));
                    }
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(Vid_player_VideoAdapter.this.mediadata.get(position));
                    Vid_player_DS_Helper.putRecentPlay(new Gson().toJson(arrayList));
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Vid_player_MyEvent eventBus = new Vid_player_MyEvent();
                        eventBus.setType(3);
                        eventBus.setValue(0);
                        org.greenrobot.eventbus.EventBus.getDefault().post(eventBus);
                    }
                }, 1500L);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType) {
        View view = this.inflater.inflate(R.layout.vid_player_item_video, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int itemCount() {
        return this.mediadata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView duration;
        ImageView videooption;
        TextView videosize;
        ImageView videothumb;
        TextView videotitle;

        public ViewHolder(View view) {
            super(view);
            this.videotitle = (TextView) view.findViewById(R.id.video_title);
            this.videosize = (TextView) view.findViewById(R.id.video_size);
            this.duration = (TextView) view.findViewById(R.id.duration);
            this.videothumb = (ImageView) view.findViewById(R.id.video_thumb);
            this.videooption = (ImageView) view.findViewById(R.id.video_option);
        }
    }

    public class View$OnClickListenerC0983AnonymousClass1 implements View.OnClickListener {
        final int pos;
        ViewHolder holder;

        View$OnClickListenerC0983AnonymousClass1(int viewHolder, ViewHolder holder) {
            this.pos = viewHolder;
            this.holder = holder;
        }

        public void newDeleteMethod(String str) {
            removableList.add(Uri.parse(str));
            removableLists = str;
            final ArrayList arrayList = new ArrayList();
            MediaScannerConnection.scanFile(activity, new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String str2, Uri uri) {
                    PendingIntent pendingIntent;
                    arrayList.add(uri);
                    if (Build.VERSION.SDK_INT >= 30) {
                        pendingIntent = MediaStore.createDeleteRequest(activity.getContentResolver(), arrayList);
                    } else {
                        pendingIntent = null;
                    }
                    try {
                        activity.startIntentSenderForResult(pendingIntent.getIntentSender(), Vid_player_OpenFileActivity.DELETE_REQUEST_CODE, null, 0, 0, 0, null);
                        Vid_player_VideoAdapter.this.mediadata.remove(View$OnClickListenerC0983AnonymousClass1.this.pos);
                        Vid_player_VideoAdapter.this.notifyItemRemoved(View$OnClickListenerC0983AnonymousClass1.this.pos);
                        Vid_player_VideoAdapter.this.notifyDataSetChanged();
                    } catch (Exception unused) {
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            PopupWindow mypopupWindow;
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.vid_player_video_menus_layout, null);
            mypopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            mypopupWindow.setFocusable(true);
            mypopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mypopupWindow.setOutsideTouchable(true);
            mypopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            mypopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            mypopupWindow.setContentView(v);
            int[] values = new int[2];
            holder.videooption.getLocationInWindow(values);
            int positionOfIcon = values[1];
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            int height = (displayMetrics.heightPixels * 2) / 3;
            if (positionOfIcon > height) {
                mypopupWindow.showAsDropDown(holder.videooption, 0, -320);
            } else {
                mypopupWindow.showAsDropDown(holder.videooption, 0, 0);
            }
            LinearLayout Delete = (LinearLayout) v.findViewById(R.id.delete);
            LinearLayout Share = (LinearLayout) v.findViewById(R.id.share);
            LinearLayout Detail = (LinearLayout) v.findViewById(R.id.detail);
            view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mypopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            mypopupWindow.setHeight(v.getMeasuredHeight());
            boolean z = true;
            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mypopupWindow.dismiss();
                    currentpossition = pos;
                    if (Build.VERSION.SDK_INT >= 30 && type != 2) {
                        newDeleteMethod(Vid_player_VideoAdapter.this.mediadata.get(pos).getPath());
                        return;
                    }
                    if (Vid_player_VideoAdapter.this.type != 2) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogDanger));
                        builder.setTitle("Delete Video");
                        builder.setMessage("Are you sure you have to Delete " + Vid_player_VideoAdapter.this.mediadata.get(View$OnClickListenerC0983AnonymousClass1.this.pos).getName() + " ?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean z2;
                                notifyDataSetChanged();
                                File file = new File(Vid_player_VideoAdapter.this.mediadata.get(View$OnClickListenerC0983AnonymousClass1.this.pos).getPath());
                                try {
                                    if (file.exists()) {
                                        if (type != 2) {
                                            if (Build.VERSION.SDK_INT >= 30 && type != 2) {
//                                                newDeleteMethod(Vid_player_VideoAdapter.this.mediadata.get(pos).getPath());
//                                                Vid_player_VideoAdapter.this.notifyItemRemoved(View$OnClickListenerC0983AnonymousClass1.this.pos);
//                                                Vid_player_VideoAdapter.this.mediadata.remove(View$OnClickListenerC0983AnonymousClass1.this.pos);
                                            } else {
                                                if (file.delete()) {
                                                    Vid_player_VideoAdapter.this.notifyItemRemoved(View$OnClickListenerC0983AnonymousClass1.this.pos);
                                                    Vid_player_VideoAdapter.this.mediadata.remove(View$OnClickListenerC0983AnonymousClass1.this.pos);


                                                    if (Vid_player_VideoAdapter.this.type == 1) {
                                                        Vid_player_MyEvent eventBus = new Vid_player_MyEvent();
                                                        eventBus.setType(1);
                                                        eventBus.setValue(0);
                                                        org.greenrobot.eventbus.EventBus.getDefault().post(eventBus);
                                                    }
                                                    List list = (List) new Gson().fromJson(Vid_player_DS_Helper.getRecentPlay(), new TypeToken<List<Vid_player_MediaData>>() {
                                                    }.getType());
                                                    if (type == 2) {
                                                        if (list != null) {
                                                            int i2 = 0;
                                                            while (true) {
                                                                if (i2 >= list.size()) {
                                                                    i2 = 0;
                                                                    z2 = false;
                                                                    break;
                                                                } else if (((Vid_player_MediaData) list.get(i2)).getPath().equals(Vid_player_VideoAdapter.this.mediadata.get(View$OnClickListenerC0983AnonymousClass1.this.pos).getPath())) {
                                                                    z2 = true;
                                                                    break;
                                                                } else {
                                                                    i2++;
                                                                }
                                                            }
                                                            if (z2) {
                                                                list.remove(i2);
                                                                Vid_player_DS_Helper.putRecentPlay(new Gson().toJson(list));
                                                            }
                                                        }
                                                        notifyDataSetChanged();
                                                    }
                                                    if (Vid_player_VideoAdapter.this.type == 0) {
                                                        if (list.size() == 0) {
                                                            Vid_player_RecentFragment.ivnodata.setVisibility(View.VISIBLE);
                                                            Vid_player_VideoFragment.videorecycler.setVisibility(View.GONE);
                                                        }
                                                    } else if (Vid_player_VideoAdapter.this.type == 1 && list.size() == 0) {
                                                        Vid_player_RecentFragment.ivnodata.setVisibility(View.VISIBLE);
                                                        Vid_player_VideolistActivity.videolist.setVisibility(View.GONE);
                                                    }


                                                }
                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        List list = (List) new Gson().fromJson(Vid_player_DS_Helper.getRecentPlay(), new TypeToken<List<Vid_player_MediaData>>() {
                        }.getType());
                        if (list == null) {
                            Vid_player_VideoAdapter.this.mediadata.remove(View$OnClickListenerC0983AnonymousClass1.this.pos);
                            if (list.size() == 0) {
                                Vid_player_RecentFragment.ivnodata.setVisibility(View.VISIBLE);
                                Vid_player_RecentFragment.recentrecycler.setVisibility(View.GONE);
                            }
                        } else {
                            if (z) {
                                notifyDataSetChanged();
                                mediadata.remove(pos);
                                Vid_player_DS_Helper.putRecentPlay(new Gson().toJson(mediadata));
                                notifyDataSetChanged();
                                if (mediadata.size() == 0) {
                                    Vid_player_RecentFragment.ivnodata.setVisibility(View.VISIBLE);
                                    Vid_player_RecentFragment.recentrecycler.setVisibility(View.GONE);
                                } else {
                                    Vid_player_RecentFragment.ivnodata.setVisibility(View.GONE);
                                    Vid_player_RecentFragment.recentrecycler.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            });
            Detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mypopupWindow.dismiss();
                    Vid_player_VideoDetailsDialog.getInstance(Vid_player_VideoAdapter.this.mediadata.get(View$OnClickListenerC0983AnonymousClass1.this.pos)).show(((AppCompatActivity) Vid_player_VideoAdapter.this.activity).getSupportFragmentManager(), "");
                }
            });
            Share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mypopupWindow.dismiss();
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("video_downshot/*");
                    intent.putExtra("android.intent.extra.STREAM", Uri.parse(mediadata.get(pos).getPath()));
                    intent.putExtra("android.intent.extra.TEXT", "" );
                    intent.putExtra("android.intent.extra.SUBJECT", mediadata.get(pos).getName());
                    Vid_player_VideoAdapter.this.activity.startActivity(Intent.createChooser(intent, "Share Video"));
                }
            });
            mypopupWindow.showAsDropDown(view, -153, 0);
        }
    }

    @Override
    public int getItemCount() {
        return this.mediadata.size();
    }
}