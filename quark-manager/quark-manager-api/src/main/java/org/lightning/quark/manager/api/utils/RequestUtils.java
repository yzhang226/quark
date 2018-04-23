package org.lightning.quark.manager.api.utils;

import com.google.common.collect.Maps;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

public abstract class RequestUtils {

    /**
     * 当前请求
     * @return
     */
    public static HttpServletRequest currentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    /**
     * 请求头信息(s)
     * @param request
     * @return
     */
    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Enumeration<String> names = request.getHeaderNames();
        Map<String, String> headers = Maps.newHashMap();
        while (names.hasMoreElements()) {
            String na = (String) names.nextElement();
            String val = request.getHeader(na);
            headers.put(na, val);
        }
        return headers;
    }
}
