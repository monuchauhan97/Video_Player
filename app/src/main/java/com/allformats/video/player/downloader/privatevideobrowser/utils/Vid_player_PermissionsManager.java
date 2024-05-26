package com.allformats.video.player.downloader.privatevideobrowser.utils;

import android.app.Activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.allformats.video.player.downloader.privatevideobrowser.Vid_player_MainBrowserActivity;

public abstract class Vid_player_PermissionsManager implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Activity activity;
    private boolean grantedPermissions;
    private String[] permissions;
    private int requestCode;

    public abstract void onPermissionsDenied();

    public abstract void onPermissionsGranted();

    public abstract void requestDisallowedAction();

    public abstract void showRequestPermissionRationale();

    
    public Vid_player_PermissionsManager(Activity activity) {
        this.activity = activity;
        ((Vid_player_MainBrowserActivity) activity).setOnRequestPermissionsResultListener(this);
    }

    private boolean notGrantedPermission(String str) {
        return ContextCompat.checkSelfPermission(this.activity, str) != 0;
    }

    public void checkPermissions(String str, int i) {
        checkPermissions(new String[]{str}, i);
    }

    public void checkPermissions(String[] strArr, int i) {
        this.permissions = strArr;
        this.requestCode = i;
        int length = strArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            String str = strArr[i2];
            if (!notGrantedPermission(str)) {
                this.grantedPermissions = true;
                i2++;
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, str)) {
                showRequestPermissionRationale();
            } else {
                requestPermissions();
            }
        }
        if (this.grantedPermissions) {
            onPermissionsGranted();
        }
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(this.activity, this.permissions, this.requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        int i2 = 0;
        while (true) {
            if (i2 >= strArr.length) {
                break;
            } else if (iArr[i2] == 0) {
                this.grantedPermissions = true;
                i2++;
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this.activity, strArr[i2])) {
                this.grantedPermissions = false;
                requestDisallowedAction();
            } else {
                this.grantedPermissions = false;
                onPermissionsDenied();
            }
        }
        if (this.grantedPermissions) {
            onPermissionsGranted();
        }
    }
}
