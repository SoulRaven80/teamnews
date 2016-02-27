package com.soulraven.teamnews.futbol.riverplate.activity;

import com.startapp.android.publish.StartAppAd;

public class NewsListActivity extends com.soulraven.teamnews.activity.NewsListActivity {

    public NewsListActivity() {
        startAppAd = new StartAppAd(this);
    }
}