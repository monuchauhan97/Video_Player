package plugin.adsdk.service;

import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static plugin.adsdk.service.AdsUtility.config;
import static plugin.adsdk.service.AdsUtility.currentActivityCount;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashSet;

public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String TAG = "AppOpenManager";
    private static AppOpenManager self;
    private static boolean isShowingAd = false;
    private long lastLoadTime = 0;

    private AppOpenAd appOpenAd = null;
    private WeakReference<Activity> currentActivity;
    private static final HashSet<String> blockedComponent = new HashSet<>();

    private AppOpenManager(Application application) {
        application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public static AppOpenManager get() {
        return self;
    }

    void destroy() {
        //appOpenAd = null; //this only works for preload ao
        config.adMob.appOpenId = ""; //required for on-demand ao
    }

    public static void init(Application application) {
        self = new AppOpenManager(application);
    }

    static void refreshAppOpen(BaseActivity activity) {
        blockedComponent.clear();
        if (AdsUtility.showInitialAppOpen()) {
            if (self != null) {
                self.appOpenAd = null;
            }
        } else {
            blockAppOpen(activity);
        }
    }

    public static void blockAppOpen(BaseActivity activity) {
        if (!activity.getComponentName().toString().isEmpty()) {
            blockedComponent.add(activity.getComponentName().toString());
        }
    }

    public void loadAppOpen() {
        if (self == null) return;

        showAdIfAvailable();
    }

    @SuppressWarnings("deprecation")
    @OnLifecycleEvent(ON_START)
    public void onStart() {
        boolean onDemand = config.onDemandAppOpen;
        if (onDemand) {
            //app-open with loader
            requestOnDemandAdmobAppOpen();
        } else {
            showAdIfAvailable();
        }
        Log.d(TAG, "onStart");
    }

    public boolean isAdmobAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    public void fetchAdmobAd() {
        // Have unused ad, no need to fetch another.
        boolean onDemand = config.onDemandAppOpen;
        if (isAdmobAdAvailable() || TextUtils.isEmpty(AdsUtility.config.adMob.appOpenId) || onDemand) {
            return;
        }

        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NotNull AppOpenAd ad) {
                AppOpenManager.this.appOpenAd = ad;
                lastLoadTime = new Date().getTime();
            }

            @Override
            public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                // Handle the error.
                Log.e(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
            }

        };

        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                currentActivity.get(),
                AdsUtility.config.adMob.appOpenId,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                loadCallback
        );
    }

    public void showAdIfAvailable() {
        showAdmobAdIfAvailable();
    }

    public void showAdmobAdIfAvailable() {
        boolean isBlocked = blockedComponent.contains(currentActivity.get().getComponentName().toString());
        if (!isShowingAd && isAdmobAdAvailable() && !isBlocked) {
            Log.d(TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;
                            fetchAdmobAd(); //closed
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity.get());

            //shifted above on ad load failed, when ad not available shift to next id
            //AdsUtility.appOpenCount = (AdsUtility.appOpenCount + 1) % AdsUtility.config.adMob.appOpenId.size();
        } else {
            Log.d(TAG, "Can not show ad.");
            fetchAdmobAd(); //blocked or no id or already showing
        }
    }

    public void requestInitialAppOpen(@NonNull final OnShowAdCompleteListener listener) {
        requestInitialAdmobAppOpen(listener);
    }

    private void requestInitialAdmobAppOpen(@NonNull final OnShowAdCompleteListener listener) {
        if (TextUtils.isEmpty(AdsUtility.config.adMob.appOpenId)) {
            listener.onShowAdComplete();
            return;
        }

        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NotNull AppOpenAd ad) {
                AppOpenManager.this.appOpenAd = ad;
                lastLoadTime = new Date().getTime();
//                retryCount = 0; //reset after initial ad

                FullScreenContentCallback fullScreenContentCallback =
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Set the reference to null so isAdAvailable() returns false.
                                AppOpenManager.this.appOpenAd = null;
                                isShowingAd = false;
                                fetchAdmobAd(); //closed initial

                                if (config.countAppOpenInterval) { //true - show ad on first click
                                    currentActivityCount = (currentActivityCount + 1) % config.activityCount;   //count first app-open in interval
                                }
                                listener.onShowAdComplete();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {
                                Log.e(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                                listener.onShowAdComplete();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                isShowingAd = true;
                            }
                        };

                appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                appOpenAd.show(currentActivity.get());

                //shifted above on ad load failed, when ad not available shift to next id
                //AdsUtility.appOpenCount = (AdsUtility.appOpenCount + 1) % AdsUtility.config.adMob.appOpenId.size();
            }

            @Override
            public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                // Handle the error.
                Log.e(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                listener.onShowAdComplete();
            }
        };

        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                currentActivity.get(),
                AdsUtility.config.adMob.appOpenId,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                loadCallback
        );
    }

    private void requestOnDemandAdmobAppOpen() {
        if (blockedComponent.contains(currentActivity.get().getComponentName().toString())) return;
        if (TextUtils.isEmpty(AdsUtility.config.adMob.appOpenId)) return;
        if (isShowingAd) return;

        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NotNull AppOpenAd ad) {
                FullScreenContentCallback fullScreenContentCallback =
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                isShowingAd = false;
                                BaseActivity.dismissDialog();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {
                                Log.e(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                                isShowingAd = false;
                                BaseActivity.dismissDialog();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                isShowingAd = true;
                                BaseActivity.dismissDialog();
                            }
                        };

                ad.setFullScreenContentCallback(fullScreenContentCallback);
                ad.show(currentActivity.get());
                //shifted above on ad load failed, when ad not available shift to next id
                //AdsUtility.appOpenCount = (AdsUtility.appOpenCount + 1) % AdsUtility.config.adMob.appOpenId.size();
            }

            @Override
            public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                // Handle the error.
                Log.e(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                BaseActivity.dismissDialog();
            }
        };

        if (currentActivity.get() instanceof BaseActivity) {
            ((BaseActivity) currentActivity.get()).setupDialog();
            ((BaseActivity) currentActivity.get()).showDialog();
        }
        AdRequest request = new AdRequest.Builder().build();
        String appOpenId = config.adMob.appOpenId;
        int orientation = AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT;
        AppOpenAd.load(currentActivity.get(), appOpenId, request, orientation, loadCallback);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NotNull Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityResumed(@NotNull Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStopped(@NotNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NotNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NotNull Activity activity, @NotNull Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(@NotNull Activity activity) {
        //currentActivity = null;
    }

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    public static void overrideAppOpenShow(boolean override) {
        isShowingAd = override;
    }

    private boolean wasLoadTimeLessThanNHoursAgo(@SuppressWarnings("SameParameterValue") long numHours) {
        long dateDifference = (new Date()).getTime() - lastLoadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }
}
