package com.soulraven.teamnews.nba.rockets.activity;

import com.startapp.android.publish.StartAppAd;

public class SplashScreenActivity extends com.soulraven.teamnews.activity.SplashScreenActivity {

    public SplashScreenActivity() {
        this.startAppAd = new StartAppAd(this);
    }

    @Override
    protected Class<NewsListActivity> getListActivityClass() {
        return NewsListActivity.class;
    }

    @Override
    protected String getAppId() {
        return "206652533";
    }
}
