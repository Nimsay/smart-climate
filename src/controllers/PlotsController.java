package controllers;

/**
 * Created by hassina on 12/03/2017.
 */
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import utils.Pair;

import java.util.*;

public class PlotsController {

    AreaChart<String,Number> areaChart;

    PlotsController(AreaChart<String, Number> areaChart){
        this.areaChart = areaChart;
    }

    private LinkedHashMap<String, Double> agg(ArrayList<Pair> dataPair, String currentMode){
        // Aggregate
        LinkedHashMap<String, Double> aggData = new LinkedHashMap<>();
        for (Pair p: dataPair){
            try {
                String x = p.getKey().substring(4, 6);
                if (currentMode.equals("monthMode")){
                    x = p.getKey().substring(6, 8);
                } else if (currentMode.equals("dayMode")){
                    x = p.getKey().substring(8, 10) + "h";
                }

                Double y = Double.valueOf(p.getValue());
                if (!aggData.containsKey(x)){
                    aggData.put(x, y);
                } else {
                    // if mean
                    aggData.put(x, (aggData.get(x) + y)/2 );
                }
            } catch (NumberFormatException e){
                System.out.println(p.getKey() + "=" + p.getValue());
            }
        }
        return aggData;
    }

    private XYChart.Series<String, Number> sortedSeries(LinkedHashMap<String, Double> aggData){
        // insert sorted
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        aggData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {
                    series.getData().add(new XYChart.Data<String, Number>(e.getKey(), e.getValue()));
                });
        series.setName("Month Pay");
        return series;
    }

    public void draw(boolean compare, ArrayList<Pair> dataPair1, ArrayList<Pair> dataPair2,
                     String date1, String date2, String stationId, String col, String currentMode,
                     String currentAggMode){


        this.areaChart.getData().clear();

        LinkedHashMap<String, Double> aggData1 = agg(dataPair1, currentMode);
        XYChart.Series<String, Number> serie1 = sortedSeries(aggData1);

        // this.areaChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
        this.areaChart.getData().add(serie1);

        if (compare){
            LinkedHashMap<String, Double> aggData2 = agg(dataPair2, currentMode);
            XYChart.Series<String, Number> serie2 = sortedSeries(aggData2);

            // this.areaChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
            this.areaChart.getData().add(serie2);
        }



    }
}
