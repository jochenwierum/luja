package de.jowisoftware.luja.integrationtests.startup;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.jowisoftware.luja.startup.Startup;

public class StartupTest {
    @Test public void
    StartupStartsMainClass() {
        final Startup startup = new Startup(new File("src/test/fixtures/main.jar"));
        try {
            startup.callMain(new String[0]);
            Assert.fail("Fixture class should throw an acception");
        } catch(final RuntimeException e) {
            if (!e.getCause().getMessage().equals("successfull: ")) {
                throw e;
            }
        }
    }

    @Test public void
    StartupStartsMainClassWithParameters() {
        final Startup startup = new Startup(new File("src/test/fixtures/main.jar"));
        try {
            startup.callMain(new String[]{"hello", "world"});
            Assert.fail("Fixture class should throw an acception");
        } catch(final RuntimeException e) {
            if (!e.getCause().getMessage().equals("successfull: helloworld")) {
                throw e;
            }
        }
    }
}
