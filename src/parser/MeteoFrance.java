package parser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import dao.Data;
import utils.Pair;



public class MeteoFrance extends Downloader{

    private final String baseUrl = "https://donneespubliques.meteofrance.fr/donnees_libres/Txt/Synop/Archive/synop.";
    private final String extUrl = ".csv.gz";

    Data data;
    public MeteoFrance() throws IOException{
        data = new Data();
    }

    public ArrayList<Pair> get(String datetime, String col, String stationId) throws IOException {
        assert datetime.length() == 4 || datetime.length() == 6 || datetime.length() == 8;
        assert col.equals("t") || col.equals("u") || col.equals("n");

        String csvFile;
        ArrayList<Pair> result = new ArrayList<>();

        if (datetime.length() == 4){    // download year
            result = data.year(datetime, stationId, col);
        }

        if (datetime.length() == 6 || datetime.length() == 8){ // download month


            String month = datetime.substring(0, 6);
            String csvUrl = baseUrl + month + extUrl;

            if ( !(csvFile = download(csvUrl)).equals("") ){
                MeteoFranceCsvParser csv = new MeteoFranceCsvParser(csvFile);
                result = csv.select(datetime, stationId, col);
            }
        }

        return result;

    }
}

