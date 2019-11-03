package org.xmltv.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.xmltv.jackson.UnixTimestampDeserializer;

import java.util.Date;
import java.util.List;

@Data
public class CntvEpgChannel {

    String isLive;

    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    Date liveSt;

    String channelId;

    String channelName;

    List<CntvEpgChannelProgram> program;
}
