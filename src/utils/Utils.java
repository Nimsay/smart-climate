package utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by hassina on 27/02/2017.
 */

public class Utils {

    public static Double kelvinToCelsius(Double kelvin){
        return kelvin-273.15;
    }

    public static Double celsiusToKelvin (Double celsius){
        return celsius+273.15;
    }

    public static ArrayList<Double> kelvinToCelsius(ArrayList<Double> kelvins){

        ArrayList<Double> results = new ArrayList<>();
        for (Double kelvin: kelvins){
           results.add(kelvinToCelsius(kelvin));
        }
        return results;
    }

    public static ArrayList<Double> celsiusToKelvin(ArrayList<Double> celsius){

        ArrayList<Double> results = new ArrayList<>();
        for (Double celsiu: celsius){
            results.add(celsiusToKelvin(celsiu));
        }
        return results;
    }


}
