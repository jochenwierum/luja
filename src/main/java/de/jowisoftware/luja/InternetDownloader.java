package de.jowisoftware.luja;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class InternetDownloader implements Downloader {
    private final URI uri;
    private final Properties properties = new Properties();

    public InternetDownloader(final String url) {
        try {
            this.uri = new URI(url);
        } catch (final URISyntaxException e) {
            throw new RuntimeException("Error while parsing download uri", e);
        }
    }

    @Override
    public String getCurrentVersion() {
        assurePropertiesAreLoaded();
        return properties.getProperty("SCM-Revision");
    }

    @Override
    public InputStream startDownload() throws IOException, URISyntaxException {
        assurePropertiesAreLoaded();
        return new URI(properties.getProperty("Download-URI")).toURL().openStream();
    }

    @Override
    public int getSize() {
        assurePropertiesAreLoaded();
        return Integer.parseInt(properties.getProperty("Size"));
    }

    private void assurePropertiesAreLoaded() {
        if (properties.isEmpty()) {
            try {
                final InputStream stream = uri.toURL().openStream();
                properties.load(stream);
                stream.close();
            } catch(final IOException e) {
                throw new RuntimeException("Could not download update information");
            }
        }
    }
}
