package application.panels.informative;

import application.panels.plot.PlotPanelController;
import application.util.format.DecimalDisplayFormat;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import simulation.asset.AssetData;

public abstract class AssetInfoPanelController extends InfoPanelController {

    @FXML
    protected Label openingPriceLabel;
    @FXML
    protected Label minimalPriceLabel;
    @FXML
    protected Label maximalPriceLabel;
    @FXML
    protected Button plotButton;

    public void onPlotButtonClicked() {
        this.mainController.getWindowsManager().openNewPlotWindow(
                PlotPanelController.class.getResource("plot_panel.fxml"),
                "Plot",
                simulation,
                this.getAssetID());
    }

    protected abstract String getAssetID();

    protected void updateFields(AssetData assetData) {
        this.nameLabel.setText(assetData.getName());
        var decimal = new DecimalDisplayFormat(6);
        this.openingPriceLabel.setText(decimal.format(assetData.getOpeningPrice()));
        this.minimalPriceLabel.setText(decimal.format(assetData.getMinimalPrice()));
        this.maximalPriceLabel.setText(decimal.format(assetData.getMaximalPrice()));
    }
}
