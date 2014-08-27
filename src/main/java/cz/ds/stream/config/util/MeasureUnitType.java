package cz.ds.stream.config.util;

/**
 * Created by ds (petr.hasalik@direct-services.cz) on 23.8.2014.
 */
public enum MeasureUnitType implements MeasureUnit {

    B("byte") {
        @Override
        public double toByte(double value) {
            return value;
        }

        @Override
        public double toKilobyte(double value) {
            return value / M1;
        }

        @Override
        public double toMegabyte(double value) {
            return value / M2;
        }

        @Override
        public double toGigabyte(double value) {
            return value / M3;
        }

        @Override
        public double toTerabyte(double value) {
            return value / M4;
        }
    },
    kB("kilobyte") {
        @Override
        public double toByte(double value) {
            return value * M1;
        }

        @Override
        public double toKilobyte(double value) {
            return value;
        }

        @Override
        public double toMegabyte(double value) {
            return value / M1;
        }

        @Override
        public double toGigabyte(double value) {
            return value / M2;
        }

        @Override
        public double toTerabyte(double value) {
            return value / M3;
        }
    },
    MB("megabyte") {
        @Override
        public double toByte(double value) {
            return value * M2;
        }

        @Override
        public double toKilobyte(double value) {
            return value * M1;
        }

        @Override
        public double toMegabyte(double value) {
            return value;
        }

        @Override
        public double toGigabyte(double value) {
            return value / M1;
        }

        @Override
        public double toTerabyte(double value) {
            return value / M2;
        }
    },
    GB("gigabyte") {
        @Override
        public double toByte(double value) {
            return value * M3;
        }

        @Override
        public double toKilobyte(double value) {
            return value * M2;
        }

        @Override
        public double toMegabyte(double value) {
            return value * M1;
        }

        @Override
        public double toGigabyte(double value) {
            return value;
        }

        @Override
        public double toTerabyte(double value) {
            return value / M1;
        }
    },
    TB("terabyte") {
        @Override
        public double toByte(double value) {
            return value * M4;
        }

        @Override
        public double toKilobyte(double value) {
            return value * M3;
        }

        @Override
        public double toMegabyte(double value) {
            return value * M2;
        }

        @Override
        public double toGigabyte(double value) {
            return value * M1;
        }

        @Override
        public double toTerabyte(double value) {
            return value;
        }
    };

    // multiplying arguments
    private static final double M1 = 1024.0;
    private static final double M2 = 1024.0 * 1024.0;
    private static final double M3 = 1024.0 * 1024.0 * 1024.0;
    private static final double M4 = 1024.0 * 1024.0 * 1024.0 * 1024.0;
    private String name;

    MeasureUnitType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUnitName() {
        return name();
    }
}
