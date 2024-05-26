package plugin.adsdk.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.jetbrains.annotations.NotNull;

import io.michaelrocks.paranoid.Obfuscate;
import plugin.adsdk.R;
import plugin.adsdk.service.api.ListModel;

@SuppressWarnings("unused")
@Obfuscate
@SuppressLint("InflateParams")
public class AdsUtility {

    private static final String TAG = "AdsUtility";

    public static ListModel config = new ListModel();
    public static int currentActivityCount = 0;

    protected static boolean needsLoading = true;
    public static int startScreenCount = 0;

    private static InterstitialAd interstitialAd = null;
    private static NativeAd nativeAd = null;
    private static AdView bannerAd = null;

    protected static void requestBannerAd(final BaseActivity activity, ViewGroup adViewLayout) {
        if (config.preloadNative) {
            if (bannerAd != null) {
                Log.d(TAG, "requestBannerAd: Pre-Loaded");
                requestAdMobBannerPreload(activity, adViewLayout);
                return;
            }
        }
        requestAdMobBanner(activity, adViewLayout);
    }

    private static void requestAdMobBannerPreload(BaseActivity activity, ViewGroup adViewLayout) {
        if (bannerAd.getParent() != null)
            ((ViewGroup) bannerAd.getParent()).removeView(bannerAd);

        adViewLayout.removeAllViews();
        adViewLayout.addView(bannerAd);

        bannerAd = null;
        loadAdMobBannerPreload(activity);
    }

    public static void loadAdMobBannerPreload(BaseActivity activity) {
        if (TextUtils.isEmpty(config.adMob.bannerAd)) return;
        if (bannerAd != null) return;

        bannerAd = new AdView(activity);
        bannerAd.setAdUnitId(config.adMob.bannerAd);
        //adView.setAdSize(AdSize.BANNER);
        bannerAd.setAdSize(getAdSize(activity));
        AdRequest.Builder builder = new AdRequest.Builder();
        bannerAd.loadAd(builder.build());
        bannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                bannerAd = null;
                //loadAdMobBannerPreload(activity);   //preload failed
            }
        });
    }

    private static void requestAdMobBanner(BaseActivity activity, ViewGroup adViewLayout) {
        AdView adView = new AdView(activity);
        adView.setAdUnitId(config.adMob.bannerAd);
        //adView.setAdSize(AdSize.BANNER);
        adView.setAdSize(getAdSize(activity));
        AdRequest.Builder builder = new AdRequest.Builder();
        adView.loadAd(builder.build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adViewLayout.setVisibility(View.GONE);
            }
        });

        if (adView.getParent() != null)
            ((ViewGroup) adView.getParent()).removeView(adView);

        adViewLayout.removeAllViews();
        adViewLayout.addView(adView);
    }

    private static AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public static void requestNativeAdMedium(final BaseActivity activity, ViewGroup adView) {
        if (config.preloadNative) {
            //preloading natives
            if (nativeAd != null) {
                Log.d(TAG, "requestNativeAd: Pre-Loaded");
                requestAdMobMediumNativePreload(activity, adView);
                return;
            }
        }

        Log.d(TAG, "requestNativeAd: On-Demand");
        AdsUtility.requestAdMobMediumNative(activity, adView);
    }

    public static void loadAdMobMediumNativePreload(final BaseActivity activity) {
        if (TextUtils.isEmpty(config.adMob.nativeAd)) return;
        if (nativeAd != null) return;

        Log.d(TAG, "loadAdMobMediumNativePreload: preload-native request");

        AdLoader adLoader = new AdLoader.Builder(activity, config.adMob.nativeAd)
                .forNativeAd(unifiedNativeAd -> nativeAd = unifiedNativeAd).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(TAG, "onAdFailedToLoad: " + loadAdError);

                        //adNativeCount = (adNativeCount + 1) % config.adMob.nativeAd.size();
                        nativeAd = null;
                        //loadAdMobMediumNativePreload(activity);    //preload failed
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private static void requestAdMobMediumNativePreload(final BaseActivity activity, final ViewGroup nativeAdLayout) {
        NativeAdView unifiedNativeAdView;
        int layout = R.layout.ad_native_medium;
        unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(layout, null);

//        double res = (getDeviceHeight(activity) / 100.0f) * config.nativeHeightPercentage;
        nativeAdLayout.getLayoutParams().height = (int) activity.getResources().getDimension(R.dimen.medium_native_ad_height);
        populateAdMobNative(nativeAd, unifiedNativeAdView);
        nativeAdLayout.removeAllViews();
        nativeAdLayout.addView(unifiedNativeAdView);

        nativeAd = null;
        loadAdMobMediumNativePreload(activity);    //preload displayed
    }

    private static void requestAdMobMediumNative(final BaseActivity activity, final ViewGroup nativeAdLayout) {
        if (TextUtils.isEmpty(config.adMob.nativeAd)) return;

        AdLoader adLoader = new AdLoader.Builder(activity, config.adMob.nativeAd)
                .forNativeAd(unifiedNativeAd -> {
                    NativeAdView unifiedNativeAdView;

                    int layout = R.layout.ad_native_medium;
                    unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(layout, null);

                    nativeAdLayout.getLayoutParams().height = (int) activity.getResources().getDimension(R.dimen.medium_native_ad_height);
                    populateAdMobNative(unifiedNativeAd, unifiedNativeAdView);
                    nativeAdLayout.removeAllViews();
                    nativeAdLayout.addView(unifiedNativeAdView);
                }).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(TAG, "onAdFailedToLoad: " + loadAdError);
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        //adNativeCount = (adNativeCount + 1) % config.adMob.nativeAd.size();
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public static void requestNativeAdSmall(final BaseActivity activity, ViewGroup adView) {
        if (config.preloadNative) {
            //preloading natives
            if (nativeAd != null) {
                Log.d(TAG, "requestNativeAd: Pre-Loaded");
                requestAdMobSmallNativePreload(activity, adView);
                return;
            }
        }

        Log.d(TAG, "requestNativeAd: On-Demand");
        AdsUtility.requestAdMobSmallNative(activity, adView);
    }

    public static void loadAdMobSmallNativePreload(final BaseActivity activity) {
        if (TextUtils.isEmpty(config.adMob.nativeAd)) return;
        if (nativeAd != null) return;

        Log.d(TAG, "loadAdMobSmallNativePreload: preload-native request");

        AdLoader adLoader = new AdLoader.Builder(activity, config.adMob.nativeAd)
                .forNativeAd(unifiedNativeAd -> nativeAd = unifiedNativeAd).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(TAG, "onAdFailedToLoad: " + loadAdError);

                        //adNativeCount = (adNativeCount + 1) % config.adMob.nativeAd.size();
                        nativeAd = null;
                        //loadAdMobSmallNativePreload(activity);    //preload failed
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private static void requestAdMobSmallNativePreload(final BaseActivity activity, final ViewGroup nativeAdLayout) {
        NativeAdView unifiedNativeAdView;
        int layout = R.layout.ad_native_small;
        unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(layout, null);

//        double res = (getDeviceHeight(activity) / 100.0f) * config.nativeHeightPercentage;
        nativeAdLayout.getLayoutParams().height = (int) activity.getResources().getDimension(R.dimen.small_native_ad_height);
        populateAdMobNative(nativeAd, unifiedNativeAdView);
        nativeAdLayout.removeAllViews();
        nativeAdLayout.addView(unifiedNativeAdView);

        nativeAd = null;
        loadAdMobSmallNativePreload(activity);    //preload displayed
    }

    private static void requestAdMobSmallNative(final BaseActivity activity, final ViewGroup nativeAdLayout) {
        if (TextUtils.isEmpty(config.adMob.nativeAd)) return;

        AdLoader adLoader = new AdLoader.Builder(activity, config.adMob.nativeAd)
                .forNativeAd(unifiedNativeAd -> {
                    NativeAdView unifiedNativeAdView;

                    int layout = R.layout.ad_native_small;
                    unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(layout, null);

                    nativeAdLayout.getLayoutParams().height = (int) activity.getResources().getDimension(R.dimen.small_native_ad_height);
                    populateAdMobNative(unifiedNativeAd, unifiedNativeAdView);
                    nativeAdLayout.removeAllViews();
                    nativeAdLayout.addView(unifiedNativeAdView);
                }).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(TAG, "onAdFailedToLoad: " + loadAdError);
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        //adNativeCount = (adNativeCount + 1) % config.adMob.nativeAd.size();
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public static void requestNativeAd(final BaseActivity activity, ViewGroup adView) {
        if (config.preloadNative) {
            //preloading natives
            if (nativeAd != null) {
                Log.d(TAG, "requestNativeAd: Pre-Loaded");
                requestAdMobBigNativePreload(activity, adView);
                return;
            }
        }

        Log.d(TAG, "requestNativeAd: On-Demand");
        AdsUtility.requestAdMobBigNative(activity, adView);
    }

    private static void populateAdMobNative(NativeAd nativeAd, @NonNull NativeAdView adView) {
        try {
            String[] forcedColors = config.forcedNativeColor.split(",");
            boolean forcedBackgroundColor = forcedColors.length > 1 && !TextUtils.isEmpty(forcedColors[1]);
            if (forcedBackgroundColor) {
                int color = Color.parseColor(forcedColors[1]);
                adView.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        View headline = adView.findViewById(R.id.ad_headline);
        if (headline != null) {
            adView.setHeadlineView(headline);
        }

        View body = adView.findViewById(R.id.ad_body);
        if (body != null) {
            adView.setBodyView(body);
        }

        View cta = adView.findViewById(R.id.ad_call_to_action);
        if (cta != null) {
            adView.setCallToActionView(cta);
            try {
                String[] forcedColors = config.forcedNativeColor.split(",");
                boolean forcedButtonColor = forcedColors.length > 0 && !TextUtils.isEmpty(forcedColors[0]);
                if (forcedButtonColor) {
                    int color = Color.parseColor(forcedColors[0]);
                    cta.setBackgroundTintList(ColorStateList.valueOf(color));
                }
            } catch (Exception ignored) {
            }
        }

        View appIcon = adView.findViewById(R.id.ad_app_icon);
        if (appIcon != null) {
            adView.setIconView(appIcon);
        }

        MediaView adMedia = adView.findViewById(R.id.ad_media);
        if (adMedia != null) {
            adView.setMediaView(adMedia);
        }

        if (adView.getHeadlineView() instanceof TextView) {
            String spacedHeadline = String.format("      %s", nativeAd.getHeadline());
            ((TextView) adView.getHeadlineView()).setText(spacedHeadline);
        }

        if (adView.getBodyView() instanceof TextView) {
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (adView.getCallToActionView() instanceof AppCompatButton) {
            ((AppCompatButton) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            if (adView.getIconView() != null) {
                adView.getIconView().setVisibility(View.GONE);
            }
        } else {
            if (adView.getIconView() instanceof ImageView) {
                ImageView iconView = (ImageView) adView.getIconView();
                iconView.setImageDrawable(nativeAd.getIcon().getDrawable());
                iconView.setVisibility(View.VISIBLE);
            }
        }
        adView.setNativeAd(nativeAd);
    }

    public static void loadAdMobBigNativePreload(final BaseActivity activity) {
        if (TextUtils.isEmpty(config.adMob.nativeAd)) return;
        if (nativeAd != null) return;

        Log.d(TAG, "loadAdMobBigNativePreload: preload-native request");
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();
        AdLoader adLoader = new AdLoader.Builder(activity, config.adMob.nativeAd)
                .forNativeAd(unifiedNativeAd -> nativeAd = unifiedNativeAd).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(TAG, "onAdFailedToLoad: " + loadAdError);

                        //adNativeCount = (adNativeCount + 1) % config.adMob.nativeAd.size();
                        nativeAd = null;
                        //loadAdMobBigNativePreload(activity);    //preload failed
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                    }
                }).withNativeAdOptions(adOptions).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private static void requestAdMobBigNativePreload(final BaseActivity activity, final ViewGroup nativeAdLayout) {
        //flashing does not support in preloading natives
        if (config.nativeHeightPercentage > 0) {
            double res = (getDeviceHeight(activity) / 100.0f) * config.nativeHeightPercentage;
            float recommendedHeight = activity.getResources().getDimension(R.dimen.native_ad_height);
            double regretValue = recommendedHeight - ((recommendedHeight / 100.0f) * 22);
            if (res < regretValue) {
                AdsUtility.requestAdMobMediumNativePreload(activity, nativeAdLayout); //downgrade to medium
                return;
            }
            nativeAdLayout.getLayoutParams().height = (int) res;
        }

        NativeAdView unifiedNativeAdView;
        int layout = R.layout.ad_native_big;
        unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(layout, null);
        populateAdMobNative(nativeAd, unifiedNativeAdView);
        nativeAdLayout.removeAllViews();
        nativeAdLayout.addView(unifiedNativeAdView);

        nativeAd = null;
        loadAdMobBigNativePreload(activity);    //preload displayed
    }

    private static void requestAdMobBigNative(final BaseActivity activity, final ViewGroup nativeAdLayout) {
        if (TextUtils.isEmpty(config.adMob.nativeAd)) return;

        if (config.nativeHeightPercentage > 0) {
            double res = (getDeviceHeight(activity) / 100.0f) * config.nativeHeightPercentage;
            float recommendedHeight = activity.getResources().getDimension(R.dimen.native_ad_height);
            double regretValue = recommendedHeight - ((recommendedHeight / 100.0f) * 22);
            if (res < regretValue) {
                AdsUtility.requestAdMobMediumNative(activity, nativeAdLayout); //downgrade to medium
                return;
            }
            nativeAdLayout.getLayoutParams().height = (int) res;
        }
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();
        AdLoader adLoader = new AdLoader.Builder(activity, config.adMob.nativeAd)
                .forNativeAd(unifiedNativeAd -> {
                    NativeAdView unifiedNativeAdView;

                    int layout = R.layout.ad_native_big;
                    unifiedNativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(layout, null);

                    populateAdMobNative(unifiedNativeAd, unifiedNativeAdView);
                    nativeAdLayout.removeAllViews();
                    nativeAdLayout.addView(unifiedNativeAdView);
                }).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(TAG, "onAdFailedToLoad: " + loadAdError);

                        if (!TextUtils.isEmpty(config.video) && config.preloadNative && nativeAd == null) {
                            requestAdMobBigNativePreload(activity, nativeAdLayout);  //fallback preload
                        }
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        //adNativeCount = (adNativeCount + 1) % config.adMob.nativeAd.size();
                    }
                }).withNativeAdOptions(adOptions).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected static void showInterstitial(BaseActivity activity, Intent intent) {
        showInterstitial(activity, () -> startActivity(activity, intent));
    }

    protected static void showInterstitial(BaseActivity activity, final BaseCallback callback) {
        if (AdsUtility.currentActivityCount == 0) {
            if (AdsUtility.shouldPreloadFullScreen()) {
                AdsUtility.showAdMobInterstitialPreLoaded(activity, callback);
                return;
            }

            activity.setupDialog();
            //progressDialog.show();
            AdsUtility.showAdMobInterstitial(activity, callback);
        } else {
            AdsUtility.currentActivityCount = (AdsUtility.currentActivityCount + 1) % config.activityCount;
            callback.completed();
        }
    }

    protected static void showAdMobInterstitial(BaseActivity activity, Intent intent) {
        showAdMobInterstitial(activity, () -> startActivity(activity, intent));
    }

    protected static void showAdMobInterstitial(BaseActivity activity, final BaseCallback callback) {
        if (TextUtils.isEmpty(config.adMob.interstitialAd)) {
            //BaseActivity.dismissDialog();
            callback.completed();
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        activity.showDialog();
        InterstitialAd.load(activity, config.adMob.interstitialAd, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                interstitialAd.show(activity);
                AppOpenManager.overrideAppOpenShow(true);
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                AppOpenManager.overrideAppOpenShow(false);
                                BaseActivity.dismissDialog();
                                currentActivityCount = (currentActivityCount + 1) % config.activityCount;
//                                interstitialAd = null;
                                callback.completed();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NotNull com.google.android.gms.ads.AdError adError) {
                                AppOpenManager.overrideAppOpenShow(false);
                                BaseActivity.dismissDialog();
                                currentActivityCount = (currentActivityCount + 1) % config.activityCount;
                                callback.completed();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                            }
                        });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i(TAG, loadAdError.getMessage());
                BaseActivity.dismissDialog();
                currentActivityCount = (currentActivityCount + 1) % config.activityCount;
                callback.completed();

                if (!TextUtils.isEmpty(config.video) && AdsUtility.shouldPreloadFullScreen()) {
                    if (interstitialAd == null && !isLoadingFullScreen) {
                        loadAdMobInterstitial(activity); //fallback preload
                    }
                }
            }
        });
    }

    private static void startActivity(BaseActivity activity, Intent intent) {
        activity.startActivity(intent);
    }

    public static boolean showInitialAppOpen() {
        return AdsUtility.config.initialAppOpen;
    }

    //Rate
    public static void rateUs(BaseActivity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            activity.startActivity(goToMarket);

        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));

        }
    }

    //More
    public static void moreApps(BaseActivity activity) {
        if (!TextUtils.isEmpty(config.accountName)) {
            Uri uri = Uri.parse("market://developer?id=" + config.accountName + "&hl=en");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                activity.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/developer?id=" + config.accountName + "&hl=en")));
            }
        } else {
            Toast.makeText(activity, "Coming Soon!", Toast.LENGTH_SHORT).show();
        }
    }

    //Share
    public static void shareApp(BaseActivity activity) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
            String shareMessage = "\nPlease try this application\n\n"
                    + "https://play.google.com/store/apps/details?id="
                    + activity.getPackageName() + "\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception ignored) {
        }
    }

    //Policy
    public static void privacyPolicy(BaseActivity activity) {
        try {
            Intent browserIntent;
            if (!TextUtils.isEmpty(config.privacyPolicyUrl)) {
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(config.privacyPolicyUrl));
            } else {
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://visiontecprivacypolicy.blogspot.com/?m=1"));

            }
            activity.startActivity(browserIntent);
        } catch (Exception ignored) {
        }
    }

    public static void refreshTokens(BaseActivity activity) {
        startScreenCount = 0;
        currentActivityCount = 0;
        AdsUtility.needsLoading = true;
        AppOpenManager.refreshAppOpen(activity);
    }

    private static int displayMatrixHeight = -1;

    private static int getDeviceHeight(BaseActivity activity) {
        if (displayMatrixHeight > -1) return displayMatrixHeight;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayMatrixHeight = displayMetrics.heightPixels;
        return displayMatrixHeight;
    }

    public static boolean shouldPreloadFullScreen() {
        return config.preloadInterstitial;
    }

    public interface CallbackListener {
        void onCallback();
    }

    //to prevent multiple load requests (only in case of preload)
    private static boolean isLoadingFullScreen = false;

    public static void loadAdMobInterstitial(Context context) {
        if (TextUtils.isEmpty(config.adMob.interstitialAd)) return;
        if (AdsUtility.interstitialAd != null) return;
        if (isLoadingFullScreen) return;

        Log.d(TAG, "loadAdMobInterstitial: preload request");
        isLoadingFullScreen = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, config.adMob.interstitialAd, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                isLoadingFullScreen = false;
                //interstitialRetryCount = 0;
                AdsUtility.interstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i(TAG, loadAdError.getMessage());
                isLoadingFullScreen = false;
                interstitialAd = null;
            }
        });
    }

    public static void loadAdMobInterstitialWithCallback(Context context, CallbackListener listener) {
        if (TextUtils.isEmpty(config.adMob.interstitialAd) || AdsUtility.interstitialAd != null || isLoadingFullScreen) {
            listener.onCallback();
            return;
        }

        Log.d(TAG, "loadAdMobInterstitialWithCallback: preload request");
        isLoadingFullScreen = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, config.adMob.interstitialAd, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                isLoadingFullScreen = false;
                //interstitialRetryCount = 0; //reset after initial ad
                AdsUtility.interstitialAd = interstitialAd;
                listener.onCallback();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i(TAG, loadAdError.getMessage());
                isLoadingFullScreen = false;
                interstitialAd = null;
                listener.onCallback();
            }
        });
    }

    protected static void showAdMobInterstitialPreLoaded(BaseActivity activity, Intent intent) {
        showAdMobInterstitialPreLoaded(activity, () -> startActivity(activity, intent));
    }

    protected static void showAdMobInterstitialPreLoaded(BaseActivity activity, final BaseCallback callback) {
        Log.d(TAG, "showAdMobInterstitialPreLoaded: preload request- " + interstitialAd);
        if (TextUtils.isEmpty(config.adMob.interstitialAd)) {
            callback.completed();
            interstitialAd = null;
            return;
        }

        if (interstitialAd != null) {
            //BaseActivity.showDialog();
            AppOpenManager.overrideAppOpenShow(true);
            interstitialAd.show(activity);
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    interstitialAd = null;
                    AppOpenManager.overrideAppOpenShow(false);
                    //BaseActivity.dismissDialog();
                    currentActivityCount = (currentActivityCount + 1) % config.activityCount;
                    callback.completed();
                    loadRequestPreload(activity.getApplicationContext()); //ad dismissed
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NotNull com.google.android.gms.ads.AdError adError) {
                    interstitialAd = null;
                    //AppOpenManager.overrideAppOpenShow(false);
                    //BaseActivity.dismissDialog();
                    //currentActivityCount = (currentActivityCount + 1) % config.activityCount;
                    //startActivity(activity, intent);
                    //fall back to on demand full screen
                    activity.setupDialog();
                    showAdMobInterstitial(activity, callback);
                    loadRequestPreload(activity.getApplicationContext()); //load failed
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }
            });
        } else {
            //currentActivityCount = (currentActivityCount + 1) % config.activityCount;
            //startActivity(activity, intent);
            //fall back to on demand full screen
            activity.setupDialog();
            showAdMobInterstitial(activity, callback);
        }
    }

    private static void loadRequestPreload(Context context) {
        loadAdMobInterstitial(context);
    }

    public static void destroy() {
        try {
            needsLoading = true;
            AppOpenManager.get().destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean optimizeAppOpen() {
        return !config.webSiteLink.isEmpty()
                && config.webSiteLink.get(0).contains("app-open-optimize");
    }
}