package ru.arutyun.agababyanarutyun.net;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "enclosure", strict = false)
public class XmlEnclosure {
    @Attribute(name = "url") private String mUrl;

    public String getUrl() {
        return mUrl;
    }

}
