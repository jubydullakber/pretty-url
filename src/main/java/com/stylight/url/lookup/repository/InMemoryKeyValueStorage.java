package com.stylight.url.lookup.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylight.url.lookup.common.UrlUtility;
import com.stylight.url.lookup.exception.InvalidUrlException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryKeyValueStorage {

    private static Map<String,String> store = new ConcurrentHashMap<>();
    private static Map<String,String> keyValueStore = new ConcurrentHashMap<>();
    private static Map<String,String> invertKeyValueStore = new ConcurrentHashMap<>();

    private static final String FILE_NAME = "sample_url.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    @Value("${app.base.domain}")
    private String basePath;

    @EventListener
    public void init(ApplicationEvent event) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource( File.separator + FILE_NAME);
        store = mapper.readValue(classPathResource.getInputStream(), Map.class);
        lookUpLoad(store);
        invertKeyValueStore = invertMap(keyValueStore);
    }

    public String getPrettyByKey(String key) {
        return keyValueStore.getOrDefault(key, getBestMatch(key, true));
    }

    public String getOriginalByKey(String val) {
        return invertKeyValueStore.getOrDefault(val, getBestMatch(val, false));
    }

    private String getBestMatch(String key , boolean isKey) {
        try {
            URL url = new URL(key);
            List<String> queryParamMap = UrlUtility.urlSplitter(url);
            if (CollectionUtils.isEmpty(queryParamMap)) {
                return key;
            }
            String matchedKey = queryParamMap.stream()
                    .map(q -> UrlUtility.removeUrlQueryParam(key, q))
                    .filter(q -> {
                        String val = getFromStore(q, key, isKey);
                        return (val != key);
                    }).findFirst().orElse(key);
            return getFromStore(matchedKey, key, isKey);

        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new InvalidUrlException();
        }

    }
    public static <V, K> Map<V, K> invertMap(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }


    public String baseDomain(String uri) {
        return basePath + uri;
    }

    private String getFromStore(String q, String defaultVal, boolean isKey) {
        if (isKey) {
            return keyValueStore.getOrDefault(q, defaultVal);
        } else {
            return invertKeyValueStore.getOrDefault(q, defaultVal);
        }
    }

    public void lookUpLoad(Map<String, String> data) {
        data.entrySet().stream().forEach(entry -> loadLookup(entry.getKey(), entry.getValue()));
    }

    public void loadLookup(String key, String value) {
        keyValueStore.put(basePath + key, basePath + value);
    }

    public void loadReverseLookup(String key, String value) {
        invertKeyValueStore.put(basePath + key, basePath + value);
    }
}
