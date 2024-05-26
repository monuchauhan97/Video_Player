package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Fragment;

import static com.allformats.video.player.downloader.Vid_player_DS_Helper.aBoolean;
import static com.allformats.video.player.downloader.Vid_player_DS_Helper.wb;
import static java.lang.Character.MAX_VALUE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Adapter.Vid_player_Status_Adapter;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.model.Vid_player_StatusModel;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.util.Vid_player_SharedPrefs;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.util.Vid_player_Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;

public class Vid_player_RecentWapp extends Fragment implements Vid_player_Status_Adapter.OnCheckboxListener {
    LinearLayout actionLay;
    loadDataAsync async;
    LinearLayout deleteIV;
    LinearLayout downloadIV;
    RelativeLayout emptyLay;
    RecyclerView imageGrid;
    RelativeLayout loaderLay;
    LinearLayout sAccessBtn;
    Vid_player_Status_Adapter statusAdapter;
    CheckBox selectAll;
    SwipeRefreshLayout swipeToRefresh;
    int REQUEST_ACTION_OPEN_DOCUMENT_TREE = 101;
    ArrayList<Vid_player_StatusModel> f = new ArrayList<>();
    ArrayList<Vid_player_StatusModel> filesToDelete = new ArrayList<>();

    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.vid_player_recent_fragment, viewGroup, false);
        this.loaderLay = (RelativeLayout) inflate.findViewById(R.id.loaderLay);
        this.emptyLay = (RelativeLayout) inflate.findViewById(R.id.emptyLay);
        this.imageGrid = (RecyclerView) inflate.findViewById(R.id.WorkImageGrid);
        this.progressBar = inflate.findViewById(R.id.loaddata);
        wb=false;
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeToRefresh);
        this.swipeToRefresh = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public final void onRefresh() {
                Vid_player_RecentWapp.this.lambda$onCreateView$0$RecentWapp();
            }
        });
        this.actionLay = (LinearLayout) inflate.findViewById(R.id.actionLay);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.deleteIV);
        this.deleteIV = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_RecentWapp.this.lambda$onCreateView$2$RecentWapp(view);
            }
        });
        LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.downloadIV);
        this.downloadIV = linearLayout2;
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_RecentWapp.this.lambda$onCreateView$3$RecentWapp(view);
//                m53xf576e32b();
            }
        });
        CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.selectAll);
        this.selectAll = checkBox;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                Vid_player_RecentWapp.this.lambda$onCreateView$4$RecentWapp(compoundButton, z);
            }
        });
        LinearLayout linearLayout3 = (LinearLayout) inflate.findViewById(R.id.sAccessBtn);
        this.sAccessBtn = linearLayout3;
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_RecentWapp.this.lambda$onCreateView$5$RecentWapp(view);
            }
        });
        if (!Vid_player_SharedPrefs.getWATree(getActivity()).equals("")) {
            populateGrid();
        }
        return inflate;
    }

    public void lambda$onCreateView$0$RecentWapp() {
        if (!Vid_player_SharedPrefs.getWATree(getActivity()).equals("")) {
            Iterator<Vid_player_StatusModel> it = this.filesToDelete.iterator();
            while (it.hasNext()) {
                ArrayList<Vid_player_StatusModel> arrayList = this.f;
                it.next().selected = false;
                arrayList.contains(false);
            }
            this.filesToDelete.clear();
            this.selectAll.setChecked(false);
//            this.actionLay.setVisibility(View.GONE);
            populateGrid();
        }
        this.swipeToRefresh.setRefreshing(false);
    }


    public void lambda$onCreateView$2$RecentWapp(View view) {
        if (!this.filesToDelete.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomBottomSheetDialogTheme);
            final View customLayout = getLayoutInflater().inflate(R.layout.vid_player_custom_layout, null);
            builder.setView(customLayout);
            AlertDialog dialog = builder.create();
            dialog.show();

            TextView positive = (TextView) customLayout.findViewById(R.id.positive);
            TextView negative = (TextView) customLayout.findViewById(R.id.negative);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vid_player_RecentWapp.this.DeleteWhatsappstatus();
                    dialog.dismiss();
                }
            });

            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        wb=false;
        if (aBoolean) {
            populateGrid();
            aBoolean = false;
        }
    }

    public void deleteAPI29(String mediaList) {
        Uri persistedUri = getActivity().getContentResolver().getPersistedUriPermissions().get(0).getUri();
        DocumentFile documentFile = DocumentFile.fromTreeUri(getActivity(), persistedUri);
        File file = new File(mediaList);
        DocumentFile nextDocument = documentFile.findFile(file.getName());
        try {
            DocumentsContract.deleteDocument(getActivity().getContentResolver(), nextDocument.getUri());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.c = 0;
        }
    }

    char c = MAX_VALUE;

    public void DeleteWhatsappstatus() {
        aBoolean = true;
        if (Build.VERSION.SDK_INT < 30) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.v("TAG", "Permission is granted");
                    //File write logic here
                    ArrayList arrayList = new ArrayList();
                    Iterator<Vid_player_StatusModel> it = this.filesToDelete.iterator();

                    while (it.hasNext()) {
                        Vid_player_StatusModel next = it.next();

                        Log.e("TAG", "lambda$null$1$RecentWapp: " + next.getFilePath());
                        DocumentFile fromSingleUri = DocumentFile.fromSingleUri(getActivity(), next.getFileUri());
                        try {
                            deleteAPI29(next.getFilePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                            this.c = 0;
                        }
                        arrayList.add(next);
                        if (this.c != 0) {
                            this.c = 1;
                        }
                    }
                    if (c == 0) {
                        Toast.makeText(getContext(), getResources().getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
                        this.filesToDelete.clear();
                    } else if (c == 1) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                        this.filesToDelete.clear();
                        Iterator it2 = arrayList.iterator();
                        while (it2.hasNext()) {
                            this.f.remove((Vid_player_StatusModel) it2.next());
                        }
                        this.statusAdapter.notifyDataSetChanged();
                    }
                    this.actionLay.setVisibility(View.GONE);
                    this.selectAll.setChecked(false);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101010);

                }
            }
        } else {
            ArrayList arrayList = new ArrayList();
            Iterator<Vid_player_StatusModel> it = this.filesToDelete.iterator();
            char c = '\uFFFF';
            while (it.hasNext()) {
                Vid_player_StatusModel next = it.next();
                DocumentFile fromSingleUri = DocumentFile.fromSingleUri(getActivity(), Uri.parse(next.getFilePath()));
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
            if (c == 0) {
                Toast.makeText(getContext(), getResources().getString(R.string.delete_error), Toast.LENGTH_SHORT).show();
                this.filesToDelete.clear();
            } else if (c == 1) {
                Toast.makeText(getActivity(), getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                this.filesToDelete.clear();
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    this.f.remove((Vid_player_StatusModel) it2.next());
                }
                this.statusAdapter.notifyDataSetChanged();
            }
            this.actionLay.setVisibility(View.GONE);
            this.selectAll.setChecked(false);
        }
    }

    public void lambda$onCreateView$3$RecentWapp(View view) {
        if (!this.filesToDelete.isEmpty()) {
            char c = '\uFFFF';
            ArrayList arrayList = new ArrayList();
            Iterator<Vid_player_StatusModel> it = this.filesToDelete.iterator();
            Handler handler1 = new Handler();
            Vid_player_StatusModel statusModel;
            for (int a = 0; a < filesToDelete.size(); a++) {
                statusModel = filesToDelete.get(a);
                Log.e("again: ", new File(statusModel.getFilePath()).getAbsolutePath());
                if (Vid_player_Utils.download(getActivity(), statusModel.getFilePath())) {
                    arrayList.add(statusModel);
                }
                c = 0;
            }
            this.filesToDelete.clear();
            this.selectAll.setChecked(false);
            Toast.makeText(getActivity(), getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            Iterator it2 = arrayList.iterator();
            this.actionLay.setVisibility(View.GONE);
            this.statusAdapter.notifyDataSetChanged();
            while (it2.hasNext()) {
                ArrayList<Vid_player_StatusModel> arrayList2 = this.f;
                ((Vid_player_StatusModel) it2.next()).selected = false;
                arrayList2.contains(false);
            }
            this.selectAll.setChecked(false);
        }
    }


    public void lambda$onCreateView$4$RecentWapp(CompoundButton compoundButton, boolean z) {
        if (compoundButton.isPressed()) {
            this.filesToDelete.clear();

            int i = 0;
            while (true) {
                if (i >= this.f.size()) {
                    break;
                } else if (!this.f.get(i).selected) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            if (z) {
                for (int i2 = 0; i2 < this.f.size(); i2++) {
                    this.f.get(i2).selected = true;
                    this.filesToDelete.add(this.f.get(i2));
                }
                this.selectAll.setChecked(true);

            } else {
                for (int i3 = 0; i3 < this.f.size(); i3++) {
                    this.f.get(i3).selected = false;
                }
                this.actionLay.setVisibility(View.GONE);
            }
            statusAdapter.notifyDataSetChanged();
        }
    }

    public void lambda$onCreateView$5$RecentWapp(View view) {
        Intent intent;
        if (Vid_player_Utils.appInstalledOrNot(getActivity(), "com.whatsapp")) {
            StorageManager storageManager = (StorageManager) getActivity().getSystemService(Context.STORAGE_SERVICE);
            String whatsupFolder = getWhatsupFolder();
            if (Build.VERSION.SDK_INT >= 29) {
                intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                String replace = ((Uri) intent.getParcelableExtra("android.provider.extra.INITIAL_URI")).toString().replace("/root/", "/document/");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(replace + "%3A" + whatsupFolder));
            } else {
                intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse("content://com.android.externalstorage.documents/document/primary%3A" + whatsupFolder));
            }
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(intent, this.REQUEST_ACTION_OPEN_DOCUMENT_TREE);
            return;
        }
        Toast.makeText(getActivity(), "Please Install WhatsApp For Download Status!!!!!", Toast.LENGTH_SHORT).show();
    }

    public void populateGrid() {
        loadDataAsync loaddataasync = new loadDataAsync();
        this.async = loaddataasync;
        loaddataasync.execute(new Void[0]);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadDataAsync loaddataasync = this.async;
        if (loaddataasync != null) {
            loaddataasync.cancel(true);
        }
    }

    public static ArrayList<DocumentFile> getFromSdcard(Context c) {
        ArrayList<DocumentFile> inFiles = new ArrayList<>();
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(c, Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"));
        for (int i = 0; i < fromTreeUri.listFiles().length; i++) {
            if (!fromTreeUri.listFiles()[i].getUri().toString().contains(".nomedia")) {
                inFiles.add(fromTreeUri.listFiles()[i]);
            }
        }
        return inFiles;
    }

    public ArrayList<DocumentFile> getFromSdcard(File parentDir) {
        ArrayList<DocumentFile> inFiles = new ArrayList<>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                Log.e("check", file.getName());
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".gif") || file.getName().endsWith(".mp4") && !file.getName().contains(".nomedia")) {
                    if (!inFiles.contains(file))
                        inFiles.add(DocumentFile.fromFile(file));
                }
            }
        }
        return inFiles;
    }

    public String getWhatsupFolder() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append(File.separator);
        sb.append("Android/media/com.whatsapp/WhatsApp");
        sb.append(File.separator);
        sb.append("Media");
        sb.append(File.separator);
        sb.append(".Statuses");
        return new File(sb.toString()).isDirectory() ? "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses" : "WhatsApp%2FMedia%2F.Statuses";
    }

    final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);

        if (i == 10 && i2 == 10) {
            this.f = new ArrayList<>();
            ArrayList<DocumentFile> from;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                from = Vid_player_RecentWapp.this.getFromSdcard(new File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION));
            } else {
                from = Vid_player_RecentWapp.this.getFromSdcard(getContext());
            }
            DocumentFile[] fromSdcard = new DocumentFile[from.size()];
            for (int j = 0; j < from.size(); j++) {
                fromSdcard[j] = from.get(j);
                Vid_player_StatusModel vid_player_statusModel = new Vid_player_StatusModel(fromSdcard[j].getUri().toString());
                vid_player_statusModel.setFileUri(fromSdcard[j].getUri());
                this.f.add(vid_player_statusModel);
            }
            Vid_player_Status_Adapter recentAdapter2 = new Vid_player_Status_Adapter(this.selectAll,((BaseActivity) getActivity()), this.f, this, AdsUtility.config.listNativeCount);
            this.statusAdapter = recentAdapter2;

            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            recentAdapter2.notifyDataSetChanged();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = recentAdapter2.getItemViewType(position);
                    if (itemViewType == NativeAdsAdapter.AD) {
                        return 2;
                    }
                    return 1;
                }
            });
            this.imageGrid.setLayoutManager(layoutManager);
            this.imageGrid.setAdapter(recentAdapter2);

            this.actionLay.setVisibility(View.GONE);
            this.selectAll.setChecked(false);
        }
        if (i == this.REQUEST_ACTION_OPEN_DOCUMENT_TREE && i2 == -1) {
            Uri data = intent.getData();
            Log.e("onActivityResult: ", "" + intent.getData());
            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    requireContext().getContentResolver().takePersistableUriPermission(data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Vid_player_SharedPrefs.setWATree(getActivity(), data.toString());
            populateGrid();
        }
    }

    @Override
    public void onCheckboxListener(View view, List<Vid_player_StatusModel> list) {
        this.filesToDelete.clear();
        for (Vid_player_StatusModel vidplayerStatusModel : list) {
            if (vidplayerStatusModel.isSelected()) {
                this.filesToDelete.add(vidplayerStatusModel);
            }
        }
        if (this.filesToDelete.size() == this.f.size()) {
            this.selectAll.setChecked(true);
        }
        if (!this.filesToDelete.isEmpty()) {
            this.actionLay.setVisibility(View.VISIBLE);
            return;
        }
        this.selectAll.setChecked(false);
        this.actionLay.setVisibility(View.GONE);
    }

    public class loadDataAsync extends AsyncTask<Void, Void, Void> {
        DocumentFile[] allFiles;

        loadDataAsync() {
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            Vid_player_RecentWapp.this.loaderLay.setVisibility(View.VISIBLE);
            Vid_player_RecentWapp.this.imageGrid.setVisibility(View.GONE);
            Vid_player_RecentWapp.this.sAccessBtn.setVisibility(View.GONE);
            Vid_player_RecentWapp.this.emptyLay.setVisibility(View.GONE);
            Vid_player_RecentWapp.this.progressBar.setVisibility(View.VISIBLE);
        }

        public Void doInBackground(Void... voidArr) {
            this.allFiles = null;
            Vid_player_RecentWapp.this.f = new ArrayList<>();
            ArrayList<DocumentFile> from;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                from = Vid_player_RecentWapp.this.getFromSdcard(new File(Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses"));
            } else {
                from = Vid_player_RecentWapp.this.getFromSdcard(getContext());
            }
            DocumentFile[] fromSdcard = new DocumentFile[from.size()];
            for (int j = 0; j < from.size(); j++) {
                fromSdcard[j] = from.get(j);
            }
            this.allFiles = fromSdcard;
            Arrays.sort(fromSdcard, Vid_player_RecentWapploadDataAsync.INSTANCE);

            for (int j = 0; j < from.size(); j++) {
                Vid_player_StatusModel vid_player_statusModel = new Vid_player_StatusModel(allFiles[j].getUri().toString());
                vid_player_statusModel.setFileUri(allFiles[j].getUri());
                Vid_player_RecentWapp.this.f.add(vid_player_statusModel);
            }
            return null;
        }

        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            new Handler().postDelayed(new Runnable() {
                @Override
                public final void run() {
                    loadDataAsync.this.lambda$onPostExecute$1$RecentWapp$loadDataAsync();
                }
            }, 1000L);
        }

        public void lambda$onPostExecute$1$RecentWapp$loadDataAsync() {
            if (Vid_player_RecentWapp.this.getActivity() != null) {
                if (!(Vid_player_RecentWapp.this.f == null || Vid_player_RecentWapp.this.f.size() == 0)) {
                    Vid_player_Status_Adapter statusAdapter = new Vid_player_Status_Adapter(selectAll, ((BaseActivity) getActivity()), f, Vid_player_RecentWapp.this, AdsUtility.config.listNativeCount);
                    Vid_player_RecentWapp.this.statusAdapter = statusAdapter;
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            int itemViewType = statusAdapter.getItemViewType(position);
                            if (itemViewType == NativeAdsAdapter.AD) {
                                return 2;
                            }
                            return 1;
                        }
                    });
                    Vid_player_RecentWapp.this.imageGrid.setLayoutManager(layoutManager);
                    Vid_player_RecentWapp.this.imageGrid.setAdapter(statusAdapter);

                    Vid_player_RecentWapp.this.imageGrid.setVisibility(View.VISIBLE);
                }
                Vid_player_RecentWapp.this.loaderLay.setVisibility(View.GONE);
            }
            Vid_player_RecentWapp.this.progressBar.setVisibility(View.GONE);
            if (Vid_player_RecentWapp.this.f == null || Vid_player_RecentWapp.this.f.size() == 0) {
                Vid_player_RecentWapp.this.emptyLay.setVisibility(View.VISIBLE);
            } else {
                Vid_player_RecentWapp.this.emptyLay.setVisibility(View.GONE);
            }
        }
    }
}
