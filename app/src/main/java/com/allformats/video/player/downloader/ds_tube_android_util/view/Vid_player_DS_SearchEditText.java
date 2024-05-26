package com.allformats.video.player.downloader.ds_tube_android_util.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.ds_tube_android_util.Vid_player_ActivityUtil;


public class Vid_player_DS_SearchEditText extends LinearLayout {
    private EditText editText;
    private OnQueryTextListener onQueryTextListener;
    private ImageView txtClear;
    private TextView txtDummy;
    public interface OnQueryTextListener {
        void onQueryTextChange(String str);

        boolean onQueryTextSubmit(String str);
    }

    public Vid_player_DS_SearchEditText(Context context) {
        super(context);
        init();
    }

    public Vid_player_DS_SearchEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public Vid_player_DS_SearchEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        inflate(getContext(), R.layout.vid_player_ds_search_edit_text_layout, this);
        this.editText = (EditText) findViewById(R.id.edit_text);
        this.txtClear = (ImageView) findViewById(R.id.txt_clear);
        this.txtDummy = (TextView) findViewById(R.id.txt_dummy);
        this.txtClear.setVisibility(View.GONE);
        this.txtClear.setOnClickListener(new OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Vid_player_DS_SearchEditText.this.editText.setText("");
            }
        });
        this.editText.addTextChangedListener(new TextWatcher() { 
            @Override 
            public void afterTextChanged(Editable editable) {
            }

            @Override 
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override 
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    Vid_player_DS_SearchEditText.this.txtClear.setVisibility(View.VISIBLE);
                } else {
                    Vid_player_DS_SearchEditText.this.txtClear.setVisibility(View.GONE);
                }
                if (Vid_player_DS_SearchEditText.this.onQueryTextListener != null) {
                    Vid_player_DS_SearchEditText.this.onQueryTextListener.onQueryTextChange(String.valueOf(charSequence));
                }
            }
        });
        this.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { 
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent == null || keyEvent.getAction() != 0 || keyEvent.getKeyCode() != 66) && i != 3 && i != 6) {
                    return false;
                }
                String obj = Vid_player_DS_SearchEditText.this.editText.getText().toString();
                Vid_player_DS_SearchEditText.this.txtDummy.requestFocus();
                if (Vid_player_DS_SearchEditText.this.onQueryTextListener != null) {
                    return Vid_player_DS_SearchEditText.this.onQueryTextListener.onQueryTextSubmit(obj);
                }
                return true;
            }
        });
        this.editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    Vid_player_ActivityUtil.showKeyboard(Vid_player_DS_SearchEditText.this.getContext(), view);
                } else {
                    Vid_player_ActivityUtil.hideKeyboard(Vid_player_DS_SearchEditText.this.getContext(), view);
                }
            }
        });
    }

    public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
        this.onQueryTextListener = onQueryTextListener;
    }

    public void open() {
        this.editText.requestFocus();
    }

    public void close() {
        this.txtDummy.requestFocus();
    }

    public EditText getEditText() {
        return this.editText;
    }
}
