package dao;

/**
 * Created by hassina on 12/03/2017.
 */
import parser.MeteoFranceCsvParser;
import utils.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import parser.Downloader;

public class Data {
    private final String baseUrl = "https://donneespubliques.meteofrance.fr/donnees_libres/Txt/Synop/Archive/synop.";
    private final String extUrl = ".csv.gz";
    Downloader downloader;

    public Data() throws IOException {
        downloader = new Downloader();
    }
    public ArrayList<Pair> year(String datetime, String stationId, String col) throws IOException {
        ArrayList<Pair> result = new ArrayList<Pair>();
        for (int m=1; m<=12; m++){
            String mois = String.format("%02d", m);
            String csvUrl = baseUrl + datetime + mois + extUrl;
            String csvFile = downloader.download(csvUrl);
            if (!csvFile.equals("")) {
                MeteoFranceCsvParser csv = new MeteoFranceCsvParser(csvFile);
                result.addAll(csv.select(datetime, stationId, col));
            }
        }

        return result;
    }


}
