package de.jowisoftware.luja.integrationtests.settings;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.jowisoftware.luja.versions.DirectoryManager;
import de.jowisoftware.luja.versions.Version;

public class DirectoryManagerTest {
    private final File dir = new File("target/dirScanTest");
    private DirectoryManager manager;

    private void createVersion(final String name, final String content) throws IOException {
        final String fileName = name + ".jar";
        writeVersionFile(name, dir, fileName);
        if (content != null) {
            writeContentFile(content, dir, fileName);
        }
    }

    private void writeVersionFile(final String name, final File dir,
            final String fileName) throws FileNotFoundException {
        final PrintStream stream = new PrintStream(new File(dir, name + ".version"));
        stream.println("version=" + name);
        stream.println("filename=" + fileName);
        stream.println("downloadDate=2012-10-03 02\\:15\\:02");
        stream.close();
    }

    private void writeContentFile(final String content, final File dir,
            final String fileName) throws FileNotFoundException {
        final PrintStream stream = new PrintStream(new File(dir, fileName));
        stream.println(content);
        stream.close();
    }

    private boolean hasVersion(final LinkedList<Version> versions, final String versionName) {
        for (final Version version : versions) {
            if (version.getVersion().equals(versionName)) {
                return true;
            }
        }
        return false;
    }

    @After public void
    cleanUp() {
        delete(dir);
    }

    @Before public void
    setUp() {
        dir.mkdirs();
        manager = new DirectoryManager(dir);
    }

    private void delete(final File file) {
        if (file.isDirectory()) {
            for (final File dFile : file.listFiles()) {
                delete(dFile);
            }
        }
        file.delete();
    }

    @Test public void
    managerReadsDirectory() throws IOException {
        createVersion("v1", null);
        createVersion("v2", null);
        createVersion("v3", null);

        final LinkedList<Version> versions = manager.getVersions();
        assertThat(versions.size(), is(3));
        assertTrue(hasVersion(versions, "v1"));
        assertTrue(hasVersion(versions, "v2"));
        assertTrue(hasVersion(versions, "v3"));
    }

    @Test public void
    managerRemovesIncompleteVersions() throws IOException {
        createVersion("broken1", null);
        createVersion("broken2", null);
        createVersion("working1", "ok");
        createVersion("working2", "ok");

        manager.cleanUp();
        final LinkedList<Version> versions = manager.getVersions();
        assertThat(versions.size(), is(2));
        assertTrue(hasVersion(versions, "working1"));
        assertTrue(hasVersion(versions, "working2"));
    }
}
