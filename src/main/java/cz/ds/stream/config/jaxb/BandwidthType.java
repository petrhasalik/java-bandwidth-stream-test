package cz.ds.stream.config.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BandwidthType {

    @XmlAttribute(required = true)
    private String poolName;

    private TimeType from = TimeType.FROM;
    private TimeType to = TimeType.TO;
    private SpeedType speed;
    private DayType dayType = DayType.all;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public TimeType getFrom() {
        return from;
    }

    public void setFrom(TimeType from) {
        this.from = from;
    }

    public TimeType getTo() {
        return to;
    }

    public void setTo(TimeType to) {
        this.to = to;
    }

    public SpeedType getSpeed() {
        return speed;
    }

    public void setSpeed(SpeedType speed) {
        this.speed = speed;
    }

    public DayType getDayType() {
        return dayType;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }
}
