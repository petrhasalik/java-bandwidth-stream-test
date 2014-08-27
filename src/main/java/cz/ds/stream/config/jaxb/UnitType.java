package cz.ds.stream.config.jaxb;

import cz.ds.stream.config.util.MeasureUnitType;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 24.8.2014.
 */
@XmlRootElement
public enum UnitType {
    B, kB, MB, GB, TB;

    @XmlTransient
    public MeasureUnitType toUnitType() {
        return MeasureUnitType.valueOf(name());
    }

    ;
}
