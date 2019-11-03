package org.xmltv.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmltv.pojo.CntvEpgChannel;
import org.xmltv.service.CntvEpgService;
import org.xmltv.pojo.CntvXmltv;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/epg")
public class EpgController {

    @Autowired
    private CntvEpgService cntvEpgService;

    /**
     * CNTV EPG 信息
     * http://localhost:8081/epg/cntv/epginfo
     */
    @RequestMapping(path = "/cntv/epginfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CntvEpgChannel> cntvEpgInfo() {
        return cntvEpgService.getCntvEpgInfo();
    }

    /**
     * CNTV EPG XMLTV
     * http://localhost:8081/epg/cntv/xmltv
     */
    @RequestMapping(path = "/cntv/xmltv", produces = MediaType.APPLICATION_XML_VALUE)
    public CntvXmltv cntvXmltv() {
        return cntvEpgService.getCntvXmltv();
    }

}
