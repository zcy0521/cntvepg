package org.xmltv.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CntvEpgChannelProgram {

    String t;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    LocalDate st;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    LocalDate et;

    String showTime;

    String eventType;

    String eventId;

    Long duration;

}
