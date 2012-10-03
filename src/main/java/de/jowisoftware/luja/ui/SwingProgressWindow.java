package de.jowisoftware.luja.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class SwingProgressWindow implements ProgressWindow {
    private static class Window extends JFrame {
        private static final long serialVersionUID = -4639754987105519840L;

        private final JProgressBar bar = new JProgressBar();

        public Window(final String name) {
            setupContent(name);
            setupWindow(name);
        }

        private void setupContent(final String name) {
            final JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            add(panel);


            final JLabel label = new JLabel("<html><center><p>Updating and Starting<br><b>" +
                    name + "</b></center></p></html>");
            final Dimension dim = label.getPreferredSize();
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(Math.max(dim.width,
                    bar.getPreferredSize().width),
                    (int) (dim.height * 1.3)));
            label.setMinimumSize(label.getPreferredSize());
            panel.add(label);

            bar.setIndeterminate(true);
            panel.add(bar, BorderLayout.SOUTH);
        }

        private void setupWindow(final String name) {
            setTitle("Starting " + name);
            setResizable(false);
            setUndecorated(true);
            pack();
            SwingUtils.centerOnScreen(this);
        }

        public void addProgress(final int size) {
            bar.setValue(bar.getValue() + size);
        }

        public void startProgress(final int size) {
            bar.setIndeterminate(false);
            bar.setMaximum(size);
            bar.setEnabled(true);
        }
    }

    private volatile Window window;

    public SwingProgressWindow(final String name) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    window = new Window(name);
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
    public void startProgress(final int size) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    window.startProgress(size);
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

    public void error(final String message) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(window, message);
                }
            });
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
