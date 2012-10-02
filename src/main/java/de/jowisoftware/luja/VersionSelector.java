package de.jowisoftware.luja;

import de.jowisoftware.luja.ui.VersionSelectWindow;
import de.jowisoftware.luja.versions.Version;

public class VersionSelector {
    private final AppState appState;

    public VersionSelector(final AppState appState) {
        this.appState = appState;
    }

    public Version findStartVersion() {
        scanVersions();

        Version version = null;

        final String commandLineVersion = appState.arguments.getArg("version");
        if (commandLineVersion != null) {
            version = findVersion(commandLineVersion);
        } else if (appState.userSettings.isAutostart()) {
            version = findVersion(appState.userSettings.getLastStartedVersion());
        }

        if (version == null || appState.arguments.hasArg("select")) {
            version = new VersionSelectWindow(appState.manager.getVersions(),
                    version, appState.userSettings, appState.settings.getName()).ask();
        }

        if (version != null) {
            appState.userSettings.setLastStartedVersion(version.getVersion());
        }

        return version;
    }

    private void scanVersions() {
        appState.manager.scan();
        appState.manager.cleanUp();
    }

    private Version findVersion(final String name) {
        for (final Version version : appState.manager.getVersions()) {
            if (version.getVersion().equals(name)) {
                return version;
            }
        }
        return null;
    }
}
