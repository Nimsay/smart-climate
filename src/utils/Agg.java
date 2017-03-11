package utils;

import java.util.ArrayList;

/**
 * Created by hassina on 11/03/2017.
 */
public class Agg {


    public static Double aggMax (ArrayList<String> resultat) {
        assert (resultat.size() > 0);

        double max =0;
        Double tmpMax = 0.0;
        for  (int i=0; i< resultat.size(); i++){
            tmpMax =  Double.parseDouble(resultat.get(i));
            if (max < tmpMax || !resultat.get(i).equals("mq") ){
                max = tmpMax;
            }
        }

        return max;
    }

    public static Double aggMin (ArrayList<String> resultat) {
        assert (resultat.size() > 0);

        double min = Double.parseDouble(resultat.get(0));
        Double tmpMin;
        for ( int i=1; i< resultat.size(); i++){
            tmpMin = Double.parseDouble(resultat.get(i));
            if (min >tmpMin || !resultat.get(i).equals("mq") ){
                min = Double.parseDouble(resultat.get(i));
            }
        }

        return min;
    }

    public static Integer aggSize (ArrayList<String> resultat){

        int size_L = resultat.size();
        for (int i=0; i < size_L; i++){
            if ( !resultat.get(i).equals("mq") ) size_L -= 1;
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
