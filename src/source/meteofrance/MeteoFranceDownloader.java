package source.meteofrance;

import source.Downloader;
import utils.Utils;

import java.io.File;
import java.io.IOException;


public class MeteoFranceDownloader extends Downloader {

    private final String baseUrl = "https://donneespubliques.meteofrance.fr/donnees_libres/Txt/Synop/Archive/synop.";
    private final String extUrl = ".csv.gz";

    String cacheDir;

    public MeteoFranceDownloader() throws IOException {
        super();
        this.cacheDir = (new MeteoFranceCache()).cacheDir;
        File cache = new File(this.cacheDir);
        if (!cache.exists() && !cache.mkdirs() ){
            throw new IOException("Cannot create cache directory");
        }
    }

    @Override
    public boolean downloadMonth(String year, String month) throws IOException {
        String requestUrl = baseUrl + year + month + extUrl;
        String gzFilename = this.cacheDir + "synop."+year+month+".csv.gz";
        String csvFilename = this.cacheDir + "synop."+year+month+".csv";

        if (!super.download(requestUrl, gzFilename)){
            return false;
        }

        if (!Utils.gunzip(gzFilename, csvFilename)){
            return false;
        }

        // delete gz now it's unzipped
        (new File(gzFilename)).delete();

        return true;
    }

}

