package application.panels.plot;

import application.panels.ReferencingController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;
import simulation.util.Constants;

import java.util.ArrayList;

public class PlotPanelController extends ReferencingController {

    protected String assetID;
    protected int period = 365;
    protected int lastSliderTick = 0;
    @FXML
    protected LineChart<Number, Number> mainPlot;
    @FXML
    protected Slider timeSlider;
    @FXML
    protected NumberAxis xAxis;

    public void  initialize() {
        this.timeSlider.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double n) {
                if (n < 0.5) return "1Y";
                if (n < 1.5) return "2Y";
                if (n < 2.5) return "5Y";
                return "10Y";
            }

            @Override
            public Double fromString(String s) {
                switch (s) {
                    case "1Y":
                        return 0d;
                    case "2Y":
                        return 1d;
                    case "5Y":
                        return 2d;
                    default:
                        return 3d;
                }
            }
        });
        this.timeSlider.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.intValue() != this.lastSliderTick) {
                        this.lastSliderTick = newValue.intValue();
                        this.onTimeSliderDragDone();
                    }
                }
        );
    }

    public void onTimeSliderDragDone() {
        int value = (int) Math.floor(this.timeSlider.getValue());
        switch(value) {
            case 0 -> this.period = Constants.YEAR;
            case 1 -> this.period = 2 * Constants.YEAR;
            case 2 -> this.period = 5 * Constants.YEAR;
            default -> this.period = 10 * Constants.YEAR;
        }
        this.updateChart();
    }

    public void passAssetID(String assetID) {
        this.assetID = assetID;
        this.mainPlot.setTitle(
                this.simulation.getAssetManager().getAssetData(assetID).getName() + " Prices Plot"
        );
        this.refresh();
    }

    protected XYChart.Series<Number, Number> createLine(String asset) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        var priceHistory = new ArrayList<>(this.simulation.getAssetManager().getAssetData(asset).getPriceHistory());
        int day = this.simulation.getSimulationDay();
        int initialDay = day;
        int maxPeriod = 10 * Constants.YEAR;
        for (int i = priceHistory.size() - 1; i >= 0 && day >= initialDay - maxPeriod; i--) {
            series.getData().add(new XYChart.Data<>(day--, priceHistory.get(i)));
        }
        series.setName("Asset Price");
        this.mainPlot.getData().add(series);
        return series;
    }

    protected void updateChart() {
        int day = this.simulation.getSimulationDay();
        this.xAxis.setLowerBound(Math.max(day - this.period, 0));
        this.xAxis.setUpperBound(Math.max(day, this.period));
        if (this.mainPlot.getData().size() == 0) {
            this.createLine(this.assetID);
        } else {
            var series = this.mainPlot.getData().get(0).getData();
            var newPrice = this.simulation.getAssetManager().getAssetData(assetID).getOpeningPrice();
            series.add(new XYChart.Data<>(day, newPrice));
        }
    }

    @Override
    public void refresh() {
        this.updateChart();
    }
}
