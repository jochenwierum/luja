package de.jowisoftware.luja;

public interface UserSettings {
    void save();

    String getLastVersion();
    void setLastVersion(String version);

    boolean isAutostart();
    void setAutostart(boolean value);

    String getRecentVersion();
    void setRecentVersion(String version);
}
