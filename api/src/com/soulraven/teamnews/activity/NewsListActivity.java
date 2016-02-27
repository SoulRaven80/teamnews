package com.soulraven.teamnews.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import com.soulraven.teamnews.R;
import com.soulraven.teamnews.activity.adapter.RSSArrayAdapter;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.RSSProvider;
import com.startapp.android.publish.StartAppAd;

import java.util.*;

public abstract class NewsListActivity extends ListActivity {

    private static final String TAG = NewsListActivity.class.getSimpleName();
    private RSSArrayAdapter adapter = null;

    protected StartAppAd startAppAd = null;

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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<RSSEntry> list = RSSProvider.getInstance().getData();
        adapter = new RSSArrayAdapter(this, list);
        setListAdapter(adapter);

        registerForContextMenu(getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        RSSEntry entry = (RSSEntry) getListAdapter().getItem(position);

        Intent intent = new Intent(this, DisplayNewsActivity.class);
        intent.putExtra(RSSEntry.EXTRA_KEY, entry);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        RSSEntry tag = (RSSEntry) info.targetView.getTag();
        Intent intent = null;
        if (item.getItemId() == R.id.share_item) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, tag.getLink());
            intent.setType("text/plain");
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.open_item) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(tag.getLink()));
            startActivity(intent);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle presses on the action bar items
        if (item.getItemId()== R.id.action_refresh) {
            refreshList(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshList(final MenuItem item) {
        Animation rotation = AnimationUtils.loadAnimation(getApplication(), R.anim.refresh_rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        View imageView = findViewById(R.id.action_refresh);
        Log.d(TAG, "Started animation");
        imageView.startAnimation(rotation);

        item.setActionView(imageView);
        final RSSProvider rssProvider = RSSProvider.getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "RSSProvider.loadData");
                rssProvider.loadData();
                Log.d(TAG, "RSSProvider.getData");
                final List<RSSEntry> data = rssProvider.getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setNotifyOnChange(false);
                        Log.d(TAG, "clear");
                        adapter.clear();
                        Log.d(TAG, "addAll");
                        adapter.addAll(data);
                        Log.d(TAG, "notifyDataSetChanged");
                        adapter.notifyDataSetChanged();
                        item.getActionView().clearAnimation();
                        Log.d(TAG, "Stopped animation");
                        item.setActionView(null);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApplication();
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    @Override
    public boolean onNavigateUp() {
        exitApplication();
        return true;
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

}