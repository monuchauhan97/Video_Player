package com.allformats.video.player.downloader.ds_tube_android_util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class Vid_player_ConnectUtil {


    public interface InternetConnectionCheckCallback {
        void onResult(boolean z);
    }

    public static void checkInternetConnection(final int i, final InternetConnectionCheckCallback internetConnectionCheckCallback) {
        new Thread() { // from class: com.allformats.video.player.downloader.ds_tube_android_util.Vid_player_ConnectUtil.1
            @Override 
            public void run() {
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress("8.8.8.8", 53), i);
                    socket.close();
                    internetConnectionCheckCallback.onResult(true);
                } catch (IOException unused) {
                    internetConnectionCheckCallback.onResult(false);
                }
            }
        }.start();
    }
}
