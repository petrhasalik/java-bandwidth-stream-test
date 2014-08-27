package cz.ds.stream.config.util;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 23.8.2014.
 */
public interface MeasureUnit {
    double toByte(double value);

    double toKilobyte(double value);

    double toMegabyte(double value);

    double toGigabyte(double value);

    double toTerabyte(double value);

    String getName();

    String getUnitName();
}
