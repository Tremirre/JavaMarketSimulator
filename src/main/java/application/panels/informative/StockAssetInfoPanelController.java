package application.panels.informative;

import application.panels.plot.PlotPanelController;
import application.util.format.DecimalDisplayFormat;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import simulation.asset.StockData;

public class StockAssetInfoPanelController extends AssetInfoPanelController {
    private StockData assetData;

    @FXML
    private Label revenueLabel;
    @FXML
    private Label profitLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label capitalLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label buildingNumberLabel;
    @FXML
    private ComboBox<String> buyoutComboBox;

    public void initialize() {
        this.buyoutComboBox.getItems().addAll(
                "20%",
                "40%",
                "60%",
                "80%",
                "100%"
        );
        this.buyoutComboBox.setValue("100%");
    }

    @Override
    public void onPlotButtonClicked() {
        this.mainController.getWindowsManager().openNewPlotWindow(
                PlotPanelController.class.getResource("stock_plot_panel.fxml"),
                "Plot",
                simulation,
                this.getAssetID());
    }

    public void onBuyoutButtonClicked() {
        String percentage = this.buyoutComboBox.getValue();
        double value = ((double) Integer.parseInt(percentage.substring(0, percentage.length() - 1)))/100;
        this.assetData.getCompany().buyout(value);
    }

    @Override
    public void passID(String assetUID) {
        this.assetData = (StockData) this.simulation.getAssetManager().getAssetData(assetUID);
        this.updateFields(assetData);
    }

    protected void updateFields(StockData assetData) {
        super.updateFields(assetData);
        this.nameLabel.setText(assetData.getCompany().getCompanyName());
        var company = assetData.getCompany();
        var decimal = new DecimalDisplayFormat(3);
        this.revenueLabel.setText(decimal.format(company.getRevenue()));
        this.profitLabel.setText(decimal.format(company.getProfit()));
        this.capitalLabel.setText(decimal.format(company.getCapital()));
        this.dateLabel.setText(company.getIPODate());
        var address = company.getAddress();
        this.countryLabel.setText(address.getCountry());
        this.cityLabel.setText(address.getCity());
        this.postalCodeLabel.setText(address.getPostalCode());
        this.streetLabel.setText(address.getStreetName());
        this.buildingNumberLabel.setText(String.valueOf(address.getBuildingNumber()));
    }

    @Override
    public void refresh() {
        this.passID(this.assetData.getUniqueIdentifyingName());
    }

    @Override
    protected String getAssetID() {
        return this.assetData.getUniqueIdentifyingName();
    }
}
