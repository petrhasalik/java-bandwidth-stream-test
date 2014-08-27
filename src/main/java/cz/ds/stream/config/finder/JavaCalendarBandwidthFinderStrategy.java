package cz.ds.stream.config.finder;

import cz.ds.stream.config.Bandwidth;
import cz.ds.stream.config.jaxb.DayType;
import org.joda.time.LocalTime;

import java.util.*;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 */
public class JavaCalendarBandwidthFinderStrategy implements BandwidthFinderStrategy {

    protected List<Bandwidth> getBandwidthByDayType(DayType dayType, List<Bandwidth> bandwidths) {
        List<Bandwidth> result = new ArrayList<Bandwidth>(bandwidths.size());
        for (Bandwidth bandwidth : bandwidths) {
            if (bandwidth.getDayType() == dayType) {
                result.add(bandwidth);
            }
        }

        return result;
    }

    protected Bandwidth findBandwidth(List<Bandwidth> filteredBandwidths, LocalTime time) {
        Collections.sort(filteredBandwidths, new BandwidthComparator());
        for (Bandwidth bandwidth : filteredBandwidths) {
            if (bandwidth.getFrom().isBefore(time) && bandwidth.getTo().isAfter(time)) {
                return bandwidth;
            }
            if (bandwidth.getFrom().isEqual(time)) {
                return bandwidth;
            }
            if (bandwidth.getTo().isEqual(time)) {
                return bandwidth;
            }
        }

        return null;
    }

    @Override
    public Bandwidth findBandwidth(List<Bandwidth> bandwidths, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        boolean isWorkingDay = day != Calendar.SUNDAY && day != Calendar.SATURDAY;
        LocalTime time = LocalTime.fromCalendarFields(calendar);

        List<Bandwidth> filteredBandwidths;
        Bandwidth result = null;
        if (isWorkingDay) {
            filteredBandwidths = getBandwidthByDayType(DayType.workingDay, bandwidths);
            if (!filteredBandwidths.isEmpty()) {
                result = findBandwidth(filteredBandwidths, time);
            }
        } else {
            filteredBandwidths = getBandwidthByDayType(DayType.weekend, bandwidths);
            if (!filteredBandwidths.isEmpty()) {
                result = findBandwidth(filteredBandwidths, time);
            }
        }
        if (result == null) {
            filteredBandwidths = getBandwidthByDayType(DayType.all, bandwidths);
            result = findBandwidth(filteredBandwidths, time);
        }

        return result;
    }

    private static class BandwidthComparator implements Comparator<Bandwidth> {

        @Override
        public int compare(Bandwidth o1, Bandwidth o2) {
            long millisInDay1 = o1.getTo().getMillisOfDay() - o1.getFrom().getMillisOfDay();
            long millisInDay2 = o2.getTo().getMillisOfDay() - o2.getFrom().getMillisOfDay();
            return Long.compare(millisInDay1, millisInDay2);
        }
    }
}
