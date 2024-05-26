package com.allformats.video.player.downloader.video_player.Dialog;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;
import com.allformats.video.player.downloader.video_player.Util.Vid_player_Utils;

import java.io.File;
import java.util.Date;

public class Vid_player_VideoDetailsDialog extends DialogFragment {
    Vid_player_MediaData video;
    private View view;

    public static Vid_player_VideoDetailsDialog getInstance(Vid_player_MediaData vidplayerMediaData) {
        Vid_player_VideoDetailsDialog vidplayerVideoDetailsDialog = new Vid_player_VideoDetailsDialog();
        vidplayerVideoDetailsDialog.video = vidplayerMediaData;
        return vidplayerVideoDetailsDialog;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(STYLE_NO_TITLE, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.vid_player_dialog_video_details, viewGroup, false);
        this.view = inflate;
        return inflate;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView();
    }

    private void initView() {
        ((TextView) this.view.findViewById(R.id.videoName)).setText(this.video.getName());
        ((TextView) this.view.findViewById(R.id.videoPath)).setText(this.video.getPath());
        ((TextView) this.view.findViewById(R.id.videoModifyDate)).setText(DateFormat.format("dd/MM/yyyy", new Date(new File(this.video.getPath()).lastModified())).toString());
        ((TextView) this.view.findViewById(R.id.videoSize)).setText(Vid_player_Utils.formateSize(Long.parseLong(this.video.getLength())));
        ((TextView) this.view.findViewById(R.id.videoDuration)).setText(this.video.getDuration());
        ((TextView) this.view.findViewById(R.id.videoResolution)).setText(this.video.getResolution());
        ((Button) this.view.findViewById(R.id.btnOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_VideoDetailsDialog.this.getDialog().dismiss();
            }
        });
        ((Button) this.view.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_VideoDetailsDialog.this.getDialog().dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
