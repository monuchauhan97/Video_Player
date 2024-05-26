package plugin.adsdk.extras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import plugin.adsdk.R;
import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;

public class ThankYouBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = "ThankYouBottomSheetFrag";

    public static ThankYouBottomSheet newInstance() {
        return new ThankYouBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ad_thank_you_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.tvShare).setOnClickListener(this);
        view.findViewById(R.id.tvQuit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        dismiss();
        if (viewId == R.id.tvShare) {
            AdsUtility.rateUs(((BaseActivity) requireActivity()));
        } else if (viewId == R.id.tvQuit) {
            AdsUtility.destroy();
            try {
                requireActivity().finishAffinity();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

}
