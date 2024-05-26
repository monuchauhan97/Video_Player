package com.allformats.video.player.downloader.privatevideobrowser.historyFragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.Vid_player_DS_BrowserFragment;
import com.allformats.video.player.downloader.privatevideobrowser.Vid_player_MainBrowserActivity;
import com.allformats.video.player.downloader.privatevideobrowser.utils.Vid_player_Utils;

import java.util.List;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.NativeAdsAdapter;

public class Vid_player_VideoHistoryFragmentVidplayer extends Vid_player_DS_BrowserFragment implements Vid_player_MainBrowserActivity.OnBackPressedListener {
    public static Activity activity;
    private Vid_player_HistorySQLite vidplayerHistorySQLite;
    private EditText searchText;
    private View view;
    private List<Vid_player_VisitedPage> vidplayerVisitedPages;
    private RecyclerView visitedPagesView;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        setRetainInstance(true);
        if (this.view == null) {
            getVDActivity().setOnBackPressedListener(this);
            View inflate = layoutInflater.inflate(R.layout.vid_player_activity_ds_browse_history, viewGroup, false);
            this.view = inflate;
            this.searchText = (EditText) inflate.findViewById(R.id.history_Search_Text);
            this.visitedPagesView = (RecyclerView) this.view.findViewById(R.id.rvHistoryList);
            this.vidplayerHistorySQLite = new Vid_player_HistorySQLite(getActivity());
            this.vidplayerVisitedPages = vidplayerHistorySQLite.getAllVisitedPages();
            this.visitedPagesView.setAdapter(new VisitedPagesAdapter(((BaseActivity) getActivity()), AdsUtility.config.listNativeCount));
            this.visitedPagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
            ((ImageView) this.view.findViewById(R.id.btn_delete_history)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Vid_player_VideoHistoryFragmentVidplayer.this.vidplayerHistorySQLite.clearHistory();
                    Vid_player_VideoHistoryFragmentVidplayer.this.vidplayerVisitedPages.clear();
                    Vid_player_VideoHistoryFragmentVidplayer.this.visitedPagesView.getAdapter().notifyDataSetChanged();
                    Vid_player_VideoHistoryFragmentVidplayer.this.isHistoryEmpty();
                }
            });
            ((ImageView) this.view.findViewById(R.id.history_Search_Icon)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Vid_player_VideoHistoryFragmentVidplayer.this.searchGo();
                }
            });
            final ImageView imageView = (ImageView) this.view.findViewById(R.id.history_search_cancel);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Vid_player_VideoHistoryFragmentVidplayer.this.searchText.getText().clear();
                    Vid_player_VideoHistoryFragmentVidplayer.this.searchGo();
                }
            });
            this.searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                }

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    if (charSequence.toString().trim().length() == 0) {
                        imageView.setVisibility(View.GONE);
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
            this.searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    Vid_player_VideoHistoryFragmentVidplayer.this.searchGo();
                    return false;
                }
            });
            isHistoryEmpty();
        }
        return this.view;
    }

    public void searchGo() {
        if (getActivity().getCurrentFocus() != null) {
            Vid_player_Utils.hideSoftKeyboard(getActivity(), getActivity().getCurrentFocus().getWindowToken());
            this.vidplayerVisitedPages = this.vidplayerHistorySQLite.getVisitedPagesByKeyword(this.searchText.getText().toString());
            this.visitedPagesView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onBackpressed() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }


    private class VisitedPagesAdapter extends NativeAdsAdapter {
        private VisitedPagesAdapter(BaseActivity activity, int listNativeCount) {
            super(activity, listNativeCount);
        }

        @Override
        public void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int i) {
            VisitedPageItem visitedPageItem = (VisitedPageItem) baseHolder;
            visitedPageItem.bind((Vid_player_VisitedPage) Vid_player_VideoHistoryFragmentVidplayer.this.vidplayerVisitedPages.get(i));
            visitedPageItem.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vid_player_VideoHistoryFragmentVidplayer.this.getVDActivity().getVideoBrowserManagerFragment().newWindow(((Vid_player_VisitedPage) Vid_player_VideoHistoryFragmentVidplayer.this.vidplayerVisitedPages.get(i)).link);
                    Vid_player_VideoHistoryFragmentVidplayer.this.getVDActivity().homeClicked();
                }
            });

            visitedPageItem.option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    PopupWindow mypopupWindow;
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = inflater.inflate(R.layout.vid_player_menus_layout, null);
                    LinearLayout Delete = (LinearLayout) v.findViewById(R.id.delete);
                    LinearLayout Open = (LinearLayout) v.findViewById(R.id.open_btn);
                    LinearLayout Copy = (LinearLayout) v.findViewById(R.id.copy_btn);
                    mypopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    int[] values = new int[2];
                    visitedPageItem.option.getLocationInWindow(values);
                    int positionOfIcon = values[1];
                    DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
                    int height = (displayMetrics.heightPixels * 2) / 3;
                    if (positionOfIcon > height) {
                        mypopupWindow.showAsDropDown(visitedPageItem.option, 0, -320);
                    } else {
                        mypopupWindow.showAsDropDown(visitedPageItem.option, 0, 0);
                    }
                    Delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mypopupWindow.dismiss();
                            Vid_player_VideoHistoryFragmentVidplayer.this.vidplayerHistorySQLite.deleteFromHistory(((Vid_player_VisitedPage) Vid_player_VideoHistoryFragmentVidplayer.this.vidplayerVisitedPages.get(i)).link);
                            Vid_player_VideoHistoryFragmentVidplayer.this.vidplayerVisitedPages.remove(i);
                            VisitedPagesAdapter.this.notifyItemRemoved(i);
                            Vid_player_VideoHistoryFragmentVidplayer.this.isHistoryEmpty();
                        }
                    });

                    Open.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mypopupWindow.dismiss();
                            Vid_player_VideoHistoryFragmentVidplayer.this.getVDActivity().getVideoBrowserManagerFragment().newWindow(((Vid_player_VisitedPage) Vid_player_VideoHistoryFragmentVidplayer.this.vidplayerVisitedPages.get(i)).link);
                            Vid_player_VideoHistoryFragmentVidplayer.this.getVDActivity().homeClicked();
                        }
                    });

                    Copy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mypopupWindow.dismiss();
                            Toast.makeText(Vid_player_VideoHistoryFragmentVidplayer.this.getActivity(), Vid_player_VideoHistoryFragmentVidplayer.this.getString(R.string.copy_msg), Toast.LENGTH_SHORT).show();
                            FragmentActivity activity = Vid_player_VideoHistoryFragmentVidplayer.this.getActivity();
                            Vid_player_VideoHistoryFragmentVidplayer.this.getActivity();
                            ((ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copied URL", ((Vid_player_VisitedPage) Vid_player_VideoHistoryFragmentVidplayer.this.vidplayerVisitedPages.get(i)).link));
                        }
                    });
                    mypopupWindow.showAsDropDown(view2, -153, 0);
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType) {
            return new VisitedPageItem(LayoutInflater.from(Vid_player_VideoHistoryFragmentVidplayer.this.getActivity()).inflate(R.layout.vid_player_history_item, viewGroup, false));
        }

        @Override
        public int itemCount() {
            return Vid_player_VideoHistoryFragmentVidplayer.this.vidplayerVisitedPages.size();
        }

        public class VisitedPageItem extends RecyclerView.ViewHolder {
            private TextView subtitle;
            private TextView title;
            ImageView option;

            VisitedPageItem(View view) {
                super(view);
                this.title = (TextView) view.findViewById(R.id.row_history_title);
                this.subtitle = (TextView) view.findViewById(R.id.row_history_subtitle);
                this.option = (ImageView) view.findViewById(R.id.row_history_menu);
            }

            void bind(Vid_player_VisitedPage vidplayerVisitedPage) {
                this.title.setText(vidplayerVisitedPage.title);
                this.subtitle.setText(vidplayerVisitedPage.link);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.vidplayerHistorySQLite = new Vid_player_HistorySQLite(getActivity());
    }

    public void isHistoryEmpty() {
        if (this.vidplayerVisitedPages.isEmpty()) {
            this.view.findViewById(R.id.llNoHistory).setVisibility(View.VISIBLE);
            this.view.findViewById(R.id.history_SearchBar).setVisibility(View.INVISIBLE);
            this.view.findViewById(R.id.llShowHistory).setVisibility(View.INVISIBLE);
            this.view.findViewById(R.id.llNoHistory).setVisibility(View.VISIBLE);
            return;
        }
        this.view.findViewById(R.id.llNoHistory).setVisibility(View.INVISIBLE);
        this.view.findViewById(R.id.history_SearchBar).setVisibility(View.VISIBLE);
        this.view.findViewById(R.id.llShowHistory).setVisibility(View.VISIBLE);
        this.view.findViewById(R.id.llNoHistory).setVisibility(View.GONE);
    }
}
