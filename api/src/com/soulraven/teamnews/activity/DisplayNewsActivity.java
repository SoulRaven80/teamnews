package com.soulraven.teamnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.AdapterView;
import com.soulraven.teamnews.R;
import com.soulraven.teamnews.model.RSSEntry;

public class DisplayNewsActivity extends Activity {

    private RSSEntry rssEntry = null;
    private String encoding = "UTF-8";


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_news);

        rssEntry = (RSSEntry) getIntent().getSerializableExtra(RSSEntry.EXTRA_KEY);

        WebView webView = (WebView) findViewById(R.id.preview);
        webView.getSettings().setDefaultTextEncodingName(encoding);
        webView.loadData(getHtmlPage(rssEntry), "text/html; charset=UTF-8", encoding);
    }   

    private String getHtmlPage(RSSEntry entry) {
        return "<html><body><h3>" + entry.getOriginalTitle() + "</h3>" +
                entry.getOriginalDescription() + "</body></html>";
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preview_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Intent intent = null;
        if (item.getItemId() == R.id.action_share) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, rssEntry.getLink());
            intent.setType("text/plain");
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.action_open) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(rssEntry.getLink()));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}