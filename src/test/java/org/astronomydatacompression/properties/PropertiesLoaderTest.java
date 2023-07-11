package org.astronomydatacompression.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        String givenValue = PropertiesLoader.INSTANCE.getListOfValuesSepratedByComma(PropertiesType.INTERNAL, "test.property").get(0);
        String correctValue = "testProperty";

        Assertions.assertNotNull(givenValue);
        Assertions.assertEquals(correctValue, givenValue);

        List<String> givenValues = PropertiesLoader.INSTANCE.getListOfValuesSepratedByComma(PropertiesType.INTERNAL, "test.properties");
        Assertions.assertFalse(givenValues.isEmpty());
        Assertions.assertEquals(3, givenValues.size());
        Assertions.assertArrayEquals(new String[]{"testProperty1", "testProperty2", "testProperty3"}, givenValues.toArray());
    }

    @Test
    void getListOfValuesDefinedInArray() {
        List<String> givenValue = PropertiesLoader.INSTANCE.getListOfValuesDefinedInArray(PropertiesType.INTERNAL, "test.arrayProps");
        List<String> correctValue = List.of("testProperty1", "testProperty2", "testProperty3");
        Assertions.assertArrayEquals(correctValue.toArray(), givenValue.toArray());
    }

    @Test
    void getListOfValuesSeparatedByCommaDefinedInArray() {
        List<List<String>> givenValue = PropertiesLoader.INSTANCE.getListOfValuesSeparatedByCommaDefinedInArray(PropertiesType.INTERNAL, "test.arrayCommaProps");
        List<List<String>> correctValue = new ArrayList<>();
        correctValue.add(List.of("testProperty1", "testProperty2", "testProperty3"));
        correctValue.add(new ArrayList<>());
        correctValue.add(List.of("testProperty1", "testProperty2", "testProperty3"));

        Assertions.assertArrayEquals(correctValue.toArray(), givenValue.toArray());
    }
}