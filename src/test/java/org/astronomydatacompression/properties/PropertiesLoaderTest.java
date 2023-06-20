package org.astronomydatacompression.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class PropertiesLoaderTest {
    @Test
    void getValueByKey() {
        String givenValue = PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.INTERNAL, "test.property");
        String correctValue = "testProperty";

        Assertions.assertNotNull(givenValue);
        Assertions.assertEquals(correctValue, givenValue);

        givenValue = PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "test.property");
        correctValue = "testProperty";

        Assertions.assertNotNull(givenValue);
        Assertions.assertEquals(correctValue, givenValue);
    }

    @Test
    void getListOfValuesByKey() {
        String givenValue = PropertiesLoader.INSTANCE.getListOfValuesByKey(PropertiesType.INTERNAL, "test.property").get(0);
        String correctValue = "testProperty";

        Assertions.assertNotNull(givenValue);
        Assertions.assertEquals(correctValue, givenValue);

        List<String> givenValues = PropertiesLoader.INSTANCE.getListOfValuesByKey(PropertiesType.INTERNAL, "test.properties");
        Assertions.assertFalse(givenValues.isEmpty());
        Assertions.assertEquals(3, givenValues.size());
        Assertions.assertArrayEquals(new String[]{"testProperty1", "testProperty2", "testProperty3"}, givenValues.toArray());
    }
}