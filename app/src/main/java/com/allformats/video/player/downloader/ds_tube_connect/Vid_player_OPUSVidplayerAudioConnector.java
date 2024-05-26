package com.allformats.video.player.downloader.ds_tube_connect;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.R;

import org.schabi.newpipe.extractor.stream_info.AudioStream;

import java.util.List;

public class Vid_player_OPUSVidplayerAudioConnector extends Vid_player_AudioConnector {
    public static void tryConnectAudio(String str, final OnConnectionCompleteListener onConnectionCompleteListener) {
        new Vid_player_AudioDecipher(str, new Vid_player_AudioDecipher.Callback() {
            @Override
            public void onSuccess(List<AudioStream> list) {
                AudioStream findFormat = Vid_player_OPUSVidplayerAudioConnector.findFormat(list, 1024);
                if (findFormat == null) {
                    findFormat = Vid_player_OPUSVidplayerAudioConnector.findFormat(list, 512);
                }
                if (findFormat == null) {
                    findFormat = Vid_player_OPUSVidplayerAudioConnector.findFormat(list, 1280);
                }
                if (findFormat == null) {
                    findFormat = Vid_player_OPUSVidplayerAudioConnector.findFormat(list, 512);
                }
                if (findFormat == null) {
                    onConnectionCompleteListener.onConnectionError(new Exception(Vid_player_DS_Helper.getInstance().getResources().getString(R.string.music_connection_error)));
                } else {
                    onConnectionCompleteListener.onConnectionSuccess(findFormat.url);
                }
            }

            @Override
            public void onError(Exception exc) {
                onConnectionCompleteListener.onConnectionError(new Exception(Vid_player_DS_Helper.getInstance().getResources().getString(R.string.music_connection_error)));
            }
        });
    }

    public static AudioStream findFormat(List<AudioStream> list, int i) {
        for (AudioStream audioStream : list) {
            if (audioStream.format == i) {
                return audioStream;
            }
        }
        return null;
    }
}
