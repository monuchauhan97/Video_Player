package com.allformats.video.player.downloader.video_player.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.video_player.Util.Vid_player_Constant;

import java.io.File;


public class Vid_player_PermissionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView btnAllow;

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_permission);
        initView();
        initListener();
    }

    private void initListener() {
        this.btnAllow.setOnClickListener(this);
    }

    private void initView() {
        this.btnAllow = (TextView) findViewById(R.id.btnAllow);
    }

    @Override 
    public void onClick(View view) {
        if (view.getId() == R.id.btnAllow && Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 101);
        }
    }


    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 101) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
                if (!new File(Vid_player_Constant.HIDE_PATH).exists()) {
                    new File(Vid_player_Constant.HIDE_PATH).mkdirs();
                }
                startActivity(new Intent(this, Vid_player_HomeActivity.class));
                finish();
                return;
            }
            Toast.makeText(this, "Allow this permission!", Toast.LENGTH_SHORT).show();
        }
    }
}
