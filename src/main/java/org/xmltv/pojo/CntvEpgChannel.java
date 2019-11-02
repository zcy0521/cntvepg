package org.xmltv.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class CntvEpgChannel {

    String isLive;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    LocalDate liveSt;

    String channelId;

    String channelName;

    List<CntvEpgChannelProgram> program;
}
