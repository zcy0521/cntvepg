package org.xmltv.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.xmltv.pojo.CntvEpgChannel;
import org.xmltv.pojo.CntvEpgChannelProgram;
import org.xmltv.pojo.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

/**
 * CNTV EPG信息工具类
 */
@Slf4j
@Service
public class CntvEpgService {

    private static final String CNTV_M3U_FILE_NAME = "public/cntv.m3u";

    private static final String CNTV_EPG_API_SCHEME = "http";

    private static final String CNTV_EPG_API_HOST = "api.cntv.cn";

    private static final String CNTV_EPG_API_PATH = "/epg/epginfo";

    private static final Integer SEARCH_DATE_DEFAULT_OFFSET = 8;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 获取代查询的频道名称列表
     */
    List<String> getCntvChannelList() {
        // 解析 cntv.m3u 文件
        String text = "";
        try {
            Resource cntvM3uResource = resourceLoader.getResource(CLASSPATH_URL_PREFIX + CNTV_M3U_FILE_NAME);
            InputStream inputStream = cntvM3uResource.getInputStream();
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            text = FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 正则匹配
        Pattern pattern = Pattern.compile("tvg-id=\"(.*?)\"");
        Matcher matcher = pattern.matcher(text);
        List<String> channelList = Lists.newArrayList();
        while (matcher.find()) {
            // matcher.group(0) tvg-id="cctv"
            // matcher.group(1) cctv
            String channelName = matcher.group(1);
            if (!channelList.contains(channelName) && StringUtils.isNotBlank(channelName)) {
                channelList.add(channelName);
            }
        }

        return channelList;
    }

    /**
     * 查询 CNTV EPG 接口
     */
    private String searchEpgInfoFromApi(String channels, String date) {
        // 查询频道
        if (StringUtils.isEmpty(channels)) {
            return "";
        }

        // 查询日期
        if (StringUtils.isEmpty(date)) {
            date = new DateTime(date).toString("yyyyMMdd");
        }

        // 请求接口
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String responseBody = "";
        try {
            // uri
            URI uri = new URIBuilder().setScheme(CNTV_EPG_API_SCHEME).setHost(CNTV_EPG_API_HOST).setPath(CNTV_EPG_API_PATH)
                    .setParameter("c", channels)
                    .setParameter("d", date)
                    .build();

            // httpget
            HttpGet httpget = new HttpGet(uri);
            log.debug("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler;
            responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            responseBody = httpclient.execute(httpget, responseHandler);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseBody;
    }

    /**
     * 获取 xmltv
     * @param channelIdList 查询频道集合
     * @param offset 查询日期偏移量
     * @return xml
     */
    CntvXmltv getCntvXmltv(List<String> channelIdList, Integer offset) {
        // 查询频道
        String channelValue = Joiner.on(",").skipNulls().join(channelIdList);

        // 查询日期
        offset = null == offset ? SEARCH_DATE_DEFAULT_OFFSET : offset;

        // 请求 cntv 接口
        ObjectMapper mapper = new ObjectMapper();
        CntvXmltv xmltv = new CntvXmltv();
        List<CntvXmltvChannel> channel = Lists.newArrayList();
        List<CntvXmltvProgramme> programme = Lists.newArrayList();
        for (int i = 0; i < offset; i++) {
            DateTime date = new DateTime().plusDays(i);
            String dateValue = date.toString("yyyyMMdd");
            String egpInfo = searchEpgInfoFromApi(channelValue, dateValue);
            if (StringUtils.isEmpty(egpInfo)) {
                continue;
            }

            // 解析json
            try {
                JsonNode epgNode = mapper.readTree(egpInfo);
                for (String channelId : channelIdList) {
                    JsonNode channelNode = epgNode.get(channelId);
                    CntvEpgChannel epgChannel = mapper.treeToValue(channelNode, CntvEpgChannel.class);

                    // channel tag
                    if (i == 0) {
                        CntvXmltvChannel xmltvChannel = new CntvXmltvChannel();
                        xmltvChannel.setId(channelId);
                        CntvXmltvChannelDisplayName displayName = new CntvXmltvChannelDisplayName();
                        displayName.setLang("zh");
                        displayName.setValue(epgChannel.getChannelName());
                        xmltvChannel.setDisplayName(displayName);
                        channel.add(xmltvChannel);
                    }

                    // programme tag
                    List<CntvEpgChannelProgram> epgChannelProgramList = epgChannel.getProgram();
                    if (CollectionUtils.isEmpty(epgChannelProgramList)) {
                        continue;
                    }
                    for (CntvEpgChannelProgram epgChannelProgram : epgChannelProgramList) {
                        CntvXmltvProgramme xmltvProgramme = new CntvXmltvProgramme();
                        xmltvProgramme.setStart(epgChannelProgram.getSt());
                        xmltvProgramme.setStop(epgChannelProgram.getEt());
                        xmltvProgramme.setChannel(channelId);
                        CntvXmltvProgrammeTitle title = new CntvXmltvProgrammeTitle();
                        title.setLang("zh");
                        xmltvProgramme.setTitle(title);
                        title.setValue(epgChannelProgram.getT());
                        CntvXmltvProgrammeDesc desc = new CntvXmltvProgrammeDesc();
                        title.setLang("zh");
                        xmltvProgramme.setDesc(desc);
                        programme.add(xmltvProgramme);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        xmltv.setChannel(channel);
        xmltv.setProgramme(programme);
        return xmltv;
    }

    public CntvXmltv getCntvXmltv() {
        return getCntvXmltv(getCntvChannelList(), SEARCH_DATE_DEFAULT_OFFSET);
    }

}
