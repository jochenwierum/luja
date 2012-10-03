package de.jowisoftware.luja;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public interface Downloader {
    String getCurrentVersion();
    int getSize();
    InputStream startDownload() throws IOException, URISyntaxException;
}