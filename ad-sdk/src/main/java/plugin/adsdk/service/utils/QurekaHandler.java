package plugin.adsdk.service.utils;

import static plugin.adsdk.service.AdsUtility.config;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import plugin.adsdk.R;
import plugin.adsdk.service.BaseActivity;

public class QurekaHandler {

    private static QurekaHandler instance;

    private QurekaHandler() {
    }

    public static QurekaHandler get() {
        if (instance == null) {
            instance = new QurekaHandler();
        }
        return instance;
    }

    public boolean qurekaEnabled() {
        return config.qurekaEnabled;
    }

    public void openQureka(BaseActivity activity) {
        if (!qurekaEnabled()) {
            Toast.makeText(activity, "Coming Soon!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
                    .setNavigationBarColor(ContextCompat.getColor(activity, R.color.ad_qureka_color))
                    .setToolbarColor(ContextCompat.getColor(activity, R.color.ad_qureka_color))
                    .setSecondaryToolbarColor(ContextCompat.getColor(activity, R.color.ad_qureka_color))
                    .build();
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setDefaultColorSchemeParams(params);
            CustomTabsIntent customTabsIntent = builder.build();
            if (activity.getPackageManager().getPackageInfo("com.android.chrome", 0) != null) {
                customTabsIntent.intent.setPackage("com.android.chrome");
            }
            customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(activity, Uri.parse(config.qurekaURL));
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(activity, "Coming Soon!", Toast.LENGTH_SHORT).show();
        }
    }

}
