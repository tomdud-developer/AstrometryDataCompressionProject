package org.astronomydatacompression.properties;

import java.util.List;

public interface PropertiesLoaderInterface {
    String getValueByKey(PropertiesType type, String key);
    List<String> getListOfValuesSepratedByComma(PropertiesType type, String key);

}
