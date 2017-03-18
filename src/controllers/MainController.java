package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
//import javafx.util.Pair;
import parser.MeteoFrance;
import utils.Pair;
import parser.CSV;
import parser.Downloader;
import java.io.IOException;
import java.util.*;


public class MainController {
    @FXML
    AreaChart<String, Number> areaChart;
    @FXML private CheckBox compareCheckbox;
    @FXML private ChoiceBox<String> year1, month1, day1, year2, month2, day2;
    @FXML private ChoiceBox<Pair> aggMode;
    @FXML private ChoiceBox<Pair> stations, columnChoice;
    @FXML private ToggleButton yearMode, monthMode, dayMode;
    @FXML private ToggleButton celsiusMode, kelvinMode;
    @FXML private ToggleGroup tempUnitToggleGroup, modeToggleGroup;
    @FXML NumberAxis xAxis;
    @FXML NumberAxis yAxis;

    final private Integer METEO_FRANCE_MIN_YEAR = 1996;

    private ArrayList<String> yearsList, monthsList, days1List, days2List;
    private ObservableList<String> yearsOList, monthsOList, days1OList, days2OList;
    private ArrayList<Pair> aggList;
    private ObservableList<Pair> aggOList;
    private Calendar cal;

    private ArrayList<String> stationIds;
    private ArrayList<Pair> stationsName, columnsList;
    private ObservableList<Pair> columnsOlist;

    private MeteoFrance meteoFrance;
    private PlotsController plot;

    public MainController() throws IOException {

        this.meteoFrance = new MeteoFrance();


        // init yearlist, month list, daylist based on actual date
        yearsList = new ArrayList<>();
        cal = Calendar.getInstance();
        for (int year = cal.get(Calendar.YEAR); year >= METEO_FRANCE_MIN_YEAR; year--){
            yearsList.add(String.valueOf(year));
        }
        yearsOList = FXCollections.observableArrayList(yearsList);

        monthsList= genMonths(cal.get(Calendar.YEAR));
        monthsOList = FXCollections.observableArrayList(monthsList);

        days1List = genDays(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        days1OList = FXCollections.observableArrayList(days1List);

        days2List = genDays(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        days2OList = FXCollections.observableArrayList(days2List);

        aggList = new ArrayList<>();
        aggList.add(new Pair("mean", "Moyenne"));
        // aggList.add(new Pair("minmax", "MinMax"));
        // aggList.add(new Pair("std", "Ecart Type"));
        aggOList = FXCollections.observableArrayList(aggList);

        columnsList = new ArrayList<Pair>();
        columnsList.add(new Pair("t", "Température"));
        columnsList.add(new Pair("u", "Humidité"));
        columnsList.add(new Pair("n", "Nebulosité totale"));
        columnsOlist = FXCollections.observableArrayList(columnsList);

        // Download stations list if not in cache
        Downloader downloader = new Downloader();
        String stationsUrl = "https://donneespubliques.meteofrance.fr/donnees_libres/Txt/Synop/postesSynop.csv";
        String stationFile = downloader.download(stationsUrl);

        CSV csv = new CSV(stationFile, ";", "\"", true, false);
        stationsName = new ArrayList<>();
        for (int i=0; i < csv.col(0).size(); i++){
            stationsName.add(new Pair(csv.col(0).get(i), csv.col(1).get(i) ));
        }
    }

    /**
     * Generate list of valid months
     * @param year The year you want to generate the months for.
     * @return An ArrayList of months from "Janvier" to "Décembre". of if year is the actual year,
     * Return only months till the actual month.
     */
    private ArrayList<String> genMonths(Integer year) {
        ArrayList<String> months = new ArrayList<>();
        Integer maxMonths = (cal.get(cal.YEAR) == year) ? cal.get(cal.MONTH) : 11;
        for ( int i=0; i <= maxMonths; i++){
            months.add(
                Arrays.asList(
                    "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                    "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
                ).get(i)
            );
        }
        return months;
    }

    private ArrayList<String> genDays(String year, String month){
        return genDays(Integer.parseInt(year), Integer.parseInt(month));
    }

    /**
     * Generate list of days
     * @param year The year you want to generate the months for
     *@param month the month you want to generate the days for
     *@return arraylist of days
     */
        private ArrayList<String> genDays(Integer year, Integer month){
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        Integer maxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (cal.get(cal.YEAR) == year && cal.get(cal.MONTH) == month){
            maxDays = cal.get(cal.DAY_OF_MONTH);
        }
        ArrayList<String> days = new ArrayList<String>();
        for (int i=1; i <= maxDays; i++){
            days.add(String.format("%02d", i));
        }
        return days;
    }

    private ObservableList<String> genODays(String year, String month){
        return  FXCollections.observableArrayList(genDays(year, month));
    }

    private ObservableList<String> genODays(Integer year, Integer month){
        return  FXCollections.observableArrayList(genDays(year, month));
    }

    @FXML void initialize() throws IOException {

        /*
        * Init
        * */

        this.plot = new PlotsController(this.areaChart);

        yearMode.setUserData("yearMode");
        monthMode.setUserData("monthMode");
        dayMode.setUserData("dayMode");


        /*
        * Populate choice boxes
        * */

        // stations
        ObservableList<Pair> stationsList = FXCollections.observableArrayList(stationsName);
        stations.setItems(stationsList);
        stations.getSelectionModel().select(0);

        // year1
        year1.setItems(yearsOList);
        year1.getSelectionModel().select(0);

        // year2
        year2.setItems(yearsOList);
        year2.getSelectionModel().select(0);

        // months1
        month1.setItems(monthsOList);
        month1.getSelectionModel().select(cal.get(cal.MONTH));

        // months2
        month2.setItems(monthsOList);
        month2.getSelectionModel().select(cal.get(cal.MONTH));

        day1.setItems(days1OList);
        day1.getSelectionModel().select(cal.get(cal.DAY_OF_MONTH)-1);

        day2.setItems(days2OList);
        day2.getSelectionModel().select(cal.get(cal.DAY_OF_MONTH)-1);

        aggMode.setItems( aggOList );
        aggMode.getSelectionModel().selectFirst();

        columnChoice.setItems( columnsOlist );
        columnChoice.getSelectionModel().selectFirst();




      /*
        *   Listeners
        * */


        year1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldIndex, Number newIndex) {
            updateDay(newIndex.intValue(), month1.getSelectionModel().getSelectedIndex(),
                    day1.getSelectionModel().getSelectedIndex(), 1 );
            updateMonth(newIndex.intValue(), month1.getSelectionModel().getSelectedIndex(), 1);

        }
    });

        year2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldIndex, Number newIndex) {
            updateDay(newIndex.intValue(), month2.getSelectionModel().getSelectedIndex(),
                    day2.getSelectionModel().getSelectedIndex(), 2 );
            updateMonth(newIndex.intValue(), month2.getSelectionModel().getSelectedIndex(), 2);
        }
    });

        month1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldIndex, Number newIndex) {
            updateDay(year1.getSelectionModel().getSelectedIndex(), newIndex.intValue(),
                    day1.getSelectionModel().getSelectedIndex(), 1 );
        }
    });

        month2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldIndex, Number newIndex) {
            updateDay(year2.getSelectionModel().getSelectedIndex(), newIndex.intValue(),
                    day2.getSelectionModel().getSelectedIndex(), 2 );
        }
    });

    }

    private void updateDay(int yearIndex, int monthIndex, int dayIndex, int dayChoiceBoxIndex){
        ArrayList<String> daysList = genDays(yearsList.get(yearIndex), String.valueOf(monthIndex));
        dayIndex = dayIndex<=daysList.size()-1 ? dayIndex : daysList.size()-1;
        ObservableList<String> daysOList = FXCollections.observableArrayList(daysList);
        if (dayChoiceBoxIndex==1) {
            day1.setItems(daysOList);
            day1.getSelectionModel().select(dayIndex);
        } else {
            day2.setItems(daysOList);
            day2.getSelectionModel().select(dayIndex);
        }
    }

    private void updateMonth(int yearIndex, int monthIndex, int yearChoiceBoxIndex){
        ArrayList<String> monthsList = genMonths(Integer.parseInt(yearsList.get(yearIndex)));
        monthIndex = monthIndex<=monthsList.size()-1 ? monthIndex : monthsList.size()-1;
        if (yearChoiceBoxIndex==1) {
            month1.setItems(FXCollections.observableArrayList(monthsList));
            month1.getSelectionModel().select(monthIndex);
        } else {
            month2.setItems(FXCollections.observableArrayList(monthsList));
            month2.getSelectionModel().select(monthIndex);
        }
    }


    @FXML public void applyFilters(ActionEvent event) throws IOException {

        /*
         * get the actual configuration
         */

        String date1 = yearsList.get(year1.getSelectionModel().getSelectedIndex());
        String date2 = yearsList.get(year2.getSelectionModel().getSelectedIndex());

        String currentMode = modeToggleGroup.getSelectedToggle().getUserData().toString();
        if (currentMode.equals("monthMode") || currentMode.equals("dayMode")) {
            date1 += String.format("%02d", month1.getSelectionModel().getSelectedIndex() + 1); // +1 because it's 0-based
            date2 += String.format("%02d", month2.getSelectionModel().getSelectedIndex() + 1);
        }
        if(currentMode.equals("dayMode")){
            date1 += days1List.get(day1.getSelectionModel().getSelectedIndex());
            date2 += days1List.get(day2.getSelectionModel().getSelectedIndex());
        }

        String stationId = (String) stations.getSelectionModel().getSelectedItem().getKey();
        String col = columnChoice.getSelectionModel().getSelectedItem().getKey();



        ArrayList<Pair> dataPair1 = this.meteoFrance.get(date1, col, stationId);
        ArrayList<Pair> dataPair2 = this.meteoFrance.get(date2, col, stationId);

        String currentAggMode = aggMode.getSelectionModel().getSelectedItem().getKey();
        this.plot.draw(compareCheckbox.isSelected(), dataPair1, dataPair2,
                date1, date2, stationId, col, currentMode, currentAggMode);
        // System.out.println();

    }
        // Curves comparision
        @FXML
        public void onCompare (ActionEvent event){
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
        public void onKelvinSelected () {
            System.out.println("Kelvin Selected");
        }

        @FXML
        public void onCelsiusSelected () {
            System.out.println("Celsius Selected");
        }

        @FXML
        public void onYearMode (ActionEvent event){
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
        public void onMonthMode (ActionEvent event){
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
        public void onDayMode (ActionEvent event){
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






