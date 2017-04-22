package source.meteofrance;




import dao.Table;
import source.Cache;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/** Implementation de l'interface cache et de ses methodes pour le cas de MeteoFrance.
 */

public class MeteoFranceCache implements Cache {

    public String cacheDir;
    private ArrayList<Integer> mfHours;

    public MeteoFranceCache(){
        this.cacheDir = "./cache/meteofrance/";
        File dir = new File(this.cacheDir);
        dir.mkdirs();

        mfHours = new ArrayList<Integer>(Arrays.asList(0, 3, 6, 9, 12, 15, 18, 21));
    }


    private Integer getNearestMFHour(int hour){
        Integer mfHour = 0; // Meteo France Hour

        for (int h=0; h < mfHours.size()-1; h++){
            if (hour >= mfHours.get(h+1)){ mfHour = mfHours.get(h); }
        }

        return mfHour;
    }

    @Override
    public boolean exists(String year, String month) {
        return (new File(this.cacheDir+"synop."+year+month+".csv")).exists();
    }

    public boolean fileExists(String f){
        return (new File(this.cacheDir+f)).exists();
    }

    @Override
    public boolean complete(String year, String month) {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH); // 0 based
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);

        Calendar selectedCal = Calendar.getInstance();
        selectedCal.set(Integer.parseInt(year), Integer.parseInt(month)-1, 1);
        int expectedLastDay = selectedCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int expectedLastHour = mfHours.get(mfHours.size()-1);

        // Si on est dans le mois courant
        // alors re-ajuster expectedLastDay et expectedLastHour
        //
        if ((Integer.parseInt(year) == currentYear
                && (Integer.parseInt(month)-1) == currentMonth)){
            expectedLastHour = getNearestMFHour(currentHour);
            expectedLastDay = currentDay;
        }

        // Toujours vérifier si le csv est complet
        String fileInCache = this.cacheDir + "synop." + year + month + ".csv";
        MeteoFranceCsvParser csvParser = new MeteoFranceCsvParser(fileInCache);

        String expectedPartialDate = year + month + expectedLastDay + expectedLastHour;
        Table csvTable = csvParser.getTable()
                .filterRows("date", expectedPartialDate, true);

        return (csvTable.size() > 0);
    }


    /**
      Surcharge de la methode load.
     */
    @Override
    public Table load(String year, String month) {
        String fileInCache = this.cacheDir + "synop." + year + month + ".csv";
        MeteoFranceCsvParser csvParser = new MeteoFranceCsvParser(fileInCache);
        Table csvTable = csvParser.getTable();
        csvTable.header = csvParser.getHeader();
        return csvTable;
    }
}
