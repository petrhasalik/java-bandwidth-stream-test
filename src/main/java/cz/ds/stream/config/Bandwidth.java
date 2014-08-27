package cz.ds.stream.config;

import cz.ds.stream.config.jaxb.DayType;
import cz.ds.stream.config.util.MeasureUnit;
import org.joda.time.LocalTime;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 *
 * An object to define a bandwidth
 */
public interface Bandwidth {

    /**
     * Time since
     * @return Time since when bandwidth is valid
     */
    LocalTime getFrom();

    /**
     * Time till
     * @return Time till when bandwidth is valid
     */
    LocalTime getTo();

    /**
     * Bandwidth
     * @return Bandwidth value
     */
    double getBandwidth();

    /**
     * Bandwidth unit (B, kB, MB, GB, TB)
     * @return Unit of bandwidth
     * @see cz.ds.stream.config.util.MeasureUnitType
     * @see cz.ds.stream.config.jaxb.UnitType
     */
    MeasureUnit getUnit();

    /**
     * Type of day when bandwidth is valid (all, workingDay, weekend)
     * @return type of day
     * @see cz.ds.stream.config.jaxb.DayType
     */
    DayType getDayType();
}
