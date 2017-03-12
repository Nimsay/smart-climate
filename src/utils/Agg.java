package utils;

import java.util.ArrayList;

/**
 * Created by hassina on 11/03/2017.
 */
public class Agg {


    public static Double aggMax (ArrayList<String> resultat) {
        assert (resultat.size() > 0);

        Double max = 0.0;
        Double tmpMax = 0.0;
        for(String r: resultat){
            if (!r.equals("mq")){
                tmpMax =  Double.parseDouble(r);
                if (max < tmpMax){
                    max = tmpMax;
                }
            }
        }

        return max;
    }

    public static Double aggMin (ArrayList<String> resultat) {
        assert (resultat.size() > 0);

        Double tmpMin;
        Double min = Double.POSITIVE_INFINITY;
        for ( String r: resultat){
           if (!r.equals("mq")){
                tmpMin = Double.parseDouble(r);
                if (min > tmpMin){
                    min = tmpMin;
                }
           }
        }

        return min;
    }

    public static Integer aggSize (ArrayList<String> resultat){

        int size_L = 0;
        for (String e: resultat){
            if ( !e.equals("mq") ) size_L += 1;
        }
        return size_L;
    }

    public static Double aggMoy (ArrayList<String> resultat){
        Double som =0.0;

        for ( String e: resultat)
            if ( !e.equals("mq") ){
                som += Double.parseDouble(e);
            }
        return 1.0 *  som / aggSize(resultat);
    }


    public static double aggET (ArrayList<String> resultat) {
        Double moy = aggMoy(resultat);
        Double std = 0.0;

        for (String e: resultat) {
            if (!e.equals("mq")){
                std +=  Math.pow( (moy - Double.parseDouble(e)), 2);
            }
        }
        std /= aggSize(resultat);
        std = Math.sqrt(std);

        return std;
    }
}
