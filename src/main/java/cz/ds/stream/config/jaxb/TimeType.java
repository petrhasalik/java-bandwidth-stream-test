package cz.ds.stream.config.jaxb;

import javax.xml.bind.annotation.*;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TimeType {

    @XmlTransient
    public static final TimeType FROM = new TimeType(0, 0, 0);
    @XmlTransient
    public static final TimeType TO = new TimeType(23, 59, 59);

    @XmlAttribute
    private int hour;
    @XmlAttribute
    private int minute;
    @XmlAttribute
    private int second;

    public TimeType() {
    }

    public TimeType(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
