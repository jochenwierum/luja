package de.jowisoftware.luja;

import java.io.File;

public interface Settings {
    String getName();
    File getRepositoryDir();
    UserSettings getUserSettings();
}
