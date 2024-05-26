package com.allformats.video.player.downloader.ds_tube_android_util.view;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;

import java.lang.reflect.Field;


public class Vid_player_ViewPopupMenu extends PopupMenu {
    private OnShowPopupMenuListener onShowPopupMenuListener;

    
    public interface OnShowPopupMenuListener {
        boolean onShowPopupMenu(Vid_player_ViewPopupMenu vidplayerViewPopupMenu);
    }

    public Vid_player_ViewPopupMenu(Context context, View view) {
        super(context, view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view2) {
                if (Vid_player_ViewPopupMenu.this.onShowPopupMenuListener == null) {
                    Vid_player_ViewPopupMenu.this.show();
                } else if (Vid_player_ViewPopupMenu.this.onShowPopupMenuListener.onShowPopupMenu(Vid_player_ViewPopupMenu.this)) {
                    Vid_player_ViewPopupMenu.this.show();
                }
            }
        });
    }

    public void setForceShowIcon(boolean z) {
        try {
            Field declaredField = PopupMenu.class.getDeclaredField("mPopup");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(this);
            obj.getClass().getDeclaredMethod("setForceShowIcon", Boolean.TYPE).invoke(obj, Boolean.valueOf(z));
        } catch (Exception e) {
            Log.getStackTraceString(e);
        }
    }

    @Override
    public void inflate(int i) {
        getMenuInflater().inflate(i, getMenu());
    }

    public void setOnShowPopupMenuListener(OnShowPopupMenuListener onShowPopupMenuListener) {
        this.onShowPopupMenuListener = onShowPopupMenuListener;
    }
}
