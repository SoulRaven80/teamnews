package com.soulraven.teamnews.rss;

import android.text.Html;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.util.presentation.RSSRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.soulraven.teamnews.rss.parser.RSSParser.RSS_FORMAT;

public class JSONReader {

/*{
   "responseData":{
      "feed":{
         "feedUrl":"http://web.ole.com.ar/rss/section/66/",
         "title":"Olé - River Plate",
         "link":"http://www.ole.com.ar/river-plate/",
         "author":"",
         "description":"Olé - River Plate",
         "type":"rss20",
         "entries":[
            {
               "title":"Horarios con dudas",
               "link":"http://www.ole.com.ar/futbol-primera/Horarios-dudas_0_1325867724.html",
               "author":"",
               "publishedDate":"Mon, 23 Mar 2015 16:44:42 -0700",
               "contentSnippet":"Un tentativo de cómo se juega la fecha que viene, aunque la AFA aún no definió oficialmente cómo se disputará el sábado, día en ...",
               "content":"Un tentativo de cómo se juega la fecha que viene, aunque la AFA aún no definió oficialmente cómo se disputará el sábado, día en que también se presenta la Selección.",
               "categories":[

               ]
            },
            {
               "title":"\"Necesitábamos este envión\"",
               "link":"http://www.ole.com.ar/river-plate/futbol/Necesitabamos-envion_0_1325867718.html",
               "author":"",
               "publishedDate":"Mon, 23 Mar 2015 16:30:00 -0700",
               "contentSnippet":"Antes   de sumarse a su selección, Teo Gutiérrez destacó lo importante que fue para River haber regresado al triunfo ante Godoy ...",
               "content":"Antes   de sumarse a su selección, Teo Gutiérrez destacó lo importante que fue para River haber regresado al triunfo ante Godoy Cruz. Además, agradeció el cariño de la gente y   confía en torcer la historia en la Libertadores.",
               "categories":[

               ]
            }
         ]
      }
   },
   "responseDetails":null,
   "responseStatus":200
}*/

    public List<RSSEntry> readJsonStream(final InputStream stream, final RSSFilter filter) throws IOException, ParseException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        stream.close();
        return readEntriesArray(new JSONObject(sb.toString()), filter);
    }

    public List<RSSEntry> readEntriesArray(final JSONObject json, final RSSFilter filter) throws JSONException, IOException, ParseException {
        List<RSSEntry> entries = new ArrayList<RSSEntry>();

        JSONObject responseData = json.getJSONObject("responseData");
        JSONObject feed = responseData.getJSONObject("feed");
        JSONArray jsonArray = feed.getJSONArray("entries");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonEntry = jsonArray.getJSONObject(i);
            if (filter.filter(jsonEntry)) {
                entries.add(readEntry(jsonEntry));
            }
        }

        return entries;
    }

    public RSSEntry readEntry(final JSONObject jsonEntry) throws IOException, ParseException, JSONException {
        RSSEntry rssEntry = new RSSEntry();

        /*
        newEntry.mediaurl = entry.mediaGroups[0].contents[0].url;
         */
        rssEntry.setTitle(Html.fromHtml(jsonEntry.getString("title")).toString());
        rssEntry.setDescription(Html.fromHtml(jsonEntry.getString("contentSnippet")).toString());
        rssEntry.setOriginalDescription(Html.fromHtml(jsonEntry.getString("content")).toString());
        rssEntry.setLink(jsonEntry.getString("link"));
        rssEntry.setPubDate(RSS_FORMAT.parse(jsonEntry.getString("publishedDate")));
        try {
            rssEntry.setImageLink(jsonEntry.getJSONArray("mediaGroups").getJSONObject(0).getJSONArray("contents").getJSONObject(0).getString("url"));
        }
        catch (Exception e) {
            System.out.println("No image link information");
        }
        return rssEntry;
    }

    // TODO see usage
    private String stripHtml(final String text) {
        String html = RSSRenderer.stripHTML(text);
        String spanned = Html.fromHtml(Html.fromHtml(html).toString()).toString();
        return spanned.trim();
    }

    // TODO see usage
    private String getImg(final String text) {
        String ret = null;
        Pattern pattern = Pattern.compile("<img\\s+src\\s*=\\s*([\"'][^\"']+[\"']+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            ret = matcher.group(1).replaceAll("'", "");
        }
        return ret;
    }
}