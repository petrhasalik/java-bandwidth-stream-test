package cz.ds.stream.config.finder;

import cz.ds.stream.config.Bandwidth;

import java.util.Date;
import java.util.List;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 */
public interface BandwidthFinderStrategy {
    Bandwidth findBandwidth(List<Bandwidth> bandwidths, Date date);
}
