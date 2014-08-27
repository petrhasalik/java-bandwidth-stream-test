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
public class SpeedType {

    @XmlAttribute
    private UnitType unit = UnitType.kB;

    @XmlAttribute
    private double value;

    public SpeedType(UnitType unit, double value) {
        this.unit = unit;
        this.value = value;
    }

    public SpeedType() {
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
