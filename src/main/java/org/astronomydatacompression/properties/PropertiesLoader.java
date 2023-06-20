package org.astronomydatacompression.properties;

import java.io.FileInputStream;
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
    private final String EXTERNAL_PROPERTIES_FILE_NAME = "external.properties";
    private Properties internalProperties;
    private Properties externalProperties;
    private PropertiesLoader() {
        loadInternalProperties();
        loadExternalProperties();
    }

    private void loadInternalProperties() {
        try(
                InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(INTERNAL_PROPERTIES_FILE_NAME);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        ) {
            internalProperties = new Properties();
            internalProperties.load(inputStreamReader);
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    private void loadExternalProperties() {
        try(
                InputStream inputStream = new FileInputStream(EXTERNAL_PROPERTIES_FILE_NAME);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        ) {
            externalProperties = new Properties();
            externalProperties.load(inputStreamReader);
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    @Override
    public String getValueByKey(PropertiesType type, String key) {
        if(type.equals(PropertiesType.EXTERNAL))
            return externalProperties.getProperty(key);
        else
            return internalProperties.getProperty(key);
    }

    @Override
    public List<String> getListOfValuesByKey(PropertiesType type, String key) {
        return Arrays.stream(getValueByKey(type, key).split(",")).toList();
    }
}
