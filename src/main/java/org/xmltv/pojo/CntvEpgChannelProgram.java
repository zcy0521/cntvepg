package org.xmltv.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.xmltv.jackson.UnixTimestampDeserializer;

import java.util.Date;

@Data
public class CntvEpgChannelProgram {

    String t;

    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    Date st;

    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    Date et;

    String showTime;

    String eventType;

    String eventId;

    Long duration;

}
