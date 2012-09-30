package de.jowisoftware.luja.versions;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DirectoryManager {
    private final File directory;
    private List<Version> versions;

    public DirectoryManager(final File directory) {
        this.directory = directory;
    }

    private List<Version> findVersions() {
        final List<Version> versions = new LinkedList<Version>();

        final File[] files = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.matches("(?i).*\\.version");
            }
        });

        for (final File file : files) {
            versions.add(new SimpleVersion(file, directory));
        }

        return versions;
    }

    public void cleanUp() {
        if (versions == null) {
            scan();
        }

        final Iterator<Version> it = versions.iterator();
        while(it.hasNext()) {
            final Version version = it.next();
            if (!version.isSane()) {
                version.delete();
                it.remove();
            }
        }
    }

    public LinkedList<Version> getVersions() {
        if (versions == null) {
            scan();
        }

        return new LinkedList<Version>(versions);
    }

    public void scan() {
        versions = findVersions();
    }
}
