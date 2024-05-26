package plugin.adsdk.service.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonModel {

    @SerializedName("interstitial_ad")
    @Expose
    public String interstitialAd;
    @SerializedName("banner_ad")
    @Expose
    public String bannerAd;
    @SerializedName("video_ad")
    @Expose
    public String videoAd;
    @SerializedName("native_ad")
    @Expose
    public String nativeAd;
    @SerializedName("app_open_ad")
    @Expose
    public String appOpenId;

}
