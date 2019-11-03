package org.xmltv.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CntvXmltvProgramme {

    @JacksonXmlProperty(isAttribute = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss Z", timezone = "Asia/Shanghai")
    Date start;

    @JacksonXmlProperty(isAttribute = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss Z", timezone = "Asia/Shanghai")
    Date stop;

    @JacksonXmlProperty(isAttribute = true)
    String channel;

    @JacksonXmlProperty
    CntvXmltvProgrammeTitle title;

    @JacksonXmlProperty
    CntvXmltvProgrammeDesc desc;

}
