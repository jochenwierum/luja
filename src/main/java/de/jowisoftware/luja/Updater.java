package de.jowisoftware.luja;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import de.jowisoftware.luja.ui.ProgressWindow;
import de.jowisoftware.luja.versions.Version;

public class Updater {
    private final ProgressWindow progressWindow;
    private final Downloader downloader;
    private final AppState appState;

    public Updater(final AppState appState, final Downloader downloader,
            final ProgressWindow progressWindow) {
        this.appState = appState;

        this.downloader = downloader;
        this.progressWindow = progressWindow;
    }

    public boolean shouldUpdate() {
        if (appState.arguments.hasArg("offline")) {
            return true;
        }

        return appState.arguments.hasArg("online") || shouldCheckForUpdate();
    }

    public void update() {
        final String version = downloader.getCurrentVersion();

        if (!appState.userSettings.getLastDownloadedVersion().equals(version)) {
            appState.manager.scan();

            downloadVersion(downloader, version);
            // download, store

            cleanOldVersions();
            selectStartupVersion(version);
            appState.userSettings.setLastDownloadedVersion(version);
        }

        appState.userSettings.setLastUpdateCheck(new Date().getTime());
    }

    private void downloadVersion(final Downloader downloader, final String version) {
        final int size = downloader.getSize();
        try {
            final InputStream inputStream = downloader.startDownload();
            final String fileName = getBaseName(version);
            saveFile(inputStream, fileName + ".jar", size);
            inputStream.close();
            createMetadata(fileName, version);
        } catch (final IOException e) {
            throw new RuntimeException("Could not download update", e);
        } catch (final URISyntaxException e) {
            throw new RuntimeException("Could not download update", e);
        }
    }

    private String getBaseName(final String version) {
        final String prefix = "v" + version.replaceAll("[^A-Z0-9]", "");

        int counter = 0;
        String baseName = prefix;

        while(!isCleanBaseName(baseName)) {
            ++counter;
            baseName = prefix + "-" + counter;
        }

        return baseName;
    }

    private boolean isCleanBaseName(final String fileName) {
        appState.settings.getRepositoryDir().mkdirs();
        final boolean jarExists = new File(appState.settings.getRepositoryDir(),
                fileName + ".jar").isFile();
        final boolean versionExists = new File(appState.settings.getRepositoryDir(),
                fileName + ".version").isFile();
        return !jarExists && !versionExists;
    }

    private void saveFile(final InputStream inputStream, final String fileName,
            final int size) throws IOException {
        final byte buffer[] = new byte[2048];

        progressWindow.startProgress(size);

        final FileOutputStream outputStream = new FileOutputStream(
                new File(appState.settings.getRepositoryDir(), fileName));

        int read;
        while ((read = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
            progressWindow.addProgress(read);
        }

        progressWindow.close();

        outputStream.close();
        inputStream.close();
    }

    private void createMetadata(final String fileName, final String version) throws IOException {
        final String realFileName = fileName + ".version";
        final Properties properties = new Properties();

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        properties.setProperty("version", version);
        properties.setProperty("filename", fileName + ".jar");
        properties.setProperty("downloadDate", format.format(new Date()));

        final OutputStream outputStream = new FileOutputStream(
                new File(appState.settings.getRepositoryDir(), realFileName));
        properties.store(outputStream, "Version information for " + version);
        outputStream.close();
    }

    private void cleanOldVersions() {
        if (appState.userSettings.isAutoClean()) {
            for (final Version oldVersion : appState.manager.getVersions()) {
                oldVersion.delete();
            }
        }
    }

    private void selectStartupVersion(final String version) {
        if (appState.userSettings.isAutostart()) {
            appState.userSettings.setLastStartedVersion(version);
        }
    }

    private boolean shouldCheckForUpdate() {
        final long now = new Date().getTime();
        final long lastUpdate = appState.userSettings.getLastUpdateCheck();

        final long diff = now - lastUpdate;

        return appState.userSettings.getUpdatePeriod().shouldUpate(diff);
    }

}
