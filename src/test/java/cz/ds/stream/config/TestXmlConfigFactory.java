package cz.ds.stream.config;

import cz.ds.stream.config.jaxb.DayType;
import cz.ds.stream.config.util.MeasureUnitType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 */
public class TestXmlConfigFactory {

    private static final double DELTA = 10e-15;

    private BandwidthConfigFactory configFactory;

    @BeforeClass
    public void setUp() {
        configFactory = new XmlBandwidthConfigFactory("config/bandwidthConfig-full.xml");
        configFactory.loadConfig();
    }

    @Test
    public void testLoadConfigWithUnrestrictedPool() {
        Bandwidth bandwidth = configFactory.getBandwidth("unrestricted pool", new Date());
        Assert.assertNotNull(bandwidth);
        Assert.assertEquals(bandwidth, BandwidthConfigItem.NON_RESTRICTED_BANDWIDTH);
    }

    @Test
    public void testLoadInnerBandwidth() {
        LocalDateTime date = new LocalDateTime(2014, 8, 24, 11, 0);
        Bandwidth bandwidth = configFactory.getBandwidth("http pool", date.toDate());
        Assert.assertNotNull(bandwidth);
        Assert.assertEquals(bandwidth.getDayType(), DayType.all);
        Assert.assertEquals(bandwidth.getBandwidth(), 1500.0, DELTA);
        Assert.assertEquals(bandwidth.getUnit(), MeasureUnitType.kB);
        Assert.assertEquals(bandwidth.getFrom(), new LocalTime(10, 30));
        Assert.assertEquals(bandwidth.getTo(), new LocalTime(11, 30, 59));
    }

    @Test
    public void testLoadBandwidthFromUnknownPool() {
        Bandwidth bandwidth = configFactory.getBandwidth("unknown pool", new Date());
        Assert.assertNotNull(bandwidth);
        Assert.assertEquals(bandwidth, BandwidthConfigItem.NON_RESTRICTED_BANDWIDTH);
    }

}
