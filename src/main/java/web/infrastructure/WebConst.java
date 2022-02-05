package web.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.SimpleDateFormat;

public class WebConst {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String USER_ID_HEADER = "X-USER-ID";

    private static final ObjectMapper OBJECT_MAPPER = initObjectMapper();

    private static ObjectMapper initObjectMapper() {
        var mapper = new ObjectMapper();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        mapper.setDateFormat(format);
        return mapper;
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
