package application.panels.plot;

import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import simulation.core.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiAssetPanelController extends PlotPanelController {
    @FXML
    private VBox assetVBox;
    private final Map<String, AssetRecord> records = new HashMap<>();
    private final ArrayList<String> plottedAssets = new ArrayList<>();

    protected XYChart.Series<Number, Number> createLine(String asset) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        var priceHistory = new ArrayList<>(this.simulation.getAssetManager().getAssetData(asset).getPriceHistory());
        double initialValue = priceHistory.get(0);
        int day = this.simulation.getSimulationDay();
        int initialDay = day;
        for (int i = priceHistory.size() - 1; i >= 0 && day >= initialDay - this.period; i--) {
            series.getData().add(new XYChart.Data<>(day--, priceHistory.get(i)/initialValue));
        }
        this.mainPlot.getData().add(series);
        return series;
    }

    @Override
    public void onTimeSliderDragDone() {
        int value = (int) Math.floor(this.timeSlider.getValue());
        switch(value) {
            case 0 -> this.period = 365;
            case 1 -> this.period = 2 * 365;
            case 2 -> this.period = 5 * 365;
            default -> this.period = 10 * 365;
        }
        this.mainPlot.getData().clear();
        for (var asset : this.plottedAssets) {
            var series = this.createLine(asset);
            series.setName(asset);
        }   this.refresh();
    }

    @Override
    public void updateChart() {
        int day = this.simulation.getSimulationDay();
        this.xAxis.setLowerBound(Math.max(day - this.period, 0));
        this.xAxis.setUpperBound(Math.max(day, this.period));
        for (int i = 0; i < this.plottedAssets.size(); i++) {
            var priceHistory = this.simulation.getAssetManager().getAssetData(this.plottedAssets.get(i)).getPriceHistory();
            var series = this.mainPlot.getData().get(i).getData();
            double initialValue = priceHistory.get(0);
            double newPrice = priceHistory.get(priceHistory.size() - 1)/initialValue;
            series.add(new XYChart.Data<>(day, newPrice));
            for (int j = series.size() - 1; j >= 0; j--) {
                if(series.get(j).getXValue().doubleValue() < this.xAxis.getLowerBound())
                    series.remove(j);
            }
        }
    }

    @Override
    public void passSimulationReference(Simulation simulation) {
        super.passSimulationReference(simulation);
        var allAssetsPrices = simulation.getAssetManager().getAssetOpeningPrices();
        for (var entry : allAssetsPrices.entrySet()) {
            var record = new AssetRecord(entry.getKey(), entry.getValue(), this);
            this.records.put(entry.getKey(), record);
            this.assetVBox.getChildren().add(record);
        }
    }

    @Override
    public void refresh() {
        var allAssetsPrices = simulation.getAssetManager().getAssetOpeningPrices();
        for (var entry : allAssetsPrices.entrySet()) {
            if (!this.records.containsKey(entry.getKey())) {
                var record = new AssetRecord(entry.getKey(), entry.getValue(), this);
                this.records.put(entry.getKey(), record);
                this.assetVBox.getChildren().add(record);
            } else {
                this.records.get(entry.getKey()).updatePrice(entry.getValue());
            }
        }
        for (var entry : this.records.entrySet()) {
            if (!allAssetsPrices.containsKey(entry.getKey())) {
                this.assetVBox.getChildren().remove(entry.getValue());
                if (this.plottedAssets.contains(entry.getKey())) {
                    this.removeAssetFromPlot(entry.getKey());
                }
            }
        }
        this.updateChart();
    }

    public void plotAsset(String asset) {
        if (this.plottedAssets.contains(asset))
            return;
        this.plottedAssets.add(asset);
        var series = this.createLine(asset);
        series.setName(asset);
    }

    public void removeAssetFromPlot(String asset) {
        if (!this.plottedAssets.contains(asset))
            return;
        int seriesIndex = this.plottedAssets.indexOf(asset);
        this.plottedAssets.remove(seriesIndex);
        this.mainPlot.getData().remove(seriesIndex);
    }
}
