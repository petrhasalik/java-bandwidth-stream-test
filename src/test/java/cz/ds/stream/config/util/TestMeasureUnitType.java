package cz.ds.stream.config.util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 23.8.2014.
 */
public class TestMeasureUnitType {

    private static final double DELTA = 1e-15; // double comparison deviation

    // valid multiplies of 2
    private static final double RIGHT_VALUES[] = {1099511627776.0, 1073741824.0, 1048576.0, 1024.0, 1.0,
            0.0009765625, 9.5367431640625e-07, 9.31322574615479e-10, 9.09494701772928e-13};

    @DataProvider(name = "measureUnitTypeProvider")
    public Object[][] getTestConfig() {
        return new Object[][]{
                {MeasureUnitType.B, 4},
                {MeasureUnitType.kB, 3},
                {MeasureUnitType.MB, 2},
                {MeasureUnitType.GB, 1},
                {MeasureUnitType.TB, 0}
        };
    }

    @Test(dataProvider = "measureUnitTypeProvider")
    public void testMeasureUnitTypes(MeasureUnitType unitType, Integer index) {
        int idx = index;

        assertEquals(unitType.toByte(1), RIGHT_VALUES[idx++], DELTA);
        assertEquals(unitType.toKilobyte(1), RIGHT_VALUES[idx++], DELTA);
        assertEquals(unitType.toMegabyte(1), RIGHT_VALUES[idx++], DELTA);
        assertEquals(unitType.toGigabyte(1), RIGHT_VALUES[idx++], DELTA);
        assertEquals(unitType.toTerabyte(1), RIGHT_VALUES[idx++], DELTA);
    }

}
