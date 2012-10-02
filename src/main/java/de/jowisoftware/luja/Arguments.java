package de.jowisoftware.luja;

import java.util.LinkedList;
import java.util.List;

public class Arguments {
    private final String[] thisArgs;
    private final String[] mainArgs;

    public Arguments(final String[] args) {
        thisArgs = getArgs(args, false);
        mainArgs = getArgs(args, true);

        for (int i = 0; i < thisArgs.length; ++i) {
            thisArgs[i] = thisArgs[i].substring(thisArgs[i].indexOf(':') + 1);
        }
    }

    private String[] getArgs(final String[] args, final boolean selectMainArgs) {
        final List<String> result = new LinkedList<String>();

        for (final String arg : args) {
            if ((arg.startsWith("-L:") || arg.startsWith("--L:") ||
                    arg.startsWith("/L:")) != selectMainArgs) {
                result.add(arg);
            }
        }

        return result.toArray(new String[result.size()]);
    }

    public boolean hasArg(final String name) {
        for (final String arg : thisArgs) {
            if (arg.toLowerCase().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String getArg(final String name) {
        for (final String arg : thisArgs) {
            if (arg.toLowerCase().startsWith(name+"=")) {
                return arg.substring(name.length() + 1);
            }
        }
        return null;
    }

    public String[] getMainArgs() {
        return mainArgs;
    }
}
