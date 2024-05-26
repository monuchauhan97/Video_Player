package com.allformats.video.player.downloader;

import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.units.Decade;

import java.util.Locale;

public class Vid_player_Localization {
    private static PrettyTime prettyTime;

    private Vid_player_Localization() {
    }

    public static void init() {
        initPrettyTime();
    }

    private static void initPrettyTime() {
        PrettyTime prettyTime2 = new PrettyTime(Locale.getDefault());
        prettyTime = prettyTime2;
        prettyTime2.removeUnit(Decade.class);
    }


}
