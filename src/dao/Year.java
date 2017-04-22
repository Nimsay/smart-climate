package dao;


import source.Cache;
import source.CsvParser;
import source.Downloader;

import java.io.IOException;
import java.util.Calendar;

public class Year extends Table {

    String year;

    /**
     * fonction qui permet de
     * charger les données du cache, ou de les télécharger
     * si nécessaire en utilisant la classe Month
     * @param year chaine de 4 caractères corespondant à l'année qu'on veut télécharger
     * @param downloader objet qui hérite de Downloader
     * @param cache objet qui hérite de Cache

     */
    public Year(String year, Downloader downloader, Cache cache) throws IOException {
        super();
        this.year = year;
        Calendar cal = Calendar.getInstance();

        // If current year is selected, do not try
        // to downloadMonth further than current month
        Integer maxIndexMonths = 11;  // 0 based
        if (year.equals(String.valueOf(cal.get(Calendar.YEAR)))){
            maxIndexMonths  = cal.get(Calendar.MONTH);
        }

        // concat every month in this.data
        for (int m = 0; m <= maxIndexMonths; m++){
            String month = String.format("%02d", m+1);
            Month mnth = new Month(year, month, downloader, cache);
            this.data.addAll( mnth.getData() );
            if (m==0){ // header should be identical in each month, add it only once
                this.header = mnth.header;
            }
        }
    }


}
