package web.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;

import java.text.SimpleDateFormat;

/**
 * Class storing various constants for web operations.
 */
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

    /**
     * Returns ObjectMapper instance to serialize/deserialize object to/from Json strings
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * Returns headers containing single item "Content-Type": "application/json".
     */
    public static Headers getJsonContentHeaders() {
        var headers = new Headers();
        headers.set(WebConst.CONTENT_TYPE, WebConst.APPLICATION_JSON);
        return headers;
    }
}
