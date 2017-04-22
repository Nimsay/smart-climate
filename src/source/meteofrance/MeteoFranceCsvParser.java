package source.meteofrance;



import source.CsvParser;
import utils.Pair;

import java.io.IOException;
import java.util.*;

public class MeteoFranceCsvParser extends CsvParser {

    public MeteoFranceCsvParser(String csvFile){
        super(csvFile, ";", "", true);
    }

    final Integer COL_STATION = 0;
    final Integer COL_DATETIME = 1;
    final Integer COL_TEMPERATURE = 7;

    HashMap<String, ArrayList<Integer>> stations;
    HashMap<String, ArrayList<Integer>> days;
    HashMap<String, ArrayList<Integer>> months;
    HashMap<String, ArrayList<Integer>> years;

    private void analyse(){
        this.stations = new HashMap<String, ArrayList<Integer>>(); // stationId: indexes
        this.days = new HashMap<String, ArrayList<Integer>>();     // days: indexes
        this.months = new HashMap<String, ArrayList<Integer>>();   // months: indexes
        this.years = new HashMap<String, ArrayList<Integer>>();    // years: indexes

        String stationId, day, month, year;

        for (int y=0; y < this.getData().size(); y++) {

            stationId = this.cell(COL_STATION, y);
            day = this.cell(COL_DATETIME, y).substring(0, 8);
            month = day.substring(0, 6);
            year = month.substring(0, 4);

            this.stations.putIfAbsent(stationId, new ArrayList<Integer>());
            this.stations.get(stationId).add(y);

            this.days.putIfAbsent(day, new ArrayList<Integer>());
            this.days.get(day).add(y);

            this.months.putIfAbsent(month, new ArrayList<Integer>());
            this.months.get(month).add(y);

            this.years.putIfAbsent(year, new ArrayList<Integer>());
            this.years.get(year).add(y);
        }

    }

   // à supprimer je crois

    private ArrayList<Integer> intersectIndexes(String date, String stationId){
        ArrayList<Integer> dateArray = new ArrayList<>();
        if (date.length() == 4 && this.years.get(date) != null){ dateArray = this.years.get(date); }
        if (date.length() == 6 && this.months.get(date) != null){ dateArray = this.months.get(date); }
        if (date.length() == 8 && this.days.get(date) != null){ dateArray = this.days.get(date); }

        Set<Integer> s1 = new HashSet<Integer>(this.stations.get(stationId));
        Set<Integer> s2 = new HashSet<Integer>(dateArray);

        s1.retainAll(s2);
        ArrayList<Integer> intersectionIndexes = new ArrayList<Integer>();
        intersectionIndexes.addAll(s1);

        return intersectionIndexes;
    }

    public ArrayList<Pair> select(String date, String stationId, String col) throws IOException {
        ArrayList<Pair> data = new ArrayList<>();
        Integer colIndex = this.colIndex(col);

        for (Integer index: this.intersectIndexes(date, stationId)){
            data.add(new Pair(this.cell(COL_DATETIME, index), this.cell(colIndex, index)));
        }
        return data;
    }

    public ArrayList<Integer> selectFromCache(String date) {
        ArrayList<Integer> dateArray = new ArrayList<>();
        if (date.length() == 4 && this.years.get(date) != null ){ dateArray = this.years.get(date); }
        if (date.length() == 6 && this.months.get(date) != null ){ dateArray = this.months.get(date); }
        if (date.length() == 8 && this.days.get(date) != null ){ dateArray = this.days.get(date); }
        return dateArray;
    }

}

