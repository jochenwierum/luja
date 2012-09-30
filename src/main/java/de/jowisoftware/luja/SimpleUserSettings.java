package de.jowisoftware.luja;

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

    @Override
    public void save() {
        try {
            final FileOutputStream outputStream = new FileOutputStream(settingsFile);
            properties.store(outputStream, "automatically saved user settings");
            outputStream.close();
            System.out.println(isAutoClean());
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
    public String getLastVersion() {
        return properties.getProperty("lastversion");
    }

    @Override
    public void setLastVersion(final String version) {
        properties.setProperty("lastversion", version);
    }

    @Override
    public String getRecentVersion() {
        return properties.getProperty("recentversion");
    }

    @Override
    public void setRecentVersion(final String version) {
        properties.setProperty("recentversion", version);
    }

    @Override
    public boolean isAutoClean() {
        return getBoolean("autoclean", true);
    }

    @Override
    public void setAutoClean(final boolean autoClean) {
        properties.setProperty("autoclean", Boolean.toString(autoClean));
    }
}
