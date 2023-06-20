package org.astronomydatacompression.properties;

import java.util.List;
import java.util.Properties;

public interface PropertiesLoaderInterface {
    String getValueByKey(PropertiesType type, String key);
    List<String> getListOfValuesByKey(PropertiesType type, String key);

}
