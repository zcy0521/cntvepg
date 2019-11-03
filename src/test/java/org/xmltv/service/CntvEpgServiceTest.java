package org.xmltv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xmltv.pojo.CntvEpgChannel;
import org.xmltv.pojo.CntvXmltv;

import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * CNTV EPG 服务测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CntvEpgServiceTest {

    @Autowired
    private CntvEpgService cntvEpgService;

    @Test
    public void testGetCntvEpgInfo() {
        Set<String> channelIdSet = Sets.newHashSet("cctv1", "cctv2");
        List<CntvEpgChannel> epgInfo = cntvEpgService.getCntvEpgInfo(channelIdSet, 2);

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(epgInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(json);
    }

    @Test
    public void testGetCntvXmltv() {
        Set<String> channelIdSet = Sets.newHashSet("cctv1", "cctv2");
        CntvXmltv xmltv = cntvEpgService.getCntvXmltv(channelIdSet, 2);

        XmlMapper mapper = new XmlMapper();
        String xml = "";
        try {
            xml = mapper.writeValueAsString(xmltv);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(xml);
    }

    @Test
    public void testZoneIds() {
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        for (String zoneId : zoneIds) {
            System.out.println(zoneId);
        }
    }

    @Test
    public void testGMT8ZoneId() {
        for (String string : TimeZone.getAvailableIDs(TimeZone.getTimeZone("GMT+08:00").getRawOffset())) {
            System.out.println(string);
        }
    }

}
