package cz.ds.stream;

import cz.ds.stream.config.BandwidthConfigFactory;
import cz.ds.stream.config.XmlBandwidthConfigFactory;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 */
public class TestThrottledInputStream {

    class ThrottledThread extends Thread {

        private ThrottledInputStream throttledInputStream;

        public ThrottledThread(ThrottledStreamStrategy strategy, InputStream stream) {
            setName("ThrottledInputStream[" + stream + "]");
            this.throttledInputStream = new ThrottledInputStream(strategy, stream);
            setDaemon(false);
        }

        public void run() {
            try {
                while (throttledInputStream.read() != -1) {/*do nothing...*/}
                throttledInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testThrottling() throws FileNotFoundException, InterruptedException {
        String fileName = "data/bigFile.png";
        BandwidthConfigFactory configFactory = new XmlBandwidthConfigFactory("config/bandwidthConfig-full.xml");
        configFactory.loadConfig();

        // see comments in config\bandwidthConfig-full.xml
        final int threadCount = 5;

        ThrottledStreamStrategy strategy = new DefaultThrottledStreamStrategy("TestThrottledInputStream pool", configFactory);
        ThrottledThread[] threads = new ThrottledThread[threadCount];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ThrottledThread(strategy, new FileInputStream(fileName));
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

    }
}
