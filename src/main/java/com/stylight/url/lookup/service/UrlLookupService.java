package com.stylight.url.lookup.service;

import com.stylight.url.lookup.enums.UrlType;

import java.util.List;
import java.util.Map;

public interface UrlLookupService {
    Map<String,String> generatesPrettyUrl(List<String> urls);
    Map<String,String> generatesOriginalUrl(List<String> urls);
}
