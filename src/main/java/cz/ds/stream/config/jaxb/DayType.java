package cz.ds.stream.config.jaxb;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 */
@XmlRootElement
public enum DayType {
    all, workingDay, weekend;
}
