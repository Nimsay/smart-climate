package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yasmin Ait Maksène
 */
public class CSV {

    String filename;
    Integer nCols = 0;
    Integer nRows = 0;
    ArrayList<String> header;
    String sep = ";";
    String del = "\"";
    ArrayList<ArrayList<String>> data;

    public CSV(String csvFile, String sep, String del, boolean hasHeader) {
        this.filename = csvFile;
        this.sep = sep;
        this.del = del;
        this.data = new ArrayList<ArrayList<String>>();
        this.header = new ArrayList<String>();

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            Integer lineIndex = 0;
            while ((line = br.readLine()) != null) {

                ArrayList<String> row = new ArrayList<String>(Arrays.asList(line.split(cvsSplitBy)));

                if (lineIndex == 0){
                    this.nCols = row.size();
                    if (hasHeader) {
                        this.header = (ArrayList<String>) row;
                        this.nRows = -1;
                    } else {
                        this.data.add(row);
                    }
                } else {
                    this.data.add(row);
                }
                lineIndex++;
            }

            this.nRows += lineIndex;


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getFilename(){ return this.filename; }
    public Integer getNCols(){ return this.nCols; }
    public Integer getNRows(){ return this.nRows; }
    public ArrayList<String> getHeader(){ return this.header; }
    public ArrayList<ArrayList<String>> getData(){return this.data; }

    public Integer colIndex(String colName){
        return this.getHeader().indexOf(colName);
    }

    public String cell(Integer x, Integer y){
        return this.getData().get(y).get(x);
    }

    public ArrayList<String> row(Integer y){
        return this.getData().get(y);
    }

    public ArrayList<String> col(Integer x){
        ArrayList<String> col = new ArrayList<String>();
        for (ArrayList<String> row:  this.getData()){
            col.add(row.get(x));
        }
        return col;
    }

}
