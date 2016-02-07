package com.soulraven.teamnews.nba.rockets.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.soulraven.teamnews.nba.rockets.R;
import com.soulraven.teamnews.rss.RSSProvider;
import com.startapp.android.publish.StartAppAd;

public class NewsListActivity extends com.soulraven.teamnews.activity.NewsListActivity {

    private StartAppAd startAppAd = new StartAppAd(this);

    @Override
    protected void onPause() {
        super.onPause();
        startAppAd.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAppAd.onResume();
    }

    protected void exitApplication() {
        new AlertDialog.Builder(this).setMessage(R.string.confirm_on_exit)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startAppAd.onBackPressed();
                        finish();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public RSSProvider getRSSProvider() {
        return com.soulraven.teamnews.nba.rockets.rss.RSSProvider.getInstance();
    }
}