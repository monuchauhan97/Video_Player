package plugin.adsdk.extras;

import static android.os.Build.VERSION.SDK_INT;
import static plugin.adsdk.service.AdsUtility.config;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.michaelrocks.paranoid.Obfuscate;
import plugin.adsdk.R;
import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.AppOpenManager;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;
import plugin.adsdk.service.api.AppConfigApiService;
import plugin.adsdk.service.api.ListModel;
import plugin.adsdk.service.utils.PurchaseHandler;
import plugin.adsdk.service.utils.QurekaHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Obfuscate
public abstract class BaseLauncherActivity extends BaseActivity {
    @LayoutRes
    private final int splash;
    @LayoutRes
    private final int dashboard;

    @SuppressWarnings("unused")
    public BaseLauncherActivity(@LayoutRes int splash) {
        this.splash = splash;
        this.dashboard = R.layout.ad_activity_extra_dashboard;
    }

    @SuppressWarnings("unused")
    public BaseLauncherActivity(@LayoutRes int splash, @LayoutRes int dashboard) {
        this.splash = splash;
        this.dashboard = dashboard;
    }

    protected abstract Intent destinationIntent();

    protected abstract String extraAppContentText();

    @DrawableRes
    protected abstract int extraAppContentImage();

    protected String[] permissions() {
        return new String[]{};
    }

    protected Intent noAdsIntent() {
        return null;
    }

    protected abstract String baseURL();

    private AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AdsUtility.refreshTokens(this);
        View decorView = getWindow().getDecorView();
        int newUiOptions = decorView.getSystemUiVisibility();
        newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(newUiOptions);
        super.onCreate(savedInstanceState);
        if (SDK_INT >= Build.VERSION_CODES.N) {
            getWindow().setStatusBarColor(Window.DECOR_CAPTION_SHADE_AUTO);
        }

        setContentView(splash);
        //statusBarDark();

        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.registerListener(updatedListener);
        launchApiCall();
    }

    private final InstallStateUpdatedListener updatedListener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            String message = "An update has just been downloaded.";
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    };

    private void launchUpdateFlow(BaseCallback callback) {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            this,
                            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                                    .setAllowAssetPackDeletion(true)
                                    .build(),
                            REQUEST_CODE_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    callback.completed();
                }
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // If an in-app update is already running, resume the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            this,
                            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                                    .setAllowAssetPackDeletion(true)
                                    .build(),
                            REQUEST_CODE_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    callback.completed();
                }
            } else {
                callback.completed();
            }
        }).addOnCanceledListener(callback::completed).addOnFailureListener(e -> {
            e.printStackTrace();
            callback.completed();
        });
    }

    private void launchApiCall() {
        if (baseURL().isEmpty()) {
            showSnack("BASE URL not found!");
            return;
        }

        if (!AdsUtility.isNetworkConnected(this)) {
            showSnack("Please check your internet connection!");
            return;
        }

        //test devices
        @SuppressWarnings("SpellCheckingInspection")
        List<String> testDeviceIds = Arrays.asList(
                "D69BF67BD0A5BC4DEC9B55345782FBAC",
                "D8C4CCF98AE6BD8E992EA4C17C8FFFBB"
        );
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        final String packageName = getApplicationContext().getPackageName();
        final AppConfigApiService appConfigApiService = new AppConfigApiService(baseURL());
        appConfigApiService.getApp(packageName).enqueue(new Callback<ListModel>() {
            @Override
            public void onResponse(@NotNull Call<ListModel> call, @NotNull Response<ListModel> response) {
                if (response.body() != null && response.isSuccessful()) {
                    try {
                        config = response.body();

                        if (config.packageName.isEmpty()) {
                            onApiFailed("Invalid response!");
                            return;
                        }

                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        if (!pref.contains("key_countryCode")) {
                            String countryCode = getCountryCode().toLowerCase().trim();
                            pref.edit().putString("key_countryCode", countryCode).apply();
                        }
                        onApiSuccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                        onApiFailed("Could not parse response!");
                    }
                } else {
                    onApiFailed("Failed to fetch response!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ListModel> call, @NotNull Throwable t) {
                t.printStackTrace();
                onApiFailed("Cannot connect to Server!");
            }
        });
    }

    private void onApiSuccess() {
        if (PurchaseHandler.hasPurchased(this)) {
            config.migrateToNoAds();
            recheckUpdate(); //bypassing for purchased user
            return;
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String cc = pref.getString("key_countryCode", "");
        setupAdMembers(!cc.isEmpty() ? Collections.singletonList(cc) : Collections.emptyList());
    }

    private void onApiFailed(String msg) {
        showSnack(msg);
    }

    private void recheckUpdate() {
        launchUpdateFlow(this::proceed);
    }

    private void proceed() {
        if (config.forcedAppUpdate) {
            String message = "Update is required in order to get all features.";
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("UPDATE", view -> launchUpdateFlow(this::proceed));
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
            return;
        }

        boolean haveStartScreenData = !AdsUtility.config.startScreens.isEmpty();
        boolean shouldShowScreens = AdsUtility.config.startScreenRepeatCount > AdsUtility.startScreenCount;
        Intent intent;
        if (shouldShowScreens && haveStartScreenData) {
            intent = new Intent(this, StartOneActivity.class);
            intent.putExtra("DEST", destinationIntent());
            intent.putExtra("NO_ADS_INTENT", noAdsIntent());
            intent.putExtra("CONTENT_IMAGE", extraAppContentImage());
            if (extraAppContentText() != null)
                intent.putExtra("CONTENT_TEXT", extraAppContentText());
            intent.putExtra("DASH", dashboard);
        } else {
            intent = destinationIntent();
        }

        BaseCallback callback = () -> {
            startActivity(intent);
            //finish();
        };

        String[] permissions = permissions();
        if (permissions.length > 0) {
            boolean showAd = !AdsUtility.showInitialAppOpen() && !QurekaHandler.get().qurekaEnabled(); //extra qureka flag for no-inter no-app-open
            checkRunTimePermission(callback, showAd, permissions);
        } else {
            if (AdsUtility.showInitialAppOpen() || QurekaHandler.get().qurekaEnabled()) {    //extra qureka flag for no-inter no-app-open
                callback.completed();
            } else {
                showInterstitial(callback);
            }
        }
    }

    private void setupAdMembers(List<String> limitedArea) {
        //Toast.makeText(this, "" + limitedArea, Toast.LENGTH_SHORT).show();
        Log.d("TAGGER", "onResponse found: " + limitedArea);
        Log.d("TAGGER", "onResponse requested: " + config.limitedCountries);
        List<String> limitedCountries = Arrays.asList(config.limitedCountries.toLowerCase().split("-"));
        if (!Collections.disjoint(limitedArea, limitedCountries)) {
            config.addLimits();
        }

        try {
            String[] splits = config.screenCount.split("-");
            if (!splits[0].equals("")) {
                config.startScreens = new Gson().fromJson("[\"" + splits[0].replace("*", "\",\"") + "\"]", new TypeToken<List<String>>() {
                }.getType());

                config.startScreenRepeatCount = config.startScreens.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //screens setup & ad loading configurations
        int activityCount = config.activityCount;
        if (activityCount > 0) {
            config.activityCount = activityCount + 1;
        } else {
            config.activityCount = 1;
        }

        postInit(); //sdk initialization complete
    }

    protected void postInit() {
        Log.d("TAG", "INT postInit()");

        if (config.preloadNative) {
            //preloading natives
            AdsUtility.loadAdMobBigNativePreload(this); //initial request
        }

        if (config.preloadNative) {
            //preloading banners
            AdsUtility.loadAdMobBannerPreload(this); //initial request
        }

        //if initially app open not required, preload for next use
        boolean admobValidation = !TextUtils.isEmpty(config.adMob.appOpenId);
        boolean onDemand = config.onDemandAppOpen;
        if (admobValidation && !AdsUtility.showInitialAppOpen() && !onDemand) {
            if (AppOpenManager.get() != null) {
                AppOpenManager.get().loadAppOpen();
            }
        }

        //initial app open, load and show app open
        if (AdsUtility.showInitialAppOpen()) {
            //skip displaying fullscreen if initial app open is displayed
            //AdsUtility.currentActivityCount = (AdsUtility.currentActivityCount + 1) % config.activityCount;
            AppOpenManager.get().requestInitialAppOpen(this::recheckUpdate);
            if (AdsUtility.shouldPreloadFullScreen()) {
                //load full screen for next use
                AdsUtility.loadAdMobInterstitial(this);
            }
        } else {
            AdsUtility.currentActivityCount = 0;
            if (AdsUtility.shouldPreloadFullScreen()) {
                //load and show full screen
                AdsUtility.loadAdMobInterstitialWithCallback(this, this::recheckUpdate);
            } else {
                //on-demand full screen, wait for request
                recheckUpdate();
            }
        }
    }

    private void showSnack(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction("Retry", v -> launchApiCall());
        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @NonNull
    private String getCountryCode() {
        try {
            final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) {
                // SIM country code is available
                return simCountry.toLowerCase(Locale.getDefault());
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
                // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) {
                    // network country code is available
                    return networkCountry.toLowerCase(Locale.getDefault());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // nothing's working shoot the default locale ;)
        return getResources().getConfiguration().locale.getCountry().toLowerCase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            recheckUpdate();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
