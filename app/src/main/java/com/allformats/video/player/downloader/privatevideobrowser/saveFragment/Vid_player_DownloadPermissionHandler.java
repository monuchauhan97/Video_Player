package com.allformats.video.player.downloader.privatevideobrowser.saveFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.allformats.video.player.downloader.privatevideobrowser.utils.Vid_player_PermissionsManager;

public abstract class Vid_player_DownloadPermissionHandler extends Vid_player_PermissionsManager implements PreferenceManager.OnActivityResultListener {
    private Activity activity;
    public abstract void onPermissionGranted();
    public Vid_player_DownloadPermissionHandler(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public void showRequestPermissionRationale() {
        showPermissionSummaryDialog(new DialogInterface.OnClickListener() {
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                Vid_player_DownloadPermissionHandler.this.requestPermissions();
            }
        });
    }

    @Override 
    public void requestDisallowedAction() {
        SharedPreferences sharedPreferences = this.activity.getSharedPreferences("activity_ds_browse_settings", 0);
        if (sharedPreferences.getBoolean("requestDisallowed", false)) {
            showPermissionSummaryDialog(new DialogInterface.OnClickListener() {
                @Override 
                public void onClick(DialogInterface dialogInterface, int i) {
                    new AlertDialog.Builder(Vid_player_DownloadPermissionHandler.this.activity).setMessage("Go to SettingsActivity?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override 
                        public void onClick(DialogInterface dialogInterface2, int i2) {
                            Vid_player_DownloadPermissionHandler.this.activity.startActivityForResult(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", Vid_player_DownloadPermissionHandler.this.activity.getPackageName(), null)), 1337);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override 
                        public void onClick(DialogInterface dialogInterface2, int i2) {
                            Toast.makeText(Vid_player_DownloadPermissionHandler.this.activity, "Can't download; Necessary PERMISSIONS denied. Try again", Toast.LENGTH_LONG).show();
                        }
                    }).create().show();
                }
            });
            return;
        }
        sharedPreferences.edit().putBoolean("requestDisallowed", true).apply();
        onPermissionsDenied();
    }

    @Override 
    public void onPermissionsGranted() {
        onPermissionGranted();
    }

    @Override 
    public void onPermissionsDenied() {
        Toast.makeText(this.activity, "Can't download; Necessary PERMISSIONS denied. Try again", Toast.LENGTH_LONG).show();
    }

    private void showPermissionSummaryDialog(DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this.activity).setPositiveButton("OK", onClickListener).setMessage("This feature requires WRITE_EXTERNAL_STORAGE permission to save downloaded videos into the Download folder. Make sure to grant this permission. Otherwise, downloading videos is not possible.").create().show();
    }

    @Override
    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i != 1337) {
            return true;
        }
        new Vid_player_PermissionsManager(this.activity) {
            @Override 
            public void showRequestPermissionRationale() {
            }

            @Override 
            public void requestDisallowedAction() {
                onPermissionsDenied();
            }

            @Override 
            public void onPermissionsGranted() {
                Vid_player_DownloadPermissionHandler.this.onPermissionGranted();
            }

            @Override 
            public void onPermissionsDenied() {
                Toast.makeText(Vid_player_DownloadPermissionHandler.this.activity, "Can't download; Necessary PERMISSIONS denied. Try again", Toast.LENGTH_LONG).show();
            }
        }.checkPermissions("android.permission.WRITE_EXTERNAL_STORAGE", 4444);
        return true;
    }
}
