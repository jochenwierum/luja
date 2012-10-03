package de.jowisoftware.luja.settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UpdatePeriodTest {
    private void testPeriod(final UpdatePeriod period, final long lastBadValue) {
        assertFalse(period.shouldUpate(0));
        assertTrue(period.shouldUpate(Integer.MAX_VALUE));

        assertTrue(period.shouldUpate(lastBadValue + 1));
        assertFalse(period.shouldUpate(lastBadValue));
    }

    @Test public void
    dailyPeriodMatchesADay() {
        testPeriod(UpdatePeriod.DAILY, 24 * 60 * 60);
    }

    @Test public void
    monthlyPeriodMatchesAMonth() {
        testPeriod(UpdatePeriod.MONTHLY, 30 * 24 * 60 * 60);
    }

    @Test public void
    weeklyPeriodMatchesAWeek() {
        testPeriod(UpdatePeriod.WEEKLY, 7 * 24 * 60 * 60);
    }

    @Test public void
    startUpPeriodMatchesAlways() {
        assertTrue(UpdatePeriod.STARTUP.shouldUpate(0));
        assertTrue(UpdatePeriod.STARTUP.shouldUpate(Integer.MAX_VALUE));
    }

    @Test public void
    neverPeriodMatchesNever() {
        assertFalse(UpdatePeriod.NEVER.shouldUpate(0));
        assertFalse(UpdatePeriod.NEVER.shouldUpate(Integer.MAX_VALUE));
    }
}
