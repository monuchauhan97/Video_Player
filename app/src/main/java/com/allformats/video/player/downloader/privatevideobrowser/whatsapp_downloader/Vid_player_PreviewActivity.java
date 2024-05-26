package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader;

import static com.allformats.video.player.downloader.Vid_player_DS_Helper.aBoolean;
import static java.lang.Character.MAX_VALUE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager.widget.ViewPager;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity.Vid_player_OpenFileActivity;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Adapter.Vid_player_PreviewAdapter;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.model.Vid_player_StatusModel;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.util.Vid_player_SharedPrefs;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.util.Vid_player_Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_PreviewActivity extends BaseActivity {
    ImageView backIV;
    LinearLayout container;
    LinearLayout deleteIV;
    LinearLayout downloadIV;
    ArrayList<Vid_player_StatusModel> imageList;
    int position;
    Vid_player_PreviewAdapter vidplayerPreviewAdapter;
    LinearLayout shareIV;
    String statusDownload;
    ViewPager viewPager;
//    LinearLayout wAppIV;
    ArrayList<Vid_player_StatusModel> filesToDelete = new ArrayList<>();

    public void lambda$null$1$RecentWapp(/*DialogInterface dialogInterface, int i*/) {
        ArrayList arrayList = new ArrayList();
        Iterator<Vid_player_StatusModel> it = this.filesToDelete.iterator();
        char c = '\uFFFF';
        while (it.hasNext()) {
            Vid_player_StatusModel next = it.next();
            DocumentFile fromSingleUri = DocumentFile.fromSingleUri(this, Uri.parse(next.getFilePath()));
            if (!fromSingleUri.exists() || !fromSingleUri.delete()) {
                c = 0;
            } else {
                arrayList.add(next);
                if (c != 0) {
                    c = 1;
                } else {
                    return;
                }
            }
        }
        this.filesToDelete.clear();
//        Iterator it2 = arrayList.iterator();
//        while (it2.hasNext()) {
//            this.imageList.remove((Vid_player_StatusModel) it2.next());
        Vid_player_PreviewActivity.this.delete(0);
        this.vidplayerPreviewAdapter.notifyDataSetChanged();
//        }

        if (c == 0) {
            Toast.makeText(this, getResources().getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
        } else if (c == 1) {
            Toast.makeText(this, getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
        }
       /* this.actionLay.setVisibility(View.GONE);
        this.selectAll.setChecked(false);*/
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        char c = MAX_VALUE;

        public void deleteAPI29(String mediaList) {
            Uri persistedUri = Vid_player_PreviewActivity.this.getContentResolver().getPersistedUriPermissions().get(0).getUri();
            DocumentFile documentFile = DocumentFile.fromTreeUri(Vid_player_PreviewActivity.this, persistedUri);
            File file = new File(mediaList);
            DocumentFile nextDocument = documentFile.findFile(file.getName());
            try {
                DocumentsContract.deleteDocument(Vid_player_PreviewActivity.this.getContentResolver(), nextDocument.getUri());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                this.c = 0;
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.backIV:
                    Vid_player_PreviewActivity.this.onBackPressed();
                    return;
                case R.id.deleteIV:
                    if (Vid_player_PreviewActivity.this.imageList.size() > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Vid_player_PreviewActivity.this, R.style.CustomBottomSheetDialogTheme);
                        final View customLayout = getLayoutInflater().inflate(R.layout.vid_player_custom_layout, null);
                        builder.setView(customLayout);
                        AlertDialog dialog = builder.create();
                        TextView positive = (TextView) customLayout.findViewById(R.id.positive);
                        TextView negative = (TextView) customLayout.findViewById(R.id.negative);

                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                aBoolean = true;
                                if (Vid_player_PreviewActivity.this.statusDownload.equals("download")) {
                                    File file = new File(Vid_player_PreviewActivity.this.imageList.get(Vid_player_PreviewActivity.this.viewPager.getCurrentItem()).getFilePath());

                                    if (file.exists()) {
                                        if (Build.VERSION.SDK_INT >= 30) {
                                            newDeleteMethod(file.getAbsolutePath(), Vid_player_PreviewActivity.this);
                                            return;
                                        }

                                        return;
                                    }
                                    return;
                                }

                                DocumentFile fromSingleUri = DocumentFile.fromSingleUri(Vid_player_PreviewActivity.this, Uri.parse(Vid_player_PreviewActivity.this.imageList.get(Vid_player_PreviewActivity.this.viewPager.getCurrentItem()).getFilePath()));
                                if (fromSingleUri.exists()) {
                                    if (Build.VERSION.SDK_INT >= 30) {
                                        filesToDelete.add(Vid_player_PreviewActivity.this.imageList.get(Vid_player_PreviewActivity.this.viewPager.getCurrentItem()));
                                        lambda$null$1$RecentWapp();
//                                        Toast.makeText(Vid_player_PreviewActivity.this, "Can't Able to Delete Status..", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                        Log.v("TAG", "Permission is granted");
                                        //File write logic here
                                        ArrayList arrayList = new ArrayList();

                                        Vid_player_StatusModel next = Vid_player_PreviewActivity.this.imageList.get(Vid_player_PreviewActivity.this.viewPager.getCurrentItem());

                                        Log.e("TAG", "lambda$null$1$RecentWapp: " + next.getFilePath());
                                        DocumentFile documentFile = DocumentFile.fromSingleUri(Vid_player_PreviewActivity.this, next.getFileUri());
                                        try {
                                            deleteAPI29(next.getFilePath());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            c = 0;
                                        }
                                        arrayList.add(next);
                                        if (c != 0) {
                                            c = 1;
                                        }

                                        if (c == 0) {
                                            Toast.makeText(Vid_player_PreviewActivity.this, getResources().getString(R.string.delete_error), Toast.LENGTH_SHORT).show();

                                        } else if (c == 1) {
                                            Toast.makeText(Vid_player_PreviewActivity.this, getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();

//                                                Iterator it2 = arrayList.iterator();
//                                                while (it2.hasNext()) {
//                                                    Vid_player_PreviewActivity.this.imageList.remove((Vid_player_StatusModel) it2.next());
                                            Vid_player_PreviewActivity.this.delete(0);
                                            Vid_player_PreviewActivity.this.vidplayerPreviewAdapter.notifyDataSetChanged();
//                                                }
                                        }
                                    } else {
                                        ActivityCompat.requestPermissions(Vid_player_PreviewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101010);

                                    }
                                }
                            }
                        });


                        negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    return;
                case R.id.downloadIV:
                    if (Vid_player_PreviewActivity.this.imageList.size() > 0) {
                        try {
                            Vid_player_PreviewActivity vidplayerPreviewActivity = Vid_player_PreviewActivity.this;
                            Vid_player_Utils.download(vidplayerPreviewActivity, vidplayerPreviewActivity.imageList.get(Vid_player_PreviewActivity.this.viewPager.getCurrentItem()).getFilePath());
                            Vid_player_PreviewActivity vidplayerPreviewActivity2 = Vid_player_PreviewActivity.this;
                            Toast.makeText(vidplayerPreviewActivity2, vidplayerPreviewActivity2.getResources().getString(R.string.saved_success), Toast.LENGTH_SHORT).show();
                            return;
                        } catch (Exception unused) {
                            Toast.makeText(Vid_player_PreviewActivity.this, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Vid_player_PreviewActivity.this.finish();
                        return;
                    }
               /* case R.id.repostIV:
                    Vid_player_PreviewActivity vidplayerPreviewActivity3 = Vid_player_PreviewActivity.this;
                    Vid_player_Utils.repostWhatsApp(vidplayerPreviewActivity3, Vid_player_Utils.isVideoFile(vidplayerPreviewActivity3, vidplayerPreviewActivity3.imageList.get(Vid_player_PreviewActivity.this.viewPager.getCurrentItem()).getFilePath()), Vid_player_PreviewActivity.this.imageList.get(Vid_player_PreviewActivity.this.viewPager.getCurrentItem()).getFilePath());
                    return;*/
                case R.id.shareIV:
                    if (Vid_player_PreviewActivity.this.imageList.size() > 0) {
                        Vid_player_PreviewActivity vidplayerPreviewActivity4 = Vid_player_PreviewActivity.this;
                        Vid_player_Utils.shareFile(vidplayerPreviewActivity4, Vid_player_Utils.isVideoFile(vidplayerPreviewActivity4, vidplayerPreviewActivity4.imageList.get(Vid_player_PreviewActivity.this.viewPager.getCurrentItem()).getFilePath()), Vid_player_PreviewActivity.this.imageList.get(Vid_player_PreviewActivity.this.viewPager.getCurrentItem()).getFilePath());
                        return;
                    }
                    Vid_player_PreviewActivity.this.finish();
                    return;
                default:
                    return;
            }
        }
    };


    public static void newDeleteMethod(String str, Activity context) {
        ArrayList<Uri> removableList = new ArrayList<>();
        removableList.add(Uri.parse(str));
        final ArrayList arrayList = new ArrayList();
        MediaScannerConnection.scanFile(context, new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String str2, Uri uri) {
                PendingIntent pendingIntent;
                arrayList.add(uri);
                if (Build.VERSION.SDK_INT >= 30) {
                    pendingIntent = MediaStore.createDeleteRequest(context.getContentResolver(), arrayList);
                } else {
                    pendingIntent = null;
                }
                try {
                    context.startIntentSenderForResult(pendingIntent.getIntentSender(), Vid_player_OpenFileActivity.DELETE_REQUEST_CODE, null, 0, 0, 0, null);
                } catch (Exception unused) {
                    Toast.makeText(context, "Unable to Delete File..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_preview);
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black, getTheme()));
        } else if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }
        Vid_player_Utils.setLanguage(this, Vid_player_SharedPrefs.getLang(this));
        this.backIV = (ImageView) findViewById(R.id.backIV);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.shareIV = (LinearLayout) findViewById(R.id.shareIV);
        this.downloadIV = (LinearLayout) findViewById(R.id.downloadIV);
        this.deleteIV = (LinearLayout) findViewById(R.id.deleteIV);
//        this.wAppIV = (LinearLayout) findViewById(R.id.repostIV);

        this.imageList = getIntent().getParcelableArrayListExtra("images");

        this.position = getIntent().getIntExtra("position", 0);
        String stringExtra = getIntent().getStringExtra("statusdownload");
        this.statusDownload = stringExtra;
        if (stringExtra.equals("download")) {
            this.downloadIV.setVisibility(View.GONE);
        } else {
            this.downloadIV.setVisibility(View.VISIBLE);
        }

        Vid_player_PreviewAdapter vidplayerPreviewAdapter = new Vid_player_PreviewAdapter(this, this.imageList);
        this.vidplayerPreviewAdapter = vidplayerPreviewAdapter;
        this.viewPager.setAdapter(vidplayerPreviewAdapter);
        this.viewPager.setCurrentItem(this.position);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
            }
        });
        this.downloadIV.setOnClickListener(this.clickListener);
        this.shareIV.setOnClickListener(this.clickListener);
        this.deleteIV.setOnClickListener(this.clickListener);
        this.backIV.setOnClickListener(this.clickListener);
//        this.wAppIV.setOnClickListener(this.clickListener);
    }

    @Override
    public void onBackPressed() {
        if (AdsUtility.config.adOnBack) {
            showInterstitial(new BaseCallback() {
                @Override
                public void completed() {
                    Vid_player_PreviewActivity.super.onBackPressed();
                }
            });
        } else {
            Vid_player_PreviewActivity.super.onBackPressed();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void delete(int i) {
        if (this.imageList.size() > 0 && this.viewPager.getCurrentItem() < this.imageList.size()) {
            i = this.viewPager.getCurrentItem();
        }
        this.imageList.remove(this.viewPager.getCurrentItem());
        Vid_player_PreviewAdapter vidplayerPreviewAdapter = new Vid_player_PreviewAdapter(this, this.imageList);
        this.vidplayerPreviewAdapter = vidplayerPreviewAdapter;
        this.viewPager.setAdapter(vidplayerPreviewAdapter);
        setResult(10, new Intent());
        if (this.imageList.size() > 0) {
            this.viewPager.setCurrentItem(i);
        } else {
            finish();
        }
    }
}
