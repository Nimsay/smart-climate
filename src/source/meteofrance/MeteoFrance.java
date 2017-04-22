package source.meteofrance;



import dao.Data;
import dao.Table;
import source.UILists;
import utils.Pair;

import java.io.IOException;
import java.util.ArrayList;


public class MeteoFrance{

    Data data;
    public UILists uiLists;
    public MeteoFrance() throws IOException {
        MeteoFranceDownloader downloader = new MeteoFranceDownloader();
        MeteoFranceCache cache = new MeteoFranceCache();

        data = new Data(downloader, cache);
        uiLists = new MeteoFranceUILists();
    }


    /**
     *
     * @param datetime la date pour laquelle on veut extraire des donnees
     * @param dataCol la colonne de data qu'on veut extraire
     * @param stationId L'id de la station dont on veut avoir les information
     * @param keysAgg clef de l'aggregation qu'on a choisit
     * @param tempUnit l'unite qu'on veut pour la temperature( Kelvin ou Celsus).
     * @return un array list de pair corresponadant a ce que l'utilisateur a demande
     */
    public ArrayList<Pair> get(String datetime, String dataCol, String stationId, String keysAgg, String tempUnit) throws IOException, InterruptedException {
        assert (datetime.length() == 4 || datetime.length() == 6 || datetime.length() == 8);
        assert (dataCol.equals("t") || dataCol.equals("u") || dataCol.equals("n"));
        assert (keysAgg.equals("mean"));
        assert (tempUnit.equals("kelvin") || tempUnit.equals("celsius"));

        // Les données sont déja en kelvin
        // la conversion d'unité ne concerne que les données "t" aka "température"
        if (tempUnit.equals("kelvin") || !dataCol.equals("t")){
            tempUnit = "";
        }

        String dateTimeCol = "date";
        String stationCol = "numer_sta";

        ArrayList<Pair> result = new ArrayList<>();

        if (datetime.length() == 4){
            result = data.getYear(datetime)
                    .filterRows(stationCol, stationId, false)
                    .selectColsPair(dateTimeCol, dataCol, keysAgg, 6, tempUnit); // aggréger par mois
        }

        if (datetime.length() == 6){
            String year = datetime.substring(0, 4);
            String month = datetime.substring(4, 6);
            result = data.getMonth(year, month)
                    .filterRows(stationCol, stationId, false)
                    .selectColsPair(dateTimeCol, dataCol, keysAgg, 8, tempUnit); // aggréger par jours
        }

        if (datetime.length() == 8){
            String year = datetime.substring(0, 4);
            String month = datetime.substring(4, 6);
            result = data.getMonth(year, month)
                    // Select only one day from the whole month
                    .filterRows(dateTimeCol, datetime, true)
                    .filterRows(stationCol, stationId,  false)
                    .selectColsPair(dateTimeCol, dataCol, keysAgg, -1, tempUnit); // ne pas aggréger
        }

        return result;

    }

    /**
     *
     * @param datetime la date de l'enregistrement qu'on veut extraire.
     * @param cols ArrayList des colonnes qu'on veut avoir dans cet enregistrement.
     * @param stationId l'id de la station à partir de laquelle on veut extraire un enregistrement.
     * @return une table dans laquelle sera contenu les données de l'enregistrement qu'on veut.
     */

    public Table getRaw(String datetime, ArrayList<String> cols, String stationId) throws IOException {

        String dateTimeCol = "date";
        String stationCol = "numer_sta";

        Table result = new Table();

        if (datetime.length() == 4){
            result = data.getYear(datetime)
                    .filterRows(stationCol, stationId, false)
                    .filterCols(cols);
        }

        if (datetime.length() == 6){
            String year = datetime.substring(0, 4);
            String month = datetime.substring(4, 6);
            result = data.getMonth(year, month)
                    .filterRows(stationCol, stationId, false)
                    .filterCols(cols);
        }

        if (datetime.length() == 8){
            String year = datetime.substring(0, 4);
            String month = datetime.substring(4, 6);
            result = data.getMonth(year, month)
                    // Select only one day from the whole month
                    .filterRows(dateTimeCol, datetime, true)
                    .filterRows(stationCol, stationId,  false)
                    .filterCols(cols);
        }

        return result;

    }
}

