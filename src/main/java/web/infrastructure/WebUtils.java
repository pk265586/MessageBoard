package web.infrastructure;
import com.sun.net.httpserver.Headers;

public class WebUtils {
    public static Headers getJsonContentHeaders() {
        var headers = new Headers();
        headers.set(WebConst.CONTENT_TYPE, WebConst.APPLICATION_JSON);
        return headers;
    }
}
