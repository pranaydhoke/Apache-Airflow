package com.airflow.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class PropertyLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyLoader.class);
    private static PropertyLoader propertyLoader = null;

    private PropertyLoader() {
    }

    public static PropertyLoader getInstance() {
        if (propertyLoader == null) {
            synchronized (PropertyLoader.class) {
                if (propertyLoader == null) {
                    propertyLoader = new PropertyLoader();
                }
            }
        }
        return propertyLoader;
    }

    public Map<String, String> getAirflowCommands(String path) {
        Properties airflowCommands = new Properties();
        InputStream inputStream = PropertyLoader.class.getResourceAsStream(path);
        try {
            airflowCommands.load(inputStream);
        } catch (IOException ex) {
            LOGGER.error("Unable to load airflow commands", ex);
        }
        return (Map) airflowCommands;
    }
}
