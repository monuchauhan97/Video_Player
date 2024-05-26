package com.allformats.video.player.downloader.ds_tube_connect;


public class Vid_player_AudioConnector {
    
    public interface OnConnectionCompleteListener {
        void onConnectionError(Exception exc);

        void onConnectionSuccess(String str);
    }
}
