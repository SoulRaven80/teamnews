package com.soulraven.teamnews.nba.nets.activity;

import android.content.Intent;
import android.os.Bundle;
import com.soulraven.teamnews.rss.RSSProvider;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

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
        return "";
    }
}
