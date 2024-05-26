package com.allformats.video.player.downloader.ds_tube_android_util;

import android.webkit.MimeTypeMap;

import java.io.File;


public class Vid_player_FileUtil {

    public static File getNotExistFile(File file) {
        String absolutePath = file.getParentFile().getAbsolutePath();
        String baseFileName = getBaseFileName(file);
        String fileExtension = getFileExtension(file);
        if (fileExtension.length() > 0) {
            int i = 1;
            while (file.exists()) {
                i++;
                file = new File(absolutePath, baseFileName + "/" + i + "." + fileExtension);
            }
            return file;
        }
        int i2 = 1;
        while (file.exists()) {
            i2++;
            file = new File(absolutePath, baseFileName + "/" + i2);
        }
        return file;
    }

    public static String parseToFilename(String str) {
        return str.replaceAll("[^\\p{L}0-9\\s_\\-&']", "").replaceAll("^[\\-_\\s]+", "").replaceAll("[\\-_\\s]+$", "").replaceAll("-+", "-").replaceAll("_+", "_").replaceAll("\\s+", " ");
    }

    public static String getFileExtension(File file) {
        int lastIndexOf = file.getName().lastIndexOf(46);
        return lastIndexOf > 0 ? file.getName().substring(lastIndexOf + 1) : "";
    }

    public static String getMimeTypeFromExtension(String str) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(str);
    }

    public static String getBaseFileName(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(46);
        return (lastIndexOf == -1 || lastIndexOf == 0) ? name : name.substring(0, lastIndexOf);
    }
}
