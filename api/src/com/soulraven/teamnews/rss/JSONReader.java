package com.soulraven.teamnews.rss;

import android.text.Html;
import android.util.Log;
import com.soulraven.teamnews.model.RSSEntry;
import com.soulraven.teamnews.rss.parser.DateParser;
import com.soulraven.teamnews.rss.parser.postprocess.RSSFilter;
import com.soulraven.teamnews.util.presentation.RSSRenderer;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.jsonjava.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONReader {

    private static final String TAG = JSONReader.class.getSimpleName();

    /*

{
   "rss":{
      "xmlns:media":"http://search.yahoo.com/mrss/",
      "channel":{
         "description":"InformaciÃ³n y noticias sobre el torneo de primera divisiÃ³n del fÃºtbol argentino",
         "title":"Futbolargentino.com - DivisiÃ³n",
         "link":"http://www.futbolargentino.com/primera-division/",
         "image":{
            "link":"http://www.futbolargentino.com/primera-division/",
            "title":"Futbolargentino.com - Primera DivisiÃ³n",
            "url":"http://www.futbolargentino.com/images/logo.png"
         },
         "copyright":"Copyright Futbolargentino.com",
         "language":"es-ar",
         "atom10:link":{
            "href":"http://www.futbolargentino.com/primera-division/rss/",
            "type":"application/rss+xml",
            "xmlns:atom10":"http://www.w3.org/2005/Atom",
            "rel":"self"
         },
         "item":[
            {
               "pubDate":"2017-12-30T19:05:00-03:00",
               "title":"Conoce al tÃ©cnico que estarÃ­a cerca de dirigir a Gimnasia y Esgrima de la Plata",
               "description":"Tras la salida de Mariano Soso del club, Facundo Sava&nbsp;ya suena como el posible reemplazo de Soso.\n",
               "guid":{
                  "isPermaLink":true
               },
               "link":"http://www.futbolargentino.com/primera-division/noticias/sdi/173700/conoce-al-tecnico-que-estaria-cerca-de-dirigir-a-gimnasia-y-esgrima-de-la-plata"
            },
            {
               "pubDate":"2017-12-30T18:48:00-03:00",
               "title":"Independiente tambiÃ©n quiere contar con los servicios de Romero",
               "description":"Silvio Romero de estar en agenda en River Plate, ahora pasar&iacute;a a la lista de Independiente de Avellaneda.\n",
               "guid":{
                  "isPermaLink":true
               },
               "link":"http://www.futbolargentino.com/primera-division/noticias/sdi/173697/independiente-tambien-quiere-contar-con-los-servicios-de-romero"
            }
         ],
         "ttl":10
      },
      "xmlns:atom":"http://www.w3.org/2005/Atom",
      "version":2
   }
}
     */
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
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"), 8);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        stream.close();
        String xml = sb.toString();
        org.json.jsonjava.JSONObject jObject = XML.toJSONObject(xml);
        String json = jObject.toString();

        return readEntriesArray(new JSONObject(json), filter);
    }

    public List<RSSEntry> readEntriesArray(final JSONObject json, final RSSFilter filter) throws JSONException, IOException, ParseException {
        List<RSSEntry> entries = new ArrayList<RSSEntry>();

        JSONObject responseData = json.getJSONObject("rss");
        JSONObject feed = responseData.getJSONObject("channel");
        JSONArray jsonArray = feed.getJSONArray("item");
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

        try {
            rssEntry.setTitle(Html.fromHtml(jsonEntry.getString("title")).toString());
            rssEntry.setDescription(Html.fromHtml(jsonEntry.getString("description")).toString());
            rssEntry.setOriginalDescription(StringEscapeUtils.unescapeJava(jsonEntry.getString("description")));
            rssEntry.setLink(jsonEntry.getString("link"));
            rssEntry.setPubDate(DateParser.parse(jsonEntry.getString("pubDate")));
            try {
                rssEntry.setImageLink(jsonEntry.getJSONArray("mediaGroups").getJSONObject(0).getJSONArray("contents").getJSONObject(0).getString("url"));
            }
            catch (Exception e) {
                Log.e(TAG, "Cannot found image link");
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Error processing the rss entry", e);
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