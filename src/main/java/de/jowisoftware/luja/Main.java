package de.jowisoftware.luja;

import java.util.LinkedList;
import java.util.List;

import de.jowisoftware.luja.ui.VersionSelectWindow;
import de.jowisoftware.luja.versions.DirectoryManager;
import de.jowisoftware.luja.versions.Version;

public class Main {
    private final Settings settings;
    private final String[] thisArgs;
    private final String[] mainArgs;

    private final DirectoryManager manager;
    private final UserSettings userSettings;

    public Main(final String[] args) {
        thisArgs = getArgs(args, false);
        mainArgs = getArgs(args, true);

        for (int i = 0; i < thisArgs.length; ++i) {
            thisArgs[i] = thisArgs[i].substring(thisArgs[i].indexOf(':') + 1);
        }

        settings = new IncludedSettings();
        userSettings = settings.getUserSettings();

        manager = new DirectoryManager(settings.getRepositoryDir());
    }

    public static void main(final String[] args) {
        new Main(args).run();
    }

    private void run() {
        update();
        initVersions();
        startMain();
    }

    private boolean hasArg(final String name) {
        for (final String arg : thisArgs) {
            if (arg.toLowerCase().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private String getArg(final String name) {
        for (final String arg : thisArgs) {
            if (arg.toLowerCase().startsWith(name+"=")) {
                return arg.substring(name.length() + 1);
            }
        }
        return null;
    }

    private void initVersions() {
        manager.scan();
        manager.cleanUp();
    }

    private void startMain() {
        final Version version = findStartVersion();
        userSettings.save();

        if (version != null) {
            version.start(mainArgs);
        }
    }

    private Version findStartVersion() {
        Version version = null;

        final String commandLineVersion = getArg("version");
        if (commandLineVersion != null) {
            version = findVersion(commandLineVersion);
        } else if (userSettings.isAutostart()) {
            version = findVersion(userSettings.getLastVersion());
        }

        if (version == null || hasArg("select")) {
            version = new VersionSelectWindow(manager.getVersions(),
                    version, userSettings).ask();
        }

        if (version != null) {
            userSettings.setLastVersion(version.getVersion());
        }

        return version;
    }

    private Version findVersion(final String name) {
        for (final Version version : manager.getVersions()) {
            if (version.getVersion().equals(name)) {
                return version;
            }
        }
        return null;
    }

    private void update() {
        // hasArg("offline")
        // TODO: if online and last update check is long enough ago: check for update
        // if update available: download, store and save as last version
        // if user set: delete last version
    }

    private String[] getArgs(final String[] args, final boolean selectMainArgs) {
        final List<String> result = new LinkedList<String>();

        for (final String arg : args) {
            if ((arg.startsWith("-L:") || arg.startsWith("--L:") ||
                    arg.startsWith("/L:")) != selectMainArgs) {
                result.add(arg);
            }
        }

        return result.toArray(new String[result.size()]);
    }
}
