package com.stylight.url.lookup.enums;

public enum UrlType {
    ORIGINAL("ORIGINAL"),
    PRETTY("PRETTY");

    private String urlType;

    UrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getUrlType() {
        return urlType;
    }
}
