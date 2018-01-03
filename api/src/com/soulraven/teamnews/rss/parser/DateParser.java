package com.soulraven.teamnews.rss.parser;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateParser {

    private static final String TAG = DateParser.class.getSimpleName();

    private static final List<DateFormat> DATE_FORMAT_LIST = new ArrayList<>();
    static {
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyyMMddHHmm", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyyMMdd HHmm", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyyMMdd HHmmss", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH));
    }

    public static Date parse(String date) {
        for (DateFormat df : DATE_FORMAT_LIST) {
            try {
                return df.parse(date);
            } catch (ParseException e) {
//                silently fail
                // e.printStackTrace();
            }
        }
        Log.e(TAG, "Cannot parse date " + date);
        return null;
//        throw new ParseException("Unknown date format", 0);
    }
}
