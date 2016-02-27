package com.soulraven.teamnews.nba.rockets.activity;

import com.startapp.android.publish.StartAppAd;

public class NewsListActivity extends com.soulraven.teamnews.activity.NewsListActivity {

    public NewsListActivity() {
        this.startAppAd = new StartAppAd(this);
    }
}