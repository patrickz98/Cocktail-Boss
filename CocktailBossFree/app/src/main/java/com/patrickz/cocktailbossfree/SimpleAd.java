package com.patrickz.cocktailbossfree;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Map;

// https://firebase.google.com/docs/admob/android/interstitial

public class SimpleAd
{
    private final static String LOGTAG = "SimpleAd";  // Google Test

    private final static String ADMOB_ID_TEST    = "ca-app-pub-3940256099942544/6300978111"; // Google Test
    private final static String ADMOB_ID_PATRICK = "ca-app-pub-8214806806906370/3375427645";

    private final static String ADMOB_INTERSTITIAL_TEST    = "ca-app-pub-3940256099942544/1033173712"; // Google Test
    private final static String ADMOB_INTERSTITIAL_PATRICK = "ca-app-pub-8214806806906370/9832260441";

//    private final static String ADMOB_ID = ADMOB_ID_TEST;
//    private final static String ADMOB_INTERSTITIAL_ID = ADMOB_INTERSTITIAL_TEST;

    private final static String ADMOB_ID = ADMOB_ID_PATRICK;
    private final static String ADMOB_INTERSTITIAL_ID = ADMOB_INTERSTITIAL_PATRICK;

    private final static int adMargin = 2;
    private final static AdSize adSize = AdSize.BANNER;

    private static AdRequest adRequest()
    {
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

         return adRequestBuilder.build();
    }

    // ## AdMarker MEDIUM_RECTANGLE context --> layout ##
    public static void addAdvertisementMedium(Context context, ViewGroup layout)
    {
        LinearLayout containerLayout = SimpleLayout.getLayoutMargins(context, 20, 20, 20, 20);
        layout.addView(containerLayout);

        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        mAdView.setAdUnitId(ADMOB_ID);

        mAdView.setLayoutParams(SimpleLayout.getLayoutParams(-1, -1));

        mAdView.loadAd(adRequest());

        containerLayout.addView(mAdView);
    }

    // ## AdMarker BANNER context --> layout --> AdSize ##
    public static AdView addAdvertisement(Context context, ViewGroup layout, AdSize size)
    {
        AdView mAdView = new AdView(context);
        mAdView.setAdSize(size);
        mAdView.setAdUnitId(ADMOB_ID);

        int margin = SimpleLayout.getSize(adMargin);

        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(-1, -2);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lay.setMargins(margin, margin, margin, margin);

        mAdView.setLayoutParams(lay);
        mAdView.loadAd(adRequest());

        layout.addView(mAdView);

        return mAdView;
    }

    public static Map<String, Object> createContentLayout(Activity context, String title)
    {
        Map<String, Object> map = SimpleLayout.createContentLayout(context, title);

        LinearLayout contentLayout = (LinearLayout) map.get("contentLayout");
        contentLayout.removeAllViewsInLayout();

        //
        // AdBlock helper layout
        //

        RelativeLayout adLayout = new RelativeLayout(context);
        adLayout.setGravity(Gravity.BOTTOM);
        adLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        adLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        contentLayout.addView(adLayout);

        //
        // AdBanner
        //

        AdView mAdView = addAdvertisement(context, adLayout, adSize);
        map.put("mAdView", mAdView);

        //
        // container for scroll
        //

        LinearLayout container = new LinearLayout(context);

        RelativeLayout.LayoutParams paramsContainer = new RelativeLayout.LayoutParams(-1, -1);
        paramsContainer.setMargins(0, 0, 0, adSize.getHeightInPixels(context));

        container.setLayoutParams(paramsContainer);

        // Prevents AdView jumping
        container.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        adLayout.addView(container);

        //
        // Scroll
        //

        LinearLayout scroll = SimpleLayout.createScrollView(context, container);

        map.put("scrollLayout", scroll);

        return map;
    }

    public static Map<String, Object> createContentLayout(Activity context, int titleId)
    {
        return createContentLayout(context, Simple.getText(context, titleId));
    }

    // ## AdMarker INTERSTITIAL context ##
    public static void createInterstitialAd(Context context)
    {
        Log.d(LOGTAG, "createInterstitialAd()");

        final InterstitialAd mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(ADMOB_INTERSTITIAL_ID);

        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                super.onAdLoaded();

                Log.d(LOGTAG, "onAdLoaded()");

                if (! mInterstitialAd.isLoaded()) return;

                mInterstitialAd.show();
                Log.d(LOGTAG, "mInterstitialAd.show()");
            }
        });

        mInterstitialAd.loadAd(adRequest());
    }
}
