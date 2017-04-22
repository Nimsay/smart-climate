package dao;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;

/**
 * classe qui permet de retourner les 4 colones
 * dont on aura besoin (chaque colnes distincte)
 */

public class Row{

    public ArrayList<String> row;
    public Row(ArrayList<String> row){
        this.row = row;
    }

    public ObservableValue<String> firstColValue(){
        return new SimpleStringProperty(this.row.get(0));
    }

    public ObservableValue<String> secondColValue(){
        return new SimpleStringProperty(this.row.get(1));
    }

    public ObservableValue<String> thirdColValue(){
        return new SimpleStringProperty(this.row.get(2));
    }

    public ObservableValue<String> fourthColValue(){
        return new SimpleStringProperty(this.row.get(3));
    }

    public String get(Integer x) {
        return this.row.get(x);
    }
}
