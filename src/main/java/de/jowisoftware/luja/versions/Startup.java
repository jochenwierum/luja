package de.jowisoftware.luja.versions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Startup {
    private final File jarfile;

    public Startup(final File file) {
        this.jarfile = file;
    }

    public void callMain(final String[] args) {
        final String mainClassName = findMain();
        final ClassLoader loader = createClassLoader();
        final Class<?> mainClass = findMainClass(mainClassName, loader);
        final Method main = findMainMethod(mainClass);

        try {
            main.invoke(null, new Object[] { args });
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException("Exception in main class", e.getCause());
        }
    }

    private String findMain() {
        final InputStream inputStream;
        final JarFile file;
        final Properties properties = new Properties();

        try {
            file = new JarFile(jarfile);
            final JarEntry entry = file.getJarEntry("META-INF/MANIFEST.MF");
            inputStream = file.getInputStream(entry);

            properties.load(inputStream);

            inputStream.close();
            file.close();
        } catch (final IOException e) {
            throw new RuntimeException("Could not access jar file", e);
        }

        return properties.getProperty("Main-Class");
    }

    private Method findMainMethod(final Class<?> mainClass) {
        try {
            return mainClass.getMethod("main", String[].class);
        } catch (final SecurityException e) {
            throw new RuntimeException("Unable to access main method", e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException("Main class does not contain main method", e);
        }
    }

    private Class<?> findMainClass(final String mainClassName,
            final ClassLoader loader) {
        try {
            return loader.loadClass(mainClassName);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("Unable to load main class: " + mainClassName, e);
        }
    }

    private ClassLoader createClassLoader() {
        try {
            return new URLClassLoader(new URL[]{jarfile.toURI().toURL()},
                    getClass().getClassLoader());
        } catch (final MalformedURLException e) {
            throw new RuntimeException("Unable to load jar file", e);
        }
    }
}
