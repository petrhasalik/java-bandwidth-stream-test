package cz.ds.stream.util;

import cz.ds.stream.config.jaxb.UnitType;
import cz.ds.stream.config.util.MeasureUnitType;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 25.8.2014.
 */
public class TestMeasureUnitConverter {

    @Test
    public void testConvertMeasureUnitTypeToUnitType() {
        for (MeasureUnitType measureUnitType : MeasureUnitType.values()) {
            UnitType unitType = UnitType.valueOf(measureUnitType.name());
            Assert.assertNotNull(unitType);
        }
    }

    @Test
    public void testConvertUnitTypeToMeasureUnitType() {
        for (UnitType unitType : UnitType.values()) {
            MeasureUnitType measureUnitType = MeasureUnitType.valueOf(unitType.name());
            Assert.assertNotNull(measureUnitType);
        }
    }
}
