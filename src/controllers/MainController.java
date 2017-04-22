package controllers;

import dao.Table;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import source.CsvParser;
import source.meteofrance.MeteoFrance;
import utils.Pair;
import source.Downloader;
import java.io.IOException;
import java.util.*;


public class MainController {

    final String LAYOUT_ABOUT = "../views/AboutLayout.fxml";
    final String LAYOUT_PLOTS = "../views/PlotsLayout.fxml";
    final String LAYOUT_TABLE = "../views/TableLayout.fxml";

    @FXML private AreaChart<String, Number> areaChart;
    @FXML private ToggleButton aboutBtn;
    @FXML private Pane contentPane;
    @FXML private CheckBox compareCheckbox;
    @FXML private ChoiceBox<String> year1, month1, day1, year2, month2, day2;
    @FXML private ChoiceBox<Pair> aggMode;
    @FXML private ChoiceBox<Pair> stations, columnChoice;
    @FXML private ToggleButton yearMode, monthMode, dayMode;
    @FXML private ToggleButton celsiusMode, kelvinMode;
    @FXML private ToggleGroup tempUnitToggleGroup, modeToggleGroup, displayToggleGroup;
    @FXML private ToggleButton graphMode, tableMode;

    private FXMLLoader fxmlLoader;
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
    private AboutController aboutController;
    private PlotsController plotsController;
    private TableController tableController;

    public MainController() throws IOException {

        this.aboutController = new AboutController();
        this.meteoFrance = new MeteoFrance();
        this.plotsController = new PlotsController();
        this.tableController = new TableController();
        this.fxmlLoader = new FXMLLoader();

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
        stationsName = this.meteoFrance.uiLists.getStationsList();

    }

    /**
     * Genere une liste de mois valide.
     * @param year l'année pour laquelle on veut génerer des mois .
     * @return un ArrayList de mois en partant de "Janvier" à "Décembre". Si l'année est l'année actuelle,
     * Retourne seuelemnt les mois jusqu'à la date actuelle.
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
     * Genere une liste de jours.
     * @param year l'année du mois pour lequel on veut generer la liste des jours.
     * @param month le mois pour lequel on veut generer les jours.
     * @return Un ArrayList de jours.
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
        * Default view is Plot View
        * */
        switchView(LAYOUT_PLOTS, this.plotsController);


        /*
        * Init
        * */

        yearMode.setUserData("yearMode");
        monthMode.setUserData("monthMode");
        dayMode.setUserData("dayMode");

        celsiusMode.setUserData("celsius");
        kelvinMode.setUserData("kelvin");

        graphMode.setUserData("graphMode");
        tableMode.setUserData("tableMode");

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

    private void updateMonth(Integer yearIndex, Integer monthIndex, Integer yearChoiceBoxIndex){
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

    /**
     * Methode de l'interface permettant de recuperer la configuratuion de l'utilisateur pour qu'on puisse afficher les donnees souhaitees.
     */
    @FXML public void applyFilters(ActionEvent event) throws IOException, InterruptedException {
        /*
        * If we are in AboutView, get back to PlotsView
        * */

        this.aboutBtn.setSelected(false);

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

        String currentAggMode = aggMode.getSelectionModel().getSelectedItem().getKey();

        String tempUnit = tempUnitToggleGroup.getSelectedToggle().getUserData().toString();
        String displayMode = displayToggleGroup.getSelectedToggle().getUserData().toString();

        /*
         * Get data based on current configuration
         */

        if (displayMode.equals("graphMode")){
            switchView(LAYOUT_PLOTS, this.plotsController);

            ArrayList<Pair> dataPair1 = this.meteoFrance.get(date1, col, stationId, currentAggMode, tempUnit);

            ArrayList<Pair> dataPair2 = new ArrayList<>();
            if (compareCheckbox.isSelected()){
                dataPair2 = this.meteoFrance.get(date2, col, stationId, currentAggMode, tempUnit);
            }
            this.plotsController.draw(compareCheckbox.isSelected(), dataPair1, dataPair2,
                currentMode);
        } else {
            switchView(LAYOUT_TABLE, this.tableController);

            ArrayList<String> cols = new ArrayList<>();
            cols.add("date");
            cols.add("t");
            cols.add("u");
            cols.add("n");

            Table table = this.meteoFrance.getRaw(date1, cols, stationId);

            // Renomer le header
            ArrayList<String> header = new ArrayList<>();
            header.add("Date & Heure");
            header.add("Température");
            header.add("Humidité");
            header.add("Nebulosité totale");
            table.header = header;

            this.tableController.display(table);
        }
    }

    @FXML public void aboutBtnClicked(ActionEvent event) throws IOException {
        ToggleButton aboutToggleBtn = (ToggleButton) event.getSource();

        if (aboutToggleBtn.isSelected()) {
            switchView(LAYOUT_ABOUT, this.aboutController);
        } else {
            switchView(LAYOUT_PLOTS, this.plotsController);
        }
    }

    /**
     * Permet de charger un nouveau fichier fxml et son controlleur.
     */
    private void switchView(String layout, Object controller) throws IOException {
        this.contentPane.getChildren().clear();
        this.fxmlLoader.setRoot(null);
        this.fxmlLoader.setLocation(getClass().getResource(layout));
        this.fxmlLoader.setController(controller);
        this.contentPane.getChildren().add(this.fxmlLoader.load());
    }

    /**
     *C'est methode permettant d'activer ou desactiver les controls.
     */
    @FXML public void onCompare(ActionEvent event){
        if (compareCheckbox.isSelected()){
            year2.setDisable(false);

            if ( dayMode.isSelected() || monthMode.isSelected() ){
                month2.setDisable(false);
            }
            if (dayMode.isSelected()){
                day2.setDisable(false);
            }
        } else {
            year2.setDisable(true);
            month2.setDisable(true);
            day2.setDisable(true);
        }
    }

    @FXML public void onYearMode(ActionEvent event){
        year1.setDisable(false);
        month1.setDisable(true);
        day1.setDisable(true);
        if (compareCheckbox.isSelected()) {
            year2.setDisable(false);
            month2.setDisable(true);
            day2.setDisable(true);
        }
    }

    @FXML public void onMonthMode(ActionEvent event){
        year1.setDisable(false);
        month1.setDisable(false);
        day1.setDisable(true);
        if (compareCheckbox.isSelected()) {
            month2.setDisable(false);
            year2.setDisable(false);
            day2.setDisable(true);
        }
    }

    @FXML public void onDayMode(ActionEvent event){
        year1.setDisable(false);
        month1.setDisable(false);
        day1.setDisable(false);
        if (compareCheckbox.isSelected()) {
            year2.setDisable(false);
            month2.setDisable(false);
            day2.setDisable(false);
        }
    }

    @FXML public void onGraphModeSelected(ActionEvent event) throws IOException {
        switchView(LAYOUT_PLOTS, this.plotsController);
        aggMode.setDisable(false);
        columnChoice.setDisable(false);
    }

    @FXML public void onTableModeSelected(ActionEvent event) throws IOException {
        compareCheckbox.setSelected(false);
        aggMode.setDisable(true);
        columnChoice.setDisable(true);
        onCompare(new ActionEvent());
        switchView(LAYOUT_TABLE, this.tableController);
    }




}

