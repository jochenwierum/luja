package de.jowisoftware.luja.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.jowisoftware.luja.UserSettings;
import de.jowisoftware.luja.versions.Version;

public class VersionSelectWindow extends JFrame  {
    private static final long serialVersionUID = -2146020734170600981L;

    private final Object lock = new Object();
    private volatile Version result = null;

    private final LinkedList<Version> versions;
    private final Version selectedVersion;
    private final UserSettings userSettings;

    private final List<ListDataListener> listeners = new LinkedList<ListDataListener>();

    private JButton launchButton;
    private JButton deleteButton;
    private JList versionList;

    public VersionSelectWindow(final LinkedList<Version> versions, final Version selectedVersion,
            final UserSettings userSettings) {
        this.versions = versions;
        this.selectedVersion = selectedVersion;
        this.userSettings = userSettings;
    }

    private void initWindow() {
        setTitle("Select version to start");

        setLayout(new BorderLayout());
        add(createButtonBar(), BorderLayout.SOUTH);
        add(createVersionList(), BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setupWindowListener();
        setResizable(false);
        setSize(320, 240);
    }

    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                synchronized(lock) {
                    lock.notify();
                }
            }
        });
    }

    private JPanel createButtonBar() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        launchButton = createLaunchButton();
        deleteButton = createDeleteButton();

        panel.add(Box.createHorizontalGlue());

        panel.add(createOptionButton());
        panel.add(deleteButton);

        panel.add(Box.createHorizontalStrut(16));

        panel.add(launchButton);
        panel.add(createCancelButton());

        return panel;
    }

    private JButton createOptionButton() {
        final JButton button = new JButton("Options");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new SettingsWindow(VersionSelectWindow.this, userSettings)
                        .setVisible(true);
            }
        });
        return button;
    }

    private JButton createDeleteButton() {
        final JButton button = new JButton("Delete");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (result != null) {
                    result.delete();
                    versions.remove(result);

                    for (final ListDataListener listener : listeners) {
                        listener.contentsChanged(new ListDataEvent(versionList,
                                ListDataEvent.INTERVAL_REMOVED,
                                versionList.getSelectedIndex(),
                                versionList.getSelectedIndex()+1));
                    }

                    updateSelection();
                }
            }
        });

        return button;
    }

    private JButton createCancelButton() {
        final JButton button = new JButton("Cancel");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                result = null;
                dispose();
            }
        });
        return button;
    }

    private JButton createLaunchButton() {
        final JButton button = new JButton("Launch");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });

        button.setEnabled(false);
        return button;
    }

    private JScrollPane createVersionList(){
        versionList = new JList();

        setupListModel(versionList);
        addSelectionSupport(versionList);
        addDoubleClickSupport(versionList);

        if (selectedVersion != null) {
            versionList.setSelectedValue(selectedVersion, true);
            updateSelection();
        }

        return new JScrollPane(versionList);
    }

    private void setupListModel(final JList list) {
        list.setModel(new ListModel() {
            @Override
            public int getSize() {
                return versions.size();
            }

            @Override
            public Object getElementAt(final int index) {
                return versions.get(index);
            }

            @Override
            public void addListDataListener(final ListDataListener l) {
                listeners.add(l);
            }

            @Override
            public void removeListDataListener(final ListDataListener l) {
                listeners.remove(l);
            }
        });
    }

    private void addDoubleClickSupport(final JList list) {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    final Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex());
                    if (r != null && r.contains(e.getPoint())) {
                        dispose();
                    }
                }
            }
        });
    }

    private void addSelectionSupport(final JList list) {
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                updateSelection();
            }
        });
    }

    private void updateSelection() {
        result = (Version) versionList.getSelectedValue();
        launchButton.setEnabled(true);
        deleteButton.setEnabled(!result.getVersion().equals(userSettings.getRecentVersion()));
    }

    private void centerOnScreen() {
        final Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension windowSize = getSize();

        final int x = (screensize.width - windowSize.width) / 2;
        final int y = (screensize.height - windowSize.height) / 2;

        setLocation(x, y);
    }

    public Version ask() {
        try {
            SwingUtilities.invokeAndWait(new Runnable(){
                @Override
                public void run() {
                    initWindow();
                    centerOnScreen();
                    setVisible(true);
                }
            });
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        waitForClose();

        return result;
    }

    private void waitForClose() {
        final Thread t = new Thread() {
            @Override
            public void run() {
                while(isVisible()) {
                    try {
                        synchronized(lock) {
                            lock.wait();
                        }
                    } catch(final InterruptedException e){
                    }
                }
            }
        };
        t.start();

        try {
            t.join();
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
