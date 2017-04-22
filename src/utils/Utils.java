package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

public class Utils {

    /**
     * Gunzip prend un fichier gz en entrée et donne en sortie un fichier decompresse
     * @param inputGzFile nom de fichier d'entree en gz format
     * @param outputFile nom de fichier de sortie
     * @return true si ça c'est decompresse correctement. false sinon
     */
    public static boolean gunzip(String inputGzFile, String outputFile){

        byte[] buffer = new byte[1024];

        try{
            GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputGzFile));
            FileOutputStream out = new FileOutputStream(outputFile);

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            gzis.close();
            out.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static Double kelvinToCelsius(Double kelvin){
        return kelvin-273.15;
    }

    public static ArrayList<Pair> kelvinToCelsius(ArrayList<Pair>kelvins){

        ArrayList<Pair> results = new ArrayList<>();
        for (Pair kelvinPair: kelvins){
            results.add(new Pair(
                    kelvinPair.getKey(),
                    String.valueOf(kelvinToCelsius(Double.valueOf(kelvinPair.getValue()))))
            );
        }
        return results;
    }


}