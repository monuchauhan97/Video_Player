package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Fragment;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonElement;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity.Vid_player_PhotosVideoDownloadActivity;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity.Vid_player_ReelDownlodActivity;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.retrofit.Responseapi;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.retrofit.RetrofitClient;

import plugin.adsdk.service.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Vid_player_HomeFragment extends Fragment {
    TextView btn_find;
    TextView btn_paste;
    CardView cardView;
    Context context;
    EditText et_link;
    FrameLayout frameLayout;
    ProgressDialog progressDialog;

    public Vid_player_HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.vid_player_fragment_insta_home, viewGroup, false);
        ((BaseActivity) getActivity()).nativeAd(inflate.findViewById(R.id.native_ad_container));
        findid(inflate);
        onclick();
        return inflate;
    }

    private void findid(View view) {
        this.et_link = (EditText) view.findViewById(R.id.et_link);
        this.btn_find = (TextView) view.findViewById(R.id.btn_find);
        this.btn_paste = (TextView) view.findViewById(R.id.btn_paste);
        this.frameLayout = (FrameLayout) view.findViewById(R.id.fl_adplaceholder);
        this.cardView = (CardView) view.findViewById(R.id.card_view);
    }

    private void onclick() {
        this.btn_paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Vid_player_HomeFragment.this.et_link.setText(((ClipboardManager) Vid_player_HomeFragment.this.context.getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip().getItemAt(0).getText());
                } catch (Exception unused) {
                }
            }
        });
        this.btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String obj = Vid_player_HomeFragment.this.et_link.getText().toString();
                if (obj.contains("https://www.instagram.com")) {
                    Vid_player_HomeFragment.this.progressDialog = new ProgressDialog(Vid_player_HomeFragment.this.context, R.style.CustomBottomSheetDialogTheme1);
                    Vid_player_HomeFragment.this.progressDialog.setTitle("Fetching Data");
                    Vid_player_HomeFragment.this.progressDialog.setMessage("Please Wait");
                    Vid_player_HomeFragment.this.progressDialog.setProgressStyle(0);
                    Vid_player_HomeFragment.this.progressDialog.setCancelable(false);
                    Vid_player_HomeFragment.this.progressDialog.show();
                    Vid_player_HomeFragment.this.apicall(obj);
                    return;
                }
                Toast.makeText(Vid_player_HomeFragment.this.context, "Please enter correct link", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void apicall(String str) {
        ((Responseapi) RetrofitClient.getRetrofitInstance().create(Responseapi.class)).responseapi(str).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement body = response.body();
                if (Vid_player_HomeFragment.this.progressDialog.isShowing()) {
                    Vid_player_HomeFragment.this.progressDialog.dismiss();
                }
                if (response.code() == 200) {
                    if (body.isJsonObject()) {
                        Intent intent = new Intent(Vid_player_HomeFragment.this.getContext(), Vid_player_ReelDownlodActivity.class);
                        intent.putExtra("FINALRESPONSE", response.body().toString());
                        ((BaseActivity) getContext()).showInterstitial(intent);
                    } else if (body.isJsonArray()) {
                        Intent intent2 = new Intent(Vid_player_HomeFragment.this.getContext(), Vid_player_PhotosVideoDownloadActivity.class);
                        intent2.putExtra("FINALRESPONSE", response.body().toString());
                        ((BaseActivity) getContext()).showInterstitial(intent2);
                    } else if (body.isJsonNull()) {
                        Toast.makeText(Vid_player_HomeFragment.this.context, "data empty", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Vid_player_HomeFragment.this.context, "Try again", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(Vid_player_HomeFragment.this.context, "Invalid Instagram URL", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Vid_player_HomeFragment.this.context, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable th) {
                Vid_player_HomeFragment.this.progressDialog.dismiss();
                Context context = Vid_player_HomeFragment.this.context;
                Toast.makeText(context, "" + /*th.getMessage()*/"Getting Some Error, Please try Again..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
