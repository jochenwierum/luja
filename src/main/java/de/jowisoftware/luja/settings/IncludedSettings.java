package de.jowisoftware.luja.settings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IncludedSettings implements Settings {
    private final Properties properties = new Properties();

    public IncludedSettings() {
        try {
            final InputStream inputStream =
                    getClass().getClassLoader().getResourceAsStream("launcher.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch(final IOException e) {
            throw new RuntimeException("Could not load loader properties", e);
        }
    }

    @Override
    public File getRepositoryDir() {
        final String dirName = properties.getProperty("dirname");

        final String windowsEnv = System.getenv("LOCALAPPDATA");
        File env;

        if (windowsEnv == null) {
            env = new File(System.getProperty("user.home"), "." + dirName);
        } else {
            env = new File(windowsEnv, dirName);
        }

        return env;
    }

    @Override
    public UserSettings getUserSettings() {
        return new SimpleUserSettings(getRepositoryDir());
    }

    @Override
    public String getName() {
        return properties.getProperty("name");
    }

    @Override
    public String getUri() {
        return properties.getProperty("uri");
    }
}
