package de.jowisoftware.luja.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.jowisoftware.luja.UserSettings;

public class SettingsWindow extends JDialog {
    private static final long serialVersionUID = 4750662089711466437L;
    private final UserSettings settings;

    public SettingsWindow(final JFrame parent, final UserSettings settings) {
        super(parent);
        this.settings = settings;

        initWindow();

        setResizable(false);
        setSize(400, 240);

        setModal(true);
        centerOnScreen();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initWindow() {
        setTitle("Options");

        setLayout(new BorderLayout());

        final JPanel panel = createControlPanel();
        final JPanel buttons = createButtonPanel();

        add(panel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        final JPanel buttons = new JPanel();
        buttons.setLayout(new BorderLayout());

        buttons.add(new JLabel(
                "<html>If you check the first checkbox, this window will never " +
                 "appear automatically again. However, you can get this window " +
                 "back by starting this application with the <kbd>-L:select</kb" +
                 "d> command line switch.</html>"), BorderLayout.CENTER);
        buttons.add(createCloseButton(), BorderLayout.SOUTH);

        return buttons;
    }

    private JButton createCloseButton() {
        final JButton button = new JButton("Close");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        return button;
    }

    private JPanel createControlPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Automatically start last version"));
        panel.add(new JLabel("x"));

        panel.add(new JLabel("Update every"));
        panel.add(new JLabel("start, day, week, month, never"));

        panel.add(new JLabel("Delete old version on update"));
        panel.add(new JLabel("x"));
        return panel;
    }

    private void centerOnScreen() {
        final Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension windowSize = getSize();

        final int x = (screensize.width - windowSize.width) / 2;
        final int y = (screensize.height - windowSize.height) / 2;

        setLocation(x, y);
    }
}
