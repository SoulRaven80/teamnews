package com.soulraven.teamnews.nba.spurs.activity;

public class SplashScreenActivity extends com.soulraven.teamnews.activity.SplashScreenActivity {

    @Override
    protected Class<NewsListActivity> getListActivityClass() {
        return NewsListActivity.class;
    }

    @Override
    protected String getAppId() {
        return "205500306";
    }
}
