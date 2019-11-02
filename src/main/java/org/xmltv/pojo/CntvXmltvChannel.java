package org.xmltv.pojo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class CntvXmltvChannel {

    @JacksonXmlProperty(isAttribute = true)
    String id;

    @JacksonXmlProperty(localName = "display-name")
    CntvXmltvChannelDisplayName displayName;
}
