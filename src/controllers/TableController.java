package controllers;


import dao.Row;
import dao.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class TableController {

    @FXML
    TableView table;

    private final TableColumn<Row, String> date = new TableColumn("Date/Heure");
    private final TableColumn<Row, String> temp = new TableColumn("Température");
    private final TableColumn<Row, String> humi = new TableColumn("Humidité");
    private final TableColumn<Row, String> nebu = new TableColumn("Nebulosité totale");

    /**
     * Permet d'afficher un tableau dans l'interface,contenant les 4 colonnes ( date, températeure, Humidité et Nebulosité).
     * @param tbl qui est de type Table.
     * @return
     */
    public void display(Table tbl) {

        this.table.getColumns().clear();
        this.table.getColumns().addAll(date, temp, humi, nebu);

        date.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        temp.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        humi.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        nebu.prefWidthProperty().bind(table.widthProperty().multiply(0.25));

        date.setResizable(false);
        temp.setResizable(false);
        humi.setResizable(false);
        nebu.setResizable(false);

        date.setCellValueFactory(cellData -> cellData.getValue().firstColValue());
        temp.setCellValueFactory(cellData -> cellData.getValue().secondColValue());
        humi.setCellValueFactory(cellData -> cellData.getValue().thirdColValue());
        nebu.setCellValueFactory(cellData -> cellData.getValue().fourthColValue());

        ObservableList<Row> obsTable = FXCollections.observableArrayList(tbl.getData());

        this.table.setItems(obsTable);

    }
}
