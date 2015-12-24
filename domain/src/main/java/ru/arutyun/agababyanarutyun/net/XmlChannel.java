package ru.arutyun.agababyanarutyun.net;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


@Root(name = "channel" , strict = false)
public class XmlChannel {

    @Element(name = "title")
    private String mTitle;

    @ElementList(name = "item", required = true, inline = true)
    private List<XmlNews> mItemList;

    public List<XmlNews> getItemList() {
        return mItemList;
    }

    public String getTitle() {
        return mTitle;
    }

}
