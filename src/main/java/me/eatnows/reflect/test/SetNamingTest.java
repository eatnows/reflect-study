package me.eatnows.reflect.test;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class SetNamingTest {

    @Test
    public void 키값을setter로바꾸기() {
        String key = "email";

        String firstKey = "set";
        String upperKey = key.substring(0, 1).toUpperCase();
        String remainKey = key.substring(1);

        assertEquals("setEmail", firstKey + upperKey + remainKey);

    }
}
