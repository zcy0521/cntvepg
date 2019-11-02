package org.xmltv.pojo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class CntvXmltvProgramme {

    @JacksonXmlProperty(isAttribute = true)
    String start;

    @JacksonXmlProperty(isAttribute = true)
    String stop;

    @JacksonXmlProperty(isAttribute = true)
    String channel;

    @JacksonXmlProperty
    CntvXmltvProgrammeTitle title;

    @JacksonXmlProperty
    CntvXmltvProgrammeDesc desc;

}
