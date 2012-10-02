package de.jowisoftware.luja.settings;



public interface UserSettings {
    void save();

    String getLastStartedVersion();
    void setLastStartedVersion(String version);

    boolean isAutostart();
    void setAutostart(boolean value);

    String getLastDownloadedVersion();
    void setLastDownloadedVersion(String version);

    boolean isAutoClean();
    void setAutoClean(boolean value);

    UpdatePeriod getUpdatePeriod();
    void setUpdatePeriod(UpdatePeriod period);

    boolean isFirstStart();
    void setFirstStart(boolean value);

    long getLastUpdateCheck();
    void setLastUpdateCheck(long value);
}
