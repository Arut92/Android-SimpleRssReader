package ru.arutyun.agababyanarutyun.net;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class XmlRss {
    @Element(name = "channel")
    private XmlChannel mChannel;

    public XmlChannel getChannel() {
        return mChannel;
    }

}
