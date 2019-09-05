package com.louis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Louis
 */
public class EnhancedServiceLoader {
    private static Logger LOGGER = Logger.getLogger(EnhancedServiceLoader.class.getName());

    public static void loadFile(String resourceName, String dir, ClassLoader classLoader, List<Class> extensions)
            throws IOException {
        String fileName = dir + resourceName;
        Enumeration<URL> urls;
        if (classLoader != null) {
            urls = classLoader.getResources(fileName);
        } else {
            urls = ClassLoader.getSystemResources(fileName);
        }

        if (urls != null) {
            while (urls.hasMoreElements()) {
                java.net.URL url = urls.nextElement();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.length() > 0) {
                            try {
                                extensions.add(Class.forName(line, true, classLoader));
                            } catch (LinkageError | ClassNotFoundException e) {
                                LogUtil.error(LOGGER, e, String.format("Load %s class fail.", line));
                            }
                        }
                    }
                } catch (Throwable e) {
                    LogUtil.error(LOGGER, e, "Load service error");
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException ioe) {
                        LogUtil.nothing();
                    }
                }
            }
        }
    }
}
