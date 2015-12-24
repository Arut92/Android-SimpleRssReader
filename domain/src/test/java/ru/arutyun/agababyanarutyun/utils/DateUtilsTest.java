package ru.arutyun.agababyanarutyun.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DateUtilsTest {

    public static class parseStringToDateTimeMethod {

        @Test
        public void testEmptyString_ReturnsNull() {
            assertNull(ru.arutyun.agababyanarutyun.util.DateUtils.parseStringToDateTime(""));
        }

        @Test
        public void testOneCorrectString_ReturnsNotNull() {
            assertNotNull(ru.arutyun.agababyanarutyun.util.DateUtils.parseStringToDateTime("Sat, 21 Nov 2015 16:56:04 +0300"));
        }

        @Test
        public void testIncorrectString_ReturnsNull() {
            assertNull(ru.arutyun.agababyanarutyun.util.DateUtils.parseStringToDateTime("text"));
        }

    }
}
