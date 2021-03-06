package de.jowisoftware.luja;

import javax.swing.UIManager;

import de.jowisoftware.luja.settings.IncludedSettings;
import de.jowisoftware.luja.settings.UserSettings;
import de.jowisoftware.luja.ui.InitialSetup;
import de.jowisoftware.luja.ui.SwingProgressWindow;
import de.jowisoftware.luja.versions.DirectoryManager;
import de.jowisoftware.luja.versions.Version;

public class Main {
    private final AppState appState;

    public Main(final String[] args) {
        final Arguments arguments = new Arguments(args);
        final IncludedSettings settings = new IncludedSettings();
        final UserSettings userSettings = settings.getUserSettings();
        final DirectoryManager manager = new DirectoryManager(settings.getRepositoryDir());

        appState = new AppState(settings, userSettings, arguments, manager);
    }

    public static void main(final String[] args) {
        new Main(args).run();
    }

    private void run() {
        setupLookAndFeel();
        final SwingProgressWindow progress = new SwingProgressWindow(
                appState.settings.getName());

        firstStartSetup();
        try {
            update(progress);
        } catch(final Exception e) {
            e.printStackTrace();
            progress.error("Exception while updating: " + e.getMessage());
        }

        progress.close();
        startMain();
    }

    private void firstStartSetup() {
        final InitialSetup setup = new InitialSetup(appState);
        if (setup.requireSetup()) {
            setup.doSetup();

            if (setup.exitRequested()) {
                System.exit(0);
            }
        }
    }

    private void startMain() {
        final VersionSelector versionSelector = new VersionSelector(appState);
        final Version version = versionSelector.findStartVersion();
        appState.userSettings.save();

        if (version != null) {
            version.start(appState.arguments.getMainArgs());
        }
    }

    private void update(final SwingProgressWindow progress) {
        final Updater updater = new Updater(appState,
                new InternetDownloader(appState.settings.getUri()),
                progress);
        if (updater.shouldUpdate()) {
            updater.update();
        }
    }

    private void setupLookAndFeel() {
        try {
            final String nativeLF = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(nativeLF);
        } catch (final Exception e) {
        }
    }
}
