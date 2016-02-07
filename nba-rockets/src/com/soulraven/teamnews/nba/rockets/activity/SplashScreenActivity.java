package com.soulraven.teamnews.nba.rockets.activity;

import android.content.Intent;
import android.os.Bundle;
import com.soulraven.teamnews.rss.RSSProvider;
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
        StartAppSDK.init(this, "206652533", true);
    }

    @Override
    protected void afterPropertiesInitialized(Bundle savedInstanceState) {
        startAppAd.showAd();
        startAppAd.loadAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        startAppAd.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        startAppAd.onPause();
    }

    @Override
    public RSSProvider getRSSProvider() {
        return com.soulraven.teamnews.nba.rockets.rss.RSSProvider.getInstance();
    }
}