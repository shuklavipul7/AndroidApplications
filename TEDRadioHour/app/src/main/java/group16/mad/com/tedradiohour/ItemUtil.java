package group16.mad.com.tedradiohour;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Vipul.Shukla on 3/8/2017.
 */

/* Assignment # 7.
   ItemUtil.java
   Vipul Shukla, Shanmukh Anand
* */

public class ItemUtil {
    static class ItemParser {
        static ArrayList<Item> parseGameList(InputStream in) throws XmlPullParserException, IOException, ParseException {
            ArrayList<Item> listItem = new ArrayList<>();
            Item item = null;
            boolean mainTitle = true;
            boolean imageTitle = true;
            boolean mainDescription = true;
            boolean iTunesMainImage = true;
            DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("title") && mainTitle == true) {
                            mainTitle = false;
                        }
                        else if (parser.getName().equals("title") && mainTitle == false && imageTitle==true) {
                            imageTitle = false;
                        } else if (parser.getName().equals("description") && mainDescription == true) {
                            mainDescription = false;
                        } else if (parser.getName().equals("itunes:image") && iTunesMainImage == true) {
                            iTunesMainImage = false;
                        } else if (parser.getName().equals("item")) {
                            item = new Item();
                        } else if (parser.getName().equals("title") && mainTitle == false && imageTitle == false) {
                            item.setTitle(parser.nextText().trim());
                        } else if (parser.getName().equals("description") && mainDescription == false) {
                            item.setDescription(parser.nextText().trim());
                        }else if (parser.getName().equals("pubDate")) {
                            item.setPublicationDate(format.parse(parser.nextText().trim()));
                        } else if (parser.getName().equals("itunes:image") && iTunesMainImage == false) {
                            item.setImageURL(parser.getAttributeValue(0));
                        }else if (parser.getName().equals("enclosure")) {
                            item.setmP3FileURL(parser.getAttributeValue(0));
                        }else if (parser.getName().equals("itunes:duration")) {
                            item.setDuration(parser.nextText().trim());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            listItem.add(item);
                            item = null;
                        }
                        break;

                    default:
                        break;
                }
                event = parser.next();
            }
            return listItem;
        }
    }
}
