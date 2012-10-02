package de.jowisoftware.luja.ui;

public interface ProgressWindow {
    void create(int size);
    void close();
    void addProgress(int size);
}
