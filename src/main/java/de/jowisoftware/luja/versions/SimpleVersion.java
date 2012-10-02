package de.jowisoftware.luja.versions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class SimpleVersion implements Version {
    private final File jarFile;
    private final String version;
    private final File propertiesFile;
    private final String downloadDate;

    public SimpleVersion(final File propertiesFile, final File dir) {
        this.propertiesFile = propertiesFile;

        final Properties properties = readProperties(propertiesFile);
        this.version = properties.getProperty("version", "unknown version");
        this.downloadDate = properties.getProperty("downloadDate", "unknown Date");

        File file = null;
        try {
            file = new File(dir, properties.getProperty("filename"));
        } catch (final Exception e) {
        }
        this.jarFile = file;
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
        return jarFile != null && jarFile.exists() && jarFile.isFile();
    }

    @Override
    public String toString() {
        return version + " [" + downloadDate + "]";
    }
}
