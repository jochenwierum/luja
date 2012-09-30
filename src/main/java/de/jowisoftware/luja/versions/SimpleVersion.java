package de.jowisoftware.luja.versions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import de.jowisoftware.luja.startup.Startup;

public class SimpleVersion implements Version {
    private final File jarFile;
    private final String version;
    private final File propertiesFile;
    private final String downloadDate;

    public SimpleVersion(final File propertiesFile, final File dir) {
        final Properties properties = readProperties(propertiesFile);
        this.version = properties.getProperty("version");
        this.downloadDate = properties.getProperty("downloadDate", "unknown Date");
        this.jarFile = new File(dir, properties.getProperty("filename"));
        this.propertiesFile = propertiesFile;
    }

    private Properties readProperties(final File file) {
        final Properties properties = new Properties();
        try {
            final FileInputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
            inputStream.close();
        } catch(final IOException e) {
            throw new RuntimeException("Unable to read properties file", e);
        }
        return properties;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void start(final String[] args) {
        new Startup(jarFile).callMain(args);
    }

    @Override
    public void delete() {
        jarFile.delete();
        propertiesFile.delete();
    }

    @Override
    public boolean isSane() {
        return jarFile.exists() && jarFile.isFile();
    }

    @Override
    public String toString() {
        return version + " [" + downloadDate + "]";
    }
}
