package de.jowisoftware.luja;

import de.jowisoftware.luja.settings.Settings;
import de.jowisoftware.luja.settings.UserSettings;
import de.jowisoftware.luja.versions.DirectoryManager;

public class AppState {
    public final Settings settings;
    public final UserSettings userSettings;
    public final Arguments arguments;
    public final DirectoryManager manager;

    public AppState(final Settings settings, final UserSettings userSettings,
            final Arguments arguments, final DirectoryManager manager) {
        this.settings = settings;
        this.userSettings = userSettings;
        this.arguments = arguments;
        this.manager = manager;
    }
}
