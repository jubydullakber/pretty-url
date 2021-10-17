package com.stylight.url.lookup.controller;

import com.stylight.url.lookup.service.UrlLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/url/")
public class UrlLookupController {

    private static final Logger logger = LoggerFactory.getLogger(UrlLookupController.class);

    private final UrlLookupService urlLookupService;

    @Autowired
    public UrlLookupController(UrlLookupService urlLookupService) {
        this.urlLookupService = urlLookupService;
    }


    @PostMapping("/pretty")
    public Map<String, String> getPrettyUrls(@RequestBody List<String> originalUrls) {
        return urlLookupService.generatesPrettyUrl(originalUrls);
    }

    @PostMapping("/original")
    public Map<String, String> getOriginalsUrls(@RequestBody List<String> prettyUrls) {
        return urlLookupService.generatesOriginalUrl(prettyUrls);
    }
}
