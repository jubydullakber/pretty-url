package com.stylight.url.lookup.common;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class UrlUtility {

    public static List<String> urlSplitter(URL url) throws UnsupportedEncodingException {
        List<String> queryPairs = new LinkedList<>();
        String query = url.getQuery();
        if (StringUtils.hasText(query)) {
            String[] pairs = query.split("&");
            for (int i = pairs.length - 1; i >= 0; i--) {
                queryPairs.add(URLDecoder.decode(pairs[i], StandardCharsets.UTF_8.name()));
            }
        }
        return queryPairs;
    }

    public static String removeUrlQueryParam(String url, String param) {
        return url.substring(0, url.lastIndexOf(param) - 1);
    }
}
