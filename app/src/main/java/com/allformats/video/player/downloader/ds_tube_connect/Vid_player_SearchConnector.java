package com.allformats.video.player.downloader.ds_tube_connect;

import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.allformats.video.player.downloader.ds_tube_android_util.Vid_player_FileUtil;
import com.allformats.video.player.downloader.ds_tube_android_util.classes.Vid_player_DelayTask;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_AudioItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Vid_player_SearchConnector {
    String attr;
    public Callbacks callbacks;
    private String currentSearch;
    private Map<String, String> headers;
    public int loadedItems;
    public boolean noMoreResults;
    public boolean noResults;
    private String prefString;
    public boolean searchError;
    public boolean searchProcessed;
    public WebView searchWebView;
    private int timeout = 35000;
    public boolean timeoutReached;
    public Vid_player_DelayTask timeoutTask;
    private String titleRegexString;

    
    public interface Callbacks {
        void onLoadMoreComplete(ArrayList<Vid_player_AudioItem> arrayList);

        void onLoadMoreError();

        void onNoMoreResults();

        void onNoResultsFound();

        void onSearchComplete(ArrayList<Vid_player_AudioItem> arrayList);

        void onSearchError();
    }

    
    public interface Callbacks2 {
        void onLoadMoreComplete(ArrayList<Vid_player_AudioItem> arrayList);

        void onLoadMoreError();

        void onNoMoreResults();

        void onNoResultsFound();

        void onSearchComplete(ArrayList<Vid_player_AudioItem> arrayList);

        void onSearchError();
    }

    
    public interface OnInitErrorCallback {
        void onInitError(Exception exc);
    }

    
    private class LoadListener {
        private LoadListener() {
        }

        @JavascriptInterface
        public void processSearch(String str) {
            Document parse = Jsoup.parse(str);
            ArrayList<Vid_player_AudioItem> arrayList = new ArrayList<>();
            try {
                String html = parse.select("ytm-search ytm-item-section-renderer div.ytm-message-text").html();
                Vid_player_SearchConnector.this.noResults = html != null && html.length() > 0;
            } catch (Exception e) {
                Log.e(getClass().getName(), Log.getStackTraceString(e));
            }
            try {
                Elements select = parse.select("ytm-video-with-context-renderer div.media-item-metadata");
                for (int i = 0; i < select.size(); i++) {
                    Element element = select.get(i);
                    Iterator<Element> it = element.getElementsByTag("a").iterator();
                    while (it.hasNext()) {
                        Vid_player_SearchConnector.this.attr = it.next().attr("href");
                    }
                    String substring = Vid_player_SearchConnector.this.attr.substring(Vid_player_SearchConnector.this.attr.indexOf("?v=") + 3);
                    String parseTitle = Vid_player_SearchConnector.this.parseTitle(element.select("h3").text());
                    arrayList.add(new Vid_player_AudioItem(substring, parseTitle, new String(Base64.decode("aHR0cHM6Ly9pLnl0aW1nLmNvbS92aS8=", 0), "UTF-8") + substring + "/mqdefault.jpg"));
                }
                Vid_player_SearchConnector.this.loadedItems = arrayList.size();
            } catch (Exception e2) {
                Log.e(getClass().getName(), Log.getStackTraceString(e2));
            }
            if (Vid_player_SearchConnector.this.noResults) {
                Vid_player_SearchConnector.this.callbacks.onNoResultsFound();
            } else if (arrayList.size() == 0) {
                Vid_player_SearchConnector.this.searchError = true;
                Vid_player_SearchConnector.this.callbacks.onSearchError();
            } else {
                Vid_player_SearchConnector.this.callbacks.onSearchComplete(arrayList);
            }
            Vid_player_SearchConnector.this.searchProcessed = true;
        }

        @JavascriptInterface
        public void processLoadMore(String str) {
            Document parse = Jsoup.parse(str);
            ArrayList<Vid_player_AudioItem> arrayList = new ArrayList<>();
            try {
                String html = parse.select("ytm-search ytm-item-section-renderer div.ytm-message-text").html();
                Vid_player_SearchConnector.this.noMoreResults = html != null && html.length() > 0;
            } catch (Exception e) {
                Log.e(getClass().getName(), Log.getStackTraceString(e));
            }
            try {
                Elements select = parse.select("ytm-video-with-context-renderer div.media-item-metadata");
                for (int i = Vid_player_SearchConnector.this.loadedItems; i < select.size(); i++) {
                    Element element = select.get(i);
                    Iterator<Element> it = element.getElementsByTag("a").iterator();
                    while (it.hasNext()) {
                        Vid_player_SearchConnector.this.attr = it.next().attr("href");
                    }
                    String substring = Vid_player_SearchConnector.this.attr.substring(Vid_player_SearchConnector.this.attr.indexOf("?v=") + 3);
                    arrayList.add(new Vid_player_AudioItem(substring, Vid_player_SearchConnector.this.parseTitle(element.select("h3").text()), new String(Base64.decode("aHR0cHM6Ly9pLnl0aW1nLmNvbS92aS8=", 0), "UTF-8") + substring + "/mqdefault.jpg"));
                }
                Vid_player_SearchConnector.this.loadedItems += arrayList.size();
            } catch (Exception e2) {
                Log.e(getClass().getName(), Log.getStackTraceString(e2));
            }
            if (Vid_player_SearchConnector.this.noMoreResults) {
                Vid_player_SearchConnector.this.callbacks.onNoMoreResults();
            } else if (arrayList.size() == 0) {
                Vid_player_SearchConnector.this.callbacks.onLoadMoreError();
            } else {
                Vid_player_SearchConnector.this.callbacks.onLoadMoreComplete(arrayList);
            }
        }
    }

    public Vid_player_SearchConnector(final WebView webView, final Callbacks callbacks, OnInitErrorCallback onInitErrorCallback) {
        this.callbacks = callbacks;
        this.timeoutTask = new Vid_player_DelayTask(new Runnable() {
            @Override 
            public void run() {
                Vid_player_SearchConnector.this.timeoutReached = true;
                callbacks.onSearchError();
                webView.loadUrl("about:blank");
            }
        }, this.timeout);
        try {
            this.prefString = new String(Base64.decode("aHR0cHM6Ly9tLnlvdXR1YmUuY29tL3Jlc3VsdHM/cGVyc2lzdF9hcHA9MSZhcHA9bSZzZWFyY2hfcXVlcnk9", 0), "UTF-8");
            this.titleRegexString = new String(Base64.decode("b2ZmaWNpYWwgbXVzaWMgdmlkZW98bXVzaWMgdmlkZW98dmlkZW8gb2ZpY2lhbHxvZmZpY2lhbCB2aWRlb3xvZmZpY2lhbHx2aWRlb3xvZmljaWFsfHVsdHJhIGhkfGhkaHF8aHFoZHxocS9oZHxoZC9ocXxoZHxocXx5b3V0dWJlfHlvdSB0dWJlfGx5cmljc3w0a3xoZHJ8Mmt8OGt8MTA4MHB8NzIwcHw0ODBwfDM2MHB8MjQwcHwxNjBwfDIxNjBwfDYwZnBzfDMwZnBzfDMwIGZwc3w2MCBmcHN8ZnBzfG0vdnxhbXZ8bXZ8c3VifHN1YnN8c3ViYmVkfHdpdGggbHlyaWN8d2l0aCBseXJpY3N8bHlyaWN8d2lkZXNjcmVlbnwxNjo5IHNjcmVlbnw0OjMgc2NyZWVufDE2Ojl8NDoz", 0), "UTF-8");
            this.searchWebView = webView;
            webView.setVisibility(View.INVISIBLE);
            this.searchWebView.setWillNotDraw(true);
            this.searchWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.2; Nexus 4 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.114 Mobile Safari/537.36");
            this.searchWebView.getSettings().setJavaScriptEnabled(true);
            this.searchWebView.getSettings().setLoadsImagesAutomatically(false);
            this.searchWebView.getSettings().setDefaultTextEncodingName("UTF-8");
            this.searchWebView.addJavascriptInterface(new LoadListener(), "LoadListener");
            this.searchWebView.setWebViewClient(new WebViewClient() { // from class: com.allformats.video.player.downloader.ds_tube_connect.Vid_player_SearchConnector.2
                @Override 
                public void onPageFinished(final WebView webView2, String str) {
                    super.onPageFinished(webView2, str);
                    Vid_player_SearchConnector.this.timeoutTask.cancel();
                    if (!Vid_player_SearchConnector.this.timeoutReached) {
                        webView2.postDelayed(new Runnable() {
                            @Override 
                            public void run() {
                                Vid_player_SearchConnector.this.processSearch(webView2);
                            }
                        }, 2000);
                    }
                }
            });
            HashMap hashMap = new HashMap();
            this.headers = hashMap;
            hashMap.put("Accept-Language", "en");
        } catch (Exception e) {
            onInitErrorCallback.onInitError(e);
        }
    }

    public void search(String str) {
        this.currentSearch = str;
        this.loadedItems = 0;
        this.noResults = false;
        this.searchError = false;
        this.searchProcessed = false;
        this.timeoutReached = false;
        this.timeoutTask.start();
        this.searchWebView.loadUrl(this.prefString + this.currentSearch + " music", this.headers);
    }

    public void loadMore() {
        if (canLoadMore()) {
            evaluateJavascript(this.searchWebView, "window.scrollTo(0,document.body.scrollHeight);");
            this.searchWebView.postDelayed(new Runnable() {
                @Override 
                public void run() {
                    Vid_player_SearchConnector vidplayerSearchConnector = Vid_player_SearchConnector.this;
                    vidplayerSearchConnector.processLoadMore(vidplayerSearchConnector.searchWebView);
                }
            }, 3000);
        }
    }

    public void processSearch(WebView webView) {
        evaluateJavascript(webView, "window.LoadListener.processSearch('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
    }

    public void processLoadMore(WebView webView) {
        evaluateJavascript(webView, "window.LoadListener.processLoadMore('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
    }

    public boolean canLoadMore() {
        return this.searchProcessed && !this.searchError && !this.noResults && !this.noMoreResults;
    }

    public String parseTitle(String str) {
        String unescapeEntities = Parser.unescapeEntities(str, false);
        return Vid_player_FileUtil.parseToFilename(unescapeEntities.replaceAll("(?i)[ \\(\\[\\|]+(" + this.titleRegexString + ")+[\\)\\]\\|]*", " "));
    }

    public void evaluateJavascript(WebView webView, String str) {
        if (Build.VERSION.SDK_INT >= 21) {
            webView.evaluateJavascript(str, null);
            return;
        }
        webView.loadUrl("javascript:" + str);
    }
}
