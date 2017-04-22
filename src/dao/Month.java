package dao;

import source.Cache;
import source.Downloader;

import java.io.IOException;

public class Month extends Table {

    String year;
    String month;

    /**
     * fonction qui permet de
     * charger les données du cache, ou de les télécharger
     * si nécessaire (Les données de MeteoFrance étant
     * distribuées par mois)
     * @param year chaine de 4 caractères corespondant à l'année qu'on veut télécharger
     * @param month chaine de 2 caractères corespondant au mois qu'on veut télécharger
     * @param downloader objet qui hérite de Downloader
     * @param cache objet qui hérite de Cache

     */
    public Month(String year, String month, Downloader downloader, Cache cache) throws IOException {
        super();
        this.year = year;
        this.month = month;

        if ( !cache.exists(year, month) || !cache.complete(year, month)) {
            downloader.downloadMonth(year, month);
        }

        Table csvTable = cache.load(year, month);
        this.data = csvTable.data;
        this.header = csvTable.header;
    }

}