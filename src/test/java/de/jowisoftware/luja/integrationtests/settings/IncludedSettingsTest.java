package de.jowisoftware.luja.integrationtests.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.jowisoftware.luja.settings.IncludedSettings;

public class IncludedSettingsTest {
    @Test public void
    includedSettingsAreFound() {
        final IncludedSettings settings = new IncludedSettings();
        assertEquals("Test-Name", settings.getName());
        assertTrue(settings.getRepositoryDir().getName().matches("\\.?Test-Dir"));
        assertTrue(settings.getRepositoryDir().getParentFile().isDirectory());
        assertEquals("http://example.org/Test-URI", settings.getUri());

        assertNotNull(settings.getUserSettings());
    }
}
