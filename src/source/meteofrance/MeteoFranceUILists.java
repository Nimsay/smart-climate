package source.meteofrance;



import source.CsvParser;
import source.UILists;
import utils.Pair;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Contient des methodes pour charger des listes a afficher dans les listes de l'UI.
 */

public class MeteoFranceUILists implements UILists {


    MeteoFranceDownloader downloader;
    MeteoFranceCache cache;

    public MeteoFranceUILists() throws IOException {
        downloader = new MeteoFranceDownloader();
        cache = new MeteoFranceCache();
    }

    @Override
    public ArrayList<Pair> getStationsList() throws IOException {
        // Download stations list if not in cache
        String stationsListFileName = "postesSynop.csv";
        String stationsUrl = "https://donneespubliques.meteofrance.fr/donnees_libres/Txt/Synop/" + stationsListFileName;

        if (!cache.fileExists(stationsListFileName)){
            if (!downloader.download(stationsUrl, cache.cacheDir + stationsListFileName)){
                System.out.println("Cannot download " + stationsUrl + "into " + stationsListFileName);
            }
        }

        CsvParser csvParser = new CsvParser(cache.cacheDir + stationsListFileName, ";", "\"", true);
        ArrayList<Pair> stationsName = new ArrayList<>();
        for (int i = 0; i < csvParser.col(0).size(); i++){
            stationsName.add(new Pair(csvParser.col(0).get(i), csvParser.col(1).get(i) ));
        }
        return stationsName;
    }
}
