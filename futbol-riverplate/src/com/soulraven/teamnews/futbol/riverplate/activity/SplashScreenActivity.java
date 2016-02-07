package com.soulraven.teamnews.futbol.riverplate.activity;

import android.content.Intent;
import android.os.Bundle;
import com.soulraven.teamnews.rss.RSSProvider;
import com.startapp.android.publish.SDKAdPreferences;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class SplashScreenActivity extends com.soulraven.teamnews.activity.SplashScreenActivity {

    private StartAppAd startAppAd = new StartAppAd(this);
    private Intent intent = null;

    @Override
    protected Intent getListIntent() {
        if (intent == null) {
            intent = new Intent(this, NewsListActivity.class);
        }
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        StartAppSDK.init(this, "206883327", true);
    }

    @Override
    protected void afterPropertiesInitialized(Bundle savedInstanceState) {
        startAppAd.showAd();
        startAppAd.loadAd();
    }

    @Override
    public RSSProvider getRSSProvider() {
        return com.soulraven.teamnews.futbol.riverplate.rss.RSSProvider.getInstance();
    }
}
