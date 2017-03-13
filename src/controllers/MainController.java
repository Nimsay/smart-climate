package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleButton;
import parser.CSV;

import parser.MeteoFrance;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Thunderstorm on 19/02/17.
 */




public class MainController {



    @FXML
    LineChart<String, Number> lineChart;
    @FXML
    CheckBox compareCheckbox;
    @FXML
    ChoiceBox year1, month1, day1;
    @FXML
    ChoiceBox year2, month2, day2;
    @FXML ChoiceBox aggMode;
    @FXML ToggleButton yearMode, monthMode, dayMode;

    private ObservableList<String> yearsOList;
    private ArrayList<String>  yearsList,monthsList;
    private Calendar cal;
    private MeteoFrance meteoFrance ;
    final private Integer METEO_FRANCE_MIN_YEAR = 1996;

    public MainController() throws IOException {

        this.meteoFrance = new MeteoFrance();

        //init year list based on actual date
        yearsList = new ArrayList<>();
        cal = Calendar.getInstance();
        for (int year = cal.get(Calendar.YEAR); year >= METEO_FRANCE_MIN_YEAR; year--){
            yearsList.add(String.valueOf(year));
        }
        yearsOList = FXCollections.observableArrayList(yearsList);

    }
        @FXML
    void initialize() {
        // populate
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        ArrayList<String> availableYears = new ArrayList<>();
        for (int year = currentYear; year >= METEO_FRANCE_MIN_YEAR; year--) {
            availableYears.add(String.valueOf(year));
        }

        ObservableList<String> yearsList = FXCollections.observableArrayList(availableYears);
        year1.setItems(yearsList);
        year1.getSelectionModel().selectFirst();
        year2.setItems(yearsList);
        year2.getSelectionModel().selectFirst();


        aggMode.setItems(FXCollections.observableArrayList("Moyenne", "MinMax", "Ecart Type"));
        aggMode.getSelectionModel().selectFirst();

    }

    @FXML
    public void onCompare(ActionEvent event) {
        if (compareCheckbox.isSelected()) {
            year2.setDisable(false);

            if (dayMode.isSelected() || monthMode.isSelected()) {
                month2.setDisable(false);
            }
            if (dayMode.isSelected()) {
                day2.setDisable(false);
            }
        } else {
            year2.setDisable(true);
            month2.setDisable(true);
            day2.setDisable(true);
        }
    }

    @FXML
    public void onKelvinSelected() {
        System.out.println("Kelvin Selected");
    }

    @FXML
    public void onCelsiusSelected() {
        System.out.println("Celsius Selected");
    }

    @FXML
    public void onYearMode(ActionEvent event) {
        year1.setDisable(false);
        month1.setDisable(true);
        day1.setDisable(true);
        if (compareCheckbox.isSelected()) {
            year2.setDisable(false);
            month2.setDisable(true);
            day2.setDisable(true);
        }
    }

    @FXML
    public void onMonthMode(ActionEvent event) {
        year1.setDisable(false);
        month1.setDisable(false);
        day1.setDisable(true);
        if (compareCheckbox.isSelected()) {
            month2.setDisable(false);
            year2.setDisable(false);
            day2.setDisable(true);
        }
    }

    @FXML
    public void onDayMode(ActionEvent event) {
        year1.setDisable(false);
        month1.setDisable(false);
        day1.setDisable(false);
        if (compareCheckbox.isSelected()) {
            year2.setDisable(false);
            month2.setDisable(false);
            day2.setDisable(false);
        }
    }

}







