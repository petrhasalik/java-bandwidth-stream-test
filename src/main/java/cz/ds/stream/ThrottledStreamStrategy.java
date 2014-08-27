package cz.ds.stream;

import java.io.IOException;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 *
 * Strategy object which do a throttling on the pool.
 */
public interface ThrottledStreamStrategy {

    void throttleStream(ThrottledInputStream stream) throws IOException;

    long addBytesRead(ThrottledInputStream throttledInputStream, long l);

    void addStream(ThrottledInputStream stream);

    void releaseStream(ThrottledInputStream throttledInputStream);

    String getPoolName();

    long getTotalBytesRead(ThrottledInputStream throttledInputStream);
}
