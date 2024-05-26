package plugin.adsdk.service.utils;

import static plugin.adsdk.service.AdsUtility.config;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aemerse.iap.DataWrappers;
import com.aemerse.iap.IapConnector;
import com.aemerse.iap.PurchaseServiceListener;

import java.util.Collections;
import java.util.Map;

import plugin.adsdk.BuildConfig;
import plugin.adsdk.service.BaseActivity;

public class PurchaseHandler {
    public static final String NO_ADS = "no_ads"; //sku-id for purchase

    private PurchaseHandler() {
    }

    public static void init(Context context) {
        setupPurchase(context);
    }

    private static IapConnector iapConnector;

    private static void setupPurchase(Context context) {
        if (iapConnector != null) {
            return;
        }
        iapConnector = new IapConnector(
                context,
                Collections.singletonList(NO_ADS),
                Collections.emptyList(),
                Collections.emptyList(),
                TextUtils.isEmpty(config.base64InAppKey) ? null : config.base64InAppKey,
                BuildConfig.DEBUG
        );

        iapConnector.addPurchaseListener(new PurchaseServiceListener() {
            @Override
            public void onPricesUpdated(@NonNull Map<String, DataWrappers.ProductDetails> map) {

            }

            @Override
            public void onProductPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                //setHasPurchased(context);
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(NO_ADS, true).apply();
                Toast.makeText(context, "Purchased, Please Restart Application.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProductRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                //setHasPurchased(context);
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(NO_ADS, true).apply();
            }
        });
    }

    public static void purchaseNoAds(BaseActivity activity) {
        if (iapConnector == null) {
            init(activity);
            Toast.makeText(activity, "resetting purchase, please try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        iapConnector.purchase(activity, NO_ADS);
    }

    public static boolean hasPurchased(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NO_ADS, false);
        //return true;
    }

}
