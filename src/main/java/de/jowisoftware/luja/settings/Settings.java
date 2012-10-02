package de.jowisoftware.luja.settings;

import java.io.File;

public interface Settings {
    String getName();
    File getRepositoryDir();
    UserSettings getUserSettings();
    String getUri();
}
