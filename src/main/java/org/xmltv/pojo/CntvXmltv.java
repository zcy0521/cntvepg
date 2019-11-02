package org.xmltv.pojo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "tv")
public class CntvXmltv {

    @JacksonXmlProperty(isAttribute = true, localName = "source-info-url")
    String sourceInfoUrl = "http://www.schedulesdirect.org/";

    @JacksonXmlProperty(isAttribute = true, localName = "source-info-name")
    String sourceInfoName = "Schedules Direct";

    @JacksonXmlProperty(isAttribute = true, localName = "generator-info-name")
    String generatorInfoName = "XMLTV/$Id: tv_grab_na_dd.in,v 1.70 2008/03/03 15:21:41 rmeden Exp $";

    @JacksonXmlProperty(isAttribute = true, localName = "generator-info-url")
    String generatorInfoUrl = "http://www.xmltv.org/";

    @JacksonXmlElementWrapper(useWrapping = false)
    List<CntvXmltvChannel> channel = Lists.newArrayList();

    @JacksonXmlElementWrapper(useWrapping = false)
    List<CntvXmltvProgramme> programme = Lists.newArrayList();
}
