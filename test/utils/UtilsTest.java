package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by hassina on 27/02/2017.
 */
class UtilsTest {
    @Test
    void kelvinToCelsius() {
        assertTrue(Utils.kelvinToCelsius(2315.0).equals(2041.85));
        assertTrue(Utils.kelvinToCelsius(-23.0).equals(-296.15));
        assertTrue(Utils.kelvinToCelsius(0.0).equals(-273.15));
    }

    @Test
    void celsiusToKelvin() {

    }

    @Test
    void kelvinToCelsius1() {

    }

    @Test
    void celsiusToKelvin1() {

    }

}