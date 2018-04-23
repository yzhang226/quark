package org.lightning.quark.manager.api.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 可重复读 流 过滤器
 *
 * Created by cook on 16/9/22.
 */
public class RepeatableBodyRequestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RepeatableBodyRequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (!StringUtils.equalsIgnoreCase(httpRequest.getMethod(), "GET")) {
            logger.debug("filter body request {}", httpRequest.getContentType());
            request = new BodyRequestWrapper(httpRequest);
        }

        HttpStatusServletResponse httpStatusResp = new HttpStatusServletResponse((HttpServletResponse) response);

        chain.doFilter(request, httpStatusResp);

        logger.debug("access method [{}]、url [{}], params [{}], status [{}]",
                httpRequest.getMethod(), httpRequest.getRequestURI(),
                request.getParameterMap(), httpStatusResp.getStatus());
    }

    @Override
    public void destroy() {

    }

}

