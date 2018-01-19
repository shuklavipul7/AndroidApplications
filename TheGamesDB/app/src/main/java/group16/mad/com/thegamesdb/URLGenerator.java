package group16.mad.com.thegamesdb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vipul.Shukla on 2/16/2017.
 */

public class URLGenerator {
    String baseUrl = "";
    Map<String, String> params = new HashMap<>();

    public URLGenerator(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void addParams(String key, String value) {
        params.put(key, value);
    }

    String makeUrl() {
        String urlToReturn = "";
        String encodedName = "";
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        try {
            urlBuilder.append("?");
            for (String key : params.keySet()) {
                urlBuilder.append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlBuilder.toString();
    }
}
