package de.jowisoftware.luja.versions;


public interface Version {
    public String getVersion();
    public void start(String[] args);

    public boolean isSane();
    public void delete();
}
