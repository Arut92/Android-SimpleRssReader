package ru.arutyun.agababyanarutyun;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;

public enum RssSource {
    LENTA("lenta", "http://lenta.ru", "rss"),
    GAZETA("газета", "http://www.gazeta.ru", "export/rss/lenta.xml");

    private String mName;
    private String mBaseUrl;
    private String mPath;

    RssSource(@NotNull String name, @NotNull String baseUrl, @NotNull String path) {
        try {
            mName    = new String(name.getBytes("CP1251"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mBaseUrl = baseUrl;
        mPath    = path;
    }

    @NotNull
    public String getName() {
        return mName;
    }

    @NotNull
    public String getBaseUrl() {
        return mBaseUrl;
    }

    @NotNull
    public String getPath() {
        return mPath;
    }

}
