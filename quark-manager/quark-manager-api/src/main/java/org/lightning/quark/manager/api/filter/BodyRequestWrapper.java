package org.lightning.quark.manager.api.filter;


import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * Created by cook on 16/9/22.
 */
public class BodyRequestWrapper extends HttpServletRequestWrapper {

    private static final Log log = LogFactory.getLog(BodyRequestWrapper.class);

    private final byte[] body;

    public BodyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = parseBodyByte(request);
    }

    /**
     * return readed byte array
     * @return
     */
    public byte[] getBody() {
        return body;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    /**
     * 获取请求Body
     * @param request
     * @return
     */
    private byte[] parseBodyByte(final ServletRequest request) {
        byte[] br = new byte[0];
        InputStream is = null;
        try {
            is = request.getInputStream();
            br = IOUtils.toByteArray(is);
        } catch (Exception e) {
            log.error("parseBodyByte error.", e);
        } finally {
            try {
                if (is != null) is.close();
            } catch (Exception e) {
                log.error("close inputStream error.", e);
            }
        }
        return br;
    }
}
