package source;


import dao.Row;
import dao.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CsvParser {

    public CsvParser(){

    }

    String filename;
    Integer nCols = 0;
    Integer nRows = 0;
    ArrayList<String> header;
    String sep = ";";
    String del = "\"";
    Table data;

    /**
     * Création d'un objet pretique pour la manupulation du fichier csv.
     * @param csvFile le nom du fichier csv
     * @param sep le séparateur du fichier csv, généralement c'est une vergule (,).
     * @param del le délimiteur du fichier csv, généralement c'est un double quote (").
     * @param hasHeader boolean qui est mis à vrais si la ligne est l'entête du fichier.
     *
     */
    public CsvParser(String csvFile, String sep, String del, boolean hasHeader) {
        this.filename = csvFile;
        this.sep = sep;
        this.del = del;
        this.header = new ArrayList<String>();
        this.data = new Table();

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
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

        this.data.header = this.header;

    }

    /**
     * Permet de récupérer le nom du fichier csv passer au constructeur.
     * @return un string qui contient le nom du fichier csv.
     */
    public String getFilename(){ return this.filename; }


    /**
     * Permet de récupérer le numéro de la colonne actuelle du fichier csv.
     * @return un entier qui contient le numéro de la colonne actuelle.
     */
    public Integer getNCols(){ return this.nCols; }


    /**
     * Permet de récupérer le numéro de la ligne actuelle du fichier csv.
     * @return un entier qui contient le numéro de la ligne actuelle.
     */
    public Integer getNRows(){ return this.nRows; }


    /**
     * Permet de récupérer l'entête du fichier s'il existe.
     * @return un tableau de strings, chaque élément du tableau est le nom d'une colonne.
     */
    public ArrayList<String> getHeader(){ return this.header; }


    /**
     * Permet de récupérer le contenu du fichier csv (les données sans l'entête) dans un format adéquat.
     * @return tableau à deux dimentions de string (ligne, index).
     */
    public ArrayList<Row> getData(){ return this.data.getData(); }


    /**
     * Permet de d'obtenir les données csv sous forme de table
     * @return instance de la table contenant les données csv
     */
    public Table getTable(){ return this.data; }


    /**
     * Permet d'obtenez l'index d'une colonne en fonction de l'en-tête.
     * @param colName le nom de la colonne
     * @return L'index de la colonne si trouvé, -1 sinon.
     */
    public Integer colIndex(String colName){
        return this.getHeader().indexOf(colName);
    }


    /**
     * permet de récupérer la cellule de l'intersection de la colonne x et la ligne y.
     * @param x index de la colonne (commence de 0)
     * @param y index de la ligne (commence de 0, l'entête pas inclut)
     * @return une cellule csv contenant un string.
     */
    public String cell(Integer x, Integer y){
        return this.getData().get(y).get(x);
    }


    /**
     * Permet de récupérer une ligne avec l'index y.
     * @param y index de la ligne (commence de 0, l'entête pas inclut)
     * @return un tableau de string contenant les cellules de la ligne
     */
    public Row row(Integer y){
        return this.getData().get(y);
    }


    /**
     * Permet de récupérer une clonne avec l'index x.
     * @param x index de la colonne (commence de 0).
     * @return un tableau de string contenant les cellules de la colonne.
     */
    public ArrayList<String> col(Integer x){
        ArrayList<String> col = new ArrayList<String>();
        for (Row row:  this.getData()){
            col.add(row.get(x));
        }
        return col;
    }

}
