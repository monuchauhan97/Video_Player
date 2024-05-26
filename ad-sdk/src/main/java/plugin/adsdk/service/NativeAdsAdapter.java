package plugin.adsdk.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import plugin.adsdk.R;

@SuppressWarnings("unused")
public abstract class NativeAdsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final BaseActivity context;
    private final int listAdDelta;
    private final boolean smallNative;
    public static final int AD = 1;

    public NativeAdsAdapter(BaseActivity context, int listAdDelta) {
        this.context = context;
        this.listAdDelta = listAdDelta;
        this.smallNative = false;
    }

    public NativeAdsAdapter(BaseActivity context, boolean smallNative) {
        this.context = context;
        this.listAdDelta = AdsUtility.config.listNativeCount >= 0 ? AdsUtility.config.listNativeCount : 6;
        this.smallNative = smallNative;
    }

    public NativeAdsAdapter(BaseActivity context, int listAdDelta, boolean smallNative) {
        this.context = context;
        this.listAdDelta = listAdDelta;
        this.smallNative = smallNative;
    }

    public NativeAdsAdapter(BaseActivity context) {
        this.context = context;
        this.listAdDelta = AdsUtility.config.listNativeCount >= 0 ? AdsUtility.config.listNativeCount : 6;
        this.smallNative = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == AD) {
            AdRecyclerHolder holder;
            if (smallNative) {
                int container = R.layout.ad_item_small_native_container;
                holder = new AdRecyclerHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(container, viewGroup, false));
                context.nativeAdSmall(holder.native_ad_container);
            } else {
                int container = R.layout.ad_item_native_container;
                holder = new AdRecyclerHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(container, viewGroup, false));
                context.nativeAd(holder.native_ad_container);
            }
            return holder;
        }
        return createView(viewGroup, viewType);
    }

    static class AdRecyclerHolder extends RecyclerView.ViewHolder {
        public CardView native_ad_container;

        AdRecyclerHolder(View view) {
            super(view);
            this.native_ad_container = view.findViewById(R.id.native_ad_container);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder baseHolder, int position) {
        if (getItemViewType(baseHolder.getAdapterPosition()) != AD) {
            bindView(baseHolder, getRealPosition(baseHolder.getAdapterPosition()));
        }
    }

    public abstract void bindView(@NonNull RecyclerView.ViewHolder baseHolder, int position);

    public abstract RecyclerView.ViewHolder createView(@NonNull ViewGroup viewGroup, int viewType);

    public int viewType() {
        return 0;
    }

    public abstract int itemCount();

    private int getRealPosition(int position) {
        if (listAdDelta == 0) return position;

        int additionalContent = 0;
        int counter = 0;
        for (int i = 0; i < position; i++) {
            if (counter == listAdDelta) {
                counter = 0;
                additionalContent++;
            } else {
                counter++;
            }
        }
        return position - additionalContent;
    }

    @Override
    public int getItemCount() {
        int viewItems = itemCount();
        if (listAdDelta == 0) return viewItems;

        int additionalContent = 0;
        int counter = 0;
        for (int i = 0; i < viewItems; i++) {
            if (counter == listAdDelta) {
                counter = 0;
                additionalContent++;
            }
            counter++;
        }
        if (counter == listAdDelta) {
            additionalContent++;
        }
        return viewItems + additionalContent;
    }

    @Override
    public int getItemViewType(int position) {
        if (listAdDelta == 0) return viewType();

        List<Integer> adsIndexes = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < itemCount(); i++) {
            if (counter == listAdDelta) {
                counter = 0;
                adsIndexes.add(i + adsIndexes.size());
            }
            counter++;
        }
        if (counter == listAdDelta) {
            adsIndexes.add(itemCount() + adsIndexes.size());
        }
        if (adsIndexes.contains(position)) return AD;
        return viewType();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NativeSize {
        int BIG = 0;
        int SMALL = 1;
    }
}
