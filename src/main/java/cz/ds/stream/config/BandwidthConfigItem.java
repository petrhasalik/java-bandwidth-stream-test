package cz.ds.stream.config;

import cz.ds.stream.config.jaxb.DayType;
import cz.ds.stream.config.util.MeasureUnit;
import cz.ds.stream.config.util.MeasureUnitType;
import org.joda.time.LocalTime;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 * <p/>
 * Class to store one bandwidth record.
 * Class is threadsafe (immutable).
 */
public class BandwidthConfigItem implements Bandwidth {

    /**
     * Unrestricted bandwidth instance. Instance is valid all-day since 0:00:00 till 23:59:59.
     */
    public static final BandwidthConfigItem NON_RESTRICTED_BANDWIDTH;

    static {
        LocalTime from = new LocalTime(0, 0, 0);
        LocalTime to = new LocalTime(23, 59, 59);

        NON_RESTRICTED_BANDWIDTH = new BandwidthConfigItem();
        NON_RESTRICTED_BANDWIDTH.setFrom(from);
        NON_RESTRICTED_BANDWIDTH.setTo(to);
        NON_RESTRICTED_BANDWIDTH.setDayType(DayType.all);
        NON_RESTRICTED_BANDWIDTH.setBandwidth(Long.MAX_VALUE);
        NON_RESTRICTED_BANDWIDTH.setUnit(MeasureUnitType.kB);
    }

    private LocalTime from;
    private LocalTime to;
    private double bandwidth;
    private MeasureUnit unit;
    private DayType dayType;

    /**
     * A factory method to create immutable config item
     *
     * @param from      Time since when bandwidth is valid
     * @param to        Time till when bandwidth is valid
     * @param bandwidth Bandwidth value
     * @param unit      Type of measure unit
     * @param dayType   Type of day
     * @return Created and populated config item instance
     */
    public static BandwidthConfigItem createConfigItem(LocalTime from, LocalTime to, Double bandwidth,
                                                       MeasureUnit unit, DayType dayType) {

        BandwidthConfigItem item = new BandwidthConfigItem();
        item.setFrom(from != null ? from : NON_RESTRICTED_BANDWIDTH.getFrom());
        item.setTo(to != null ? to : NON_RESTRICTED_BANDWIDTH.getTo());
        item.setBandwidth(bandwidth != null ? bandwidth.doubleValue() : NON_RESTRICTED_BANDWIDTH.getBandwidth());
        item.setUnit(unit != null ? unit : MeasureUnitType.kB);
        item.setDayType(dayType != null ? dayType : DayType.all);

        return item;
    }

    @Override
    public LocalTime getFrom() {
        return from;
    }

    private void setFrom(LocalTime from) {
        this.from = from;
    }

    @Override
    public LocalTime getTo() {
        return to;
    }

    private void setTo(LocalTime to) {
        this.to = to;
    }

    @Override
    public double getBandwidth() {
        return bandwidth;
    }

    private void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    @Override
    public MeasureUnit getUnit() {
        return unit;
    }

    private void setUnit(MeasureUnit unit) {
        this.unit = unit;
    }

    @Override
    public DayType getDayType() {
        return dayType;
    }

    private void setDayType(DayType dayType) {
        this.dayType = dayType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BandwidthConfigItem that = (BandwidthConfigItem) o;

        if (Double.compare(that.bandwidth, bandwidth) != 0) return false;
        if (dayType != that.dayType) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        temp = Double.doubleToLongBits(bandwidth);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (dayType != null ? dayType.hashCode() : 0);
        return result;
    }
}
