package de.jowisoftware.luja.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class SimpleUserSettings implements UserSettings {
    private final Properties properties = new Properties();
    private final File settingsFile;

    public SimpleUserSettings(final File repositoryDir) {
        repositoryDir.mkdirs();
        settingsFile = new File(repositoryDir, "user.properties");

        if (settingsFile.exists()) {
            try {
                final InputStream inputStream = new FileInputStream(settingsFile);
                properties.load(inputStream);
                inputStream.close();
            } catch (final Exception e) {
                throw new RuntimeException("Unable to load user.settings from "
                        + settingsFile, e);
            }
        }
    }

    private boolean getBoolean(final String key, final boolean defaultValue) {
        final String value = properties.getProperty(key);

        if (value == null) {
            return defaultValue;
        } else {
            return value.toLowerCase().equals("true");
        }
    }

    private long getLong(final String key, final long defaultValue) {
        final String value = properties.getProperty(key);

        if (value == null) {
            return defaultValue;
        } else {
            return Long.parseLong(value);
        }
    }

    private int getInt(final String key, final long defaultValue) {
        return (int) getLong(key, defaultValue);
    }

    @Override
    public void save() {
        try {
            final FileOutputStream outputStream = new FileOutputStream(settingsFile);
            properties.store(outputStream, "automatically saved user settings");
            outputStream.close();
        } catch (final IOException e) {
            throw new RuntimeException("Unable to save user.settings to " +
                    settingsFile, e);
        }
    }

    @Override
    public boolean isAutostart() {
        return getBoolean("autostart", true);
    }

    @Override
    public void setAutostart(final boolean value) {
        properties.setProperty("autostart", Boolean.toString(value));
    }

    @Override
    public String getLastStartedVersion() {
        return properties.getProperty("lastStartedVersion");
    }

    @Override
    public void setLastStartedVersion(final String version) {
        properties.setProperty("lastStartedVersion", version);
    }

    @Override
    public String getLastDownloadedVersion() {
        return properties.getProperty("lastDownloadedVersion", "");
    }

    @Override
    public void setLastDownloadedVersion(final String version) {
        properties.setProperty("lastDownloadedVersion", version);
    }

    @Override
    public boolean isAutoClean() {
        return getBoolean("autoclean", true);
    }

    @Override
    public void setAutoClean(final boolean autoClean) {
        properties.setProperty("autoclean", Boolean.toString(autoClean));
    }

    @Override
    public UpdatePeriod getUpdatePeriod() {
        return UpdatePeriod.values()[getInt("updatePeriod", 0)];
    }

    @Override
    public void setUpdatePeriod(final UpdatePeriod period) {
        properties.setProperty("updatePeriod", Integer.toString(period.ordinal()));
    }

    @Override
    public boolean isFirstStart() {
        return getBoolean("firstStart", true);
    }

    @Override
    public void setFirstStart(final boolean value) {
        properties.setProperty("firstStart", Boolean.toString(value));
    }

    @Override
    public long getLastUpdateCheck() {
        return getLong("lastCheck", 0);
    }

    @Override
    public void setLastUpdateCheck(final long value) {
        properties.setProperty("lastCheck", Long.toString(value));
    }
}
