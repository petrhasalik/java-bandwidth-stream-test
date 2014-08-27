package cz.ds.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 *
 * Class to add throttling to an InputStream. Throttling strategy is defined by ThrottledStreamStrategy.
 */
public class ThrottledInputStream extends InputStream {

    private static final Logger logger = LoggerFactory.getLogger(ThrottledInputStream.class);

    private final ThrottledStreamStrategy throttledStreamStrategy;
    private final InputStream rawStream;

    public ThrottledInputStream(ThrottledStreamStrategy throttledStreamStrategy, InputStream rawStream) {
        this.rawStream = rawStream;
        this.throttledStreamStrategy = throttledStreamStrategy;
        logger.info("Throttled stream strategy {} on stream {} has been created.",
                throttledStreamStrategy.getClass().getSimpleName(), rawStream);

        throttledStreamStrategy.addStream(this);
    }

    @Override
    public void close() throws IOException {
        try {
            logger.debug("closing original stream");
            rawStream.close();
        } finally {
            logger.debug("releasing strategy");
            throttledStreamStrategy.releaseStream(this);
        }
    }

    @Override
    public int read() throws IOException {
        throttledStreamStrategy.throttleStream(this);
        int data = rawStream.read();
        if (data != -1) {
            throttledStreamStrategy.addBytesRead(this, 1);
            long bytesRead = throttledStreamStrategy.getTotalBytesRead(this);
            if(bytesRead % 1024 == 0) {
                logger.debug("total {} kB read", (bytesRead/1024));
            }
        } else {
            logger.debug("read(): EOF reached");
        }
        return data;
    }

    @Override
    public int read(byte[] b) throws IOException {
        throttledStreamStrategy.throttleStream(this);
        int readLen = rawStream.read(b);
        if (readLen != -1) {
            throttledStreamStrategy.addBytesRead(this, readLen);
            long bytesRead = throttledStreamStrategy.getTotalBytesRead(this);
            if(bytesRead % 1024 == 0) {
                logger.debug("total {} kB read", (bytesRead/1024));
            }
        } else {
            logger.debug("read(): EOF reached");
        }
        return readLen;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        throttledStreamStrategy.throttleStream(this);
        int readLen = rawStream.read(b, off, len);
        if (readLen != -1) {
            throttledStreamStrategy.addBytesRead(this, readLen);
            long bytesRead = throttledStreamStrategy.getTotalBytesRead(this);
            if(bytesRead % 1024 == 0) {
                logger.debug("total {} kB read", (bytesRead/1024));
            }
        } else {
            logger.debug("read(): EOF reached");
        }
        return readLen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThrottledInputStream that = (ThrottledInputStream) o;

        if (rawStream != null ? !rawStream.equals(that.rawStream) : that.rawStream != null) return false;
        if (!throttledStreamStrategy.equals(that.throttledStreamStrategy))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = throttledStreamStrategy.hashCode();
        result = 31 * result + (rawStream != null ? rawStream.hashCode() : 0);
        return result;
    }
}
