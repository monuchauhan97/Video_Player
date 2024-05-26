package plugin.adsdk.service;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

import io.michaelrocks.paranoid.Obfuscate;
import plugin.adsdk.service.utils.PurchaseHandler;

@Obfuscate
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this);
        AppOpenManager.init(this);
        PurchaseHandler.init(this);
    }
}
