package com.soulraven.teamnews.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.Toast;
import com.soulraven.teamnews.R;
import com.soulraven.teamnews.properties.PropertiesLoader;

public abstract class SplashScreenActivity extends Activity implements ProviderActivity {

    private static final long SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(savedInstanceState);

        setContentView(R.layout.activity_splash);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PropertiesLoader.initialize(SplashScreenActivity.this);

                afterPropertiesInitialized(savedInstanceState);

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data
                    if (getRSSProvider().loadData(SplashScreenActivity.this)) {
                        startActivity(getListIntent());
                    } else {
                        exitOnError(getResources().getString(R.string.error_no_fetched_data));
                    }
                } else {
                    // TODO display error... & close the application?
                    exitOnError(getResources().getString(R.string.error_no_connection));
                }
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void exitOnError(final String text) {
        Toast toast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    protected void init(final Bundle savedInstanceState) { }

    protected void afterPropertiesInitialized(final Bundle savedInstanceState) { }

    protected abstract Intent getListIntent();
}
