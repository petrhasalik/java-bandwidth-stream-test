package cz.ds.stream.config.jaxb;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 */
@XmlRootElement(name = "bandwidthConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class BandwidthConfigType {

    @XmlTransient
    public static final Class[] JAXB_CLASSES = {
            BandwidthConfigType.class,
            BandwidthType.class,
            DayType.class,
            UnitType.class,
            SpeedType.class,
            TimeType.class};

    @XmlElementWrapper(name = "bandwidths")
    @XmlElement(name = "bandwidth")
    private List<BandwidthType> bandwidths;

    public List<BandwidthType> getBandwidths() {
        return bandwidths;
    }

    public void setBandwidths(List<BandwidthType> bandwidths) {
        this.bandwidths = bandwidths;
    }
}
