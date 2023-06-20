package org.astronomydatacompression.properties;

import java.util.List;
import java.util.Properties;

public interface PropertiesLoaderInterface {
    String getValueByKey(Properties type, String key);
    List<String> getListOfValuesByKey(Properties type, String key);

}
