package de.jowisoftware.luja.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

public final class SwingUtils {
    private SwingUtils() { /* util class */ }

    public static void centerOnScreen(final Window window) {
        final Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension windowSize = window.getSize();

        final int x = (screensize.width - windowSize.width) / 2;
        final int y = (screensize.height - windowSize.height) / 2;

        window.setLocation(x, y);
    }
}
