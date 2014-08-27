package cz.ds.stream.config.jaxb;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 */
public class TestConfigMarshaller {

    private TimeType addTime(int hour, int minute, int second) {
        TimeType time = new TimeType(hour, minute, second);
        return time;
    }

    private SpeedType addBandwidthSpeed(UnitType unit, double speed) {
        SpeedType bandwidthSpeed = new SpeedType();
        bandwidthSpeed.setUnit(unit);
        bandwidthSpeed.setValue(speed);

        return bandwidthSpeed;
    }

    private BandwidthType addBandwidthType(String poolName, DayType dayType, TimeType from, TimeType to, SpeedType speed) {
        BandwidthType bandwidth = new BandwidthType();
        bandwidth.setPoolName(poolName);
        bandwidth.setDayType(dayType);
        bandwidth.setFrom(from);
        bandwidth.setTo(to);
        bandwidth.setSpeed(speed);

        return bandwidth;
    }

    @Test
    public void testXmlConfigMarshaller() throws JAXBException {
        BandwidthConfigType config = new BandwidthConfigType();
        config.setBandwidths(new ArrayList<BandwidthType>());
        String[] pools = {"default pool", "http pool"};
        for (String pool : pools) {
            config.getBandwidths().add(
                    addBandwidthType(pool, DayType.all, addTime(0, 0, 0), addTime(23, 59, 59),
                    addBandwidthSpeed(UnitType.kB, 1500d)));

            config.getBandwidths().add(
                    addBandwidthType(pool, DayType.all, addTime(8, 30, 0), addTime(15, 30, 59),
                    addBandwidthSpeed(UnitType.kB, 1000d)));

            config.getBandwidths().add(
                    addBandwidthType(pool, DayType.all, addTime(15, 31, 0), addTime(22, 00, 59),
                    addBandwidthSpeed(UnitType.kB, 1200d)));

            config.getBandwidths().add(
                    addBandwidthType(pool, DayType.workingDay, addTime(0, 0, 0), addTime(23, 59, 59),
                    addBandwidthSpeed(UnitType.MB, 2)));
        }

        JAXBContext context = JAXBContext.newInstance(BandwidthConfigType.JAXB_CLASSES);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        m.marshal(config, sw);

        Assert.assertEquals(sw.toString(), "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<bandwidthConfig>\n" +
                "    <bandwidths>\n" +
                "        <bandwidth poolName=\"default pool\">\n" +
                "            <from hour=\"0\" minute=\"0\" second=\"0\"/>\n" +
                "            <to hour=\"23\" minute=\"59\" second=\"59\"/>\n" +
                "            <speed unit=\"kB\" value=\"1500.0\"/>\n" +
                "            <dayType>all</dayType>\n" +
                "        </bandwidth>\n" +
                "        <bandwidth poolName=\"default pool\">\n" +
                "            <from hour=\"8\" minute=\"30\" second=\"0\"/>\n" +
                "            <to hour=\"15\" minute=\"30\" second=\"59\"/>\n" +
                "            <speed unit=\"kB\" value=\"1000.0\"/>\n" +
                "            <dayType>all</dayType>\n" +
                "        </bandwidth>\n" +
                "        <bandwidth poolName=\"default pool\">\n" +
                "            <from hour=\"15\" minute=\"31\" second=\"0\"/>\n" +
                "            <to hour=\"22\" minute=\"0\" second=\"59\"/>\n" +
                "            <speed unit=\"kB\" value=\"1200.0\"/>\n" +
                "            <dayType>all</dayType>\n" +
                "        </bandwidth>\n" +
                "        <bandwidth poolName=\"default pool\">\n" +
                "            <from hour=\"0\" minute=\"0\" second=\"0\"/>\n" +
                "            <to hour=\"23\" minute=\"59\" second=\"59\"/>\n" +
                "            <speed unit=\"MB\" value=\"2.0\"/>\n" +
                "            <dayType>workingDay</dayType>\n" +
                "        </bandwidth>\n" +
                "        <bandwidth poolName=\"http pool\">\n" +
                "            <from hour=\"0\" minute=\"0\" second=\"0\"/>\n" +
                "            <to hour=\"23\" minute=\"59\" second=\"59\"/>\n" +
                "            <speed unit=\"kB\" value=\"1500.0\"/>\n" +
                "            <dayType>all</dayType>\n" +
                "        </bandwidth>\n" +
                "        <bandwidth poolName=\"http pool\">\n" +
                "            <from hour=\"8\" minute=\"30\" second=\"0\"/>\n" +
                "            <to hour=\"15\" minute=\"30\" second=\"59\"/>\n" +
                "            <speed unit=\"kB\" value=\"1000.0\"/>\n" +
                "            <dayType>all</dayType>\n" +
                "        </bandwidth>\n" +
                "        <bandwidth poolName=\"http pool\">\n" +
                "            <from hour=\"15\" minute=\"31\" second=\"0\"/>\n" +
                "            <to hour=\"22\" minute=\"0\" second=\"59\"/>\n" +
                "            <speed unit=\"kB\" value=\"1200.0\"/>\n" +
                "            <dayType>all</dayType>\n" +
                "        </bandwidth>\n" +
                "        <bandwidth poolName=\"http pool\">\n" +
                "            <from hour=\"0\" minute=\"0\" second=\"0\"/>\n" +
                "            <to hour=\"23\" minute=\"59\" second=\"59\"/>\n" +
                "            <speed unit=\"MB\" value=\"2.0\"/>\n" +
                "            <dayType>workingDay</dayType>\n" +
                "        </bandwidth>\n" +
                "    </bandwidths>\n" +
                "</bandwidthConfig>\n");
    }
}
