package de.jowisoftware.luja.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.jowisoftware.luja.settings.UpdatePeriod;
import de.jowisoftware.luja.settings.UserSettings;

public class SettingsWindow extends JDialog {
    private static final long serialVersionUID = 4750662089711466437L;
    private final UserSettings settings;

    public SettingsWindow(final JFrame parent, final UserSettings settings) {
        super(parent);
        this.settings = settings;

        initWindow();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setModal(true);

        setResizable(false);
        pack();
        SwingUtils.centerOnScreen(this);
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
                "<html><p><br />If you check the first checkbox, this window will " +
                "never appear<br />automatically again. However, you can get " +
                "this window back by<br /> starting this application with the" +
                " <kbd>-L:select</kbd> command line switch. <br /><br />" +
                "Available command line switches:</p><table>" +
                "<tr><td><kbd>-L:offline</kbd></td><td>don't perform update check</td></tr>" +
                "<tr><td><kbd>-L:update</kbd></td><td>perform update check</td></tr>" +
                "<tr><td><kbd>-L:select</kbd></td><td>show select and settings window</td></tr>" +
                "</table></html>"), BorderLayout.CENTER);
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
        panel.add(createAutostartCheckbox());

        panel.add(new JLabel("Delete old versions on update"));
        panel.add(createCleanupCheckbox());

        panel.add(new JLabel("Update every"));
        panel.add(createUpdateList());
        return panel;
    }

    private JComboBox createUpdateList() {
        final JComboBox list = new JComboBox(UpdatePeriod.values());

        list.setSelectedItem(settings.getUpdatePeriod());

        list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                settings.setUpdatePeriod((UpdatePeriod) list.getSelectedItem());
            }
        });

        return list;
    }

    private JCheckBox createCleanupCheckbox() {
        final JCheckBox box = new JCheckBox("enable auto clean");
        box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                settings.setAutoClean(box.isSelected());
            }
        });
        box.setSelected(settings.isAutoClean());
        return box;
    }

    private JCheckBox createAutostartCheckbox() {
        final JCheckBox box = new JCheckBox("enable autostart");
        box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                settings.setAutostart(box.isSelected());
            }
        });
        box.setSelected(settings.isAutostart());
        return box;
    }
}
