package com.allformats.video.player.downloader.ds_tube_connect;

import org.schabi.newpipe.extractor.stream_info.AudioStream;

import java.util.List;

public class Vid_player_AudioDecipher {

    public Vid_player_AudioDecipher(final String str, final Callback callback) {
        new Thread() {
            @Override
            public void run() {
                try {
                    List<AudioStream> audioStreams = (List<AudioStream>) new AudioStream(str, 1, 168, 136, 90);
                    if (audioStreams.size() > 0) {
                        callback.onSuccess(audioStreams);
                    } else {
                        callback.onError(new Exception("Not suitable format found"));
                    }
                } catch (Exception e) {
                    callback.onError(e);
                }
            }
        }.start();
    }

    public interface Callback {
        void onError(Exception exc);

        void onSuccess(List<AudioStream> list);
    }
}
