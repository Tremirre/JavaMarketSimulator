package application.panels.plot;

import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import simulation.asset.StockData;
import simulation.holders.Company;
import simulation.util.Constants;

public class StockPlotPanelController extends PlotPanelController {
    @FXML
    private CheckBox volumesCheckBox;
    @FXML
    private CheckBox salesCheckBox;
    private Company company;

    private XYChart.Series<Number, Number> volumesSeries;
    private XYChart.Series<Number, Number> salesSeries;

    public void initialize() {
        super.initialize();
        volumesSeries = new XYChart.Series<>();
        salesSeries = new XYChart.Series<>();
        volumesSeries.setName("Trading volume (in units)");
        salesSeries.setName("Daily sales");
    }

    @Override
    public void updateChart() {
        super.updateChart();
        int currentDay = this.simulation.getSimulationDay();
        if (currentDay > 0) {
            this.volumesSeries.getData().add(new XYChart.Data<>(
                    currentDay,
                    company.getDailyTradingVolumes().get(company.getDailyTradingVolumes().size() - 1)
            ));
            this.salesSeries.getData().add(new XYChart.Data<>(
                    currentDay,
                    company.getDailyTotalSales().get(company.getDailyTotalSales().size() - 1)
            ));
        }
    }

    @Override
    public void passAssetID(String assetID) {
        this.company = ((StockData) this.simulation.getAssetManager().getAssetData(assetID)).getCompany();
        var volumes = company.getDailyTradingVolumes();
        var sales = company.getDailyTotalSales();
        int currentDay = this.simulation.getSimulationDay();
        for (int i = volumes.size() - 1; i >= Math.max(0, volumes.size() - 10 * Constants.YEAR) ; i--) {
            this.volumesSeries.getData().add(new XYChart.Data<>(currentDay, volumes.get(i)));
            this.salesSeries.getData().add(new XYChart.Data<>(currentDay--, sales.get(i)));
        }
        super.passAssetID(assetID);
    }

    public void onVolumesCheckBoxSelected() {
        if (this.volumesCheckBox.isSelected())
            this.mainPlot.getData().add(this.volumesSeries);
        else
            this.mainPlot.getData().remove(this.volumesSeries);
    }

    public void onSalesCheckBoxSelected() {
        if (this.salesCheckBox.isSelected())
            this.mainPlot.getData().add(this.salesSeries);
        else
            this.mainPlot.getData().remove(this.salesSeries);
    }
}
