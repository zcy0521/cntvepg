package org.xmltv.pojo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

@Data
public class CntvXmltvProgrammeDesc {

    @JacksonXmlProperty(isAttribute = true)
    private String lang = "zh";

    @JacksonXmlText
    private String value;

}
