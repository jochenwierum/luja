package de.jowisoftware.luja.ui;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class UIProgressWindow implements ProgressWindow {
    private static class Window extends JFrame {
        private static final long serialVersionUID = -4639754987105519840L;

        private final JProgressBar bar = new JProgressBar();

        public Window(final int size) {
            setTitle("Downloading...");
            setSize(200, 60);
            setResizable(false);

            SwingUtils.centerOnScreen(this);

            setLayout(new BorderLayout());
            add(bar, BorderLayout.CENTER);
            bar.setMaximum(size);
            bar.setValue(0);
        }

        public void addProgress(final int size) {
            bar.setValue(bar.getValue() + size);
        }
    }

    private volatile Window window;

    @Override
    public void close() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    window.dispose();
                }
            });
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(final int size) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    window = new Window(size);
                    window.setVisible(true);
                }
            });
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addProgress(final int size) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    window.addProgress(size);
                }
            });
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
