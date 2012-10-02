package de.jowisoftware.luja.settings;

public enum UpdatePeriod {
    DAILY("daily", days(1)),
    WEEKLY("weekly", days(7)),
    MONTHLY("monthly", days(30)),
    STARTUP("every start", 0) {
        @Override
        public boolean shouldUpate(final long diff) {
            return true;
        }
    },
    NEVER("never", 0) {
        @Override
        public boolean shouldUpate(final long diff) {
            return false;
        }
    };

    private static final long DAY_IN_SECONDS = 24 * 60 * 60;

    private static long days(final int days) {
        return days * DAY_IN_SECONDS;
    }

    private final String niceName;
    private long diffSeconds;

    private UpdatePeriod(final String niceName, final long diffSeconds) {
        this.niceName = niceName;
        this.diffSeconds = diffSeconds;
    }

    @Override
    public String toString() {
        return niceName;
    }

    public boolean shouldUpate(final long diff) {
        return diff > diffSeconds;
    }
}
