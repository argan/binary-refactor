package org.hydra.gui.web;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hydra.util.Log;
import org.hydra.util.Utils;
import org.springframework.web.servlet.View;

public class StreamView implements View {
    protected int bufferSize = 1024;
    protected int contentLength = -1;
    protected String contentType;
    protected InputStream inputStream;
    protected String contentDisposition = "inline";

    public StreamView(String contentType, InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input Stream cannot be null");
        }

        if (contentType == null) {
            throw new IllegalArgumentException("Content Type cannot be null");
        }

        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType
     *            The contentType to set.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return Returns the bufferSize.
     */
    public int getBufferSize() {
        return (bufferSize);
    }

    /**
     * @param bufferSize
     *            The bufferSize to set.
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * @return Returns the contentLength.
     */
    public int getContentLength() {
        return contentLength;
    }

    /**
     * @param contentLength
     *            The contentLength to set.
     */
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * @return Returns the Content-disposition header value.
     */
    public String getContentDisposition() {
        return contentDisposition;
    }

    /**
     * @param contentDisposition
     *            the Content-disposition header value to use.
     */
    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public void render(Map<String, ?> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream oOutput = null;

        try {
            // Find the Response in context
            HttpServletResponse oResponse = response;

            // Set the content type
            oResponse.setContentType(contentType);

            // Set the content length
            if (contentLength >= 0) {
                oResponse.setContentLength(contentLength);
            }

            // Set the content-disposition
            if (contentDisposition != null) {
                oResponse.addHeader("Content-disposition", contentDisposition);
            }

            // Get the outputstream
            oOutput = oResponse.getOutputStream();

            Log.debug("Streaming result [ inputStream ] type=[%s] length=[%d] content-disposition=[%s]", contentType,
                    contentLength, contentDisposition);

            // Copy input to output
            Log.debug("Streaming to output buffer +++ START +++");
            byte[] oBuff = new byte[bufferSize];
            int iSize;
            while (-1 != (iSize = inputStream.read(oBuff))) {
                oOutput.write(oBuff, 0, iSize);
            }
            Log.debug("Streaming to output buffer +++ END +++");

            // Flush
            oOutput.flush();
        } finally {
            Utils.close(inputStream, oOutput);
        }
    }

}