package com.allformats.video.player.downloader.privatevideobrowser.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.EditText;

public abstract class Vid_player_RenameDialog implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
    private Context context;
    private AlertDialog dialog;
    private EditText text;

    public abstract void onOK(String str);

    
    public Vid_player_RenameDialog(Context context, String str) {
        this.context = context;
        EditText editText = new EditText(context);
        this.text = editText;
        editText.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.text.setHint(str);
        AlertDialog create = new AlertDialog.Builder(context).setView(this.text).setMessage("Type new name:").setPositiveButton("OK", this).setNegativeButton("CANCEL", this).create();
        this.dialog = create;
        create.show();
    }

    @Override 
    public final void onClick(DialogInterface dialogInterface, int i) {
        Vid_player_Utils.hideSoftKeyboard((Activity) this.context, this.text.getWindowToken());
        if (i == -1) {
            onOK(this.text.getText().toString());
        }
    }

    public boolean isActive() {
        return this.dialog.isShowing();
    }

    public void dismiss() {
        this.dialog.dismiss();
    }
}
