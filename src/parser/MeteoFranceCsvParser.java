package parser;

import utils.Pair;

import java.util.*;

public class MeteoFranceCsvParser extends CSV {

    final Integer COL_STATION = 0;
    final Integer COL_DATETIME = 1;
    final Integer COL_TEMPERATURE = 7;

    HashMap<String, ArrayList<Integer>> stations;
    HashMap<String, ArrayList<Integer>> days;
    HashMap<String, ArrayList<Integer>> months;
    HashMap<String, ArrayList<Integer>> years;

    public MeteoFranceCsvParser(String csvFile) {

        super(csvFile, ";", "", true, true);

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

    private ArrayList<Integer> intersectIndexes(String date, String stationId){
        ArrayList<Integer> dateArray = new ArrayList<>();
        if (date.length() == 4){ dateArray = this.years.get(date); }
        if (date.length() == 6){ dateArray = this.months.get(date); }
        if (date.length() == 8){ dateArray = this.days.get(date); }

        Set<Integer> s1 = new HashSet<Integer>(this.stations.get(stationId));
        Set<Integer> s2 = new HashSet<Integer>(dateArray);

        s1.retainAll(s2);
        ArrayList<Integer> intersectionIndexes = new ArrayList<Integer>();
        intersectionIndexes.addAll(s1);

        return intersectionIndexes;
    }

    public ArrayList<ArrayList<String>> intersect(String date, String stationId){
        ArrayList<ArrayList<String>> intersection = new ArrayList<ArrayList<String>>();
        for (Integer index: this.intersectIndexes(date, stationId)){
            intersection.add(this.row(index));
        }
        return intersection;
    }

    public ArrayList<Pair> select(String date, String stationId, String col){
        ArrayList<Pair> data = new ArrayList<>();
        Integer colIndex = this.colIndex(col);

        for (Integer index: this.intersectIndexes(date, stationId)){
            data.add(new Pair(this.cell(COL_DATETIME, index), this.cell(colIndex, index)));
        }
        return data;
    }

}
