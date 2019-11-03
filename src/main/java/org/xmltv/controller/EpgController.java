package org.xmltv.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmltv.service.CntvEpgService;
import org.xmltv.pojo.CntvXmltv;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class EpgController {

    @Autowired
    private CntvEpgService cntvEpgService;

    /**
     * CNTV EPG XMLTV
     * http://localhost:8081/cntv.xml
     */
    @RequestMapping(path = "/cntv.xml")
    public void cntvXmltv(HttpServletResponse response) {
        CntvXmltv xmltv = cntvEpgService.getCntvXmltv();

        XmlMapper mapper = new XmlMapper();
        try {
            String declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            String doctype = "<!DOCTYPE tv SYSTEM \"http://localhost:8081/xmltv.dtd\">";
            String xml = mapper.writeValueAsString(xmltv);

            response.setContentType("application/xml;charset=UTF-8");
            response.getWriter().write(declaration);
            response.getWriter().write(doctype);
            response.getWriter().write(xml);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
