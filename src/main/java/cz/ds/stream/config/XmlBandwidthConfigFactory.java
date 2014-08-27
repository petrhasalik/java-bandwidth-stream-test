package cz.ds.stream.config;

import cz.ds.stream.config.finder.BandwidthFinderStrategy;
import cz.ds.stream.config.finder.JavaCalendarBandwidthFinderStrategy;
import cz.ds.stream.config.jaxb.*;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 *
 * Bandwidth factory which is based on XML file.
 */
public class XmlBandwidthConfigFactory implements BandwidthConfigFactory {

    private static final Logger logger = LoggerFactory.getLogger(XmlBandwidthConfigFactory.class);

    private Map<String, List<Bandwidth>> configMap;
    private String fileName;

    public XmlBandwidthConfigFactory(String fileName) {
        this.fileName = fileName;
        this.configMap = new HashMap<String, List<Bandwidth>>();
    }

    /**
     * Static factory method to read config defined in a XML file.
     * XML file name is read from environment property cz.ds.stream.config.XmlBandwidthConfigFactory.config
     *
     * @return
     */
    public static final XmlBandwidthConfigFactory getDefaultFactory() {
        final String propertyName = XmlBandwidthConfigFactory.class.getName() + ".config";
        final String fileName = System.getProperty(propertyName);
        logger.info("creating default XmlBandwidthConfigFactory with fileName '{}' (ENV property '{}')",
                fileName, propertyName);

        return new XmlBandwidthConfigFactory(fileName);
    }

    public String getFileName() {
        return fileName;
    }

    private List<Bandwidth> getBandwidthConfig(String poolName) {

        List<Bandwidth> config = configMap.get(poolName);
        if (config == null) {
            config = new LinkedList<Bandwidth>();
            configMap.put(poolName, config);
        }

        return config;
    }

    private LocalTime getLocalTime(String poolName, int hour, int minute, int second) {

        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException(String.format("Invalid hour value: %d. Pool name: '%s', config name: '%s'",
                    hour, poolName, getFileName()));
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException(String.format("Invalid minute value: %d. Pool name: '%s', config name: '%s'",
                    minute, poolName, getFileName()));
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException(String.format("Invalid minute value: %d. Pool name: '%s', config name: '%s'",
                    minute, poolName, getFileName()));
        }

        return new LocalTime(hour, minute, second);
    }

    public void loadFromXmlFile() throws JAXBException {

        logger.info("loading bandwidth configuration from file '{}'", fileName);
        JAXBContext context = JAXBContext.newInstance(BandwidthConfigType.JAXB_CLASSES);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        BandwidthConfigType xmlConfig = (BandwidthConfigType) unmarshaller.unmarshal(new File(fileName));

        configMap.clear();
        for (BandwidthType bandwidthType : xmlConfig.getBandwidths()) {

            List<Bandwidth> config = getBandwidthConfig(bandwidthType.getPoolName());

            BandwidthConfigItem item = BandwidthConfigItem.createConfigItem(
                    // time from...
                    getLocalTime(bandwidthType.getPoolName(),
                            bandwidthType.getFrom().getHour(),
                            bandwidthType.getFrom().getMinute(),
                            bandwidthType.getFrom().getSecond()),
                    // time to...
                    getLocalTime(bandwidthType.getPoolName(),
                            bandwidthType.getTo().getHour(),
                            bandwidthType.getTo().getMinute(),
                            bandwidthType.getTo().getSecond()),
                    // bandwidth
                    bandwidthType.getSpeed() != null ? bandwidthType.getSpeed().getValue() : null,
                    // unit
                    bandwidthType.getSpeed() != null ? bandwidthType.getSpeed().getUnit().toUnitType() : null,
                    // day type
                    bandwidthType.getDayType()
            );

            config.add(item);
        }

        logger.info("bandwidth configuration has been successfully loaded, {} pools have been processed", configMap.size());
    }

    public void saveToXmlFile() throws JAXBException {

        logger.info("saving bandwidth configuration to file '{}'", fileName);
        BandwidthConfigType bandwidthConfig = new BandwidthConfigType();
        for (String poolName : configMap.keySet()) {

            for (Bandwidth config : configMap.get(poolName)) {

                BandwidthType bandwidthType = new BandwidthType();
                bandwidthType.setDayType(config.getDayType());
                bandwidthType.setFrom(new TimeType(config.getFrom().getHourOfDay(),
                        config.getFrom().getMinuteOfHour(),
                        config.getFrom().getSecondOfMinute()));

                bandwidthType.setTo(new TimeType(config.getTo().getHourOfDay(),
                        config.getTo().getMinuteOfHour(),
                        config.getTo().getSecondOfMinute()));

                bandwidthType.setSpeed(new SpeedType(UnitType.valueOf(config.getUnit().getName()),
                        config.getBandwidth()));

                bandwidthType.setPoolName(poolName);

                bandwidthConfig.getBandwidths().add(bandwidthType);
            }
        }

        JAXBContext context = JAXBContext.newInstance(BandwidthConfigType.JAXB_CLASSES);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        m.marshal(bandwidthConfig, new File(fileName));
        logger.info("bandwidth configuration has been successfully saved");
    }

    @Override
    public void saveConfig() {
        try {
            saveToXmlFile();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadConfig() {
        try {
            loadFromXmlFile();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getConfigName() {
        return getFileName();
    }

    @Override
    public boolean isInitialized() {
        return !configMap.isEmpty();
    }

    @Override
    public Bandwidth getBandwidth(String poolName, Date date) {
        if (!isInitialized()) {
            loadConfig();
        }

        Bandwidth bandwidth = null;
        List<Bandwidth> bandwidths = configMap.get(poolName);
        if (bandwidths != null && !bandwidths.isEmpty()) {
            BandwidthFinderStrategy finderStrategy = new JavaCalendarBandwidthFinderStrategy();
            bandwidth = finderStrategy.findBandwidth(bandwidths, date);
        }

        if (bandwidth == null) {
            logger.trace("pool '{}': can't find specific bandwidth on date {}, returns NON_RESTRICTED_BANDWIDTH",
                    poolName, date);
        }

        return bandwidth != null ? bandwidth : BandwidthConfigItem.NON_RESTRICTED_BANDWIDTH;
    }
}
