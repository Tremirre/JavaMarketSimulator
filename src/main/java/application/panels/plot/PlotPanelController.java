package application.panels.plot;

import application.panels.ReferencingController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;

import java.util.ArrayList;

public class PlotPanelController extends ReferencingController {

    private String assetID;
    private int period = 365;
    private int lastSliderTick = 0;
    @FXML
    private LineChart<Number, Number> mainPlot;
    @FXML
    private Slider timeSlider;

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
            case 0 -> this.period = 365;
            case 1 -> this.period = 2 * 365;
            case 2 -> this.period = 5 * 365;
            default -> this.period = 10 * 365;
        }
        this.mainPlot.getData().clear();
        this.updateLine();
    }

    public void passAssetID(String assetID) {
        this.assetID = assetID;
        this.mainPlot.setTitle(
                this.simulation.getAssetManager().getAssetData(assetID).getName() + " Prices Plot"
        );
        this.refresh();
    }

    private void createLine() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        var priceHistory = new ArrayList<>(this.simulation.getAssetManager().getAssetData(assetID).getPriceHistory());
        int day = this.simulation.getSimulationDay();
        int initial = day;
        for (int i = priceHistory.size() - 1; i >= 0 && day >= initial - this.period; i--) {
            series.getData().add(new XYChart.Data<>(day--, priceHistory.get(i)));
        }
        this.mainPlot.getData().add(series);
    }

    private void updateLine() {
        if (this.mainPlot.getData().size() == 0) {
            this.createLine();
        } else {
            var series = this.mainPlot.getData().get(0).getData();
            var newPrice = this.simulation.getAssetManager().getAssetData(assetID).getOpeningPrice();
            series.add(new XYChart.Data<>(this.simulation.getSimulationDay(), newPrice));
            while (series.size() > 365)
                series.remove(0);
        }
    }

    @Override
    public void refresh() {
        this.updateLine();
    }
}
