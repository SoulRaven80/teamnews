package com.soulraven.teamnews.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.soulraven.teamnews.lazylist.ImageLoader;
import com.soulraven.teamnews.R;
import com.soulraven.teamnews.model.RSSEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RSSArrayAdapter extends ArrayAdapter<RSSEntry> {

    private static final String TAG = RSSArrayAdapter.class.getSimpleName();
    private static final DateFormat UI_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm");

    private final Context context;
    private ImageLoader imageLoader;
    private List<String> dates;
    private Handler handler;

    public RSSArrayAdapter(final Context context, final List<RSSEntry> values) {
        super(context, R.layout.activity_news, values);
        this.context = context;
        this.imageLoader = new ImageLoader(context);
        this.dates = new ArrayList<String>();
        this.handler = new Handler(context.getMainLooper());
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(final int position) {
        return position % 4;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View rowView;

        if (convertView != null) {
            rowView = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.activity_news, parent, false);
        }

        final TextView titleView = (TextView) rowView.findViewById(R.id.news_title);
        final TextView descriptionView = (TextView) rowView.findViewById(R.id.news_description);
        final TextView dateView = (TextView) rowView.findViewById(R.id.news_date);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.news_icon);

        final RSSEntry rssEntry = getItem(position);
        rowView.setTag(rssEntry);
        titleView.setText(rssEntry.getTitle());
        if (rssEntry.getPubDate() != null) {
            try {
                String date = UI_FORMAT.format(rssEntry.getPubDate());
                dateView.setText(date);
            }
            catch (Exception e) {
                Log.e(TAG, "Error parsing the publication date", e);
                dateView.setVisibility(View.GONE);
            }
        }
        else {
            dateView.setVisibility(View.GONE);
        }
        descriptionView.setText(rssEntry.getDescription());
        try {
            Log.d(TAG, rssEntry.getImageLink());
            if (rssEntry.getImageLink() != null && !rssEntry.getImageLink().trim().isEmpty()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = imageLoader.getBitmap(rssEntry.getImageLink());
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
            else {
                int v = (int)(Math.random() * 12);
                final int resource;
                switch (v) {
                    case 1:
                        resource = R.drawable.stub1;
                    break;
                    case 2:
                        resource = R.drawable.stub2;
                        break;
                    case 3:
                        resource = R.drawable.stub3;
                        break;
                    case 4:
                        resource = R.drawable.stub4;
                        break;
                    case 5:
                        resource = R.drawable.stub5;
                        break;
                    case 6:
                        resource = R.drawable.stub6;
                        break;
                    case 7:
                        resource = R.drawable.stub7;
                        break;
                    case 8:
                        resource = R.drawable.stub8;
                        break;
                    case 9:
                        resource = R.drawable.stub9;
                        break;
                    case 10:
                        resource = R.drawable.stub10;
                        break;
                    case 11:
                        resource = R.drawable.stub11;
                        break;
                    default:
                        resource = R.drawable.logo;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(resource);
                    }
                });
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Error setting the bitmap image", e);
        }
        return rowView;
    }

    @Override
    public void clear() {
        dates.clear();
        super.clear();
    }
}
