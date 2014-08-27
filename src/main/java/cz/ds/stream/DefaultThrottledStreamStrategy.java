package cz.ds.stream;

import cz.ds.stream.config.Bandwidth;
import cz.ds.stream.config.BandwidthConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 * <p/>
 * This class provides throttling strategy which is controlled via BandwidthConfigFactory.
 * Class is threadsafe and immutable.
 */
public class DefaultThrottledStreamStrategy implements ThrottledStreamStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DefaultThrottledStreamStrategy.class);
    private static final long SLEEP_DURATION_MS = 50;

    /**
     * Class to encapsulate raw stream statistics.
     */
    private static final class StreamData {
        private final AtomicLong actualBandwidth = new AtomicLong(Integer.MAX_VALUE);
        private final AtomicLong actualBytesRead = new AtomicLong(0L);
        private final long started;
        private long sleptTime;

        private StreamData() {
            started = System.currentTimeMillis();
            sleptTime = 0L;
        }
    }

    /**
     * Watcher thread. It guarantees that all clients have the same bandwidth.
     */
    private static final class ThrottlerThread extends Thread {

        private Map<ThrottledInputStream, StreamData> streamDataMap;
        private DefaultThrottledStreamStrategy streamStrategy;

        private ThrottlerThread(DefaultThrottledStreamStrategy streamStrategy) {
            setName("ThrottlerThread[" + streamStrategy.getPoolName() + "]");
            this.streamStrategy = streamStrategy;
            this.streamDataMap = streamStrategy.streamDataMap;

            setDaemon(true);
        }

        @Override
        public void run() {
            logger.debug("Throttler thread has been started.");
            boolean streamsWereRegistered = false;
            while (true) {
                try {
                    final int count = streamDataMap.size();
                    if (count > 0) {
                        streamsWereRegistered = true;
                        final long maxBandwidthPerStrategy = streamStrategy.getMaxBandwidthPerSecond();
                        final long maxBandwidthPerStream = maxBandwidthPerStrategy / count;

                        // for each stream update your bandwidth and reset data which was read
                        for (StreamData data : streamDataMap.values()) {
                            data.actualBandwidth.set(maxBandwidthPerStream);
                            data.actualBytesRead.set(0L);
                        }

                        logger.debug("maximum bandwidth is set to {} bytes/sec ({} bytes/sec per stream)",
                                maxBandwidthPerStrategy, maxBandwidthPerStream);

                        // sleep cca 1 second
                        TimeUnit.MILLISECONDS.sleep(900L);
                    }

                    // if streams were registered and data map is empty => all data are processed
                    if (streamsWereRegistered && count == 0) {
                        break;
                    }

                    TimeUnit.MILLISECONDS.sleep(100L);
                } catch (InterruptedException e) {
                    break;
                }
            }

            logger.debug("Throttler thread has been finished.");
        }
    }

    private final Map<ThrottledInputStream, StreamData> streamDataMap;
    private final String poolName;
    private final BandwidthConfigFactory configFactory;
    private final AtomicLong totalBytesRead;
    private final ThrottlerThread throttlerThread;

    public DefaultThrottledStreamStrategy(String poolName, BandwidthConfigFactory configFactory) {
        this.poolName = poolName;
        this.configFactory = configFactory;
        this.totalBytesRead = new AtomicLong(0L);
        this.streamDataMap = new ConcurrentHashMap<ThrottledInputStream, StreamData>();
        this.throttlerThread = new ThrottlerThread(this);

        logger.info("Throttled stream strategy with pool name '{}' has been created.", getPoolName());
        this.throttlerThread.start();
    }

    @Override
    public void throttleStream(ThrottledInputStream stream) throws IOException {
        StreamData data = streamDataMap.get(stream);
        if (data.actualBytesRead.get() >= data.actualBandwidth.get()) {
            logger.trace("Applying throttling strategy on the stream {} for {} ms.", stream, SLEEP_DURATION_MS);
            try {
                data.sleptTime += SLEEP_DURATION_MS;
                Thread.sleep(SLEEP_DURATION_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public long getMaxBandwidthPerSecond() {
        Date date = new Date();
        Bandwidth bandwidth = configFactory.getBandwidth(getPoolName(), date);
        long maxBandwidthPerSecond = (long) bandwidth.getUnit().toByte(bandwidth.getBandwidth());
        return maxBandwidthPerSecond;
    }

    @Override
    public long addBytesRead(ThrottledInputStream throttledInputStream, long l) {
        streamDataMap.get(throttledInputStream).actualBytesRead.addAndGet(l);
        return totalBytesRead.addAndGet(l);
    }

    @Override
    public void addStream(ThrottledInputStream stream) {
        StreamData data = new StreamData();
        streamDataMap.put(stream, data);
        logger.info("Stream {} has been registered in the pool '{}'.", stream, getPoolName());
    }

    @Override
    public void releaseStream(ThrottledInputStream stream) {
        StreamData streamData = streamDataMap.remove(stream);
        long totalTime = (System.currentTimeMillis() - streamData.started - streamData.sleptTime)/1000;
        logger.info("Stream {} has been released from the pool '{}'. It was processed in {} seconds, {} seconds it slept",
                stream, getPoolName(), totalTime, streamData.sleptTime);
    }

    @Override
    public String getPoolName() {
        return poolName;
    }

    @Override
    public long getTotalBytesRead(ThrottledInputStream throttledInputStream) {
        return totalBytesRead.get();
    }
}
