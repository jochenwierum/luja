package de.jowisoftware.luja.integrationtests.settings;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.jowisoftware.luja.settings.SimpleUserSettings;
import de.jowisoftware.luja.settings.UpdatePeriod;
import de.jowisoftware.luja.settings.UserSettings;

public class SimpleUserSettingsTest {
    private UserSettings settings;

    @Before public void
    setUp() {
        final File file = new File("target/testrep/user.properties");
        if (file.exists()) {
            file.delete();
        }

        settings = createSettings();
    }

    private SimpleUserSettings createSettings() {
        return new SimpleUserSettings(new File("target/testrep"));
    }

    @Test public void
    defaultValuesAreSet() {
        assertEquals(true, settings.isAutoClean());
        assertEquals(true, settings.isAutostart());
        assertEquals(true, settings.isFirstStart());
        assertEquals("", settings.getLastDownloadedVersion());
        assertEquals(null, settings.getLastStartedVersion());
        assertEquals(0, settings.getLastUpdateCheck());
        assertEquals(UpdatePeriod.DAILY, settings.getUpdatePeriod());
    }

    @Test public void
    valuesAreSaved() {
        settings.setAutoClean(false);
        settings.setAutostart(false);
        settings.setFirstStart(false);
        settings.setLastDownloadedVersion("ldv 1");
        settings.setLastStartedVersion("lsv 1");
        settings.setLastUpdateCheck(1234);
        settings.setUpdatePeriod(UpdatePeriod.STARTUP);
        settings.save();

        final SimpleUserSettings loadedSettings = createSettings();
        assertEquals(false, loadedSettings.isAutoClean());
        assertEquals(false, loadedSettings.isAutostart());
        assertEquals(false, loadedSettings.isFirstStart());
        assertEquals("ldv 1", loadedSettings.getLastDownloadedVersion());
        assertEquals("lsv 1", loadedSettings.getLastStartedVersion());
        assertEquals(1234, loadedSettings.getLastUpdateCheck());
        assertEquals(UpdatePeriod.STARTUP, loadedSettings.getUpdatePeriod());
    }
}
