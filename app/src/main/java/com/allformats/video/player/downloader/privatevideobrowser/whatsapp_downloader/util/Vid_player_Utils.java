package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.util;

import static android.os.Build.VERSION.SDK_INT;

import static com.allformats.video.player.downloader.Vid_player_DS_Helper.wb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;

import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.allformats.video.player.downloader.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Vid_player_Utils {

    public static boolean download(Context context, String str) {
        return copyFileInSavedDir(context, str);
    }

    public static boolean isVideoFile(Context context, String str) {
        if (str.startsWith("content")) {
            String type = DocumentFile.fromSingleUri(context, Uri.parse(str)).getType();
            return type != null && type.startsWith("video");
        }
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(str);
        return guessContentTypeFromName != null && guessContentTypeFromName.startsWith("video");
    }

    public static boolean copyFileInSavedDir(Context context, String str) {
        String str2;
        String ext;
        String name;

        if (isVideoFile(context, str)) {
            str2 = getDir(context, "WhatsApp Status").getAbsolutePath();
            name = "Video";
            ext = ".mp4";
        } else {
            str2 = getDir(context, "WhatsApp Status").getAbsolutePath();
            name = "Image";
            ext = ".jpg";
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSSS").format(new Date());
        String FileName = "_" + timeStamp;
        Uri fromFile = Uri.fromFile(new File(str2 + File.separator + name + FileName + ext /*new File(str).getName()*/));
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
            OutputStream openOutputStream = context.getContentResolver().openOutputStream(fromFile, "w");
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read > 0) {
                    openOutputStream.write(bArr, 0, read);
                } else {
                    openInputStream.close();
                    openOutputStream.flush();
                    openOutputStream.close();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(fromFile);
                    context.sendBroadcast(intent);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static File getDir(Context context, String str) {
        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "Download" + File.separator + context.getResources().getString(R.string.app_name) + File.separator + str);
        file.mkdirs();
        return file;
    }

    public static void setLanguage(Context context, String str) {
        Locale locale = new Locale(str);
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public static boolean appInstalledOrNot(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static boolean isAppInstalled(Activity activity, String str) {
        PackageManager packageManager = activity.getPackageManager();
        try {
            packageManager.getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return packageManager.getApplicationInfo(str, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void shareFile(Context context, boolean z, String str) {
        Uri uri;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        if (z) {
            intent.setType("Video/*");
        } else {
            intent.setType("image/*");
        }
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (str.startsWith("content")) {
                uri = Uri.parse(str);
            } else {
                uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(str));
            }
            intent.putExtra("android.intent.extra.STREAM", uri);
            context.startActivity(intent);
        } else {
            File file = new File(str);
            uri = Uri.parse(file.getPath());
            intent.putExtra("android.intent.extra.STREAM", uri);
            context.startActivity(intent);
        }

    }

    public static void repostWhatsApp(Context context, boolean z, String str) {
        Uri uri;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        if (z) {
            intent.setType("Video/*");
        } else {
            intent.setType("image/*");
        }
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (str.startsWith("content")) {
                uri = Uri.parse(str);
            } else {
                uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(str));
            }
        } else {
            File file = new File(str);
            uri = Uri.parse(file.getPath());
        }
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.setPackage("com.whatsapp");
       /* if (wb) {
            intent.setPackage("com.whatsapp.w4b");
        } else {
            intent.setPackage("com.whatsapp");
        }*/
        context.startActivity(intent);
    }
}
