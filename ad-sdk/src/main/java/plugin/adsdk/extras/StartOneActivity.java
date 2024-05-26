package plugin.adsdk.extras;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import plugin.adsdk.DebouncedOnClickListener;
import plugin.adsdk.R;
import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.utils.PurchaseHandler;
import plugin.adsdk.service.utils.QurekaHandler;

public class StartOneActivity extends BaseActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        AdsUtility.startScreenCount++;
        if (AdsUtility.startScreenCount <= 0) {
            AdsUtility.startScreenCount = 1;
        }

        @LayoutRes
        int dashboard = getIntent().getIntExtra("DASH", R.layout.ad_activity_extra_onboard);
        try {
            if (AdsUtility.config.startScreens.get(AdsUtility.startScreenCount - 1)
                    .equalsIgnoreCase("dashboard")) {
                setContentView(dashboard);
                initDashboardData();
            } else {
                if (AdsUtility.startScreenCount % 2 == 0) {
                    setContentView(R.layout.ad_activity_extra_start_even);
                } else {
                    setContentView(R.layout.ad_activity_extra_start_odd);
                }
                initStartData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setContentView(dashboard);
            initDashboardData();
        }
    }

    private void callIntent() {
        if (AdsUtility.config.startScreenRepeatCount > AdsUtility.startScreenCount) {
            Intent intent = new Intent(StartOneActivity.this, StartOneActivity.class);
            if (getIntent().getParcelableExtra("DEST") != null) {
                intent.putExtra("DEST", (Intent) getIntent().getParcelableExtra("DEST"));
            }
            intent.putExtra("DASH", getIntent().getIntExtra("DASH", R.layout.ad_activity_extra_onboard));
            if (getIntent().getParcelableExtra("NO_ADS_INTENT") != null) {
                intent.putExtra("NO_ADS_INTENT", (Intent) getIntent().getParcelableExtra("NO_ADS_INTENT"));
            }
            if (getIntent().hasExtra("CONTENT_IMAGE")) {
                intent.putExtra("CONTENT_IMAGE", getIntent().getIntExtra("CONTENT_IMAGE", R.drawable.ic_arrow_down));
            }
            if (getIntent().hasExtra("CONTENT_TEXT")) {
                intent.putExtra("CONTENT_TEXT", getIntent().getStringExtra("CONTENT_TEXT"));
            }
            showInterstitial(intent);
        } else {
            if (getIntent().getParcelableExtra("DEST") != null) {
                Intent dest = getIntent().getParcelableExtra("DEST");
                showInterstitial(dest);
            } else {
                Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
                AdsUtility.destroy();
                try {
                    finishAffinity();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }

    private void initDashboardData() {
        nativeAd();
        bannerAd();

        View noAds = findViewById(R.id.noAds);
        if (noAds != null && AdsUtility.config.qurekaButtons.contains("1")) {
            noAds.setVisibility(PurchaseHandler.hasPurchased(this) ? View.GONE : View.VISIBLE);
            noAds.setOnClickListener(v -> {
                Intent noAdsIntent = getIntent().getParcelableExtra("NO_ADS_INTENT");
                if (noAdsIntent != null) {
                    showInterstitial(noAdsIntent);
                } else {
                    PurchaseHandler.purchaseNoAds(this);
                }
            });
        }

        DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(500) {
            @Override
            public void onDebouncedClick(View v) {
                if (v.getId() == R.id.start) {
                    callIntent();
                } else if (v.getId() == R.id.ivShare) {
                    AdsUtility.shareApp(StartOneActivity.this);
                } else if (v.getId() == R.id.ivPrivacy) {
                    AdsUtility.privacyPolicy(StartOneActivity.this);
                } else if (v.getId() == R.id.ivRate) {
                    AdsUtility.rateUs(StartOneActivity.this);
                } else if (v.getId() == R.id.TermsText) {
                    AdsUtility.privacyPolicy(StartOneActivity.this);
                }
            }
        };

        findViewById(R.id.start).setOnClickListener(debouncedOnClickListener);
        try {
            View ivShare = findViewById(R.id.ivShare);
            if (ivShare != null)
                ivShare.setOnClickListener(debouncedOnClickListener);
            View ivPrivacy = findViewById(R.id.ivPrivacy);
            if (ivPrivacy != null)
                ivPrivacy.setOnClickListener(debouncedOnClickListener);
            View ivRate = findViewById(R.id.ivRate);
            if (ivRate != null)
                ivRate.setOnClickListener(debouncedOnClickListener);
            View termsText = findViewById(R.id.TermsText);
            if (termsText != null)
                termsText.setOnClickListener(debouncedOnClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView contentImage = findViewById(R.id.contentImage);
        if (contentImage != null) {
            if (getIntent().hasExtra("CONTENT_IMAGE")) {
                contentImage.setImageResource(getIntent().getIntExtra("CONTENT_IMAGE", R.drawable.ic_arrow_down));
            } else {
                contentImage.setVisibility(View.GONE);
            }
        }
        TextView contentText = findViewById(R.id.contentText);
        if (contentText != null) {
            if (getIntent().hasExtra("CONTENT_TEXT")) {
                contentText.setText(Html.fromHtml(getIntent().getStringExtra("CONTENT_TEXT")));
            } else {
                contentText.setVisibility(View.GONE);
            }
        }

        final NestedScrollView scrollView = findViewById(R.id.scrollView);
//        final FloatingActionButton fab = findViewById(R.id.fabScrollDown);
     /*   if (fab != null && scrollView != null) {
            ViewTreeObserver observer = scrollView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int viewHeight = scrollView.getMeasuredHeight();
                    int contentHeight = scrollView.getChildAt(0).getHeight();
                    if (viewHeight - contentHeight < 0) {
                        fab.setVisibility(View.VISIBLE);
                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        fab.setVisibility(View.GONE);
                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
            fab.setOnClickListener(v -> {
                try {
                    scrollView.smoothScrollTo(0, scrollView.getChildAt(0).getHeight());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        if (scrollView != null) {
            scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                        if (v.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollY)) {
                            if (fab != null && fab.getVisibility() == View.VISIBLE) {
                                fab.setVisibility(View.GONE);
                            }
                        } else {
                            if (fab != null && fab.getVisibility() == View.GONE) {
                                fab.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }*/
    }

    public void onBackPressed() {
        if (AdsUtility.startScreenCount != 1) {
            AdsUtility.startScreenCount--;
            backPressed();
        } else {
            showThankYouSheet();
        }
    }

    //    Start Data
    private void initStartData() {
        nativeAd();
        bannerAd();

        ConstraintLayout llQureka2 = findViewById(R.id.llQureka2);
        ConstraintLayout llQureka3 = findViewById(R.id.llQureka3);
        TextView ad_call_to_action_zero = findViewById(R.id.ad_call_to_action_zero);
        TextView ad_call_to_action_two = findViewById(R.id.ad_call_to_action_two);
        TextView ad_call_to_action_three = findViewById(R.id.ad_call_to_action_three);

        llQureka2.setVisibility(View.GONE);
        llQureka3.setVisibility(View.GONE);
        if (!AdsUtility.config.qurekaButtons.isEmpty()) {
            if (AdsUtility.config.qurekaButtons.contains("3") && AdsUtility.config.qurekaEnabled) {
                llQureka3.setVisibility(View.VISIBLE);
            }
            if (AdsUtility.config.qurekaButtons.contains("2") && AdsUtility.config.qurekaEnabled) {
                llQureka2.setVisibility(View.VISIBLE);
            }
            View noAds = findViewById(R.id.noAds);
            if (noAds != null && AdsUtility.config.qurekaButtons.contains("1")) {
                noAds.setVisibility(PurchaseHandler.hasPurchased(this) ? View.GONE : View.VISIBLE);
                noAds.setOnClickListener(v -> {
                    Intent noAdsIntent = getIntent().getParcelableExtra("NO_ADS_INTENT");
                    if (noAdsIntent != null) {
                        showInterstitial(noAdsIntent);
                    } else {
                        PurchaseHandler.purchaseNoAds(this);
                    }
                });
            }
        }

        String[] splitCTA = AdsUtility.config.startScreens.get(AdsUtility.startScreenCount - 1).split(",");

        String ctaOne;
        try {
            ctaOne = splitCTA.length > 0 ? splitCTA[0] : "";
        } catch (Exception e) {
            e.printStackTrace();
            ctaOne = "";
        }

        String ctaTwo;
        try {
            ctaTwo = splitCTA.length > 1 ? splitCTA[1] : "";
        } catch (Exception e) {
            e.printStackTrace();
            ctaTwo = "";
        }

        String ctaThree;
        try {
            ctaThree = splitCTA.length > 2 ? splitCTA[2] : "";
        } catch (Exception e) {
            e.printStackTrace();
            ctaThree = "";
        }

        String finalCtaTwo = ctaTwo.toLowerCase();
        String finalCtaThree = ctaThree.toLowerCase();
        ad_call_to_action_zero.setText(ctaOne);

        if (ctaTwo.isEmpty()) {
            ad_call_to_action_two.setVisibility(View.GONE);
        } else {
            if (finalCtaTwo.contains("more")) {
                findViewById(R.id.ivAdTag).setVisibility(View.VISIBLE);
                ConstraintLayout.LayoutParams layoutParams =
                        new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin = 15;
                layoutParams.rightMargin = 15;
                layoutParams.topToTop = R.id.ad_call_to_action_two;
                layoutParams.endToEnd = R.id.ad_call_to_action_two;
                findViewById(R.id.ivAdTag).setLayoutParams(layoutParams);
            }
            ad_call_to_action_two.setText(ctaTwo);
        }

        if (ctaThree.isEmpty()) {
            ad_call_to_action_three.setVisibility(View.GONE);
        } else {
            if (finalCtaThree.contains("more")) {
                findViewById(R.id.ivAdTag).setVisibility(View.VISIBLE);
                ConstraintLayout.LayoutParams layoutParams =
                        new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin = 15;
                layoutParams.rightMargin = 15;
                layoutParams.topToTop = R.id.ad_call_to_action_three;
                layoutParams.endToEnd = R.id.ad_call_to_action_three;
                findViewById(R.id.ivAdTag).setLayoutParams(layoutParams);
            }
            ad_call_to_action_three.setText(ctaThree);
        }

        DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                int id = v.getId();
                if (id == R.id.ad_call_to_action_zero) {
                    callIntent();
                } else if (id == R.id.ad_call_to_action_two) {
                    getExtraIntent(finalCtaTwo);
                } else if (id == R.id.ad_call_to_action_three) {
                    getExtraIntent(finalCtaThree);
                } else if (id == R.id.llQureka2 || id == R.id.llQureka3) {
                    QurekaHandler.get().openQureka(StartOneActivity.this);
                }
            }
        };

        ad_call_to_action_zero.setOnClickListener(debouncedOnClickListener);
        ad_call_to_action_two.setOnClickListener(debouncedOnClickListener);
        ad_call_to_action_three.setOnClickListener(debouncedOnClickListener);
        llQureka2.setOnClickListener(debouncedOnClickListener);
        llQureka3.setOnClickListener(debouncedOnClickListener);
    }

    private void getExtraIntent(@NonNull String cta) {
        if (cta.contains("share")) {
            AdsUtility.shareApp(this);
        } else if (cta.contains("rate")) {
            AdsUtility.rateUs(this);
        } else if (cta.contains("more")) {
            AdsUtility.moreApps(this);
        } else if (cta.contains("privacy")) {
            AdsUtility.privacyPolicy(this);
        } else {
            Toast.makeText(this, "cannot perform action", Toast.LENGTH_SHORT).show();
        }
    }
}