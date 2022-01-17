package application.panels.plot;

import application.panels.ReferencingController;
import application.panels.Refreshable;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class PlotPanelController extends ReferencingController implements Refreshable {

    private String assetID;
    @FXML
    private LineChart<Number, Number> mainPlot;

    public void passAssetID(String assetID) {
        this.assetID = assetID;
        this.mainPlot.setTitle(
                this.simulation.getAssetManager().getAssetData(assetID).getName() + " Prices Plot"
        );
        this.refresh();
    }

    private void updateLine() {
        if (this.mainPlot.getData().size() == 0) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            var priceHistory = new ArrayList<>(this.simulation.getAssetManager().getAssetData(assetID).getPriceHistory());
            for (int i = 0; i < priceHistory.size(); i++) {
                series.getData().add(new XYChart.Data<>(i, priceHistory.get(i)));
            }
            this.mainPlot.getData().add(series);
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
