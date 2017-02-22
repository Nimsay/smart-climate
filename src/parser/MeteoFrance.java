package parser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;



public class MeteoFrance extends Downloader{

    private final String baseUrl = "https://donneespubliques.meteofrance.fr/donnees_libres/Txt/Synop/Archive/synop.";
    private final String extUrl = ".csv.gz";

    public MeteoFrance() throws IOException{

    }

    public ArrayList<String> get(String datetime, String col) throws IOException {
        assert datetime.length() == 4 || datetime.length() == 6 || datetime.length() == 8;
        assert col.equals("temperature") || col.equals("nebulosité") || col.equals("humidité");

        String csvFile;
        ArrayList<String> result = new ArrayList<>();

        if (datetime.length() == 4){    // download year
            for (int m=1; m<=12; m++){
                String mois = String.format("%1$2s", m);
                String csvUrl = baseUrl + datetime + mois + extUrl;
                if (download(csvUrl).equals("")){
                    // mettre en cache & parser
                }
            }
        }

        if (datetime.length() == 6 || datetime.length() == 8){ // download month
            String csvUrl = baseUrl + datetime + extUrl;

            if ( !(csvFile = download(csvUrl)).equals("") ){
                CSV csv = new MeteoFranceCsvParser(csvFile);
                result = csv.col(7);
                System.out.println("csv filename: " + csv.getFilename());
                System.out.println("csv rows: " + String.valueOf(csv.getNRows()));
                System.out.println("csv cols: " + String.valueOf(csv.getNCols()));
                System.out.println("csv header: " + String.valueOf(csv.getHeader()));
                // System.out.println("csv data: " + String.valueOf(csv.getData()));
                System.out.println("csv cell: " + String.valueOf(csv.cell(7, 0)));
                System.out.println("csv temperature col: " + String.valueOf(csv.col(7)));
                System.out.println("csv 1st row: " + String.valueOf(csv.row(0)));
            }
        }

        return result;

    }
}
