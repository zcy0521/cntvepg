package org.xmltv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;
import org.xmltv.pojo.CntvEpgChannel;
import org.xmltv.pojo.CntvXmltv;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CNTV EPG 服务测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CntvEpgServiceTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CntvEpgService cntvEpgService;

    @Test
    public void testGetCntvChannelList() {
        List<String> channelList = cntvEpgService.getCntvChannelList();
        for (String channel : channelList) {
            System.out.println(channel);
        }
    }

    @Test
    public void testGetCntvEpgInfo() {
        List<String> channelIdList = Lists.newArrayList("cctv1", "cctv2");
        List<CntvEpgChannel> epgInfo = cntvEpgService.getCntvEpgInfo(channelIdList, 2);

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
        List<String> channelIdList = Lists.newArrayList("cctv1", "cctv2");
        CntvXmltv xmltv = cntvEpgService.getCntvXmltv(channelIdList, 2);

        XmlMapper mapper = new XmlMapper();
        mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        String xml = "";
        try {
            xml = mapper.writeValueAsString(xmltv);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(xml);
    }

    @Test
    public void testReadResource() {
        try {
            Resource cntvM3uResource = resourceLoader.getResource("classpath:cntv.m3u");
            InputStream inputStream = cntvM3uResource.getInputStream();

            // text
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            String text = FileCopyUtils.copyToString(reader);
            System.out.println(text);

            // bytes
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegex() {
        String text = "#EXTM3U\n" +
                "#EXTINF:-1 tvg-id=\"cctv1\" tvg-name=\"CCTV-1\" tvg-chno=\"1\"\n" +
                "rtsp://192.168.26.20/PLTV/88888914/224/3221225795/10000100000000060000000000000658_0.smil\n" +
                "#EXTINF:-1 tvg-id=\"cctv2\" tvg-name=\"CCTV-2\" tvg-chno=\"2\"\n" +
                "rtsp://192.168.26.20/PLTV/88888914/224/3221226140/10000100000000060000000000114913_0.smil";

        Pattern pattern = Pattern.compile("tvg-id=\"(.*?)\"");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
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
