package utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
        assertTrue(Utils.celsiusToKelvin(2041.85).equals(2315.0));
        assertTrue(Utils.celsiusToKelvin(-296.15).equals(-23.0));
        assertTrue(Utils.celsiusToKelvin(-273.15).equals(0.0));
    }

    @Test
    void kelvinToCelsius1() {
        ArrayList<Double> a = new ArrayList<>(Arrays.asList(2315.0, -23.0, 0.0));
        ArrayList<Double> b = new ArrayList<>(Arrays.asList(2041.85, -296.15, -273.15));
        assertTrue(Utils.kelvinToCelsius(a).equals(b));
    }

    @Test
    void celsiusToKelvin1() {
        ArrayList<Double> a = new ArrayList<>(Arrays.asList(2315.0, -23.0, 0.0));
        ArrayList<Double> b = new ArrayList<>(Arrays.asList(2041.85, -296.15, -273.15));
        assertTrue(Utils.celsiusToKelvin(b).equals(a));
    }

}