package dao;

/**
 * Created by hassina on 12/03/2017.
 */
import source.Cache;
import source.Downloader;

import java.io.IOException;

public class Data {
    Downloader downloader;
    Cache cache;
    public Data(Downloader downloader, Cache cache){
        this.downloader = downloader;
        this.cache = cache;
    }
    /**
     * fonction qui permet d'instancier un objet de type year
     * @param year chaine de 4 caractères contenant une année valide le met dans l'objet retourné
     * @return un objet de type Year

     */
    public Year getYear(String year) throws IOException {
        assert year.length() == 4;
        return new Year(year, downloader, cache);
    }

    /**
     * fonction qui permet d'instancier un objet de type Month
     * @param year chaine de 4 caractères contenant une année valide et le met dans l'objet retourné
     * @param month chaine de 2 caractères contenant un mois valide et le met dans l'objet retourné
     * @return un objet de type Month

     */

    public Month getMonth(String year, String month) throws IOException {
        assert month.length() == 2;
        return new Month(year, month, downloader, cache);
    }


}
