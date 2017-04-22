package dao;


import source.Cache;
import source.Downloader;
import utils.Pair;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {

    protected Cache cache;
    protected Downloader downloader;

    public ArrayList<Row> data;
    public ArrayList<String> header;

    public Table(){
        this.data = new ArrayList<>();
        this.header = new ArrayList<>();
    }

    public void add(ArrayList<String> row){
        this.data.add(new Row(row));
    }
    public ArrayList<Row> getData(){
        return this.data;
    }

    /**
     * Permet de retourner un ArrayList de Pair (clé, valeur) depuis une Table.
     * @param keysCol le nom de la colonne qui sera retenu pour les clés
     * @param valuesCol le nom de la colonne qui contient les valeurs
     * @param keysAgg type d'agregation à utiliser (mean, etc.)
     */
    public ArrayList<Pair> selectColsPair(String keysCol, String valuesCol,
                                          String keysAgg, Integer keysAggLength,
                                          String tempUnit){
        assert keysAgg.equals("mean") || keysAgg.equals("none");
        Integer keysColIndex = this.colIndex(keysCol);
        Integer valuesColIndex = this.colIndex(valuesCol);

        ArrayList<Pair> pairs = new ArrayList<>();

        // Extraire les colonnes clés, valeurs
        for (Row row: this.data){
            pairs.add(new Pair(row.get(keysColIndex), row.get(valuesColIndex)));
        }

        // Aggréger
        if (keysAggLength > 0) {

            HashMap<String, Double> aggPairs = new HashMap<>();
            Integer n = 0;
            for (Pair p : pairs) {
                String newKey = p.getKey().substring(0, keysAggLength);
                try {
                    Double newValue = Double.valueOf(p.getValue().trim());
                    if (aggPairs.containsKey(newKey)) {
                        n += 1;
                        newValue = (newValue + aggPairs.get(newKey) * n) / (n + 1);
                    } else {
                        n = 0;
                    }
                    aggPairs.put(newKey, newValue);
                } catch (NumberFormatException e) {
                    // non double value found.
                }
            }

            // convert to ArrayList<Pair>
            pairs.clear();
            for (Map.Entry<String, Double> e: aggPairs.entrySet()){
                pairs.add(new Pair(e.getKey(), String.valueOf(e.getValue())));
            }
        }



        // convert unit
        if (tempUnit.equals("celsius")){
            pairs = Utils.kelvinToCelsius(pairs);
        }

        return pairs;

    }

    /**
     * permet de filtrer les ligne d'une table par rapport a une colonne donnée
     * @param colName le nom de la colonne
     * @param value la valeur par rapport à laquelle on veut fitrer
     * @param partialMatch boolean qui premet d'avoir une valeure partielle
     * @return une table filtrer
     */
    public Table filterRows(String colName, String value, boolean partialMatch){
        ArrayList<Row> filteredData = new ArrayList<>();
        Integer colIndex = this.colIndex(colName);
        if (colIndex == -1) {
            this.data = filteredData;
            return this;
        }
        for (int i=0; i < this.data.size(); i++){
            Row row = this.data.get(i);
            if (!partialMatch && row.get(colIndex).equals(value)) {
                filteredData.add(row);
            }

            if (partialMatch && row.get(colIndex).contains(value)){
                filteredData.add(row);
            }
        }
        this.data = filteredData;
        return this;
    }

    public Table agg(String aggMode){
        return this;
    }

    public ArrayList<Pair> firstCol(){
        return new ArrayList<Pair>();
    }

    public Integer size() {
        return this.data.size();
    }

    public Integer colIndex(String colName){
        return this.header.indexOf(colName);
    }

    public String cell(Integer x, Integer y){
        return this.getData().get(y).get(x);
    }

    public Row row(Integer y){
        return this.getData().get(y);
    }

    /**
     * retourne un arrayListe de string contenant des colonnes
     */
    public ArrayList<String> col(Integer x){
        ArrayList<String> col = new ArrayList<String>();
        for (Row row:  this.getData()){
            col.add(row.get(x));
        }
        return col;
    }

    /**
     * permet de filtrer une table en mettant dans
     * une array liste le nom des colonnes à garder
     */
    public Table filterCols(ArrayList<String> cols) {
        ArrayList<Row> result = new ArrayList<>();

        for (Row row: this.data) {
            ArrayList<String> filteredRow = new ArrayList<>();

            for (String col : cols) {
                Integer colIndex = this.colIndex(col);
                filteredRow.add(row.get(colIndex));
            }
            result.add(new Row(filteredRow));
        }
        this.data = result;
        return this;
    }
}
