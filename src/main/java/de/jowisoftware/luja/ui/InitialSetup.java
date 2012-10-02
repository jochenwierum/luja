package de.jowisoftware.luja.ui;

import javax.swing.JOptionPane;

import de.jowisoftware.luja.AppState;
import de.jowisoftware.luja.settings.UpdatePeriod;

public class InitialSetup {
    private boolean exit;
    private final AppState appState;

    public InitialSetup(final AppState appState) {
        this.appState = appState;
    }

    public boolean requireSetup() {
        return appState.userSettings.isFirstStart();
    }

    public void doSetup() {
        final int result = JOptionPane.showConfirmDialog(null,
                "<html>This is the first time you " +
                "start " + appState.settings.getName() + ".<br /><br />" +
                "This software can automatically update itself daily, or you can <br />" +
                "manually select the version you want to start. <br /><br />"+
                "Do you want to enable auto update (choose yes if unsure)?</html>",
                "Update " + appState.settings.getName(), JOptionPane.YES_NO_CANCEL_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            appState.userSettings.setAutoClean(true);
            appState.userSettings.setAutostart(true);
        } else if (result == JOptionPane.NO_OPTION) {
            appState.userSettings.setAutoClean(false);
            appState.userSettings.setAutostart(false);
        } else {
            exit = true;
            return;
        }

        appState.userSettings.setUpdatePeriod(UpdatePeriod.DAILY);
        appState.userSettings.setFirstStart(false);
        exit = false;
    }

    public boolean exitRequested() {
        return exit;
    }
}
