package org.astronomydatacompression.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropertiesLoader implements PropertiesLoaderInterface {

    public static final PropertiesLoader INSTANCE = new PropertiesLoader();

    private final String INTERNAL_PROPERTIES_FILE_NAME = "application.properties";
    private final String EXTERNAL_PROPERTIES_FILE_NAME = "application.properties";
    private Properties properties;
    private PropertiesLoader() {
        try(
                InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        ) {
            properties = new Properties();
            properties.load(inputStreamReader);
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    @Override
    public String getValueByKey(String key) {
        return properties.getProperty(key);
    }

    @Override
    public List<String> getListOfValuesByKey(String key) {
        return Arrays.stream(getValueByKey(key).split(",")).toList();
    }
}
