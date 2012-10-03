package de.jowisoftware.luja.ui;

public interface ProgressWindow {
    void startProgress(int size);
    void close();
    void addProgress(int size);
}
