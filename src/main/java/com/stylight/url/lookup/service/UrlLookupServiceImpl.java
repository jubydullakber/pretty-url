package com.stylight.url.lookup.service;

import com.stylight.url.lookup.repository.InMemoryKeyValueStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UrlLookupServiceImpl implements UrlLookupService{

    private InMemoryKeyValueStorage inMemoryKeyValueStorage;

    @Autowired
    public UrlLookupServiceImpl(InMemoryKeyValueStorage inMemoryKeyValueStorage) {
        this.inMemoryKeyValueStorage = inMemoryKeyValueStorage;
    }

    @Override
    public Map<String, String> generatesPrettyUrl(List<String> urls) {
        return urls.stream().map(url -> inMemoryKeyValueStorage.baseDomain(url)).collect(Collectors.toMap(Function.identity(),
                key -> inMemoryKeyValueStorage.getPrettyByKey(key)));
    }

    @Override
    public Map<String, String> generatesOriginalUrl(List<String> urls) {
        return urls.stream().map(url -> inMemoryKeyValueStorage.baseDomain(url)).collect(Collectors.toMap(Function.identity(),
                val -> inMemoryKeyValueStorage.getOriginalByKey(val)));
    }
}
